/**
 * Mule Anypoint Template
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.integration;

import java.io.InputStream;
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
import org.mule.construct.Flow;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.transformer.types.DataTypeFactory;

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
	private Flow mainFlow;

	private List<OrganizationReferenceWWSType> organizationsToDeleteFromWDAY = new ArrayList<OrganizationReferenceWWSType>();

	@Before
	public void setUp() throws Exception {
		muleContext.getRegistry().lookupObject(BatchManager.class).cancelAllRunningInstances();

		dissolveOrganizationFromWdayFlow = getSubFlow("dissolveOrganizationFromWdayFlow");
		dissolveOrganizationFromWdayFlow.initialise();

		retieveOrganizationFromWdayFlow = getSubFlow("retieveOrganizationFromWdayFlow");
		retieveOrganizationFromWdayFlow.initialise();
		
		mainFlow =  (Flow) muleContext.getRegistry().lookupObject("callBatchFlow");
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

		final MuleEvent testEvent = getTestEvent(null, mainFlow);
		testEvent.getMessage().setPayload(xmlPayload, DataTypeFactory.create(InputStream.class, "application/xml"));
		mainFlow.process(testEvent);
		
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

	@SuppressWarnings("unchecked")
	protected OrganizationReferenceWWSType invokeRetrieveFlow(SubflowInterceptingChainLifecycleWrapper flow,Map<String, Object> payload) throws Exception {
		
		MuleEvent event = flow.process(getTestEvent(payload,MessageExchangePattern.REQUEST_RESPONSE));
		List<Object> resultList = (List<Object>) event.getMessage().getPayload();

		if (resultList.size() > 0) {
			return (OrganizationReferenceWWSType) resultList.get(0);
		} else {
			return null;
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
