package com.gbroche.view.components.shared.form.groups;

import java.awt.event.ActionListener;

import javax.swing.JComponent;

/**
 * Interface to be implemented by input belonging to FormGroups
 */
public interface FormInput {

    FormInput getInputInstance();

    JComponent getComponent();

    String getValue();

    void setValue(String value);

    void addListener(ActionListener listener);
}
