package com.udacity.jdnd.course3.critter.service.impl;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.exceptions.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.PetNotFoundException;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;
    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;

    public Optional<Schedule> findSchedule(Long id) {
        return scheduleRepository.findById(id);
    }

    public List<Schedule> findAllSchedules() {
        return scheduleRepository.findAll();
    }
    @Override
    @Transactional
    public Schedule save(Schedule s)
            throws PetNotFoundException, EmployeeNotFoundException {

        s = scheduleRepository.save(s);

        // save the schedule to employees
        for (Employee employee : s.getEmployees()) {
            employee.getSchedules().add(s);
            employeeRepository.save(employee);
        }

        // save the schedule to pets
        for (Pet pet : s.getPets()) {
            pet.getSchedules().add(s);
            petRepository.save(pet);
        }

        return s;
    }
    @Override
    public List<Schedule> findSchedulesForPet(long petId) {
        Pet p = petRepository.findById(petId).orElseThrow(() -> new PetNotFoundException("ID: " + petId));
        return p.getSchedules();
    }
    @Override
    public List<Schedule> findSchedulesForEmployee(long employeeId) {
        Employee e = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("ID: " + employeeId));
        return e.getSchedules();
    }
    @Override
    public List<Schedule> findSchedulesForCustomer(long customerId) {
        Customer c = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("ID: " + customerId));
        List<Schedule> customerSchedules = c.getPets()
                .stream()
                .map(Pet::getSchedules)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return customerSchedules;
    }
}
