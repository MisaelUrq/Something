package com.urquieta.something.game.board;

import com.urquieta.something.game.GameObject;
import com.urquieta.something.game.board.Circle;
import com.urquieta.something.game.util.Vec2;

// TODO(Misael): Make the renderer static, so we can envoke it from
// everywere
import com.urquieta.something.platform.Renderer;
import java.util.Random;

public class GameBoard extends GameObject {

    private Circle[] circles_array;
    private int width;
    private int height;
    private final float objects_proportion = 10;
    private Renderer r;
    private Vec2 cursor_position;
    private Random random;
    private int[] color_palette;

    public GameBoard(Renderer r, int width, int height) {
        super(r, new Vec2(0, 0));
        this.width = width;
        this.height = height;
        this.r = renderer;
        this.random = new Random();
        this.cursor_position = new Vec2(0, 0);
        this.color_palette = new int[5];
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

    public void Draw() {
        for (Circle circle : this.circles_array) {
            if (circle.HasCollide(this.cursor_position)) {
                circle.DEBUG_DrawPostionLocation();
            }

            circle.Draw();
        }
    }

}
