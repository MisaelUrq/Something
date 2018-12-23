package com.urquieta.something.game.board;

import com.urquieta.something.game.GameObject;
import com.urquieta.something.game.board.Circle;
import com.urquieta.something.game.board.Wall;
import com.urquieta.something.game.board.GameBoardObject;
import com.urquieta.something.game.util.Vec2;
import com.urquieta.something.platform.Sound;
import com.urquieta.something.output.OutputSystem;

// TODO(Misael): Make the renderer static, so we can envoke it from
// everywere
import com.urquieta.something.platform.Renderer;
import java.util.Random;
import java.util.ArrayList;

public class GameBoard extends GameObject {
    private GameBoardObject[] objects_array;
    private int width;
    private int height;
    private float objects_proportion;
    private Renderer r;
    private Vec2 cursor_position;
    private Random random;
    // TODO(Misael): Make a color class and maybe a color palette class?
    private int[] color_palette;
    private boolean player_dragged;
    private boolean player_move_init;
    private ArrayList<GameBoardObject> objects_connected;
    private Vec2    object_padding;
    private float start_x_position;
    private float start_y_position;
    private boolean is_update_done;
    private int last_color_clear;
    private boolean player_connected_color;
    private Sound collect_sound;
    
    public GameBoard(Renderer renderer, String format, Sound collect_sound) {
        super(renderer, new Vec2(0, 0));
        this.width  = (int)format.charAt(0);
        this.height = (int)format.charAt(1);
        this.r = renderer;
        CommonInit(collect_sound);
        this.objects_array = InitGameObjectArray(format.substring(2, format.length()));
    }
    
    public GameBoard(Renderer renderer, int width, int height, Sound collect_sound) {
        super(renderer, new Vec2(0, 0));
        this.width = width;
        this.height = height;
        this.r = renderer;
        CommonInit(collect_sound);
        this.objects_array = InitGameObjectArray();
        
    }

    private void CommonInit(Sound collect_sound) {
        this.collect_sound = collect_sound;
        this.random = new Random();
        this.cursor_position = new Vec2(0, 0);
        this.color_palette = new int[5];
        this.player_dragged = false;
        this.player_move_init = false;
        Vec2 screen_size=  r.GetScreen().GetSize();
        // TODO(Misael): Find a way to make the padding more dependent
        // on the width and height of the board itself.
        
        float padding = 45;
        this.objects_proportion = padding/3.3f;
        this.object_padding = new Vec2(padding/screen_size.x, padding/screen_size.y);
        this.objects_connected = new ArrayList<GameBoardObject>();
        
        this.start_x_position = -((this.object_padding.x * (float)(this.width+1)  / 2.0f));
        this.start_y_position =  ((this.object_padding.y * (float)(this.height+1) / 2.0f));
        this.color_palette[0] =  0xFFFF0000;
        this.color_palette[1] =  0xFF00FF00;
        this.color_palette[2] =  0xFF0000FF;
        this.color_palette[3] =  0xFFFF00FF;
        this.color_palette[4] =  0xFF00FFFF;
        this.is_update_done = true;
        this.last_color_clear = 0;
        this.player_connected_color = false;
    }
    
    private GameBoardObject[] InitGameObjectArray() {
        GameBoardObject[] array = new GameBoardObject[width * height];
        float y_position = start_y_position;
        float radius = ((objects_proportion / this.r.GetScreen().GetWidth()) +
                        (objects_proportion / this.r.GetScreen().GetHeight())) / 2;
        int wall_random = (Math.abs(random.nextInt()) % 79)+1;
        for (int y = 0; y < height; y++) {
            float x_position = start_x_position;
            y_position -= this.object_padding.y;
            for (int x = 0; x < width; x++) {
                int index = width * y + x;
                x_position += this.object_padding.x;
                int color = this.color_palette[Math.abs(random.nextInt() % 5)];
                if (index % wall_random == 0) {
                    array[index] = new Wall(this.r, x_position, y_position, radius*10);
                } else {
                    array[index] = new Circle(this.r, x_position, y_position, radius,
                                              color);
                }
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
                    last_color_clear = color;
                    this.objects_connected.clear();
                    ClearObjectsOfColor(this.objects_array, color);
                    this.player_connected_color = false;
                }
                else {
                    for (GameBoardObject object: this.objects_connected) {
                        DeleteObjectFromArray(this.objects_array, object);
                    }
                }
                this.is_update_done = false;
            }
            this.objects_connected.clear();
            this.player_move_init = false;

        }
        
