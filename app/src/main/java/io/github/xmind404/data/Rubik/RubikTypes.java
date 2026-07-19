package io.github.xmind404.data.Rubik;

public enum RubikTypes {
  TWOxTWO("2x2x2"),
  THRxTHR("3x3x3"),
  FOUxFOU("4x4x4"),
  FIVxFIV("5x5x5");

  private final String code;

  RubikTypes(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
