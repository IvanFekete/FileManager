import jdk.nashorn.internal.runtime.WithObject;

import javax.swing.*;
import java.awt.event.*;


public class TextEditor{
    private JTextPane textPane;
    private JPanel rootPanel;
    private JButton saveButton;
    private JButton loadFromButton;
    private JButton openButton;
    private JLabel fileNameLabel;
    private JButton mergeWithButton;
    private JButton searchHtmlTagsButton;
    private JButton replaceTagButton;
    private JButton copyButton;
    private JButton pasteButton;
    private JButton imagesLinkButton;
    private JButton searchButton;

    private TextEditorController controller;



    public JPanel getRootPanel() {
        return rootPanel;
    }


    private void addListeners() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.save(textPane, fileNameLabel);
            }
        });
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.openOrNot(textPane, fileNameLabel);
            }
        });
        loadFromButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.loadOrNot(textPane, fileNameLabel);
            }
        });
        textPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                controller.setEdited(fileNameLabel);
            }
        });

        mergeWithButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.addOrNot(textPane, fileNameLabel);
            }
        });
        searchHtmlTagsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.searchHtmlTags(textPane);
            }
        });
        replaceTagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.replaceTag(textPane, fileNameLabel);
            }
        });
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.setClipboardContents(textPane.getSelectedText());

            }
        });
        pasteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.paste(textPane, fileNameLabel);
            }
        });
        imagesLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.printImagesLinks(textPane);
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.searchSimilar(textPane);
            }
        });
    }

    public TextEditor() {
        controller = new TextEditorController();

        addListeners();
    }

    public TextEditor(String path) {
        controller = new TextEditorController();
        if(WorkWithFiles.getFormat(path).toLowerCase().equals("xml")) {
            textPane.setEditorKitForContentType("text/xml", new XmlEditorKit());
            textPane.setContentType("text/xml");
        }
        controller.openFile(textPane, fileNameLabel, path);

        addListeners();
    }

    public void beforeClosing() {
        if(!controller.isSaved()) controller.saveOrNot(textPane, fileNameLabel);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("TextEditor");
        frame.setContentPane(new TextEditor().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
