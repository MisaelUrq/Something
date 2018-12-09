package com.urquieta.something.game.board;

import com.urquieta.something.game.GameObject;
import com.urquieta.something.game.board.Circle;
import com.urquieta.something.game.board.GameBoardObject;
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
    private boolean player_move_init;
    private ArrayList<GameBoardObject> circles_conected;
    private float object_padding;

    public GameBoard(Renderer r, int width, int height) {
        super(r, new Vec2(0, 0));
        this.width = width;
        this.height = height;
        this.r = renderer;
        this.random = new Random();
        this.cursor_position = new Vec2(0, 0);
        this.color_palette = new int[5];
        this.player_dragged = false;
        this.player_move_init = false;
        this.object_padding = 0.15f;
        this.circles_conected = new ArrayList<GameBoardObject>();

        // TODO(Misael): Colors should not be random, insted they
        // should be pick up from a pre-made color palettes.
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
        float start_x_position = -((this.object_padding * (float)(this.width+1)  / 2.0f));
        float start_y_position =  ((this.object_padding * (float)(this.height+1) / 2.0f));
        float y_position = start_y_position;
        float radius = ((objects_proportion / this.r.GetScreen().GetWidth()) +
                        (objects_proportion / this.r.GetScreen().GetHeight())) / 2;
        for (int y = 0; y < height; y++) {
            float x_position = start_x_position;
            y_position -= this.object_padding;
            for (int x = 0; x < width; x++) {
                x_position += this.object_padding;
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

    public void Update() {
        if (this.player_dragged) {
            if (this.player_move_init == false) {
                for (Circle circle: this.circles_array) {
                    if (circle.HasCollide(this.cursor_position) &&
                        (this.circles_conected.contains(circle) == false)) {
                        this.circles_conected.add(circle);
                        this.player_move_init = true;
                        break;
                    }
                }
            }
            else {
                Circle last_circle_conected = (Circle)this.circles_conected.get(this.circles_conected.size()-1);
                int color = ((Circle)this.circles_conected.get(0)).GetColor();
                Vec2 last_index_position = GetIndexPositionFromScreenPosition(last_circle_conected.GetPosition());
                Circle[] circles_around_position = GetCirclesAroundIndexPosition(last_index_position);
                for (Circle c: circles_around_position) {
                    if (c.HasCollide(this.cursor_position) &&
                        (color == c.GetColor()) &&
                        (this.circles_conected.contains(c) == false)) {
                        this.circles_conected.add(c);
                        break;
                    }
                }
            }
        }
        else {
            if (circles_conected.size() > 1) {
                for (GameBoardObject object: this.circles_conected) {
                    Circle circle = (Circle)object;
                    Vec2 position = GetIndexPositionFromScreenPosition(circle.GetPosition());
                    int index = VecToIndex(position);
                    this.circles_array[index] = new Circle(this.r, circle.GetPosition(), 0.05f, 0xFFFFFFFF);
                }
            }
            this.circles_conected.clear();
            this.player_move_init = false;
            UpdateCirclesPosition();
        }
    }

    // TODO(Misael Urquieta): Fix the update of the circle falling, it
    // does not move all of the objects, and we like animation! This
    // sistem does not work for that.
    private void UpdateCirclesPosition() {
        for (int y = this.height-1; y > 0; y--) {
            for (int x = 0; x < this.width; x++) {
                int index = y*this.width+x;
                if (this.circles_array[index].GetColor() == 0xFFFFFFFF) {
                    int previous_index = (y-1)*this.width+x;
                    Circle Temp = this.circles_array[index];
                    this.circles_array[index] = this.circles_array[previous_index];
                    this.circles_array[index].Move(new Vec2(0, -object_padding));
                    this.circles_array[previous_index] = Temp;
                }
            }
        }

        for (int x = 0; x < this.width; x++) {
            if (this.circles_array[x].GetColor() == 0xFFFFFFFF) {
                this.circles_array[x] = new Circle(this.r, this.circles_array[x].GetPosition(),
                                                   0.05f,
                                                   this.color_palette[Math.abs(random.nextInt() % 5)]);
            }
        }
    }

    private int VecToIndex(Vec2 position) {
        return (int)position.y * this.width + (int)position.x;
    }

    private Circle[] GetCirclesAroundIndexPosition(Vec2 position) {
        int array_size = 4;
        int size = 0;
        int x = (int)position.x;
        int y = (int)position.y;

        if (x < 1) { array_size--; }
        if (y < 1) { array_size--; }
        if (x >= this.width-1)  { array_size--; }
        if (y >= this.height-1) { array_size--; }

        Circle[] array = new Circle[array_size];
        if (x < 1 == false) {
            array[size++] = this.circles_array[y * this.width + (x-1)];
        }
        if (x >= this.width-1 == false) {
            array[size++] = this.circles_array[y * this.width + (x+1)];
        }
        if (y < 1 == false) {
            array[size++] = this.circles_array[(y-1) * this.width + x];
        }
        if (y >= this.height-1 == false) {
            array[size++] = this.circles_array[(y+1) * this.width + x];
        }
        return array;
    }

    private Vec2 GetIndexPositionFromScreenPosition(Vec2 entity_position) {
        float start_x_position = -((this.object_padding * (float)(this.width+1)  / 2.0f));
        float start_y_position =  ((this.object_padding * (float)(this.height+1) / 2.0f));

        float x =  (entity_position.x - start_x_position) / this.object_padding;
        float y = -(entity_position.y - start_y_position) / this.object_padding;
        int  x_index = Math.round(x)-1;
        int  y_index = Math.round(y)-1;
        Vec2 index_position = new Vec2(x_index, y_index);
        return index_position;
    }

    public void Draw() {
        for (Circle circle : this.circles_array) {
            if (circle.HasCollide(this.cursor_position)) {
                circle.DEBUG_DrawPostionLocation();
            }

            circle.Draw();
        }

        if (this.circles_conected.isEmpty() == false) {
            Vec2 current_position = this.cursor_position;
            for (int index = this.circles_conected.size()-1;
                 index >= 0; index--) {
                Circle c = (Circle)this.circles_conected.get(index);
                Vec2 position = c.GetPosition();
                this.r.DrawLine(current_position, position, 0.02f, c.GetColor());
                current_position = position;
            }
        }
    }

}
