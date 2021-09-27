package es.service.event.bpm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskFactory {
	
	@Autowired
	private CreateInvoiceTask createInvoiceTask;
	
	@Autowired
	private DefaultTask defaultTask;
	
	public IProcedureBPM getTask(String nameTask) {

		if(nameTask.equalsIgnoreCase("createInvoiceTask")) {
			return createInvoiceTask;
		}
		else {
			return defaultTask;
		}
	}

}
