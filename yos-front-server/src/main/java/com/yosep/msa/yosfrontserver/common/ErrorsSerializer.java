package com.yosep.msa.yosfrontserver.common;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

// ErrorSerializer를 ObjectMapper에 등록해야 하는데 @JsonComponent를 사용하면 쉽게 등록 가능하다.
@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors>{

	@Override
	public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		// TODO Auto-generated method stub
		gen.writeStartArray();
		errors.getFieldErrors().stream().forEach(e -> {
			try {
				gen.writeStartObject();
				gen.writeStringField("field", e.getField());
				gen.writeStringField("objectName", e.getObjectName());
				gen.writeStringField("code", e.getCode());
				gen.writeStringField("defaultMessage", e.getDefaultMessage());
				Object rejectedValue = e.getRejectedValue();
				if(rejectedValue != null) {
					gen.writeStringField("rejectedValue", rejectedValue.toString());
				}
				
				gen.writeEndObject();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		errors.getGlobalErrors().forEach(e -> {
			try {
				gen.writeStartObject();
				gen.writeStringField("objectName", e.getObjectName());
				gen.writeStringField("code", e.getCode());
				gen.writeStringField("defaultMessage", e.getDefaultMessage());
				
				gen.writeEndObject();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});;
		gen.writeEndArray();
	}

}