package com.urquieta.something;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.Window;
import android.graphics.Point;

import com.urquieta.something.game.Game;
import com.urquieta.something.platform.Screen;
import com.urquieta.something.platform.Input;

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
        InitGame();
        setContentView(game.GetScreen());
    }

    private void InitGame() {
        this.game = new Game();
        Screen game_screen = new Screen(game, this);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        game_screen.SetSize(size.x, size.y);
        this.game.setScreen(game_screen);
        Input game_input = new Input();
        game_screen.setOnTouchListener(game_input);
        this.game.setInput(game_input);
    }

    @Override
    public void onPause() {
        super.onPause();
        game.Pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        game.Resume();
    }
}
