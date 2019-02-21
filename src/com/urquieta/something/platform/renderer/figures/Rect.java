package com.urquieta.something.platform.renderer.figures;

import com.urquieta.something.game.util.Color;
import com.urquieta.something.platform.renderer.models.RawModel;

public class Rect {
    private RawModel raw_model;

    public Rect(float x, float y, float width, float height, float z, Color color) {
        float positions[] = {
            x,     y,      z,
            x,     height, z,
            width, height, z,
            width, y,      z
        };
        short draw_order[] = {
            0, 1, 2, 0, 2, 3
        };
        raw_model = new RawModel(positions, draw_order, color);
    }

    public boolean HasBeenCreated() {
        return raw_model.HasBeenCreated();
    }

    public void Create() {
        raw_model.CreateProgram();
    }

    public void UpdatePosition(float x, float y, float width, float height, float z) {
        float position[] = {
            x,     y,      z,
            x,     height, z,
            width, height, z,
            width, y,      z
        };
        raw_model.UpdatePosition(position);
    }

    public void Draw(float[] mvp_matrix) {
        raw_model.Draw(mvp_matrix);
    }

    public void Delete() {
        raw_model.Delete();
    }

    public String toString() {
        return "RECT => " + raw_model.toString();
    }
}
