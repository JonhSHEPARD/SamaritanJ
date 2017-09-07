package io.github.jonhshepard.samaritanj.bot;

import io.github.jonhshepard.samaritanj.Samaritan;
import io.github.jonhshepard.samaritanj.connect.PostConnection;
import io.github.jonhshepard.samaritanj.connect.ResponseObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JonhSHEPARD
 */
public class PandoraBot {

	private String botid;

	public PandoraBot(String botid) {
		this.botid = botid;
	}

	public String think(String input) {
		try {
			PostConnection conn = new PostConnection();

			Map<String, String> data = new HashMap<String, String>();
			data.put("botid", botid);
			data.put("input", input);
			conn.setData(data);

			ResponseObject responseObject = conn.send();

			if (responseObject.getCode() == 200) {
				String reply = responseXMLtoString(responseObject.getData());

				if (Samaritan.isDebug()) {
					System.out.println("-------- START DEBUG PandoraBotResponse --------");
					System.out.println("Reply: " + reply);
					System.out.println("-------- END DEBUG PandoraBotResponse --------");
				}
				return reply;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return "";
	}

	private String responseXMLtoString(String response) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new ByteArrayInputStream(response.getBytes()));

			Element e = document.getDocumentElement();

			NodeList list = e.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node n = list.item(i);

				if (n.getNodeName().equalsIgnoreCase("that")) {
					return n.getTextContent();
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}
}