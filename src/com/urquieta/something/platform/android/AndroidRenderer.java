package com.urquieta.something.platform.android;

import android.graphics.Paint;
import android.graphics.RectF;

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

    public void DrawCircle(int x, int y, int radius, int color) {
        if (this.screen.getCanvas() != null) {
            Paint paint = new Paint();
            paint.setColor(color);
            float pos_x = x-radius;
            float pos_y = y-radius;
            RectF rect = new RectF(pos_x, pos_y,
                                   radius+pos_x,
                                   radius+pos_y);
            this.screen.getCanvas().drawOval(rect, paint);
        }
    }

    public void DrawLine(int x1, int y1, int x2, int y2, int color) {
        if (this.screen.getCanvas() != null) {
            Paint paint = new Paint();
            paint.setColor(color);
            this.screen.getCanvas().drawLine(x1, y1, x2, y2, paint);
        }
    }
}
