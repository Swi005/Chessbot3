package Chessbot3.PGNConverter;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.PieceResources.WhiteBlack;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

    public class Converter {

        public static Move convertToMove(String str)
        {
            Board board = new Board();
            String chars = "abcdefgh";
            String badstuff = "x+#*";
            boolean extraInfo = false;
            Tuple<Integer, Integer> partialInfo = null;
            char[] temp = str.toCharArray();
            str = "";

            for(int i = 0; i < temp.length; ++i)
            {
                char c = temp[i];
                if (badstuff.indexOf(c) == -1)
                {
                    str = str + Character.toString(c);
                }
            }

            char fst = str.charAt(0);
            String endofmove = "";
            if (str.equals("O-O")) //Kingside casteling
            {
                Move mv;
                if(board.getColorToMove() == WhiteBlack.WHITE)//Check if mover is white
                {
                    mv = new Move(new Tuple<Integer, Integer>(4,7), new Tuple<Integer, Integer>(6,7));

                }
                else{//Else is black
                    mv = new Move(new Tuple<Integer, Integer>(4,0), new Tuple<Integer, Integer>(6,0));
                }
                board.movePiece(mv);
                return mv;
            }
            else if (str.equals("O-O-O")) //Queenside casteling
            {
                Move mv;
                if(board.getColorToMove() == WhiteBlack.WHITE)//Check if mover is white
                {
                    mv = new Move(new Tuple<Integer, Integer>(4,8), new Tuple<Integer, Integer>(6,8));

                }
                else{//Else is black
                    mv = new Move(new Tuple<Integer, Integer>(4,1), new Tuple<Integer, Integer>(2,1));
                }
                board.movePiece(mv);
                return mv;
            }
            else
            {
                if (str == "Be7") {
                    System.out.println("test");
                }
                if (Character.isUpperCase(fst)) {
                    endofmove = str.substring(1);
                } else {
                    if (Character.isDigit(fst)) {
                        return null;
                    }

                    endofmove = str;
                    fst = 'P';
                }

                Tuple endPos;
                if (endofmove.length() > 2) {
                    extraInfo = true;
                    if (endofmove.length() == 4) {
                        endPos = new Tuple(chars.indexOf(endofmove.charAt(0)), 8 - chars.indexOf(endofmove.charAt(1)));
                        Tuple<Integer, Integer> end = new Tuple(chars.indexOf(endofmove.charAt(2)), 8 - chars.indexOf(endofmove.charAt(3)));
                        return new Move(endPos, end);
                    }

                    if (endofmove.length() == 3) {
                        if (Character.isDigit(endofmove.charAt(0))) {
                            partialInfo = new Tuple(-1, 8 - chars.indexOf(endofmove.charAt(0)));
                        } else {
                            partialInfo = new Tuple(chars.indexOf(endofmove.charAt(0)), -1);
                        }

                        endofmove = endofmove.substring(1);
                    }
                }

                endPos = new Tuple(chars.indexOf(endofmove.charAt(0)), 8 - Integer.parseInt(endofmove.substring(1)));
                List<Move> posMoves = board.genCompletelyLegalMoves(board.getColorToMove());
                posMoves = pruneMoves(posMoves, endPos);

                for (Move m: posMoves)
                {
                    //Check that its the correct pice
                    if(fst == Character.toUpperCase(board.getPiece((Integer) m.getX().getX(), (Integer) m.getX().getY()).getSymbol()))
                    {
                        if (extraInfo && (Integer) partialInfo.getX() == -1)//Check if extra id info exists
                        {
                            if (m.getX().getY() == partialInfo.getY())
                            {
                                board.movePiece(m);
                                return m;
                            }

                            if ((Integer) partialInfo.getY() == -1 && m.getX().getX() == partialInfo.getX())
                            {
                                board.movePiece(m);
                                return m;
                            }
                        }

                        if(m.getY().equals(endPos))
                        {
                            board.movePiece(m);
                            return m;
                        }
                    }
                }
            }
            return null;
        }

        static List<Move> pruneMoves(List<Move> list, Tuple<Integer, Integer> endPos) {
            List<Move> retList = new ArrayList();
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
                Move m = (Move)var3.next();
                if (m.getY().equals(endPos)) {
                    retList.add(m);
                }
            }

            return retList;
        }

        public static void main(String[] args) {
            String[] var1 = Parser.parseMoves(new File("C:\\Users\\Sande\\Documents\\INF101\\Chessbot3\\src\\Chessbot3\\files\\test.pgn"));
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                String s = var1[var3];
                Move move = convertToMove(s);
                if (move != null) {
                    System.out.println(move.toAlgebraicNotation());
                } else {
                    System.out.println("oh-oh- something went wrong!");
                }
            }

        }
    }

