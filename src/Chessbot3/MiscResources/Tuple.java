package Chessbot3.MiscResources;

public class Tuple<X, Y> {
    /**
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

    @Override
    public boolean equals(Object obj){
        if(! (obj instanceof Tuple)) return false;
        Tuple<X, Y> other = (Tuple) obj;
        return (this.x.equals(other.getX()) && this.y.equals(other.getY()));
    }
}
