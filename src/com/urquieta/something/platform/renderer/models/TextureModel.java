package com.urquieta.something.platform.renderer.models;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

// TODO(Misael): Make sure all of this dependencies are platform free.
import static android.opengl.GLES20.*;
import android.opengl.GLUtils;
import android.graphics.Bitmap;

// import static org.lwjgl.opengl.GL20.*;
// import static org.lwjgl.opengl.GL13.*;

import com.urquieta.something.platform.Renderer;
import com.urquieta.something.platform.Image;
import com.urquieta.something.platform.ImageLoader;
import com.urquieta.something.game.util.Color;
import com.urquieta.something.utils.Buffers;


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
    ShortBuffer draw_buffer;
    FloatBuffer vertex_buffer;
    FloatBuffer texture_coords_buffer;
    ByteBuffer  byte_buffer;

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
        vertex_buffer = Buffers.ArrayToBuffer(positions);
        draw_buffer   = Buffers.ArrayToBuffer(draw_order);
        texture_coords_buffer = Buffers.ArrayToBuffer(texture_coords);
        image = ImageLoader.CreateImage(filename);
        byte_buffer = Buffers.GenByteBuffer(draw_order);
    }

    public boolean HasBeenCreated() {
        return (program != 0);
    }

    public void CreateProgram() {
        int vertex_shader_id   = Renderer.LoadShader(GL_VERTEX_SHADER, vertex_shader);
        int fragment_shader_id = Renderer.LoadShader(GL_FRAGMENT_SHADER, fragment_shader);

        program = glCreateProgram();
        glAttachShader(program, vertex_shader_id);
        glAttachShader(program, fragment_shader_id);
        glLinkProgram(program);
        glDeleteShader(vertex_shader_id);
        glDeleteShader(fragment_shader_id);

        int temp_texture_id[] = new int[1];
        glGenTextures(1, temp_texture_id, 0);
        texture_id = temp_texture_id[0];
        glBindTexture(GL_TEXTURE_2D, texture_id);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        GLUtils.texImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.GetBuffer(), 0);
    }

    public void UpdatePosition(float positions[]) {
        this.position = positions;
        vertex_buffer = Buffers.ArrayToBuffer(positions);
    }

    public void Draw(float[] mvp_matrix) {
        // TODO(Misael): Maybe uses VAOs VBOs? I don't know how they work on android.
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture_id);
        glUseProgram(program);
        int mvp_matrix_handle    = glGetUniformLocation(program, "mvp_matrix");
        FloatBuffer mvp_buffer = Buffers.ArrayToBuffer(mvp_matrix);
        glUniformMatrix4fv(mvp_matrix_handle, 1, false, mvp_matrix, 0);

        int position_handle = glGetAttribLocation(program, "vertex_position");
        glEnableVertexAttribArray(position_handle);
        glVertexAttribPointer(position_handle, 3, GL_FLOAT, false, 3*4, vertex_buffer);

        int texture_handle = glGetAttribLocation(program, "texture_coords_in");
        glEnableVertexAttribArray(texture_handle);
        glVertexAttribPointer(texture_handle, 2, GL_FLOAT, false, 2*4, texture_coords_buffer);

        int sampler_texture_handle = glGetUniformLocation(program, "texture_sampler");
        glUniform1i(sampler_texture_handle, 0);

        int uniform_color_handle = glGetUniformLocation(program, "fragment_color");
        glUniform4fv(uniform_color_handle, 1, color.GetNormalizeArray(), 0);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glDrawElements(GL_TRIANGLES, 6,  GL_UNSIGNED_SHORT, byte_buffer);
        glDisableVertexAttribArray(position_handle);
        glDisableVertexAttribArray(texture_handle);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    public void Delete() {
        int temp[] = { texture_id};
        glDeleteTextures(1, temp, 0);
        glDeleteProgram(program);
        program = 0;
    }

    public String toString() {
        return String.format("RawModel { program: %d, positions { "+Arrays.toString(position)+"}}", program);
    }
}
