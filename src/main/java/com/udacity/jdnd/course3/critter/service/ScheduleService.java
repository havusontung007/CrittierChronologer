package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.exceptions.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.PetNotFoundException;

import java.util.List;


public interface ScheduleService {
    Schedule save(Schedule s)
            throws PetNotFoundException, EmployeeNotFoundException;

    List<Schedule> findSchedulesForPet(long petId);

    List<Schedule> findSchedulesForEmployee(long employeeId);

    List<Schedule> findSchedulesForCustomer(long customerId);
}
