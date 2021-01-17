package Chessbot3.Multiplayer;

public class Tuple<X, Y> {
    /*
    Definerer Tupler, som fra nå av er mutable. "Reality can be whatever I want" - Thanos
     */
    private X x;
    private Y y;
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
    public X getX() { return this.x; }
    public Y getY() { return this.y; }

    public void setX(Object x) { this.x = (X) x; }
    public void setY(Object y) { this.y = (Y) y; }

    public Tuple<X, Y> copy() { return new Tuple(getX(), getY()); }

    public String toString(){
        return "(" + this.x.toString() + ", " + this.y.toString() + ")";
    }
    public boolean equals(Tuple obj){ return (this.x == obj.getX() && this.y == obj.getY()); }
}
