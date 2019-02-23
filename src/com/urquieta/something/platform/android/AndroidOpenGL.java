package com.urquieta.something.platform.android;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import android.opengl.GLSurfaceView;
import static android.opengl.GLES20.*;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.urquieta.something.utils.Buffers;

public class AndroidOpenGL {
    private static int gl_active_program;

    public final static int GL_SHADER_VERTEX   = GL_VERTEX_SHADER;
    public final static int GL_SHADER_FRAGMENT = GL_FRAGMENT_SHADER;

    public static void GLInit(int width, int height) {
        glViewport(0, 0, width, height);
        glClearColor(0.9f, 0.9f, 1.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthFunc(GL_LEQUAL);
        glDepthMask(true);
    }

    public static int GLCreateProgramAndLinkShaders(int vertex_id, int fragment_id) {
        int program = glCreateProgram();
        glAttachShader(program, vertex_id);
        glAttachShader(program, fragment_id);
        glLinkProgram(program);
        glDeleteShader(vertex_id);
        glDeleteShader(fragment_id);
        return program;
    }

    public static void GLUseProgram(int program) {
        gl_active_program = program;
        glUseProgram(gl_active_program);
    }

    public static void GLUniformMatrix4fv(String name, float matrix[]) {
        int uniform_handle  = glGetUniformLocation(gl_active_program, name);
        glUniformMatrix4fv(uniform_handle, 1, false, Buffers.ArrayToBuffer(matrix));
    }

    public static int GLActiveVertexAttrib(String name, FloatBuffer buffer, int coord_per_vertex, int vertex_stride) {
        int handle = glGetAttribLocation(gl_active_program, name);
        glEnableVertexAttribArray(handle);
        glVertexAttribPointer(handle, coord_per_vertex, GL_FLOAT, false, coord_per_vertex*vertex_stride, buffer);
        return handle;
    }

    public static void GLDisableVertexAttrib(int handle) {
        glDisableVertexAttribArray(handle);
    }

    public static void GLDrawElements(ByteBuffer buffer) {
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, buffer);
    }

    public static void GLDeleteProgram(int program) {
        glDeleteProgram(program);
    }

    public static void GLUniform1i(String name) {
        int handle = glGetUniformLocation(gl_active_program, name);
        glUniform1i(handle, 0);
    }

    public static void GLUniform4fv(String name, float array[]) {
        int uniform_handle = glGetUniformLocation(gl_active_program, name);
        glUniform4fv(uniform_handle, 1, array, 0);
    }

    public static void GLUseTexture(int texture_id) {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture_id);
    }

    public static void GLInitBlendConfig() {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void GLEndBlendConfig() {
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    public static void GLDeleteTexture(int texture_id) {
        int temp[] = { texture_id};
        glDeleteTextures(1, temp, 0);
    }

    public static int GLGenDefaultTexture() {
        int temp_texture_id[] = new int[1];
        glGenTextures(1, temp_texture_id, 0);
        int texture_id = temp_texture_id[0];
        glBindTexture(GL_TEXTURE_2D, texture_id);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        return texture_id;
    }
}
