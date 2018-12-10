package com.urquieta.something.game.board;

import com.urquieta.something.game.GameObject;
import com.urquieta.something.platform.Renderer;

import com.urquieta.something.game.util.Vec2;

public class GameBoardObject extends GameObject {
    protected boolean is_touchable;
    protected boolean can_be_used;
    protected boolean can_fall;
    private   Vec2 position_to_go;

    public GameBoardObject(Renderer r, Vec2 position) {
        this(r, position, false, true, false);
    }

    public GameBoardObject(Renderer r, Vec2 position,
                           boolean touchable, boolean can_be_used, boolean can_fall) {
        super(r, position);
        this.is_touchable = touchable;
        this.can_be_used = can_be_used;
        this.can_fall = can_fall;
        this.position_to_go = position;
    }

    public boolean CanFall() {
        return this.can_fall;
    }

    public void PositionToMove(Vec2 position) {
        this.position_to_go = position;
    }

    public void PositionToMove(float x, float y) {
        PositionToMove(new Vec2(x, y));
    }

    public void Move(Vec2 vector) {
        super.position = super.position.Add(vector);
    }

    public Vec2 GetPositionToGo() {
        return this.position_to_go;
    }

    public boolean CanSpaceBeUsed() {
        return this.can_be_used;
    }

    public boolean IsTouchable() {
        return this.is_touchable;
    }

    public void Draw() {

    }
}
