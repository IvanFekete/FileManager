import javax.swing.*;
import java.awt.event.*;

public class TableEditor {
    private JTable table;
    private JPanel panel;
    private JButton viewFormulas;
    private JButton viewValues;
    private JButton saveButton;
    private JButton addRowButton;
    private JButton addColumnButton;
    private JButton delRowButton;
    private JButton deleteColumnButton;
    private JLabel fileNameLabel;
    private JLabel modeLabel;
    private JButton helpButton;
    private JScrollPane jscrp;
    private TableController controller;

    public JPanel getRootPanel() {
        return panel;
    }


    public TableEditor(String path) {
        controller = new TableController(path);
        controller.constructJTable(table, fileNameLabel, modeLabel);
        String[][] rowIndetifiers = new String[controller.N][1];
        for(int i = 0; i < controller.N; i++) {
            rowIndetifiers[i][0] = String.valueOf(i);
        }
        String[] cell = new String[1];
        cell[0] = "";


        JTable rowHeaderTable = new JTable(rowIndetifiers, cell);
        rowHeaderTable.setRowHeight(30);
        rowHeaderTable.getColumnModel().getColumn(0).setMaxWidth(40);
        rowHeaderTable.getColumnModel().getColumn(0).setMinWidth(40);
        rowHeaderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JViewport rowHeader = new JViewport();
        rowHeader.setView(rowHeaderTable);
        rowHeader.setPreferredSize(rowHeaderTable.getMaximumSize());

        jscrp.setRowHeader(rowHeader);


        viewFormulas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.setFormulasMode(table, fileNameLabel, modeLabel);
            }
        });

        viewValues.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.setValuesMode(table, fileNameLabel, modeLabel);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.save(table, fileNameLabel);
            }
        });

        table.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                controller.setEdited(fileNameLabel);
                super.focusGained(focusEvent);
            }
        });
        delRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.delRowOrNot(table, fileNameLabel);
            }
        });
        deleteColumnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.delColumnOrNot(table, fileNameLabel);
            }
        });
        addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.addRowOrNot(table, fileNameLabel);
            }
        });
        addColumnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.addColumnOrNot(table, fileNameLabel);
            }
        });
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogCreator.showHelp();
            }
        });
    }


    public void beforeClosing(){
        if(!controller.isSaved()) {
            controller.saveOrNot(table, fileNameLabel);
        }
    }


}
