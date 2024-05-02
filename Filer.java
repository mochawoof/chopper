import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import javax.swing.JOptionPane;
/**
 * Static file helper class.
 */
public class Filer {
    public static void delete(File file) {
        try {
            Main.bar.setMaximum(file.listFiles().length);
            Main.bar.setValue(0);
            Main.bar.setString("Deleting...");
            deleteLayer(file, true);
        } catch (IOException e) {
            Main.errorBox(e);
        }
        Main.resetBar();
    }
    public static void deleteLayer(File file, boolean updateBar) throws IOException {
        boolean ok = true;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteLayer(f, false);
                if (updateBar) {
                    Main.bar.setValue(Main.bar.getValue() + 1);
                }
            }
            ok = file.delete();
        } else {
            ok = file.delete();
        }
        if (!ok) {
            throw new IOException();
        }
    }
}
