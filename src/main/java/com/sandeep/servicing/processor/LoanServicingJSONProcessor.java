package com.sandeep.servicing.processor;

import com.sandeep.servicing.dto.LoanServicingDTO;
import com.sandeep.servicing.model.LoanServicing;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class LoanServicingJSONProcessor implements ItemProcessor<LoanServicingDTO, LoanServicing> {

    @Override
    public LoanServicing process (LoanServicingDTO loanServicingDTO) throws Exception {
        LoanServicing loanServicing = new LoanServicing();
        loanServicing.setLoanIdSysGen(loanServicingDTO.getLoanIdSysGen());
        loanServicing.setLoanStatus(loanServicingDTO.getLoanStatus());
        loanServicing.setMortgageType(loanServicingDTO.getMortgageType());
        loanServicing.setNoteRate(loanServicingDTO.getNoteRate());
        loanServicing.setUpb(loanServicingDTO.getUpb());
        System.out.println("inside processor " + loanServicing.toString());
        return loanServicing;
    }


}
