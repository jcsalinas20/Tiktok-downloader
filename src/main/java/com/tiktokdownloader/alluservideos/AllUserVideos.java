package com.tiktokdownloader.alluservideos;

import com.tiktokdownloader.Main;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import static com.tiktokdownloader.Main.screenDownloaderList;

public class AllUserVideos extends Application implements Initializable {

    /************************* VARIABLES FXML **************************/

    @FXML
    Menu menuDownloadList;

    @FXML
    Menu menuAllUserVideos;

    @FXML
    Menu menuOneVideo;

    @FXML
    Menu menuFileGenerator;

    @FXML
    Menu menuLogs;

    @FXML
    TextField tfTikTokName;

    @FXML
    TextField tfPath;

    @FXML
    Button btnExamine;

    @FXML
    ImageView imageLogo;

    @FXML
    ScrollPane scrollPane;

    @FXML
    TextFlow tFlowImports;

    @FXML
    Button btnStart;

    @FXML
    Button btnStop;

    /**************************** VARIABLES ****************************/

    Main main = new Main();
    ObservableList<Node> listTextFlow;
    public static Thread thread;

    /***************************** METHODS *****************************/

    private ArrayList<String> getNameFiles(String path) {
        ArrayList<String> files = new ArrayList<>();
        File folder = new File(path);
        File[] allFiles = folder.listFiles();
        assert allFiles != null;
        for (File file: allFiles) {
            if (file.isFile()) {
                String[] nameSplit = file.getName().split("\\.");
                String extensionFile = nameSplit[nameSplit.length - 1];
                if (extensionFile.equalsIgnoreCase("mp4") && nameSplit.length == 2) {
                    if (nameSplit[0].matches("[0-9]+")) {
                        files.add(nameSplit[0]);
                    }
                }
            }
        }
        return files;
    }

