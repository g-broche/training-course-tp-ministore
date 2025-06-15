package com.gbroche.view.components.customer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.gbroche.dao.CityDao;
import com.gbroche.dao.CountryDao;
import com.gbroche.dao.CustomerDao;
import com.gbroche.model.City;
import com.gbroche.model.Country;
import com.gbroche.view.components.shared.ViewPanel;
import com.gbroche.view.components.shared.form.groups.ComboBoxInput;
import com.gbroche.view.components.shared.form.groups.FormGroup;
import com.gbroche.view.components.shared.form.groups.FormGroupInteger;
import com.gbroche.view.components.shared.form.groups.TextFieldInput;
import com.gbroche.view.components.shared.form.validators.EmailValidator;
import com.gbroche.view.components.shared.form.validators.MaxIntegerValidator;
import com.gbroche.view.components.shared.form.validators.MaxLengthValidator;
import com.gbroche.view.components.shared.form.validators.MinIntegerValidator;
import com.gbroche.view.components.shared.form.validators.MinLengthValidator;
import com.gbroche.view.components.shared.form.validators.RequiredValidator;
import com.gbroche.view.components.shared.form.validators.Validator;

/**
 * Component for the view letting the user add a new customer
 */
public final class AddCustomerForm extends ViewPanel {

    private JPanel form;
    private FormGroup firstNameField;
    private FormGroup lastNameField;
    private FormGroup address1Field;
    private FormGroup address2Field;
    private FormGroup countryField;
    private FormGroup cityField;
    private FormGroup stateField;
    private FormGroup zipField;
    private FormGroup regionField;
    private FormGroup emailField;
    private FormGroup phoneField;
    private FormGroup creditCardTypeField;
    private FormGroup creditCardField;
    private FormGroup creditCardExpirationField;
    private FormGroup usernameField;
    private FormGroup passwordField;
    private FormGroup ageField;
    private FormGroup incomeField;
    private FormGroup genderField;
    private JButton submit;

    // list of FormGroup instances used for inputs validations and interactions
    private final List<FormGroup> formGroups = new ArrayList<>();

    private Map<String, String> countryMap = new LinkedHashMap<>();
    private Map<Integer, String> cityMap = new LinkedHashMap<>();
    private final String[] genders = new String[] { "M", "F", "N" };

    public AddCustomerForm() {
        super("Add Customer");
        buildContent();
    }

    /**
     * Build component content on creation
     */
    @Override
    protected void buildContent() {
        form = new JPanel(new GridBagLayout());
        loadAvailableCountries();
        generateFields();
        // Uncomment line below to preload the fields with working values
        // fillFieldsForTest()
        // Get the next available row index to append the form button
        int nextRow = createLayout();
        // Add button at the bottom of the GridBagLayout
        addButtonToView(nextRow);
        JScrollPane scrollPane = new JScrollPane(form);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // fillFieldsForTest();
        submit.addActionListener(e -> handleSubmit());

        changeContent(scrollPane);
    }

