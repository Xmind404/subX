package io.github.xmind404;

import io.github.xmind404.data.Rubik.RubikTypes;
import io.github.xmind404.utils.Ascii;
import io.github.xmind404.utils.GenerateScramble;
import io.github.xmind404.data.Rubik.RubikTypes;



public class App {

    public static void main(String[] args) {

        System.out.println(GenerateScramble.generate(RubikTypes.FIVxFIV));
    }
}