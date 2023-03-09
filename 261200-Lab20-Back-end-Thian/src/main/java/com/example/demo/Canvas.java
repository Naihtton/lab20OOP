package com.example.demo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class Canvas {
    private String[][] colorGrid;
    private String currentPlayer;

    public Canvas() {
        colorGrid = new String[][]{{" ", " ", " "}, {" ", " ", " "}, {" ", " ", " "}};
        currentPlayer = "X";
    }

    public synchronized void paint(PaintMessage paintMessage) {
        int posX = paintMessage.getPosX();
        int posY = paintMessage.getPosY();
        if (posX >= 0 && posX < 3 && posY >= 0 && posY < 3) {
            if (colorGrid[posY][posX].equals(" ")) {
                colorGrid[posY][posX] = paintMessage.getColor();
                currentPlayer = currentPlayer.equals("X") ? "O" : "X";
            }
        }
    }

    public synchronized void reset() {
        colorGrid = new String[][]{{" ", " ", " "}, {" ", " ", " "}, {" ", " ", " "}};
        currentPlayer = "X";
    }
}
