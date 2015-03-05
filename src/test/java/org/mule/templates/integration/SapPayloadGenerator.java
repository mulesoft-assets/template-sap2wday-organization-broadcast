/**
 * Mule Anypoint Template
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.integration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SapPayloadGenerator {
	private static final String ORG_ID_XPATH = "//E1PLOGI/E1PITYP/E1P1000/OBJID";
	private static final String ORG_NAME_XPATH = "//E1PLOGI/E1PITYP/E1P1000/STEXT";
	private static final String ORG_CODE_XPATH = "//E1PLOGI/E1PITYP/E1P1000/SHORT";
	private static final String ORG_LANG_XPATH = "//E1PLOGI/E1PITYP/E1P1000/LANGU_ISO";
	
	private XPath xpath;
	private Document doc;

	private List<String> uniqueOrgNameList = new ArrayList<String>();
	private List<String> uniqueOrgCodeList = new ArrayList<String>();
	private List<String> idList = new ArrayList<String>();

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		String xml = loadFile("./src/test/resources/hrmd_aba01_new_org.xml");
		SapPayloadGenerator generator = new SapPayloadGenerator(xml);
		System.out.println(generator.generateXML());
	}

	private static String loadFile(String filePath) throws IOException {
		InputStream in = new FileInputStream(filePath);
		return IOUtils.toString(in);
	}

	private static Document buildDocument(String xml) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

		return doc;
	}

	public SapPayloadGenerator(String xmlFile) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		doc = buildDocument(xmlFile);
		xpath = XPathFactory.newInstance().newXPath();
	}

	/**
	 * Generates an SAP XML based on an example XML provided. The returned XML
	 * has its respective IDs created in a unique way for this particular run.
	 * 
	 * @return
	 */
	public String generateXML() {
		uniqueOrgNameList.clear();

		try {
			return generateUniqueFields();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getUniqueOrgNameList() {
		return uniqueOrgNameList;
	}

	public List<String> getUniqueOrgCodeList() {
		return uniqueOrgCodeList;
	}

	public List<String> getIdList() {
		return idList;
	}

	private String generateUniqueFields() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException,	TransformerException {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		NodeList nodeList = (NodeList) xpath.compile(ORG_NAME_XPATH).evaluate(doc, javax.xml.xpath.XPathConstants.NODESET);
		NodeList langNodeList = (NodeList) xpath.compile(ORG_LANG_XPATH).evaluate(doc, javax.xml.xpath.XPathConstants.NODESET);		
		makeNamesUnique(nodeList,langNodeList);
		nodeList = (NodeList) xpath.compile(ORG_CODE_XPATH).evaluate(doc, javax.xml.xpath.XPathConstants.NODESET);
		makeCodesUnique(nodeList);
		nodeList = (NodeList) xpath.compile(ORG_ID_XPATH).evaluate(doc, javax.xml.xpath.XPathConstants.NODESET);
		getIds(nodeList);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.transform(new DOMSource(doc), new StreamResult(os));

		return new String(os.toByteArray());

	}

	private void makeNamesUnique(NodeList nodeList, NodeList langNodeList) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		String timeStamp = Long.valueOf(new Date().getTime()).toString();
		String waterMark = "_" + timeStamp;

		int index = 0;
		while (index < nodeList.getLength()) {
			//In this template, we are migrating just organizations in EN language
			if (langNodeList.item(index).getTextContent().equals("EN")) {
				Node node = nodeList.item(index);
				String uniqueName = node.getTextContent() + waterMark;
				node.setTextContent(uniqueName);
				uniqueOrgNameList.add(uniqueName);
			}
			index++;
		}
	}
	
	private void getIds(NodeList nodeList) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		int index = 0;
		while (index < nodeList.getLength()) {
			Node node = nodeList.item(index++);
			String id = node.getTextContent();
			idList.add(id);
		}
	}
	
	private void makeCodesUnique(NodeList nodeList) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		String timeStamp = Long.valueOf(new Date().getTime()).toString();
		String waterMark = "_" + timeStamp;

		int index = 0;
		while (index < nodeList.getLength()) {
			Node node = nodeList.item(index);
			String uniqueCode = node.getTextContent() + waterMark;
			node.setTextContent(uniqueCode);
			index++;

			uniqueOrgCodeList.add(uniqueCode);
		}
	}
}
