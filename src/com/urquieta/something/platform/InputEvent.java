package com.urquieta.something.platform;

public class InputEvent {
   // NOTE(Misael): This are for only for the android version.
    public static final int TOUCH_DOWN    = (1 << 1);
    public static final int TOUCH_UP      = (1 << 2);
    public static final int TOUCH_DRAGGED = (1 << 3);
    public static final int TOUCH_HOLD    = (1 << 4);
    public static final int TOUCH_MOVED   = (1 << 5);

    public static final int KEY_DOWN      = (1 << 6);
    public static final int KEY_UP        = (1 << 7);

    public static final boolean is_android = true;
    public int type;
    public float x, y;
    public int x_in_pixels, y_in_pixels;
}
