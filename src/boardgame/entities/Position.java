package boardgame.entities;

public class Position {
    
    private Integer row;
    private Integer column;
    
    public Position() {
    }

    public Position(Integer row, Integer column) {
        this.row = row;
        this.column = column;
    }

    public Integer getRow() {
        return row;
    }
    public Integer getColumn() {
        return column;
    }

    public void setValues(Integer row, Integer column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Position) {
            Position other = (Position) obj;
            if (row.equals(other.getRow()) && column.equals(other.getColumn())) return true;
        }
        return false;
    }

}
