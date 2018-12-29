import javax.swing.*;
import javax.swing.tree.TreePath;
import java.util.List;

public class DialogCreator {

    private static void showNewDialog(JDialog dialog) {
        dialog.setSize(400, 100);
        dialog.setLocation(400, 100);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private static void showNewLongDialog(JDialog dialog) {
        dialog.setSize(400,  600);
        dialog.setLocation(400, 100);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    public static void showTextEditor(String path) {
        TextEditor textEditor = new TextEditor(path);

        JFrame frame = new JFrame("Text Editor");
        frame.setSize(800, 800);
        frame.setContentPane(textEditor.getRootPanel());
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                textEditor.beforeClosing();
                e.getWindow().dispose();
            }
        });
        frame.pack();
        frame.setVisible(true);

    }

    public static void showTableEditor(String path) {
        TableEditor tableEditor = new TableEditor(path);

        JFrame frame = new JFrame("Table Editor");
        frame.setSize(800, 800);
        frame.setContentPane(tableEditor.getRootPanel());
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                tableEditor.beforeClosing();
                e.getWindow().dispose();
            }
        });

        frame.pack();
        frame.setVisible(true);

    }

    public static void showEditor(JTree tree, JList list) {
        if(list.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "There are no selected files.");
        }
        else {
            String fileToOpen = list.getSelectedValue().toString();
            String path = WorkWithFiles.getGlobalPathStr(tree.getSelectionPath()) + "/" +fileToOpen;

            if(WorkWithFiles.getFormat(fileToOpen).equals("etab") ) {
                showTableEditor(path);
            }
            else {
                showTextEditor(path);
            }
        }
    }



    public static void showCreationDialog(JTree tree, CreationDialog dialog, UtilController controller) {
        if(tree.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "There are no selected folders.");
        }
        else {
            TreePath path = tree.getSelectionPath();
            dialog.setDir(WorkWithFiles.getGlobalPathStr(path));
            dialog.setController(controller);
            showNewDialog(dialog);
        }
    }

    public static String askDir(String text, String defaultText) {
        return JOptionPane.showInputDialog(null, text, defaultText);
    }

    public static String askNewName(String text, String defaultText) {
        return JOptionPane.showInputDialog(null, text, defaultText);
    }

    public static void showSearchResultDialog(List<Tag> tags) {
        showNewLongDialog(new SearchTagsDialog(tags));
    }

    public static void showLinksResultDialog(String[] links) {
        String message = "";
        for(String link : links) {
            message += link + "\n";
        }
        JOptionPane.showMessageDialog(null, message);
    }

    public static void showHelp() {
        String message = "List of supported operations:\n" +
                "+ (addition)\n" +
                "- (subtraction)\n" +
                "* (multiplication)\n" +
                "/ (discrete division)\n" +
                "% (carrying)\n" +
                "^ (powering)\n" +
                "Also you can use brackets ( and ) in your expressions.\n" +
                "All operations are supported for 32-bit signed integer numbers and for values from other cells.\n" +
                "WARNING: your expression should be started by opening bracket and finished by closing bracket,\n" +
                "for example, if you want to calculate the sum 1 + 2, your final expression should be (1 + 2).\n" +
                "If your expressions will contain cyclic dependencies or incorrect mathematical expressions\n" +
                "(for example, too few operands or unbalanced sequence brackets), your value\n" +
                "will be assigned as ERR. Also, if your expression uses an operand equal ERR,\n" +
                " value of the expression also will be ERR." ;
        JOptionPane.showMessageDialog(null, message);
    }
}
