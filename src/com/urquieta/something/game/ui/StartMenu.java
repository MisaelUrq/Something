package com.urquieta.something.game.ui;

import com.urquieta.something.game.GameObject;
import com.urquieta.something.game.util.Vec2;
import com.urquieta.something.platform.Renderer;
import com.urquieta.something.platform.Sound;
import com.urquieta.something.game.GameState;
import com.urquieta.something.platform.InputEvent;

public class StartMenu extends GameObject {
    private Button infinite_mode;
    private Button normal_mode;
    private Button exit;
    private InputEvent event;
    private Vec2 label_position = new Vec2(-.6f, .7f);

    public StartMenu(Renderer renderer, Sound sound) {
        super(renderer, new Vec2(0, 0));
        this.infinite_mode = new Button(renderer, new Vec2(-.25f, .25f), .2f, .1f,
                                        "Modo Infinito", 0xff7b68ee, 0xfff0ffff, sound);
        this.normal_mode = new Button(renderer, new Vec2(.25f, .25f), .2f, .1f,
                                      "Levels", 0xff7b68ee, 0xfff0ffff, sound);
        this.exit = new Button(renderer, new Vec2(-.25f, -.25f), .2f, .1f,
                               "Salir", 0xff7b68ee, 0xfff0ffff, sound);
        event = new InputEvent();
    }

    public void UpdateEvent(InputEvent event) {
        this.event = event;
    }

    @Override
    public void Draw() {
        super.renderer.DrawText("Something - Super BETA - v 0.0.1", label_position, 0xffcd1076);
        this.infinite_mode.Draw();
        this.normal_mode.Draw();
        this.exit.Draw();
    }

    @Override
    public void Update(double delta) {
        if (this.infinite_mode.IsPressed(event)) {
            GameState.current_mode = GameState.INFINITE_MODE;
        } else if (this.normal_mode.IsPressed(event)) {
            GameState.current_mode = GameState.NORMAL_MODE;
        } else if (this.exit.IsPressed(event)) {
            GameState.current_mode = GameState.EXIT;
        }
    }
}
