package com.urquieta.something.output;

import java.io.File;
import java.io.FileOutputStream;

public class OutputSystem {

    public enum Levels {
        NONE,
        MESSAGES,
        WARNINGS,
        ERRORS,
    }

    private static Levels  level    = Levels.NONE;
    private static boolean did_error_occur = false;

    private static boolean write_on_file = false;
    private static File   file = null;

    public static void WriteErrosOnFile(String path, String filename) {
        OutputSystem.write_on_file = true;
        OutputSystem.file = new File(path, filename);
    }

    public static void SetLevel(Levels level) {
        OutputSystem.level = level;
    }

    public static void DebugPrint(String message, Levels level) {
        switch (level) {
        case WARNINGS: {
            String output_message = "SOMETHING_WARNING: "+message;
            if (write_on_file && OutputSystem.file != null) {
                OutputSystem.WriteToFile(output_message);
            }
            new Exception(output_message).printStackTrace(System.out);
        } break;
        case ERRORS: {
            // TODO(Misael): Make a option to  display this  in the game as a window or something.
            String output_message = "SOMETHING_ERROR: "+message;
            if (write_on_file && OutputSystem.file != null) {
                OutputSystem.WriteToFile(output_message);
            }
            new Exception(output_message).printStackTrace(System.err);
            OutputSystem.did_error_occur = true;
        } break;
        case MESSAGES: {
            String output_message = "SOMETHING_MESSAGE: "+message;
            System.out.println(output_message);
        } break;
        default:
            break;
        }
    }

    // TODO(Misael): Test this since I haven't actually run any of this.
    private static void WriteToFile(String message) {
        try {
            OutputSystem.file.createNewFile();
            FileOutputStream stream = new FileOutputStream(OutputSystem.file);
            stream.write(message.getBytes());
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean DidErrorOccur() {
        return OutputSystem.did_error_occur;
    }
}
