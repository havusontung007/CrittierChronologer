package com.udacity.jdnd.course3.critter.dto;

import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the form that schedule request and response data takes. Does not map
 * to the database directly.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScheduleDTO {
    private long id;
    private List<Long> employeeIds = new ArrayList<>();
    private List<Long> petIds = new ArrayList<>();
    private LocalDate date;
    private Set<EmployeeSkill> activities = new HashSet<>();
}
