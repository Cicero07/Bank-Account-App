package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {


    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<TransferDTO> myTransferList(String username){
        List<TransferDTO> transferDTOList = new ArrayList<>();
        String sql = "SELECT transfer.transfer_id, transfer.amount, sender.username as from_username,\n" +
                "receiver.username as to_username\n" +
                "FROM transfer\n" +
                "JOIN account AS sender_account ON transfer.from_account_id = sender_account.account_id\n" +
                "JOIN tenmo_user AS sender ON sender_account.user_id = sender.user_id\n" +
                "JOIN account AS receiver_account ON transfer.to_account_id = receiver_account.account_id\n" +
                "JOIN tenmo_user AS receiver ON receiver_account.user_id = receiver.user_id\n" +
                "WHERE (sender.username = ? OR receiver.username = ?);";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username, username);

        while (results.next()){
            TransferDTO transferDTO = new TransferDTO();
            transferDTO.setTransfer_id(results.getInt("transfer_id"));
            transferDTO.setAmount(results.getBigDecimal("amount"));
            transferDTO.setFrom_account_name(results.getString("from_username"));
            transferDTO.setTo_account_name(results.getString("to_username"));
            transferDTOList.add(transferDTO);

        }
        return transferDTOList;
    }



    public TransferDTO transferById(int id){


        String sql = "SELECT transfer.transfer_id, transfer.amount, sender.username as from_username,\n" +
                "receiver.username as to_username\n" +
                "FROM transfer\n" +
                "JOIN account AS sender_account ON transfer.from_account_id = sender_account.account_id\n" +
                "JOIN tenmo_user AS sender ON sender_account.user_id = sender.user_id\n" +
                "JOIN account AS receiver_account ON transfer.to_account_id = receiver_account.account_id\n" +
                "JOIN tenmo_user AS receiver ON receiver_account.user_id = receiver.user_id\n" +
                "WHERE (transfer.transfer_id = ?);";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,id);

        if (results.next()){
            TransferDTO transferDTO = new TransferDTO();
            transferDTO.setTransfer_id(results.getInt("transfer_id"));
            transferDTO.setAmount(results.getBigDecimal("amount"));
            transferDTO.setFrom_account_name(results.getString("from_username"));
            transferDTO.setTo_account_name(results.getString("to_username"));
            return transferDTO;
        }

        throw new EmptyResultDataAccessException("Expected Single Object", 1);
    }


}











