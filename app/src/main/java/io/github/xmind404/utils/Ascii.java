package io.github.xmind404.utils;

public final class Ascii {
  private Ascii() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  public static void printCube() {
    double a = 0, b = 0;
    String chars = "@%#:*+=-.";
    int w = 80, h = 24;

    while (true) {
      char[] screen = new char[w * h];
      java.util.Arrays.fill(screen, ' ');

      for (double i = -1; i <= 1; i += 0.2) {
        for (double j = -1; j <= 1; j += 0.2) {
          for (double k = -1; k <= 1; k += 0.2) {
            double x = i, y = j, z = k;
            double t = x * Math.cos(a) - z * Math.sin(a);
            z = x * Math.sin(a) + z * Math.cos(a); x = t;
            t = y * Math.cos(b) - z * Math.sin(b);
            z = y * Math.sin(b) + z * Math.cos(b); y = t;

            int px = (int)(x * 12 + w / 2);
            int py = (int)(y * 6 + h / 2);
            int idx = px + py * w;

            if (idx >= 0 && idx < w * h) {
              int shade = (int)((z + 1.5) * 2);
              shade = Math.max(0, Math.min(chars.length() - 2, shade));
              screen[idx] = chars.charAt(shade);
            }
          }
        }
      }

      System.out.print("\033[H");
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < h; i++) sb.append(screen, i * w, w).append('\n');
      System.out.print(sb);

      a += 0.08; b += 0.05;
      try { Thread.sleep(30); } catch (Exception e) { break; }
    }
  }
}