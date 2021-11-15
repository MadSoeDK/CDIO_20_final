import gui_fields.GUI_Street;

public class Property extends Field {
    //protected String color;
    protected GUI_Street field;
    protected int rent;
    protected Player owner;

    public Property(GUI_Street field, int rent, Player owner, String name, int fieldType) {
        this.field = field;
        this.rent = rent;
        this.owner = owner;
        this.name = name;
        this.fieldType = fieldType;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getType() {
        return fieldType;
    }
    public int getRent() {
        return rent;
    }
    public void setOwner(Player player) {
        this.owner = player;
    }
    public Player getOwner() {
        return owner;
    }


}
