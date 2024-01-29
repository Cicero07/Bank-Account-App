package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDTO {
    private int transfer_id;

    private BigDecimal amount;

    private String from_account_name;

    private String to_account_name;
    private TransferStatus status;

    public int getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(int transfer_id) {
        this.transfer_id = transfer_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getFrom_account_name() {
        return from_account_name;
    }

    public void setFrom_account_name(String from_account_name) {
        this.from_account_name = from_account_name;
    }

    public String getTo_account_name() {
        return to_account_name;
    }

    public void setTo_account_name(String to_account_name) {
        this.to_account_name = to_account_name;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }
}
