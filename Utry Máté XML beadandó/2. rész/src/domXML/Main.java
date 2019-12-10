package domXML;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {
	private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	private static final String INPUT_FILE_PATH = "src/minta.xml";
	private static final String OUTPUT_FILE_PATH = "src/modositott.xml";


	public static void main(String[] args) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		documentBuilderFactory.setValidating(true);
		documentBuilderFactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
		
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			//set ErrorHandler:
			//documentBuilder.setErrorHandler(new BeadandoErrorHandler());
			Document document = documentBuilder.parse(new File(INPUT_FILE_PATH));
			
			BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
			Scanner scanner = new Scanner(System.in);

			while(true) {
				System.out.println("Valassz egy menupontot: ");
				System.out.println("\b 1: Uj megrendelo hozzaadas");
				System.out.println("\b 2: Uj felhasznalo hozzaadas");
				System.out.println("\b 3: Letezo felhasznalo modositas");
				System.out.println("\b 4: XML kiiras (fileba/konzolra)");
				System.out.println("\b 5: EXIT");

				int valasztas = Integer.parseInt(scanner.next());
				switch(valasztas) {
				case 1:
					Megrendelo megrendelo = new Megrendelo();
					System.out.println("Add meg a megrendelo nevet: ");
					megrendelo.setNev(buf.readLine());
					System.out.println("Add meg a telefonszamat (06 nelkul): ");
					megrendelo.setTelefon(scanner.nextInt());
					System.out.println("Add meg a megrendelohoz tartozo felhasznalot (az o fkod-jat fogja megkapni)\nCsak letezo felhasznalohoz rendelheto hozza megrendelo!: ");
					megrendelo.setFelhasznalo(buf.readLine());
					System.out.println("Add meg a megrendelo kodot (ez lesz az mkod-ja): ");
					megrendelo.setMkod(buf.readLine());
					addMegrendelo(document, megrendelo);
					break;
				case 2:
					Felhasznalo felhasznalo = new Felhasznalo();
					System.out.println("Add meg a felhasznalo nevet: ");
					felhasznalo.setNev(buf.readLine());
					System.out.println("Most pedig a felhasznalo felhasznalonevet: ");
					felhasznalo.setFelhasznalonev(buf.readLine());
					addFelhasznalo(document, felhasznalo);
					break;
				case 3:
					Felhasznalo oldFelhasznalo = new Felhasznalo();
					System.out.println("Add meg az eredeti nevet: ");
					oldFelhasznalo.setNev(buf.readLine());
					System.out.println("Add meg az ehhez tartozo felhasznalonevet: ");
					oldFelhasznalo.setFelhasznalonev(buf.readLine());
					Felhasznalo newFelhasznalo = new Felhasznalo();
					System.out.println("Add meg a regi ("+oldFelhasznalo.getNev()+") felhasznalo uj nevet: ");
					newFelhasznalo.setNev(buf.readLine());
					System.out.println("Add meg az uj felhasznalonevet: ");
					newFelhasznalo.setFelhasznalonev(buf.readLine());
					felhasznaloModosit(document, oldFelhasznalo, newFelhasznalo);
					break;
				case 4:
					System.out.println("Hova ohajtod kiirni?");
					System.out.println("\b 1: Fajlba.");
					System.out.println("\b 2: Konzolra.");
					
					int choose = Integer.parseInt(scanner.next());
					switch(choose) {
					case 1:
						printDocument(document, new File(OUTPUT_FILE_PATH));
						System.out.println("Az XML kiirva a "+ OUTPUT_FILE_PATH +" helyre.\n");
						break;
					case 2:
						printDocument(document);
						System.out.println("\nA konzolra kiirt XML fent lathato\n");
						break;
					}
					break;
				default:
					break;
				}
				if (valasztas==5) {
					System.out.println("\tA program befejezodott!");
					break;
				}
			}
			buf.close();
			scanner.close();
			
		//catch 5_autok DOMapp
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	//1: Uj megrendelo hozzaadas
	public static void addMegrendelo(Document document, Megrendelo megrendelo) {
		
		Integer fkod = felhasznaloExistsIndex(document,String.valueOf(megrendelo.getFelhasznalo()));
		if (fkod == -1) {
			System.out.println(String.valueOf(megrendelo.getFelhasznalo())+" nevu felhasznalo nem letezik!\n");
			return;
		}
		System.out.println(String.valueOf(megrendelo.getNev())+" nevu megrendelo hozzaadva!\n");
		
		Node megrendelokNode = document.getElementsByTagName("megrendelok").item(0);

		Element megrendeloElement = document.createElement("megrendelo");
		megrendelokNode.appendChild(megrendeloElement);

		Attr felhasznaloAttribute = document.createAttribute("fkod");
		felhasznaloAttribute.setNodeValue(Integer.toString(fkod));
		megrendeloElement.setAttributeNode(felhasznaloAttribute);

		Attr mkodAttribute = document.createAttribute("mkod");
		mkodAttribute.setNodeValue(String.valueOf(megrendelo.getMkod()));
		megrendeloElement.setAttributeNode(mkodAttribute);

		Element cimElement = document.createElement("nev");
		cimElement.setTextContent(String.valueOf(megrendelo.getNev()));
		megrendeloElement.appendChild(cimElement);
		
		Element telefonElement = document.createElement("telefon");
		telefonElement.setTextContent(String.valueOf(megrendelo.getTelefon()));
		megrendeloElement.appendChild(telefonElement);
	}
	
	//2: Uj felhasznalo hozzaadas
	public static void addFelhasznalo(Document document, Felhasznalo felhasznalo) {
		System.out.println(String.valueOf(felhasznalo.getNev())+" nevu felhasznalo hozzaadva!\n");
		Node felhasznalokNode = document.getElementsByTagName("felhasznalok").item(0);
		
		NodeList felhasznaloNodes = felhasznalokNode.getChildNodes();
		
		Integer newFkod = 0;
		for (int i = 0; i < felhasznaloNodes.getLength(); i++) {
			Node felhasznaloNode = felhasznaloNodes.item(i);
			if (felhasznaloNode.getNodeName().equals("felhasznalo")) {
				newFkod++;
			}
		}

		Element felhasznaloElement = document.createElement("felhasznalo");
		felhasznalokNode.appendChild(felhasznaloElement);

		Attr fkodAttribute = document.createAttribute("fkod");
		fkodAttribute.setNodeValue(Integer.toString(newFkod));
		felhasznaloElement.setAttributeNode(fkodAttribute);

		Element nevElement = document.createElement("nev");
		nevElement.setTextContent(String.valueOf(felhasznalo.getNev()));
		felhasznaloElement.appendChild(nevElement);
		
		Element fnevElement = document.createElement("felhasznalonev");
		fnevElement.setTextContent(String.valueOf(felhasznalo.getFelhasznalonev()));
		felhasznaloElement.appendChild(fnevElement);
	}
	
	//3: Letezo felhasznalo modositas
	public static void felhasznaloModosit(Document document, Felhasznalo oldFelhasznalo, Felhasznalo felhasznalo) {
		
		Node felhasznaloNode = getFelhasznaloByNev(document, String.valueOf(oldFelhasznalo.getNev()));
		if ( felhasznaloNode == null) {
			System.out.println("A "+'"'+String.valueOf(oldFelhasznalo.getNev())+'"'+" nevu felhasznalo nem letezik!\n");
			return;
		}
		System.out.println(String.valueOf(oldFelhasznalo.getNev())+" nevu felhasznalo modositva!\n");
		
		NodeList felhasznaloChildNodes = felhasznaloNode.getChildNodes();
		for (int j = 0; j < felhasznaloChildNodes.getLength(); j++) {
			Node felhasznaloChildNode = felhasznaloChildNodes.item(j);
			
			if (felhasznaloChildNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) felhasznaloChildNode;
				//Atiras az XML-ben:
				if ("nev".equals(eElement.getNodeName())) {
					if (String.valueOf(oldFelhasznalo.getNev()).equals(eElement.getTextContent())) {
						eElement.setTextContent(String.valueOf(felhasznalo.getNev()));
					}
				}
				if ("felhasznalonev".equals(eElement.getNodeName())) {
					if (String.valueOf(oldFelhasznalo.getFelhasznalonev()).equals(eElement.getTextContent())) {
						eElement.setTextContent(String.valueOf(felhasznalo.getFelhasznalonev()));
					}	
				}
			}
		}
	}

	//4: XML kiiras - 6_auto_ember
	public static void printDocument(Document document) {
		printDocument(document, null);
	}

	public static void printDocument(Document document, File outputFile) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			DOMSource source = new DOMSource(document);
			StreamResult result;
			
			if (outputFile == null) {
				result = new StreamResult(System.out);
			} else {
				result = new StreamResult(outputFile);
			}

			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	//Letezo felhasznalo keresese, amit modositani akarunk
	public static Node getFelhasznaloByNev(Document document, String nev) {
		Node felhasznalokNode = document.getElementsByTagName("felhasznalok").item(0);
		NodeList felhasznaloNodeList = felhasznalokNode.getChildNodes();
		
		Node felhasznaloModosit = null;
		
		for (int i = 0; i < felhasznaloNodeList.getLength(); i++) {
			Node felhasznaloNode = felhasznaloNodeList.item(i);
			if (felhasznaloNode.getNodeName().equals("felhasznalo")) {
				NodeList felhasznaloChildNodes = felhasznaloNode.getChildNodes();
				for (int j = 0; j < felhasznaloChildNodes.getLength(); j++) {
					Node felhasznaloChildNode = felhasznaloChildNodes.item(j);
					
					if (felhasznaloChildNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) felhasznaloChildNode;
						
						if ("nev".equals(eElement.getNodeName())) {
							if (String.valueOf(nev).equals(eElement.getTextContent())) {
								felhasznaloModosit = felhasznaloNode;
							}
						}
					}	
				}
			}
		}
		return felhasznaloModosit;
	}
	
	//Uj megrendelot csak letezo felhasznalohoz adhatunk hozza
	public static Integer felhasznaloExistsIndex(Document document, String felhasznalo) {
		List<String> felhasznalok = new ArrayList<>();
		Node felhasznalokNode = document.getElementsByTagName("felhasznalok").item(0);
		NodeList felhasznaloNodeList = felhasznalokNode.getChildNodes();

		for (int i = 0; i < felhasznaloNodeList.getLength(); i++) {
			Node felhasznaloNode = felhasznaloNodeList.item(i);
			if (felhasznaloNode.getNodeName().equals("felhasznalo")) {
				NodeList felhasznaloChildNodes = felhasznaloNode.getChildNodes();
				for (int j = 0; j < felhasznaloChildNodes.getLength(); j++) {
					Node felhasznaloChildNode = felhasznaloChildNodes.item(j);
					if (felhasznaloChildNode.getNodeName().equals("nev")) {
						felhasznalok.add(felhasznaloChildNode.getTextContent());
					}
				}
			}
		}
		return felhasznalok.indexOf(felhasznalo);
	}
}