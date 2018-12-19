package com.urquieta.something.game.ui;

import com.urquieta.something.game.GameObject;

import com.urquieta.something.platform.Renderer;
import com.urquieta.something.platform.InputEvent;
import com.urquieta.something.game.util.Vec2;
import com.urquieta.something.platform.Sound;
import com.urquieta.something.platform.Audio;

public class Button extends GameObject {
    private String message;
    private float width;
    private float height;
    private int color_text;
    private Sound sound;

    // TODO(Misael): Make one to display images.
    public Button(Renderer render, Vec2 position, float width, float height,
                  String message, int color, int color_text, Sound sound) {
        super(render, position);
        this.message = message;
        // float message_length = render.GetScreen().PixelsToLength(message.length());
        this.width  = width;
        this.height = height;
        this.color = color;
        this.color_text = color_text;
        this.sound = sound;
    }

    @Override
    public void Update(double delta) {

    }

    @Override
    public void Draw() {
        float x = width/2;
        float y = height/2;
        super.renderer.DrawRect(position.x-x, position.y+y,
                                position.x+x, position.y-y,
                                super.color);
        super.renderer.DrawText(this.message, position.Add(-(x-.03f), -.02f), color_text);
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
            if (this.sound != null) {
                this.sound.Play(1);
            }
            return true;
        } else {
            return false;
        }
    }

}
