package cmd;
//DBZ Sparking! HYPER Patcher by ViveTheJoestar
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class TranslatedText {
	public static final int NUM_LANGS = 5;
	private static final int NUM_LINES = 79;
	private static String[] abbrs = new String[NUM_LANGS]; //two-letter abbreviations per language
	private String[] langs = new String[NUM_LANGS];
	private String[] text = new String[NUM_LINES];
	
	public static String[] getAbbrs() {
		File[] langFiles = new File("lang/").listFiles(
			(dir, name) -> (name.startsWith("lang") && name.endsWith(".txt"))
		);
		for (int i=0; i<NUM_LANGS; i++) abbrs[i] = langFiles[i].getName().substring(5, 7);
		return abbrs;
	}
	public String[] getLangs() {
		return langs;
	}
	public String[] getLangs(String lang) throws IOException {
		return getLangsFromFile(new File("lang/lang_" + lang + ".txt"));
	}
	public String[] getText() {
		return text;
	}
	private String[] getTextFromFile(File txt) throws IOException {
		int lineIdx = 0;
		Scanner sc = new Scanner(txt, "UTF-8");
		String[] text = new String[NUM_LINES];
		while (sc.hasNextLine() && lineIdx < text.length) {
			String line = sc.nextLine();
			if (line.startsWith("\"")) {
				line = line.substring(1, line.length() - 1); //remove quotes
				line = line.replace("[br]", "\n"); //add new lines
				text[lineIdx++] = line;
			}			
		}
		sc.close();
		return text;
	}
	private String[] getLangsFromFile(File txt) throws IOException {
		int lineIdx = 0;
		Scanner sc = new Scanner(txt, "UTF-8");
		String[] langs = new String[NUM_LANGS];
		while (sc.hasNextLine() && lineIdx < langs.length) {
			String line = sc.nextLine();
			if (line.startsWith("\"")) {
				line = line.substring(1, line.length() - 1); //remove quotes
				langs[lineIdx++] = line;
			}
		}
		sc.close();
		return langs;
	}
	public TranslatedText(String lang) {
		try {
			//I originally wanted this inside the jar, but FileSystemNotFoundException... yeah
			File langFolder = new File("lang/");
			File[] langFiles = langFolder.listFiles(
				(dir, name) -> (name.startsWith("lang") && name.endsWith(".txt"))
			);
			File[] txtFiles = langFolder.listFiles(
				(dir, name) -> (name.startsWith("text") && name.endsWith(".txt"))
			);
			//binary search is used in case more languages are added in the future...
			int lang_en_idx = Arrays.binarySearch(langFiles, new File(langFolder.toString() +
			"/lang_en.txt"));
			for (int i=0; i<langFiles.length; i++) {
				if (langFiles[i].getName().endsWith(lang + ".txt")) {
					langs = getLangsFromFile(langFiles[i]);
					break;
				}
				 //set to lang_en.txt if language from locale is not supported
				else langs = getLangsFromFile(langFiles[lang_en_idx]);
			}
			for (File txt: txtFiles) {
				if (txt.getName().endsWith(lang + ".txt")) {
					text = getTextFromFile(txt);
					break;
				}
				//set to text_en.txt if language from locale is not supported
				else text = getTextFromFile(txtFiles[lang_en_idx]);
			}
		} catch (IOException e) {e.printStackTrace();}
	}
}