package sample.util;

import javafx.stage.Stage;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 场景管理器
 */
public class StageManager {

    /**
     * 场景集合
     */
    private static Map<String, Stage> stageMap = new ConcurrentHashMap<>();

    /**
     * 根据key存放Scene
     *
     * @param key
     * @param stage
     */
    public static void put(String key, Stage stage) {
        if (Utils.isEmpty(key)) {
            throw new RuntimeException("key不为空!");
        }
        if (Objects.isNull(stage)) {
            throw new RuntimeException("scene不为空!");
        }
        stageMap.put(key, stage);
    }

    /**
     * 根据key获取Scene
     *
     * @param key
     * @return
     */
    public static Stage getStage(String key) {
        if (Utils.isEmpty(key)) {
            throw new RuntimeException("key不为空!");
        }
        return stageMap.get(key);
    }

}