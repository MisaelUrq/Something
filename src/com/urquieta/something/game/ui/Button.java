package com.urquieta.something.game.ui;

import com.urquieta.something.game.GameObject;

import com.urquieta.something.platform.Renderer;
import com.urquieta.something.platform.InputEvent;
import com.urquieta.something.game.util.Vec2;
import com.urquieta.something.platform.Sound;
import com.urquieta.something.platform.Audio;

public class Button extends GameObject {
    private String message;
    private float  width;
    private float  height;
    private int    color_text;
    private Sound  sound;
    private Vec2   text_position;

    // TODO(Misael): Maybe the button needs to have a center point and
    // then change add and substract the width and the height?
    public Button(Renderer renderer, Vec2 position, Vec2 dimensions,
                  String message, int color, int color_text, Sound sound) {
        super(renderer, position);
        this.message = message;
        this.width  = dimensions.x;
        this.height = dimensions.y;
        this.color = color;
        this.color_text = color_text;
        this.sound = sound;
        this.text_position = this.position.Add(.03f, -0.06f);
    }

    // TODO(Misael): Make one to display images.
    public Button(Renderer renderer, Vec2 position, float width, float height,
                  String message, int color, int color_text, Sound sound) {
        super(renderer, position);
        this.message = message;
        this.width  = position.x + width;
        this.height = position.y - height;
        this.color = color;
        this.color_text = color_text;
        this.sound = sound;
        this.text_position = this.position.Add(.03f, -0.06f);
    }

    @Override
    public void Update(double delta) {
        // NOTE(Misael): Why I am doing this?
        this.text_position = this.position.Add(.03f, -0.06f);
    }

    @Override
    public void Draw() {
        super.renderer.DrawRect(position.x, position.y, width, height, color);
        super.renderer.DrawText(this.message, this.text_position, this.color_text);
    }

    private boolean HasCollide(Vec2 input) {
        if ((input.x >= position.x && input.x <= width) &&
            (input.y <= position.y && input.y >= height)) {
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
