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

    public Goals(Renderer renderer, char[] objectives_count, char[] objectives_current_score,  String objectives) {
        this(renderer);
        for (int index = 0; index < objectives_count.length; index++) {
            char game_object = objectives.charAt(index*9);
            String color_format = objectives.substring(index*9+1,
                                                       index*9+9);
            int color = (int)Long.parseLong(color_format, 16);
            AddColorGoal(color, objectives_count[index]);
            AddToScore(color, objectives_current_score[index]);
        }
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

        return (color_scores.size() > 0);
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

    public int GetNumObjectives() {
        return color_scores.size();
    }

    public String GetFormat() {
        String format = new String();

        for (ColorScores c : color_scores) {
            format += "C" + Integer.toHexString(c.color);
        }
        return format;
    }

    public char[] GetObjectivesScore() {
        char[] result = new char[color_scores.size()];
        for (int i = 0; i < color_scores.size(); i++) {
            result[i] = (char)color_scores.get(i).current_score;
        }

        return result;
    }

    public char[] GetObjectivesCount() {
        char[] result = new char[color_scores.size()];
        for (int i = 0; i < color_scores.size(); i++) {
            result[i] = (char)color_scores.get(i).goal_score;
        }

        return result;
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
            if (this.current_score >= this.goal_score) {
                this.current_score = goal_score;
            }
        }

        public boolean IsGoalCompleted() {
            return (current_score >= goal_score);
        }
    }
}
