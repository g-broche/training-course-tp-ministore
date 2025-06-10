package com.gbroche.view.components.shared.form.validators;

/**
 * Min length validator, validate that a string's length is longer than the
 * integer given to this class constructor
 */
public class MinLengthValidator implements Validator {

    private final int min;

    public MinLengthValidator(int min) {
        this.min = min;
    }

    @Override
    public boolean isValid(String value) {
        if (value == null) {
            return true;
        }
        int len = value.length();
        return len >= min;
    }

    @Override
    public String getErrorMessage() {
        return "Must have more than " + min + " characters.";
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
