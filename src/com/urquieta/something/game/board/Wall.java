package com.urquieta.something.game.board;

import com.urquieta.something.game.board.GameBoardObject;
import com.urquieta.something.game.util.Vec2;
import com.urquieta.something.platform.Renderer;

public class Wall extends GameBoardObject {
    public Vec2 dimensions;
    
    public Wall(Renderer renderer, float x, float y, float width) {
        this(renderer, new Vec2(x, y), 0xff131313);
        
    }


    // TODO(Misael): Fix the way the dimensions are gone be set, since
    // right now I'm not sure how I want them to be.
    public Wall(Renderer renderer, Vec2 position, float width) {
        super(renderer, position, 0xff131313, false, false, false);

        this.DEBUG_area_position_1 = this.position.Add(-.05f, .04f);
        this.DEBUG_area_position_2 = this.position.Add(.05f, -.04f);
    }

    @Override
    public String ToFileFormat() {
        String Result = "W"+Integer.toHexString(color);
        return Result;
    }
    
    @Override
    public void Draw() {
        super.renderer.DrawRect(DEBUG_area_position_1.x, DEBUG_area_position_1.y,
                                DEBUG_area_position_2.x, DEBUG_area_position_2.y,
                                color);
    }

    @Override
    public String toString() {
        return "Rect: "+super.GetPosition()+" - Dim("+dimensions+")";
    }
}
