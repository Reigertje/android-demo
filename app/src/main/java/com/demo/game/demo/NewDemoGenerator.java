package com.demo.game.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by brian on 8-6-18.
 */

public class NewDemoGenerator {

    private static final char UNKNOWN = '?';
    private static final char OPEN = ' ';
    private static final char JUMP = '#';

    private char[][] generationGrid;

    public NewDemoGenerator() {

        generationGrid = new char[3][20];
    }

    private void resetGenerationGrid() {
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 20; ++y) {
                generationGrid[x][y] = UNKNOWN;
            }
        }
    }

    private void generateRow(int x, int y, int src) {

        List<Integer> possibleDst = new ArrayList<>();

        if (src != -1 && x > 0) {
            possibleDst.add(-1);
        }
        if (src != 1 && x < 2) {
            possibleDst.add(1);
        }
        possibleDst.add(0);

        Collections.shuffle(possibleDst);

        int dst = possibleDst.get(0);


        char cellType = OPEN;
        if (src == 0 && dst == 0) {
            cellType = JUMP;
        }

        generationGrid[x][y] = cellType;

        int nextX = x + dst;
        int nextY = dst == 0 ? y + 1 : y;

        if (nextY < 20) {
            generateRow(nextX, dst == 0 ? y + 1 : y, -dst);
        }

    }

    private void generateGrid() {
        generateRow(1, 0, 0);


    }


}
