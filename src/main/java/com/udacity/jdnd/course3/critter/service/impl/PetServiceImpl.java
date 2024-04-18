package com.udacity.jdnd.course3.critter.service.impl;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exceptions.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.PetNotFoundException;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {


    private final PetRepository petRepository;


    private final CustomerRepository customerRepository;

    @Override
    public Optional<Pet> findPet(Long id) {
        return petRepository.findById(id);
    }

    @Override
    public List<Pet> findPetByOwner(Long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Pet> findPets(List<Long> petIds) {
        List<Pet> pets = petRepository.findAllById(petIds);

        if (petIds.size() != pets.size()) {
            List<Long> found = pets.stream().map(p -> p.getId()).collect(Collectors.toList());
            String missing = (String) petIds
                    .stream()
                    .filter(id -> !found.contains(id))
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            throw new PetNotFoundException("Could not find pet(s) with id(s): " + missing);
        }
        return pets;
    }

    @Override
    public List<Pet> findAllPets() {
        return petRepository.findAll();
    }

    @Override
    @Transactional
    public Pet save(Pet p, Long ownerId) throws CustomerNotFoundException {
        Customer owner = customerRepository.findById(ownerId)
                .orElseThrow(() -> new CustomerNotFoundException("ID: " + ownerId));
        p.setOwner(owner);
        p = petRepository.save(p);
        owner.getPets().add(p);
        customerRepository.save(owner);

        return p;
    }
}
