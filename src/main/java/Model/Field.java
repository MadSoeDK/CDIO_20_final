package Model;

/**
 * Parent Model.Field Class
 */
public abstract class Field {
    private final String NAME;
    private final int PLACEMENT;

    //protected String description;

    public Field(String name,int placement) {
        this.NAME = name;
        this.PLACEMENT = placement;
    }

    public String getName() {
        return NAME;
    }
    public int getPlacement() {
        return PLACEMENT;
    }
}
