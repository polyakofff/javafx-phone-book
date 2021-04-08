import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Date;

public class ContactBuilderWindowController implements ChildController {
    @FXML private Label titleLabel;
    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField patronymicField;
    @FXML private TextField mobilePhoneField;
    @FXML private TextField homePhoneField;
    @FXML private TextField addressField;
    @FXML private DatePicker birthdayField;
    @FXML private TextArea notesField;
    private final boolean forNew;
    private final Contact editableContact;
    private final MainWindowController mainController;

    private static final String NORMAL_FIELD_BORDER_STYLE = "-fx-border-width: 0;";
    private static final String BAD_FIELD_BORDER_STYLE = "-fx-border-width: 1; -fx-border-color: red";
    private static final String NUMBER_PATTERN = "^\\+?[0-9]+$";

    public ContactBuilderWindowController(boolean forNew, Contact editableContact, MainWindowController mainController) {
        this.forNew = forNew;
        this.editableContact = editableContact;
        this.mainController = mainController;
    }

    @FXML private void save() {
        renew();
        boolean ok = true;
        if (nameField.getText().isEmpty()) {
            nameField.setStyle(BAD_FIELD_BORDER_STYLE);
            ok = false;
        }
        if (surnameField.getText().isEmpty()) {
            surnameField.setStyle(BAD_FIELD_BORDER_STYLE);
            ok = false;
        }
        if (!mobilePhoneField.getText().isEmpty() && !mobilePhoneField.getText().matches(NUMBER_PATTERN)) {
            mobilePhoneField.setStyle(BAD_FIELD_BORDER_STYLE);
            ok = false;
        }
        if (!homePhoneField.getText().isEmpty() && !homePhoneField.getText().matches(NUMBER_PATTERN)) {
            homePhoneField.setStyle(BAD_FIELD_BORDER_STYLE);
            ok = false;
        }
        if (mobilePhoneField.getText().isEmpty() && homePhoneField.getText().isEmpty()) {
            mobilePhoneField.setStyle(BAD_FIELD_BORDER_STYLE);
            homePhoneField.setStyle(BAD_FIELD_BORDER_STYLE);
            ok = false;
        }
        if (ok) {
            Contact contact = new Contact(nameField.getText(), surnameField.getText(), patronymicField.getText(),
                    mobilePhoneField.getText(), homePhoneField.getText(),
                    addressField.getText(), birthdayField.getValue(), notesField.getText());
            if (forNew) {
                mainController.saveContact(contact);
            } else {
                mainController.saveContact(editableContact, contact);
            }
            ((Stage) titleLabel.getScene().getWindow()).close();
        }
    }

    @FXML private void cancel() {
        renew();
        ((Stage) titleLabel.getScene().getWindow()).close();
    }

    private void renew() {
        nameField.setStyle(NORMAL_FIELD_BORDER_STYLE);
        surnameField.setStyle(NORMAL_FIELD_BORDER_STYLE);
        patronymicField.setStyle(NORMAL_FIELD_BORDER_STYLE);
        mobilePhoneField.setStyle(NORMAL_FIELD_BORDER_STYLE);
        homePhoneField.setStyle(NORMAL_FIELD_BORDER_STYLE);
        addressField.setStyle(NORMAL_FIELD_BORDER_STYLE);
        birthdayField.setStyle(NORMAL_FIELD_BORDER_STYLE);
        notesField.setStyle(NORMAL_FIELD_BORDER_STYLE);
    }

    @Override public void init() {
        if (forNew) {
            titleLabel.setText("Создание контакта");
        } else {
            titleLabel.setText("Редактирование контакта");
            nameField.setText(editableContact.getName());
            surnameField.setText(editableContact.getSurname());
            patronymicField.setText(editableContact.getPatronymic());
            mobilePhoneField.setText(editableContact.getMobilePhone());
            homePhoneField.setText(editableContact.getHomePhone());
            addressField.setText(editableContact.getAddress());
            birthdayField.setValue(editableContact.getBirthday());
            notesField.setText(editableContact.getNotes());
        }
    }

}
