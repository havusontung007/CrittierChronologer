package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exceptions.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.MissingDataException;
import com.udacity.jdnd.course3.critter.exceptions.PetNotFoundException;
import com.udacity.jdnd.course3.critter.service.impl.PetServiceImpl;
import com.udacity.jdnd.course3.critter.service.impl.UserServiceImpl;
import com.udacity.jdnd.course3.critter.service.impl.ValidationServiceImpl;
import org.mockito.exceptions.verification.ArgumentsAreDifferent;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final String []  PROPERTIES_TO_IGNORE_ON_COPY = { "id" };

    private UserServiceImpl userServiceImpl;

    private PetServiceImpl petServiceImpl;

    private ValidationServiceImpl validationServiceImpl;

    public UserController(UserServiceImpl userServiceImpl, PetServiceImpl petServiceImpl, ValidationServiceImpl validationServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.petServiceImpl = petServiceImpl;
        this.validationServiceImpl = validationServiceImpl;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Long id = Optional.ofNullable(customerDTO.getId()).orElse(Long.valueOf(-1));
        Customer c = userServiceImpl.findCustomer(id).orElseGet(Customer::new);
        BeanUtils.copyProperties(customerDTO, c, PROPERTIES_TO_IGNORE_ON_COPY);
        List<Long> petIds = Optional.ofNullable(customerDTO.getPetIds()).orElseGet(ArrayList::new);
        c = userServiceImpl.save(c, petIds);
        return copyCustomerToDTO(c);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = userServiceImpl.getAllCustomers();
        return copyCustomersToDTOs(customers);
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) throws PetNotFoundException{
        Pet p = petServiceImpl.findPet(petId).orElseThrow(() -> new PetNotFoundException("ID: " + petId));
        return copyCustomerToDTO(p.getOwner());
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee e = userServiceImpl.findEmployee(employeeDTO.getId()).orElseGet(Employee::new);
        BeanUtils.copyProperties(employeeDTO, e, PROPERTIES_TO_IGNORE_ON_COPY);
        e = userServiceImpl.save(e);
        return copyEmployeeToDTO(e);
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) throws EmployeeNotFoundException {
        Employee e = userServiceImpl.findEmployee(employeeId).orElseThrow(() -> new EmployeeNotFoundException("ID: " + employeeId));
        return copyEmployeeToDTO(e);
    }

    @GetMapping("/employees")
    public List<EmployeeDTO> getEmployees() {
        List<Employee> employees = userServiceImpl.findEmployees();
        return employees.stream().map((e) -> {return copyEmployeeToDTO(e);}).collect(Collectors.toList());
    }

    @Transactional
    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) throws EmployeeNotFoundException {
        Employee e = userServiceImpl.findEmployee(employeeId).orElseThrow(() -> new EmployeeNotFoundException("ID: " + employeeId));
        e.setDaysAvailable(daysAvailable);
        userServiceImpl.save(e);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeRequestDTO) throws MissingDataException {
        validationServiceImpl.validatePOJOAttributesNotNullOrEmpty(employeeRequestDTO);
        List<Employee> employees = userServiceImpl.findAvailableEmployees(employeeRequestDTO.getSkills(), employeeRequestDTO.getDate());
        return employees.stream().map(this::copyEmployeeToDTO).collect(Collectors.toList());
    }

    private EmployeeDTO copyEmployeeToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(employee, dto);
        return dto;
    }

    private CustomerDTO copyCustomerToDTO(Customer c){
        CustomerDTO dto = new CustomerDTO();
        List<Long> petList = new ArrayList<>();
        BeanUtils.copyProperties(c, dto);
        c.getPets().forEach( pet -> {
            petList.add(pet.getId());
        });
        dto.setPetIds(petList);
        return dto;
    }

    private List<CustomerDTO> copyCustomersToDTOs (List<Customer> customers) {
        List dtos = new ArrayList<CustomerDTO>();
        customers.forEach( c -> {
            dtos.add(this.copyCustomerToDTO((Customer)c));
        });
        return dtos;
    }

}
