package application.downloadList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static application.Main.*;

public class StartDownload extends Application implements Initializable {

    /************************* VARIABLES FXML **************************/

    @FXML
    ScrollPane scrollPane;

    @FXML
    TextFlow tFlowImports;

    @FXML
    Button btnOpenFolder;

    @FXML
    Label lblFolderPath;

    /**************************** VARIABLES ****************************/

    public static File file;
    public static ObservableList<String> listUrl;
    ObservableList<Node> listTextFlow;
    public static Thread thread;

    /***************************** METHODS *****************************/

    public boolean showAlert(String title, String context) {
        Alert alert = new Alert(null);
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);
        Optional<ButtonType> result = alert.showAndWait();
        return result.filter(buttonType -> buttonType == ButtonType.OK).isPresent();
    }

    public void showErrorAlert(String title, String context) {
        Alert alert = new Alert(null);
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);
        alert.showAndWait();
    }

    private void createText(String clazz, String text) {
        Text textLabel = new Text();
        textLabel.getStyleClass().add(clazz);
        textLabel.setText(text);
        listTextFlow.add(textLabel);
    }

    private void closeWindow() {
        frameStartDownload.close();
    }

    private boolean createFolder(File file) {
        if (!file.getParentFile().exists())
            return file.getParentFile().mkdirs();
        return true;
    }

    private void readListToStartCommand(File file) {
        for (String link : StartDownload.listUrl)
            runCommand("cmd", "/C", "tiktok-scraper video "+link+" -d -w false --filepath '"+file.getParent()+"'");
    }

    private void runCommand(String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder().command(command);
        try {
            Process process = processBuilder.start();

            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String output;
            while ((output = bufferedReader.readLine()) != null) {
                System.out.println(output);
                String finalOutput = output;
                Platform.runLater(() -> createText("normal-text", finalOutput +"\n"));
            }
            process.waitFor();

            bufferedReader.close();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*************************** METHODS FXML***************************/

    @FXML
    private void btnOpenFolderOnClick() {
        try {
            Desktop.getDesktop().open(new File(lblFolderPath.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /************************ METHODS INITIALIZE ************************/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblFolderPath.setText(file.getParent());

        listTextFlow = tFlowImports.getChildren();
        tFlowImports.heightProperty().addListener(observable -> scrollPane.setVvalue(1.0));
        scrollPane.setVvalue(1.0);

        Task<Void> task = new Task<Void>() {
            @Override protected Void call() {
                Platform.runLater(() -> createText("title", "Start Download\n"));
                if (createFolder(StartDownload.file))
                    readListToStartCommand(StartDownload.file);
                else  {
                    showErrorAlert("Start Download Error", "Error to create destination folder");
                    closeWindow();
                }
                Platform.runLater(() -> createText("title", "Finished\n"));
                Platform.runLater(() -> {
                    boolean status = showAlert("Finished", "The download is finished\nDo you want to close the window?");
                    if (status) closeWindow();
                });
                return null;
            }
        };
        thread = new Thread(task);
        thread.start();
    }

    @Override
    public void start(Stage primaryStage) {
    }

}