package com.urquieta.something.platform.renderer.figures;

import com.urquieta.something.game.util.Color;
import com.urquieta.something.platform.renderer.models.TextureModel;
import com.urquieta.something.platform.ImageLoader;
import com.urquieta.something.platform.Image;

public class Circle {
    private TextureModel model;
    public Circle(float x, float y, float z, float radius, Color color) {
        float positions[] = {
            x-radius, y+radius, z,
            x-radius, y-radius, z,
            x+radius, y-radius, z,
            x+radius, y+radius, z
        };
        short draw_order[] = {
            0, 1, 2, 0, 2, 3
        };
        float texture_coords[] = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
        };
        model = new TextureModel(positions, draw_order, texture_coords, "circle.png", color);
    }

    public boolean HasBeenCreated() {
        return model.HasBeenCreated();
    }

    public void Create() {
        model.CreateProgram();
    }

    public void UpdatePosition(float x, float y, float z, float radius) {
        float positions[] = {
            x-radius, y+radius, z,
            x-radius, y-radius, z,
            x+radius, y-radius, z,
            x+radius, y+radius, z
        };
        model.UpdatePosition(positions);
    }

    public void Draw(float[] mvp_matrix) {
        model.Draw(mvp_matrix);
    }

    public void Delete() {
        model.Delete();
    }

    public String toString() {
        return "CIRCLE => " + model.toString();
    }
}
