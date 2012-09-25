package com.bazaarvoice.training.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ComputersMoveServiceAsync {

    void getComputersMove(int[][] gameField, AsyncCallback<int[]> callback);
}
