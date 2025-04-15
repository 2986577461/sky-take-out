package com.sky.mapper;


import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper  {

    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    int insert(Employee employee);

    List<Employee> getPageByLikeName(@Param("employee")EmployeePageQueryDTO employeePageQueryDTO);

    void updateEmployee(Employee employee);

    @Select("SELECT * FROM employee WHERE id=#{id}")
    Employee selectOne(Long id);
}
