package io.github.xmind404;

import io.github.xmind404.utils.Ascii;

public class App {

    public static void main(String[] args) {

        Thread cubeThread =
            new Thread(
                Ascii::printCube
            );

        cubeThread.start();

        System.out.println("Xmind404");
    }
}