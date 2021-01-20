package Chessbot3.MiscResources;;

public class Move implements Comparable
{
    //Et objekt for å representere et trekk.
    //Objektet holder selv styr på hvor den skal flytte, samt 'vekten' og 'stabiliteten' dens.
    //Positiv vekt er et bra trekk for hvit, negativ vekt er bra for svart.
    //stabilityIndex=false betyr at trekket kan forårsake at verdifulle brikker (fra begge sider) blir drept, og må søkes lengre.

    //Hjelpestrenger, for å kunne parse det brukeren skriver inn til trekk.
    private static String chars = "abcdefgh";
    private static String nums = "12345678";


    private Tuple<Integer, Integer> from;
    private Tuple<Integer, Integer> to;
    private boolean stabilityIndex;
    private int weight;
    public Move(Tuple<Integer, Integer> from , Tuple<Integer, Integer> to) {
        this.from = from;
        this.to = to;
        this.weight = 0;
        this.stabilityIndex = false; //Default er at alle trekk er ustabile.
    }

    public Move(String input){
        //Tar en streng fra brukeren og oversetter det til et trekk.
        //f. eks 'e2 e4' blir til new Move((4, 6) (4, 4)).
        input = input.replace(" ", "").toLowerCase();
        if (!isAMove(input)) throw new IllegalArgumentException("Could not parse.");
        from = new Tuple<>(chars.indexOf(input.charAt(0)), 7 - nums.indexOf(input.charAt(1)));
        to = new Tuple<>(chars.indexOf(input.charAt(2)), 7 - nums.indexOf(input.charAt(3)));
    }

    public Move(Tuple<Integer, Integer> from , Tuple<Integer, Integer> to, boolean stabIndex, int weight) {
        this.from = from;
        this.to = to;
        this.stabilityIndex = stabIndex;
        this.weight = weight;
    }

    public static boolean isAMove(String input) {
        //Sjekker om det brukeren skrev kan tolkes som et trekk eller ikke.
        //Standard sjakknotasjon (som Nf3) fungerer ikke, man må skrive to koordinater etter hverandre (som g1f3).
        //f. eks 'e2 e4' returnerer true, 'tcfvyguy76ftviyubv7ughbij' returnerer false.
        input = input.replace(" ", "").toLowerCase();
        if(input.length() < 4) return false;
        return (chars.contains(input.substring(0, 1)) && nums.contains(input.substring(1, 2))
                && chars.contains(input.substring(2, 3)) && nums.contains(input.substring(3, 4)));
    }

    public Tuple<Integer, Integer> getFrom() { return this.from; }
    public Tuple<Integer, Integer> getTo() { return this.to; }
    public int getWeight() {return this.weight; }
    public boolean getStabIndex() { return this.stabilityIndex; }

    public void setFrom(Tuple<Integer, Integer> from) { this.from = from; }
    public void setTo(Tuple<Integer, Integer> to) { this.to = to; }
    public void addWeight(int n) {this.weight += n; }
    public void setWeight(int n) { this.weight = n; }
    public void setStabIndex(boolean set) { this.stabilityIndex = set; }

    public boolean equals(Move obj) {
        return (this.getFrom().getX().equals(obj.getFrom().getX()) && this.getFrom().getY().equals(obj.getFrom().getY())
                && this.getTo().getX().equals(obj.getTo().getX()) && this.getTo().getY().equals(obj.getTo().getY()));
    }
    @Override
    public int compareTo(Object m){
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
        char rekkefra = chars.charAt(from.getX());
        int radfra = 8-from.getY();
        char rekketil = chars.charAt(to.getX());
        int radtil = 8-to.getY();
        return ("" + rekkefra + radfra + ":" + rekketil + radtil).toUpperCase();
    }
}