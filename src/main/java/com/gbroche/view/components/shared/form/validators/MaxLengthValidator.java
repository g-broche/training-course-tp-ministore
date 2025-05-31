package com.gbroche.view.components.shared.form.validators;

public class MaxLengthValidator implements Validator {

    private final int max;

    public MaxLengthValidator(int max) {
        this.max = max;
    }

    @Override
    public boolean isValid(String value) {
        if (value == null) {
            return true;
        }
        int len = value.length();
        return len <= max;
    }

    @Override
    public String getErrorMessage() {
        return "Must have less than " + max + " characters.";
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