    /**
     * Validates if valid data was inputed and in such case will call the
     * CustomerDao to insert the
     * the new custormer based on inputs data
     */
    private void handleSubmit() {
        if (!isFormValid()) {
            System.out.println("form is invalid");
            return;
        }
        System.out.println("form is valid");
        Map<String, String> inputResults = getInputsForBinding();
        boolean insertSuccess = CustomerDao.getInstance().addCustomer(inputResults);
        if (!insertSuccess) {
            JOptionPane.showMessageDialog(
                    null,
                    "An error occurred while attempting to add the user to the database, a user with similar data may already exist and cause a conflict",
                    "Error creating new user",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(
                null,
                "User was successfully added",
                "User created",
                JOptionPane.INFORMATION_MESSAGE);
        resetFields();
    }

    /**
     * Iterates through each FormGroup of the form to check if they are all valid
     * 
     * @return true if all input are valid, false otherwise
     */
    private boolean isFormValid() {
        List<Boolean> validationResults = new ArrayList<>();
        for (FormGroup formGroup : formGroups) {
            validationResults.add(formGroup.validateInput());
        }
        return !validationResults.isEmpty() && validationResults.stream().allMatch(Boolean::booleanValue);
    }

    /**
     * Retrieves the inputed data using the FormGroups' name to create a map of
     * key:value that will be used for binding values in the DAO query statement
     */
    private Map<String, String> getInputsForBinding() {
        Map<String, String> inputResults = new HashMap<>();
        for (FormGroup formGroup : formGroups) {
            System.out.println(">>> formgroup: " + formGroup.getFieldName());
            if (formGroup.validateInput()) {
                System.out.println("fieldName: " + formGroup.getFieldName() + " ; value: " + formGroup.getValue());
                inputResults.put(formGroup.getFieldName(), formGroup.getValue());
            }
        }
        return inputResults;
    }

    /**
     * Creates the form layout
     * 
     * @return next row if the GridBag layout to add the submit button below the
     *         inputs
     */
    private int createLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 0;

        for (FormGroup group : formGroups) {
            // Label - 3fr
            gbc.gridx = 0;
            gbc.weightx = 3;
            gbc.anchor = GridBagConstraints.LINE_END;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            form.add(group.getLabel(), gbc);

            // Input - 3fr
            gbc.gridx = 1;
            gbc.weightx = 3;
            gbc.anchor = GridBagConstraints.LINE_START;
            form.add(group.getInputComponent(), gbc);

            // Error - 6fr
            gbc.gridx = 2;
            gbc.weightx = 6;
            gbc.anchor = GridBagConstraints.LINE_START;
            form.add(group.getErrorLabel(), gbc);

            gbc.gridy++;
        }

        return gbc.gridy;
    }

    /**
     * Generate all form groups
     */
    private void generateFields() {
        generateFirstNameField();
        generateLastNameField();
        generateAddress1Field();
        generateAddress2Field();
        generateCountryField();
        generateCityField();
        generateStateField();
        generateZipField();
        generateRegionField();
        generateEmailField();
        generatePhoneField();
        generateCreditCardTypeField();
        generateCreditCardField();
        generateCreditCardExpirationField();
        generateUsernameField();
        generatePasswordField();
        generateAgeField();
        generateIncomeField();
        generateGenderField();
    }

    private void addButtonToView(int nextRow) {
        submit = new JButton("Submit");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = nextRow;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);
        form.add(submit, gbc);
    }

    private void generateFirstNameField() {
        firstNameField = new FormGroup("firstname", "First name", new TextFieldInput());
        firstNameField.addValidators(new Validator[] {
                new RequiredValidator(),
                new MaxLengthValidator(50)
        });
        formGroups.add(firstNameField);
    }

    private void generateLastNameField() {
        lastNameField = new FormGroup("lastname", "Last name", new TextFieldInput());
        lastNameField.addValidators(new Validator[] {
                new RequiredValidator(),
                new MaxLengthValidator(50)
        });
        formGroups.add(lastNameField);
    }

    private void generateAddress1Field() {
        address1Field = new FormGroup("address1", "Address", new TextFieldInput());
        address1Field.addValidators(new Validator[] {
                new RequiredValidator(),
                new MaxLengthValidator(50)
        });
        formGroups.add(address1Field);
    }

    private void generateAddress2Field() {
        address2Field = new FormGroup("address2", "Address (complement)", new TextFieldInput());
        address2Field.addValidators(new Validator[] {
                new MaxLengthValidator(50)
        });
        formGroups.add(address2Field);
    }

