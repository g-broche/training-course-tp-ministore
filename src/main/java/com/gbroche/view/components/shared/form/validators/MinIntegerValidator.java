package com.gbroche.view.components.shared.form.validators;

public class MinIntegerValidator implements Validator {

    private final int min;

    public MinIntegerValidator(int min) {
        this.min = min;
    }

    @Override
    public boolean isValid(String stringNumber) {
        if (stringNumber == null) {
            return true;
        }
        try {
            int value = Integer.parseInt(stringNumber);
            return value >= min;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getErrorMessage() {
        return "Must be integer greater than " + min;
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
