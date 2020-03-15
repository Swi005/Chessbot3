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
                b.addPiece(pie, BLACK);
                break;
            case 'P':
                pie = new Pawn(WHITE, b);
                b.addPiece(pie, WHITE);
                break;
            case 'r':
                pie = new Rook(BLACK, b);
                b.addPiece(pie, BLACK);
                break;
            case 'R':
                pie = new Rook(WHITE, b);
                b.addPiece(pie, WHITE);
                break;
            case 'n':
                pie = new Knight(BLACK, b);
                b.addPiece(pie, BLACK);
                break;
            case 'N':
                pie = new Knight(WHITE, b);
                b.addPiece(pie, WHITE);
                break;
            case 'b':
                pie = new Bishop(BLACK, b);
                b.addPiece(pie, BLACK);
                break;
            case 'B':
                pie = new Bishop(WHITE, b);
                b.addPiece(pie, WHITE);
                break;
            case 'q':
                pie = new Queen(BLACK, b);
                b.addPiece(pie, BLACK);
                break;
            case 'Q':
                pie = new Queen(WHITE, b);
                b.addPiece(pie, WHITE);
                break;
            case 'k':
                pie = new King(BLACK, b);
                b.addPiece(pie, BLACK);
                break;
            case 'K':
                pie = new King(WHITE, b);
                b.addPiece(pie, WHITE);
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
