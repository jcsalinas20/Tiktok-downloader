package application.downloadList;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import static application.Main.screenDownloaderList;
import static application.Main.frameSaveAsFile;
import static css.StylesCSS.errorTextField;
import static css.StylesCSS.defaultTextField;

public class SaveAsFile  extends Application implements Initializable {

    /************************* VARIABLES FXML **************************/

    @FXML
    TextField tfPath;

    @FXML
    TextField tfNameFile;

    @FXML
    Button btnExamine;

    @FXML
    Button btnSave;

    @FXML
    Button btnCancel;

    @FXML
    Label lblFinalPath;

    /**************************** VARIABLES ****************************/

    public static String fileName;
    public static ObservableList<String> urls;
    boolean[] fieldInRed = new boolean[] {false, false};

    /***************************** METHODS *****************************/

    private void createDirectoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        btnExamine.setOnAction(e -> {
            if (new File(tfPath.getText()).getParentFile().exists()) {
                directoryChooser.setInitialDirectory(new File(tfPath.getText()).getParentFile());
            } else {
                directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")).getParentFile());
            }
            File selectedDirectory = directoryChooser.showDialog(screenDownloaderList);
            if (selectedDirectory != null) {
                tfPath.setText(selectedDirectory.getAbsolutePath());
                changeAbsolutePathFile();
            }
        });
    }

    public void showAlert(Alert.AlertType type, String title, String context) {
        Alert alert = new Alert(null);
        alert.setAlertType(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);
        alert.show();
    }

    public boolean showAlert(String title, String context) {
        Alert alert = new Alert(null);
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);
        Optional<ButtonType> result = alert.showAndWait();
        return result.filter(buttonType -> buttonType == ButtonType.OK).isPresent();
    }

    private void changeAbsolutePathFile() {
        lblFinalPath.setText(tfPath.getText() + File.separator + tfNameFile.getText() + ".txt");
    }

    private TextFormatter.Change verifyLength(TextFormatter.Change change) {
        int newLength = change.getControlNewText().length();
        if (newLength > 30) {
            String tail = change.getControlNewText().substring(0, 30);
            change.setText(tail);
            int oldLength = change.getControlText().length();
            change.setRange(0, oldLength);
        }
        return change;
    }

    private void tfFileNameOnKeyTyped() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (change.isAdded()) {
                String addedText = change.getText();
                if (addedText.matches("[a-zA-Z0-9-_.]+")) {
                    return verifyLength(change);
                }
                // remove illegal characters:
                addedText = addedText.replaceAll("[^a-zA-Z0-9-_.]", "");
                change.setText(addedText);
            }
            return verifyLength(change);
        };
        tfNameFile.setTextFormatter(new TextFormatter<String>(filter));
    }

    private boolean isEmpty(TextField field) {
        String text = field.getText().replaceAll(" ", "");
        return text.length() == 0;
    }

    private boolean verifyTextFields() {
        boolean allCorrect = true;
        if (isEmpty(tfPath)) {
            allCorrect = false;
            fieldInRed[0] = true;
            tfPath.setText("");
            errorTextField(tfPath);
        }
        if (isEmpty(tfNameFile)) {
            allCorrect = false;
            fieldInRed[1] = true;
            tfNameFile.setText("");
            errorTextField(tfNameFile);
        }
        return allCorrect;
    }

    private boolean verifyFieldPath(String text) {
        File path = new File(text);
        if (path.exists())
            if (path.isDirectory()) return true;
            else showAlert(Alert.AlertType.ERROR, "Error in Text Field Path", "The path of the field is a file");
        else showAlert(Alert.AlertType.ERROR, "Error in Text Field Path", "The path of the field doesn't exist");
        return false;
    }

    private void createFile(String text) {
        File file = new File(text);
        if (!file.exists()) {
            try {
                boolean createFileStatus = file.createNewFile();
                PrintStream ps = new PrintStream(file);
                for (String url : urls) ps.println(url);
                ps.close();
                if (createFileStatus)
                    showAlert(Alert.AlertType.INFORMATION, "File export", "The file "+file.getName()+" has been saved in \n\r"+file.getAbsolutePath());
                else
                    showAlert(Alert.AlertType.ERROR, "File export", "Error to export the file\n\r"+file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            boolean status = showAlert("File export", "The file "+file.getAbsolutePath()+" already exists.\n\rDo you want to replace the file?");
            if (status) {
                try {
                    PrintStream ps = new PrintStream(file);
                    for (String url : urls) ps.println(url);
                    ps.close();
                    showAlert(Alert.AlertType.INFORMATION, "File export", "The file "+file.getName()+" has been saved in \n\r"+file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*************************** METHODS FXML***************************/

    @FXML
    private void btnSaveOnClick() {
        if (verifyTextFields()) {
            if (!verifyFieldPath(tfPath.getText())) return;
            createFile(lblFinalPath.getText());
        }
    }

    @FXML
    private void btnCancelOnClick() {
        frameSaveAsFile.close();
    }

    @FXML
    private void tfNameKeyReleased() {
        if (fieldInRed[1]) {
            fieldInRed[1] = false;
            defaultTextField(tfNameFile);
        }
        changeAbsolutePathFile();
    }

    @FXML
    private void tfPathKeyReleased() {
        if (fieldInRed[0]) {
            fieldInRed[0] = false;
            defaultTextField(tfPath);
        }
        changeAbsolutePathFile();
    }

    /************************ METHOD INITIALIZE ************************/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfPath.setText("/home/zell_zdark/Imágenes/my_videos/TIKTOK");
        //tfPath.setText(System.getProperty("user.home"));

        createDirectoryChooser();

        tfFileNameOnKeyTyped();

        tfNameFile.setText(fileName);
        lblFinalPath.setText(tfPath.getText() + File.separator + tfNameFile.getText() + ".txt");
    }

    @Override
    public void start(Stage primaryStage) {
    }

}
