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

    public Vec2 Add(float number) {
        return new Vec2(this.x + number, this.y + number);
    }

    public Vec2 Substract(float number) {
        return this.Add(-number);
    }

    public Vec2 Move(float number) {
        return new Vec2(this.x - number, this.y + number);
    }

    public String toString() {
        return "Vec2("+this.x+","+this.y+")";
    }
}
