package io.github.xmind404.utils;

import io.github.xmind404.data.Colors;
import io.github.xmind404.data.Rubik.RubikTypes;
import io.github.xmind404.data.Solve;

public final class Timer {

  private static final int SPACE = 32;
  private static final int ESC = 27;
  private static final int ENTER = 13;
  private static final int LF = 10;
  private static final int BACKSPACE = 127;
  private static final int BACKSPACE_ALT = 8;

  private static final long INSPECTION_MS = 15000;
  private static final long INSPECTION_DNF_MS = 17000;
  private static final long HOLD_THRESHOLD_MS = 300;
  private static final long RELEASE_GAP_MS = 150;

  private Timer() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  public static Solve runSolve(SolveSession session, String initialScramble) {
    String scramble = initialScramble;
    String status = Colors.GRAY + "[Space] start   [I] enter time   [D] delete last   [H] history   [S] settings" + Colors.RESET;
    double[] rot = {0, 0};

    while (true) {
      String cube = Ascii.cubeFrame(rot[0], rot[1]);
      rot[0] += 0.15;
      rot[1] += 0.09;

      Renderer.frame(cube, session.buildHeader(), scramble, status);

      int key = TerminalInput.poll();
      if (key == -1) {
        sleepQuiet(40);
        continue;
      }

      if (key == ESC) {
        RawTerminal.disable();
        System.out.println();
        System.exit(0);
      } else if (key == 'd' || key == 'D') {
        session.deleteLast();
      } else if (key == 'h' || key == 'H') {
        showHistory(session);
      } else if (key == 's' || key == 'S') {
        RubikTypes chosen = openSettings(session);
        if (chosen != null) {
          session.setCubeType(chosen);
          scramble = GenerateScramble.generate(chosen);
        }
      } else if (key == 'i' || key == 'I') {
        Solve manual = inputManualTime(session, scramble, rot);
        if (manual != null) {
          promptPenalty(session.buildHeader(), scramble, manual);
          return manual;
        }
      } else if (key == SPACE) {
        break;
      }
    }

    String header = session.buildHeader();

    long inspectionStart = System.nanoTime();
    boolean holding = false;
    boolean holdReady = false;
    long holdBeganAt = 0;
    long lastSpaceAt = 0;
    long inspectionElapsedAtStartMs = -1;

    while (true) {
      long elapsedMs = (System.nanoTime() - inspectionStart) / 1_000_000;

      if (!holding && elapsedMs >= INSPECTION_DNF_MS) {
        String cube = Ascii.cubeFrame(rot[0], rot[1]);
        Renderer.frame(cube, header, scramble, Colors.RED + Colors.BOLD + "DNF - inspection overtime" + Colors.RESET);
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
      String cube = Ascii.cubeFrame(rot[0], rot[1]);
      rot[0] += 0.15;
      rot[1] += 0.09;

      Renderer.frame(cube, header, scramble,
          timeColor + String.format("Inspection: %.1fs", secondsLeft) + Colors.RESET + holdText);

      sleepQuiet(20);
    }

    TerminalInput.drain();
    long solveStart = System.nanoTime();

    while (true) {
      String cube = Ascii.cubeFrame(rot[0], rot[1]);
      rot[0] += 0.15;
      rot[1] += 0.09;

      Renderer.frame(cube, header, scramble,
          Colors.GREEN + Colors.BOLD + "Solving...  [Space] to stop" + Colors.RESET);

      int key = TerminalInput.poll();
      if (key == ESC) {
        RawTerminal.disable();
        System.out.println();
        System.exit(0);
      }
      if (key == SPACE) break;

      sleepQuiet(20);
    }

    long totalNanos = System.nanoTime() - solveStart;

    Solve solve = new Solve(scramble, totalNanos, inspectionElapsedAtStartMs);
    String cube = Ascii.cubeFrame(rot[0], rot[1]);
    Renderer.frame(cube, header, scramble, Colors.BOLD + "Time: " + Colors.RESET + solve.display());

    promptPenalty(header, scramble, solve);
    return solve;
  }

  private static Solve inputManualTime(SolveSession session, String scramble, double[] rot) {
    StringBuilder digits = new StringBuilder();

    while (true) {
      String preview = digits.length() == 0 ? "0.00" : formatTime(parseManualNanos(digits.toString()));

      String cube = Ascii.cubeFrame(rot[0], rot[1]);
      rot[0] += 0.15;
      rot[1] += 0.09;

      Renderer.frame(cube, session.buildHeader(), scramble,
          "Enter time: " + Colors.BOLD + preview + Colors.RESET +
              "   " + Colors.GRAY + "[type digits]  [Enter] confirm  [Backspace] delete  [Esc] cancel" + Colors.RESET);

      int key = TerminalInput.poll();
      if (key == -1) {
        sleepQuiet(30);
        continue;
      }

      if (key == ESC) return null;

      if (key == ENTER || key == LF) {
        if (digits.length() == 0) return null;
        return Solve.manual(scramble, parseManualNanos(digits.toString()));
      }

      if (key == BACKSPACE || key == BACKSPACE_ALT) {
        if (digits.length() > 0) digits.deleteCharAt(digits.length() - 1);
        continue;
      }

      if (key >= '0' && key <= '9' && digits.length() < 6) {
        digits.append((char) key);
      }
    }
  }

  private static long parseManualNanos(String digits) {
    if (digits.isEmpty()) return 0;
    String padded = digits.length() == 1 ? "0" + digits : digits;
    String centisStr = padded.substring(padded.length() - 2);
    String rest = padded.substring(0, padded.length() - 2);

    int centis = Integer.parseInt(centisStr);
    int minutes = 0;
    int seconds;

    if (rest.isEmpty()) {
      seconds = 0;
    } else if (rest.length() <= 2) {
      seconds = Integer.parseInt(rest);
    } else {
      minutes = Integer.parseInt(rest.substring(0, rest.length() - 2));
      seconds = Integer.parseInt(rest.substring(rest.length() - 2));
    }

    return (minutes * 60_000L + seconds * 1000L + centis * 10L) * 1_000_000L;
  }

  private static RubikTypes openSettings(SolveSession session) {
    RubikTypes[] types = RubikTypes.values();
    RubikTypes selected = null;

    while (true) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < types.length && i < 9; i++) {
        boolean current = types[i] == session.getCubeType();
        sb.append(current ? Colors.GREEN + Colors.BOLD : Colors.RESET);
        sb.append(" [").append(i + 1).append("] ").append(types[i].name());
        sb.append(Colors.RESET).append("\r\n");
      }

      TerminalInput.drain();
      Renderer.plainFrame("Settings - select cube", sb.toString(), "[1-" + Math.min(9, types.length) + "] select   [Space] back");

      int key = TerminalInput.blocking();
      if (key == ESC) {
        RawTerminal.disable();
        System.out.println();
        System.exit(0);
      } else if (key == SPACE) {
        return selected;
      } else if (key >= '1' && key <= '9') {
        int idx = key - '1';
        if (idx < types.length) {
          selected = types[idx];
          return selected;
        }
      }
    }
  }

  private static void showHistory(SolveSession session) {
    TerminalInput.drain();
    Renderer.plainFrame("History", session.buildHistory(), "[Space] back");
    int key;
    do {
      key = blockingKey();
      if (key == ESC) {
        RawTerminal.disable();
        System.out.println();
        System.exit(0);
      }
    } while (key != SPACE && key != ENTER && key != LF);
  }

  private static void promptPenalty(String header, String scramble, Solve solve) {
    TerminalInput.drain();
    while (true) {
      Renderer.frame("", header, scramble,
          "[1] no penalty   [2] +2   [3] DNF   [Space] continue   -> " + solve.display());
      int key = blockingKey();
      if (key == '1') {
        solve.clearPenalty();
      } else if (key == '2') {
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