package com.gbroche.view.components.shared.form.validators;

/**
 * Required validator, validates that a string is not empty or null
 */
public class RequiredValidator implements Validator {

    @Override
    public boolean isValid(String value) {
        return value != null && !value.trim().isEmpty();
    }

    @Override
    public String getErrorMessage() {
        return "This field is required.";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RequiredValidator;
    }

    @Override
    public int hashCode() {
        return RequiredValidator.class.hashCode();
    }
}
