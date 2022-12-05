package pawnrace

/**
 * PEACEFUL: when no capture is involved
 * CAPTURE: when a regular capture happens
 * EN_PASSANT: when there is a capture following the En Passant rule
 */
enum class MoveType {
  PEACEFUL, CAPTURE, EN_PASSANT
}
