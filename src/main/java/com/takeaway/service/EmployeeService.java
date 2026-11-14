package com.takeaway.service;

import com.takeaway.dto.EmployeeDTO;
import com.takeaway.dto.EmployeeLoginDTO;
import com.takeaway.dto.EmployeePageQueryDTO;
import com.takeaway.vo.EmployeeLoginVO;
import com.takeaway.vo.EmployeeVO;
import com.takeaway.vo.PageResult;

/**
 * 员工服务
 */
public interface EmployeeService {

    /** 登录 */
    EmployeeLoginVO login(EmployeeLoginDTO dto);

    /** 新增员工 */
    void save(EmployeeDTO dto);

    /** 编辑员工 */
    void update(EmployeeDTO dto);

    /** 分页查询 */
    PageResult<EmployeeVO> pageQuery(EmployeePageQueryDTO dto);

    /** 根据ID查询 */
    EmployeeVO getById(Long id);

    /** 启停用 */
    void enableOrDisable(Long id, Integer status);
}
