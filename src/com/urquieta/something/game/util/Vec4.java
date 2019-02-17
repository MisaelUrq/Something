package com.urquieta.something.game.util;

public class Vec4 {
    public float x;
    public float y;
    public float z;
    public float w;

    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public String toString() {
        return "Vec4("+this.x+","+this.y+", "+this.z+", "+this.w+")";
    }
}
