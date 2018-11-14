package com.urquieta.something;

import android.app.Activity;
import android.os.Bundle;

import com.urquieta.something.game.*;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Game game = new Game();
        Screen gameScreen = new Screen(game, this);
    }
}
