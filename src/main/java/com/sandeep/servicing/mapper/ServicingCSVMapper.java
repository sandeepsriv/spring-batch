package com.sandeep.servicing.mapper;

import com.sandeep.servicing.dto.LoanServicingDTO;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;


public class ServicingCSVMapper implements FieldSetMapper<LoanServicingDTO>{

    @Override
    public LoanServicingDTO mapFieldSet(FieldSet fieldSet){
        LoanServicingDTO loanServicingDTO = new LoanServicingDTO();
        loanServicingDTO.setLoanIdSysGen(fieldSet.readString("loanIdSysGen"));
        loanServicingDTO.setUpb(fieldSet.readInt("upb"));
        loanServicingDTO.setNoteRate(fieldSet.readInt("noteRate"));
        loanServicingDTO.setLoanStatus(fieldSet.readString("loanStatus"));
        loanServicingDTO.setMortgageType(fieldSet.readString("mortgageType"));
        return loanServicingDTO;
    }
}
