package com.gbroche.view.components.shared.form.validators;

public class IntegerValidator implements Validator {

    @Override
    public boolean isValid(String value) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String getErrorMessage() {
        return "Must be a number.";
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
