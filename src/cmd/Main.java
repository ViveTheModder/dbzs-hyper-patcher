package cmd;
//DBZ Sparking! HYPER Patcher by ViveTheJoestar
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JTextArea;
import gui.App;

public class Main {
	private static String getDateTime() {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS");
		return ldt.format(dtf);
	}
	public static boolean isHyperIso(RandomAccessFile iso) throws IOException {
		if (iso.length() == 2658074624L) {
			iso.seek(32787); //first instance of DBZS-HYPER string in the ISO
			byte[] isoNameBytes = new byte[53];
			iso.read(isoNameBytes);
			iso.seek(0);
			String isoName = new String(isoNameBytes).replace(" ", "");
			if (isoName.equals("DBZS-HYPER")) return true;
		};
		return false;
	}
	public static void displayResult(String result, JTextArea textarea, boolean cli) {
		if (cli) System.out.println(result);
		else textarea.setText(textarea.getText() + "[" + getDateTime() + "] " + result + "\n");
	}
	public static void main(String[] args) {
		try {
			int[] patchDescIdx = {54, 58, 64, 73, 76, 82, 85, 88, 22};
			//common variables
			Locale loc = Locale.getDefault(Locale.Category.FORMAT);
			String lang = loc.getLanguage(), version = "v2.4";
			String[] patchArgs = {
				"-fix-crash", "-fix-typos", "-fix-vegeta", "-fix-pikkon",
				"-fix-goku", "-fix-buutenks", "-fix-krillin", "-fix-sim", 
				"-fix-combos", "-fix-buu", "-fix-bgmlock", "-fix-cell", 
				"-fix-story", "-fix-skills", "-fix-absorb", "-fix-all"
			};
			TranslatedText tt = new TranslatedText(lang);
			//terminal-only code
			if (args.length > 0) {
				String[] text = tt.getText();
				String[] patchDesc = new String[patchArgs.length];
				System.arraycopy(text, 15, patchDesc, 0, 7);
				for (int i = 7; i < patchDesc.length; i++) patchDesc[i] = text[patchDescIdx[i-7]];
				if (args.length > 1) {
					File tmp = new File(args[0].replace("\"", ""));
					String tmpName = tmp.getName();
					if (tmp.isFile() && tmpName.toLowerCase().endsWith(".iso")) {
						RandomAccessFile iso = new RandomAccessFile(tmp, "rw");
						if (isHyperIso(iso)) {
							long start = System.currentTimeMillis();
							Patch.apply(iso, args[1], patchArgs, text, true, null);
							long end = System.currentTimeMillis();
							double time = (end - start) / 1000.0;
							System.out.printf(text[11].replace("[time]", "" + time));
						}
						else 
							System.out.println(text[49] + text[12].replace("[iso-name]", tmpName));
					}
				} else if (args.length == 1) {
					if (args[0].equals("-h")) {
						System.out.println("=== " + text[0] + " " + version + " ===\n" +
							text[13] + "\n--- " + text[14] + "---");
						for (int i = 0; i < patchArgs.length; i++)
							System.out.println(patchArgs[i] + ":\n" + patchDesc[i]);
					}
				}
			} else App.setApp(patchArgs, version, tt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}