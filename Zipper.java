/**
 * Static file compressor class.
 */
import java.io.File;
import java.nio.file.*;
import java.time.LocalDate;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import javax.swing.JOptionPane;
public class Zipper {
    public static final String DATE = "DATE";
    public static void zip(File[] files, String fileName, Path destination) {
        try {
            ZipParameters params = new ZipParameters();
            params.setCompressionLevel(CompressionLevel.HIGHER);
            
            File resolvedDestination = destination.resolve(fileName).toFile();
            if (fileName.equals(DATE)) {
                resolvedDestination = destination.resolve(LocalDate.now().toString() + ".zip").toFile();
            }
            
            //Ternary: Prevents bar maximum from becoming 0
            Main.bar.setMaximum(files.length);
            Main.bar.setValue(0);
            Main.bar.setString("Zipping...");
            
            boolean ok = true;
            if (resolvedDestination.exists()) {
                if (Main.askBox(resolvedDestination.getName() + " already exists. Do you want to replace it?", "Warning") == JOptionPane.OK_OPTION) {
                    ok = true;
                    resolvedDestination.delete();
                } else {
                    ok = false;
                }
            }
            if (ok) {
                System.out.println("Zipping into " + resolvedDestination.getPath() + "...");
                
                resolvedDestination.createNewFile();
                ZipFile zipped = new ZipFile(resolvedDestination.getPath());
                for (File f : files) {
                    if (f.isDirectory()) {
                        zipped.addFolder(f, params);
                    } else {
                        zipped.addFile(f, params);
                    }
                    Main.bar.setValue(Main.bar.getValue() + 1);
                }
                zipped.close();
                Main.openInExplorer(resolvedDestination);
            }
        } catch (Exception e) {
            Main.errorBox(e);
        }
        Main.resetBar();
    }
}
