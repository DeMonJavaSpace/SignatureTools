package sample;

import javafx.util.Pair;

import java.io.File;

public class KeyConfig {
    private String path;
    private String keyAlias;
    private String keyPassword;
    private String storePassword;

    private static KeyConfig instance;

    private KeyConfig() {
    }

    public static KeyConfig getInstance() {
        if (instance == null) {
            instance = new KeyConfig();
        }
        return instance;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getKeyAlias() {
        return keyAlias;
    }

    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public String getStorePassword() {
        return storePassword;
    }

    public void setStorePassword(String storePassword) {
        this.storePassword = storePassword;
    }

    public Pair<Boolean, String> checkKey() {
        if (Utils.isEmpty(path) || Utils.isEmpty(storePassword) || Utils.isEmpty(keyAlias) || Utils.isEmpty(keyPassword)) {
            return new Pair(false, "密钥配置文件异常，请检查！");
        }

        if (!new File(path).exists()) {
            return new Pair(false, "密钥文件不存在，请检查！");
        }

        if (!path.endsWith(".keystore") && !path.endsWith(".jks")) {
            return new Pair(false, "密钥文件格式有误，请检查！");
        }

        return new Pair(true, "密钥配置文件初步检查通过，可进行下一步！");
    }
}
