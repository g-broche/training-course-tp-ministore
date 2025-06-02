package com.gbroche.view.components.shared.form.validators;

public class MaxIntegerValidator implements Validator {

    private final int max;

    public MaxIntegerValidator(int max) {
        this.max = max;
    }

    @Override
    public boolean isValid(String stringNumber) {
        if (stringNumber == null) {
            return true;
        }
        try {
            int value = Integer.parseInt(stringNumber);
            return value <= max;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getErrorMessage() {
        return "Must be integer lower than " + max;
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
