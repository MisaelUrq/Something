package com.urquieta.something.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

public class Buffers {
    public static FloatBuffer ArrayToBuffer(float array[]) {
        FloatBuffer result;
        ByteBuffer temp_buffer = ByteBuffer.allocateDirect(array.length * 4);
        temp_buffer.order(ByteOrder.nativeOrder());
        result = temp_buffer.asFloatBuffer();
        result.put(array);
        result.position(0);
        return result;
    }

    public static ShortBuffer ArrayToBuffer(short array[]) {
        ShortBuffer result;
        ByteBuffer temp_buffer = ByteBuffer.allocateDirect(array.length * 2);
        temp_buffer.order(ByteOrder.nativeOrder());
        result = temp_buffer.asShortBuffer();
        result.put(array);
        result.position(0);
        return result;
    }

    public static IntBuffer ArrayToBuffer(int array[]) {
        IntBuffer result;
        ByteBuffer temp_buffer = ByteBuffer.allocateDirect(array.length*4);
        temp_buffer.order(ByteOrder.nativeOrder());
        result = temp_buffer.asIntBuffer();
        result.put(array);
        result.position(0);
        return result;
    }

    // NOTE(Misael): SO i can't create a normal byteBuffer from a byte
    // array, I have to pass a short wich is 2 bytes. Create a
    // bytebuffer and then overwrite its contents creating a dummy
    // Shortbuffer and setting the short array there. I don't know why
    // this works, or how this works for the matter, but it almost 1
    // a.m. I'm tired.
    public static ByteBuffer GenByteBuffer(short array[]) {
        ByteBuffer result = ByteBuffer.allocateDirect(array.length * 2);
        result.order(ByteOrder.nativeOrder());
        ShortBuffer temp = result.asShortBuffer();
        temp.put(array);
        temp.position(0);
        return result;
    }

    public static ByteBuffer ArrayToBuffer(byte array[]) {
        ByteBuffer result = ByteBuffer.allocateDirect(array.length);
        result.order(ByteOrder.nativeOrder());
        result.put(array);
        result.position(0);
        return result;
    }
}
