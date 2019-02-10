package com.urquieta.something.platform.android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.res.AssetManager;
import android.content.res.AssetFileDescriptor;

import java.io.InputStream;

import com.urquieta.something.platform.Image;
import com.urquieta.something.output.OutputSystem;
import com.urquieta.something.game.util.Color;

public class AndroidImageLoader {
    AssetManager asset_manager;

    public AndroidImageLoader() {

    }

    public void Init(Activity activity) {
        this.asset_manager = activity.getAssets();
        try {
            for (String asset : asset_manager.list("")) {
                System.out.println("SOMETHING ASSETS: "+asset);
            }
        } catch (Exception e) {

        }
    }

    public Image CreateImage(String path, Color color_blend) {
        Image result = CreateImage(path);

        Bitmap buffer = result.GetBuffer();
        int width  = buffer.getWidth();
        int height = buffer.getHeight();
        int[] pixels = new int[width*height];
        buffer.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int index = 0; index < pixels.length; index++) {
            Color original_color = new Color(pixels[index]);
            original_color.Blend(color_blend);
            pixels[index] = original_color.GetColorInt();
        }

        buffer.setPixels(pixels, 0, width, 0, 0, width, height);

        return result;
    }

    public Image CreateImage(String filename) {
        Image image = new Image();
        try {
            InputStream input_stream = this.asset_manager.open(filename);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap buffer_image = BitmapFactory.decodeStream(input_stream, null, options);
            image.SetBuffer(buffer_image);
        } catch (Exception e) {
            OutputSystem.DebugPrint(e.toString(), OutputSystem.ERRORS);
        }

        return image;
    }
}
