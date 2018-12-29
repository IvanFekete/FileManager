import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.TreeSet;

public class TextEditorController {
    private String openedFile;
    private Boolean saved;
    private TextTransfer textTransfer;


    public Boolean isSaved() {
        return saved;
    }

    public void save(JTextPane textPane, JLabel fileNameLabel) {
        try {
            File file = new File(openedFile);
            FileOutputStream outputStream = new FileOutputStream(file, false);

            byte[] myBytes = textPane.getText().getBytes();
            try {
                outputStream.write(myBytes);
                outputStream.close();
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Some error occurred while saving file.");
            }
            saved = true;
            fileNameLabel.setText(openedFile);
        }
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File you are editing now is not found.");
        }
    }


    public void saveOrNot(JTextPane textPane, JLabel fileNameLabel) {
        int dialogResult = JOptionPane.showConfirmDialog(null, "The file '" + openedFile +"' is not saved, do you want to save it?",
                "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            save(textPane, fileNameLabel);
        }
    }

    public void setEdited(JLabel fileNameLabel) {
        if(fileNameLabel.getText().charAt(0) != '*') {
            fileNameLabel.setText("*" + fileNameLabel.getText());
        }
        saved = false;
    }


    public void loadFile(JTextPane textPane, JLabel fileNameLabel,  String path) {
        String text = "";
        try {
            text = new String(Files.readAllBytes(Paths.get(path)));
            textPane.setText(text);
            setEdited(fileNameLabel);
        }
        catch(NoSuchFileException e) {
            JOptionPane.showMessageDialog(null, "There is no such file with this path");
        }
        catch(IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Some error occurred while reading text.");
        }
    }

    public void openFile(JTextPane textPane, JLabel fileNameLabel, String path) {
        if(!saved) {
            saveOrNot(textPane, fileNameLabel);
        }
        openedFile = path;
        System.out.println(path);
        fileNameLabel.setText(path);
        loadFile(textPane, fileNameLabel, path);
        saved = true;
        fileNameLabel.setText(path);
    }

    public void addThisFile(JTextPane textPane, JLabel fileNameLabel, String path) {
        String text = "";
        try {
            text = new String(Files.readAllBytes(Paths.get(path)));
            textPane.setText(textPane.getText() + "\n" + text);
            setEdited(fileNameLabel);
        }
        catch(NoSuchFileException e) {
            JOptionPane.showMessageDialog(null, "There is no such file with this path");
        }
        catch(IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Some error occurred while reading text.");
        }
    }


    private void modifyText(JTextPane textPane, JLabel fileNameLabel, String newText) {
        textPane.setText(newText);
        setEdited(fileNameLabel);
    }



    public void replaceTag(JTextPane textPane, JLabel fileNameLabel) {
        String initTag = JOptionPane.showInputDialog(null, "Enter tag name you want to find", "tag");
        String replaceTag = JOptionPane.showInputDialog(null, "Enter tag name you want to replace", "tag");
        String newText = TextProcessor.getReplaced(textPane.getText(), initTag, replaceTag);

        try {
            modifyText(textPane, fileNameLabel, newText);
        }
        catch(NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Failed to replace: some value missing");
            return;
        }

        JOptionPane.showMessageDialog(null, "Successfully replaced");
    }

    public void searchHtmlTags(JTextPane textPane) {
        DialogCreator.showSearchResultDialog(TextProcessor.searchHtmlTags(textPane.getText()));
    }

    public void setClipboardContents(String selectedText) {
        textTransfer.setClipboardContents(selectedText);
    }

    public void paste(JTextPane textPane, JLabel fileNameLabel) {
        textPane.paste();
        setEdited(fileNameLabel);
    }

    public void printImagesLinks(JTextPane textPane) {
        String[] result = TextProcessor.searchImagesLink(textPane.getText());
        DialogCreator.showLinksResultDialog(result);
    }

    public void searchSimilar(JTextPane textPane) {
        String wordToFind = JOptionPane.showInputDialog(null, "Enter a word you want to find:", "word");

        if(wordToFind != null) {
            TreeSet<String> words = TextProcessor.searchSimilar(textPane.getText(), wordToFind);
            String message = "There are words may be similar with your word:\n";
            for (String word : words) {
                message += word + "\n";
            }
            JOptionPane.showMessageDialog(null, message);
        }
    }


    public void openOrNot(JTextPane textPane, JLabel fileNameLabel) {
        String pathToOpen = JOptionPane.showInputDialog(null, "Enter full path of the file you want to open:", "/home/fekete/Desktop/");
        if(pathToOpen != null) openFile(textPane, fileNameLabel, pathToOpen);
    }

    public void loadOrNot(JTextPane textPane, JLabel fileNameLabel) {
        String pathToOpen = JOptionPane.showInputDialog(null, "Enter full path of the file you want to load from:", "/home/fekete/Desktop/");
        if(pathToOpen != null) loadFile(textPane, fileNameLabel, pathToOpen);
    }

    public void addOrNot(JTextPane texPane, JLabel fileNameLabel) {
        String pathToLoad = JOptionPane.showInputDialog(null, "Enter full path of the file you want to merge:", "/home/fekete/Desktop/");
        addThisFile(texPane, fileNameLabel, pathToLoad);
    }

    public TextEditorController() {
        saved = true;
        openedFile = "";
        textTransfer = new TextTransfer();

    }
}
