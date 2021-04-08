import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainWindowController {

    @FXML private TableView<Contact> contactsPanel;
    @FXML private TextField searchLine;

    private final Model model = new Model();

    @FXML public void initialize() {
        model.registerEvents(() -> displayContacts(model.getContacts()), this::displayContacts);
        displayContacts(model.getContacts());

    }

    @FXML private void addContact() {
        createContactBuilderWindow(true, null);
    }

    @FXML private void editContact() {
        if (contactsPanel.getSelectionModel().getSelectedIndex() != -1) {
            Contact contact = contactsPanel.getSelectionModel().getSelectedItem();
            createContactBuilderWindow(false, contact);
        }
    }

    private void createContactBuilderWindow(boolean forNew, Contact contact) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ContactBuilderWindow.fxml"));
            ChildController controller = new ContactBuilderWindowController(forNew, contact, this);
            fxmlLoader.setController(controller);
            VBox vBox = fxmlLoader.load();
            Scene scene = new Scene(vBox);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(contactsPanel.getScene().getWindow());
            stage.setWidth(400);
            stage.setHeight(400);
            stage.setScene(scene);
            stage.show();
            controller.init();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveContact(Contact contact) {
        model.addContact(contact);
    }

    public void saveContact(Contact oldContact, Contact contact) {
        model.editContact(oldContact, contact);
    }

    @FXML private void deleteContact() {
        if (contactsPanel.getSelectionModel().getSelectedIndex() != -1) {
            Contact contact = contactsPanel.getSelectionModel().getSelectedItem();
            model.deleteContact(contact);
        }
    }

    @FXML private void searchContacts() {
        String pattern = searchLine.getText();
        model.searchContacts(pattern);
    }

    @FXML private void exit() {
        model.exit();
        ((Stage) contactsPanel.getScene().getWindow()).close();
    }

    @FXML private void importContacts() {
        createContactsImportExportWindow(true);
    }

    @FXML private void exportContacts() {
        createContactsImportExportWindow(false);
    }

    private void createContactsImportExportWindow(boolean forImport) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ContactsImportExportWindow.fxml"));
            ChildController controller = new ContactsImportExportWindowController(forImport, this);
            fxmlLoader.setController(controller);
            VBox vBox = fxmlLoader.load();
            Scene scene = new Scene(vBox);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(contactsPanel.getScene().getWindow());
            stage.setWidth(400);
            stage.setHeight(400);
            stage.setScene(scene);
            stage.show();
            controller.init();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importContacts(String location) {
        model.importContacts(location);
    }

    public void exportContacts(String location) {
        model.exportContacts(location);
    }

    @FXML private void aboutAuthor() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AboutAuthorWindow.fxml"));
            ChildController controller = new AboutAuthorWindowController(this);
            fxmlLoader.setController(controller);
            VBox vBox = fxmlLoader.load();
            Scene scene = new Scene(vBox);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(contactsPanel.getScene().getWindow());
            stage.setWidth(400);
            stage.setHeight(400);
            stage.setScene(scene);
            stage.show();
            controller.init();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayContacts(List<Contact> contacts) {
        contactsPanel.getItems().clear();
        for (Contact c : contacts) {
            contactsPanel.getItems().add(c);
        }
    }

    interface ContactsUpdated {
        void event();
    }

    interface ContactsFound {
        void event(List<Contact> foundContacts);
    }
}
