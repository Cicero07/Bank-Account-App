package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferDTO;

import java.util.List;

public interface TransferDao {
    public List<TransferDTO> myTransferList(String username);
}
