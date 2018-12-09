package com.urquieta.something.game.board;

import com.urquieta.something.platform.Renderer;
import com.urquieta.something.game.board.GameBoardObject;
import com.urquieta.something.game.util.Vec2;

public class Circle extends GameBoardObject {
    private float radius;
    private int color;

    // TODO(Misael Urquieta): In the game_state make a thing to make
    // thing depending if we are in realease of debug.
    public Circle(Renderer renderer, Vec2 position, float radius, int color) {
        super(renderer, position);
        this.radius  = radius;
        this.color   = color;
        // TODO(Misael Urquieta): Make the move better, I think we
        // need to add the screen length to the move.
        this.DEBUG_area_position_1 = this.position.Move(radius);
        this.DEBUG_area_position_2 = this.position.Move(-radius);
    }

    public Circle(Renderer renderer, float x, float y, float radius, int color) {
        this(renderer, new Vec2(x, y), radius, color);
    }

    public void SetColor(byte r, byte g, byte b, byte a) {
        this.color = (a << 24) | (r << 16) | (b << 8) | a;
    }

    public void SetColor(int color) {
        this.color = color;
    }

    public int GetColor() {
        return this.color;
    }

    public float GetRadius() {
        return this.radius;
    }

    public void Draw() {
        super.renderer.DrawCircle(this.position.x, this.position.y,
                                  this.radius, this.color);
    }

    public void Move(Vec2 vector) {
        super.position = super.position.Add(vector);
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

    public String toString() {
        return "Circle: "+super.GetPosition()+" - radius("+this.radius+") - color("+this.color+")";
    }
}
