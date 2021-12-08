package com.tiktokdownloader.filegenerator;

import com.tiktokdownloader.Main;
import com.tiktokdownloader.downloadlist.StartDownload;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.function.UnaryOperator;

import static com.tiktokdownloader.Main.screenDownloaderList;

public class FileGenerator extends Application implements Initializable {

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
    Button btnCreateFile;

    @FXML
    Button btnRestart;

    @FXML
    ComboBox<String> cbTypeFile;

    Main main = new Main();
    ObservableList<Node> listTextFlow;
    ObservableList<String> listTypeFile;
    public static Thread thread;

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

    @FXML
    private void onClickGenerateFile() {
        if (!tfTikTokName.getText().isBlank()) {
            if (!tfPath.getText().isBlank()) {
                File folder = new File(tfPath.getText());
                if (folder.exists()) {
                    if (folder.isDirectory()) {
                        btnCreateFile.setDisable(true);
                        if (cbTypeFile.getSelectionModel().getSelectedIndex() == 0 || cbTypeFile.getSelectionModel().getSelectedIndex() == -1) {
                            ArrayList<String> files = getNameFiles(tfPath.getText(), "mp4");
                            String destinyFile = createFile(tfTikTokName.getText(), tfPath.getText());
                            ArrayList<String> tiktokVideos = new ArrayList<>();
                            for (String file: files) {
                                tiktokVideos.add("https://www.tiktok.com/@" + tfTikTokName.getText() + "/video/" + file);
                            }
                            printInFile(destinyFile, tiktokVideos);
                        } else {
                            ArrayList<String> files = getNameFiles(tfPath.getText(), "txt");
                            String path = tfPath.getText();
                            path = path.replaceAll("\\\\", "/");
                            path = (path.endsWith("/")) ? path : path + "/" ;
                            new File( path + tfTikTokName.getText() + ".txt").delete();
                            String destinyFile = createFile(tfTikTokName.getText(), tfPath.getText());
                            ArrayList<String> tiktokVideos = readAllFiles(tfPath.getText(), files);
                            printInFile(destinyFile, tiktokVideos);
                        }
                        Platform.runLater(() -> createText("title", "Finished\n"));
                        btnRestart.setDisable(false);
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

    private ArrayList<String> readAllFiles(String path, ArrayList<String> files) {
        ArrayList<String> urlVideos = new ArrayList<>();
        path = path.replaceAll("\\\\", "/");
        path = (path.endsWith("/")) ? path : path + "/" ;
        for (String name : files) {
            File file = new File(path + name);
            if (file.exists()) {
                try {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        urlVideos.add(scanner.nextLine());
                    }
                    scanner.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return urlVideos;
    }

    private ArrayList<String> getNameFiles(String path, String extension) {
        ArrayList<String> files = new ArrayList<>();
        File folder = new File(path);
        File[] allFiles = folder.listFiles();
        assert allFiles != null;
        for (File file: allFiles) {
            if (file.isFile()) {
                String[] nameSplit = file.getName().split("\\.");
                String extensionFile = nameSplit[nameSplit.length - 1];
                if (extension.equals("mp4")) {
                    if (extensionFile.equalsIgnoreCase("mp4") && nameSplit.length == 2) {
                        if (nameSplit[0].matches("[0-9]+")) {
                            files.add(nameSplit[0]);
                        }
                    }
                } else if (extension.equals("txt")) {
                    if (extensionFile.equalsIgnoreCase("txt")) {
                        files.add(file.getName());
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

    private void printInFile(String destinyFile, ArrayList<String> tiktoks) {
        try {
            PrintStream ps = new PrintStream(destinyFile);
            for (String tiktok: tiktoks) {
                ps.println(tiktok);
                Platform.runLater(() -> createText("success-text", tiktok + "\n"));
            }
            ps.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClickRestart() {
        btnRestart.setDisable(true);

        thread.interrupt();
        tFlowImports.getChildren().clear();
        listTextFlow = tFlowImports.getChildren();
        tFlowImports.heightProperty().addListener(observable -> scrollPane.setVvalue(1.0));
        scrollPane.setVvalue(1.0);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                Platform.runLater(() -> createText("title", "Text File Generator\n"));
                Platform.runLater(() -> createText("normal-text", "URL: https://www.tiktok.com/@{Username}/video/{ID}\n"));
                Platform.runLater(() -> createText("success-text", "Waiting...\n"));
                return null;
            }
        };
        thread = new Thread(task);
        thread.start();

        btnCreateFile.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createImageLogo();
        createDirectoryChooser();

        cbTypeFile.setItems(FXCollections.observableArrayList("File Generator", "Merge Files"));
        tfTikTokNameOnKeyTyped();
        tfPath.setText(System.getProperty("user.home"));
        tfPath.setText("C:\\Users\\jcsal\\Downloads\\cosas\\__FILES\\TEMP\\merge");
        tfTikTokName.setText("TEMP");

        Label menuDownloadListLabel = new Label("Download List");
        menuDownloadListLabel.setOnMouseClicked(event -> main.showScreenDownloadList());
        menuDownloadList.setGraphic(menuDownloadListLabel);

        Label menuAllUserVideosLabel = new Label("All User Videos");
        menuAllUserVideosLabel.setOnMouseClicked(event -> main.showScreenAllUserVideos());
        menuAllUserVideos.setGraphic(menuAllUserVideosLabel);

        Label menuOneVideoLabel = new Label("One Video");
        menuOneVideoLabel.setOnMouseClicked(event -> main.showScreenOneVideo());
        menuOneVideo.setGraphic(menuOneVideoLabel);

        Label menuFileGeneratorLabel = new Label("File Generator");
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
                Platform.runLater(() -> createText("title", "Text File Generator\n"));
                Platform.runLater(() -> createText("normal-text", "URL: https://www.tiktok.com/@{Username}/video/{ID}\n"));
                Platform.runLater(() -> createText("success-text", "Waiting...\n"));
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
