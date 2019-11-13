package com.sandeep.servicing.processor;

import com.sandeep.servicing.dto.LoanDTO;
import com.sandeep.servicing.model.Loan;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class LoanJSONProcessor implements ItemProcessor<LoanDTO, Loan> {

    // Use this execution context if you want to store any metadata
    // about the job or the NAS file in database
    private ExecutionContext executionContext;

    public LoanJSONProcessor(ExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    @Override
    public Loan process (LoanDTO loanDTO) throws Exception {
        Loan loan = new Loan();
        loan.setLoanIdSysGen(loanDTO.getLoanIdSysGen());
        loan.setLoanStatus(loanDTO.getLoanStatus());
        loan.setMortgageType(loanDTO.getMortgageType());
        loan.setNoteRate(loanDTO.getNoteRate());
        loan.setUpb(loanDTO.getUpb());
        return loan;
    }


}
