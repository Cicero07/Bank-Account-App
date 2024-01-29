package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.BalanceDTO;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;


    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BalanceDTO getBalance(String username) {

        String sql = "SELECT username, balance from account\n" +
                "JOIN tenmo_user on account.user_id = tenmo_user.user_id \n" +
                "WHERE username = ?;";

        BalanceDTO balanceDTO = null;

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
            if (results.next()) {
                String user = results.getString("username");
                BigDecimal balance = results.getBigDecimal("balance");

                balanceDTO = new BalanceDTO();
                balanceDTO.setUsername(user);
                balanceDTO.setBalance(balance);

            }
        } catch (DataAccessException e) {

        }

        return balanceDTO;
    }


    // Add method below when we have a transaction table


    @Override
    public TransferDTO updateBalance(BigDecimal amount, String from_account_name, String to_account_name) {
        BalanceDTO balanceDTO = new BalanceDTO();

    // Method should only pass in balance and update balance, not perform all functions of transfer.

        String sqlFetch = "SELECT balance FROM account\n" +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
                "WHERE username = ?;";

        BigDecimal accountBalance = jdbcTemplate.queryForObject(sqlFetch, BigDecimal.class, from_account_name);


        if (accountBalance.compareTo(amount) >= 0 && amount.compareTo(BigDecimal.ZERO) > 0) {
            String sql = "UPDATE account\n" +
                    "SET balance = balance + ?\n" +
                    "WHERE account.account_id = (\n" +
                    "    SELECT account.account_id\n" +
                    "    FROM account\n" +
                    "    JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
                    "    WHERE username = ?\n" +
                    ");";

            jdbcTemplate.update(sql, amount, to_account_name);

            String sql2 = "INSERT INTO transfer (from_account_id, to_account_id, amount)\n" +
                    "VALUES (\n" +
                    "    (SELECT account.account_id\n" +
                    "     FROM account\n" +
                    "     JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
                    "     WHERE username = ?), \n" +
                    "    (SELECT account.account_id\n" +
                    "     FROM account\n" +
                    "     JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
                    "     WHERE username = ?),\n" +
                    "    ?  \n" +
                    ") RETURNING transfer_id;";

            Integer result = jdbcTemplate.queryForObject(sql2, Integer.class, from_account_name, to_account_name, amount);

            String sql3 = "UPDATE account\n" +
                    "SET balance = balance - ?\n" +
                    "WHERE account.account_id = (\n" +
                    "    SELECT account.account_id\n" +
                    "    FROM account\n" +
                    "    JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
                    "    WHERE username = ?\n" +
                    ");";

            jdbcTemplate.update(sql3, amount, from_account_name);


            String sql4 = "SELECT transfer.transfer_id, transfer.amount, sender.username as from_username, " +
                    "receiver.username as to_username\n" +
                    "FROM transfer\n" +
                    "JOIN account AS sender_account ON transfer.from_account_id = sender_account.account_id\n" +
                    "JOIN tenmo_user AS sender ON sender_account.user_id = sender.user_id\n" +
                    "JOIN account AS receiver_account ON transfer.to_account_id = receiver_account.account_id\n" +
                    "JOIN tenmo_user AS receiver ON receiver_account.user_id = receiver.user_id\n" +
                    "WHERE (sender.username = ? OR receiver.username = ?)\n" +
                    "  AND (sender.username = ? OR receiver.username = ?) and transfer_id = ?;";

            SqlRowSet results = jdbcTemplate.queryForRowSet(sql4, from_account_name, to_account_name, from_account_name, to_account_name, result);

            if (results.next()) {
                TransferDTO transferDTO = new TransferDTO();
                transferDTO.setTransfer_id(results.getInt("transfer_id"));
                transferDTO.setAmount(results.getBigDecimal("amount"));
                transferDTO.setFrom_account_name(results.getString("from_username"));
                transferDTO.setTo_account_name(results.getString("to_username"));
                return transferDTO;
            }


        }
        throw new EmptyResultDataAccessException("Expected a single result but found none", 1);
    }
    // REFACTOR methods so they are more loosely coupled/less dependencies

    // EX:
    // transfer requested, requires to account and from account, balance > request amount
    // transfer approved, updates to account balance and from account balance
    // when transfer approved, return json of
    // transfer rejected, no change to account balances

    // UPDATE VARIABLE NAMES

}
