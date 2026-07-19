package io.github.xmind404.utils;

import io.github.xmind404.data.Rubik.RubikMoves;
import io.github.xmind404.data.Rubik.RubikTypes;
import io.github.xmind404.data.Rubik.RubikTypesScrambleLengths;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class GenerateScramble {
  private static final Random RANDOM = new Random();

  private GenerateScramble() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }




  public static String generate(RubikTypes rubikTypes){
    int ScrambleLength = 0;
    String previousMove = null;

    switch (rubikTypes){
      case TWOxTWO -> ScrambleLength = RubikTypesScrambleLengths.TWOxTWO;
      case THRxTHR -> ScrambleLength = RubikTypesScrambleLengths.THRxTHR;
      case FOUxFOU -> ScrambleLength = RubikTypesScrambleLengths.FOUxFOU;
      case FIVxFIV -> ScrambleLength = RubikTypesScrambleLengths.FIVxFIV;
    }

    List<String> scramble = new ArrayList<>();

    while (scramble.size() < ScrambleLength){
      String move = RubikMoves.All[RANDOM.nextInt(RubikMoves.All.length)];
      String moveBase = move.substring(0, 1);

      if (!moveBase.equals(previousMove)){
        scramble.add(move);
        previousMove = moveBase;
      }
    }

    return String.join(" ", scramble);
  }
}
