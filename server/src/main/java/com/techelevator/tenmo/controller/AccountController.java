package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.*;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;


@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {


    private AccountDao accountDao;
    private JdbcUserDao jdbcUserDao;
    private JdbcAccountDao jdbcAccountDao;
    private JdbcTransferDao jdbcTransferDao;





    public AccountController(AccountDao accountDao,JdbcUserDao jdbcUserDao,JdbcAccountDao jdbcAccountDao, JdbcTransferDao jdbcTransferDao) {
        this.accountDao = accountDao;
        this.jdbcUserDao = jdbcUserDao;
        this.jdbcAccountDao = jdbcAccountDao;
        this.jdbcTransferDao = jdbcTransferDao;
    }


    @RequestMapping(path="/balance", method = RequestMethod.GET)
    public BalanceDTO getBalance(Principal principal) {
        return accountDao.getBalance( principal.getName() );
    }

    @RequestMapping(path = "/transferUserList", method = RequestMethod.GET)
    public List<UsernameDTO> transferUserList(){
        return jdbcUserDao.findAllUsers();
    }


    @RequestMapping(path="/transfer_balance", method = RequestMethod.POST)
    public TransferDTO transferBalance(Principal principal, @RequestBody TransferDTO transferDTO) {
        if (!principal.getName().equals(transferDTO.getTo_account_name())) {
         return jdbcAccountDao.updateBalance(transferDTO.getAmount(), transferDTO.getFrom_account_name(), transferDTO.getTo_account_name());

        }
        throw new IllegalArgumentException("To account name should not match principal name.");
    }


    @RequestMapping(path = "/mytransfers", method = RequestMethod.GET)
    public List<TransferDTO> myTransfers(Principal principal){
        return jdbcTransferDao.myTransferList(principal.getName());
    }


    @RequestMapping(path = "/", method = RequestMethod.GET)
    public TransferDTO getSpecificTransfer(@RequestParam(name = "id") int id){
   //     TransferDTO transferDTO = new TransferDTO();

     //   if (transferDTO == null){
       //     throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer Not Found");
        //}else {
            return jdbcTransferDao.transferById(id);
        //}

    }

}
