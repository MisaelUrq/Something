package com.urquieta.something.platform.renderer.models;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

// TODO(Misael): Make sure all of this dependencies are platform free.
// import static android.opengl.GLES20.*;

// NOTE(Misael): This is gone be the class for the OpenGL abstraction.
import static com.urquieta.something.platform.pc.PCOpenGL.*;

import com.urquieta.something.platform.Renderer;
import com.urquieta.something.game.util.Color;
import com.urquieta.something.utils.Buffers;

public class RawModel {
    // TODO(Misael): Do we need more shaders right now?

    // TODO(Misael): Find if the shaders work the same on OpenGL and OpenGL ES.
    private final String vertex_shader =
        "uniform mat4 mvp_matrix; attribute vec4 vertex_position;" +
        "void main() { gl_Position  = mvp_matrix * vertex_position; }";

    private final String fragment_shader =
        "uniform vec4 fragment_color;"+
        " void main() { gl_FragColor = fragment_color; }";

    private int program;
    FloatBuffer vertex_buffer;
    ShortBuffer  draw_buffer;
    ByteBuffer byte_buffer;

    Color color;
    float position[];
    short draw_order[];

    public RawModel(float positions[], short draw_order[], Color color) {
        this.draw_order = draw_order;
        this.position = positions;
        this.color = color;
        vertex_buffer = Buffers.ArrayToBuffer(position);
        draw_buffer   = Buffers.ArrayToBuffer(draw_order);
        byte_buffer   = Buffers.GenByteBuffer(draw_order);
    }

    public boolean HasBeenCreated() {
        return (program != 0);
    }

    public void CreateProgram() {
        int vertex_shader_id   = Renderer.LoadShader(GL_SHADER_VERTEX, vertex_shader);
        int fragment_shader_id = Renderer.LoadShader(GL_SHADER_FRAGMENT, fragment_shader);
        program = GLCreateProgramAndLinkShaders(vertex_shader_id, fragment_shader_id);
    }

    public void UpdatePosition(float positions[]) {
        this.position = positions;
        vertex_buffer = Buffers.ArrayToBuffer(position);
    }

    public void Draw(float[] mvp_matrix) {
        // TODO(Misael): Maybe uses VAOs VBOs? I don't know how they work on android.
        GLUseProgram(program);

        int position_handle = GLActiveVertexAttrib("vertex_position", vertex_buffer, 3, 4);
        GLUniformMatrix4fv("mvp_matrix", mvp_matrix);
        GLUniformMatrix4fv("fragment_color", color.GetNormalizeArray());

        // int uniform_color_handle = glGetUniformLocation(program, "fragment_color");
        // FloatBuffer color_buffer = Buffers.ArrayToBuffer(color.GetNormalizeArray());
        // glUniform4fv(uniform_color_handle, 1, color.GetNormalizeArray(), 0);

        GLDrawElements(byte_buffer);
        GLDisableVertexAttrib(position_handle);
    }

    public void Delete() {
        GLDeleteProgram(program);
        program = 0;
    }

    public String toString() {
        return String.format("RawModel { program: %d, positions { "+Arrays.toString(position)+"}}", program);
    }
}
