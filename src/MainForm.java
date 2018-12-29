import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Stack;


public class MainForm {
    private JPanel rootPanel;

    private JTree tree1;
    private JTree tree2;
    private JList list1;
    private JList list2;
    private JButton newFileButton1;
    private JButton newFolderButton1;
    private JButton backButton1;
    private JButton backButton2;
    private JButton newFolderButton2;
    private JButton newFileButton2;
    private JButton delButton1;
    private JButton delButton2;
    private JButton copyButton1;
    private JButton copyButton2;
    private JButton openButton1;
    private JButton openButton2;
    private JButton pasteButton1;
    private JButton pasteButton2;
    private JButton moveButton1;
    private JButton moveButton2;
    private JButton renameButton1;
    private JButton renameButton2;

    private MainFormController controller;
    private Stack<TreePath> history1, history2;

    private void updateLists() {
        if(!tree1.isSelectionEmpty()) {
            controller.updateList(list1, history1, tree1.getSelectionPath());
        }
        else {
            controller.updateList(list1, history1, tree1.getPathForRow(0));
        }

        if(!tree2.isSelectionEmpty()) {
            controller.updateList(list2, history2, tree2.getSelectionPath());
        }
        else {
            controller.updateList(list2, history2, tree2.getPathForRow(0));
        }
    }

    private void updateAll() {
        updateTrees();
        updateLists();
    }

    private void updateTrees() {
        DefaultTreeModel treeModel = WorkWithFiles.getDefaultTreeModel();

        tree1.setModel(treeModel);
        tree2.setModel(treeModel);

        if(!history1.isEmpty()) tree1.setSelectionPath(history1.peek());
        if(!history2.isEmpty()) tree2.setSelectionPath(history2.peek());

    }


    public MainForm() {

        tree1.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                controller.updateList(list1, history1, treeSelectionEvent.getPath());
            }
        });
        tree2.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                controller.updateList(list2, history2, treeSelectionEvent.getPath());
            }
        });
        newFileButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.newFile(tree1);
                updateLists();
            }
        });
        newFolderButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.newFolder(tree1);
                updateAll();
            }
        });
        newFileButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.newFile(tree2);
                updateLists();
            }
        });
        newFolderButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.newFolder(tree2);
                updateAll();
            }
        });
        delButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.deleteSelectedFiles(tree1, list1);
                updateAll();
            }
        });
        delButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.deleteSelectedFiles(tree2, list2);
                updateAll();
            }
        });

        backButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.goBack(tree1, list1, history1);
                updateAll();
            }
        });
        backButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.goBack(tree2, list2, history2);
                updateAll();
            }
        });
        openButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DialogCreator.showEditor(tree2, list2);
            }
        });
        openButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DialogCreator.showEditor(tree1, list1);
            }
        });
        copyButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.copySelectedFiles(tree1, list1);
            }
        });
        copyButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.copySelectedFiles(tree2, list2);
            }
        });
        pasteButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.pasteToFolder(tree1);
                updateAll();
            }
        });
        pasteButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.pasteToFolder(tree2);
                updateAll();
            }
        });
        moveButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.moveToFolder(tree1, list1);
                updateAll();
            }
        });
        moveButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.moveToFolder(tree2, list2);
                updateAll();
            }
        });
        renameButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, controller.renameFile(tree1, list1));
                updateLists();
            }
        });
        renameButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, controller.renameFile(tree2, list2));
                updateLists();
            }
        });
    }


    private void createUIComponents() {
        controller = new MainFormController();

        DefaultTreeModel treeModel = WorkWithFiles.getDefaultTreeModel();

        tree1 = new JTree(treeModel);
        tree2 = new JTree(treeModel);
        tree1.setSelectionPath(tree1.getPathForRow(0));
        tree2.setSelectionPath(tree2.getPathForRow(0));

        DefaultTreeCellRenderer renderer1 = (DefaultTreeCellRenderer) tree1.getCellRenderer();
        renderer1.setLeafIcon(renderer1.getOpenIcon());
        DefaultTreeCellRenderer renderer2 = (DefaultTreeCellRenderer) tree2.getCellRenderer();
        renderer2.setLeafIcon(renderer2.getOpenIcon());

        history1 = new Stack<>();
        history1.push(tree1.getPathForRow(0));

        history2 = new Stack<>();
        history2.push(tree2.getPathForRow(0));

        File fileRoot = new File(WorkWithFiles.getRootPath());
        FileNode fileNode = new FileNode(fileRoot);

        list1 = new JList<>(fileNode.getFiles());
        list2 = new JList<>(fileNode.getFiles());

    }

    private void $$$setupUI$$$() {
        createUIComponents();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}


