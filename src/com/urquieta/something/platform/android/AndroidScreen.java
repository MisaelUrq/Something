package com.urquieta.something.platform.android;

import android.content.Context;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.graphics.Canvas;

import com.urquieta.something.game.Game;

public class AndroidScreen extends SurfaceView implements SurfaceHolder.Callback {
    protected static int width;
    protected static int height;
    private Game game;
    private Canvas game_canvas;
    private SurfaceHolder holder;

    public AndroidScreen(Game game, Context context) {
        super(context);
        this.game = game;
        this.holder = getHolder();
        this.holder.addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO(Misael): Find out how to preserve the state of the
        // game after the app is put to rest.
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.holder.setFixedSize(this.width, this.height);
        game.startThread();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        game.stopThread();
    }

    public void beginDraw() {
        this.game_canvas = this.holder.lockCanvas();;
    }

    public void endDraw() {
        if (this.game_canvas != null)
        {
            this.holder.unlockCanvasAndPost(this.game_canvas);
        }
    }

    public Canvas getCanvas() {
        return this.game_canvas;
    }
}
