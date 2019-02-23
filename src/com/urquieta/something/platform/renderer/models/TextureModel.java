package com.urquieta.something.platform.renderer.models;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

// TODO(Misael): Make sure all of this dependencies are platform free.
// import static android.opengl.GLES20.*;
// import android.opengl.GLUtils;
// import android.graphics.Bitmap;

import static com.urquieta.something.platform.OpenGL.*;

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
        int vertex_shader_id   = Renderer.LoadShader(GL_SHADER_VERTEX, vertex_shader);
        int fragment_shader_id = Renderer.LoadShader(GL_SHADER_FRAGMENT, fragment_shader);

        program = GLCreateProgramAndLinkShaders(vertex_shader_id, fragment_shader_id);
        texture_id = GLGenDefaultTexture();
        // GLUtils.texImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.GetBuffer(), 0);
    }

    public void UpdatePosition(float positions[]) {
        this.position = positions;
        vertex_buffer = Buffers.ArrayToBuffer(positions);
    }

    public void Draw(float[] mvp_matrix) {
        // TODO(Misael): Maybe uses VAOs VBOs? I don't know how they work on android.
        GLUseTexture(texture_id);
        GLUseProgram(program);
        GLUniformMatrix4fv("mvp_matrix", mvp_matrix);
        int position_handle = GLActiveVertexAttrib("vertex_position", vertex_buffer, 3, 4);
        int texture_handle  = GLActiveVertexAttrib("texture_coords_in", texture_coords_buffer, 2, 4);
        GLUniform1i("texture_sampler");
        GLUniform4fv("fragment_color", color.GetNormalizeArray());
        GLInitBlendConfig();
        GLDrawElements(byte_buffer);
        GLDisableVertexAttrib(position_handle);
        GLDisableVertexAttrib(texture_handle);
        GLEndBlendConfig();
    }

    public void Delete() {
        GLDeleteProgram(program);
        GLDeleteTexture(texture_id);
        program = 0;
    }

    public String toString() {
        return String.format("RawModel { program: %d, positions { "+Arrays.toString(position)+"}}", program);
    }
}
