package io.github.jonhshepard.samaritanj.connect;

import io.github.jonhshepard.samaritanj.Samaritan;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.Map;

/**
 * @author JonhSHEPARD
 */
public class PostConnection {

	private final String USER_AGENT = "Mozilla/5.0";
	private final String PANDORA_URL = "https://www.pandorabots.com/pandora/talk-xml";

	private URL sendTo;
	private String[] dataToSend;

	public PostConnection() throws URISyntaxException, MalformedURLException {
		sendTo = new URI(PANDORA_URL).toURL();
	}

	public void setData(Map<String, String> data) throws UnsupportedEncodingException {
		this.dataToSend = new String[data.size()];
		int i = 0;
		for (String s : data.keySet()) {
			this.dataToSend[i] = s + "=" + URLEncoder.encode(data.get(s), "UTF-8");
			i++;
		}
	}

	public ResponseObject send() throws IOException {
		if (dataToSend == null) throw new IOException();

		HttpsURLConnection con = (HttpsURLConnection) sendTo.openConnection();

		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("charset", "utf-8");

		// Send post request
		con.setDoOutput(true);

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		String param = "";
		for (String s : dataToSend) {
			if (param.length() > 0) param += "&";
			param += s;
		}
		wr.writeBytes(param);

		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		if (Samaritan.isDebug()) {
			System.out.println("-------- START DEBUG PostConnection --------");
			System.out.println("Sending 'POST' request to URL : " + sendTo);
			System.out.println("Response Code : " + responseCode);
		}

		InputStream input = con.getErrorStream();
		if (input != null) {
			BufferedReader inErr = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			String inputLineErr;
			StringBuilder responseErr = new StringBuilder();
			while ((inputLineErr = inErr.readLine()) != null) {
				responseErr.append(inputLineErr);
			}
			System.out.println(responseErr.toString());
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		if (Samaritan.isDebug()) {
			System.out.println("Response Data : " + response.toString());
			System.out.println("-------- END DEBUG PostConnection --------");
		}

		return new ResponseObject(responseCode, response.toString());
	}
}
