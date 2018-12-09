package com.urquieta.something.game.board;

import com.urquieta.something.game.GameObject;
import com.urquieta.something.game.board.Circle;
import com.urquieta.something.game.util.Vec2;

// TODO(Misael): Make the renderer static, so we can envoke it from
// everywere
import com.urquieta.something.platform.Renderer;
import java.util.Random;
import java.util.ArrayList;

public class GameBoard extends GameObject {
    private Circle[] circles_array;
    private int width;
    private int height;
    private final float objects_proportion = 10;
    private Renderer r;
    private Vec2 cursor_position;
    private Random random;
    private int[] color_palette;
    private boolean player_dragged;
    private ArrayList<Vec2> circles_conected_positions;

    public GameBoard(Renderer r, int width, int height) {
        super(r, new Vec2(0, 0));
        this.width = width;
        this.height = height;
        this.r = renderer;
        this.random = new Random();
        this.cursor_position = new Vec2(0, 0);
        this.color_palette = new int[5];
        this.player_dragged = false;
        this.circles_conected_positions = new ArrayList<Vec2>();
        for (int index = 0; index < 5; index++) {
            this.color_palette[index] = ((0xFF << 24) |
                                         ((byte)(random.nextInt() % 0xFF) << 16) |
                                         ((byte)(random.nextInt() % 0xFF) <<  8) |
                                         ((byte)(random.nextInt() % 0xFF)));
        }
        this.circles_array = InitCircleArray();
    }

    private Circle[] InitCircleArray() {
        Circle[] array = new Circle[width * height];
        float padding_x = 0.15f;
        float padding_y = 0.15f;
        float start_x_position = -((padding_x * (float)(this.width+1)  / 2.0f));
        float start_y_position =  ((padding_y * (float)(this.height+1) / 2.0f));
        float y_position = start_y_position;
        float radius = ((objects_proportion / this.r.GetScreen().GetWidth()) +
                        (objects_proportion / this.r.GetScreen().GetHeight())) / 2;
        for (int y = 0; y < height; y++) {
            float x_position = start_x_position;
            y_position -= padding_y;
            for (int x = 0; x < width; x++) {
                x_position += padding_x;
                int color = this.color_palette[Math.abs(random.nextInt() % 5)];
                array[width * y + x] = new Circle(this.r, x_position, y_position, radius,
                                                  color);
            }
        }
        return array;
    }

    public void UpdateCursor(Vec2 new_position) {
        this.cursor_position = new_position;
    }

    public void PlayerDragged(boolean new_status) {
        this.player_dragged = new_status;
    }

    // TODO(Misael Urquieta): Make this so only the circles that are
    // next to eachother can actually be conected, and of the same
    // color.
    public void Update() {
        if (this.player_dragged) {
            for (Circle circle: this.circles_array) {
                if (circle.HasCollide(this.cursor_position) &&
                    (this.circles_conected_positions.contains(circle.GetPosition()) == false)) {
                    this.circles_conected_positions.add(circle.GetPosition());
                }
            }
        }
        else {
            this.circles_conected_positions.clear();
        }
    }

    public void Draw() {
        for (Circle circle : this.circles_array) {
            if (circle.HasCollide(this.cursor_position)) {
                circle.DEBUG_DrawPostionLocation();
            }

            circle.Draw();
        }

        if (this.circles_conected_positions.isEmpty() == false) {
            Vec2 current_position = this.cursor_position;
            for (int index = this.circles_conected_positions.size()-1;
                 index >= 0; index--) {
                Vec2 position = this.circles_conected_positions.get(index);
                this.r.DrawLine(current_position, position, 0.02f, 0xFFFF00FF);
                current_position = position;
            }
        }
    }

}
