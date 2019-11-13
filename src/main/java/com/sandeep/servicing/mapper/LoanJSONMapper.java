package com.sandeep.servicing.mapper;

import com.sandeep.servicing.dto.LoanDTO;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.json.JsonObjectReader;
import org.springframework.core.io.Resource;


public class LoanJSONMapper implements JsonObjectReader<LoanDTO> {

    public LoanDTO mapFieldSet(FieldSet fieldSet) {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setLoanIdSysGen(fieldSet.readString("loanIdSysGen"));
        loanDTO.setUpb(fieldSet.readInt("upb"));
        loanDTO.setNoteRate(fieldSet.readInt("noteRate"));
        loanDTO.setLoanStatus(fieldSet.readString("loanStatus"));
        loanDTO.setMortgageType(fieldSet.readString("mortgageType"));
        return loanDTO;
    }

    @Override
    public void open(Resource resource) throws Exception {

    }

    @Override
    public LoanDTO read() throws Exception {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
