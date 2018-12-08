package com.urquieta.something.game;

import com.urquieta.something.game.util.Vec2;
import com.urquieta.something.platform.Renderer;


public abstract class GameObject {
    protected Vec2 position;
    final protected Renderer renderer;
    final private int debug_output_color;

    protected Vec2 DEBUG_area_position_1;         // TODO(Misael Urquieta): Find a better name.
    protected Vec2 DEBUG_area_position_2;         // TODO(Misael Urquieta): Find a better name.

    public GameObject(Renderer renderer, Vec2 position) {
        this.position = position;
        this.renderer = renderer;
        this.debug_output_color = 0x55FF0000;
        this.DEBUG_area_position_1 = new Vec2(0, 0);
        this.DEBUG_area_position_2 = new Vec2(0, 0);
    }

    public abstract void Draw();

    public void DEBUG_DrawPostionLocation() {
        this.renderer.DrawLine(this.position.x, 1f,
                               this.position.x, -1f,
                               this.debug_output_color);
        this.renderer.DrawLine(-1f, this.position.y,
                               1f, this.position.y,
                               this.debug_output_color);
    }

    public void DEBUG_DrawRectOverArea() {
        this.renderer.DrawRect(this.DEBUG_area_position_1.x, this.DEBUG_area_position_1.y,
                               this.DEBUG_area_position_2.x, this.DEBUG_area_position_2.y,
                               this.debug_output_color);
    }
}
