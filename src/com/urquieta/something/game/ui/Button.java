package com.urquieta.something.game.ui;

import com.urquieta.something.game.GameObject;

import com.urquieta.something.platform.Renderer;
import com.urquieta.something.platform.InputEvent;
import com.urquieta.something.game.util.Vec2;

public class Button extends GameObject {
    private String message;
    private float width;
    private float height;
    private int color_text;

    // TODO(Misael): Make one to display images.
    public Button(Renderer render, Vec2 position, float width, float height,
                  String message, int color, int color_text) {
        super(render, position);
        this.message = message;
        float message_length = render.GetScreen().PixelsToLength(message.length()*2);
        this.width  = message_length + width;
        this.height = height;
        this.color = color;
        this.color_text = color_text;
    }

    @Override
    public void Update(double delta) {

    }

    @Override
    public void Draw() {
        float x = width/2;
        float y = height/2;
        float text_padd = (float)message.length() / 45f;
        super.renderer.DrawRect(position.x-x, position.y+y,
                                position.x+x, position.y-y,
                                super.color);
        super.renderer.DrawText(this.message, position.Add(-(x-text_padd), 0), color_text);
    }

    private boolean HasCollide(Vec2 input) {
        float x = this.width/2;
        float y = this.height/2;
        if (input.x >= position.x-x && input.x <= position.x+x &&
            input.y >= position.y-y && input.y <= position.y+y) {
            return true;
        } else {
            return false;
        }
    }

    public boolean IsPressed(InputEvent event) {
        if (HasCollide(event.cursor_position) && event.type == InputEvent.TOUCH_CLIC) {
            return true;
        } else {
            return false;
        }
    }

}
