import javax.swing.tree.DefaultMutableTreeNode;
import java.lang.*;
import java.io.File;

public class CreateChildNodes implements Runnable {
    private DefaultMutableTreeNode root;
    private File fileRoot;

    public CreateChildNodes(File fileRoot, DefaultMutableTreeNode root) {
        this.fileRoot = fileRoot;
        this.root = root;
    }

    @Override
    public void run() {
        createChildren(fileRoot, root);
    }

    private void createChildren(File file, DefaultMutableTreeNode node) {
        File[] files = file.listFiles();
        if(files == null) return;

        for(File newFile : files) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new FileNode(newFile));
            if(newFile.isDirectory()) {
                node.add(childNode);
                createChildren(newFile, childNode);
            }
        }
    }
}
