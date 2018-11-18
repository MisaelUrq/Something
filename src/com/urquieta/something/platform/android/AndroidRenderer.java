package com.urquieta.something.platform.android;

import android.graphics.Paint;

import com.urquieta.something.platform.Screen;

public class AndroidRenderer {
    public Screen screen;

    public AndroidRenderer(Screen screen) {
        this.screen = screen;
    }

    public void DrawRect(int x, int y, int width, int height, int color) {
        if (this.screen.getCanvas() != null)
        {
            Paint paint = new Paint();
            paint.setColor(color);
            this.screen.getCanvas().drawRect(x, y, width, height, paint);
        }
    }
}
