package com.urquieta.something.game.board;

import com.urquieta.something.platform.Renderer;
import com.urquieta.something.game.board.GameBoardObject;
import com.urquieta.something.game.util.Vec2;

public class Circle extends GameBoardObject {
    private float radius;
    private final double bounce_animation_time;

    public Circle(Renderer renderer, Vec2 position, float radius, int color) {
        super(renderer, position, color, true, false, true);
        this.radius  = radius;
        this.DEBUG_area_position_1 = this.position.Add(-radius, radius);
        this.DEBUG_area_position_2 = this.position.Add(radius, -radius);
        this.bounce_animation_time = 0.7;
    }

    public Circle(Renderer renderer, float x, float y, float radius, int color) {
        this(renderer, new Vec2(x, y), radius, color);
    }

    public float GetRadius() {
        return this.radius;
    }

    @Override
    public String ToFileFormat() {
        String Result = "C" + Integer.toHexString(color);
        return Result;
    }
    
    @Override
    public void Draw() {
        super.renderer.DrawCircle(this.position.x, this.position.y,
                                  this.radius, this.color);
    }

    @Override
    public void Update(double delta) {
        if (this.animation_on) {
            this.animation_current_time += delta;
            if (this.animation_current_time < this.bounce_animation_time) {
                this.Move(new Vec2(0, 0.1f));
            }
        }
    }

    public boolean HasCollide(Vec2 position) {
        return HasCollide(position.x, position.y);
    }

    public boolean HasCollide(float x, float y) {
        if ((x > (this.position.x - radius) && x < (this.position.x + radius)) &&
            (y > (this.position.y - radius) && y < (this.position.y + radius))) {
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Circle: "+super.GetPosition()+" - radius("+this.radius+") - color("+this.color+")";
    }
}
