package es.orchestration.wf.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import es.orchestration.wf.client.parameter.message.MessageParameter;

@FeignClient(name = "camundaRestClient",url = "${server.wf.url}")
public interface CamundaRest {

	@PostMapping(value = "/message")
	public void message(@RequestBody MessageParameter message);
}
