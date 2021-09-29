package es.juntadeandalucia.gesstiona.orchestration.event.mapper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class FlowMapper {

	private Map<String,String> mapper;
	
	
	public FlowMapper() {
		mapper = new HashMap<String,String>();
		mapper.put("createInvoiceTask", "serviceA-topic");
		mapper.put("archiveInvoiceTask", "serviceA-topic");
		mapper.put("noArchiveInvoiceTask", "serviceA-topic");
	}
	
	
	public String getServiceByTask(String activityId) {
		return mapper.get(activityId);
	}
}
