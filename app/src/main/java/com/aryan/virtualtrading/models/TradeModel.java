package com.aryan.virtualtrading.models;

import java.util.Date;

public class TradeModel {

    private String _id, company, trader, tradeType, remarks, status;
    private Date date;
    private int currentBalance, creditQty, debitQty;
    private float pricePerShare;

    public TradeModel(String _id, String company, String trader, String tradeType, String remarks, String status, Date date, int currentBalance, int creditQty, int debitQty, float pricePerShare) {
        this._id = _id;
        this.company = company;
        this.trader = trader;
        this.tradeType = tradeType;
        this.remarks = remarks;
        this.status = status;
        this.date = date;
        this.currentBalance = currentBalance;
        this.creditQty = creditQty;
        this.debitQty = debitQty;
        this.pricePerShare = pricePerShare;
    }

    public TradeModel(String company, String trader, String tradeType, String remarks, String status, Date date, int currentBalance, int creditQty, int debitQty, float pricePerShare) {
        this.company = company;
        this.trader = trader;
        this.tradeType = tradeType;
        this.remarks = remarks;
        this.status = status;
        this.date = date;
        this.currentBalance = currentBalance;
        this.creditQty = creditQty;
        this.debitQty = debitQty;
        this.pricePerShare = pricePerShare;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTrader() {
        return trader;
    }

    public void setTrader(String trader) {
        this.trader = trader;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(int currentBalance) {
        this.currentBalance = currentBalance;
    }

    public int getCreditQty() {
        return creditQty;
    }

    public void setCreditQty(int creditQty) {
        this.creditQty = creditQty;
    }

    public int getDebitQty() {
        return debitQty;
    }

    public void setDebitQty(int debitQty) {
        this.debitQty = debitQty;
    }

    public float getPricePerShare() {
        return pricePerShare;
    }

    public void setPricePerShare(float pricePerShare) {
        this.pricePerShare = pricePerShare;
    }
}
