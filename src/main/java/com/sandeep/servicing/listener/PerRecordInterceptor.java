package com.sandeep.servicing.listener;

import com.sandeep.servicing.dto.LoanDTO;
import org.springframework.batch.core.ItemReadListener;

public class PerRecordInterceptor implements ItemReadListener<LoanDTO> {


    @Override
    public void beforeRead() {
        System.out.println("Before read record");
    }

    @Override
    public void afterRead(LoanDTO item) {
        System.out.println("After read record - " + item.toString());
    }

    @Override
    public void onReadError(Exception ex) {
        System.out.println("On error while reading");
    }
}
