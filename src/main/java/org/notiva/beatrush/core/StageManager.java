/**
 * 管理 JavaFX 應用程序中的各個 Stage（舞台）實例。
 * <p>
 * 本類使用單例模式實現，確保整個應用程序中只有一個 StageManager 實例，
 * 用於註冊、獲取、顯示和關閉各個命名的舞台。
 * </p>
 */
package org.notiva.beatrush.core;

import java.util.HashMap;
import java.util.Map;

import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class StageManager {
    /** 存儲所有已註冊舞台的映射表，鍵為舞台名稱，值為對應的 Stage 對象 */
    private final Map<String, Stage> stages = new HashMap<>();

    /**
     * 用於實現單例模式的私有靜態內部類。
     * 當第一次訪問 Holder 類時，會創建 StageManager 的唯一實例。
     */
    private static class Holder {
        /** StageManager 的單例實例 */
        private static final StageManager INSTANCE = new StageManager();
    }

    /**
     * 獲取 StageManager 的單例實例。
     *
     * @return StageManager 的唯一實例
     */
    public static StageManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 註冊一個具有指定名稱的舞台。
     *
     * @param name 舞台的唯一標識名稱
     * @param stage 要註冊的 Stage 對象
     */
    public void registerStage(String name, Stage stage) {
        this.stages.put(name, stage);
    }

    /**
     * 獲取指定名稱的舞台。
     *
     * @param name 要獲取的舞台名稱
     * @return 與指定名稱關聯的 Stage 對象
     * @throws RuntimeException 如果指定名稱的舞台不存在
     */
    public Stage getStage(String name) {
        Stage stage = this.stages.get(name);
        if (stage == null) {
            throw new RuntimeException(String.format("Stage %s does not exist.", name));
        }
        return stage;
    }

    /**
     * 顯示指定名稱的舞台，並加載指定的 FXML 檔案作為其場景內容。
     * 使用默認的寬度和高度 (600x600)。
     *
     * @param name 要顯示的舞台名稱
     * @param fxmlFile FXML 檔案路徑，用於加載場景內容
     */
    public void showStage(String name, String fxmlFile) {
        showStage(name, fxmlFile, 600, 600);
    }

    /**
     * 顯示指定名稱的舞台，並加載指定的 FXML 檔案作為其場景內容，使用指定的寬度和高度。
     *
     * @param name 要顯示的舞台名稱
     * @param fxmlFile FXML 檔案路徑，用於加載場景內容
     * @param width 場景寬度
     * @param height 場景高度
     */
    public void showStage(String name, String fxmlFile, int width, int height) {
        Stage stage = getStage(name);
        Scene scene = getScene(fxmlFile, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * 關閉指定名稱的舞台並從管理器中移除。
     *
     * @param name 要關閉的舞台名稱
     */
    public void closeStage(String name) {
        Stage stage = getStage(name);
        stage.close();
        this.stages.remove(name);
    }

    /**
     * 創建一個新的場景，使用指定的 FXML 檔案作為其內容，並應用預設樣式表。
     *
     * @param fxmlFile FXML 檔案路徑，用於加載場景內容
     * @param width 場景寬度
     * @param height 場景高度
     * @return 配置好的 Scene 對象
     */
    private Scene getScene(String fxmlFile, int width, int height) {
        Parent root = Loader.loadView(fxmlFile);
        Scene scene = new Scene(root, width, height);
        Loader.applyStyles(scene, new String[]{
                "/css/all.css",
        });
        return scene;
    }
}