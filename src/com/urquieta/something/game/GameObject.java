package com.urquieta.something.game;

import com.urquieta.something.game.util.Vec2;
import com.urquieta.something.platform.Renderer;


public abstract class GameObject {
    protected Vec2 position;
    final protected Renderer renderer;
    protected double  animation_current_time;
    protected boolean animation_on;

    protected Vec2 DEBUG_area_position_1;         // TODO(Misael Urquieta): Find a better name.
    protected Vec2 DEBUG_area_position_2;         // TODO(Misael Urquieta): Find a better name.

    public GameObject(Renderer renderer, Vec2 position) {
        this.position = position;
        this.renderer = renderer;
        this.DEBUG_area_position_1 = new Vec2(0, 0);
        this.DEBUG_area_position_2 = new Vec2(0, 0);
        this.animation_current_time = 0;
        this.animation_on   = false;
    }

    public final Vec2 GetPosition() {
        return this.position;
    }

    public void SetPosition(Vec2 position) {
        this.position = position;
    }

    public abstract void Draw();
    public abstract void Update(double delta);

    public void InitAnimation() {
        this.animation_on = true;
        this.animation_current_time = 0;
    }

    public void DEBUG_DrawPostionLocation(int color) {
        this.renderer.DrawLine(this.position.x, 1f,
                               this.position.x, -1f,
                               color);
        this.renderer.DrawLine(-1f, this.position.y,
                               1f, this.position.y,
                               color);
    }

    public void DEBUG_DrawRectOverArea(int color) {
        this.renderer.DrawRect(this.DEBUG_area_position_1.x, this.DEBUG_area_position_1.y,
                               this.DEBUG_area_position_2.x, this.DEBUG_area_position_2.y,
                               color);
    }
}
