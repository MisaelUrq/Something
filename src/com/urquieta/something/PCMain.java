package com.urquieta.something;

import com.urquieta.something.game.Game;
import com.urquieta.something.game.save.SaveState;
import com.urquieta.something.platform.Screen;
import com.urquieta.something.platform.Input;
import com.urquieta.something.platform.Audio;
import com.urquieta.something.platform.Renderer;
import com.urquieta.something.platform.ImageLoader;
import static com.urquieta.something.platform.pc.PCOpenGL.*;
import com.urquieta.something.output.OutputSystem;

import java.io.File;

public class PCMain
{
    public static void main(String[] args)
    {
        OutputSystem.WriteErrosOnFile("dump/", "pc_something.output");
        OutputSystem.SetLevel(OutputSystem.ERRORS);
        Game   game        = new Game();
        Screen game_screen = null;
        try {
            game_screen = new Screen(game, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Input  game_input  = new Input();
        Audio  game_audio  = new Audio();
        ImageLoader image  = new ImageLoader();
        File file_temp = new File("saves");
        file_temp.mkdir();
        SaveState savefile = new SaveState(file_temp);
        game.Init(game_screen, game_input, game_audio, image, savefile);
        game_screen.addKeyListener(game_input);
        game_screen.addMouseMotionListener(game_input);
        game_screen.addMouseListener(game_input);
        game.startThread();
    }
}
