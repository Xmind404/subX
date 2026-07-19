package io.github.xmind404.data;

public final class Colors {

  private Colors() {}

  public static final String RESET = "\033[0m";
  public static final String BOLD = "\033[1m";
  public static final String DIM = "\033[2m";
  public static final String ITALIC = "\033[3m";
  public static final String UNDERLINE = "\033[4m";

  public static final String BLACK = "\033[30m";
  public static final String RED = "\033[31m";
  public static final String GREEN = "\033[32m";
  public static final String YELLOW = "\033[33m";
  public static final String BLUE = "\033[34m";
  public static final String MAGENTA = "\033[35m";
  public static final String CYAN = "\033[36m";
  public static final String WHITE = "\033[37m";
  public static final String GRAY = "\033[90m";

  public static final String BRIGHT_RED = "\033[91m";
  public static final String BRIGHT_GREEN = "\033[92m";
  public static final String BRIGHT_YELLOW = "\033[93m";
  public static final String BRIGHT_BLUE = "\033[94m";
  public static final String BRIGHT_MAGENTA = "\033[95m";
  public static final String BRIGHT_CYAN = "\033[96m";
  public static final String BRIGHT_WHITE = "\033[97m";

  public static final String ORANGE = "\033[38;5;208m";
  public static final String PURPLE = "\033[38;5;135m";
  public static final String PINK = "\033[38;5;205m";
  public static final String TEAL = "\033[38;5;80m";
  public static final String GOLD = "\033[38;5;220m";

  public static final String BG_BLACK = "\033[40m";
  public static final String BG_RED = "\033[41m";
  public static final String BG_GREEN = "\033[42m";
  public static final String BG_YELLOW = "\033[43m";
  public static final String BG_BLUE = "\033[44m";
  public static final String BG_MAGENTA = "\033[45m";
  public static final String BG_CYAN = "\033[46m";
  public static final String BG_WHITE = "\033[47m";
  public static final String BG_GRAY = "\033[100m";
  public static final String BG_ORANGE = "\033[48;5;208m";
}