    private String createFile(String name, String path) {
        path = path.replaceAll("\\\\", "/");
        path = (path.endsWith("/")) ? path : path + "/" ;
        File file = new File(path + name + ".txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
                Platform.runLater(() -> createText("success-text", "File " + name +".txt created\n"));
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> createText("error-text", "Error creating file\n"));
            }
        } else {
            Platform.runLater(() -> createText("success-text", "File " + name +".txt replaced\n"));
        }
        return file.getAbsolutePath();
    }

    private void printInFile(String destinyFile, String name, ArrayList<String> files) {
        try {
            PrintStream ps = new PrintStream(destinyFile);
            for (String file: files) {
                ps.println("https://www.tiktok.com/@" + name + "/video/" + file);
                Platform.runLater(() -> createText("success-text", "https://www.tiktok.com/@" + name + "/video/" + file + "\n"));
            }
            ps.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void createImageLogo() {
        URL localUrl = Objects.requireNonNull(getClass().getClassLoader().getResource("logo.png"));
        Image image = new Image(localUrl.toString());
        imageLogo.setImage(image);
    }

    private void createDirectoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        btnExamine.setOnAction(e -> {
            if (new File(tfPath.getText()).exists()) {
                directoryChooser.setInitialDirectory(new File(tfPath.getText()));
            } else {
                directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            }
            File selectedDirectory = directoryChooser.showDialog(screenDownloaderList);
            if (selectedDirectory != null)
                tfPath.setText(selectedDirectory.getAbsolutePath());
        });
    }

    private void tfTikTokNameOnKeyTyped() {
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
        tfTikTokName.setTextFormatter(new TextFormatter<String>(filter));
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

    private void createText(String clazz, String text) {
        Text textLabel = new Text();
        textLabel.getStyleClass().add(clazz);
        textLabel.setText(text);
        listTextFlow.add(textLabel);
    }

    /*************************** METHODS FXML***************************/

    @FXML
    private void onClickStartDownload() {
        if (!tfTikTokName.getText().isBlank()) {
            if (!tfPath.getText().isBlank()) {
                File folder = new File(tfPath.getText());
                if (folder.exists()) {
                    if (folder.isDirectory()) {
                        btnStart.setDisable(true);
                        btnStop.setDisable(false);
                        ArrayList<String> files = getNameFiles(tfPath.getText());
                        String destinyFile = createFile(tfTikTokName.getText(), tfPath.getText());
                        printInFile(destinyFile, tfTikTokName.getText(), files);
                        Platform.runLater(() -> createText("title", "Finished\n"));
                    } else {
                        Alert alert = new Alert(null);
                        alert.setAlertType(Alert.AlertType.ERROR);
                        alert.setTitle("File Generator Error");
                        alert.setHeaderText(null);
                        alert.setContentText("This path is a file.");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(null);
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("File Generator Error");
                    alert.setHeaderText(null);
                    alert.setContentText("This path doesn't exist.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(null);
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle("File Generator Error");
                alert.setHeaderText(null);
                alert.setContentText("Destiny path is blank.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(null);
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle("File Generator Error");
            alert.setHeaderText(null);
            alert.setContentText("Tiktoker username is blank.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onClickStop() {
        btnStop.setDisable(true);

        thread.interrupt();
        tFlowImports.getChildren().clear();
        listTextFlow = tFlowImports.getChildren();
        tFlowImports.heightProperty().addListener(observable -> scrollPane.setVvalue(1.0));
        scrollPane.setVvalue(1.0);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                Platform.runLater(() -> createText("title", "Download All User Videos\n"));
                Platform.runLater(() -> createText("normal-text", "URL: https://www.tiktok.com/@{Username}\n"));
                Platform.runLater(() -> createText("success-text", "Waiting...\n"));
                return null;
            }
        };
        thread = new Thread(task);
        thread.start();

        btnStart.setDisable(false);
    }

    /************************ METHODS INITIALIZE ************************/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnStart.setDisable(true);
        btnStop.setDisable(true);
        btnExamine.setDisable(true);
        tfPath.setDisable(true);
        tfTikTokName.setDisable(true);
        createImageLogo();
        createDirectoryChooser();

        tfTikTokNameOnKeyTyped();
        tfPath.setText(System.getProperty("user.home"));

        Label menuDownloadListLabel = new Label("Download List");
        menuDownloadListLabel.setOnMouseClicked(event -> main.showScreenDownloadList());
        menuDownloadList.setGraphic(menuDownloadListLabel);

        Label menuAllUserVideosLabel = new Label("All User Videos");
        menuAllUserVideos.setGraphic(menuAllUserVideosLabel);

        Label menuOneVideoLabel = new Label("One Video");
        menuOneVideoLabel.setOnMouseClicked(event -> main.showScreenOneVideo());
        menuOneVideo.setGraphic(menuOneVideoLabel);

        Label menuFileGeneratorLabel = new Label("File Generator");
        menuFileGeneratorLabel.setOnMouseClicked(event -> main.showScreenFileGenerator());
        menuFileGenerator.setGraphic(menuFileGeneratorLabel);

        Label menuLogsLabel = new Label("Logs");
        menuLogsLabel.setOnMouseClicked(event -> main.showScreenLogs());
        menuLogs.setGraphic(menuLogsLabel);

        listTextFlow = tFlowImports.getChildren();
        tFlowImports.heightProperty().addListener(observable -> scrollPane.setVvalue(1.0));
        scrollPane.setVvalue(1.0);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                Platform.runLater(() -> createText("title", "Developing :)\n"));
                /*
                Platform.runLater(() -> createText("title", "Download All User Videos\n"));
                Platform.runLater(() -> createText("normal-text", "URL: https://www.tiktok.com/@{Username}\n"));
                Platform.runLater(() -> createText("success-text", "Waiting...\n"));
                */
                return null;
            }
        };
        thread = new Thread(task);
        thread.start();
    }

    @Override
    public void start(Stage primaryStage) {}
}
