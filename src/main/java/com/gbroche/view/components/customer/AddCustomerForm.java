package com.gbroche.view.components.customer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.gbroche.dao.CityDao;
import com.gbroche.dao.CountryDao;
import com.gbroche.model.City;
import com.gbroche.model.Country;
import com.gbroche.view.components.shared.ViewPanel;
import com.gbroche.view.components.shared.form.groups.ComboBoxInput;
import com.gbroche.view.components.shared.form.groups.FormGroup;
import com.gbroche.view.components.shared.form.groups.TextFieldInput;
import com.gbroche.view.components.shared.form.validators.EmailValidator;
import com.gbroche.view.components.shared.form.validators.MaxIntegerValidator;
import com.gbroche.view.components.shared.form.validators.MaxLengthValidator;
import com.gbroche.view.components.shared.form.validators.MinIntegerValidator;
import com.gbroche.view.components.shared.form.validators.MinLengthValidator;
import com.gbroche.view.components.shared.form.validators.NumericValidator;
import com.gbroche.view.components.shared.form.validators.RequiredValidator;
import com.gbroche.view.components.shared.form.validators.Validator;

public class AddCustomerForm extends ViewPanel {

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

    private List<FormGroup> formGroups = new ArrayList<>();

    private Map<String, String> countryMap = new LinkedHashMap<>();
    private Map<Integer, String> cityMap = new LinkedHashMap<>();
    private String[] genders = new String[]{"M", "F", "N"};

    // firstname_in character varying,
    // lastname_in character varying,
    // address1_in character varying,
    // address2_in character varying,
    // city_in character varying,
    // state_in character varying,
    // zip_in integer,
    // country_in character varying,
    // region_in integer,
    // email_in character varying,
    // phone_in character varying,
    // creditcardtype_in integer,
    // creditcard_in character varying,
    // creditcardexpiration_in character varying,
    // username_in character varying,
    // password_in character varying,
    // age_in integer,
    // income_in integer,
    // gender_in character varying,
    public AddCustomerForm() {
        super("Add Customer");
        buildContent();
    }

    @Override
    protected void buildContent() {
        form = new JPanel(new GridBagLayout());
        loadAvailableCountries();
        generateFields();
        int nextRow = createLayout(); // Get the next available row index
        addButtonToView(nextRow);
        JScrollPane scrollPane = new JScrollPane(form);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // submit.addActionListener(e
        //         -> handleSubmit());
        // form.add(submit);
        changeContent(scrollPane);
    }

    private void handleSubmit() {
        // nameField.clearError();

        // if (nameField.isEmpty()) {
        //     nameField.setError("Name is required");
        // } else {
        //     System.out.println("Submitted: " + nameField.getValue());
        // }
    }

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

