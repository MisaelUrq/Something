package com.urquieta.something.platform.renderer.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

// TODO(Misael): Make sure all of this dependencies are platform free.
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.graphics.Bitmap;
import android.util.Log;

import com.urquieta.something.platform.android.AndroidRenderer;
import com.urquieta.something.platform.Image;
import com.urquieta.something.platform.ImageLoader;
import com.urquieta.something.game.util.Color;

public class TextureModel {
    // TODO(Misael): Do we need more shaders right now?
    private final String vertex_shader =
        "attribute vec2 texture_coords_in; "+
        "varying vec2 texture_coords;" +
        "uniform mat4 mvp_matrix; attribute vec4 vertex_position;" +
        "void main() { gl_Position  = mvp_matrix * vertex_position; "+
        "texture_coords = texture_coords_in; }";

    private final String fragment_shader =
        "varying vec2 texture_coords;" +
        "uniform vec4 fragment_color; "+
        "uniform sampler2D texture_sampler;"+
        " void main() { gl_FragColor = texture2D(texture_sampler, texture_coords) * fragment_color;  }";

    private int program;
    private int texture_id;
    ByteBuffer  byte_buffer_positions;
    ByteBuffer  byte_buffer_order;
    ByteBuffer  byte_buffer_texture_coords;
    ShortBuffer draw_buffer;
    FloatBuffer vertex_buffer;
    FloatBuffer texture_coords_buffer;

    Color color;
    float position[];
    short draw_order[];
    float texture_coords[];
    int   textures[] = new int[1];
    Image image;

    public TextureModel(float positions[], short draw_order[], float texture_coords[], String filename, Color color) {
        this.draw_order = draw_order;
        this.position = positions;
        this.color = color;
        this.texture_coords = texture_coords;
        byte_buffer_positions = ByteBuffer.allocateDirect(position.length * 4);
        byte_buffer_positions.order(ByteOrder.nativeOrder());
        vertex_buffer = byte_buffer_positions.asFloatBuffer();
        vertex_buffer.put(position);
        vertex_buffer.position(0);


        byte_buffer_order = ByteBuffer.allocateDirect(draw_order.length * 2);
        byte_buffer_order.order(ByteOrder.nativeOrder());
        draw_buffer = byte_buffer_order.asShortBuffer();
        draw_buffer.put(draw_order);
        draw_buffer.position(0);

        byte_buffer_texture_coords = ByteBuffer.allocateDirect(texture_coords.length * (texture_coords.length/2));
        byte_buffer_texture_coords.order(ByteOrder.nativeOrder());
        texture_coords_buffer = byte_buffer_texture_coords.asFloatBuffer();
        texture_coords_buffer.put(texture_coords);
        texture_coords_buffer.position(0);

        image = ImageLoader.CreateImage(filename);
    }

    public boolean HasBeenCreated() {
        return (program != 0);
    }



    public void CreateProgram() {
        int vertex_shader_id   = AndroidRenderer.LoadShader(GLES20.GL_VERTEX_SHADER, vertex_shader);
        int fragment_shader_id = AndroidRenderer.LoadShader(GLES20.GL_FRAGMENT_SHADER, fragment_shader);

        int temp[] = new int[1];
        GLES20.glGetShaderiv(vertex_shader_id, GLES20.GL_COMPILE_STATUS, temp, 0);
        if (temp[0] != GLES20.GL_TRUE) {
            Log.e("SOMETHING VERTEX", "ERROR: "+temp[0]);
        }
        GLES20.glGetShaderiv(fragment_shader_id, GLES20.GL_COMPILE_STATUS, temp, 0);
        if (temp[0] != GLES20.GL_TRUE) {
            Log.e("SOMETHING FRAGMENT", "ERROR: "+temp[0]);
        }

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertex_shader_id);
        GLES20.glAttachShader(program, fragment_shader_id);
        GLES20.glLinkProgram(program);
        GLES20.glDeleteShader(vertex_shader_id);
        GLES20.glDeleteShader(fragment_shader_id);

        int temp_texture_id[] = new int[1];
        GLES20.glGenTextures(1, temp_texture_id, 0);
        texture_id = temp_texture_id[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture_id);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, image.GetBuffer(), 0);

    }

    public void UpdatePosition(float positions[]) {
        this.position = positions;
        byte_buffer_positions = ByteBuffer.allocateDirect(position.length * 4);
        byte_buffer_positions.order(ByteOrder.nativeOrder());
        vertex_buffer = byte_buffer_positions.asFloatBuffer();
        vertex_buffer.put(position);
        vertex_buffer.position(0);
    }

    public void Draw(float[] mvp_matrix) {
        // TODO(Misael): Maybe uses VAOs VBOs? I don't know how they work on android.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture_id);
        GLES20.glUseProgram(program);
        int mvp_matrix_handle    = GLES20.glGetUniformLocation(program, "mvp_matrix");
        GLES20.glUniformMatrix4fv(mvp_matrix_handle, 1, false, mvp_matrix, 0);

        int position_handle = GLES20.glGetAttribLocation(program, "vertex_position");
        GLES20.glEnableVertexAttribArray(position_handle);
        GLES20.glVertexAttribPointer(position_handle, 3, GLES20.GL_FLOAT, false, 3*4, vertex_buffer);

        int texture_handle = GLES20.glGetAttribLocation(program, "texture_coords_in");
        GLES20.glEnableVertexAttribArray(texture_handle);
        GLES20.glVertexAttribPointer(texture_handle, 2, GLES20.GL_FLOAT, false, 2*4, texture_coords_buffer);

        int sampler_texture_handle = GLES20.glGetUniformLocation(program, "texture_sampler");
        GLES20.glUniform1i(sampler_texture_handle, 0);

        int uniform_color_handle = GLES20.glGetUniformLocation(program, "fragment_color");
        GLES20.glUniform4fv(uniform_color_handle, 1, color.GetNormalizeArray(), 0);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6,  GLES20.GL_UNSIGNED_SHORT, draw_buffer);
        GLES20.glDisableVertexAttribArray(position_handle);
        GLES20.glDisableVertexAttribArray(texture_handle);
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

    }

    public void Delete() {
        int temp[] = { texture_id};
        GLES20.glDeleteTextures(1, temp, 0);
        GLES20.glDeleteProgram(program);
        program = 0;
    }

    public String toString() {
        return String.format("RawModel { program: %d, positions { "+Arrays.toString(position)+"}}", program);
    }
}
