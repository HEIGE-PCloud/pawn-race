package pawnrace

import java.io.PrintWriter
import java.io.InputStreamReader
import java.io.BufferedReader
import java.util.concurrent.Executors

// You should not add any more member values or member functions to this class
// (or change its name!). The auto-runner will load it in via reflection, and it
// will be safer for you to just call your code from within the playGame member
// function, without any unexpected surprises!
class PawnRace {
  // Don't edit the type or the name of this method
  // The colour can take one of two values: 'W' or 'B', this indicates your player colour
  fun playGame(colour: Char, output: PrintWriter, input: BufferedReader) {
    // init players
    val threadCount = Runtime.getRuntime().availableProcessors() / 2 - 1
    val availableThreadCount = threadCount - Thread.activeCount()
    val executor = Executors.newFixedThreadPool(availableThreadCount)
    println("[INFO] Available thread count $availableThreadCount}")
    val me = Player(Piece(colour))
    val opponent = Player(Piece(colour).opposite())
    me.opponent = opponent
    opponent.opponent = me
    println("[INFO] Players initialized, my colour is $colour")
    // You should call your code from within here
    // Step 1: If you are the black player, you should send a string containing the gaps
    // It should be of the form "wb" with the white gap first and then the black gap: i.e. "AH"
    // Send gaps with output.println()

    if (me.piece == Piece.BLACK) {
      // TODO: Investigate openings and read it from a file
      output.println("AH")
      println("[INFO] I suggest opening ${"AH"}")
    }

    // Regardless of your colour, you should now receive the gaps verified by the auto-runner
    // (or by the human if you are using your own main function below), these are provided
    // in the same form as above ("wb"), for example: "AH"
    // receive the confirmed gaps with input.readLine()
    val gaps = input.readLine()
    println("[INFO] Actual opening gaps $gaps")
    // Now you may construct your initial board
    // Initialise the board state
    val board = Board(File(gaps[0]), File(gaps[1]))
    var game = Game(board, me)
    println("[INFO] Game initialized")

    // If you are the white player, you are now allowed to move
    // you may send your move, once you have decided what it will be, with output.println(move)
    // for example: output.println("axb4")
    // White player should decide what move to make and send it
    if (me.piece == Piece.WHITE) {
      val move = me.makeMove(game, executor)
      game = game.applyMove(move)
      output.println(move)
      println("[INFO] My move is $move")
    }
    // After point, you may create a loop which waits to receive the other players move
    // (via input.readLine()), updates the state, checks for game over and, if not, decides
    // on a new move and again send that with output.println(move). You should check if the
    // game is over after every move.
    /* Create the "game loop", which:
          * gets the opponents move
          * updates board
          * checks game over, if not then
          * choose a move
          * send this move
          * update the state
          * check game over
          * rinse, and repeat.
    */
    while (true) {
      val opponentMoveString: String = input.readLine()
      val opponentMove = game.parseMove(opponent.piece, opponentMoveString)
      game = game.applyMove(opponentMove!!)
      if (game.over()) break
      val move: Move = me.makeMove(game, executor)
      output.println(move)
      game = game.applyMove(move)
      println("[INFO] My move is $move")
      if (game.over()) break
    }

    // Once the loop is over, the game has finished and you may wish to print who has won
    // If your advanced AI has used any files, make sure you close them now!
    // tidy up resources, if any
    executor.shutdownNow()
  }
}

// When running the command, provide an argument either W or B, this indicates your player colour
fun main(args: Array<String>) {
  PawnRace().playGame(args[0][0], PrintWriter(System.out, true), BufferedReader(InputStreamReader(System.`in`)))
}
