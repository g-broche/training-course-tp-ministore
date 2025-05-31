package com.gbroche.view.components.shared.form.validators;

import java.util.regex.Pattern;

public class EmailValidator implements Validator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    @Override
    public boolean isValid(String value) {
        return value != null && EMAIL_PATTERN.matcher(value).matches();
    }

    @Override
    public String getErrorMessage() {
        return "Invalid email format.";
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
