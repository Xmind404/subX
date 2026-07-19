package io.github.xmind404.utils;

import io.github.xmind404.data.Colors;
import io.github.xmind404.data.Rubik.RubikTypes;
import io.github.xmind404.data.Solve;

import java.util.ArrayList;
import java.util.List;

public final class SolveSession {

  private final List<Solve> solves = new ArrayList<>();
  private RubikTypes cubeType = RubikTypes.THRxTHR;

  public void add(Solve solve) {
    solves.add(solve);
  }

  public boolean deleteLast() {
    if (solves.isEmpty()) return false;
    solves.remove(solves.size() - 1);
    return true;
  }

  public RubikTypes getCubeType() {
    return cubeType;
  }

  public void setCubeType(RubikTypes type) {
    cubeType = type;
  }

  public int count() {
    return solves.size();
  }

  public String best() {
    long best = Long.MAX_VALUE;
    for (Solve s : solves) {
      if (!s.isDnf() && s.getFinalNanos() < best) {
        best = s.getFinalNanos();
      }
    }
    return best == Long.MAX_VALUE ? "N/A" : Timer.formatTime(best);
  }

  public String worst() {
    if (solves.stream().anyMatch(Solve::isDnf)) {
      return "DNF";
    }
    long worst = -1;
    for (Solve s : solves) {
      if (s.getFinalNanos() > worst) {
        worst = s.getFinalNanos();
      }
    }
    return worst < 0 ? "N/A" : Timer.formatTime(worst);
  }

  public String averageOf(int n) {
    if (solves.size() < n) return "N/A";

    List<Solve> window = solves.subList(solves.size() - n, solves.size());
    long dnfCount = window.stream().filter(Solve::isDnf).count();
    if (dnfCount > 1) return "DNF";

    List<Long> values = new ArrayList<>();
    for (Solve s : window) {
      values.add(s.isDnf() ? Long.MAX_VALUE : s.getFinalNanos());
    }
    values.sort(Long::compareTo);

    long sum = 0;
    for (int i = 1; i < values.size() - 1; i++) {
      sum += values.get(i);
    }
    return Timer.formatTime(sum / (n - 2));
  }

  public String buildHeader() {
    String last = solves.isEmpty() ? "N/A" : solves.get(solves.size() - 1).display();

    return Colors.BOLD + "Cube: " + Colors.RESET + cubeType.name() + "\r\n" +
        Colors.BOLD + "Last: " + Colors.RESET + last + "\r\n" +
        Colors.BOLD + "Solves: " + Colors.RESET + count() +
        "   " + Colors.BOLD + "Best: " + Colors.RESET + best() +
        "   " + Colors.BOLD + "Worst: " + Colors.RESET + worst() +
        "   " + Colors.BOLD + "ao5: " + Colors.RESET + averageOf(5) +
        "   " + Colors.BOLD + "ao12: " + Colors.RESET + averageOf(12);
  }

  public String buildHistory() {
    if (solves.isEmpty()) {
      return Colors.GRAY + "No solves yet." + Colors.RESET + "\r\n";
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < solves.size(); i++) {
      Solve s = solves.get(i);
      sb.append(Colors.BOLD).append(String.format("%3d. ", i + 1)).append(Colors.RESET);
      sb.append(s.display());
      sb.append("   ").append(Colors.GRAY).append(s.getScramble()).append(Colors.RESET);
      sb.append("\r\n");
    }
    return sb.toString();
  }
}