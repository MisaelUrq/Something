package com.urquieta.something.platform;

// @Android<>import  com.urquieta.something.platform.android.AndroidInput;
import com.urquieta.something.platform.pc.PCInput;// @PC

// @Android<>import android.view.View;
import com.urquieta.something.platform.pc.View;// @PC

import com.urquieta.something.platform.InputEvent;

public class Input extends PCInput { // @Class
    public Input() {
        super();
    }

    public void GetMousePosition(float x, float y) {
        x = super.getTouchX();
        y = super.getTouchY();
    }

    public InputEvent GetInputEvent() {
        return super.GetInputEvent();
    }
}
