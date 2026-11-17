package com.takeaway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.takeaway.common.context.CurrentUser;
import com.takeaway.common.context.UserContext;
import com.takeaway.common.exception.BaseException;
import com.takeaway.common.result.ResultCode;
import com.takeaway.dto.AddressBookDTO;
import com.takeaway.entity.AddressBook;
import com.takeaway.mapper.AddressBookMapper;
import com.takeaway.service.AddressBookService;
import com.takeaway.vo.AddressBookVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 收货地址服务实现
 */
@Service
@RequiredArgsConstructor
public class AddressBookServiceImpl implements AddressBookService {

    /** 默认地址标识 */
    private static final Integer DEFAULT_FLAG = 1;

    /** 非默认地址标识 */
    private static final Integer NOT_DEFAULT_FLAG = 0;

    private final AddressBookMapper addressBookMapper;

    @Override
    public List<AddressBookVO> list() {
        Long userId = currentUserId();
        List<AddressBook> list = addressBookMapper.selectList(new LambdaQueryWrapper<AddressBook>()
                .eq(AddressBook::getUserId, userId)
                .orderByDesc(AddressBook::getIsDefault)
                .orderByDesc(AddressBook::getUpdateTime));
        return list.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public AddressBookVO getById(Long id) {
        return toVO(getOwned(id, currentUserId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AddressBookDTO dto) {
        Long userId = currentUserId();
        AddressBook addressBook = new AddressBook();
        BeanUtils.copyProperties(dto, addressBook);
        addressBook.setId(null);
        addressBook.setUserId(userId);

        // 首条地址自动设为默认
        Long count = addressBookMapper.selectCount(new LambdaQueryWrapper<AddressBook>()
                .eq(AddressBook::getUserId, userId));
        if (count == null || count == 0) {
            addressBook.setIsDefault(DEFAULT_FLAG);
        } else if (DEFAULT_FLAG.equals(addressBook.getIsDefault())) {
            clearDefault(userId);
        }
        if (addressBook.getIsDefault() == null) {
            addressBook.setIsDefault(NOT_DEFAULT_FLAG);
        }
        addressBookMapper.insert(addressBook);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AddressBookDTO dto) {
        if (dto.getId() == null) {
            throw new BaseException(ResultCode.PARAM_ERROR);
        }
        Long userId = currentUserId();
        getOwned(dto.getId(), userId);

        AddressBook addressBook = new AddressBook();
        BeanUtils.copyProperties(dto, addressBook);
        // 归属用户不允许通过编辑接口修改
        addressBook.setUserId(null);
        if (DEFAULT_FLAG.equals(dto.getIsDefault())) {
            clearDefault(userId);
        }
        addressBookMapper.updateById(addressBook);
    }

    @Override
    public void delete(Long id) {
        getOwned(id, currentUserId());
        addressBookMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long id) {
        Long userId = currentUserId();
        getOwned(id, userId);
        clearDefault(userId);

        AddressBook addressBook = new AddressBook();
        addressBook.setId(id);
        addressBook.setIsDefault(DEFAULT_FLAG);
        addressBookMapper.updateById(addressBook);
    }

    /** 清除该用户其他地址的默认标识 */
    private void clearDefault(Long userId) {
        addressBookMapper.update(null, new LambdaUpdateWrapper<AddressBook>()
                .eq(AddressBook::getUserId, userId)
                .eq(AddressBook::getIsDefault, DEFAULT_FLAG)
                .set(AddressBook::getIsDefault, NOT_DEFAULT_FLAG));
    }

    /** 查询地址并校验归属，防止越权操作他人地址 */
    private AddressBook getOwned(Long id, Long userId) {
        AddressBook addressBook = addressBookMapper.selectById(id);
        if (addressBook == null) {
            throw new BaseException(ResultCode.NOT_FOUND);
        }
        if (!userId.equals(addressBook.getUserId())) {
            throw new BaseException(ResultCode.FORBIDDEN);
        }
        return addressBook;
    }

    private Long currentUserId() {
        CurrentUser user = UserContext.get();
        if (user == null) {
            throw new BaseException(ResultCode.UNAUTHORIZED);
        }
        return user.getId();
    }

    private AddressBookVO toVO(AddressBook addressBook) {
        AddressBookVO vo = new AddressBookVO();
        BeanUtils.copyProperties(addressBook, vo);
        return vo;
    }
}
