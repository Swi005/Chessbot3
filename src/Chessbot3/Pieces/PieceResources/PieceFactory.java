package Chessbot3.Pieces.PieceResources;

import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.Types.*;

import static Chessbot3.Pieces.PieceResources.WhiteBlack.BLACK;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class PieceFactory 
{
    public static iPiece getPiece(char bokstav, Tuple pos) {
        //Tar inn en bokstav, spawner en ny tilsvarende brikke, og returnerer brikken.
        iPiece pie;
        switch(bokstav){
            case 'p':
                pie = new Pawn(BLACK, pos);
                break;
            case 'P':
                pie = new Pawn(WHITE, pos);
                break;
            case 'r':
                pie = new Rook(BLACK, pos);
                break;
            case 'R':
                pie = new Rook(WHITE, pos);
                break;
            case 'n':
                pie = new Knight(BLACK, pos);
                break;
            case 'N':
                pie = new Knight(WHITE, pos);
                break;
            case 'b':
                pie = new Bishop(BLACK, pos);
                break;
            case 'B':
                pie = new Bishop(WHITE, pos);
                break;
            case 'q':
                pie = new Queen(BLACK, pos);
                break;
            case 'Q':
                pie = new Queen(WHITE, pos);
                break;
            case 'k':
                pie = new King(BLACK, pos);
                break;
            case 'K':
                pie = new King(WHITE, pos);
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
