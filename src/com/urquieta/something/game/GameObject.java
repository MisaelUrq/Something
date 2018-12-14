package com.urquieta.something.game;

import com.urquieta.something.game.util.Vec2;
import com.urquieta.something.platform.Renderer;


public abstract class GameObject {
    protected Vec2 position;
    protected Vec2 delta_position;
    final protected Renderer renderer;
    protected double  animation_current_time;
    protected boolean animation_on;
    protected int color;

    protected Vec2 DEBUG_area_position_1;         // TODO(Misael Urquieta): Find a better name.
    protected Vec2 DEBUG_area_position_2;         // TODO(Misael Urquieta): Find a better name.
    private   Vec2 position_to_go;

    public GameObject(Renderer renderer, Vec2 position) {
        this.position = position;
        this.renderer = renderer;
        this.DEBUG_area_position_1 = new Vec2(position.x-0.01f, position.y+0.01f);
        this.DEBUG_area_position_2 = new Vec2(position.x+.01f,  position.y-0.01f);
        this.animation_current_time = 0;
        this.animation_on   = false;
        this.delta_position = new Vec2(0, 0);
        this.position_to_go = position;
        this.color = 0xFF000000;
    }


    public void ComputeMove(float t, Vec2 acceleration) {
        Vec2 v = GetDeltaPosition();
        Vec2 p = GetPosition();

        Vec2 offset = acceleration.Mul(t*t*.5f);
        offset.AddSelf(v.Mul(t));
        offset.AddSelf(p);

        SetPosition(offset);
        SetDeltaPosition(acceleration.Mul(t).Add(v));
    }

    public void EndMove() {
        SetPosition(GetPositionToGo());
        SetDeltaPosition(0, 0);
    }

    public final Vec2 GetPosition() {
        return this.position;
    }

    public Vec2 GetDeltaPosition() {
        return this.delta_position;
    }

    public void SetDeltaPosition(Vec2 position) {
        this.delta_position = position;
    }

    public void SetDeltaPosition(float x, float y) {
        this.delta_position.x = x;
        this.delta_position.y = y;
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

    public void PositionToMove(Vec2 position) {
        this.position_to_go = position;
    }

    public void PositionToMove(float x, float y) {
        PositionToMove(new Vec2(x, y));
    }

    public Vec2 GetPositionToGo() {
        return this.position_to_go;
    }


}
