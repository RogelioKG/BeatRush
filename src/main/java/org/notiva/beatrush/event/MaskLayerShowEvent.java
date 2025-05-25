package org.notiva.beatrush.event;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * 遮罩層顯示事件，用於觸發遮罩層的顯示動作。
 */
public class MaskLayerShowEvent extends Event {

    public static final EventType<MaskLayerShowEvent> SHOW =
            new EventType<>(Event.ANY, "MASK_LAYER_SHOW");

    /**
     * 預設建構子，建立一個遮罩層顯示事件。
     */
    public MaskLayerShowEvent() {
        super(SHOW);
    }
}