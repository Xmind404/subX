package io.github.xmind404.utils;

import io.github.xmind404.data.Colors;
import io.github.xmind404.data.Rubik.RubikTypes;

public final class Ascii {

  private static final int WIDTH = 60;
  private static final int HEIGHT = 22;
  private static final double SCALE_X = 20;
  private static final double SCALE_Y = 9;
  private static final int MARGIN_LEFT = 4;
  private static final int MARGIN_TOP = 1;
  private static final int MARGIN_BOTTOM = 1;

  private static final String CHARS = ".,`'\"-~:;=+!*#@$$";
  private static final String BORDER_CHARS = ":;=!*";

  private Ascii() {}

  public static int gridSizeFor(RubikTypes type) {
    String name = type.name().toUpperCase();
    String[] parts = name.split("X");
    int size = wordToNumber(parts[0]);
    return size > 0 ? size : 3;
  }

  private static int wordToNumber(String word) {
    switch (word) {
      case "ONE": return 1;
      case "TWO": return 2;
      case "THR":
      case "THREE": return 3;
      case "FOUR": return 4;
      case "FIVE": return 5;
      case "SIX": return 6;
      case "SEVEN": return 7;
      default: return 0;
    }
  }

  public static String cubeFrame(double a, double b, int gridSize) {
    int n = Math.max(1, gridSize);

    char[] buffer = new char[WIDTH * HEIGHT];
    double[] zbuffer = new double[WIDTH * HEIGHT];
    java.util.Arrays.fill(buffer, ' ');

    double cosA = Math.cos(a), sinA = Math.sin(a);
    double cosB = Math.cos(b), sinB = Math.sin(b);

    int[] stickerOffset = buildStickerOffset(n);

    for (int faceSign = -1; faceSign <= 1; faceSign += 2) {
      for (double x = -1; x <= 1; x += 0.08) {
        for (double y = -1; y <= 1; y += 0.08) {

          for (int axis = 0; axis < 3; axis++) {
            double px, py, pz;
            if (axis == 0) { px = faceSign; py = x; pz = y; }
            else if (axis == 1) { px = x; py = faceSign; pz = y; }
            else { px = x; py = y; pz = faceSign; }

            double y1 = py * cosA - pz * sinA;
            double z1 = py * sinA + pz * cosA;

            double x1 = px * cosB - z1 * sinB;
            double z2 = px * sinB + z1 * cosB;

            double ooz = 1 / (z2 + 3);

            int xp = (int) Math.floor(WIDTH / 2.0 + SCALE_X * ooz * x1);
            int yp = (int) Math.floor(HEIGHT / 2.0 - SCALE_Y * ooz * y1);

            if (xp < 0 || xp >= WIDTH || yp < 0 || yp >= HEIGHT) continue;

            int idx = xp + yp * WIDTH;
            if (ooz <= zbuffer[idx]) continue;
            zbuffer[idx] = ooz;

            double u, v;
            if (axis == 0) { u = py; v = pz; }
            else if (axis == 1) { u = px; v = pz; }
            else { u = px; v = py; }

            double uu = (u + 1) / 2;
            double vv = (v + 1) / 2;

            int cellX = clamp((int) Math.floor(uu * n), 0, n - 1);
            int cellY = clamp((int) Math.floor(vv * n), 0, n - 1);
            int stickerId = cellX + cellY * n;

            double inCellU = (uu * n) % 1;
            double inCellV = (vv * n) % 1;

            double nx = 0, ny = 0, nz = 0;
            if (axis == 0) nx = faceSign;
            else if (axis == 1) ny = faceSign;
            else nz = faceSign;

            double ny1 = ny * cosA - nz * sinA;
            double nz1 = ny * sinA + nz * cosA;
            double nx1 = nx * cosB - nz1 * sinB;
            double nz2 = nx * sinB + nz1 * cosB;

            double light = ny1 * -0.9 + nz2 * -0.2 + nx1 * 0.1;

            double borderThickness = n <= 1 ? 0.0 : 0.10;
            boolean isBorder = borderThickness > 0 && (
                inCellU < borderThickness || inCellU > (1 - borderThickness) ||
                    inCellV < borderThickness || inCellV > (1 - borderThickness));

            if (isBorder) {
              int borderN = clamp((int) Math.floor(5 * ((light + 1) / 2)), 0, BORDER_CHARS.length() - 1);
              buffer[idx] = BORDER_CHARS.charAt(borderN);
            } else {
              int gradientIndex = (int) Math.floor(((light + 1) / 2) * (CHARS.length() - 1));
              gradientIndex += stickerOffset[stickerId % stickerOffset.length] - 2;
              gradientIndex = clamp(gradientIndex, 0, CHARS.length() - 1);
              buffer[idx] = CHARS.charAt(gradientIndex);
            }
          }
        }
      }
    }

    StringBuilder sb = new StringBuilder();
    String pad = " ".repeat(MARGIN_LEFT);

    for (int i = 0; i < MARGIN_TOP; i++) sb.append("\r\n");

    for (int row = 0; row < HEIGHT; row++) {
      sb.append(pad).append(Colors.PINK);
      sb.append(buffer, row * WIDTH, WIDTH);
      sb.append(Colors.RESET);
      sb.append("\r\n");
    }

    for (int i = 0; i < MARGIN_BOTTOM; i++) sb.append("\r\n");

    return sb.toString();
  }

  private static int[] buildStickerOffset(int n) {
    int total = n * n;
    int[] offset = new int[total];
    for (int i = 0; i < total; i++) {
      offset[i] = (i * 7) % 5;
    }
    return offset;
  }

  private static int clamp(int v, int lo, int hi) {
    return Math.max(lo, Math.min(hi, v));
  }
}