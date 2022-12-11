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

The standard algebraic notation is used to record each move. Peaceful moves are denoted by the coordinate of its target square (e.g. the move a2-a4 can be denoted as a4). Captures are denoted by an x, and the starting file of the capturing pawn is always included (e.g. b5xc6 is denoted as bxc6).

## The Tournament Rules

1. Rounds and Scoring - The tournament will consist of several rounds, in which players let their AIs compete with each other. Each round is played as best-of-five games, with only the winner advancing to the next round. In the five games played, each win counts as 2 points, while a draw/stalemate counts as 1 point for both players.
2. The colour of players in the first match of any pairing will be determined randomly. Players swap colours after each game.
3. The auto runner will be used to play off two students AIs against each other. The specific machine that playes the game will be a fast machine with many CPU cores (up to 20).
4. In each game, the player whose AI plays black may determine where the both of the pawn gaps are (since white has the starting advantage). A player may not choose the same setup (or its mirroring) twice within the same round, althrough the other player may wishe to chosse the same setup when it is his/her turn to choose. Each player may choose at most one game per round in which the gaps are directly opposite each other.
5. If a program outputs an invalid move, or refuses to accept a valid move played by the other player, the game in progress will be counted as a loss.
6. All programs should output a move in less than 5 seconds. If it takes longer to return a move, the automarker will forfeit the game on that player's behalf.
7. The top-ranked players will sit down in their pairings and log in to adjacent lab machines, they will clone their gitlab repo, and they will be verbally communicating moves made by their AIs to each other. Your AI assumes your colour, and moves of the other player need to be entered manually.
8. Any code from the Kotlin standard library (including the standard Java libraries) may be used, but any other external code (especially AI or chess libraries) is not allowed to be used.
9. You will participate with the GitLab commit that you have submitted before the deadline. Any late submissions cannot participate in this tournament.
10. The code submitted by the Top 4 winners will be inspected after the end of the tournament and you may be invited to explain how it works.
11. You may use up to 1 MB of pre-computed data.

## Implementation of AI

Since no third-party library is allowed, the idea of implementing a neutral network is not attractive any more as that requires to write a linear algebra library purely by hand, and the training and running procedure cannot utlize any GPU acceleration. This ultemately makes the depths and number of nodes in the neutral network very limited, which results in a much poorer performance comparing to a hand-coded evaludation function. There is also no need to implement a policy network since the max number of brancing factor is merely 14 at the beginning of the game, and it then decreases as pieces get captured or move into a stalement. So a normal evaluation function plus a tree search would be sufficient to achieve a decent result under those tournament rules. In fact, there is a [NN implementation](https://github.com/JordanLloydHall/pawnRace) from preious tournament and as expected, it loses 5-0 against my implementation.

My AI uses the [negamax algorithm](https://en.wikipedia.org/wiki/Negamax) to search the game tree and make moves. It also applies the alpha-beta pruning and transposition table to make the algorithm more efficient. A simple and fast evaluation function is used to make the search efficient. The iterative deepening framework is also used to keep the AI searching until it reaches the time limit. Upon that, a Java thread pool is used to parallized the search and attempt to utlize as many threads as possible. It tooks some time to get the concurrency right but the performance gain pays off.

I attempted to cache the data of the transposition table to dist, but it exceeds several hundred megabytes very quickly, making the caching not very feasible in the tournament. 

This particular implementation is far from perfect, a simple improvement is to use a bitboard implementattion to accelerate the board access. I have set up foundation for the bitboard representation, but it is only used when comparing equality inside the hash map. More board accessing and manipulation functions needs to be added to make it more useful.

The AI performs particularly well in the mid-late game, as it is performent enough to find a winning strategy. But it's openning is still a bit weak, due to the relatively simple evaluation function, its move is not always optimal.
