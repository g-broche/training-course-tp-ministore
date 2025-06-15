package com.gbroche.view.components.shared.form.validators;

/**
 * Interface to be implemented by validator classes
 */
public interface Validator {

    boolean isValid(String value);

    String getErrorMessage();
}
