package com.sandeep.servicing.dto;

import lombok.Data;

@Data
public class LoanServicingDTO {

    private String loanIdSysGen;
    private int upb;
    private int noteRate;
    private String loanStatus;
    private String mortgageType;

    public String getLoanIdSysGen() {
        return loanIdSysGen;
    }

    public void setLoanIdSysGen(String loanIdSysGen) {
        this.loanIdSysGen = loanIdSysGen;
    }

    public int getUpb() {
        return upb;
    }

    public void setUpb(int upb) {
        this.upb = upb;
    }

    public int getNoteRate() {
        return noteRate;
    }

    public void setNoteRate(int noteRate) {
        this.noteRate = noteRate;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getMortgageType() {
        return mortgageType;
    }

    public void setMortgageType(String mortgageType) {
        this.mortgageType = mortgageType;
    }
}
