package com.urquieta.something.output;


public class OutputSystem {
    private static int level    = 0;

    public  static final int WARNINGS = 1;
    public  static final int ERRORS   = 2;

    public void SetLevel(int level) {
        this.level = level;
    }

    public void DebugPrint(String message, int level) {
        if (level <= this.level) {
            System.out.printf(message);
        }
    }
}
