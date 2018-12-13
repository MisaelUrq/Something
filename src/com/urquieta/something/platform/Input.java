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

    public InputEvent GetInputEvent() {
        Update();
        InputEvent event = super.GetInputEvent();
        return event;
    }
}
