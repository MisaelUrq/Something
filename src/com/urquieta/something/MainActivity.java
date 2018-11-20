package com.urquieta.something;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.Window;
import android.graphics.Point;

import com.urquieta.something.game.Game;
import com.urquieta.something.platform.Screen;

// TODO(Misael): Make a script on python where we tell it what changes
// from each build, since the platform clases will need to change the
// pc and the android version constantly.
public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Game game = new Game();
        Screen gameScreen = new Screen(game, this);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        gameScreen.SetSize(size.x, size.y);
        game.setScreen(gameScreen);
        setContentView(gameScreen);
    }
}
