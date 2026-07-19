package io.github.xmind404;

import io.github.xmind404.data.Rubik.RubikTypes;
import io.github.xmind404.data.Solve;
import io.github.xmind404.utils.GenerateScramble;
import io.github.xmind404.utils.SolveSession;
import io.github.xmind404.utils.Timer;

public class App {
    public static void main(String[] args) {
        SolveSession session = new SolveSession();

        while (true) {
            String scramble = GenerateScramble.generate(RubikTypes.THRxTHR);
            System.out.println(scramble);

            Solve solve = Timer.runSolve(scramble);
            session.add(solve);
            session.printSummary();
        }
    }
}