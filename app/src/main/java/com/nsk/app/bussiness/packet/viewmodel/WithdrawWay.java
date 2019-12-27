package com.nsk.app.bussiness.packet.viewmodel;

/**
 * @author qianpeng
 * @Package com.nsk.app.bussiness.packet.viewmodel
 * @date 2018/9/14.
 * @describe
 */
public class WithdrawWay {


    /**
     * ModeId : 1
     * ModeName : 支付宝
     * ModeNumber :
     * CardId : 0
     * CardBank :
     * input : 1
     */

    private int ModeId;
    private String ModeName;
    private String ModeNumber;
    private int CardId;
    private String CardBank;
    private int input;
    private String inputdata; //文本框的输入内容

    public String getInputdata() {
        return inputdata;
    }

    public void setInputdata(String inputdata) {
        this.inputdata = inputdata;
    }

    public int getModeId() {
        return ModeId;
    }

    public void setModeId(int ModeId) {
        this.ModeId = ModeId;
    }

    public String getModeName() {
        return ModeName;
    }

    public void setModeName(String ModeName) {
        this.ModeName = ModeName;
    }

    public String getModeNumber() {
        return ModeNumber;
    }

    public void setModeNumber(String ModeNumber) {
        this.ModeNumber = ModeNumber;
    }

    public int getCardId() {
        return CardId;
    }

    public void setCardId(int CardId) {
        this.CardId = CardId;
    }

    public String getCardBank() {
        return CardBank;
    }

    public void setCardBank(String CardBank) {
        this.CardBank = CardBank;
    }

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
    }
}
