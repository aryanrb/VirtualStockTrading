package com.aryan.virtualtrading.models;

public class PortfolioModel {
    String _id, acHolder;
    MarketModel company;
    int shareBalance;
    float boughtPrice, previousPrice, currentPrice;

    public PortfolioModel() {
    }

    public PortfolioModel(String _id, MarketModel company, String acHolder, int shareBalance, float boughtPrice, float previousPrice, float currentPrice) {
        this._id = _id;
        this.company = company;
        this.acHolder = acHolder;
        this.shareBalance = shareBalance;
        this.boughtPrice = boughtPrice;
        this.previousPrice = previousPrice;
        this.currentPrice = currentPrice;
    }

    public PortfolioModel(MarketModel company, String acHolder, int shareBalance, float boughtPrice, float previousPrice, float currentPrice) {
        this.company = company;
        this.acHolder = acHolder;
        this.shareBalance = shareBalance;
        this.boughtPrice = boughtPrice;
        this.previousPrice = previousPrice;
        this.currentPrice = currentPrice;
    }

    public PortfolioModel(String acHolder, int shareBalance, float boughtPrice, float previousPrice, float currentPrice) {
        this.acHolder = acHolder;
        this.shareBalance = shareBalance;
        this.boughtPrice = boughtPrice;
        this.previousPrice = previousPrice;
        this.currentPrice = currentPrice;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public MarketModel getCompany() {
        return company;
    }

    public void setCompany(MarketModel company) {
        this.company = company;
    }

    public String getAcHolder() {
        return acHolder;
    }

    public void setAcHolder(String acHolder) {
        this.acHolder = acHolder;
    }

    public int getShareBalance() {
        return shareBalance;
    }

    public void setShareBalance(int shareBalance) {
        this.shareBalance = shareBalance;
    }

    public float getBoughtPrice() {
        return boughtPrice;
    }

    public void setBoughtPrice(float boughtPrice) {
        this.boughtPrice = boughtPrice;
    }

    public float getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(float previousPrice) {
        this.previousPrice = previousPrice;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }
}
