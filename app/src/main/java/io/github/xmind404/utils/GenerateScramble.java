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




  public static String generate(RubikTypes rubikType){
    int ScrambleLength = 0;

    String previousMove = "";
    String move = "";

    boolean isBigCube = (rubikType.ordinal() >= RubikTypes.FOUxFOU.ordinal()) ? true : false;

    switch (rubikType){
      case TWOxTWO -> ScrambleLength = RubikTypesScrambleLengths.TWOxTWO;
      case THRxTHR -> ScrambleLength = RubikTypesScrambleLengths.THRxTHR;
      case FOUxFOU -> ScrambleLength = RubikTypesScrambleLengths.FOUxFOU;
      case FIVxFIV -> ScrambleLength = RubikTypesScrambleLengths.FIVxFIV;
    }

    List<String> scramble = new ArrayList<>();


    while (scramble.size() < ScrambleLength){
      if (isBigCube){
        move = RubikMoves.BigCubeMoves[RANDOM.nextInt(RubikMoves.BigCubeMoves.length)];
      }else{
        move = RubikMoves.SmallCubeMoves[RANDOM.nextInt(RubikMoves.SmallCubeMoves.length)];
      }



      String moveBase = move.substring(0, 1);

      if (!moveBase.equals(previousMove)){
        scramble.add(move);
        previousMove = moveBase;
      }
    }

    return String.join(" ", scramble);
  }
}
