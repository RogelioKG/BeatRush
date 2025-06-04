package org.notiva.beatrush.util;

import com.google.gson.annotations.SerializedName;

public enum NoteType {
    @SerializedName("TAP")
    TAP,
    @SerializedName("HOLD")
    HOLD;

    /**
     * 取得指定 enum 常數上 {@link SerializedName} 註解的值。
     *
     * @return {@code @SerializedName} 的值
     */
    public String getSerializedName() {
        return Misc.getSerializedName(this);
    }
}
