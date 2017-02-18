import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/** Interact with contents of system clipboard */
public class SysClipboard {
    public static String read() {
        String msg = "";
        try {
            msg = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch(UnsupportedFlavorException |IOException exc) {
            System.out.println("Read clipboard FAILED: " + exc.getClass().getName() + " " + exc);
        }
        return msg;
    }

    public static List<String> readAsLines() {
        String[] array = SysClipboard.read().split("\n");
        return Arrays.asList(array);
    }

    public static void write(String s) {
        StringSelection selection = new StringSelection(s);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
    }
}
