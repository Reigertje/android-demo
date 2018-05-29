package com.demo.game.geom;

public class Mat4x4 {

    private float[] data = new float[16];

    public Mat4x4() {
        setIdentity();
    }

    public void setIdentity() {
        data[0] = 1;
        data[1] = 0; data[2] = 0; data[3] = 0; data[4] = 0;
        data[5] = 1;
        data[6] = 0; data[7] = 0; data[8] = 0; data[9] = 0;
        data[10] = 1;
        data[11] = 0; data[12] = 0; data [13] = 0; data[14] = 0;
        data[15] = 1;
    }

    public void setOrtho(float left, float right, float bottom, float top, float near, float far) {
        data[0] = 2.0f/(right-left);
        data[1] = 0; data[2] = 0; data[3] = 0; data[4] = 0;

        data[5] = 2.0f/(top-bottom);
        data[6] = 0; data[7] = 0; data[8] = 0; data[9] = 0;

        data[10] = (-2.0f)/(far-near);
        data[11] = 0;

        data[12] = -((right + left) / (right-left));
        data[13] = -((top + bottom) / (top - bottom));
        data[14] = -((far + near) / (far - near));
        data[15] = 1;
    }

    public void setMultiply(Mat4x4 lhs, Mat4x4 rhs) {
        data[0] = (lhs.data[0] * rhs.data[0]) + (lhs.data[4] * rhs.data[1]) + (lhs.data[8] * rhs.data[2]) + (lhs.data[12] * rhs.data[3]);
        data[1] = (lhs.data[1] * rhs.data[0]) + (lhs.data[5] * rhs.data[1]) + (lhs.data[9] * rhs.data[2]) + (lhs.data[13] * rhs.data[3]);
        data[2] = (lhs.data[2] * rhs.data[0]) + (lhs.data[6] * rhs.data[1]) + (lhs.data[10] * rhs.data[2]) + (lhs.data[14] * rhs.data[3]);
        data[3] = (lhs.data[3] * rhs.data[0]) + (lhs.data[7] * rhs.data[1]) + (lhs.data[11] * rhs.data[2]) + (lhs.data[15] * rhs.data[3]);
        data[4] = (lhs.data[0] * rhs.data[4]) + (lhs.data[4] * rhs.data[5]) + (lhs.data[8] * rhs.data[6]) + (lhs.data[12] * rhs.data[7]);
        data[5] = (lhs.data[1] * rhs.data[4]) + (lhs.data[5] * rhs.data[5]) + (lhs.data[9] * rhs.data[6]) + (lhs.data[13] * rhs.data[7]);
        data[6] = (lhs.data[2] * rhs.data[4]) + (lhs.data[6] * rhs.data[5]) + (lhs.data[10] * rhs.data[6]) + (lhs.data[14] * rhs.data[7]);
        data[7] = (lhs.data[3] * rhs.data[4]) + (lhs.data[7] * rhs.data[5]) + (lhs.data[11] * rhs.data[6]) + (lhs.data[15] * rhs.data[7]);
        data[8] = (lhs.data[0] * rhs.data[8]) + (lhs.data[4] * rhs.data[9]) + (lhs.data[8] * rhs.data[10]) + (lhs.data[12] * rhs.data[11]);
        data[9] = (lhs.data[1] * rhs.data[8]) + (lhs.data[5] * rhs.data[9]) + (lhs.data[9] * rhs.data[10]) + (lhs.data[13] * rhs.data[11]);
        data[10] = (lhs.data[2] * rhs.data[8]) + (lhs.data[6] * rhs.data[9]) + (lhs.data[10] * rhs.data[10]) + (lhs.data[14] * rhs.data[11]);
        data[11] = (lhs.data[3] * rhs.data[8]) + (lhs.data[7] * rhs.data[9]) + (lhs.data[11] * rhs.data[10]) + (lhs.data[15] * rhs.data[11]);
        data[12] = (lhs.data[0] * rhs.data[12]) + (lhs.data[4] * rhs.data[13]) + (lhs.data[8] * rhs.data[14]) + (lhs.data[12] * rhs.data[15]);
        data[13] = (lhs.data[1] * rhs.data[12]) + (lhs.data[5] * rhs.data[13]) + (lhs.data[9] * rhs.data[14]) + (lhs.data[13] * rhs.data[15]);
        data[14] = (lhs.data[2] * rhs.data[12]) + (lhs.data[6] * rhs.data[13]) + (lhs.data[10] * rhs.data[14]) + (lhs.data[14] * rhs.data[15]);
        data[15] = (lhs.data[3] * rhs.data[12]) + (lhs.data[7] * rhs.data[13]) + (lhs.data[11] * rhs.data[14]) + (lhs.data[15] * rhs.data[15]);
  }

    public void setTranslate(float x, float y, float z) {
        data[12] = x;
        data[13] = y;
        data[14] = z;
    }

    public void set(Mat4x4 other) {
        for (int i = 0; i < 16; ++i) {
            data[i] = other.data[i];
        }
    }

    public float[] data() {
        return data;
    }

}
