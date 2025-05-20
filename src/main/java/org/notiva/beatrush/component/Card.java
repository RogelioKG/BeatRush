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

    @FXML
    private ImageView imageView;

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    // 屬性定義
    private final StringProperty imageUrl = new SimpleStringProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();

    public Card() {
        Loader.loadComponentView(this, "/view/component/Card.fxml");
        bindProperty();
    }

    public Card(String imageUrl, String title, String description) {
        this();
        setImageUrl(imageUrl);
        setTitle(title);
        setDescription(description);
    }

    private void bindProperty() {
        // 綁定屬性到 UI 元素
        imageView.imageProperty().bind(imageUrlProperty().map(url -> {
            if (url == null || url.isEmpty()) {
                return null;
            }
            try {
                return new Image(url);
            } catch (Exception e) {
                System.err.println("無法載入圖片: " + url);
                return null;
            }
        }));

        titleLabel.textProperty().bind(titleProperty());
        descriptionLabel.textProperty().bind(descriptionProperty());
    }

    // ImageUrl 屬性相關方法
    public String getImageUrl() {
        return imageUrl.get();
    }

    public void setImageUrl(String value) {
        imageUrl.set(value);
    }

    public StringProperty imageUrlProperty() {
        return imageUrl;
    }

    // Title 屬性相關方法
    public String getTitle() {
        return title.get();
    }

    public void setTitle(String value) {
        title.set(value);
    }

    public StringProperty titleProperty() {
        return title;
    }

    // Description 屬性相關方法
    public String getDescription() {
        return description.get();
    }

    public void setDescription(String value) {
        description.set(value);
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}