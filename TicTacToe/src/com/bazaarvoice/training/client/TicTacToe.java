package com.bazaarvoice.training.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TicTacToe implements EntryPoint {

    private static final int FIELD_SIZE = 3;
    private static final String CROSS = "X";
    private static final String ZERO = "O";

    private FlowPanel _gridPanel = new FlowPanel();
    private Grid _grid = new Grid(FIELD_SIZE, FIELD_SIZE);
    private int[][] _gameField = new int[FIELD_SIZE][FIELD_SIZE];
    private ComputersMoveServiceAsync computersMoveSvc = GWT.create(ComputersMoveService.class);

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        _grid.addStyleName("gameFiledTable");
        _gridPanel.add(_grid);
        RootPanel.get("gameField").add(_gridPanel);

        _grid.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                int cellIndex = _grid.getCellForEvent(event).getCellIndex();
                int rowIndex = _grid.getCellForEvent(event).getRowIndex();
                if (isCellOccupied(rowIndex, cellIndex)) {
                    Window.alert("That square is already occupied. Please select another square.");
                } else {
                    // user's move
                    _grid.setText(rowIndex, cellIndex, CROSS);
                    setCell(rowIndex, cellIndex, 1);

                    if (!isStandoff()) {
                        if (!isGameOver()) {
                            computersMove();
                        }
                    }
                }
            }
        });
    }

    private boolean isCellOccupied(int rowIndex, int cellIndex) {
        return getCell(rowIndex, cellIndex) != 0;
    }

    private void computersMove() {

        // Initialize the service proxy.
        if (computersMoveSvc == null) {
            computersMoveSvc = GWT.create(ComputersMoveService.class);
        }

        // Set up the callback object.
        AsyncCallback<int[]> callback = new AsyncCallback<int[]>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }

            public void onSuccess(int[] result) {
                _grid.setText(result[0], result[1], ZERO);
                setCell(result[0], result[1], -1);
                isGameOver();
            }
        };

        // Make the call to the stock price service.
        computersMoveSvc.getComputersMove(_gameField, callback);
    }

    private boolean isGameOver() {
        for (int rowIndex = 0; rowIndex < FIELD_SIZE; rowIndex++) {
            int rowSum = 0;
            for (int columnIndex = 0; columnIndex < FIELD_SIZE; columnIndex++) {
                rowSum += getCell(rowIndex, columnIndex);
            }
            if (hasWinner(rowSum)) {
                return true;
            }
        }
        for (int columnIndex = 0; columnIndex < FIELD_SIZE; columnIndex++) {
            int columnSum = 0;
            for (int rowIndex = 0; rowIndex < FIELD_SIZE; rowIndex++) {
                columnSum += getCell(rowIndex, columnIndex);
            }
            if (hasWinner(columnSum)) {
                return true;
            }
        }
        int diagonalSum = 0;
        for (int cellIndex = 0; cellIndex < FIELD_SIZE; cellIndex++) {
            diagonalSum += getCell(cellIndex, cellIndex);
        }
        if (hasWinner(diagonalSum)) {
            return true;
        }

        diagonalSum = 0;
        for (int cellIndex = 0; cellIndex < FIELD_SIZE; cellIndex++) {
            diagonalSum += getCell(cellIndex, FIELD_SIZE - cellIndex - 1);
        }
        if (hasWinner(diagonalSum)) {
            return true;
        }
        return false;
    }

    private boolean hasWinner(int sum) {
        if (sum == FIELD_SIZE) {
            Window.alert("You win!");
            resetGame();
            return true;
        }
        if (sum == -FIELD_SIZE) {
            Window.alert("I win!");
            resetGame();
            return true;
        }
        return false;
    }

    private boolean isStandoff() {
        for (int rowIndex = 0; rowIndex < FIELD_SIZE; rowIndex++) {
            for (int cellIndex = 0; cellIndex < FIELD_SIZE; cellIndex++) {
                if (!isCellOccupied(rowIndex, cellIndex)) {
                    return false;
                }
            }
        }
        Window.alert("We tied :-(");
        resetGame();
        return true;
    }

    private int getCell(int rowIndex, int cellIndex) {
        return _gameField[rowIndex][cellIndex];
    }

    private void setCell(int rowIndex, int cellIndex, int value) {
        _gameField[rowIndex][cellIndex] = value;
    }

    private void resetGame() {
        _grid.clear(true);
        _gameField = new int[FIELD_SIZE][FIELD_SIZE];
    }
}