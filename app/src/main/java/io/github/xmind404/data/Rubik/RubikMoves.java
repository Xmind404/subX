package io.github.xmind404.data.Rubik;

public final class RubikMoves {
  private RubikMoves(){
    throw new UnsupportedOperationException("This is class with constant data( possible moves )");
  }


//              ___  U   ______
//             /    ||       /|  <===B
//            /     v       / |
//           /____________/   |
//           |            | <=== R
//     L===> | F===>      |  /
//           |            | /
//           |____________|/
//                ^
//               ||
//               D
//
//   Visible faces: U (Up), F (Front), R (Right)
//   Hidden faces (opposite sides): D (Down), B (Back), L (Left)
//
//   Move notation (Singmaster notation):
//     X   -> rotate that face  90 clockwise
//     X2  -> rotate that face 180
//     X'  -> rotate that face  90 counter-clockwise ("prime")
//
//   Example for R:  R = RIGHT face turn,  R2 = R twice,  R' = R turned the other way




  public static final String U = "U";
  public static final String U2 = "U2";
  public static final String U_PRIME = "U'";
  public static final String Uw = "Uw";
  public static final String Uw2 = "Uw2";
  public static final String Uw_PRIME = "Uw'";

  public static final String D = "D";
  public static final String D2 = "D2";
  public static final String D_PRIME = "D'";
  public static final String Dw = "Dw";
  public static final String Dw2 = "Dw2";
  public static final String Dw_PRIME = "Dw'";


  public static final String R = "R";
  public static final String R2 = "R2";
  public static final String R_PRIME = "R'";
  public static final String Rw = "Rw";
  public static final String Rw2 = "Rw2";
  public static final String Rw_PRIME = "Rw'";

  public static final String L = "L";
  public static final String L2 = "L2";
  public static final String L_PRIME = "L'";
  public static final String Lw = "Lw";
  public static final String Lw2 = "Lw2";
  public static final String Lw_PRIME = "Lw'";


  public static final String F = "F";
  public static final String F2 = "F2";
  public static final String F_PRIME = "F'";
  public static final String Fw = "Fw";
  public static final String Fw2 = "Fw2";
  public static final String Fw_PRIME = "Fw'";

  public static final String B = "B";
  public static final String B2 = "B2";
  public static final String B_PRIME = "B'";
  public static final String Bw = "Bw";
  public static final String Bw2 = "Bw2";
  public static final String Bw_PRIME = "Bw'";

  public static final String[] SmallCubeMoves = {
      U, U2, U_PRIME,
      D, D2, D_PRIME,
      R, R2, R_PRIME,
      L, L2, L_PRIME,
      F, F2, F_PRIME,
      B, B2, B_PRIME,
  };

  public static final String[] BigCubeMoves  = {
      U, U2, U_PRIME, Uw, Uw2, Uw_PRIME,
      D, D2, D_PRIME, Dw, Dw2, Dw_PRIME,
      R, R2, R_PRIME, Rw, Rw2, Rw_PRIME,
      L, L2, L_PRIME, Lw, Lw2, Lw_PRIME,
      F, F2, F_PRIME, Fw, Fw2, Fw_PRIME,
      B, B2, B_PRIME, Bw, Bw2, Bw_PRIME,
  };
}
