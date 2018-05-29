package com.demo.game.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class BufferUtil {

    public static FloatBuffer floatArrayToBuffer(float [] array) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(array);
        floatBuffer.position(0);
        floatBuffer.rewind();
        return floatBuffer;
    }

    public static FloatBuffer getFloatBuffer(int size) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(size * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        return floatBuffer;
    }

    public static IntBuffer intArrayToBuffer(int [] array) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        IntBuffer floatBuffer = byteBuffer.asIntBuffer();
        floatBuffer.put(array);
        floatBuffer.position(0);
        floatBuffer.rewind();
        return floatBuffer;
    }

    public static IntBuffer getIntBuffer(int size) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(size * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        IntBuffer floatBuffer = byteBuffer.asIntBuffer();
        return floatBuffer;
    }

    public static ShortBuffer shortArrayToBuffer(short [] array) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length * 2);
        byteBuffer.order(ByteOrder.nativeOrder());
        ShortBuffer floatBuffer = byteBuffer.asShortBuffer();
        floatBuffer.put(array);
        floatBuffer.position(0);
        floatBuffer.rewind();
        return floatBuffer;
    }

    public static ShortBuffer getShortBuffer(int size) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(size * 2);
        byteBuffer.order(ByteOrder.nativeOrder());
        ShortBuffer floatBuffer = byteBuffer.asShortBuffer();
        return floatBuffer;
    }

}
