package com.urquieta.something.game.board;

import com.urquieta.something.game.GameObject;
import com.urquieta.something.platform.Renderer;

import com.urquieta.something.game.util.Vec2;

public class GameBoardObject extends GameObject {
    protected boolean is_touchable;
    protected boolean can_be_used;
    protected boolean can_move;
    protected int color;

    public GameBoardObject(Renderer r, Vec2 position) {
        this(r, position, 0, false, true, false);
    }

    public GameBoardObject(Renderer r, Vec2 position, int color,
                           boolean touchable, boolean can_be_used, boolean can_move) {
        super(r, position);
        this.is_touchable = touchable;
        this.can_be_used = can_be_used;
        this.can_move = can_move;
        this.color = color;
    }

    public void SetColor(byte r, byte g, byte b, byte a) {
        this.color = (a << 24) | (r << 16) | (b << 8) | a;
    }

    public void SetColor(int color) {
        this.color = color;
    }

    public int GetColor() {
        return this.color;
    }

    public boolean CanMove() {
        return this.can_move;
    }

    public void Move(Vec2 vector) {
        super.position = super.position.Add(vector);
    }
    
    public boolean CanSpaceBeUsed() {
        return this.can_be_used;
    }

    public boolean IsTouchable() {
        return this.is_touchable;
    }

    public String ToFileFormat() { return null; }
    
    @Override
    public void Update(double delta) {}

    @Override
    public void Draw() {}
}
