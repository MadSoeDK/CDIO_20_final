package Model.Board;

public class Tax extends Field {
    private final int TAX;

    public Tax(String name, int position, int tax) {
        super(name, position);
        this.TAX = tax;
    }

    public int getTax() {
        return TAX;
    }

}
