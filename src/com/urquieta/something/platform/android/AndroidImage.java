package com.urquieta.something.platform.android;

import android.graphics.Bitmap;

public class AndroidImage  {
    private Bitmap buffer;

    public AndroidImage() {
        buffer = null;
    }

    public void SetBuffer(Bitmap buff) {
        this.buffer = buff;
    }

    public Bitmap GetBuffer() {
        return buffer;
    }
}
