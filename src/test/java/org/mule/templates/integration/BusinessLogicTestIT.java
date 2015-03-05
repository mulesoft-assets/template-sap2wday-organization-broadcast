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
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.transport.NullPayload;

import com.mulesoft.module.batch.api.BatchManager;
import com.workday.hr.OrganizationReferenceWWSType;

/**
 * The objective of this class is to validate the correct behavior of the flows
 * for this Anypoint Template that make calls to external systems.
 * 
 */
public class BusinessLogicTestIT extends AbstractTemplateTestCase {
	private static final String TEST_MAT_MASTER_FILE = "./src/test/resources/hrmd_aba01_new_org.xml";

	private SubflowInterceptingChainLifecycleWrapper retieveOrganizationFromWdayFlow;
	private SubflowInterceptingChainLifecycleWrapper dissolveOrganizationFromWdayFlow;

	private List<OrganizationReferenceWWSType> organizationsToDeleteFromWDAY = new ArrayList<OrganizationReferenceWWSType>();

	@Before
	public void setUp() throws Exception {
		muleContext.getRegistry().lookupObject(BatchManager.class).cancelAllRunningInstances();

		dissolveOrganizationFromWdayFlow = getSubFlow("dissolveOrganizationFromWdayFlow");
		dissolveOrganizationFromWdayFlow.initialise();

		retieveOrganizationFromWdayFlow = getSubFlow("retieveOrganizationFromWdayFlow");
		retieveOrganizationFromWdayFlow.initialise();
	}

	@After
	public void tearDown() throws Exception {
		deleteTestDataFromSandBox();
	}

	@Test
	public void testMainFlow() throws Exception {
		String originalXML = getFileString(TEST_MAT_MASTER_FILE);
		SapPayloadGenerator generator = new SapPayloadGenerator(originalXML);
		String xmlPayload = generator.generateXML();

		runFlow("callBatchFlow", xmlPayload);

		System.out.println("DONE");
		Thread.sleep(15000);

		for (String name : generator.getUniqueOrgNameList()) {
			Map<String, Object> payload = new HashMap<String, Object>();
			payload.put("orgName", name);
			OrganizationReferenceWWSType result = invokeRetrieveFlow(retieveOrganizationFromWdayFlow, payload);
			Assert.assertNotNull("The SAP Organization with name " + name + " should have been sync", result);
			Assert.assertTrue("The name should be the same.", name.equals(result.getIntegrationIDReference().getDescriptor()));
			organizationsToDeleteFromWDAY.add(result);
		}
	}

	protected OrganizationReferenceWWSType invokeRetrieveFlow(SubflowInterceptingChainLifecycleWrapper flow,Map<String, Object> payload) throws Exception {
		
		MuleEvent event = flow.process(getTestEvent(payload,MessageExchangePattern.REQUEST_RESPONSE));
		Object resultPayload = event.getMessage().getPayload();

		if (resultPayload instanceof NullPayload) {
			return null;
		} else {
			return (OrganizationReferenceWWSType) resultPayload;
		}
	}

	private void deleteTestDataFromSandBox() throws MuleException, Exception {
		disolveTestEntitiesFromSandBox(organizationsToDeleteFromWDAY);
	}

	protected void disolveTestEntitiesFromSandBox(List<OrganizationReferenceWWSType> entitities) throws MuleException, Exception {

		for (OrganizationReferenceWWSType e : entitities) {
			dissolveOrganizationFromWdayFlow.process(getTestEvent(e,MessageExchangePattern.REQUEST_RESPONSE));
		}
	}
}