        return gbc.gridy; // Return the next available row index
    }

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
        firstNameField = new FormGroup("First name", new TextFieldInput());
        firstNameField.addValidators(new Validator[]{
            new RequiredValidator(),
            new MaxLengthValidator(50)
        });
        formGroups.add(firstNameField);
    }

    private void generateLastNameField() {
        lastNameField = new FormGroup("Last name", new TextFieldInput());
        lastNameField.addValidators(new Validator[]{
            new RequiredValidator(),
            new MaxLengthValidator(50)
        });
        formGroups.add(lastNameField);
    }

    private void generateAddress1Field() {
        address1Field = new FormGroup("Address", new TextFieldInput());
        address1Field.addValidators(new Validator[]{
            new RequiredValidator(),
            new MaxLengthValidator(50)
        });
        formGroups.add(address1Field);
    }

    private void generateAddress2Field() {
        address2Field = new FormGroup("Address (complement)", new TextFieldInput());
        address2Field.addValidators(new Validator[]{
            new MaxLengthValidator(50)
        });
        formGroups.add(address2Field);
    }

    private void generateCountryField() {
        countryField = new FormGroup("Country", new ComboBoxInput(countryMap.keySet().toArray(new String[0])));
        countryField.addValidators(new Validator[]{
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
        cityField = new FormGroup("City", new ComboBoxInput(cityMap.values().toArray(new String[0])));
        cityField.addValidators(new Validator[]{
            new RequiredValidator(),
            new MaxLengthValidator(50)
        });
        formGroups.add(cityField);
    }

    private void generateStateField() {
        stateField = new FormGroup("State", new TextFieldInput());
        stateField.addValidators(new Validator[]{
            new RequiredValidator(),
            new MaxLengthValidator(50)
        });
        formGroups.add(stateField);
    }

    private void generateZipField() {
        zipField = new FormGroup("State", new TextFieldInput());
        stateField.addValidators(new Validator[]{
            new NumericValidator(),});
        formGroups.add(zipField);
    }

    private void generateRegionField() {
        regionField = new FormGroup("Region", new TextFieldInput());
        regionField.addValidators(new Validator[]{
            new RequiredValidator(),
            new NumericValidator(),
            new MaxIntegerValidator(65535)
        });
        formGroups.add(regionField);
    }

    private void generateEmailField() {
        emailField = new FormGroup("Email", new TextFieldInput());
        emailField.addValidators(new Validator[]{
            new RequiredValidator(),
            new EmailValidator(),
            new MaxLengthValidator(50)
        });
        formGroups.add(emailField);
    }

    private void generatePhoneField() {
        phoneField = new FormGroup("Phone", new TextFieldInput());
        phoneField.addValidators(new Validator[]{
            new RequiredValidator(),
            new MaxLengthValidator(50),
            new MinLengthValidator(10)
        });
        formGroups.add(phoneField);
    }

    private void generateCreditCardTypeField() {
        creditCardTypeField = new FormGroup("Credit card type", new TextFieldInput());
        creditCardTypeField.addValidators(new Validator[]{
            new RequiredValidator(),
            new NumericValidator(),
            new MaxIntegerValidator(65535)
        });
        formGroups.add(creditCardTypeField);
    }

    private void generateCreditCardField() {
        creditCardField = new FormGroup("Card number", new TextFieldInput());
        creditCardField.addValidators(new Validator[]{
            new RequiredValidator(),
            new MaxLengthValidator(50),
            new MinLengthValidator(10)
        });
        formGroups.add(creditCardField);
    }

    private void generateCreditCardExpirationField() {
        creditCardExpirationField = new FormGroup("Expiration date", new TextFieldInput());
        creditCardExpirationField.addValidators(new Validator[]{
            new RequiredValidator(),
            new MaxLengthValidator(50),
            new MinLengthValidator(10)
        });
        formGroups.add(creditCardExpirationField);
    }

    private void generateUsernameField() {
        usernameField = new FormGroup("Username", new TextFieldInput());
        usernameField.addValidators(new Validator[]{
            new RequiredValidator(),
            new MaxLengthValidator(50)
        });
        formGroups.add(usernameField);
    }

    private void generatePasswordField() {
        passwordField = new FormGroup("Password", new TextFieldInput());
        passwordField.addValidators(new Validator[]{
            new RequiredValidator(),
            new MinLengthValidator(10),
            new MaxLengthValidator(50)
        });
        formGroups.add(passwordField);
    }

    private void generateAgeField() {
        ageField = new FormGroup("Age", new TextFieldInput());
        ageField.addValidators(new Validator[]{
            new MinIntegerValidator(16),
            new MaxIntegerValidator(130)
        });
        formGroups.add(ageField);
    }

    private void generateIncomeField() {
        incomeField = new FormGroup("Income", new TextFieldInput());
        incomeField.addValidators(new Validator[]{
            new RequiredValidator(),
            new NumericValidator(),
            new MinIntegerValidator(0)
        });
        formGroups.add(incomeField);
    }

    private void generateGenderField() {
        genderField = new FormGroup("Gender", new ComboBoxInput(genders));
        genderField.addValidators(new Validator[]{
            new MaxLengthValidator(1)
        });
        formGroups.add(genderField);
    }

    private void loadAvailableCountries() {
        countryMap.clear();
        countryMap = CountryDao.getInstance().getAllCountries().stream()
                .collect(Collectors.toMap(
                        Country::getName,
                        Country::getCode,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }

    private void loadAvailableCities(String countryCode) {
        cityMap.clear();
        cityMap = CityDao.getInstance().getCitiesByCountryCode(countryCode).stream()
                .collect(Collectors.toMap(
                        City::getId,
                        City::getName,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }

    private void clearCityData() {
        cityMap.clear();
        countryField.getInputInstance().getComponent().removeAll();
    }

    private void updateCityField(String countryCode) {
        loadAvailableCities(countryCode);
        if (cityField != null) {
            ComboBoxInput cityInput = (ComboBoxInput) cityField.getInputInstance();
            cityInput.setModel(new DefaultComboBoxModel<>(cityMap.values().toArray(new String[0])));
        }
    }

    private String getCodeOfSelectedCountry() {
        if (countryField.getValue().isEmpty()) {
            return null;
        }
        return countryMap.get(countryField.getValue());
    }
}
