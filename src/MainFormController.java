import javax.swing.*;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MainFormController {

    private UtilController utilController;

    public void updateList(JList list, Stack<TreePath> history, TreePath path) {
        String globalPath = WorkWithFiles.getGlobalPathStr(path);
        File file = new File(globalPath);
        FileNode fileNode = new FileNode(file);

        list.setListData(fileNode.getFiles());
        if(!history.isEmpty() && !path.equals(history.peek())) {
            history.push(path);
        }
    }

    public void newFolder(JTree tree) {
        DialogCreator.showCreationDialog(tree, new NewFolderDialog(), utilController);
    }

    public void newFile(JTree tree) {
        DialogCreator.showCreationDialog(tree, new NewFileDialog(), utilController);
    }


    public List<String> getSelected(JTree tree, JList list) {
        if(tree.isSelectionEmpty()) {
            return new ArrayList<>();
        }
        List<String> selected = new ArrayList<>();

        List files = list.getSelectedValuesList();
        TreePath[] folders = tree.getSelectionPaths();

        if(files.isEmpty() || folders.length > 1) {
            for(TreePath path : folders) {
                if(files.isEmpty() || path != tree.getSelectionPath()) {
                    selected.add(WorkWithFiles.getGlobalPathStr(path) + "/");
                }
            }
        }
        String globalDirectoryPath = WorkWithFiles.getGlobalPathStr(tree.getSelectionPath());
        for (Object item : files) {
            selected.add(globalDirectoryPath + "/" + item.toString());
        }
        return selected;
    }

    public void deleteSelectedFiles(JTree tree, JList list) {
        List<String> filesToDel = getSelected(tree, list);


        String finalMessage = "";
        List<String> report = utilController.delete(filesToDel);
        for (String curReport : report) {
            finalMessage += curReport + "\n";
        }
        JOptionPane.showMessageDialog(null, finalMessage);

    }

    public void copySelectedFiles(JTree tree, JList list) {
        utilController.changeBuffer(getSelected(tree, list));
    }

    public void goBack(JTree tree, JList list, Stack<TreePath> history) {
        if(history.size() > 1) {
            history.pop();
            TreePath cur = history.pop();
            updateList(list, history, cur);
            tree.setSelectionPath(cur);
            System.out.println(cur);
        }

    }

    public void pasteToFolder(JTree tree) {
        if(tree.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "There is no selected folder.");
        }
        else {
            if(tree.getSelectionCount() > 1) {
                JOptionPane.showMessageDialog(null, "There are too many folders selected, please select exactly ONE folder.");
            }
            else {
                String pathToPaste = WorkWithFiles.getGlobalPathStr(tree.getSelectionPath());
                List<String> report = utilController.paste(pathToPaste);
                String finalReport = "";
                for(String curReport : report) {
                    finalReport += curReport + "\n";
                }

                JOptionPane.showMessageDialog(null, finalReport);

            }
        }
    }

    public void moveToFolder(JTree tree, JList list) {
        List<String> selected = getSelected(tree, list);
        String moveTo = DialogCreator.askDir("Select a destination folder:", WorkWithFiles.getRootPath());
        utilController.move(selected, moveTo);
    }

    public String renameFile(JTree tree, JList list) {
        String folderPath = WorkWithFiles.getGlobalPathStr(tree.getSelectionPath());
        String fileName = list.getSelectedValue().toString();
        String newFileName = DialogCreator.askNewName("Enter new name of the file:", fileName);
        String report = utilController.rename(folderPath + "/" + fileName, folderPath + "/" + newFileName);
        return report;
    }


    public MainFormController() {
        utilController = new UtilController();
    }
}
