package org.notiva.beatrush.util;

import com.google.gson.annotations.SerializedName;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Misc {

    /**
     * 根據指定目標寬高比，從圖片中心裁切出最大可能尺寸的區域。
     * 該區域保持與目標相同的寬高比，並以圖片中心為中心。
     * 基本等效於 CSS 中的 <code>object-fit: cover;</code>。
     *
     * @param imageHeight 原始圖片的高度
     * @param imageWidth  原始圖片的寬度
     * @param viewHeight  目標高度
     * @param viewWidth   目標寬度
     * @return Rectangle2D 表示裁切的區域 (minX, minY, cropWidth, cropHeight)
     */
    public static Rectangle2D getCenteredCoverCrop(
            double imageHeight, double imageWidth,
            double viewHeight, double viewWidth
    ) {
        double imageAspect = imageWidth / imageHeight;
        double viewAspect = viewWidth / viewHeight;

        double cropWidth, cropHeight;

        if (imageAspect > viewAspect) {
            // 圖片比較寬，需裁切左右兩邊
            cropHeight = imageHeight;
            cropWidth = cropHeight * viewAspect;
        } else {
            // 圖片比較高，需裁切上下兩邊
            cropWidth = imageWidth;
            cropHeight = cropWidth / viewAspect;
        }

        double minX = (imageWidth - cropWidth) / 2;
        double minY = (imageHeight - cropHeight) / 2;

        return new Rectangle2D(minX, minY, cropWidth, cropHeight);
    }

    /**
     * 格式化時間長度為字串
     * 將時間長度轉換為 MM:SS 格式的字串
     *
     * @param duration 時間長度，可能為 null 或未知狀態
     * @return 格式化後的字串，格式為 "MM:SS"，如果 duration 為 null 或未知則返回 "0:00"
     */
    public static String formatDuration(Duration duration) {
        if (duration != null && !duration.isUnknown()) {
            double totalSeconds = duration.toSeconds();
            int minutes = (int) (totalSeconds / 60);
            int seconds = (int) (totalSeconds % 60);
            return String.format("%02d:%02d", minutes, seconds);
        } else {
            return "00:00";
        }
    }

    /**
     * 檢查是否為 URL 字串
     *
     * @param path 字串
     * @return 是否為 URL 字串
     */
    public static boolean isUrl(String path) {
        try {
            URI uri = new URI(path);
            return uri.getScheme() != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 取得指定 enum 常數上 {@link SerializedName} 註解的值。
     *
     * @param e enum 常數
     * @return {@code @SerializedName} 的值
     */
    public static String getSerializedName(Enum<?> e) {
        try {
            Field field = e.getClass().getField(e.name());
            SerializedName annotation = field.getAnnotation(SerializedName.class);
            return annotation != null ? annotation.value() : null;
        } catch (NoSuchFieldException ex) {
            return null;
        }
    }
}
