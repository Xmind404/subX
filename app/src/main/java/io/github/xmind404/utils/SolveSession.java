package io.github.xmind404.utils;

import io.github.xmind404.data.Colors;
import io.github.xmind404.data.Solve;

import java.util.ArrayList;
import java.util.List;

public final class SolveSession {

  private final List<Solve> solves = new ArrayList<>();

  public void add(Solve solve) {
    solves.add(solve);
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

    return Colors.BOLD + "Last: " + Colors.RESET + last + "\n" +
        Colors.BOLD + "Solves: " + Colors.RESET + count() +
        "   " + Colors.BOLD + "Best: " + Colors.RESET + best() +
        "   " + Colors.BOLD + "Worst: " + Colors.RESET + worst() +
        "   " + Colors.BOLD + "ao5: " + Colors.RESET + averageOf(5) +
        "   " + Colors.BOLD + "ao12: " + Colors.RESET + averageOf(12);
  }
}