package com.urquieta.something.game.util;

public class Vec2 {
    public float x;
    public float y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void AddSelf(Vec2 other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void AddSelf(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public Vec2 Add(Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }

    public Vec2 Add(float x, float y) {
        return new Vec2(this.x + x, this.y + y);
    }

    public Vec2 Sub(Vec2 other) {
        return new Vec2(this.x - other.x, this.y - other.y);
    }

    public Vec2 Sub(float x, float y) {
        return new Vec2(this.x - x, this.y - y);
    }

    public Vec2 Mul(float n) {
        return new Vec2(n*x, n*y);
    }

    public void MulSelf(float n) {
        this.x = n*this.x;
        this.y = n*this.y;
    }

    public Vec2 Substract(float x, float y) {
        return this.Add(-x, -y);
    }

    public boolean Equals(Vec2 B) {
        float compare = 0.00001f;
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
