package com.camunda.example.orderhandling;

import java.util.HashMap;
import java.util.Map;

public class EventMessageRequest {

	private class VariableValue {

		private Object value;
		private String type;
			
		public VariableValue(Object value, String type) {
			
			this.setType(type);
			this.setValue(value);
		}
		
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}	

	private class VariableWithInfoValue extends VariableValue {

		private Map<String, String> valueInfo = null;
		
		public VariableWithInfoValue(Object value, String type) {
			super(value, type);
		}
		
		public VariableWithInfoValue(Object value, String type, Map<String, String> valueInfo) {
			super(value, type);
			this.setValueInfo(valueInfo);
		}

		public Map<String, String> getValueInfo() {
			return valueInfo;
		}

		public void setValueInfo(Map<String, String> valueInfo) {
			this.valueInfo = valueInfo;
		}

		public void addValueInfo(String name, String value) {

			if (valueInfo==null)
				valueInfo = new HashMap<String, String>();
			this.getValueInfo().put(name, value);
		}
	}
	
	private String messageName;
	private String processInstanceId;
	private Map<String, VariableValue> correlationKeys = null;
	private Map<String, VariableValue> processVariables = null;
	
	public String getMessageName() {
		return messageName;
	}
	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public Map<String, VariableValue> getCorrelationKeys() {
		return correlationKeys;
	}
	public void setCorrelationKeys(Map<String, VariableValue> correlationKeys) {
		this.correlationKeys = correlationKeys;
	}
	public Map<String, VariableValue> getProcessVariables() {
		return processVariables;
	}
	public void setProcessVariables(Map<String, VariableValue> processVariables) {
		this.processVariables = processVariables;
	}
	
	public void addCorrelationKey(String name, Object value, String type) {
		
		if (correlationKeys==null)
			correlationKeys = new HashMap<String, VariableValue>();
		this.getCorrelationKeys().put(name, new VariableValue(value, type));
	}
	
	public void addProcessVariable(String name, Object value, String type) {

		if (processVariables==null)
			processVariables = new HashMap<String, VariableValue>();
		this.getProcessVariables().put(name, new VariableValue(value, type));
	}
	
	public void addProcessVariable(String name, Object value, String type, Map<String, String> valueInfo) {

		if (processVariables==null)
			processVariables = new HashMap<String, VariableValue>();
		this.getProcessVariables().put(name, new VariableWithInfoValue(value, type, valueInfo));
	}
}
