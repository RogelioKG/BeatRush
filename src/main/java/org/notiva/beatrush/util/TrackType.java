package org.notiva.beatrush.util;

import com.google.gson.annotations.SerializedName;

public enum TrackType {
    @SerializedName("0")
    LEFT,
    @SerializedName("1")
    MIDDLE_LEFT,
    @SerializedName("2")
    MIDDLE_RIGHT,
    @SerializedName("3")
    RIGHT;

    /**
     * 取得指定 enum 常數上 {@link SerializedName} 註解的值。
     *
     * @return {@code @SerializedName} 的值
     */
    public String getSerializedName() {
        return Misc.getSerializedName(this);
    }
};
