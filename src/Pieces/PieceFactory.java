package Pieces;

import Chessbot3.GameBoard.Board;

import static Pieces.WhiteBlack.BLACK;
import static Pieces.WhiteBlack.WHITE;

public class PieceFactory 
{
    public static iPiece getPiece(char bokstav, Board b) {
        iPiece pie;
        switch(bokstav){
            case 'p':
                pie = new Pawn(BLACK, b);
                break;
            case 'P':
                pie = new Pawn(WHITE, b);
                break;
            case 'r':
                pie = new Rook(BLACK, b);
                break;
            case 'R':
                pie = new Rook(WHITE, b);
                break;
            case 'n':
                pie = new Knight(BLACK, b);
                break;
            case 'N':
                pie = new Knight(WHITE, b);
                break;
            case 'b':
                pie = new Bishop(BLACK, b);
                break;
            case 'B':
                pie = new Bishop(WHITE, b);
                break;
            case 'q':
                pie = new Queen(BLACK, b);
                break;
            case 'Q':
                pie = new Queen(WHITE, b);
                break;
            case 'k':
                pie = new King(BLACK, b);
                break;
            case 'K':
                pie = new King(WHITE, b);
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
