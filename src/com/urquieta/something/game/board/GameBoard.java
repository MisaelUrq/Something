package com.urquieta.something.game.board;

import com.urquieta.something.game.GameObject;
import com.urquieta.something.game.board.Circle;
import com.urquieta.something.game.board.Wall;
import com.urquieta.something.game.board.GameBoardObject;
import com.urquieta.something.game.util.Vec2;
import com.urquieta.something.game.ui.Button;
import com.urquieta.something.game.GameState;
import com.urquieta.something.platform.Sound;
import com.urquieta.something.output.OutputSystem;
import com.urquieta.something.platform.InputEvent;
import com.urquieta.something.game.save.SaveState;

// TODO(Misael): Make the renderer static, so we can envoke it from
// everywere
import com.urquieta.something.platform.Renderer;

import java.util.Random;
import java.util.ArrayList;


public class GameBoard extends GameObject {
    private Goals level_goals;
    private GameBoardObject[] objects_array;
    private int width;
    private int height;
    private float  objects_proportion;
    private Random random;
    // TODO(Misael): Make a color class and maybe a color palette class?
    private int[]   color_palette;
    private InputEvent event;
    private boolean player_move_init;
    private ArrayList<GameBoardObject> objects_connected;
    private Vec2  object_padding;
    private float start_x_position;
    private float start_y_position;
    private boolean is_update_done;
    private int     last_color_clear;
    private boolean player_connected_color;
    private Sound  collect_sound;
    private Sound  drop_sound;
    private Sound  clear_color_sound;
    private int    score;
    private boolean exit_requested;

    // NOTE(Misael): We could make this button in the main game, that
    // way we just renderer on top of every other mode except the
    // menus. But I'm not sure if that's a great idea.
    private Button return_to_menu;

    public GameBoard(Renderer renderer, int width, int height, int score, String format,
                     Sound collect_sound, Sound drop_sound, Sound clear_color_sound) {
        super(renderer, new Vec2(0, 0));
        this.width  = width;
        this.height = height;
        CommonInit(collect_sound, drop_sound, clear_color_sound);
        this.score = score;
        this.objects_array = InitGameObjectArray(format);
    }

    public GameBoard(Renderer renderer, int width, int height,
                     Sound collect_sound, Sound drop_sound, Sound clear_color_sound) {
        super(renderer, new Vec2(0, 0));
        this.width = width;
        this.height = height;
        CommonInit(collect_sound, drop_sound, clear_color_sound);
        this.objects_array = InitGameObjectArray();
    }

    private void CommonInit(Sound collect_sound, Sound drop_sound, Sound clear_color_sound) {
        this.collect_sound = collect_sound;
        this.drop_sound    = drop_sound;
        this.clear_color_sound = clear_color_sound;
        this.random = new Random();
        this.color_palette    = new int[5];
        this.level_goals = new Goals(renderer);
        this.player_move_init = false;
        Vec2 screen_size =  super.renderer.GetScreen().GetSize();
        this.score       = 0;
        // TODO(Misael): Find a way to make the padding more dependent
        // on the width and height of the board itself.

        float padding = 45;
        this.objects_proportion = padding/3.3f;
        this.object_padding     = new Vec2(padding/screen_size.x, padding/screen_size.y);
        this.objects_connected  = new ArrayList<GameBoardObject>();

        this.start_x_position = -((this.object_padding.x * (float)(this.width+1)  / 2.0f));
        this.start_y_position =  ((this.object_padding.y * (float)(this.height+1) / 2.0f));
        this.color_palette[0] =  0xFFFF0000;
        this.color_palette[1] =  0xFF00FF00;
        this.color_palette[2] =  0xFF0000FF;
        this.color_palette[3] =  0xFFFF00FF;
        this.color_palette[4] =  0xFF00FFFF;
        this.is_update_done = true;
        this.exit_requested = false;
        this.last_color_clear = 0;
        this.player_connected_color = false;
        this.return_to_menu = new Button(this.renderer, new Vec2(.80f, -.90f), .2f, .1f, "Exit",
                                         0xffafafaf, 0xff2d2d2d, drop_sound);
    }

    public void InitLevel(int width, int height, String format) {
        InitNewBoard(width, height);
        this.objects_array = InitGameObjectArray(format);
    }

    public void InitNewBoard(int width, int height) {
        this.width = width;
        this.height = height;
        CommonInit(collect_sound, drop_sound, clear_color_sound);
        this.objects_array = InitGameObjectArray();
    }

    private GameBoardObject[] InitGameObjectArray() {
        GameBoardObject[] array = new GameBoardObject[width * height];
        float y_position = start_y_position;
        float radius = ((objects_proportion / super.renderer.GetScreen().GetWidth()) +
                        (objects_proportion / super.renderer.GetScreen().GetHeight())) / 2;
        int wall_random  = (Math.abs(random.nextInt()) % width*height)+13;

        for (int y = 0; y < height; y++) {
            float x_position = start_x_position;
            y_position -= this.object_padding.y;
            int magic_number = (Math.abs(random.nextInt()) % (width*height)) / 2;
            for (int x = 0; x < width; x++) {
                int index = width * y + x;
                x_position += this.object_padding.x;
                int color = this.color_palette[Math.abs(random.nextInt() % 5)];
                if ((index+1) % wall_random == magic_number) {
                    array[index] = new Wall(this.renderer, x_position, y_position, radius*10);
                } else {
                    array[index] = new Circle(this.renderer, x_position, y_position, radius,
                                              color);
                }
            }
        }
        return array;
    }

