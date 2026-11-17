package com.takeaway.service;

import com.takeaway.dto.AddressBookDTO;
import com.takeaway.vo.AddressBookVO;

import java.util.List;

/**
 * 收货地址服务
 */
public interface AddressBookService {

    /** 查询当前用户的地址列表 */
    List<AddressBookVO> list();

    /** 根据ID查询 */
    AddressBookVO getById(Long id);

    /** 新增地址 */
    void save(AddressBookDTO dto);

    /** 编辑地址 */
    void update(AddressBookDTO dto);

    /** 删除地址 */
    void delete(Long id);

    /** 设为默认地址 */
    void setDefault(Long id);
}
