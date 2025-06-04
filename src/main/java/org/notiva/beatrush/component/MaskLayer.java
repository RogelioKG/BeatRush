package org.notiva.beatrush.component;

import javafx.animation.FadeTransition;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.notiva.beatrush.core.ResourceLoader;
import org.notiva.beatrush.event.MaskLayerHideEvent;
import org.notiva.beatrush.event.MaskLayerShowEvent;

/**
 * <h2>遮罩層元件</h2>
 * <p>使用範例：</p>
 * <pre>{@code
 * StackPane root = new StackPane();
 * MaskLayer maskLayer = new MaskLayer(Color.rgb(255, 255, 255, 0.5));
 * root.getChildren().add(maskLayer);
 *
 * // 1. 顯示遮罩層 (假設你碰得到 maskLayer 參照，比如說你在頁面的 controller)
 * maskLayer.show();
 *
 * // 2. 顯示遮罩層 (假設你碰不到 maskLayer 參照，比如說你在元件內)
 * // 首先你需在 controller 上的某節點 (通常是 root) 設定事件監聽
 * // 一旦接收到 MaskLayerShowEvent 或 MaskLayerHideEvent
 * // 就開啟或關閉遮罩層
 * maskLayer.addEventHandlersFor(root);
 *
 * // 接著就可以在任意底層元件發送 MaskLayerShowEvent 或 MaskLayerHideEvent
 * // 這個 Event 就可以一層一層上達天聽，由 root 接收，控制遮罩層。
 * fireEvent(new MaskLayerShowEvent());
 * }</pre>
 */
public class MaskLayer extends Pane {
    private FadeTransition fadeIn;
    private FadeTransition fadeOut;

    private final EventHandler<MaskLayerShowEvent> showHandler = e -> show();
    private final EventHandler<MaskLayerHideEvent> hideHandler = e -> hide();

    private final ObjectProperty<Color> maskColor = new SimpleObjectProperty<>(Color.rgb(0, 0, 0, 0.8));

    /**
     * 預設建構子，會載入對應的 FXML 版面。
     *
     * <p>FXML 使用範例：</p>
     * 1. 預設 color
     * <pre>{@code
     * <MaskLayer />
     * }</pre>
     * 2. 自訂 color
     * <pre>{@code
     * <MaskLayer>
     *     <backgroundColor>
     *         <Color red="0.0" green="0.0" blue="0.0" opacity="0.5"/>
     *     </backgroundColor>
     * </MaskLayer>
     * }</pre>
     */
    public MaskLayer() {
        ResourceLoader.loadComponentView(this, "/view/component/MaskLayer.fxml");
        init();
    }

    /**
     * 在 Java 內手動建立元件。
     *
     * @param color 遮罩背景顏色
     */
    public MaskLayer(Color color) {
        this();
        setMaskColor(color);
    }

    /**
     * 初始化遮罩層的相關設定和動畫效果。
     */
    private void init() {
        // 遮罩背景樣式
        setOpacity(0);
        setMouseTransparent(false); // 阻擋底層事件
        setVisible(false);          // 一開始先隱藏
        setPickOnBounds(true);      // 填滿區域可擋事件

        // 尺寸預設為 auto 適應
        setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        // 初始化背景
        updateBackground();

        // 綁定背景顏色變化
        maskColor.addListener((obs, oldVal, newVal) -> updateBackground());

        // 淡入動畫
        fadeIn = new FadeTransition(Duration.millis(300), this);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // 淡出動畫
        fadeOut = new FadeTransition(Duration.millis(300), this);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> setVisible(false));
    }

    /**
     * 更新遮罩層的背景樣式。
     */
    private void updateBackground() {
        setBackground(new Background(
                new BackgroundFill(maskColor.get(), CornerRadii.EMPTY, Insets.EMPTY)
        ));
    }

    /**
     * 註冊事件處理器到指定的節點上。<br>
     * 允許此節點攔截 {@link MaskLayerShowEvent} 和 {@link MaskLayerHideEvent}，<br>
     * 去控制遮罩層的 {@link #show()} 與 {@link #hide()}。
     *
     * @param node 要註冊事件處理器的節點
     */
    public void addEventHandlersFor(Node node) {
        node.addEventHandler(MaskLayerShowEvent.SHOW, showHandler);
        node.addEventHandler(MaskLayerHideEvent.HIDE, hideHandler);
    }

    /**
     * 移除指定節點的事件處理器。
     *
     * @param node 要移除事件處理器的節點
     */
    public void removeEventHandlersFor(Node node) {
        node.removeEventHandler(MaskLayerShowEvent.SHOW, showHandler);
        node.removeEventHandler(MaskLayerHideEvent.HIDE, hideHandler);
    }

    /**
     * 顯示遮罩層並播放淡入動畫。
     */
    public void show() {
        fadeOut.stop(); // 防止 hide 動畫還在跑
        setVisible(true);
        fadeIn.playFromStart();
    }

    /**
     * 隱藏遮罩層並播放淡出動畫。
     */
    public void hide() {
        fadeIn.stop(); // 防止 show 動畫還在跑
        fadeOut.playFromStart();
    }

    /**
     * 取得遮罩顏色屬性值。
     *
     * @return 目前設定的遮罩顏色
     */
    public Color getMaskColor() {
        return maskColor.get();
    }

    /**
     * 設定遮罩顏色屬性值。
     *
     * @param color 新的遮罩顏色
     */
    public void setMaskColor(Color color) {
        maskColor.set(color);
    }

    /**
     * 取得遮罩顏色的 ObjectProperty 物件。
     *
     * @return 遮罩顏色的 ObjectProperty 物件
     */
    public ObjectProperty<Color> maskColorProperty() {
        return maskColor;
    }
}