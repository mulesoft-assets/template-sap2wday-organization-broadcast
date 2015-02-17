/**
 * Mule Anypoint Template
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;

import com.arjuna.ats.internal.jdbc.drivers.modifiers.list;
import com.workday.hr.BusinessSiteReferenceDataType;
import com.workday.hr.EmployeeGetType;
import com.workday.hr.EmployeeReferenceType;
import com.workday.hr.ExternalIntegrationIDDataType;
import com.workday.hr.ExternalIntegrationIDReferenceDataType;
import com.workday.hr.IDType;
import com.workday.hr.OrganizationAddUpdateType;
import com.workday.hr.OrganizationDataType;
import com.workday.hr.OrganizationFindType;
import com.workday.hr.OrganizationGetType;
import com.workday.hr.OrganizationReferenceType;
import com.workday.hr.OrganizationStructureDissolveDataType;
import com.workday.hr.OrganizationStructureDissolveType;
import com.workday.hr.OrganizationSubtypeObjectIDType;
import com.workday.hr.OrganizationSubtypeObjectType;
import com.workday.hr.OrganizationSubtypeReferenceDataType;
import com.workday.hr.OrganizationTypeReferenceDataType;
import com.workday.hr.OrganizationVisibilityReferenceDataType;
import com.workday.hr.OrganizationWWSType;
import com.workday.hr.PrimaryBusinessSiteReferenceDataType;

/**
 * Represents the workday request for employee by external reference ID.
 * 
 */
public class WorkersRequest {
	
	public static OrganizationAddUpdateType createAddUpdateOrganizationRequest(String value, String systemId) throws Exception{
		final GregorianCalendar gcalendar = new GregorianCalendar();
		gcalendar.setTime(new Date());
			
		final OrganizationAddUpdateType orgAddUpdateType = new OrganizationAddUpdateType();
		final OrganizationReferenceType orgReferenceType = new OrganizationReferenceType();
		final ExternalIntegrationIDDataType extIdReference = new ExternalIntegrationIDDataType();
		final OrganizationDataType orgDataType = new OrganizationDataType();
		
//		final EmployeeGetType employeeGetType = new EmployeeGetType();
//		final EmployeeReferenceType employeeReferenceType = new EmployeeReferenceType();
//		final ExternalIntegrationIDReferenceDataType extIdReference = new ExternalIntegrationIDReferenceDataType();
		
		final IDType idType = new IDType();
		idType.setSystemID(systemId);
		idType.setValue(value);
		
		List<IDType> listOfIDs = new ArrayList<>();
		listOfIDs.add(idType);
		extIdReference.setID(listOfIDs);
//		employeeReferenceType.setIntegrationIDReference(extIdReference);
		//orgDataType.set //TODO .......
		orgDataType.setEffectiveDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gcalendar));
		orgDataType.setAvailabilityDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gcalendar));
		orgDataType.setOrganizationName("org name-changed-again");
		orgDataType.setOrganizationCode("ORG CODE");
		OrganizationTypeReferenceDataType orgTypeRefDataType = new OrganizationTypeReferenceDataType();
		orgTypeRefDataType.setOrganizationTypeName("Company");
		orgDataType.setOrganizationTypeReference(orgTypeRefDataType );
		OrganizationVisibilityReferenceDataType orgVisibilityRefDataType = new OrganizationVisibilityReferenceDataType();
		orgVisibilityRefDataType.setOrganizationVisibilityName("Everyone");
		orgDataType.setOrganizationVisibilityReference(orgVisibilityRefDataType);
		OrganizationSubtypeReferenceDataType orgSubtypeReferenceDataType = new OrganizationSubtypeReferenceDataType();
		orgSubtypeReferenceDataType.setOrganizationSubtypeName("Company");
		orgDataType.setOrganizationSubtypeReference(orgSubtypeReferenceDataType);
//		OrganizationSubtypeObjectType orgSubtype = new OrganizationSubtypeObjectType();
//		List<OrganizationSubtypeObjectIDType> list = new ArrayList<>();
//		OrganizationSubtypeObjectIDType orgSubtypeObjIDType = new OrganizationSubtypeObjectIDType();
//		orgSubtypeObjIDType.setType("Supervisory");
//		list.add(orgSubtypeObjIDType );
//		orgSubtype.setID(list);
		
		orgDataType.setIntegrationIDData(extIdReference);
		orgAddUpdateType.setOrganizationData(orgDataType);	
//		employeeGetType.setEmployeeReference(employeeReferenceType);
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
