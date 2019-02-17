package com.urquieta.something.game.util;

public class Color {
    public int red;
    public int green;
    public int blue;
    public int alfa;

    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public Color(int r, int g, int b, int a) {
        red   = r;
        green = g;
        blue  = b;
        alfa  = a;
    }

    public Color(int color) {
        alfa  = color >> 24;
        red   = (color >> 16) & 0xFF;
        green = (color >> 8)  & 0xFF;
        blue  = (color >> 0)  & 0xFF;
    }

    public int GetColorInt() {
        return (alfa << 24) | (red << 16) | (green << 8) | (blue << 0);
    }

    public void Blend(Color other) {
        double luminece = 0.2126*red + 0.7152*green + 0.0722*blue;
        red   = (int)((float)other.red   * luminece / 255.0);
        green = (int)((float)other.green * luminece / 255.0);
        blue  = (int)((float)other.blue  * luminece / 255.0);
    }

    public float[] GetNormalizeArray() {
        float result[] = { red/255.0f, green/255.0f, blue/255.0f, alfa/255.0f };
        return result;
    }

    public String toString() {
        return String.format("Color: { alfa: %d; red: %d; green: %d; blue: %d; }", alfa, red, green, blue);
    }
}
