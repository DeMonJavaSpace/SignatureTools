package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.*;
import sample.util.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class SignedController implements Initializable {
    private static final String TAG = "SignedController";
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
    @FXML
    private RadioButton rbYes;
    @FXML
    private RadioButton rbNo;

    private boolean isKeyOk = false;
    private boolean isApkOk = false;

    private boolean isChannel = false;
    private List<String> channelList = new ArrayList<>();

    private String channelKey = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //初始加载默认的密钥配置文件
        File keyFile = new File(System.getProperty("user.dir") + "/config.xml");
        if (keyFile.exists()) {
            String path = keyFile.getPath();
            tfKey.setText(path);
            XMlHelper.xmlParser(path);
            isKeyOk = KeyConfig.getInstance().checkKey().getKey();
            tvMsg.setText(KeyConfig.getInstance().checkKey().getValue());
        } else {
            tvMsg.setText("加载默认密钥配置文件失败，请手动选择！");
        }

        ToggleGroup group = new ToggleGroup();
        rbYes.setToggleGroup(group);
        rbYes.setUserData(true);
        rbNo.setToggleGroup(group);
        rbNo.setSelected(true);
        rbNo.setUserData(false);
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            isChannel = (boolean) newValue.getUserData();
        });

        initChannel();
    }

    private void initChannel() {
        String channels = FileUtil.readText(System.getProperty("user.dir") + "/channel.txt");
        if (!Utils.isEmpty(channels) && channels.contains("_")) {
            channelKey = channels.split("_")[0];
        }
        Log.i(TAG, "channelKey: " + channelKey);

        String selectedChannels = FileUtil.readText(System.getProperty("user.dir") + "/channel_selected.txt");
        Log.i(TAG, "selectedChannels: " + selectedChannels);
        channelList.clear();
        if (!Utils.isEmpty(selectedChannels)) {
            channelList.addAll(Arrays.asList(selectedChannels.split(";")));
        }
    }

    public void openChannel() {
        Platform.runLater(() -> {
            Stage channelStage = StageManager.getStage("channel_setting");
            if (channelStage == null) {
                try {
                    channelStage = new Stage();//创建舞台；
                    Parent target = FXMLLoader.load(getClass().getResource("apk_channel.fxml"));
                    channelStage.setScene(new Scene(target)); //将场景载入舞台；
                    channelStage.setTitle("Apk渠道配置");
                    // 存放Scene
                    StageManager.put("channel_setting", channelStage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            channelStage.show();
            channelStage.setOnCloseRequest(event -> {
                initChannel();
            });
        });
    }

    public void refreshKey(ActionEvent actionEvent) {
        File keyFile = new File(tfKey.getText());
        if (keyFile.exists()) {
            String path = keyFile.getPath();
            tfKey.setText(path);
            XMlHelper.xmlParser(path);
            isKeyOk = KeyConfig.getInstance().checkKey().getKey();
            tvMsg.setText(KeyConfig.getInstance().checkKey().getValue());
        } else {
            tvMsg.setText("刷新密钥配置文件失败，请手动选择！");
        }
    }

    public void editKey(ActionEvent actionEvent) {
        File file = new File(tfKey.getText());
        if (file.exists()) {
            Utils.openUrl(tfKey.getText());
            tvMsg.setText("编辑密钥配置完成后，请刷新！");
        } else {
            tvMsg.setText("编辑密钥配置文件失败，请手动选择！");
        }
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
        if (keyFile != null && keyFile.exists()) {
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
        File file = new File(tfApk.getText()).getParentFile();
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
            isApkOk = true;
            try {
                String msg = Utils.exeCmd("java -jar apksigner.jar verify -v " + path);
                if (Utils.isEmpty(msg)) {
                    tvMsg.setText("Apk尚未签名，可以开始签名！");
                } else {
                    tvMsg.setText("Apk已签名，可以尝试重新签名！\n" + msg);
                }
            } catch (Exception e) {
                tvMsg.setText(e.getMessage());
            }
        }
    }


    public void signOld(ActionEvent actionEvent) {
        if (checkStatus()) {
            ThreadUtil.runOnIOThread(() -> {
                String apkPath = tfApk.getText();
                File apkFile = new File(apkPath);
                File signDirectory = new File(apkFile.getParentFile(), FileUtil.getFileName(apkFile) + "_signv1");
                signDirectory.mkdirs();
                try {
                    if (isChannel) {
                        StringBuilder sb = new StringBuilder();
                        for (String channel : channelList) {
                            Platform.runLater(() -> {
                                sb.append(channel + "渠道开始...\n");
                                tvMsg.setText(sb.toString());
                            });
                            String msg = signV1(apkPath, signDirectory.getAbsolutePath(), channel);
                            sb.append(msg);
                            Platform.runLater(() -> {
                                tvMsg.setText(sb.toString());
                            });
                        }
                    } else {
                        String msg = signV1(apkPath, signDirectory.getAbsolutePath(),"");
                        Platform.runLater(() -> {
                            tvMsg.setText(msg);
                        });
                    }
                    tfSign.setText(signDirectory.getAbsolutePath());
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        tvMsg.setText(e.getMessage());
                    });
                }
            });
        }
    }


    private String signV1(String apkPath, String signDirectory, String channel) throws Exception {
        String signApkPath = signDirectory + "\\" + FileUtil.createNewFileName(new File(apkPath), channel);
        File signApk = new File(signApkPath);
        if (signApk.exists()) {
            signApk.delete();
        }
        String cmd = "jarsigner -verbose -keystore " + KeyConfig.getInstance().getPath()
                + " -storepass " + KeyConfig.getInstance().getStorePassword()
                + " -keypass " + KeyConfig.getInstance().getKeyPassword()
                + " -signedjar "
                + signApkPath + " "
                + apkPath + " "
                + KeyConfig.getInstance().getKeyAlias()
                + " -sigfile CERT";
        String msg = Utils.exeCmd(cmd);
        Log.i(TAG, "signV1 sign: " + msg);
        if (new File(signApkPath).exists() && msg.contains("META-INF/MANIFEST.MF")) {
            FileUtil.addChannel(signApkPath, channelKey, channel);
            return apkPath + "-旧v1签名成功！\n";
        } else {
            return apkPath + "-旧v1签名失败！可能存在的原因：\n1.密钥别名or密码配置错误！\n2.已经签名过的Apk，无法使用旧v1签名重新签名。\n";
        }
    }


    public void signNew(ActionEvent actionEvent) {
        if (checkStatus()) {
            ThreadUtil.runOnIOThread(() -> {
                String apkPath = tfApk.getText();
                File apkFile = new File(apkPath);
                File channelDirectory = new File(apkFile.getParentFile(), FileUtil.getFileName(apkFile) + "_channel");
                channelDirectory.mkdirs();
                File alignDirectory = new File(apkFile.getParentFile(), FileUtil.getFileName(apkFile) + "_align");
                alignDirectory.mkdirs();
                File signDirectory = new File(apkFile.getParentFile(), FileUtil.getFileName(apkFile) + "_signv2");
                signDirectory.mkdirs();
                try {
                    if (isChannel) {
                        StringBuilder sb = new StringBuilder();
                        for (String channel : channelList) {
                            Platform.runLater(() -> {
                                sb.append(channel + "渠道开始...\n");
                                tvMsg.setText(sb.toString());
                            });
                            String channelApkPath = FileUtil.copyFile(apkPath, channelDirectory.getAbsolutePath() + "\\" + FileUtil.createNewFileName(apkFile, channel));
                            FileUtil.addChannel(channelApkPath, channelKey, channel);
                            String msg = signedV2(channelApkPath, alignDirectory.getAbsolutePath(), signDirectory.getAbsolutePath());
                            sb.append(msg);
                            Platform.runLater(() -> {
                                tvMsg.setText(sb.toString());
                            });
                        }
                    } else {
                        String msg = signedV2(apkPath, alignDirectory.getAbsolutePath(), signDirectory.getAbsolutePath());
                        Platform.runLater(() -> {
                            tvMsg.setText(msg);
                        });
                    }
                    tfSign.setText(signDirectory.getAbsolutePath());
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        tvMsg.setText(e.getMessage());
                    });
                }
                FileUtil.deleteDir(alignDirectory);
                FileUtil.deleteDir(channelDirectory);
            });
        }
    }

    private String signedV2(String apkPath, String alignDirectory, String signDirectory) throws Exception {
        String apkName = new File(apkPath).getName();
        StringBuilder sb = new StringBuilder();
        //对齐
        String alignapk = alignDirectory + "\\" + apkName;
        String align = "zipalign -v 4 " + apkPath + " " + alignapk;
        String alignMsg = Utils.exeCmd(align);
        Log.i(TAG, "signedV2 align: " + alignMsg);
        if (new File(alignapk).exists()) {
            sb.append(apkPath + "-新v1&v2对齐成功！\n");
        } else {
            sb.append(apkPath + "-新v1&v2对齐失败！\n");
            return sb.toString();
        }
        String signPath = signDirectory + "\\" + apkName;
        String cmd = "java -jar apksigner.jar sign  --ks " + KeyConfig.getInstance().getPath()
                + " --ks-key-alias " + KeyConfig.getInstance().getKeyAlias()
                + " --ks-pass pass:" + KeyConfig.getInstance().getStorePassword()
                + " --key-pass pass:" + KeyConfig.getInstance().getKeyPassword()
                + " --out "
                + signPath + " "
                + alignapk;
        String msg = Utils.exeCmd(cmd);
        Log.i(TAG, "signedV2 sign: " + msg);
        if (new File(signPath).exists()) {
            sb.append(apkPath + "-新v1&v2签名成功！\n");
        } else {
            sb.append(apkPath + "-新v1&v2签名失败！可能存在的原因：密钥别名or密码配置错误！\n");
        }
        return sb.toString();
    }

    public void openSign(ActionEvent actionEvent) {
        if (!Utils.isEmpty(tfSign.getText())) {
            Utils.openUrl(new File(tfSign.getText()).getAbsolutePath());
        } else {
            tvMsg.setText("请先签名生成新Apk！");
        }
    }

    public void startStatus(ActionEvent actionEvent) {
        if (!Utils.isEmpty(tfSign.getText())) {
            ThreadUtil.runOnIOThread(() -> {
                try {
                    File[] apkFiles = new File(tfSign.getText()).listFiles((file1, filename) -> filename.endsWith(".apk"));
                    StringBuilder sb = new StringBuilder();
                    for (File apkFile : apkFiles) {
                        String msg = Utils.exeCmd("java -jar apksigner.jar verify -v " + apkFile.getAbsolutePath());
                        sb.append(apkFile.getName() + ": ");
                        String[] msgs = msg.split("\n");
                        for (int i = 0; i < 4; i++) {
                            sb.append(msgs[i] + "\n");
                        }
                        ThreadUtil.runOnUiThread(() -> {
                            tvMsg.setText(sb.toString());
                        });
                    }
                } catch (Exception e) {
                    ThreadUtil.runOnUiThread(() -> {
                        tvMsg.setText(e.getMessage());
                    });
                }
            });
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
        if (isChannel && Utils.isEmpty(channelKey)) {
            tvMsg.setText("渠道前缀不能为空！");
            return false;
        }
        if (isChannel && channelList.isEmpty()) {
            tvMsg.setText("请至少选择一个渠道！");
            return false;
        }
        return true;
    }

    private Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }
}