package com.urquieta.something.platform.android;

import android.content.Context;
import android.graphics.Canvas;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;

import com.urquieta.something.game.Game;
import com.urquieta.something.platform.android.AndroidRenderer;

public class AndroidScreen extends GLSurfaceView  {
    protected static int width;
    protected static int height;
    private Game game;
    private Canvas game_canvas;

    public AndroidScreen(Game game, Context context) {
        super(context);
        this.game = game;

        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 8, 0);
        setRenderer(new AndroidRenderer());
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setFocusable(true);
    }

    public void beginDraw() {

    }

    public void endDraw() {
        requestRender();
    }
}
