package com.udd.lucene.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SimpleQuery {
	
	private String field;
	private String value;
	private boolean isSimple;

	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean getIsSimple() {
		return isSimple;
	}
	public void setIsSimple(boolean isSimple) {
		this.isSimple = isSimple;
	}
	
}
