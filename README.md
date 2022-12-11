# Pawn Race

## Board and setup

Pawn races are played on a normal chess board, with 8x8 squares. Rows are commonly referred to as *ranks*, and are labelled 1-8, while columns are referred to as *files*, labelled A-H. From white's perspective, the square in the bottom left corner would thus be referred to as a1, while the bottom right corner is h1. White's pawns are all placed on the second rank initially, while black starts from the seventh rank. The following picture shows an example of an initial setup, in which the gaps were chosen on the H and A files, for white and black respectively.

<img width="224" alt="image" src="https://user-images.githubusercontent.com/52968553/206916724-9882d17a-68f1-415d-a4ca-6b08a87d8765.png">

## Pawns and pawn moves

Here are the rules for pawns' moves:

1. A pawn can move straight forward by 1 square if the targeted square is empty.
2. A pawn can move straight forward by 2 squares if it is in its starting position, and both the target square and the passed-through square are empty.
3. A pawn can move diagonally forward by 1 square, if and only if that square is occupied by an opposite-coloured pawn. This constitutes a capture, and the captured pawn is taken off the board.
4. Combining the previous two rules, if a pawn has moved forward by 2 squares in the last move played, it may be captured on the square that it passed through. This special type of capture is capture in passing and commonly referred to as the *En Passant rule*. A pawn can only be captured en passant immediately after it moved forward two squares, but not at any later stage in the game.

## Algebraic chess notation

The standard algebraic notation is used to record each move. Peaceful moves are denoted by the coordinate of its target square (e.g. the move `a2-a4` can be denoted as `a4`). Captures are denoted by an `x`, and the starting file of the capturing pawn is always included (e.g. `b5xc6` is denoted as `bxc6`).

# AI

The AI uses the negamax algorithm to search the game tree and make moves. It also applies the alpha-beta pruning and transposition table to make the algorithm more efficient. A simple and fast evaluation function is used to make the search efficient. I also use the iterative deepening technique to keep the AI searching until it reaches the time limit. 
