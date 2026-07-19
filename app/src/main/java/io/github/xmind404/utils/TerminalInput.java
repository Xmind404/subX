package io.github.xmind404.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

public final class TerminalInput {

  private static final ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(256);
  private static FileInputStream tty;
  private static Thread reader;
  private static volatile boolean running = false;

  private TerminalInput() {}

  public static synchronized void start() {
    if (running) return;
    try {
      tty = new FileInputStream("/dev/tty");
    } catch (IOException e) {
      throw new RuntimeException(
          "Cannot open /dev/tty. Run this in a real terminal, not through a piped Gradle task.", e);
    }
    running = true;
    reader = new Thread(TerminalInput::readLoop, "terminal-input-reader");
    reader.setDaemon(true);
    reader.start();
  }

  public static synchronized void stop() {
    running = false;
    if (tty != null) {
      try {
        tty.close();
      } catch (IOException ignored) {
      }
    }
  }

  private static void readLoop() {
    try {
      while (running) {
        int b = tty.read();
        if (b == -1) break;
        queue.offer(b);
      }
    } catch (IOException ignored) {
    }
  }

  public static int poll() {
    Integer key = queue.poll();
    return key == null ? -1 : key;
  }

  public static int blocking() {
    try {
      return queue.take();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return -1;
    }
  }

  public static void drain() {
    queue.clear();
  }
}