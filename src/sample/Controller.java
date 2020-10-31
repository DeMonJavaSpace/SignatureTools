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

    private boolean isKeyOk = false;
    private boolean isApkOk = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void openKey(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择密钥配置");
        File file = new File(tfKey.getText()).getParentFile();
        if (file != null && file.exists()) {
            fileChooser.setInitialDirectory(file);
        } else {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        }
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")

        );
        File keyFile = fileChooser.showOpenDialog(getStage());
        if (keyFile != null) {
            String path = keyFile.getPath();
            tfKey.setText(path);
            XMlHelper.xmlParser(path);
            isKeyOk = KeyConfig.getInstance().checkKey().getKey();
            tvMsg.setText(KeyConfig.getInstance().checkKey().getValue());
        }
    }

    public void openApk(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择待签名Apk");
        File file = new File(tfKey.getText()).getParentFile();
        if (file != null && file.exists()) {
            fileChooser.setInitialDirectory(file);
        } else {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        }
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("APK", "*.apk")

        );
        File apkFile = fileChooser.showOpenDialog(getStage());
        if (apkFile != null) {
            String path = apkFile.getPath();
            tfApk.setText(path);
            tfSign.setText("");
            String msg = Utils.exeCmd("java -jar apksigner.jar verify -v " + path);
            if (Utils.isEmpty(msg)) {
                tvMsg.setText("Apk尚未签名，可以开始签名！");
                isApkOk = true;
            } else {
                tvMsg.setText(msg);
                isApkOk = false;
            }
        }
    }


    public void signOld(ActionEvent actionEvent) {
        if (checkStatus()) {
            String signPath = new File(tfApk.getText()).getParent() + "\\sign_old-" + System.currentTimeMillis() + ".apk";
            String cmd = "jarsigner -verbose -keystore " + KeyConfig.getInstance().getPath()
                    + " -storepass " + KeyConfig.getInstance().getStorePassword()
                    + " -keypass " + KeyConfig.getInstance().getKeyPassword()
                    + " -sigfile CERT -signedjar "
                    + signPath + " "
                    + tfApk.getText() + " "
                    + KeyConfig.getInstance().getKeyAlias();
            String msg = Utils.exeCmd(cmd);
            if (new File(signPath).exists()) {
                tfSign.setText(signPath);
                tvMsg.setText("旧v1签名成功！\n" + msg);
            } else {
                tvMsg.setText("旧v1签名失败！可能存在的原因：\n密钥别名or密码配置错误！\n" + msg);
            }
        }
    }

    public void signNew(ActionEvent actionEvent) {
        if (checkStatus()) {
            String signPath = new File(tfApk.getText()).getParent() + "\\sign_new-" + System.currentTimeMillis() + ".apk";
            String cmd = "java -jar apksigner.jar sign  --ks " + KeyConfig.getInstance().getPath()
                    + " --ks-key-alias " + KeyConfig.getInstance().getKeyAlias()
                    + " --ks-pass pass:" + KeyConfig.getInstance().getStorePassword()
                    + " --key-pass pass:" + KeyConfig.getInstance().getKeyPassword()
                    + " --out "
                    + signPath + " "
                    + tfApk.getText();
            String msg = Utils.exeCmd(cmd);
            if (new File(signPath).exists()) {
                tfSign.setText(signPath);
                tvMsg.setText("新v1&v2签名成功！\n" + msg);
            } else {
                tvMsg.setText("新v1&v2签名失败！可能存在的原因：\n密钥别名or密码配置错误！\n" + msg);
            }
        }
    }

    public void openSign(ActionEvent actionEvent) {
        if (!Utils.isEmpty(tfSign.getText())) {
            Utils.openUrl(new File(tfSign.getText()).getParent());
        } else {
            tvMsg.setText("请先签名生成新Apk！");
        }
    }

    public void startStatus(ActionEvent actionEvent) {
        if (!Utils.isEmpty(tfSign.getText())) {
            String msg = Utils.exeCmd("java -jar apksigner.jar verify -v " + tfSign.getText());
            tvMsg.setText(msg);
        } else {
            tvMsg.setText("请先签名生成新Apk！");
        }
    }


    private Boolean checkStatus() {
        if (!isKeyOk) {
            tvMsg.setText("密钥配置文件异常，请重新选择！");
            return false;
        }
        if (!isApkOk) {
            tvMsg.setText("待签名的Apk异常，请重新选择！");
            return false;
        }
        return true;
    }

    private Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }
}