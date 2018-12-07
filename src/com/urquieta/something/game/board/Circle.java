package com.urquieta.something.game.board;

import com.urquieta.something.platform.Renderer;

public class Circle {
    private float radius;
    private float x;
    private float y;
    private int color;

    public Circle(float x, float y, float radius, int color) {
        this.x       = x;
        this.y       = y;
        this.radius  = radius;
        this.color   = color;
    }

    public void SetColor(int color) {
        this.color = color;
    }

    public void Draw(Renderer r) {
        r.DrawCircle(this.x, this.y, this.radius, this.color);
    }

    public boolean HasCollide(float x, float y) {
        if ((x > (this.x - (radius*2)) && x < (this.x + (radius*2))) &&
            (y > (this.y - (radius*2)) && y < (this.y + (radius*2)))) {
                return true;
        }
        return false;
    }
}
