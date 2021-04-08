import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ContactsImportExportWindowController implements ChildController {
    @FXML private Label titleLabel;
    @FXML private TextField fileField;
    private final FileChooser fileChooser;
    private final boolean forImport;
    private final MainWindowController mainController;

    public ContactsImportExportWindowController(boolean forImport, MainWindowController mainController) {
        this.forImport = forImport;
        this.mainController = mainController;
        fileChooser = new FileChooser();
    }

    @FXML private void chooseFile() {
        File file = fileChooser.showOpenDialog(titleLabel.getScene().getWindow());
        if (file != null) {
            fileField.setText(file.getAbsolutePath());
        }
    }

    @FXML private void ok() {
        if (new File(fileField.getText()).exists()) {
            if (forImport) {
                mainController.importContacts(fileField.getText());
            } else {
                mainController.exportContacts(fileField.getText());
            }
            ((Stage) titleLabel.getScene().getWindow()).close();
        }
    }

    @FXML private void cancel() {
        ((Stage) titleLabel.getScene().getWindow()).close();
    }

    @Override public void init() {
        if (forImport) {
            titleLabel.setText("Импорт контактов");
        } else {
            titleLabel.setText("Экспорт контактов");
        }
    }
}
