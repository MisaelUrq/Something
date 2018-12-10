package com.urquieta.something.game.util;

public class Vec2 {
    public float x;
    public float y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 Add(Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }

    public Vec2 Add(float x, float y) {
        return new Vec2(this.x + x, this.y + y);
    }

    public Vec2 Substract(float x, float y) {
        return this.Add(-x, -y);
    }

    public boolean Equals(Vec2 B) {
        float compare = 0.001f;
        if (Math.abs(this.x - B.x) < compare) {
            if (Math.abs(this.y - B.y) < compare) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "Vec2("+this.x+","+this.y+")";
    }
}
