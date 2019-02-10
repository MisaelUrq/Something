package com.urquieta.something.platform.pc;

import java.awt.image.BufferedImage;

public class PCImage  {
    private BufferedImage buffer;
    
    public PCImage() {
        buffer = null;
    }

    public void SetBuffer(BufferedImage buff) {
        this.buffer = buff;
    }

    public BufferedImage GetBuffer() {
        return buffer;
    }
}
