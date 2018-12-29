import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.io.File;

public class WorkWithFiles {
    private static String rootPath = "E:\\FileManagerFolder";

    public static String getRootPath() {
        return rootPath;
    }

    public static String getGlobalPathStr(TreePath path) {
        String pathStr = rootPath;
        for(int i = 1; i < path.getPathCount(); i++) {
            pathStr += "/" + path.getPathComponent(i).toString();
        }
        return pathStr;
    }
    public static DefaultTreeModel getDefaultTreeModel() {
        File fileRoot = new File(rootPath);
        FileNode fileNode = new FileNode(fileRoot);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(fileNode);

        CreateChildNodes create = new CreateChildNodes(fileRoot, root);
        create.run();


        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        return treeModel;
    }

    public static String getFormat(String filename) {
        String format = "";
        for(int i = filename.length() - 1; i >= 0; i--) {
            if(filename.charAt(i) == '.') {
                break;
            }
            else {
                format = filename.charAt(i) + format;
            }
        }
        return format;
    }
}
