/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.camunda.example.orderhandling;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.variable.ClientValues;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
public class HandlerConfiguration {

	  protected static final Logger LOG = LoggerFactory.getLogger(HandlerConfiguration.class);
	  
	  // wait some random time between 15 and 60 seconds
	  private void doDelay() {
		  
		  double delay = 0;
	      while (0.25<delay || delay>1) {
	    	  delay = Math.random();
	      }
	      try {
			TimeUnit.SECONDS.sleep(Math.round(delay * 60));
	      } catch (InterruptedException e) {
			e.printStackTrace();
	      }
	  }

	  @Bean
	  @ExternalTaskSubscription("invoiceCreator")
	  public ExternalTaskHandler invoiceCreatorHandler() {
	    return (externalTask, externalTaskService) -> {

	      // wait few seconds
	      this.doDelay();
	      
	      // instantiate an invoice object
	      Invoice invoice = new Invoice("A123");

	      // create an object typed variable with the serialization format XML
	      ObjectValue invoiceValue = ClientValues
	          .objectValue(invoice)
	          .serializationDataFormat("application/xml")
	          .create();

	      // add the invoice object and its id to a map
	      Map<String, Object> variables = new HashMap<>();
	      
	      // select the scope of the variables
	      boolean isRandomSample = Math.random() <= 0.5;
	      if (isRandomSample) {
  	        variables.put("invoiceId", invoice.id);
	      } else {
  	        variables.put("invoiceId", null);
	      }
  	      variables.put("invoice", invoiceValue);
	      externalTaskService.complete(externalTask, variables);

	      LOG.info("The External Task {}, with {}, has been completed!", externalTask.getId(), isRandomSample);

	    };
	  }

	  @Bean
	  @ExternalTaskSubscription(
	      topicName = "invoiceArchiver",
	      autoOpen = false
	  )
	  public ExternalTaskHandler invoiceArchiverHandler() {

	      // wait few seconds
	      this.doDelay();

	      return (externalTask, externalTaskService) -> {
	      TypedValue typedInvoice = externalTask.getVariableTyped("invoice");
	      Invoice invoice = (Invoice) typedInvoice.getValue();
	      LOG.info("Invoice on process scope ARCHIVED: {}", invoice);
	      externalTaskService.complete(externalTask);
	      };
	   }

	  @Bean
	  @ExternalTaskSubscription(
	      topicName = "invoiceDiscarder",
	      autoOpen = false
	  )
	  public ExternalTaskHandler invoiceDiscarderHandler() {

	      // wait few seconds
	      this.doDelay();

	      return (externalTask, externalTaskService) -> {
	      TypedValue typedInvoice = externalTask.getVariableTyped("invoice");
	      Invoice invoice = (Invoice) typedInvoice.getValue();
	      LOG.info("Invoice on process scope DISCARDED: {}", invoice);
	      externalTaskService.complete(externalTask);
	      };
	   }

}
