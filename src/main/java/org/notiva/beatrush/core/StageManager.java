package org.notiva.beatrush.core;

import java.util.HashMap;
import java.util.Map;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class StageManager {
    private final Map<String, Stage> stages = new HashMap<>();
    private final int DEFAULT_WIDTH = 1000;
    private final int DEFAULT_HEIGHT = 800;

    private static class Holder {
        private static final StageManager INSTANCE = new StageManager();
    }

    /**
     * 獲取 {@link StageManager} 的唯一實例 (Singleton Patterns)。
     *
     * @return {@link StageManager} 的唯一實例
     */
    public static StageManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 註冊一個具有指定名稱的視窗。
     *
     * @param name 視窗的唯一 ID
     * @param stage 要註冊的 {@link Stage} 物件
     */
    public void registerStage(String name, Stage stage) {
        this.stages.put(name, stage);
    }

    /**
     * 獲取指定名稱的視窗。
     *
     * @param name 要獲取的視窗名稱
     * @return 與指定名稱關聯的 {@link Stage} 物件
     * @throws RuntimeException 如果指定名稱的視窗不存在
     */
    public Stage getStage(String name) {
        Stage stage = this.stages.get(name);
        if (stage == null) {
            throw new RuntimeException(String.format("Stage %s does not exist.", name));
        }
        return stage;
    }

    /**
     * 顯示指定名稱的視窗，並加載指定的 FXML 檔案作為其場景內容。
     * 使用預設的寬度和高度 (600x600)。
     *
     * @param name 要顯示的視窗名稱
     * @param fxmlFile FXML 檔案路徑 (以 resources 目錄為根目錄)，用於加載場景內容
     */
    public void showStage(String name, String fxmlFile) {
        showStage(name, fxmlFile, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * 顯示指定名稱的視窗，並以指定的 {@link Parent} 物件作為其場景內容。
     * 使用預設的寬度和高度 (600x600)。
     * 主要是用於需要傳遞參數的情況。
     * 範例可見 ScorePageController
     *
     * @param name 要顯示的視窗名稱
     * @param root 要顯示的 Parent 物件
     */
    public void showStage(String name, Parent root) {
        showStage(name, root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * 顯示指定名稱的視窗，並加載指定的 FXML 檔案作為其場景內容，使用指定的寬度和高度。
     *
     * @param name 要顯示的視窗名稱
     * @param fxmlFile FXML 檔案路徑 (以 resources 目錄為根目錄)
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
     * 顯示指定名稱的視窗，並以指定的 {@link Parent} 物件作為其場景內容，使用指定的寬度和高度。
     * 主要是用於需要傳遞參數的情況。
     *
     * @param name 要顯示的視窗名稱
     * @param root 要顯示的 {@link Parent} 物件
     * @param width 場景寬度
     * @param height 場景高度
     */
    public void showStage(String name, Parent root, int width, int height) {
        Stage stage = getStage(name);
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * 註銷指定名稱的視窗。
     *
     * @param name 要註銷的視窗名稱
     */
    public void removeStage(String name) {
        this.stages.remove(name);
    }

    /**
     * 創建一個新的場景，使用指定的 FXML 檔案作為其內容。
     * 使用預設的寬度和高度 (600x600)。
     *
     * @param fxmlFile FXML 檔案路徑 (以 resources 目錄為根目錄)
     * @return 配置好的 Scene 物件
     */
    public Scene getScene(String fxmlFile) {
        return getScene(fxmlFile, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * 創建一個新的場景，使用指定的 FXML 檔案作為其內容。
     *
     * @param fxmlFile FXML 檔案路徑 (以 resources 目錄為根目錄)
     * @param width 場景寬度
     * @param height 場景高度
     * @return 配置好的 Scene 物件
     */
    public Scene getScene(String fxmlFile, int width, int height) {
        Parent root = ResourceLoader.loadView(fxmlFile);
        return new Scene(root, width, height);
    }
}