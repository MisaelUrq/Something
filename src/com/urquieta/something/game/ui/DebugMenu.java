package com.urquieta.something.game.ui;

import com.urquieta.something.game.GameObject;
import com.urquieta.something.game.ui.Button;
import com.urquieta.something.game.util.Vec2;
import com.urquieta.something.platform.InputEvent;
import com.urquieta.something.platform.Renderer;
import com.urquieta.something.platform.Sound;

import com.urquieta.something.game.GameState;

import java.util.ArrayList;

public class DebugMenu extends GameObject {
    private Button open_close;
    private Button show_hide_fps;
    private boolean is_open;
    private InputEvent event;
    private boolean is_done;
    
    ArrayList<GameObject> objects;

    public DebugMenu(Renderer renderer, Sound sound) {
        super(renderer, new Vec2(-2f, 0));
        this.objects  = new ArrayList<GameObject>();
        this.open_close = new Button(renderer, new Vec2(-.85f, .95f), .3f, .1f, "Debug",
                                     0xffafafaf, 0xff2d2d2d, sound);
        this.show_hide_fps = new Button(renderer, new Vec2(-1.7f, .9f), .2f, .1f, "FPS",
                                        0xffafafaf, 0xff2d2d2d, sound);
        this.objects.add(this.show_hide_fps);
        
        this.is_open = false;
        this.event = new InputEvent();
        this.is_done = true;
    }

    public void UpdateEvent(InputEvent event) {
        this.event = event;
    }

    @Override
    public void Draw() {
        if (is_open || is_done == false) {
            super.renderer.DrawRect(position.x, 1f, position.x+1.3f, -1f, 0x88121212);
            for (GameObject object: objects) {
                object.Draw();
            }
        }
        this.open_close.Draw();
    }

    public boolean IsMenuOpen() {
        return this.is_open;
    }

    @Override
    public void Update(double delta) {
        if (this.is_done && open_close.IsPressed(event) && event.type == InputEvent.TOUCH_CLIC) {
            GameState.is_menu_active = !GameState.is_menu_active;
            float distance_to_move = 1f;
            if (!this.is_open) {
                this.is_open = true;
                distance_to_move *= -1;
            }
            else
            {
                this.is_open = false;
            }
            for (GameObject object: objects) {
                object.PositionToMove(object.GetPosition().Sub(distance_to_move, 0));
            }
            open_close.PositionToMove(open_close.GetPosition().Sub(distance_to_move, 0));
            PositionToMove(GetPosition().Sub(distance_to_move, 0));
            this.is_done = false;
        }

        if (show_hide_fps.IsPressed(event)) {
            GameState.state ^= GameState.SHOW_FPS;
        }

        if (is_done == false) {
            float speed = (this.is_open) ? .05f : -0.05f;
            Vec2 a = new Vec2(speed, 0); // Acceleration
            float t = (float)delta;
            for (GameObject object: objects) {
                object.ComputeMove(t, a);
            }
            open_close.ComputeMove(t, a);            
            ComputeMove(t*1.015f, a);

            float x_current = this.open_close.GetPosition().x;
            float x_dest = this.open_close.GetPositionToGo().x;
            
            boolean move_ended = false;
            if ((is_open && x_current > x_dest) || (is_open == false && x_current < x_dest)) {
                move_ended = true;
                this.is_done = true;
            }
            if (move_ended) {
                for (GameObject object: objects) {
                    object.EndMove();
                }
                open_close.EndMove();
                EndMove();
            }
        }
    }

}
