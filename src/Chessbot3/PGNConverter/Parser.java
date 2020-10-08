package Chessbot3.PGNConverter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
    public Parser() {
    }

    static String[] parseMoves(File file) {
        List<String> moves = new ArrayList();
        String allMoves = "";

        try {
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.length() > 0 && line.charAt(0) != '[') {
                    allMoves = allMoves + line + " ";
                }
            }
        } catch (IOException var8) {
            System.out.println(var8.getMessage());
            var8.printStackTrace();
        }

        allMoves = allMoves.replaceAll("\\{([\\s\\S]*?)\\}", "");
        String[] subStrings = allMoves.split("([0-9]+[.]) |([\\s])");
        String[] var10 = subStrings;
        int var5 = subStrings.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String str = var10[var6];
            if (!str.equals("")) {
                moves.add(str);
            }
        }

        return moves.toArray(new String[0]);
    }

    public static void main(String[] args) {
        parseMoves(new File("C:\\Users\\Sande\\Documents\\INF101\\Chessbot3\\src\\Chessbot3\\files\\test.pgn"));
    }
}

