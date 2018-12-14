package com.urquieta.something.game.ui;

import com.urquieta.something.game.GameObject;
import com.urquieta.something.game.ui.Button;
import com.urquieta.something.game.util.Vec2;
import com.urquieta.something.platform.InputEvent;
import com.urquieta.something.platform.Renderer;

public class DebugMenu extends GameObject {
    private Button open_close;
    private boolean is_open;
    private InputEvent event;
    private boolean is_done;

    public DebugMenu(Renderer renderer) {
        super(renderer, new Vec2(-1.6f, 0));
        this.open_close = new Button(renderer, new Vec2(-.85f, .95f), .3f, .1f, "Debug",
                                     0xffafafaf, 0xff2d2d2d);
        this.is_open = false;
        this.event = new InputEvent();
        this.is_done = true;
    }

    public void UpdateEvent(InputEvent event) {
        this.event = event;
    }

    @Override
    public void Draw() {
        float extra_width;
        if (is_open) {
            extra_width = .9f;
        } else {
            extra_width = 0.6f;
        }
        super.renderer.DrawRect(position.x-3f, 1f, position.x+extra_width, -1f, 0x88121212);
        this.open_close.Draw();
    }

    public boolean IsMenuOpen() {
        return this.is_open;
    }

    @Override
    public void Update(double delta) {
        if (this.is_done && open_close.IsPressed(event) && event.type == InputEvent.TOUCH_CLIC) {
            if (this.is_open) {
                this.is_open = false;
                this.open_close.PositionToMove(open_close.GetPosition().Sub(.9f, 0));
                PositionToMove(GetPosition().Sub(.9f, 0));
            } else {
                this.is_open = true;
                this.open_close.PositionToMove(open_close.GetPosition().Add(.9f, 0));
                PositionToMove(GetPosition().Add(.9f, 0));
            }
            this.is_done = false;
        }

        if (is_done == false) {
            float speed = (this.is_open) ? .01f : -0.01f;
            Vec2 a = new Vec2(speed, 0); // Acceleration
            this.open_close.ComputeMove((float)delta, a);
            ComputeMove((float)delta, a);

            float x_current = this.open_close.GetPosition().x;
            float x_dest = this.open_close.GetPositionToGo().x;
            if (is_open && x_current > x_dest) {
                this.open_close.EndMove();
                EndMove();
                this.is_done = true;
            } else if (is_open == false && x_current < x_dest) {
                this.open_close.EndMove();
                EndMove();
                this.is_done = true;
            }
        }


    }



}
