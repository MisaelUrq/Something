package com.urquieta.something.platform;

import com.urquieta.something.game.util.Vec2;

public class InputEvent {
   // NOTE(Misael): This are for only for the android version.
    public static final int TOUCH_DOWN    = (1 << 1);
    public static final int TOUCH_UP      = (1 << 2);
    public static final int TOUCH_DRAGGED = (1 << 3);
    public static final int TOUCH_HOLD    = (1 << 4);
    public static final int TOUCH_MOVED   = (1 << 5);

    public static final int KEY_DOWN      = (1 << 6);
    public static final int KEY_UP        = (1 << 7);

    public int type;
    public Vec2 cursor_position;
    public Vec2 cursor_position_pixels;

    public InputEvent() {
        this.cursor_position        = new Vec2(0, 0);
        this.cursor_position_pixels = new Vec2(0, 0);
        this.type                   = 0;
    }
}
