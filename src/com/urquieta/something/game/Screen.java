package com.urquieta.something.game;


// This is probably where code begins it's platform dependencies.
import android.content.Context;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class Screen extends SurfaceView implements SurfaceHolder.Callback {

    private Game game;

    public Screen(Game game, Context context) {
        super(context);
        this.game = game;
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        game.startThread();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        game.stopThread();
    }
}
