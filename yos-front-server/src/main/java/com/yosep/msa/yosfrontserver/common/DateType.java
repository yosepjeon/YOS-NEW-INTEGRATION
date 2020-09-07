package com.yosep.msa.yosfrontserver.common;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class DateType {
    private LocalDateTime dateTime;
    
    public DateType(LocalDateTime dateTime) {
    	this.dateTime = dateTime;
    }
    
    public DateType() {
    	
    }
}
