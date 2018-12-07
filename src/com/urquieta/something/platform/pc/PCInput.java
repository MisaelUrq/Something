package com.urquieta.something.platform.pc;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.urquieta.something.platform.InputEvent;
import com.urquieta.something.platform.pc.View;
import com.urquieta.something.platform.Screen;

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
            System.out.println("Key["+(char)keycode+"]: Pressed.");
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        int keycode = event.getKeyCode();
        if (keycode >= 0 && keycode < MAX_KEYS) {
            keys[keycode] = false;
            System.out.println("Key["+(char)keycode+"]: Released");
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        this.input_event.type = InputEvent.TOUCH_DRAGGED;
        this.ComputeMouseCoordinates(event);
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        this.input_event.type = InputEvent.TOUCH_MOVED;
        this.ComputeMouseCoordinates(event);
    }

    @Override
    public void mouseExited(MouseEvent event) {

    }

    @Override
    public void mouseEntered(MouseEvent event) {

    }

    @Override
    public void mouseReleased(MouseEvent event) {
        this.input_event.type = InputEvent.TOUCH_UP;
        this.ComputeMouseCoordinates(event);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        this.input_event.type = InputEvent.TOUCH_DOWN;
        this.ComputeMouseCoordinates(event);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        this.input_event.type = InputEvent.TOUCH_DOWN;
        this.ComputeMouseCoordinates(event);
    }

    public InputEvent GetInputEvent() {
        return this.input_event;
    }

    public float getTouchX() {
        return this.input_event.x;
    }

    public float getTouchY() {
        return this.input_event.y;
    }

    private void ComputeMouseCoordinates(MouseEvent event) {
        this.input_event.x =  ((float)event.getX() / (float)Screen.width) * 2 - 1.0f;
        this.input_event.y = -(((float)event.getY() / (float)Screen.height) * 2 - 1.0f);
        this.input_event.x_in_pixels = event.getX();
        this.input_event.y_in_pixels = event.getY();
    }
}
