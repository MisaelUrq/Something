package com.urquieta.something.platform.android;


import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;

import com.urquieta.something.platform.InputEvent;

public class AndroidInput implements OnTouchListener {
    private boolean is_touched;
    InputEvent input_event;

    public AndroidInput(View view) {
        view.setOnTouchListener(this);
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

            input_event.x = (int)(event.getX());
            input_event.y = (int)(event.getY());
            return true;
        }
    }

    public int getTouchX() {
        synchronized(this) {
            return this.input_event.x;
        }
    }

    public int getTouchY() {
        synchronized(this) {
            return this.input_event.y;
        }
    }

    public InputEvent GetInputEvent() {
        synchronized(this) {
            return this.input_event;
        }
    }
}
