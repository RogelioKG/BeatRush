package org.notiva.beatrush.component;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.notiva.beatrush.core.Loader;


public class Card extends AnchorPane {

    @FXML
    private ImageView imageView;

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    private final StringProperty imageUrl = new SimpleStringProperty();

    private final StringProperty title = new SimpleStringProperty();

    private final StringProperty description = new SimpleStringProperty();

    /**
     * 預設建構子，會載入對應的 FXML 版面。
     *
     * <p>FXML 使用範例：</p>
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
     * 在 Java 內手動建立元件。
     *
     * <p>Java 使用範例：</p>
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
     * @param imageUrl    卡片圖片的 URL
     * @param title       卡片標題
     * @param description 卡片描述文字
     */
    public Card(String imageUrl, String title, String description) {
        this();
        setImageUrl(imageUrl);
        setTitle(title);
        setDescription(description);
    }

    /**
     * 綁定屬性到對應的 UI 元素。
     */
    private void bindProperty() {
        imageView.imageProperty().bind(imageUrlProperty().map(Loader::loadImage));

        titleLabel.textProperty().bind(titleProperty());
        descriptionLabel.textProperty().bind(descriptionProperty());
    }

    /**
     * 取得圖片 URL 屬性值。
     *
     * @return 目前設定的圖片 URL
     */
    public String getImageUrl() {
        return imageUrl.get();
    }

    /**
     * 設定圖片 URL 屬性值。
     *
     * @param value 新的圖片 URL
     */
    public void setImageUrl(String value) {
        imageUrl.set(value);
    }

    /**
     * 取得圖片 URL 的 StringProperty 物件。
     *
     * @return 圖片 URL 的 StringProperty 物件
     */
    public StringProperty imageUrlProperty() {
        return imageUrl;
    }

    /**
     * 取得標題屬性值。
     *
     * @return 目前設定的標題文字
     */
    public String getTitle() {
        return title.get();
    }

    /**
     * 設定標題屬性值。
     *
     * @param value 新的標題文字
     */
    public void setTitle(String value) {
        title.set(value);
    }

    /**
     * 取得標題的 StringProperty 物件。
     *
     * @return 標題的 StringProperty 物件
     */
    public StringProperty titleProperty() {
        return title;
    }

    /**
     * 取得描述屬性值。
     *
     * @return 目前設定的描述文字
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * 設定描述屬性值。
     *
     * @param value 新的描述文字
     */
    public void setDescription(String value) {
        description.set(value);
    }

    /**
     * 取得描述的 StringProperty 物件。
     *
     * @return 描述的 StringProperty 物件
     */
    public StringProperty descriptionProperty() {
        return description;
    }
}