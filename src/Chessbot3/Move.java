package Chessbot3;

public class Move implements Comparable
{
    private Tuple<Integer, Integer> from;
    private Tuple<Integer, Integer> to;
    private boolean stabilityIndex;
    private int weight;

    public Move(Tuple<Integer, Integer> from , Tuple<Integer, Integer> to)
    {
        this.from = from;
        this.to = to;
        this.weight = 0;
        this.stabilityIndex = false;
    }
    public Move(Tuple<Integer, Integer> from , Tuple<Integer, Integer> to, boolean stabIndex, int weight)
    {
        this.from = from;
        this.to = to;
        this.stabilityIndex = stabIndex;
        this.weight = weight;
    }
    public Tuple<Integer, Integer> getX() { return this.from; }
    public Tuple<Integer, Integer> getY() { return this.to; }
    public int getWeight() {return this.weight; }
    public boolean getStabIndex() { return this.stabilityIndex; }

    public void setX(Tuple<Integer, Integer> from) { this.from = from; }
    public void setY(Tuple<Integer, Integer> to) { this.to = to; }
    public void addWeight(int n) {this.weight += n; }
    public void setStabIndex(boolean set) { this.stabilityIndex = set; }

    public String toString() {return "(" + this.from + ", " + this.to + ")"; }

    public boolean equals(Move obj) {
        return (this.from == obj.getX() && this.to == obj.getY());
    }
    @Override
    public int compareTo(Object m) 
    {
        Move i = (Move)m;
        Integer foo = this.weight;
        Integer bar = i.weight;
        return foo.compareTo(bar);
    }

}