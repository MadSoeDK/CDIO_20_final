package Model.Board;

/**
 * Parent Model.Board.Field Class
 */
public abstract class Field {
    private final String NAME;
    private final int PLACEMENT;

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
