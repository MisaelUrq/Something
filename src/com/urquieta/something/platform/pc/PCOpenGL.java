package com.urquieta.something.platform.pc;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.awt.Canvas;

import com.urquieta.something.utils.Buffers;

public class PCOpenGL {
    private static int gl_active_program;

    public final static int GL_SHADER_VERTEX   = GL_VERTEX_SHADER;
    public final static int GL_SHADER_FRAGMENT = GL_FRAGMENT_SHADER;

    public static void GLReportErrors() {
        int error = glGetError();
        while (error != GL_NO_ERROR) {
            System.out.println("OpenGL Error: "+error);
            error = glGetError();
        }
    }


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
        //FloatBuffer buffer = Buffers.ArrayToBuffer(matrix);
        glUniformMatrix4(uniform_handle, false, Buffers.ArrayToBuffer(matrix));
    }

    public static int GLActiveVertexAttrib(String name, FloatBuffer buffer, int coord_per_vertex, int vertex_stride) {
        int handle = glGetAttribLocation(gl_active_program, name);
        glEnableVertexAttribArray(handle);
        glVertexAttribPointer(handle, coord_per_vertex, false, coord_per_vertex*vertex_stride, buffer);
        return handle;
    }

    public static void GLDisableVertexAttrib(int handle) {
        glDisableVertexAttribArray(handle);
    }

    public static void GLDrawElements(ByteBuffer buffer) {
        // glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, buffer);
        glDrawElements(GL_TRIANGLES, buffer);
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
        glUniform4(uniform_handle, Buffers.ArrayToBuffer(array));
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
        glDeleteTextures(texture_id);
    }

    public static int GLGenDefaultTexture() {
        int temp_texture_id[] = new int[1];
        IntBuffer buffer_int = Buffers.ArrayToBuffer(temp_texture_id);
        glGenTextures(buffer_int);
        int texture_id = buffer_int.array()[0];
        glBindTexture(GL_TEXTURE_2D, texture_id);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        return texture_id;
    }
}
