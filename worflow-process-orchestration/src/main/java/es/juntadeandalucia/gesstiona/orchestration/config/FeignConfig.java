package es.juntadeandalucia.gesstiona.orchestration.config;

import feign.Logger;

public class FeignConfig {
	
	Logger.Level feignLoggerLevel(){
		return Logger.Level.FULL;
	}
}