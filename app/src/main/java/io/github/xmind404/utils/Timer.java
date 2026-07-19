package io.github.xmind404.utils;

import io.github.xmind404.data.Colors;
import io.github.xmind404.data.Solve;

public final class Timer {

  private static final int SPACE = 32;
  private static final int ESC = 27;
  private static final int ENTER = 13;
  private static final int LF = 10;

  private static final long INSPECTION_MS = 15000;
  private static final long INSPECTION_DNF_MS = 17000;
  private static final long HOLD_THRESHOLD_MS = 300;
  private static final long RELEASE_GAP_MS = 150;

  private Timer() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  public static Solve runSolve(String header, String scramble) {
    TerminalInput.drain();
    Renderer.frame(header, scramble, Colors.GRAY + "Press [Space] to start inspection" + Colors.RESET);
    waitForTap();

    long inspectionStart = System.nanoTime();
    boolean holding = false;
    boolean holdReady = false;
    long holdBeganAt = 0;
    long lastSpaceAt = 0;
    long inspectionElapsedAtStartMs = -1;

    while (true) {
      long elapsedMs = (System.nanoTime() - inspectionStart) / 1_000_000;

      if (!holding && elapsedMs >= INSPECTION_DNF_MS) {
        Renderer.frame(header, scramble, Colors.RED + Colors.BOLD + "DNF - inspection overtime" + Colors.RESET);
        return Solve.dnf(scramble, elapsedMs);
      }

      int key = TerminalInput.poll();
      long now = System.nanoTime();

      if (key == ESC) {
        RawTerminal.disable();
        System.out.println();
        System.exit(0);
      }

      if (key == SPACE) {
        if (!holding) {
          holding = true;
          holdBeganAt = now;
          holdReady = false;
        }
        lastSpaceAt = now;
      }

      if (holding) {
        long heldMs = (now - holdBeganAt) / 1_000_000;
        if (heldMs >= HOLD_THRESHOLD_MS) holdReady = true;

        long sinceLastSpaceMs = (now - lastSpaceAt) / 1_000_000;
        if (sinceLastSpaceMs > RELEASE_GAP_MS) {
          if (holdReady) {
            inspectionElapsedAtStartMs = elapsedMs;
            break;
          } else {
            holding = false;
            holdReady = false;
          }
        }
      }

      String timeColor = elapsedMs >= 12000 ? Colors.RED
          : elapsedMs >= 8000 ? Colors.ORANGE
          : Colors.CYAN;

      String holdText = "";
      if (holding) {
        holdText = holdReady
            ? "  " + Colors.GREEN + Colors.BOLD + "READY - release!" + Colors.RESET
            : "  " + Colors.RED + "hold..." + Colors.RESET;
      }

      double secondsLeft = Math.max(0, (INSPECTION_MS - elapsedMs) / 1000.0);
      Renderer.frame(header, scramble,
          timeColor + String.format("Inspection: %.1fs", secondsLeft) + Colors.RESET + holdText);

      sleepQuiet(20);
    }

    TerminalInput.drain();
    Renderer.frame(header, scramble, Colors.GREEN + Colors.BOLD + "GO! [Space] to stop" + Colors.RESET);
    long solveStart = System.nanoTime();
    waitForTap();
    long totalNanos = System.nanoTime() - solveStart;

    Solve solve = new Solve(scramble, totalNanos, inspectionElapsedAtStartMs);
    Renderer.frame(header, scramble, Colors.BOLD + "Time: " + Colors.RESET + solve.display());

    promptPenalty(header, scramble, solve);
    return solve;
  }

  private static void promptPenalty(String header, String scramble, Solve solve) {
    TerminalInput.drain();
    while (true) {
      Renderer.frame(header, scramble,
          "[2] +2   [3] DNF   [Space] continue   -> " + solve.display());
      int key = blockingKey();
      if (key == '2') {
        solve.togglePlusTwo();
      } else if (key == '3') {
        solve.toggleDnf();
      } else if (key == SPACE || key == ENTER || key == LF) {
        break;
      } else if (key == ESC) {
        RawTerminal.disable();
        System.out.println();
        System.exit(0);
      }
    }
  }

  private static void waitForTap() {
    int key = blockingKey();
    if (key == ESC) {
      RawTerminal.disable();
      System.out.println();
      System.exit(0);
    }
  }

  private static int blockingKey() {
    return TerminalInput.blocking();
  }

  private static void sleepQuiet(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public static String formatTime(long nanos) {
    long totalMs = nanos / 1_000_000;
    long minutes = totalMs / 60000;
    long seconds = (totalMs % 60000) / 1000;
    long centis = (totalMs % 1000) / 10;

    if (minutes > 0) {
      return String.format("%d:%02d.%02d", minutes, seconds, centis);
    }
    return String.format("%d.%02d", seconds, centis);
  }
}