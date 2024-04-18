package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.exceptions.MissingDataException;

public interface ValidationService {
    public void validatePOJOAttributesNotNullOrEmpty(Object pojo) throws MissingDataException;

}
