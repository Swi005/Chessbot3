package Pieces;

import Chessbot3.GameBoard.Board;

import static Pieces.WhiteBlack.BLACK;
import static Pieces.WhiteBlack.WHITE;

public class PieceFactory 
{
    public static iPiece getPiece(char bokstav) {
        //Tar inn en bokstav, spawner en ny tilsvarende brikke, og returnerer brikken.
        iPiece pie;
        switch(bokstav){
            case 'p':
                pie = new Pawn(BLACK);
                break;
            case 'P':
                pie = new Pawn(WHITE);
                break;
            case 'r':
                pie = new Rook(BLACK);
                break;
            case 'R':
                pie = new Rook(WHITE);
                break;
            case 'n':
                pie = new Knight(BLACK);
                break;
            case 'N':
                pie = new Knight(WHITE);
                break;
            case 'b':
                pie = new Bishop(BLACK);
                break;
            case 'B':
                pie = new Bishop(WHITE);
                break;
            case 'q':
                pie = new Queen(BLACK);
                break;
            case 'Q':
                pie = new Queen(WHITE);
                break;
            case 'k':
                pie = new King(BLACK);
                break;
            case 'K':
                pie = new King(WHITE);
                break;
            case '.':
                pie = null;
                break;
            default:
                pie = null;
        }
        return pie;
    }
}
