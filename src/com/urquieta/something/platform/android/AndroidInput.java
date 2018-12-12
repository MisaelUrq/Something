package com.urquieta.something.platform.android;

import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;

import com.urquieta.something.platform.InputEvent;

public class AndroidInput implements OnTouchListener {
    private boolean is_touched;
    InputEvent input_event;

    public AndroidInput() {
        this.input_event = new InputEvent();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) {
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                input_event.type = InputEvent.TOUCH_DOWN;
                is_touched = true;
                break;
            case MotionEvent.ACTION_MOVE:
                input_event.type = InputEvent.TOUCH_DRAGGED;
                is_touched = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                input_event.type = InputEvent.TOUCH_UP;
                is_touched = false;
                break;
            }

            input_event.cursor_position.x =  (((event.getX() / v.getWidth())  * 2) - 1.0f);
            input_event.cursor_position.y = -(((event.getY() / v.getHeight()) * 2) - 1.0f);
            return true;
        }
    }

    public float getTouchX() {
        synchronized(this) {
            return this.input_event.cursor_position.x;
        }
    }

    public float getTouchY() {
        synchronized(this) {
            return this.input_event.cursor_position.y;
        }
    }

    public InputEvent GetInputEvent() {
        synchronized(this) {
            return this.input_event;
        }
    }
}