    /**
     * Generate the country field but also as a side effect to change available
     * cities in the city collector based on the currently selected country
     */
    private void generateCountryField() {
        countryField = new FormGroup("country", "Country",
                new ComboBoxInput(countryMap.keySet().toArray(new String[0])));
        countryField.addValidators(new Validator[] {
                new RequiredValidator(),
                new MaxLengthValidator(50)
        });
        formGroups.add(countryField);
        countryField.getInputInstance().addListener(e -> {
            String selectedCountry = countryField.getValue();
            if (selectedCountry == null || selectedCountry.isEmpty()) {
                clearCityData();
                return;
            }
            String countryCode = countryMap.get(selectedCountry);
            updateCityField(countryCode);
        });
    }

    private void generateCityField() {
        loadAvailableCities(getCodeOfSelectedCountry());
        cityField = new FormGroup("city", "City", new ComboBoxInput(cityMap.values().toArray(new String[0])));
        cityField.addValidators(new Validator[] {
                new RequiredValidator(),
                new MaxLengthValidator(50)
        });
        formGroups.add(cityField);
    }

    private void generateStateField() {
        stateField = new FormGroup("state", "State", new TextFieldInput());
        stateField.addValidators(new Validator[] {
                new RequiredValidator(),
                new MaxLengthValidator(50)
        });
        formGroups.add(stateField);
    }

    private void generateZipField() {
        zipField = new FormGroupInteger("zip", "Zip", new TextFieldInput());
        formGroups.add(zipField);
    }

    private void generateRegionField() {
        regionField = new FormGroupInteger("region", "Region", new TextFieldInput());
        regionField.addValidators(new Validator[] {
                new RequiredValidator(),
                new MaxIntegerValidator(65535)
        });
        formGroups.add(regionField);
    }

    private void generateEmailField() {
        emailField = new FormGroup("email", "Email", new TextFieldInput());
        emailField.addValidators(new Validator[] {
                new RequiredValidator(),
                new EmailValidator(),
                new MaxLengthValidator(50)
        });
        formGroups.add(emailField);
    }

    private void generatePhoneField() {
        phoneField = new FormGroup("phone", "Phone", new TextFieldInput());
        phoneField.addValidators(new Validator[] {
                new RequiredValidator(),
                new MaxLengthValidator(50),
                new MinLengthValidator(10)
        });
        formGroups.add(phoneField);
    }

    private void generateCreditCardTypeField() {
        creditCardTypeField = new FormGroupInteger("creditcardtype", "Credit card type", new TextFieldInput());
        creditCardTypeField.addValidators(new Validator[] {
                new RequiredValidator(),
                new MaxIntegerValidator(65535)
        });
        formGroups.add(creditCardTypeField);
    }

    private void generateCreditCardField() {
        creditCardField = new FormGroup("creditcard", "Card number", new TextFieldInput());
        creditCardField.addValidators(new Validator[] {
                new RequiredValidator(),
                new MaxLengthValidator(50),
                new MinLengthValidator(10)
        });
        formGroups.add(creditCardField);
    }

    private void generateCreditCardExpirationField() {
        creditCardExpirationField = new FormGroup("creditcardexpiration", "Expiration date", new TextFieldInput());
        creditCardExpirationField.addValidators(new Validator[] {
                new RequiredValidator(),
                new MaxLengthValidator(50),
                new MinLengthValidator(5)
        });
        formGroups.add(creditCardExpirationField);
    }

    private void generateUsernameField() {
        usernameField = new FormGroup("username", "Username", new TextFieldInput());
        usernameField.addValidators(new Validator[] {
                new RequiredValidator(),
                new MaxLengthValidator(50)
        });
        formGroups.add(usernameField);
    }

    private void generatePasswordField() {
        passwordField = new FormGroup("password", "Password", new TextFieldInput());
        passwordField.addValidators(new Validator[] {
                new RequiredValidator(),
                new MinLengthValidator(10),
                new MaxLengthValidator(50)
        });
        formGroups.add(passwordField);
    }

