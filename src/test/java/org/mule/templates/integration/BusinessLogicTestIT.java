/**
 * Mule Anypoint Template
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.transport.NullPayload;

import com.mulesoft.module.batch.api.BatchManager;

/**
 * The objective of this class is to validate the correct behavior of the flows
 * for this Anypoint Template that make calls to external systems.
 * 
 */
public class BusinessLogicTestIT extends AbstractTemplateTestCase {
	private static final String TEST_MAT_MASTER_FILE = "./src/test/resources/mat_master_new.xml";

	private SubflowInterceptingChainLifecycleWrapper retrieveProductFromSFDCFlow;

	private List<Map<String, Object>> productsToDeleteFromSFDC = new ArrayList<Map<String, Object>>();

	@Before
	public void setUp() throws Exception {
		muleContext.getRegistry().lookupObject(BatchManager.class).cancelAllRunningInstances();

		retrieveProductFromSFDCFlow = getSubFlow("retrieveProductFromSFDCFlow");
		retrieveProductFromSFDCFlow.initialise();
	}

	@After
	public void tearDown() throws Exception {
		deleteTestDataFromSandBox();
	}

	@Test
	public void testMainFlow() throws Exception {
		String originalXML = getFileString(TEST_MAT_MASTER_FILE);
		SapPayloadGenerator generator = new SapPayloadGenerator(originalXML);
		generator.setTemplateName(TEMPLATE_NAME);
		String xmlPayload = generator.generateXML();

		runFlow("callBatchFlow", xmlPayload);

		generator.getUniqueIdList();
		System.out.println("DONE");
		Thread.sleep(5000);
		for (String id : generator.getUniqueIdList()) {
			Map<String, Object> payload = new HashMap<String, Object>();
			payload.put("sap_external_id__c", id);
			Map<String, Object> result = invokeRetrieveFlow(retrieveProductFromSFDCFlow, payload);
			Assert.assertNotNull("The SAP Material with id " + id + " should have been sync", result);
			productsToDeleteFromSFDC.add(result);
		}

	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> invokeRetrieveFlow(SubflowInterceptingChainLifecycleWrapper flow, Map<String, Object> payload)
			throws Exception {
		MuleEvent event = flow.process(getTestEvent(payload, MessageExchangePattern.REQUEST_RESPONSE));
		Object resultPayload = event.getMessage().getPayload();

		if (resultPayload instanceof NullPayload) {
			return null;
		} else {
			return (Map<String, Object>) resultPayload;
		}
	}

	private void deleteTestDataFromSandBox() throws MuleException, Exception {
		deleteTestProductsFromSFDC(productsToDeleteFromSFDC);
	}

	protected void deleteTestProductsFromSFDC(List<Map<String, Object>> createdProductsInSFDC) throws InitialisationException,
			MuleException, Exception {

		SubflowInterceptingChainLifecycleWrapper deleteProductFromSFDCFlow = getSubFlow("deleteProductFromSFDCFlow");
		deleteProductFromSFDCFlow.initialise();

		deleteTestEntityFromSandBox(deleteProductFromSFDCFlow, createdProductsInSFDC);
	}

	protected void deleteTestEntityFromSandBox(SubflowInterceptingChainLifecycleWrapper deleteFlow, List<Map<String, Object>> entitities)
			throws MuleException, Exception {
		List<String> idList = new ArrayList<String>();
		for (Map<String, Object> c : entitities) {
			idList.add(c.get("Id").toString());
		}
		deleteFlow.process(getTestEvent(idList, MessageExchangePattern.REQUEST_RESPONSE));
	}
}
