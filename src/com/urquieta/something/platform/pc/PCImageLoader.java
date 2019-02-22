package com.urquieta.something.platform.pc;

import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import com.urquieta.something.platform.Image;
import com.urquieta.something.output.OutputSystem;
import com.urquieta.something.game.util.Color;

public class PCImageLoader {
    public PCImageLoader() {

    }

    public static Image CreateImage(String path, Color color_blend) {
        Image result = CreateImage(path);
        result.color_blend = color_blend;
        BufferedImage buffer = result.GetBuffer();
        int width  = buffer.getWidth();
        int height = buffer.getHeight();
        int[] pixels = buffer.getRGB(0, 0, width, height, null, 0, width);

        for (int index = 0; index < pixels.length; index++) {
            Color original_color = new Color(pixels[index]);
            original_color.Blend(color_blend);
            pixels[index] = original_color.GetColorInt();
        }

        buffer.setRGB(0, 0, width, height, pixels, 0, width);

        return result;
    }

    public static Image CreateImage(String path) {
        Image image = new Image();
        path = "assets/"+path;
        try {
            BufferedImage buffer_image = ImageIO.read(new File(path));
            image.SetBuffer(buffer_image);
        } catch (Exception e) {
            OutputSystem.DebugPrint("SOMETHING_ERROR: "+e, OutputSystem.ERRORS);
        }
        return image;
    }
}
