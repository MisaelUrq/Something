package com.urquieta.something.platform.pc;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.urquieta.something.platform.InputEvent;
import com.urquieta.something.platform.pc.View;
import com.urquieta.something.platform.Screen;
import com.urquieta.something.game.util.Vec2;

public class PCInput implements KeyListener, MouseMotionListener, MouseListener {
    private static final int MAX_KEYS = 256;
    public boolean[] keys; // For now none are used.
    InputEvent input_event;

    public PCInput() {
        this.input_event = new InputEvent();
        this.keys = new boolean[MAX_KEYS];
    }

    public void Update() {
        // NOTE(Misael): This is for any keys that need to be pass for
        // the PC build. This will only be for debbug purposes.
        InputEvent event = this.input_event;
        if (event.prev_type == InputEvent.TOUCH_UP && event.type == InputEvent.TOUCH_CLIC) {
            event.type = InputEvent.TOUCH_CLIC;
            SetEvent(InputEvent.TOUCH_CLIC, 0);
        } else {
            event.type = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {
        // ...
    }

    @Override
    public void keyPressed(KeyEvent event) {
        int keycode = event.getKeyCode();
        if (keycode >= 0 && keycode < MAX_KEYS) {
            keys[keycode] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        int keycode = event.getKeyCode();
        if (keycode >= 0 && keycode < MAX_KEYS) {
            keys[keycode] = false;
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        this.ComputeMouseCoordinatesAndPrevState(event);
        this.input_event.type = InputEvent.TOUCH_DRAGGED;
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        this.ComputeMouseCoordinatesAndPrevState(event);
        this.input_event.type = InputEvent.TOUCH_MOVED;
    }

    @Override
    public void mouseExited(MouseEvent event) {

    }

    @Override
    public void mouseEntered(MouseEvent event) {

    }

    @Override
    public void mouseReleased(MouseEvent event) {
        this.ComputeMouseCoordinatesAndPrevState(event);
        this.input_event.type = InputEvent.TOUCH_UP;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        this.ComputeMouseCoordinatesAndPrevState(event);
        this.input_event.type = InputEvent.TOUCH_DOWN;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        this.input_event.prev_type = this.input_event.type;
        this.input_event.type = InputEvent.TOUCH_CLIC;
        this.ComputeMouseCoordinatesAndPrevState(event);
    }

    public InputEvent GetInputEvent() {
        return this.input_event;
    }

    public Vec2 GetCursorPosition() {
        return this.input_event.cursor_position;
    }

    protected void SetEvent(int type, int prev_type) {
        this.input_event.type = type;
        this.input_event.prev_type = prev_type;
    }

    private void ComputeMouseCoordinatesAndPrevState(MouseEvent event) {
        this.input_event.cursor_position.x =   ((float)event.getX() / (float)Screen.width)  * 2 - 1.0f;
        this.input_event.cursor_position.y = -(((float)event.getY() / (float)Screen.height) * 2 - 1.0f);
        this.input_event.cursor_position_pixels.x = event.getX();
        this.input_event.cursor_position_pixels.y = event.getY();
    }
}
