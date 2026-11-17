package com.takeaway.controller;

import com.takeaway.common.result.Result;
import com.takeaway.dto.AddressBookDTO;
import com.takeaway.service.AddressBookService;
import com.takeaway.vo.AddressBookVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 收货地址管理（C 端）
 */
@RestController
@RequestMapping("/api/user/address")
@RequiredArgsConstructor
public class AddressBookController {

    private final AddressBookService addressBookService;

    /** 地址列表 */
    @GetMapping("/list")
    public Result<List<AddressBookVO>> list() {
        return Result.success(addressBookService.list());
    }

    /** 根据ID查询 */
    @GetMapping("/{id}")
    public Result<AddressBookVO> getById(@PathVariable Long id) {
        return Result.success(addressBookService.getById(id));
    }

    /** 新增地址 */
    @PostMapping
    public Result<Void> save(@Valid @RequestBody AddressBookDTO dto) {
        addressBookService.save(dto);
        return Result.success();
    }

    /** 编辑地址 */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody AddressBookDTO dto) {
        addressBookService.update(dto);
        return Result.success();
    }

    /** 删除地址 */
    @DeleteMapping
    public Result<Void> delete(@RequestParam Long id) {
        addressBookService.delete(id);
        return Result.success();
    }

    /** 设为默认地址 */
    @PutMapping("/default")
    public Result<Void> setDefault(@RequestParam Long id) {
        addressBookService.setDefault(id);
        return Result.success();
    }
}
