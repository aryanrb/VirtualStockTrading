package com.aryan.virtualtrading.models;

public class BalanceModel {

    float initialVCoin, vCoinBalance, totalValue, vCoinInvested;
    String _id;
    UserModel acHolder;

    public BalanceModel(float initialVCoin, float vCoinBalance, float totalValue, float vCoinInvested, String _id, UserModel acHolder) {
        this.initialVCoin = initialVCoin;
        this.vCoinBalance = vCoinBalance;
        this.totalValue = totalValue;
        this.vCoinInvested = vCoinInvested;
        this._id = _id;
        this.acHolder = acHolder;
    }

    public BalanceModel(float initialVCoin, float vCoinBalance, float totalValue, float vCoinInvested, UserModel acHolder) {
        this.initialVCoin = initialVCoin;
        this.vCoinBalance = vCoinBalance;
        this.totalValue = totalValue;
        this.vCoinInvested = vCoinInvested;
        this.acHolder = acHolder;
    }

    public BalanceModel(float initialVCoin, float vCoinBalance, float totalValue, float vCoinInvested) {
        this.initialVCoin = initialVCoin;
        this.vCoinBalance = vCoinBalance;
        this.totalValue = totalValue;
        this.vCoinInvested = vCoinInvested;
    }

    public float getInitialVCoin() {
        return initialVCoin;
    }

    public void setInitialVCoin(float initialVCoin) {
        this.initialVCoin = initialVCoin;
    }

    public float getvCoinBalance() {
        return vCoinBalance;
    }

    public void setvCoinBalance(float vCoinBalance) {
        this.vCoinBalance = vCoinBalance;
    }

    public float getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(float totalValue) {
        this.totalValue = totalValue;
    }

    public float getvCoinInvested() {
        return vCoinInvested;
    }

    public void setvCoinInvested(float vCoinInvested) {
        this.vCoinInvested = vCoinInvested;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public UserModel getAcHolder() {
        return acHolder;
    }

    public void setAcHolder(UserModel acHolder) {
        this.acHolder = acHolder;
    }
}
