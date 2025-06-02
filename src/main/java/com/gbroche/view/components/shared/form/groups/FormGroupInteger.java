package com.gbroche.view.components.shared.form.groups;

import com.gbroche.view.components.shared.form.validators.IntegerValidator;

public class FormGroupInteger extends FormGroup {

    public FormGroupInteger(String fieldName, String labelText, FormInput input) {
        super(fieldName, labelText, input);
        addValidator(new IntegerValidator());
    }

    public Integer getValueAsInteger() {
        try {
            int value = Integer.parseInt(getValue());
            return value;
        } catch (NumberFormatException e) {
            return null;
        }

    }
}
