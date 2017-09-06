package io.github.jonhshepard.samaritanj;


import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import io.github.jonhshepard.samaritanj.frames.SamaritanMainApp;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author JonhSHEPARD
 */
public class Samaritan {

	private static List<Arguments> arguments = new ArrayList<Arguments>();

	private ChatterBot bot;
	private ChatterBotSession bot_session;

	public static void main(String[] args) {
		for (String s : args) {
			if (s.toLowerCase().equalsIgnoreCase("-maximized") ||
					s.toLowerCase().equalsIgnoreCase("maximized")) {
				arguments.add(Arguments.MAXIMIZED);
			} else if (s.toLowerCase().equalsIgnoreCase("-debug") ||
					s.toLowerCase().equalsIgnoreCase("debug")) {
				arguments.add(Arguments.DEBUG);
			} else if ((s.toLowerCase().equalsIgnoreCase("-voice") ||
					s.toLowerCase().equalsIgnoreCase("voice"))) {
				arguments.add(Arguments.VOICE);
			}
		}
		new Samaritan();
	}

	private Samaritan() {

		try {
			ChatterBotFactory factory = new ChatterBotFactory();
			bot = factory.create(ChatterBotType.PANDORABOTS, "df385206ae377a2e");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		bot_session = bot.createSession();

		Font.loadFont(Samaritan.class.getResource("/font/magdacleanmono-bold.ttf").toExternalForm(), 10);

		new Thread(new Runnable() {
			public void run() {
				SamaritanMainApp.launch(arguments.contains(Arguments.MAXIMIZED), "Samaritan");
			}
		}).start();

		if (arguments.contains(Arguments.VOICE)) launchVoice();
		else launchText();
	}

	private void launchVoice() {
		/*Configuration configuration = new Configuration();

		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
		configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

		LiveSpeechRecognizer recognizer = null;
		try {
			recognizer = new LiveSpeechRecognizer(configuration);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		Scanner s = new Scanner(System.in);

		//TODO Do voice asking system

		while (true) {
			System.out.print("Press key to start");
			if (s.nextLine().equalsIgnoreCase("stop")) break;
			recognizer.startRecognition(true);
			System.out.print("Press key to stop");
			if (s.nextLine().equalsIgnoreCase("stop")) break;
			System.out.println(recognizer.getResult().getHypothesis());
		}*/
	}

	private void launchText() {
		Scanner s = new Scanner(System.in);
		while (true) {
			System.out.println("Text to ask: ");
			System.out.print(" > ");
			String line = s.nextLine();

			if (line.equalsIgnoreCase("stop") ||
					line.equalsIgnoreCase("quit") ||
					line.equalsIgnoreCase("exit") || line.length() == 0) {
				System.out.println("Exiting...");
				if (SamaritanMainApp.getInstance() != null)
					SamaritanMainApp.getInstance().executeText(BasicMessages.END.getMessage(), true);
				break;
			}
			if (SamaritanMainApp.getInstance() != null) {
				String st = line.toLowerCase();
				if (st.equalsIgnoreCase("author") ||
						st.equalsIgnoreCase("padre") ||
						st.equalsIgnoreCase("dad")) {
					SamaritanMainApp.getInstance().executeText(BasicMessages.AUTHOR.getMessage(), false);
				} else if (st.equalsIgnoreCase("you") ||
						st.equalsIgnoreCase("who are you") ||
						st.equalsIgnoreCase("t ki")) {
					SamaritanMainApp.getInstance().executeText(BasicMessages.PRESENTATION.getMessage(), false);
				} else {
					try {
						String reply = bot_session.think(Utils.removeAccent(line));
						if (arguments.contains(Arguments.DEBUG)) System.out.println(reply);
						SamaritanMainApp.getInstance().executeText(reply);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
