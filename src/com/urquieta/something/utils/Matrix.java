package com.urquieta.something.utils;

import com.urquieta.something.output.OutputSystem;


public class Matrix {

    public static void Multiply(float dest[], float a[], float b[]) {
        // y*i+x
        int length = dest.length/4;
        for (int i = 0; i < length; i++) {
            for (int x = 0; x < length; x++) {
                dest[length*i+x] =
                    (b[length*i+0] * a[length*0+x]) +
                    (b[length*i+1] * a[length*1+x]) +
                    (b[length*i+2] * a[length*2+x]) +
                    (b[length*i+3] * a[length*3+x]);
            }
        }
    }

    public static float[] Substract(float a[],
                                    float b[]) {
        return new float[] {
            a[0] - b[0],
            a[1] - b[1],
            a[2] - b[2]
        };
    }

    public static float[] Normal(float vector[]) {
        float length = Length(vector);
        return new float[] { vector[0]/length,
                             vector[1]/length,
                             vector[2]/length,};
    }

    // TODO(Misael): Right now this is made asuming the vectors come from origin.
    public static float[] Cross(float a[], float b[]) {
        return new float[] {
            a[1]*b[2] - a[2]*b[1],
            a[2]*b[0] - a[0]*b[2],
            a[0]*b[1] - a[1]*b[0]
        };
    }

    public static float Length(float vector[]) {
        // TODO(Misael): Handle sqrt to avoid casting...
        return (float)Math.sqrt((double)vector[0] * vector[0] +
                                (double)vector[1] * vector[1] +
                                (double)vector[2] * vector[2]);
    }

    public static void ViewMatrix(float dest[], float eye[], float target[], float up[]) {
        float zaxis[] = Normal(Substract(eye, target));
        float xaxis[] = Normal(Cross(up, zaxis));
        float yaxis[] = Cross(zaxis, xaxis);

        float orientation[] = new float[] {
            xaxis[0], yaxis[0], zaxis[0], 0,
            xaxis[1], yaxis[1], zaxis[1], 0,
            xaxis[2], yaxis[2], zaxis[2], 0,
            0,       0,       0,     1
        };

        float translation[] = new float[] {
            1,      0,      0,   0,
            0,      1,      0,   0,
            0,      0,      1,   0,
            -eye[0], -eye[1], -eye[2], 1
        };

        Multiply(dest, orientation, translation);
    }

    public static float[] ProjectionMatrix(float fov, float aspect, float near, float far) {
        final  double  D2R = Math.PI / 180.0f;
        double  y_scale = 1.0f / Math.tan(D2R * fov / 2);
        double x_scale = y_scale / aspect;
        double nearmfar = near - far;
        float result[] = new float[] {
            -5.57f, 0, 0, 0,
            0, 3.0f, 0, 0,
            0, 0, (float)((far + near) / nearmfar), -1,
            0, 0, (float)(2*far*near / nearmfar), 0
        };
        return result;
    }

    public static void PrintMatrix(String message, float matrix[]) {
        int length = matrix.length/4;
        String matrix_format = message + " => Matrix {\n";
        for (int i = 0; i < length; i++) {
            for (int x = 0; x < length; x++) {
                matrix_format += matrix[length*i+x] + " | ";
            }
            matrix_format += "\n";
        }
        matrix_format += "};";
        OutputSystem.DebugPrint(matrix_format, OutputSystem.Levels.MESSAGES);
    }

}
