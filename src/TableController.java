import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;
import java.util.TreeSet;

public class TableController {
    private String path;
    private Expression[][] table;
    public static final int N = 20;
    private ResultsSaver saver;
    private int mode;
    private boolean saved;

    private void loadFrom(String newPath) {
        String text = "";
        try {
            text = new String(Files.readAllBytes(Paths.get(newPath)));
            String[] tokens = text.split("\n");
            for(int i = 0, k = 0; i < N; i++) {
                for(int j = 0; j < N; j++, k++) {
                    table[i][j] = new Expression(k < tokens.length ? tokens[k] : "0");
                }
            }
            path = newPath;
        }
        catch(NoSuchFileException e) {
            JOptionPane.showMessageDialog(null, "There is no such file with this path");
        }
        catch(IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Some error occurred while reading text.");
        }
    }

    public TableController(String fileName) {
        this.path = fileName;
        mode = 0;
        table = new Expression[N][N];
        saver = new ResultsSaver();
        saved = true;
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                table[i][j] = new Expression("");
            }
        }
        loadFrom(fileName);
        update();
    }
    private void calculate(TreeSet<String> processing, TreeSet<String> processed, String v){
        processing.add(v);
        Point p = getCoordinates(v);
        List<String> links = table[p.getX()][p.getY()].getCoordinates();
        for(String to : links) {
            if(processing.contains(to)) {
                saver.setResult(v, "ERR");
                table[p.getX()][p.getY()].setIncorrect();
            }
            else if(!processed.contains(to)) {
                calculate(processing, processed, to);
            }

            if(saver.getResult(to).equals("ERR")) {
                saver.setResult(v, "ERR");
                table[p.getX()][p.getY()].setIncorrect();
            }
        }
        processing. remove(v);
        processed.add(v);

        if(!saver.containsKey(v)) {
            String val = String.valueOf(table[p.getX()][p.getY()].getResult(saver));
            if(table[p.getX()][p.getY()].isCorrect()) {
                saver.setResult(v, val);
            }
            else {
                saver.setResult(v, "ERR");
            }
        }
    }

    private void update() {
        saver = new ResultsSaver();
        TreeSet<String> processing = new TreeSet<>(),
                processed = new TreeSet<>();
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                String address = getAddress(new Point(i, j));
                if(!processed.contains(address)) {
                    calculate(processing, processed, address);
                }
            }
        }

    }

    private Point getCoordinates(String address) {
        int x = 0, y = 0;
        String first = "";
        for(int i = 0; i < address.length(); i++) {
            char c = address.charAt(i);
            if(Character.isUpperCase(c)) {
                first += c;
            }
            else {
                x = Integer.parseInt(address.substring(i));
                break;
            }
        }

        for(int i = first.length() - 1, pw = 1; i >= 0; i--, pw *= 26) {
            int id = first.charAt(i) - 'A';
            y += id * pw;
        }

        return new Point(x, y);
    }

    private int binPow(int x, int y) {
        int result = 1;
        while(y > 0) {
            if(y % 2 == 1) result *= x;
            x *= x;
            y >>= 1;
        }
        return result;
    }

    public String getColumnName(int x) {
        int cnt = 0, len = 0, pw = 1;
        x++;
        while(cnt < x) {
            len++;
            pw *= 26;
            cnt += pw;
        }
        String result = "";
        for(int i = len - 1; i >= 0; i--) {
            for(int c = 25; c >= 0; c--) {
                int curNumber = c * binPow(26, i);
                if(curNumber < x) {
                    x -= curNumber;
                    result += (char)(c + 'A');
                    break;
                }
            }
        }
        return result;
    }

    private String getAddress(Point p) {
        int x = p.getX(), y = p.getY();
        return getColumnName(y) + String.valueOf(x);

    }

    private void modify(Point p, Expression expression) {
        saved = false;
        table[p.getX()][p.getY()] = expression;

        update();
    }

    private String getValue(Point p) { ;
        return table[p.getX()][p.getY()].getResult(saver);
    }

    public void consoleOutput() {
        for(int x = 0; x < N; x++) {
            for(int y = 0; y < N; y++) {
                System.out.print(getValue(new Point(x, y)) + " ");
            }
            System.out.println();
        }
    }


    private DefaultTableModel getValuesTableModel(){
        String[] columnNames = new String[N];
        for(int i = 0; i < N; i++) {
            columnNames[i] = getColumnName(i);
        }
        DefaultTableModel model = new ReadOnlyTableModel();
        model.setColumnIdentifiers(columnNames);
        for(int i = 0; i < N; i++) {
            String[] values = new String[N];
            for(int j = 0; j < N; j++) {
                values[j] = table[i][j].getResult(saver);
            }
            model.insertRow(i, values);
        }

        return model;
    }

    private DefaultTableModel getFormulasTableModel(){
        String[] columnNames = new String[N];
        for(int i = 0; i < N; i++) {
            columnNames[i] = getColumnName(i);
        }
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);

        for(int i = 0; i < N; i++) {
            String[] formulas = new String[N];
            for(int j = 0; j < N; j++) {
                formulas[j] = table[i][j].getFormula();
            }
            model.insertRow(i, formulas);
        }

        return model;
    }

    public void constructJTable(JTable jtable, JLabel fileNameLabel, JLabel modeLabel) {
        jtable.setModel(getValuesTableModel());


        jtable.setRowHeight(30);
        jtable.setColumnSelectionAllowed(true);
        jtable.setRowSelectionAllowed(true);


        for(int i = 0; i < N; i++) {
            jtable.getColumnModel().getColumn(i).setWidth(90);
        }
        setValuesMode(jtable, fileNameLabel, modeLabel);
    }

    private String cut(String s) {
        return s.substring(1, s.length() - 1);
    }

    public void save(JTable jtable, JLabel fileNameLabel) {
        System.out.println(jtable.getWidth() + " " + jtable.getHeight());
        if(saved) return;

        if(mode == 1) {
            StringBuilder fileText = new StringBuilder();
            for(int x = 0; x < N; x++) {
                for(int y = 0; y < N; y++) {
                    String value = jtable.getValueAt(x, y).toString();
                    value = value.substring(1, value.length() - 1);
                    modify(new Point(x, y), new Expression(value));
                    fileText.append(cut(table[x][y].getFormula()));
                    fileText.append("\n");
                }
            }

            try {
                File file = new File(path);
                FileOutputStream outputStream = new FileOutputStream(file, false);

                byte[] myBytes = fileText.toString().getBytes();
                try {
                    outputStream.write(myBytes);
                    outputStream.close();
                }
                catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Some error occurred while saving file.");
                }
                saved = true;
            }
            catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "File you are editing now is not found.");
            }

            saved = true;
            fileNameLabel.setText(path);
        }
    }

    public void setFormulasMode(JTable jtable, JLabel fileNameLabel, JLabel modeLabel) {
        if(mode == 1) {
            int answer = JOptionPane.showConfirmDialog(null, "This file is not saved, do you want ot reload it?",
                    "Warning", JOptionPane.YES_NO_OPTION);
            if(answer == JOptionPane.NO_OPTION) {
                return;
            }
        }
        mode = 1;
        jtable.setModel(getFormulasTableModel());
        jtable.setCellSelectionEnabled(true);
        saved = true;
        fileNameLabel.setText(path);
        modeLabel.setText("Formulas mode");
    }

    public void saveOrNot(JTable jtable, JLabel fileNameLabel) {
        if(!saved) {
            int answer = JOptionPane.showConfirmDialog(null, "This file is not saved, do you want ot save it?",
                    "Warning", JOptionPane.YES_NO_OPTION);
            if(answer == JOptionPane.YES_OPTION) {
                save(jtable, fileNameLabel);
            }
        }

    }

    public void setValuesMode(JTable jtable, JLabel fileNameLabel, JLabel modeLabel) {
        saveOrNot(jtable, fileNameLabel);
        mode = 0;
        jtable.setModel(getValuesTableModel());
        jtable.setCellSelectionEnabled(false);
        saved = true;
        fileNameLabel.setText(path);
        modeLabel.setText("Values mode(cannot edit)");
    }

    public void setEdited(JLabel fileNameLabel) {
        if(mode == 0) return;
        saved = false;
        if(fileNameLabel.getText().charAt(0) != '*') {
            fileNameLabel.setText("*" + fileNameLabel.getText());
        }
    }


    public boolean isSaved() {
        return saved;
    }

    public void delRow(JTable jtable, JLabel fileNameLabel, int x) {
        save(jtable, fileNameLabel);
        for(int i = x; i + 1 < N; i++) {
            for(int j = 0; j < N; j++) {
                table[i][j] = table[i + 1][j];
            }
        }
        for(int j = 0; j < N; j++) {
            table[N - 1][j] = new Expression("0");
        }

        update();

        jtable.setModel(getFormulasTableModel());
        setEdited(fileNameLabel);
    }

    public void delRowOrNot(JTable jtable, JLabel fileNameLabel) {
        int x = jtable.getSelectedRow();
        if(0 <= x && x < N) {
            int answer = JOptionPane.showConfirmDialog(null, "Do you really want to delete " + String.valueOf(x) + "th row?",
                    "Warning", JOptionPane.YES_NO_OPTION);
            if(answer == JOptionPane.YES_OPTION) {
                delRow(jtable, fileNameLabel, x);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Please select exactly ONE row.");
        }
    }

    public void delColumn(JTable jtable, JLabel fileNameLabel, int y) {
        save(jtable, fileNameLabel);
        for(int i = y; i + 1 < N; i++) {
            for(int j = 0; j < N; j++) {
                table[j][i] = table[j][i + 1];
            }
        }
        for(int j = 0; j < N; j++) {
            table[j][N - 1] = new Expression("0");
        }
        update();
        jtable.setModel(getFormulasTableModel());
        setEdited(fileNameLabel);
    }

    public void delColumnOrNot(JTable jtable, JLabel fileNameLabel) {
        int y = jtable.getSelectedColumn();
        if(0 <= y && y < N) {
            int answer = JOptionPane.showConfirmDialog(null, "Do you really want to delete the column " + getColumnName(y) +" ?",
                    "Warning", JOptionPane.YES_NO_OPTION);
            if(answer == JOptionPane.YES_OPTION) {
                delColumn(jtable, fileNameLabel, y);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Please select exactly ONE column.");
        }
    }
    public void addRow(JTable jtable, JLabel fileNameLabel, int x) {
        save(jtable, fileNameLabel);
        for(int i = N - 1; i >= Math.max(1, x); i--) {
            for(int j = 0; j < N; j++) {
                table[i][j] = table[i - 1][j];
            }
        }
        for(int j = 0; j < N; j++) {
            table[x][j] = new Expression("0");
        }

        update();

        jtable.setModel(getFormulasTableModel());
        setEdited(fileNameLabel);
    }

    public void addRowOrNot(JTable jtable, JLabel fileNameLabel) {
        int x = jtable.getSelectedRow();
        if(0 <= x && x < N) {
            int answer = JOptionPane.showConfirmDialog(null, "Do you really want to add a row before " + String.valueOf(x) + "th row?",
                    "Warning", JOptionPane.YES_NO_OPTION);
            if(answer == JOptionPane.YES_OPTION) {
                addRow(jtable, fileNameLabel, x);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Please select exactly ONE row.");
        }
    }

    public void addColumn(JTable jtable, JLabel fileNameLabel, int y) {
        save(jtable, fileNameLabel);
        for(int i = N - 1; i >= Math.max(1, y); i--) {
            for(int j = 0; j < N; j++) {
                table[j][i] = table[j][i - 1];
            }
        }
        for(int j = 0; j < N; j++) {
            table[j][y] = new Expression("0");
        }
        update();
        jtable.setModel(getFormulasTableModel());
        setEdited(fileNameLabel);
    }

    public void addColumnOrNot(JTable jtable, JLabel fileNameLabel) {
        int y = jtable.getSelectedColumn();
        if(0 <= y && y < N) {
            int answer = JOptionPane.showConfirmDialog(null, "Do you really want to add a column before the column " + getColumnName(y) +" ?",
                    "Warning", JOptionPane.YES_NO_OPTION);
            if(answer == JOptionPane.YES_OPTION) {
                addColumn(jtable, fileNameLabel, y);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Please select exactly ONE column.");
        }
    }
}
