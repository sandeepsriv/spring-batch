package com.sandeep.servicing.mapper;

import com.sandeep.servicing.dto.LoanDTO;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;


public class LoanCSVMapper implements FieldSetMapper<LoanDTO>{

    @Override
    public LoanDTO mapFieldSet(FieldSet fieldSet){
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setLoanIdSysGen(fieldSet.readString("loanIdSysGen"));
        loanDTO.setUpb(fieldSet.readInt("upb"));
        loanDTO.setNoteRate(fieldSet.readInt("noteRate"));
        loanDTO.setLoanStatus(fieldSet.readString("loanStatus"));
        loanDTO.setMortgageType(fieldSet.readString("mortgageType"));
        return loanDTO;
    }
}
