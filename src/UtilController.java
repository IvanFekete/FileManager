import org.omg.CORBA.SystemException;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class UtilController {

    private List<String> copiedFiles, copiedFolders;

    public String createNewFile(String globalPath) {
        System.out.println("Creating new file: " + globalPath);
        try {
            File file = new File(globalPath);
            if(file.createNewFile()) {
                return "OK, new file created.";
            }
            else {
                return "This file is already exist.";
            }
        }
        catch (IOException e) {
            return e.getMessage();
        }
    }

    public String createNewFolder(String globalPath) {
        System.out.println("Creating new folder:" + globalPath);
        File dir = new File(globalPath);
        if(dir.exists()) {
            return "This directory is already exist.";
        }
        else {
            try {
                dir.mkdirs();
                return "OK, new directory created.";
            }
            catch (SystemException e) {
                return e.getMessage();
            }
        }
    }

    private void delFolder(File node) {
        if(node.listFiles() != null) {
            for (File child : node.listFiles()) {
                delFolder(child);
            }
        }
        node.delete();
    }

    private String delFile(String globalPath) {
        try {
            Files.deleteIfExists(Paths.get(globalPath));
            return globalPath + ": OK, successful deletion.";
        }
        catch (NoSuchFileException e) {
            return globalPath + ": No such file exists";
        }
        catch (DirectoryNotEmptyException e) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "The directory '" + globalPath +"' is not empty, do you want to delete it?",
                    "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                delFolder(new File(globalPath));
                return globalPath + ": OK, successful deletion.";
            } else {
                return globalPath + ": OK, the directory is not deleted.";
            }
        }
        catch (IOException e) {
            return globalPath + ": " + e.getMessage();
        }
    }

    public ArrayList<String> delete(List<String> paths) {

        System.out.println("Deleting following files:");
        for(String dir : paths) {
            System.out.println(dir);
        }

        ArrayList<String> report = new ArrayList<>();
        for(String globalPath : paths) {
            int deleteOrNot = JOptionPane.showConfirmDialog(null, "Do you really want to delete '" + globalPath+ "' ?", "Warning",
                    JOptionPane.YES_NO_OPTION);
            if(deleteOrNot == JOptionPane.YES_OPTION) {
                report.add(delFile(globalPath));
            }
        }

        return report;
    }

    public void changeBuffer(List<String> dirs) {
        copiedFiles.clear();
        copiedFolders.clear();

        for(String dir : dirs) {
            if(dir.charAt(dir.length() - 1) == '/') {
                copiedFolders.add(dir);
            }
            else {
                copiedFiles.add(dir);
            }
            System.out.println(dir);
        }
    }

    private void pasteFolder(String globalPath, File v,  String pasteTo) {
        Path initPath = Paths.get(globalPath);
        Path newPath = Paths.get(pasteTo);
        try {
            Files.copy(initPath, newPath);
        }
        catch (IOException e) {

        }

        try {
            for (File to : v.listFiles()) {
                pasteFolder(globalPath + to.getName() + "/", to, pasteTo + to.getName() + "/");
            }
        }
        catch(NullPointerException e) {

        }
    }

    private String copyFile(String oldPath, String newPath) {
        if(oldPath.equals(newPath)) {
            return oldPath + " coincides with " + newPath + ", there is no changes.";
        }
        Path old = Paths.get(oldPath);
        Path nw = Paths.get(newPath);
        try {
            Files.copy(old, nw);
            return oldPath + "OK, copied successful to " + newPath +  ".";
        }
        catch  (FileAlreadyExistsException e){
            return oldPath + ": file '" + newPath + "' is already exist.";
        }
        catch(IOException e) {
            return oldPath + ": Failed to perform this operation.";
        }
    }

    public List<String> paste(String path) {
        List<String> result = new ArrayList<>();
        for(String curPath : copiedFiles) {
            String curReport = copyFile(curPath, path + "/" + new File(curPath).getName());
            result.add(curReport);
        }
        for(String curPath : copiedFolders) {
            File v = new File(curPath);
            pasteFolder(curPath, v, path + "/" + new File(curPath).getName() + "/");
            result.add(curPath + ": copied.");
        }

        return result;
    }

    public void move(List<String> dirs, String pathToMove) {
        changeBuffer(dirs);
        paste(pathToMove);
        delete(dirs);
    }
    public String rename(String oldPath, String newPath) {
        if(oldPath == newPath) {
            return "OK, filename is not changed.";
        }
        else {
            String copyReport = copyFile(oldPath, newPath);
            if(copyReport.contains("OK")) {
                delFile(oldPath);
                return "OK, " + oldPath + " is renamed to " + newPath + ".";
            }
            else {
                return copyReport;
            }
        }
    }

    public UtilController() {
        copiedFiles = new ArrayList<>();
        copiedFolders = new ArrayList<>();
    }


}