    private void generateAgeField() {
        ageField = new FormGroupInteger("age", "Age", new TextFieldInput());
        ageField.addValidators(new Validator[] {
                new MinIntegerValidator(16),
                new MaxIntegerValidator(130)
        });
        formGroups.add(ageField);
    }

    private void generateIncomeField() {
        incomeField = new FormGroupInteger("income", "Income", new TextFieldInput());
        incomeField.addValidators(new Validator[] {
                new MinIntegerValidator(0)
        });
        formGroups.add(incomeField);
    }

    private void generateGenderField() {
        genderField = new FormGroup("gender", "Gender", new ComboBoxInput(genders));
        genderField.addValidators(new Validator[] {
                new MaxLengthValidator(1)
        });
        formGroups.add(genderField);
    }

    /**
     * Retrieves all available countriers and fills the countryMapp with the
     * countries' name as key and their code as value in order to easily retrieve
     * the value from the country name that will be used in the country selector
     */
    private void loadAvailableCountries() {
        countryMap.clear();
        countryMap = CountryDao.getInstance().getAllCountries().stream()
                .collect(Collectors.toMap(
                        Country::getName,
                        Country::getCode,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new));
    }

    /**
     * Retrieves all available countries for a given country code and stores the
     * data in a Map with the city id as key and its name as value
     * 
     * @param countryCode
     */
    private void loadAvailableCities(String countryCode) {
        cityMap.clear();
        cityMap = CityDao.getInstance().getCitiesByCountryCode(countryCode).stream()
                .collect(Collectors.toMap(
                        City::getId,
                        City::getName,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new));
    }

    /**
     * clear all city related data from both the cityMap and the city field
     */
    private void clearCityData() {
        cityMap.clear();
        cityField.getInputInstance().getComponent().removeAll();
    }

    /**
     * Updates the available cities for selection in the city selector
     * 
     * @param countryCode
     */
    private void updateCityField(String countryCode) {
        loadAvailableCities(countryCode);
        if (cityField != null) {
            ComboBoxInput cityInput = (ComboBoxInput) cityField.getInputInstance();
            cityInput.setModel(new DefaultComboBoxModel<>(cityMap.values().toArray(new String[0])));
        }
    }

    /**
     * Retrieves the country code of a selected country by using the selected
     * country name to navigate the countryMap
     * 
     * @return String with the country code of selected country if one, otherwise
     *         null
     */
    private String getCodeOfSelectedCountry() {
        if (countryField.getValue().isEmpty()) {
            return null;
        }
        return countryMap.get(countryField.getValue());
    }

    /**
     * function for testing form that will preload the input text fields with
     * working values. If used, place after field generation
     */
    private void fillFieldsForTest() {
        firstNameField.setValue("firstname test");
        lastNameField.setValue("lastname test");
        address1Field.setValue("address 1 test");
        address2Field.setValue("address 2 test");
        stateField.setValue("state test");
        zipField.setValue("12345");
        regionField.setValue("45");
        emailField.setValue("test@test.test");
        phoneField.setValue("0123456789");
        creditCardTypeField.setValue("3");
        creditCardField.setValue("53892942494");
        creditCardExpirationField.setValue("03/27");
        usernameField.setValue("usernameTest");
        passwordField.setValue("passwordTest");
        ageField.setValue("27");
        incomeField.setValue("40000");
    }

    /**
     * Clears all text field inputs
     */
    private void resetFields() {
        firstNameField.setValue("");
        lastNameField.setValue("");
        address1Field.setValue("");
        address2Field.setValue("");
        stateField.setValue("");
        zipField.setValue("");
        regionField.setValue("");
        emailField.setValue("");
        phoneField.setValue("");
        creditCardTypeField.setValue("");
        creditCardField.setValue("");
        creditCardExpirationField.setValue("");
        usernameField.setValue("");
        passwordField.setValue("");
        ageField.setValue("");
        incomeField.setValue("");
        for (FormGroup formGroup : formGroups) {
            formGroup.clearError();
        }
    }
}
