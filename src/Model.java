import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Model {

    private final List<Contact> contacts;
    private MainWindowController.ContactsUpdated contactsUpdated;
    private MainWindowController.ContactsFound contactsFound;

    public Model() {
        contacts = new ArrayList<>();

    }

    public void registerEvents(MainWindowController.ContactsUpdated contactsUpdated, MainWindowController.ContactsFound contactsFound) {
        this.contactsUpdated = contactsUpdated;
        this.contactsFound = contactsFound;
    }

    public void addContact(Contact contact) {
        if (contacts.contains(contact))
            return;
        contacts.add(contact);
        contactsUpdated.event();
    }

    public void editContact(Contact oldContact, Contact contact) {
        int index = contacts.indexOf(oldContact);
        contacts.set(index, contact);
        contactsUpdated.event();
    }

    public void deleteContact(Contact contact) {
        contacts.remove(contact);
        contactsUpdated.event();
    }

    public void searchContacts(String pattern) {
        String s = pattern.toUpperCase();
        List<Contact> foundContacts = contacts.stream()
                .filter(c -> (c.getName().toUpperCase().contains(s) ||
                        c.getSurname().toUpperCase().contains(s) ||
                        c.getPatronymic().toUpperCase().contains(s)))
                .collect(Collectors.toList());
        contactsFound.event(foundContacts);
    }

    public void exit() {
        exportContacts(".\\contacts.json");
    }

    public void importContacts(String location) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Contact> importedContacts = objectMapper.readValue(new File(location), new TypeReference<List<Contact>>(){});
            for (Contact contact : importedContacts) {
                if (!contacts.contains(contact)) {
                    contacts.add(contact);
                }
            }
            contactsUpdated.event();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportContacts(String location) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(location), contacts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}
