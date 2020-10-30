package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextField tfKey;
    @FXML
    private TextField tfApk;
    @FXML
    private TextField tfSign;
    @FXML
    private Label tvMsg;
    @FXML
    private AnchorPane root;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void openKey(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择密钥配置");
        File file = new File(tfKey.getText()).getParentFile();
        if (file != null && file.exists()) {
            fileChooser.setInitialDirectory(file);
        }
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt")

        );
        File keyFile = fileChooser.showOpenDialog(getStage());
        if (keyFile != null) {
            String path = keyFile.getPath();
            tfKey.setText(path);
        }
    }

    public void openApk(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择Apk");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("APK", "*.apk")

        );
        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            String path = file.getPath();
            tfApk.setText(path);
        }
    }

    public void openSign(ActionEvent actionEvent) {

    }

    public static void openUrl(String url) {
        String cmd = "rundll32 url.dll,FileProtocolHandler " + url;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startStatus(ActionEvent actionEvent) {
    }


    private Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }
}