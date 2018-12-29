public class Tag {
    String name;
    Boolean needClosing;

    public Tag(String name, Boolean needClosing) {
        this.name = name;
        this.needClosing = needClosing;
    }

    public String getName() {
        return this.name;
    }

    public Boolean isClosing() {
        return this.needClosing;
    }

    @Override
    public String toString() {
        return needClosing ? "<" + name + "></" + name + ">" : "<" + name + " />";
    }

}
