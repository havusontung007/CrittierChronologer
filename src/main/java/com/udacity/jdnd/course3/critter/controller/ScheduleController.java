package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.exceptions.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.MissingDataException;
import com.udacity.jdnd.course3.critter.exceptions.PetNotFoundException;
import com.udacity.jdnd.course3.critter.service.impl.PetServiceImpl;
import com.udacity.jdnd.course3.critter.service.impl.ScheduleServiceImpl;
import com.udacity.jdnd.course3.critter.service.impl.UserServiceImpl;
import com.udacity.jdnd.course3.critter.service.impl.ValidationServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private static final String []  PROPERTIES_TO_IGNORE_ON_COPY = { "id" };

    @Autowired
    ScheduleServiceImpl scheduleServiceImpl;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    PetServiceImpl petServiceImpl;

    @Autowired
    ValidationServiceImpl validationServiceImpl;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO)
            throws EmployeeNotFoundException, PetNotFoundException,
            MissingDataException {

        validationServiceImpl.validatePOJOAttributesNotNullOrEmpty(scheduleDTO);

        Schedule s = scheduleServiceImpl.findSchedule(scheduleDTO.getId()).orElseGet(Schedule::new);

        s.setDate(scheduleDTO.getDate());
        s.setActivities(scheduleDTO.getActivities());
        s.setEmployees(userServiceImpl.findEmployees(scheduleDTO.getEmployeeIds()));
        s.setPets(petServiceImpl.findPets(scheduleDTO.getPetIds()));

        s = scheduleServiceImpl.save(s);

        return copyScheduleToDTO(s);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleServiceImpl.findAllSchedules();
        return copyScheduleToDTO(schedules);
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) throws PetNotFoundException {
        return copyScheduleToDTO(scheduleServiceImpl.findSchedulesForPet(petId));
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) throws EmployeeNotFoundException {
        return copyScheduleToDTO(scheduleServiceImpl.findSchedulesForEmployee(employeeId));
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) throws CustomerNotFoundException {
        return copyScheduleToDTO(scheduleServiceImpl.findSchedulesForCustomer(customerId));
    }

    private List<ScheduleDTO> copyScheduleToDTO(List<Schedule> schedules) {
        return schedules
                .stream()
                .map(s -> { return copyScheduleToDTO(s); })
                .collect(Collectors.toList());
    }

    private ScheduleDTO copyScheduleToDTO(Schedule s) {
        ScheduleDTO dto = new ScheduleDTO();
        BeanUtils.copyProperties(s, dto);
        s.getEmployees().forEach(employee -> {dto.getEmployeeIds().add(employee.getId());});
        s.getPets().forEach(pet -> {dto.getPetIds().add(pet.getId());});
        return dto;
    }
}
