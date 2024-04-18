package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;
import com.udacity.jdnd.course3.critter.exceptions.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.EmployeeNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    Customer save(Customer c, List<Long> petIds);

    Employee save(Employee e);

    List<Customer> getAllCustomers();

    Optional<Employee> findEmployee(Long id) throws EmployeeNotFoundException;

    Customer findOwnerByPet(Long id) throws CustomerNotFoundException;

    List<Employee> findAvailableEmployees(Set<EmployeeSkill> skills, LocalDate date);

    List<Employee> findEmployees(List<Long> employeeIds) throws EmployeeNotFoundException;

    List<Employee> findEmployees();
}
