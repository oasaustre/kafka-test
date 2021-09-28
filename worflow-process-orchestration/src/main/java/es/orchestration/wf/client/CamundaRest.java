package es.orchestration.wf.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import es.orchestration.config.FeignConfig;
import es.orchestration.wf.client.parameter.message.MessageParameter;
import es.orchestration.wf.client.parameter.processdefinition.StartProcessParameter;

@FeignClient(name = "camundaRestClient",url = "${server.wf.url}",configuration = FeignConfig.class)
public interface CamundaRest {

	@PostMapping(value = "/message")
	public void message(@RequestBody MessageParameter message);
	
	@PostMapping(value = "/process-definition/key/{key}/start")
	public void startProcess(@PathVariable String key, @RequestBody StartProcessParameter param);
}
