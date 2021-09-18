package application.downloadList;

import application.DataBaseIds;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    int numLine = 0;

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
        String commandOS = (System.getProperty("os.name").equals("Linux")) ? "/bin/bash" : "cmd";
        String options = (System.getProperty("os.name").equals("Linux")) ? "-c" : "/C";
        String path = file.getParent();
        if (path.contains(" ")) path = "\"" + path;
        numLine = 1;
        boolean nodeInstalled = checkNodeJS(commandOS, options, "node -v");
        boolean npmInstalled = checkNpm(commandOS, options, "npm -v");
        boolean tiktokScraperInstalled = checkTiktokScraper(commandOS, options, "tiktok-scraper --version");
        if (nodeInstalled && npmInstalled && tiktokScraperInstalled)
            for (String link : StartDownload.listUrl)
                runCommand(commandOS, options, "tiktok-scraper video "+link+" -d -w false --filepath '"+path+"'");
    }

    private boolean checkTiktokScraper(String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder().command(command);
        try {
            Process process = processBuilder.start();

            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String output = bufferedReader.readLine();
            boolean status = false;

            if (output != null) {
                Platform.runLater(() -> createText("normal-text", "TikTok Scraper: "));
                Platform.runLater(() -> createText("success-text", "installed \n"));
                status = true;
            }else {
                Platform.runLater(() -> createText("error-text", "TikTok Scraper is not installed.\n"));
                Platform.runLater(() -> createText("error-text", "Run "));
                Platform.runLater(() -> createText("normal-text", "npm i -g tiktok-scraper\n"));
            }

            process.waitFor();
            bufferedReader.close();
            process.destroy();
            return status;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkNpm(String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder().command(command);
        try {
            Process process = processBuilder.start();

            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String output = bufferedReader.readLine();
            boolean status = false;

            if (output != null) {
                Platform.runLater(() -> createText("normal-text", "Npm version: " + output + "\n"));
                status = true;
            }else {
                Platform.runLater(() -> createText("error-text", "Npm is not installed.\n"));
                Platform.runLater(() -> createText("error-text", "Run "));
                Platform.runLater(() -> createText("normal-text", "sudo apt install npm\n"));
            }

            process.waitFor();
            bufferedReader.close();
            process.destroy();
            return status;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkNodeJS(String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder().command(command);
        try {
            Process process = processBuilder.start();

            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String output = bufferedReader.readLine();
            boolean statusNode = false;

            if (output != null) {
                Platform.runLater(() -> createText("normal-text", "Node version: " + output + "\n"));
                statusNode = true;
            }else {
                Platform.runLater(() -> createText("error-text", "Node is not installed.\n"));

                if (System.getProperty("os.name").equals("Linux")) {
                    Platform.runLater(() -> createText("error-text", "Run "));
                    Platform.runLater(() -> createText("normal-text", "sudo apt install nodejs\n"));
                } else {
                    Platform.runLater(() -> createText("error-text", "Download and install.\n"));
                    Platform.runLater(() -> createText("normal-text", "<i>https://nodejs.org/en/download/</i>\n"));
                }
            }

            process.waitFor();
            bufferedReader.close();
            process.destroy();
            return statusNode;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static final SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private void runCommand(String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder().command(command);
        try {
            Process process = processBuilder.start();

            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String output = bufferedReader.readLine();
            String link = command[2].split("tiktok-scraper video ")[1].split(" -d -w ")[0];

            if (output != null) {
                Platform.runLater(() -> createText("normal-text", numLine + ": " + output + "\n"));
                ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "ids.cfg");
                try {
                    Date date = new Date();
                    Timestamp timestamp = new Timestamp(date.getTime());
                    String[] url = link.split("/");
                    boolean exist = getVideoId(url[5], db);

                    if (!exist) {
                        DataBaseIds video = new DataBaseIds(url[3].replace("@", ""), link, Long.parseLong(url[5]), sdf3.format(timestamp));
                        db.store(video);
                    }
                } finally {
                    db.close();
                }
            } else {
                Platform.runLater(() -> createText("error-text", numLine + ": Not supported, " + link + "\n"));
            }

            process.waitFor();
            numLine++;

            bufferedReader.close();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static boolean getVideoId(String videoId, ObjectContainer db) {
        DataBaseIds videos = new DataBaseIds(null, null, Long.parseLong(videoId), null);
        ObjectSet<Object> ids = db.queryByExample(videos);
        return ids.size() > 0;
    }

    /*************************** METHODS FXML***************************/

    @FXML
    private void btnOpenFolderOnClick() {
        new Thread(() -> {
            try {
                Desktop.getDesktop().open(new File(lblFolderPath.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /************************ METHODS INITIALIZE ************************/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblFolderPath.setText(file.getParent());

        listTextFlow = tFlowImports.getChildren();
        tFlowImports.heightProperty().addListener(observable -> scrollPane.setVvalue(1.0));
        scrollPane.setVvalue(1.0);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                Platform.runLater(() -> createText("title", "Start Download\n"));
                if (createFolder(StartDownload.file))
                    readListToStartCommand(StartDownload.file);
                else {
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