package io.github.xmind404;

import io.github.xmind404.data.Rubik.RubikTypes;
import io.github.xmind404.data.Solve;
import io.github.xmind404.utils.GenerateScramble;
import io.github.xmind404.utils.RawTerminal;
import io.github.xmind404.utils.SolveSession;
import io.github.xmind404.utils.Timer;

public class App {
    public static void main(String[] args) {
        RawTerminal.enable();

        SolveSession session = new SolveSession();

        while (true) {
            String scramble = GenerateScramble.generate(RubikTypes.THRxTHR);
            String header = session.buildHeader();

            Solve solve = Timer.runSolve(header, scramble);
            session.add(solve);
        }
    }
}