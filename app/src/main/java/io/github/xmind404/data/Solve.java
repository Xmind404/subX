package io.github.xmind404.data;

import io.github.xmind404.utils.Timer;

public class Solve {

  public enum Penalty { NONE, PLUS_TWO, DNF }

  private final String scramble;
  private final long rawNanos;
  private final long inspectionElapsedMs;
  private final boolean inspectionDnf;
  private final boolean inspectionPlusTwo;
  private Penalty manualPenalty = Penalty.NONE;

  public Solve(String scramble, long rawNanos, long inspectionElapsedMs) {
    this.scramble = scramble;
    this.rawNanos = rawNanos;
    this.inspectionElapsedMs = inspectionElapsedMs;
    this.inspectionDnf = inspectionElapsedMs >= 17000;
    this.inspectionPlusTwo = !inspectionDnf && inspectionElapsedMs >= 15000;
  }

  public static Solve dnf(String scramble, long inspectionElapsedMs) {
    Solve solve = new Solve(scramble, 0, inspectionElapsedMs);
    solve.manualPenalty = Penalty.DNF;
    return solve;
  }

  public static Solve manual(String scramble, long rawNanos) {
    return new Solve(scramble, rawNanos, 0);
  }

  public String getScramble() {
    return scramble;
  }

  public long getInspectionElapsedMs() {
    return inspectionElapsedMs;
  }

  public boolean isDnf() {
    return inspectionDnf || manualPenalty == Penalty.DNF;
  }

  public void togglePlusTwo() {
    manualPenalty = manualPenalty == Penalty.PLUS_TWO ? Penalty.NONE : Penalty.PLUS_TWO;
  }

  public void toggleDnf() {
    manualPenalty = manualPenalty == Penalty.DNF ? Penalty.NONE : Penalty.DNF;
  }

  public void clearPenalty() {
    manualPenalty = Penalty.NONE;
  }

  public long getFinalNanos() {
    long penaltyNanos = 0;
    if (inspectionPlusTwo) penaltyNanos += 2_000_000_000L;
    if (manualPenalty == Penalty.PLUS_TWO) penaltyNanos += 2_000_000_000L;
    return rawNanos + penaltyNanos;
  }

  public String display() {
    if (isDnf()) {
      return Colors.RED + Colors.BOLD + "DNF" + Colors.RESET;
    }
    String time = Timer.formatTime(getFinalNanos());
    int plusTwoCount = (inspectionPlusTwo ? 1 : 0) + (manualPenalty == Penalty.PLUS_TWO ? 1 : 0);
    if (plusTwoCount > 0) {
      return Colors.ORANGE + time + " (+" + (plusTwoCount * 2) + ")" + Colors.RESET;
    }
    return Colors.GREEN + time + Colors.RESET;
  }

  @Override
  public String toString() {
    return display() + "  [" + scramble + "]";
  }
}