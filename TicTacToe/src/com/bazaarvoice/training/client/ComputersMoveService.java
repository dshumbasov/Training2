package com.bazaarvoice.training.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("computersMove")
public interface ComputersMoveService extends RemoteService {

    int[] getComputersMove(int[][] gameField);
}
