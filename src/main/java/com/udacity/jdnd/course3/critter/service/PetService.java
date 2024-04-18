package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exceptions.CustomerNotFoundException;

import java.util.List;
import java.util.Optional;

public interface PetService {
    Optional<Pet> findPet(Long id);

    List<Pet> findPetByOwner(Long ownerId);

    List<Pet> findPets(List<Long> petIds);

    List<Pet> findAllPets();

    Pet save(Pet p, Long ownerId) throws CustomerNotFoundException;
}
