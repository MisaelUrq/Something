package com.urquieta.something.game.board;

import com.urquieta.something.game.GameObject;
import com.urquieta.something.platform.Renderer;
import com.urquieta.something.game.util.Vec2;

import java.util.ArrayList;

public class Goals extends  GameObject {
    private ArrayList<ColorScores> color_scores;

    public Goals(Renderer renderer) {
        super(renderer, new Vec2(0f, .9f));
        color_scores = new ArrayList<ColorScores>();
    }

    public void AddColorGoal(int color, int goal_score) {
        color_scores.add(new ColorScores(color, goal_score));
    }

    public void AddToScore(int color, int score) {
        for (ColorScores color_score : color_scores) {
            if (color_score.color == color) {
                color_score.AddToScore(score);
                break;
            }
        }
    }

    public boolean WereGoalsCompleted() {
        for (ColorScores color_score: color_scores) {
            if (color_score.IsGoalCompleted() == false) {
                return false;
            }
        }

        return true;
    }

    public void Update(double delta) {

    }

    public void Draw() {
        this.renderer.DrawRect(position.x - .35f, position.y + .1f,
                               position.x + .35f, position.y - .05f,
                               0xfafafaff);

        float padding = -((color_scores.size() - 11f) / 30f);
        int index = 1;
        for (ColorScores color_score: color_scores) {
            float position_x = (padding * (float)index++) - padding - .27f;
            this.renderer.DrawCircle(position_x, position.y+0.03f,
                                     0.03f, color_score.color);
            this.renderer.DrawText(color_score.current_score+"/"+color_score.goal_score,
                                   new Vec2(position_x-0.05f, position.y-0.04f), 0xFF000000);
        }
    }

    class ColorScores {
        public int color;
        public int current_score;
        public int goal_score;

        public ColorScores(int color, int goal_score) {
            this.color = color;
            this.current_score = 0;
            this.goal_score    = goal_score;
        }

        public void AddToScore(int score) {
            this.current_score += score;
        }

        public boolean IsGoalCompleted() {
            return (current_score >= goal_score);
        }
    }
}