        this.time_pass += delta;
        if (this.is_update_done == false) {
            UpdateObjectsArray(this.objects_array);
            if (time_pass > 5) {
                CreateNewObjects(this.objects_array);
                this.time_pass = 0;
            }
            UpdateObjectPositions((float)delta, this.objects_array);
        } else {
            last_color_clear = 0;
        }
    }

    private double time_pass = 0;

    private void UpdateObjectPositions(double delta, GameBoardObject[] array) {
        float speed = 0.003f;
        this.is_update_done = true;
        for (GameBoardObject object: array) {
            if (object.GetPosition().Equals(object.GetPositionToGo()) == false) {
                this.is_update_done = false;
                Vec2 a = new Vec2(0, -speed); // Acceleration
                object.ComputeMove((float)delta, a);

                if (object.GetPosition().y < object.GetPositionToGo().y) {
                    object.EndMove();
                }
            }
        }
    }

    private void CreateNewObjects(GameBoardObject[] array) {
        for (int x = 0; x < this.width; x++) {
            int index = 0;
            int row = 0;
            for (row = 0; row < this.height; row++) {
                if (array[row*width+x].CanSpaceBeUsed()) {
                    index = row*width+x;
                    break;
                }
            }

            int color = 0;
            for(;;) {
                color = this.color_palette[Math.abs(random.nextInt() % 5)];
                if (color == last_color_clear) {
                    continue;
                } else {
                    break;
                }
            }
                        
            if (array[index].CanSpaceBeUsed()) {
                Vec2 position = new Vec2(this.start_x_position + ((x+1)*this.object_padding.x),
                                         this.start_y_position);
                float radius = ((objects_proportion / this.r.GetScreen().GetWidth()) +
                                (objects_proportion / this.r.GetScreen().GetHeight())) / 2;
                Circle object = new Circle(this.r, position, radius,
                                           color);
                object.PositionToMove(object.GetPosition().Sub(0, (row+1)*this.object_padding.y));
                array[index] = object;
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
                        if (array[jndex].CanSpaceBeUsed() == false && array[jndex].CanMove()) {
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

    // NOTE(Misael): Asumes that the input position is of another board object.
    private int VecToIndex(Vec2 position) {
        return (int)position.y * this.width + (int)position.x;
    }

    private Circle[] GetCirclesAroundIndexPosition(GameBoardObject[] board_array, Vec2 position) {
        int array_size = 4;
        int size = 0;
        int x = (int)position.x;
        int y = (int)position.y;
        int left = y * this.width + (x-1);
        int right = y * this.width + (x+1);
        int up = (y-1) * this.width + x;
        int down = (y+1) * this.width + x;
        
        if (x < 1) { array_size--; }
        if (y < 1) { array_size--; }
        if (x >= this.width-1)  { array_size--; }
        if (y >= this.height-1) { array_size--; }

        // TODO(Misael): this is not the final array, as there will be
        // other types of objects, and now all of them can be
        // interatable.
        Circle[] array = new Circle[array_size];
        if (x < 1 == false && board_array[left] instanceof Circle) {
            array[size++] = (Circle)board_array[left];
        }
        if (x >= this.width-1 == false && board_array[right] instanceof Circle) {
            array[size++] = (Circle)board_array[right];
        }
        if (y < 1 == false && board_array[up] instanceof Circle) {
            array[size++] = (Circle)board_array[up];
        }
        if (y >= this.height-1 == false && board_array[down] instanceof Circle) {
            array[size++] = (Circle)board_array[down];
        }
        return array;
    }

    private Vec2 GetIndexPositionFromScreenPosition(Vec2 entity_position) {
        float start_x_position = -((this.object_padding.x * (float)(this.width+1)  / 2.0f));
        float start_y_position =  ((this.object_padding.y * (float)(this.height+1) / 2.0f));

        float x =  (entity_position.x - start_x_position) / this.object_padding.x;
        float y = -(entity_position.y - start_y_position) / this.object_padding.y;
        int  x_index = Math.round(x)-1;
        int  y_index = Math.round(y)-1;
        Vec2 index_position = new Vec2(x_index, y_index);
        return index_position;
    }

    public void Draw() {
        for (GameBoardObject object: this.objects_array) {
            object.Draw();
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
                    collect_sound.Play(1);
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
            if(c != null && c.HasCollide(this.cursor_position) && color == c.GetColor()) {
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
            if(c != null && c.HasCollide(this.cursor_position) && color == c.GetColor()) {
                int index = list.indexOf(c);
                if (index == -1) {
                    list.add(c);
                    collect_sound.Play(1);
                }
                else if (index >= 0 && index < list_size-3) {
                    list.add(c);
                    collect_sound.Play(1);
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private GameBoardObject[] InitGameObjectArray(String format) {
        GameBoardObject[] array = new GameBoardObject[width * height];
        float y_position = start_y_position;
        float radius = ((objects_proportion / this.r.GetScreen().GetWidth()) +
                        (objects_proportion / this.r.GetScreen().GetHeight())) / 2;
        int object_format_offset = 9;
        for (int y = 0; y < height; y++) {
            float x_position = start_x_position;
            y_position -= this.object_padding.y;
            for (int x = 0; x < width; x++) {
                int index = width * y + x;
                char type = format.charAt(index*object_format_offset);
                String color_format = format.substring(index*object_format_offset+1,
                                                       index*object_format_offset+9);
                x_position += this.object_padding.x;
                int color = (int)Long.parseLong(color_format, 16);
                switch (type) {
                case 'C': {
                    array[index] = new Circle(this.r, x_position, y_position, radius,
                                              color);
                } break;
                case 'W': {
                    array[index] = new Wall(this.r, x_position, y_position, radius);
                } break;
                default: {
                    OutputSystem.DebugPrint("Try to create an unknow type of GameObject! Wrong Format String!", 2);
                }
                }
            }
        }
        return array;
    }
    
    public String ToFileFormat() {
        String Result = ((char)width)+""+((char)height);
        int index = 1;
        for (GameBoardObject o: objects_array) {
            index++;
            Result += o.ToFileFormat();
        }
        return Result;
    }
}
