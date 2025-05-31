package com.gbroche.view.components.shared.form.validators;

public interface Validator {

    boolean isValid(String value);

    String getErrorMessage();
}
