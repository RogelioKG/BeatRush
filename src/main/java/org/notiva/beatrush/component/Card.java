package org.notiva.beatrush.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.notiva.beatrush.core.Loader;


public class Card extends AnchorPane {
    /**
     * 卡片圖片顯示元件。
     */
    @FXML
    private ImageView imageView;

    /**
     * 卡片標題標籤元件。
     */
    @FXML
    private Label titleLabel;

    /**
     * 卡片描述標籤元件。
     */
    @FXML
    private Label descriptionLabel;

    /**
     * 圖片 URL 屬性。
     */
    private final StringProperty imageUrl = new SimpleStringProperty();

    /**
     * 標題屬性。
     */
    private final StringProperty title = new SimpleStringProperty();

    /**
     * 描述屬性。
     */
    private final StringProperty description = new SimpleStringProperty();

    /**
     * 預設建構子，會載入對應的 FXML 版面。
     * <p>
     * 在 FXML 中引用元件時，自動使用此建構子。
     * 此建構子會載入 Card.fxml 檔案並設定屬性綁定。
     *
     * <p><b>FXML 使用範例：</b></p>
     * <pre>{@code
     * <Card imageUrl="https://picsum.photos/500/300"
     *       title="First Card"
     *       description="The description of first card" />
     * }</pre>
     */
    public Card() {
        Loader.loadComponentView(this, "/view/component/Card.fxml");
        bindProperty();
    }

    /**
     * 使用指定參數建立卡片元件。
     * <p>
     * 在 Java 中手動建立元件時使用，例如控制器中動態加入卡片。
     * 此建構子會呼叫預設建構子載入 FXML，然後設定指定的屬性值。
     *
     * <p><b>Java 使用範例：</b></p>
     * <pre>{@code
     * FlowPane container = new FlowPane();
     * Card card = new Card(
     *     "https://picsum.photos/500/300",
     *     "Third Card",
     *     "The description of third card"
     * );
     * container.getChildren().add(card);
     * }</pre>
     *
     * @param imageUrl    卡片圖片的 URL，可為 null 或空字串
     * @param title       卡片標題，可為 null
     * @param description 卡片描述文字，可為 null
     */
    public Card(String imageUrl, String title, String description) {
        this();
        setImageUrl(imageUrl);
        setTitle(title);
        setDescription(description);
    }

    /**
     * 綁定屬性到對應的 UI 元素。
     * <p>
     * 此方法會將 imageUrl、title 和 description 屬性分別綁定到
     * imageView、titleLabel 和 descriptionLabel。
     * <p>
     * 對於圖片屬性，會額外處理載入失敗的情況，當圖片載入失敗時會在
     * 標準錯誤輸出中印出錯誤訊息，並將圖片設為 null。
     */
    private void bindProperty() {
        imageView.imageProperty().bind(imageUrlProperty().map(Loader::loadImage));

        titleLabel.textProperty().bind(titleProperty());
        descriptionLabel.textProperty().bind(descriptionProperty());
    }

    /**
     * 取得圖片 URL 屬性值。
     *
     * @return 目前設定的圖片 URL，可能為 null
     */
    public String getImageUrl() {
        return imageUrl.get();
    }

    /**
     * 設定圖片 URL 屬性值。
     * <p>
     * 設定後會自動觸發屬性綁定，更新對應的 ImageView 元件。
     * 若 URL 無效或圖片載入失敗，ImageView 會顯示為空白。
     *
     * @param value 新的圖片 URL，可為 null 或空字串
     */
    public void setImageUrl(String value) {
        imageUrl.set(value);
    }

    /**
     * 取得圖片 URL 的 StringProperty 物件，用於資料綁定。
     *
     * @return 圖片 URL 的 StringProperty 物件
     */
    public StringProperty imageUrlProperty() {
        return imageUrl;
    }

    /**
     * 取得標題屬性值。
     *
     * @return 目前設定的標題文字，可能為 null
     */
    public String getTitle() {
        return title.get();
    }

    /**
     * 設定標題屬性值。
     * <p>
     * 設定後會自動觸發屬性綁定，更新對應的 Label 元件顯示。
     *
     * @param value 新的標題文字，可為 null
     */
    public void setTitle(String value) {
        title.set(value);
    }

    /**
     * 取得標題的 StringProperty 物件，用於資料綁定。
     *
     * @return 標題的 StringProperty 物件
     */
    public StringProperty titleProperty() {
        return title;
    }

    /**
     * 取得描述屬性值。
     *
     * @return 目前設定的描述文字，可能為 null
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * 設定描述屬性值。
     * <p>
     * 設定後會自動觸發屬性綁定，更新對應的 Label 元件顯示。
     *
     * @param value 新的描述文字，可為 null
     */
    public void setDescription(String value) {
        description.set(value);
    }

    /**
     * 取得描述的 StringProperty 物件，用於資料綁定。
     *
     * @return 描述的 StringProperty 物件
     */
    public StringProperty descriptionProperty() {
        return description;
    }
}