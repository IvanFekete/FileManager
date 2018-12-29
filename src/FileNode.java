import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileNode {
    private File file;

    public FileNode(File file) {
        this.file = file;
    }

    private FileNode[] toArr(List<FileNode> lst) {
        FileNode[] result = new FileNode[lst.size()];
        lst.toArray(result);
        return result;
    }

    public FileNode[] getFiles() {
        File[] files = file.listFiles();
        List<FileNode> result = new ArrayList<>();
        if(files == null) return toArr(result);
        for(File child : files) {
            if(!child.isDirectory()) {
                result.add(new FileNode(child));
            }
        }
        return toArr(result);
    }

    @Override
    public String toString() {
        String name = file.getName();
        return (name == "") ? file.getAbsolutePath() : name;
    }


}
