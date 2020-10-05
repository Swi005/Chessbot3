package Chessbot3.MiscResources;

public class Move implements Comparable
{
    //Et objekt for å representere et trekk.
    //Objektet holde selv styr på hvor den skal flytte, samt 'vekten' og 'stabiliteten' dens.
    //Positiv vekt er et bra trekk, negativ vekt er et dårlig ett.
    //stabilityIndex=false betyr at trekket kan forårsake at verdifulle brikker (fra begge sider) blir drept, og må søkes lengre.

    private Tuple<Integer, Integer> from;
    private Tuple<Integer, Integer> to;
    private boolean stabilityIndex;
    private int weight;
    private String chars = "ABCDEFGH";
    private String chars2 = chars.toLowerCase();
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
    public void setWeight(int n) { this.weight = n; }
    public void setStabIndex(boolean set) { this.stabilityIndex = set; }

    public String toString2() {return "(" + this.from + ", " + this.to + ")"; }

    public boolean equals(Move obj) {
        return (this.getX().getX() == obj.getX().getX() && this.getX().getY() == obj.getX().getY() && this.getY().getX() == obj.getY().getX() && this.getY().getY() == obj.getY().getY());
    }
    @Override
    public int compareTo(Object m) 
    {
        Move i = (Move)m;
        Integer foo = this.weight;
        Integer bar = i.weight;
        return foo.compareTo(bar);
    }
    public String toString() {
        char rekkefra = chars.charAt(from.getX());
        int radfra = 8-from.getY();
        char rekketil = chars.charAt(to.getX());
        int radtil = 8-to.getY();
        return "(" + rekkefra+radfra + "-" + rekketil+radtil + ")";
    }

    public String toAlgebraicNotation() {
        char rekkefra = chars2.charAt(from.getX());
        int radfra = 8-from.getY();
        char rekketil = chars2.charAt(to.getX());
        int radtil = 8-to.getY();
        return  rekkefra + "" + radfra + ":" + rekketil + "" + radtil;
    }
}