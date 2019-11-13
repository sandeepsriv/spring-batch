package com.sandeep.servicing.processor;

import com.sandeep.servicing.dto.LoanDTO;
import com.sandeep.servicing.model.Loan;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class LoanCSVProcessor implements ItemProcessor<LoanDTO, Loan> {

    @Override
    public Loan process (LoanDTO loanDTO) throws Exception {
        Loan loan = new Loan();
        loan.setLoanIdSysGen(loanDTO.getLoanIdSysGen());
        loan.setLoanStatus(loanDTO.getLoanStatus());
        loan.setMortgageType(loanDTO.getMortgageType());
        loan.setNoteRate(loanDTO.getNoteRate());
        loan.setUpb(loanDTO.getUpb());
        System.out.println("inside processor " + loan.toString());
        return loan;

    }
}
