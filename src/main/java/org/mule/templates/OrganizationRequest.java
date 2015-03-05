/**
 * Mule Anypoint Template
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;

import com.workday.hr.ExternalIntegrationIDDataType;
import com.workday.hr.ExternalIntegrationIDReferenceDataType;
import com.workday.hr.IDType;
import com.workday.hr.OrganizationAddUpdateType;
import com.workday.hr.OrganizationDataType;
import com.workday.hr.OrganizationFindType;
import com.workday.hr.OrganizationReferenceType;
import com.workday.hr.OrganizationStructureDissolveDataType;
import com.workday.hr.OrganizationStructureDissolveType;
import com.workday.hr.OrganizationSubtypeReferenceDataType;
import com.workday.hr.OrganizationTypeReferenceDataType;
import com.workday.hr.OrganizationVisibilityReferenceDataType;

/**
 * Represents the Workday  requests for Organizations.
 * 
 */
public class OrganizationRequest {
	
	public static OrganizationAddUpdateType createAddUpdateOrganizationRequest(HashMap<String, String> data) throws Exception{
		final GregorianCalendar gcalendar = new GregorianCalendar();
		gcalendar.setTime(new Date());
			
		final OrganizationAddUpdateType orgAddUpdateType = new OrganizationAddUpdateType();
		final ExternalIntegrationIDDataType extIdReference = new ExternalIntegrationIDDataType();
		final OrganizationDataType orgDataType = new OrganizationDataType();	
		
		final IDType idType = new IDType();
		idType.setSystemID(data.get(System.getProperty("wday.system.id")));
		idType.setValue(data.get("id"));
		
		List<IDType> listOfIDs = new ArrayList<>();
		listOfIDs.add(idType);
		extIdReference.setID(listOfIDs);
		
		orgDataType.setEffectiveDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gcalendar));
		
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
		Date endDate = sdFormat.parse(data.get("endDate"));
		gcalendar.setTime(endDate);		
		orgDataType.setAvailabilityDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gcalendar));
		
		orgDataType.setOrganizationName(data.get("organizationName"));
		orgDataType.setOrganizationCode(data.get("organizationCode"));
		
		OrganizationTypeReferenceDataType orgTypeRefDataType = new OrganizationTypeReferenceDataType();
		orgTypeRefDataType.setOrganizationTypeName(data.get("organizationTypeName"));
		orgDataType.setOrganizationTypeReference(orgTypeRefDataType );
		
		OrganizationVisibilityReferenceDataType orgVisibilityRefDataType = new OrganizationVisibilityReferenceDataType();
		orgVisibilityRefDataType.setOrganizationVisibilityName(data.get("organizationVisibilityName"));
		orgDataType.setOrganizationVisibilityReference(orgVisibilityRefDataType);
		
		OrganizationSubtypeReferenceDataType orgSubtypeReferenceDataType = new OrganizationSubtypeReferenceDataType();
		orgSubtypeReferenceDataType.setOrganizationSubtypeName(data.get("organizationSubtypeName"));
		orgDataType.setOrganizationSubtypeReference(orgSubtypeReferenceDataType);
		
		orgDataType.setIntegrationIDData(extIdReference);
		orgAddUpdateType.setOrganizationData(orgDataType);	
		
		return orgAddUpdateType;
	}
	
	
	
	public static OrganizationFindType findOrganization(String name) throws Exception{
		final GregorianCalendar gcalendar = new GregorianCalendar();
		gcalendar.setTime(new Date());
			
		final OrganizationFindType orgFindType = new OrganizationFindType();		
		orgFindType.setOrganizationName(name);
		
		return orgFindType;
	}	
	
	public static OrganizationStructureDissolveType dissolveOrganization(ExternalIntegrationIDReferenceDataType extId) throws Exception{
		final GregorianCalendar gcalendar = new GregorianCalendar();
		gcalendar.setTime(new Date());
			
		final OrganizationStructureDissolveType orgDisssolveType = new OrganizationStructureDissolveType();		
		OrganizationStructureDissolveDataType orgStructureDissolveDatatype = new OrganizationStructureDissolveDataType();		
		orgStructureDissolveDatatype.setEffectiveDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gcalendar));
		orgDisssolveType.setOrganizationStructureDissolveData(orgStructureDissolveDatatype );
		OrganizationReferenceType orgRefType = new OrganizationReferenceType();
		orgRefType.setIntegrationIDReference(extId);
		orgDisssolveType.setOrganizationReferenceData(orgRefType );
		
		return orgDisssolveType;
	}	
}
