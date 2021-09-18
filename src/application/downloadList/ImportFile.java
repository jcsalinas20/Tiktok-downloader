package application.downloadList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

import static application.downloadList.DownloaderList.itemsListView;
import static application.Main.screenDownloaderList;
import static application.Main.frameImportFile;

public class ImportFile extends Application implements Initializable {

    /************************* VARIABLES FXML **************************/

    @FXML
    ScrollPane scrollPane;

    @FXML
    TextField tfPath;

    @FXML
    TextFlow tFlowImports;

    @FXML
    Button btnExamine;

    @FXML
    CheckBox cbIgnore;

    @FXML
    Button btnCancel;

    @FXML
    Button btnImport;

    /**************************** VARIABLES ****************************/

    ObservableList<Node> listTextFlow;
    DownloaderList downloaderList = new DownloaderList();

    /***************************** METHODS *****************************/

    private void createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        btnExamine.setOnAction(e -> {
            if (new File(tfPath.getText()).getParentFile().exists()) {
                fileChooser.setInitialDirectory(new File(tfPath.getText()).getParentFile());
            } else {
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + File.separator + ".txt").getParentFile());
            }
            File selectedFile = fileChooser.showOpenDialog(screenDownloaderList);
            if (selectedFile != null)
                tfPath.setText(selectedFile.getAbsolutePath());
        });
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

    private void createText(String clazz, String text) {
        Text textLabel = new Text();
        textLabel.getStyleClass().add(clazz);
        textLabel.setText(text);
        listTextFlow.add(textLabel);
    }

    private void setInstructionTextFlow() {
        createText("title", "Instructions:\n");
        createText("normal-text", "File extension: .txt\n");
        createText("normal-text", "Example of the URL structure:\n");
        createText("normal-text", "\t-https://www.tiktok.com/@aaa/video/999\n");
        createText("normal-text", "\t-https://vm.tiktok.com/vYGJYb/\n");
        createText("normal-text", "URL separated by line break.\n");
    }

    private boolean validateUrlTikTok(String url){
        url = url.split("\\?")[0];
        boolean validUrl = url.matches("^https://www.tiktok.com/@[a-zA-Z0-9-_.]+/video/[0-9]+");
        if (!validUrl) validUrl = url.matches("^https://vm.tiktok.com/[a-zA-Z0-9]+/");
        if (!validUrl) validUrl = url.matches("^https://vm.tiktok.com/[a-zA-Z0-9]+");
        return validUrl;
    }

    /*************************** METHODS FXML***************************/

    @FXML
    private void btnImportOnClick() {
        File file = new File(tfPath.getText());
        if (file.exists()) {
            boolean confirm = true;
            if (itemsListView.size() > 0) confirm = showAlert("New List Urls", "The content of the list is not empty.\nAre you sure you want to remove the links?");
            if (confirm) {
                ObservableList<String> newListUrl = FXCollections.observableArrayList();
                Map<String, String> linksMap = new HashMap<>();
                listTextFlow.clear();
                setInstructionTextFlow();
                createText("title", "\nStart Import\n");
                createText("success-text", "File "+file.getAbsolutePath()+" exists.\n");
                createText("normal-text", "Opening "+file.getName()+"\n");
                try {
                    int index = 1;
                    boolean ignoreErrors = cbIgnore.isSelected();
                    boolean errorFound = false;
                    Scanner lector = new Scanner(file);
                    while (lector.hasNextLine()) {
                        String url = lector.nextLine().split("\\?")[0];
                        String[] separateUrl = url.split("/");
                        String id = separateUrl[separateUrl.length-1];
                        if (validateUrlTikTok(url)) {
                            if (!linksMap.containsKey(id)) {
                                linksMap.put(id, url);
                                createText("success-text", index+":\t"+url+"\n");
                            } else createText("error-text", index+":\t"+url+" - duplicate URL\n");
                            //createText("error-text", index+":\t"+url+" - broken URL\n");
                        } else {
                            createText("error-text", index+":\t"+url+" - random text\n");
                            if (!ignoreErrors) {
                                errorFound = true;
                                break;
                            }
                        }
                        index++;
                    }
                    lector.close();
                    if (errorFound) {
                        createText("title-error", "Stop Import\n");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                TreeMap<String, String> sorted = new TreeMap<>(linksMap);
                for (String i : sorted.keySet())
                    newListUrl.add(sorted.get(i));
                downloaderList.addNewList(newListUrl);
            }
        }
    }

    @FXML
    private void btnCancelOnClick() {
        frameImportFile.close();
    }

    /************************ METHODS INITIALIZE ************************/

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tfPath.setText("/home/zell_zdark/Imágenes/my_videos/TIKTOK/.txt");
        //tfPath.setText(System.getProperty("user.home") + File.separator + ".txt");
        createFileChooser();

        listTextFlow = tFlowImports.getChildren();
        setInstructionTextFlow();

        tFlowImports.heightProperty().addListener(observable -> scrollPane.setVvalue(1.0));
        scrollPane.setVvalue(1.0);
    }

    @Override
    public void start(Stage primaryStage) {
    }

}
