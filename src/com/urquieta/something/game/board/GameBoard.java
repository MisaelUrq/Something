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
    private GameBoardObject[] objects_array;
    private int width;
    private int height;
    private final float objects_proportion = 14f;
    private Renderer r;
    private Vec2 cursor_position;
    private Random random;
    // TODO(Misael): Make a color class and maybe a color palette class?
    private int[] color_palette;
    private boolean player_dragged;
    private boolean player_move_init;
    private ArrayList<GameBoardObject> objects_connected;
    private float   object_padding;
    private float start_x_position;
    private float start_y_position;
    private boolean is_update_done;

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
        this.objects_connected = new ArrayList<GameBoardObject>();
        this.start_x_position = -((this.object_padding * (float)(this.width+1)  / 2.0f));
        this.start_y_position =  ((this.object_padding * (float)(this.height+1) / 2.0f));
        this.color_palette[0] =  0xFFFF0000;
        this.color_palette[1] =  0xFF00FF00;
        this.color_palette[2] =  0xFF0000FF;
        this.color_palette[3] =  0xFFFF00FF;
        this.color_palette[4] =  0xFF00FFFF;

        this.objects_array = InitGameObjectArray();
        this.is_update_done = true;
    }

    private GameBoardObject[] InitGameObjectArray() {
        GameBoardObject[] array = new GameBoardObject[width * height];
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

    private boolean player_connected_color = false;

    @Override
    public void Update(double delta) {
        if (this.player_dragged && this.is_update_done) {
            if (this.player_move_init == false) {
                this.player_move_init = DidPlayerMakeMoveOnCircles(this.objects_array, this.objects_connected);
            }
            else if (!this.player_connected_color) {
                this.player_connected_color = ConnectPlayerDots(this.objects_array, this.objects_connected);
            }
            if (this.objects_connected.size() > 1) {
                boolean is_rewind = DidPlayerRewind(this.objects_array, this.objects_connected);
                if (this.player_connected_color && is_rewind) {
                    this.player_connected_color = false;
                }
            }
        }
        else {
            if (objects_connected.size() > 1) {
                if (player_connected_color) {
                    int color = this.objects_connected.get(0).GetColor();
                    this.objects_connected.clear();
                    ClearObjectsOfColor(this.objects_array, color);
                    this.player_connected_color = false;
                }
                else {
                    for (GameBoardObject object: this.objects_connected) {
                        DeleteObjectFromArray(this.objects_array, object);
                    }
                }
            }
            this.objects_connected.clear();
            this.player_move_init = false;
        }

        this.time_pass += delta;
        UpdateObjectsArray(this.objects_array);
        if (time_pass > 5) {
            CreateNewObjects(this.objects_array);
            this.time_pass = 0;
        }
        UpdateObjectPositions(delta, this.objects_array);
    }

    private double time_pass = 0;

    private void UpdateObjectPositions(double delta, GameBoardObject[] array) {
        float position_delta = (float)delta * (this.object_padding / 4);
        this.is_update_done = true;
        for (GameBoardObject object: array) {
            if (object.GetPosition().Equals(object.GetPositionToGo()) == false) {
                this.is_update_done = false;
                Vec2 to_move = new Vec2(0, -position_delta);
                object.Move(to_move);
                float y_current = object.GetPosition().y;
                float y_dest = object.GetPositionToGo().y;
                if (y_current < y_dest) {
                    object.SetPosition(object.GetPositionToGo());
                }
            }
        }
    }

    private void CreateNewObjects(GameBoardObject[] array) {
        for (int x = 0; x < this.width; x++) {
            if (array[x].CanSpaceBeUsed()) {
                Vec2 position = new Vec2(this.start_x_position + ((x+1)*this.object_padding),
                                         this.start_y_position);
                float radius = ((objects_proportion / this.r.GetScreen().GetWidth()) +
                                (objects_proportion / this.r.GetScreen().GetHeight())) / 2;
                Circle object = new Circle(this.r, position, radius,
                                           this.color_palette[Math.abs(random.nextInt() % 5)]);
                object.PositionToMove(object.GetPosition().Substract(0, this.object_padding));
                array[x] = object;
            }
        }
    }

    private void ClearObjectsOfColor(GameBoardObject[] array, int color) {
        for (GameBoardObject object: array) {
            if (object.GetColor() == color) {
                DeleteObjectFromArray(array, object);
            }
        }
    }

    private void DeleteObjectFromArray(GameBoardObject[] array, GameBoardObject object) {
        Vec2 position = GetIndexPositionFromScreenPosition(object.GetPosition());
        int index = VecToIndex(position);
        array[index] = new GameBoardObject(this.r, object.GetPosition());
    }

    private void UpdateObjectsArray(GameBoardObject[] array) {
        for (int x = 0; x < this.width; x++) {
            for (int y = this.height-1; y > 0; y--) {
                int index = y*this.width+x;
                if (array[index].CanSpaceBeUsed()) {
                    for (int j = y-1; j >= 0; j--) {
                        int jndex = j*this.width+x;
                        if (array[jndex].CanSpaceBeUsed() == false) {
                            SwapArrayGameObjectsWithUpdatePosition(array, index, jndex);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void SwapArrayGameObjectsWithUpdatePosition(GameBoardObject[] array,
                                                        int a, int b) {
        GameBoardObject Temp = array[a];
        array[a] = array[b];
        array[b] = Temp;
        array[a].PositionToMove(array[b].GetPosition());
        array[b].SetPosition(array[a].GetPosition());
        array[b].PositionToMove(array[a].GetPosition());
    }

    private int VecToIndex(Vec2 position) {
        return (int)position.y * this.width + (int)position.x;
    }

    private Circle[] GetCirclesAroundIndexPosition(GameBoardObject[] board_array, Vec2 position) {
        int array_size = 4;
        int size = 0;
        int x = (int)position.x;
        int y = (int)position.y;

        if (x < 1) { array_size--; }
        if (y < 1) { array_size--; }
        if (x >= this.width-1)  { array_size--; }
        if (y >= this.height-1) { array_size--; }

        // TODO(Misael): this is not the final array, as there will be
        // other types of objects, and now all of them can be
        // interatable.
        Circle[] array = new Circle[array_size];
        if (x < 1 == false) {
            array[size++] = (Circle)board_array[y * this.width + (x-1)];
        }
        if (x >= this.width-1 == false) {
            array[size++] = (Circle)board_array[y * this.width + (x+1)];
        }
        if (y < 1 == false) {
            array[size++] = (Circle)board_array[(y-1) * this.width + x];
        }
        if (y >= this.height-1 == false) {
            array[size++] = (Circle)board_array[(y+1) * this.width + x];
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
        for (GameBoardObject object: this.objects_array) {
            if (object instanceof Circle) {
                Circle circle = (Circle)object;
                circle.Draw();
            }
        }

        if (this.objects_connected.isEmpty() == false) {
            Vec2 current_position = this.cursor_position;
            for (int index = this.objects_connected.size()-1;
                 index >= 0; index--) {
                Circle c = (Circle)this.objects_connected.get(index);
                Vec2 position = c.GetPosition();
                this.r.DrawLine(current_position, position, 0.02f, c.GetColor());
                current_position = position;
            }
        }
    }

    private boolean DidPlayerMakeMoveOnCircles(GameBoardObject[] array, ArrayList<GameBoardObject> list) {
        for (GameBoardObject object: array) {
            if (object instanceof Circle) {
                Circle circle = (Circle) object;
                if (circle.HasCollide(this.cursor_position) &&
                    (list.contains(circle) == false)) {
                    list.add(circle);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean DidPlayerRewind(GameBoardObject[] array, ArrayList<GameBoardObject> list) {
        int list_size = list.size();
        Circle last_circle_conected = (Circle)list.get(list_size-1);
        int color = ((Circle)list.get(0)).GetColor();
        Vec2 last_index_position = GetIndexPositionFromScreenPosition(last_circle_conected.GetPosition());
        Circle[] circles_around_position = GetCirclesAroundIndexPosition(array, last_index_position);
        for (Circle c: circles_around_position) {
            if(c.HasCollide(this.cursor_position) && color == c.GetColor()) {
                int index = list.indexOf(c);
                if (list_size > 1 && index == list_size-2) {
                    list.remove(list_size-1);
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private boolean ConnectPlayerDots(GameBoardObject[] array, ArrayList<GameBoardObject> list) {
        int list_size = list.size();
        Circle last_circle_conected = (Circle)list.get(list_size-1);
        int color = ((Circle)list.get(0)).GetColor();
        Vec2 last_index_position = GetIndexPositionFromScreenPosition(last_circle_conected.GetPosition());
        Circle[] circles_around_position = GetCirclesAroundIndexPosition(array, last_index_position);
        for (Circle c: circles_around_position) {
            if(c.HasCollide(this.cursor_position) && color == c.GetColor()) {
                int index = list.indexOf(c);
                if (index == -1) {
                    list.add(c);
                }
                else if (index >= 0 && index < list_size-3) {
                    list.add(c);
                    return true;
                }
                break;
            }
        }
        return false;
    }
}
