package com.urquieta.something;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.Window;
import android.graphics.Point;

import android.content.SharedPreferences;

import com.urquieta.something.game.Game;
import com.urquieta.something.platform.Screen;
import com.urquieta.something.platform.Input;
import com.urquieta.something.platform.Audio;
import com.urquieta.something.output.OutputSystem;

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
        game_audio.Init(this);
        getWindowManager().getDefaultDisplay().getSize(size);
        game_screen.SetSize(size.x, size.y);
        game_screen.setOnTouchListener(game_input);
        this.game.Init(game_screen, game_input, game_audio);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("game", game.ToFileFormat());
        editor.commit();
        game.Pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        game.is_resuming = true;
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String format = preferences.getString("game", "DEFAULT");
        game.Resume(format);
    }
}
