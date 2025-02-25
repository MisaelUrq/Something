package com.urquieta.something;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.Window;
import android.graphics.Point;

import android.content.SharedPreferences;

import com.urquieta.something.game.Game;
import com.urquieta.something.game.save.SaveState;
import com.urquieta.something.platform.Screen;
import com.urquieta.something.platform.Input;
import com.urquieta.something.platform.Audio;
import com.urquieta.something.platform.ImageLoader;
import com.urquieta.something.output.OutputSystem;

import java.io.File;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    private Game game;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        OutputSystem.SetLevel(OutputSystem.ERRORS);
        InitGame();
        setContentView(game.GetScreen());
    }

    private void InitGame() {
        this.game          = new Game();
        Screen game_screen = new Screen(game, this);
        Input game_input   = new Input();
        Point size         = new Point();
        Audio game_audio   = new Audio();
        ImageLoader image  = new ImageLoader();
        File file_temp = new File(this.getFilesDir()+"/saves");
        file_temp.mkdir();
        SaveState savefile = new SaveState(file_temp);
        game_audio.Init(this);
        image.Init(this);
        getWindowManager().getDefaultDisplay().getSize(size);
        game_screen.SetSize(size.x, size.y);
        game_screen.setOnTouchListener(game_input);
        this.game.Init(game_screen, game_input, game_audio, image, savefile);
    }

    @Override
    public void onPause() {
        super.onPause();
        game.SaveGame();
        game.Pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        game.is_resuming = true;
        game.Resume();
    }
}
