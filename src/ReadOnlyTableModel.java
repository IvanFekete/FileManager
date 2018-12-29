import javax.swing.table.DefaultTableModel;

public class ReadOnlyTableModel extends DefaultTableModel {
    public boolean isCellEditable(int i,int j) {
        return false;
    }
}
