package com.sandeep.servicing.mapper;

import com.sandeep.servicing.dto.LoanServicingDTO;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.json.JsonObjectReader;
import org.springframework.core.io.Resource;


public class ServicingJSONMapper implements JsonObjectReader<LoanServicingDTO> {

    public LoanServicingDTO mapFieldSet(FieldSet fieldSet) {
        LoanServicingDTO loanServicingDTO = new LoanServicingDTO();
        loanServicingDTO.setLoanIdSysGen(fieldSet.readString("loanIdSysGen"));
        loanServicingDTO.setUpb(fieldSet.readInt("upb"));
        loanServicingDTO.setNoteRate(fieldSet.readInt("noteRate"));
        loanServicingDTO.setLoanStatus(fieldSet.readString("loanStatus"));
        loanServicingDTO.setMortgageType(fieldSet.readString("mortgageType"));
        return loanServicingDTO;
    }

    @Override
    public void open(Resource resource) throws Exception {

    }

    @Override
    public LoanServicingDTO read() throws Exception {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
