package com.udacity.jdnd.course3.critter.dto;

import com.udacity.jdnd.course3.critter.entity.PetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents the form that pet request and response data takes. Does not map
 * to the database directly.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PetDTO {
    private long id;
    private PetType type;
    private String name;
    private long ownerId;
    private LocalDate birthDate;
    private String notes;
}
