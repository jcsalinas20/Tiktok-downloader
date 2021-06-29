package application.downloadList;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import application.Main;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import static application.Main.screenDownloaderList;
import static css.StylesCSS.defaultTextField;
import static css.StylesCSS.errorTextField;

public class DownloaderList extends Application implements Initializable {

    /************************* VARIABLES FXML **************************/

    @FXML
    Menu menuDownloadList;

    @FXML
    Menu menuAllUserVideos;

    @FXML
    Menu menuOneVideo;

    @FXML
    ListView<String> listLinks = new ListView<>();

    @FXML
    TextField tfTikTokName;

    @FXML
    TextField tfUrl;

    @FXML
    TextField tfPath;

    @FXML
    Label lblError;

    @FXML
    Button btnExamine;

    @FXML
    Button btnSaveAsFile;

    @FXML
    Button btnImportFile;

    @FXML
    Button btnDeleteRow;

    @FXML
    Button btnInsertRow;

    @FXML
    Button btnStartScraping;

    @FXML
    ImageView imageLogo;

    /**************************** VARIABLES ****************************/

    Main main = new Main();
    static ObservableList<String> itemsListView = FXCollections.observableArrayList();
    boolean[] fieldInRed = new boolean[] {false, false};

    /***************************** METHODS *****************************/

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

    private boolean validateUrlTikTok(String url){
        url = url.split("\\?")[0];
        boolean validUrl = url.matches("^https://www.tiktok.com/@[a-zA-Z0-9-_.]+/video/[0-9]+");
        if (!validUrl) validUrl = url.matches("^https://vm.tiktok.com/[a-zA-Z0-9]+/");
        if (!validUrl) validUrl = url.matches("^https://vm.tiktok.com/[a-zA-Z0-9]+");
        return validUrl;
    }

    private void checkStatusUrl(String url) {
        // LLAMAR A UNA API PARA SABER SI ESE VIDEO EXISTE Y COMPROBAR Y EL AUTOR ES EL MISMO QUE EL DE LA URL
        try {
            URL u = new URL(url);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");  //OR  huc.setRequestMethod ("HEAD");
            huc.connect();
            System.out.println(huc.getURL());
            System.out.println(huc.getResponseMessage() + " - " + huc.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUrlErrorType(String msg) {
        lblError.setText(msg);
        final Timeline animation = new Timeline(
            new KeyFrame(
                    Duration.seconds(3),
                    actionEvent -> lblError.setText("")
            )
        );
        animation.setCycleCount(1);
        animation.play();
    }

    private void insertRow() {
        String url = tfUrl.getText().split("\\?")[0];
        // checkStatusUrl(url);
        if (validateUrlTikTok(url)) {
            if (!listLinks.getItems().contains(url)) {
                listLinks.getItems().add(url);
                tfUrl.setPromptText(url);
            } else setUrlErrorType("This URL is in the list.");
            tfUrl.setText("");
        } else setUrlErrorType("Invalid TikTok URL");
    }

    private void tfUrlKeyListener() {
        tfUrl.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) insertRow();
        });
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

    private void createImageLogo() {
        URL localUrl = Objects.requireNonNull(getClass().getClassLoader().getResource("images/logo.png"));
        Image image = new Image(localUrl.toString());
        imageLogo.setImage(image);
    }

    private void showAlert(String title, String context) {
        Alert alert = new Alert(null);
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);
        alert.show();
    }

    public void addNewList(ObservableList<String> newItems) {
        itemsListView.setAll(newItems);
        listLinks.setItems(itemsListView);
    }

    private boolean isEmpty(TextField field) {
        String text = field.getText().replaceAll(" ", "");
        return text.length() == 0;
    }

    private boolean verifyTextFields() {
        boolean allCorrect = true;
        if (isEmpty(tfTikTokName)) {
            allCorrect = false;
            fieldInRed[0] = true;
            tfTikTokName.setText("");
            errorTextField(tfTikTokName);
        }
        if (isEmpty(tfPath)) {
            allCorrect = false;
            fieldInRed[1] = true;
            tfPath.setText("");
            errorTextField(tfPath);
        }
        return allCorrect;
    }

    private void setDefaultColorsTextField(int index, TextField field) {
        if (fieldInRed[index]) {
            fieldInRed[index] = false;
            defaultTextField(field);
        }
    }

    /*************************** METHODS FXML***************************/

