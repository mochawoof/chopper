import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.net.URI;
import com.formdev.flatlaf.FlatDarkLaf;
/**
 * chopper main class.
 */
public class Main {
    public static JFrame f;
    public static final String FOLDER = "C:\\Windows\\Temp\\chopper";
    private static final String GITHUB_URL = "https://github.com/mochawoof/chopper";
    public static final String UPDATE_URL = GITHUB_URL + "/releases/latest/download/chopper.jar";
    public static final String VERSION = "1.0.0";
    public static File folderFile;
    public static JProgressBar bar;
    public static void resetBar() {
        bar.setValue(0);
        bar.setString("");
    }
    public static void openInExplorer(File file) {
         try {
             Desktop.getDesktop().open(file);
         } catch (Exception e) {
             errorBox(e);
         }
    }
    public static void openInBrowser(String url) {
        try {
             Desktop.getDesktop().browse(new URI(url));
         } catch (Exception e) {
             errorBox(e);
         }
    }
    public static int getIconFromCaption(String caption) {
        int icon = JOptionPane.PLAIN_MESSAGE;
        String captionLower = caption.toLowerCase();
        if (captionLower.contains("error")) {
            icon = JOptionPane.ERROR_MESSAGE;
        } else if (captionLower.contains("warning")) {
            icon = JOptionPane.WARNING_MESSAGE;
        } else if (captionLower.contains("info")) {
            icon = JOptionPane.INFORMATION_MESSAGE;
        }
        return icon;
    }
    public static void box(String message, String caption) {
        JFrame frame = null;
        if (f != null) {
            frame = f;
        }
        JOptionPane.showMessageDialog(frame, message, caption, getIconFromCaption(caption));
    }
    public static int askBox(String message, String caption) {
        JFrame frame = null;
        if (f != null) {
            frame = f;
        }
        return JOptionPane.showConfirmDialog(frame, message, caption, JOptionPane.OK_CANCEL_OPTION, getIconFromCaption(caption));
    }
    public static void errorBox(Exception e) {
        box(e.toString(), "Error");
    }
    public static void main(String[] args) {
        folderFile = new File(FOLDER);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        
        FlatDarkLaf.setup();
        f = new JFrame();
        f.setResizable(false);
        f.setLayout(new BorderLayout());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("chopper " + VERSION);
        
        bar = new JProgressBar();
        bar.setStringPainted(true);
        resetBar();
        f.add(bar, BorderLayout.PAGE_START);
        
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        f.add(p, BorderLayout.CENTER);
        
        JButton open = new JButton("Open Folder");
        open.setToolTipText("Open the app folder.");
        open.addActionListener(e -> {
            openInExplorer(folderFile);
        });
        p.add(open);
        
        JButton backup = new JButton("Backup Folder");
        backup.setToolTipText("Compress the app folder into a zip file and save it in your Downloads folder.");
        backup.addActionListener(e -> {
            new Thread(() -> {
                Zipper.zip(folderFile.listFiles(), Zipper.DATE, Paths.get(System.getProperty("user.home")).resolve("Downloads"));
            }).start();
        });
        p.add(backup);
        
        JButton clean = new JButton("Clean Folder");
        clean.setToolTipText("Delete everything in the app folder.");
        clean.addActionListener(e -> {
            if (askBox("Are you sure you want to delete EVERYTHING in the app folder? This can't be undone!", "Warning") == JOptionPane.OK_OPTION) {
                new Thread(() -> {
                    for (File f : folderFile.listFiles()) {
                        Filer.delete(f);
                    }
                }).start();
            }
        });
        p.add(clean);
        
        JButton update = new JButton("Update");
        update.setToolTipText("Download the latest version from GitHub.");
        update.addActionListener(e -> {
            openInBrowser(UPDATE_URL);
        });
        p.add(update);
        
        JButton help = new JButton("?");
        help.setToolTipText("About chopper.");
        help.addActionListener(e -> {
            box("Tool to save and backup files to Temp.\n\nchopper is freeware under the terms of the GNU GPL v3.\n" + GITHUB_URL, "About chopper");
        });
        p.add(help);
        
        f.pack();
        f.setVisible(true);
        f.setIconImage(ResourceHelper.getResourceAsImage("icon.png"));
    }
}