    public void UpdateEvent(InputEvent new_event) {
        this.event = new_event;
    }

    @Override
    public void Update(double delta) {
        if ((this.event.type == InputEvent.TOUCH_CLIC || this.event.type == InputEvent.TOUCH_DRAGGED)
            && this.is_update_done) {
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
                this.score += objects_connected.size();
                if (player_connected_color) {
                    this.clear_color_sound.Play(1);
                    int color = this.objects_connected.get(0).GetColor();
                    int score_from_clear_color = ClearObjectsOfColor(this.objects_array, color);
                    last_color_clear = color;
                    this.level_goals.AddToScore(color, score_from_clear_color);
                    this.objects_connected.clear();
                    this.score += score_from_clear_color;
                    this.player_connected_color = false;
                }
                else {
                    this.level_goals.AddToScore(this.objects_connected.get(0).GetColor(),
                                                this.objects_connected.size());
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
            this.level_goals.Update(delta);
            if (time_pass > 5) {
                CreateNewObjects(this.objects_array);
                this.time_pass = 0;
            }
            UpdateObjectPositions((float)delta, this.objects_array);
        } else {
            last_color_clear = 0;
        }

        if (this.return_to_menu.IsPressed(event)) {
            this.exit_requested = true;
        }
    }

    public boolean DidPlayerWinLevel() {
        return this.level_goals.WereGoalsCompleted();
    }

    // TODO(Misael): Move this from here!! And find a better name
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
                float radius = ((objects_proportion / this.renderer.GetScreen().GetWidth()) +
                                (objects_proportion / this.renderer.GetScreen().GetHeight())) / 2;
                Circle object = new Circle(this.renderer, position, radius,
                                           color);
                object.PositionToMove(object.GetPosition().Sub(0, (row+1)*this.object_padding.y));
                array[index] = object;
            }
        }
    }

    private int ClearObjectsOfColor(GameBoardObject[] array, int color) {
        int objects_remove = 0;
        for (GameBoardObject object: array) {
            if (object.GetColor() == color) {
                ++objects_remove;
                DeleteObjectFromArray(array, object);
            }
        }
        return objects_remove;
    }

    private void DeleteObjectFromArray(GameBoardObject[] array, GameBoardObject object) {
        Vec2 position = GetIndexPositionFromScreenPosition(object.GetPosition());
        int index = VecToIndex(position);
        array[index] = new GameBoardObject(this.renderer, object.GetPosition());
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

    // NOTE(Misael): Maybe this need to be ajust depending on the score?
    private Vec2 score_position = new Vec2(.5f, .9f);
    public void Draw() {
        for (GameBoardObject object: this.objects_array) {
            object.Draw();
        }

        if (this.objects_connected.isEmpty() == false) {
            Vec2 current_position = this.event.cursor_position;
            for (int index = this.objects_connected.size()-1;
                 index >= 0; index--) {
                Circle c = (Circle)this.objects_connected.get(index);
                Vec2 position = c.GetPosition();
                this.renderer.DrawLine(current_position, position, 0.02f, c.GetColor());
                current_position = position;
            }
        }

        this.level_goals.Draw();
        this.renderer.DrawText("Score: "+this.score, this.score_position, 0xFF131313);
        this.return_to_menu.Draw();
    }

    private boolean DidPlayerMakeMoveOnCircles(GameBoardObject[] array, ArrayList<GameBoardObject> list) {
        for (GameBoardObject object: array) {
            if (object instanceof Circle) {
                Circle circle = (Circle) object;
                if (circle.HasCollide(this.event.cursor_position) &&
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
            if(c != null && c.HasCollide(this.event.cursor_position) && color == c.GetColor()) {
                int index = list.indexOf(c);
                if (list_size > 1 && index == list_size-2) {
                    list.remove(list_size-1);
                    this.drop_sound.Play(1);
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
            if(c != null && c.HasCollide(this.event.cursor_position) && color == c.GetColor()) {
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
        float radius = ((objects_proportion / this.renderer.GetScreen().GetWidth()) +
                        (objects_proportion / this.renderer.GetScreen().GetHeight())) / 2;
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
                    array[index] = new Circle(this.renderer, x_position, y_position, radius,
                                              color);
                } break;
                case 'W': {
                    array[index] = new Wall(this.renderer, x_position, y_position, radius);
                } break;
                default: {
                    OutputSystem.DebugPrint("Try to create an unknow type of GameObject! Wrong Format String!", 2);
                }
                }
            }
        }
        return array;
    }

    public boolean WasExitRequested() {
        return this.exit_requested;
    }

    public int GetWidth() {
        return this.width;
    }

    public int GetHeight() {
        return this.height;
    }

    public int GetScore() {
        return this.score;
    }

    public String ToFileFormat() {
        String level_format = "";
        for (GameBoardObject o: objects_array) {
            level_format += o.ToFileFormat();
        }
        return level_format;
    }

    public void DEBUG_InitDummyGoals() {
        this.level_goals.AddColorGoal(color_palette[1], 10);
        this.level_goals.AddColorGoal(color_palette[3], 10);
        this.level_goals.AddColorGoal(color_palette[0], 10);
    }
}
