import javax.swing.*;

public class CreationDialog extends JDialog {
    private String dir;
    protected UtilController controller;

    public String getDir() {
        return this.dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setController(UtilController controller) {
        this.controller = controller;
    }


}
