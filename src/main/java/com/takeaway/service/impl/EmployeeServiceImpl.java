package com.takeaway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeaway.common.constant.StatusConstant;
import com.takeaway.common.exception.BaseException;
import com.takeaway.common.result.ResultCode;
import com.takeaway.dto.EmployeeDTO;
import com.takeaway.dto.EmployeeLoginDTO;
import com.takeaway.dto.EmployeePageQueryDTO;
import com.takeaway.entity.Employee;
import com.takeaway.mapper.EmployeeMapper;
import com.takeaway.service.EmployeeService;
import com.takeaway.util.JwtUtil;
import com.takeaway.vo.EmployeeLoginVO;
import com.takeaway.vo.EmployeeVO;
import com.takeaway.vo.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工服务实现
 */
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    /** 新员工默认密码 */
    private static final String DEFAULT_PASSWORD = "123456";

    /** 新员工默认角色 */
    private static final String DEFAULT_ROLE = "EMPLOYEE";

    private final EmployeeMapper employeeMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public EmployeeLoginVO login(EmployeeLoginDTO dto) {
        Employee employee = employeeMapper.selectOne(
                new LambdaQueryWrapper<Employee>().eq(Employee::getUsername, dto.getUsername()));
        if (employee == null) {
            throw new BaseException("账号不存在");
        }
        if (!passwordEncoder.matches(dto.getPassword(), employee.getPassword())) {
            throw new BaseException("密码错误");
        }
        if (StatusConstant.DISABLE.equals(employee.getStatus())) {
            throw new BaseException("账号已被禁用");
        }
        String token = jwtUtil.createToken(employee.getId(), employee.getUsername(), employee.getRole());
        EmployeeLoginVO vo = new EmployeeLoginVO();
        vo.setId(employee.getId());
        vo.setName(employee.getName());
        vo.setUsername(employee.getUsername());
        vo.setToken(token);
        return vo;
    }

    @Override
    public void save(EmployeeDTO dto) {
        checkUsernameUnique(dto.getUsername(), null);
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);
        employee.setId(null);
        employee.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        employee.setRole(DEFAULT_ROLE);
        employee.setStatus(StatusConstant.ENABLE);
        employeeMapper.insert(employee);
    }

    @Override
    public void update(EmployeeDTO dto) {
        if (dto.getId() == null) {
            throw new BaseException(ResultCode.PARAM_ERROR);
        }
        checkUsernameUnique(dto.getUsername(), dto.getId());
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);
        // 密码、角色、状态不允许通过编辑接口修改
        employee.setPassword(null);
        employee.setRole(null);
        employee.setStatus(null);
        employeeMapper.updateById(employee);
    }

    @Override
    public PageResult<EmployeeVO> pageQuery(EmployeePageQueryDTO dto) {
        Page<Employee> page = new Page<>(dto.getPage(), dto.getPageSize());
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>()
                .like(StringUtils.hasText(dto.getName()), Employee::getName, dto.getName())
                .orderByDesc(Employee::getCreateTime);
        employeeMapper.selectPage(page, wrapper);
        List<EmployeeVO> records = page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), records);
    }

    @Override
    public EmployeeVO getById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        if (employee == null) {
            throw new BaseException(ResultCode.NOT_FOUND);
        }
        return toVO(employee);
    }

    @Override
    public void enableOrDisable(Long id, Integer status) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setStatus(status);
        employeeMapper.updateById(employee);
    }

    private void checkUsernameUnique(String username, Long excludeId) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>()
                .eq(Employee::getUsername, username);
        if (excludeId != null) {
            wrapper.ne(Employee::getId, excludeId);
        }
        Long count = employeeMapper.selectCount(wrapper);
        if (count != null && count > 0) {
            throw new BaseException("账号已存在");
        }
    }

    private EmployeeVO toVO(Employee employee) {
        EmployeeVO vo = new EmployeeVO();
        BeanUtils.copyProperties(employee, vo);
        return vo;
    }
}
