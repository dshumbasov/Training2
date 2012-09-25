package com.bazaarvoice.training.server;

import java.util.Random;

import com.bazaarvoice.training.client.ComputersMoveService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ComputersMoveServiceImpl extends RemoteServiceServlet implements ComputersMoveService {

    @Override
    public int[] getComputersMove(int[][] gameField) {
        int rowIndex;
        int cellIndex;
        do {
            rowIndex = new Random().nextInt(gameField.length);
            cellIndex = new Random().nextInt(gameField.length);
        } while (gameField[rowIndex][cellIndex] != 0);

        return new int[] { rowIndex, cellIndex };
    }

}