    @FXML
    private void btnDownloadListOnClick() {
        if (listLinks.getItems().size() > 0) {
            if (verifyTextFields()) {
                File folder = new File(tfPath.getText());
                if (folder.exists()) {
                    if (folder.isDirectory()) {
                        StartDownload.file = new File(folder.getAbsolutePath()+File.separator+tfTikTokName.getText()+File.separator+"._tiktok.txt");
                        StartDownload.listUrl = listLinks.getItems();
                        main.showFrameStartDownload();
                    } else {
                        fieldInRed[1] = true;
                        showAlert("Error in the path", "The path of the field is a file.");
                        errorTextField(tfPath);
                    }
                } else {
                    fieldInRed[1] = true;
                    showAlert("Error in the path", "The path of the field doesn't exists.");
                    errorTextField(tfPath);
                }
            }
        } else showAlert("Error in the list", "The list is empty.");
    }

    @FXML
    private void btnImportFileOnClick() {
        main.showFrameImportFile();
    }

    @FXML
    private void btnSaveAsFileOnClick() {
        if (listLinks.getItems().size() > 0) {
            SaveAsFile.fileName = tfTikTokName.getText();
            SaveAsFile.urls = listLinks.getItems();
            main.showFrameSaveAsFile();
        } else showAlert("Error in the List", "The list of the links have 0 items.");
    }

    @FXML
    private void btnInsertRowOnClick() { insertRow(); }

    @FXML
    private void btnDeleteRowOnClick() {
        ObservableList<Integer> positions = listLinks.getSelectionModel().getSelectedIndices();
        for (int i = positions.size() - 1; i >= 0; i--)
            listLinks.getItems().remove((int) positions.get(i));
    }

    @FXML
    private void openSelectedLink() {
        ObservableList<Integer> positions = listLinks.getSelectionModel().getSelectedIndices();
        for (Integer position : positions) {
            new Thread(() -> {
                try {
                    listLinks.getSelectionModel().clearAndSelect(position);
                    URI link = new URI(listLinks.getItems().get(position));
                    Desktop.getDesktop().browse(link);
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }).start();
            break;
        }
    }

    @FXML
    private void copySelectedLink() {
        ObservableList<Integer> positions = listLinks.getSelectionModel().getSelectedIndices();
        for (Integer position : positions) {
            listLinks.getSelectionModel().clearAndSelect(position);
            StringSelection link = new StringSelection(listLinks.getItems().get(position));
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(link, null);
            break;
        }
    }

    @FXML
    private void deleteSelectedLink() {
        ObservableList<Integer> positions = listLinks.getSelectionModel().getSelectedIndices();
        for (int i = positions.size() - 1; i >= 0; i--)
            listLinks.getItems().remove((int) positions.get(i));
    }

    @FXML
    private void deleteAllLinks() {
        listLinks.getItems().clear();
    }

    @FXML
    private void tfNameKeyReleased() {
        setDefaultColorsTextField(0, tfTikTokName);
    }

    @FXML
    private void tfPathKeyReleased() {
        setDefaultColorsTextField(1, tfPath);
    }

    /************************ METHODS INITIALIZE ************************/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createImageLogo();
        createDirectoryChooser();

        tfTikTokNameOnKeyTyped();
        tfUrlKeyListener();

        tfPath.setText(System.getProperty("user.home"));
        //tfPath.setText(System.getProperty("user.home")+"/Imágenes/my_videos/TIKTOK/");

        listLinks.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listLinks.getItems().clear();
        listLinks.setItems(itemsListView);

        Label menuDownloadListLabel = new Label("Download List");
        menuDownloadList.setGraphic(menuDownloadListLabel);

        Label menuAllUserVideosLabel = new Label("All User Videos");
        menuAllUserVideosLabel.setOnMouseClicked(event -> main.showScreenAllUserVideos());
        menuAllUserVideos.setGraphic(menuAllUserVideosLabel);

        Label menuOneVideoLabel = new Label("One Video");
        menuOneVideoLabel.setOnMouseClicked(event -> main.showScreenOneVideo());
        menuOneVideo.setGraphic(menuOneVideoLabel);

        /*
        tfTikTokName.setText("test");
        tfUrl.setText("https://www.tiktok.com/@nat_dancer2/video/6977774170515967237");
        insertRow();
        tfUrl.setText("https://www.tiktok.com/@lynaperezz/video/6978205406682287365");
        insertRow();
         */
    }

    @Override
    public void start(Stage primaryStage) {
    }

}
