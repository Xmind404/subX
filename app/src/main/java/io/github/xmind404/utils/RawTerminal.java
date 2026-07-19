package io.github.xmind404.utils;

import java.io.File;
import java.io.IOException;

public final class RawTerminal {

  private static boolean enabled = false;

  private RawTerminal() {}

  public static void enable() {
    if (enabled) return;
    run("stty", "raw", "-echo");
    TerminalInput.start();
    enabled = true;
    Runtime.getRuntime().addShutdownHook(new Thread(RawTerminal::disable));
  }

  public static void disable() {
    if (!enabled) return;
    TerminalInput.stop();
    run("stty", "sane");
    enabled = false;
  }

  private static void run(String... command) {
    try {
      ProcessBuilder pb = new ProcessBuilder(command);
      pb.redirectInput(new File("/dev/tty"));
      pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
      pb.redirectError(ProcessBuilder.Redirect.INHERIT);
      pb.start().waitFor();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}