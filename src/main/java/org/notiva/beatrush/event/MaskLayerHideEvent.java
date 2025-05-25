package org.notiva.beatrush.event;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * 遮罩層隱藏事件，用於觸發遮罩層的隱藏動作。
 */
public class MaskLayerHideEvent extends Event {

    public static final EventType<MaskLayerHideEvent> HIDE =
            new EventType<>(Event.ANY, "MASK_LAYER_HIDE");

    /**
     * 預設建構子，建立一個遮罩層隱藏事件。
     */
    public MaskLayerHideEvent() {
        super(HIDE);
    }
}