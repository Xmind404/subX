package io.github.xmind404.utils;

import io.github.xmind404.data.Colors;

public final class Renderer {

  private static final String SEP = Colors.GRAY + "======================================" + Colors.RESET;
  private static final String NL = "\r\n";

  private Renderer() {}

  public static void frame(String header, String scramble, String status) {
    StringBuilder sb = new StringBuilder();
    sb.append("\033[2J\033[H");

    sb.append(header).append(NL);
    sb.append(SEP).append(NL);
    sb.append(scramble).append(NL);
    sb.append(SEP).append(NL);
    sb.append(status).append(NL);

    System.out.print(sb);
    System.out.flush();
  }
}