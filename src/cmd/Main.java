package cmd;
//DBZ Sparking! HYPER Patcher by ViveTheJoestar
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.JTextArea;
import gui.App;

public class Main {
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
	public static boolean applyPatch(RandomAccessFile iso, String patchArg, String[] patchArgs,
	String[] text, boolean print, JTextArea ta) throws IOException {
		int patchCnt = 0;
		boolean[] patchBools = new boolean[patchArgs.length - 1];
		for (int i=0; i<patchBools.length; i++)
			patchBools[i] = patchArg.equals(patchArgs[i]);
		String result;
		//above patch bools are bound to be false if "fix-all" is used, so make all the bools true
		if (patchArg.equals("-fix-all")) {
			for (int i=0; i<patchBools.length; i++) patchBools[i] = true;
		}
		for (int i=0; i<patchBools.length; i++) {
			if (patchBools[i]) patchCnt++;
		}
		if (patchBools[0]) {
			//Sim Dragon patch
			writePatchFile(iso, 491374592, 3488160, "Sim_Dragon_US.cpak");
			result = text[48] + text[1] + text[50];
			if (print) System.out.println(result);
			else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
			//Dragon History patch
			iso.seek(497837637);
			iso.write(14); //fix Goku costume offset to point to Costume 2 rather than invalid float
			result = text[48] + text[2] + text[50];
			if (print) System.out.println(result);
			else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
			//Great Ape detransformation patch
			int[] addrs = {
				614007579, 614529563, 615053467, 615527195, 
				616000283, 616545947, 617070747, 617544475
			};
			for (int i=0; i<8; i++) {
				iso.seek(addrs[i]);
				iso.write(0xFF); //disable detransformation
			}
			result = text[48] + text[3] + text[50];
			if (print) System.out.println(result);
			else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
			//Raditz patch (invalid blink texture index would lead to a crash on console, not PCSX2)
			int[] pos = {912004428, 912004436};
			int[] texIdx = {8, 0}; //blink texture index first, then scouter texture index (absent)
			for (int i=0; i<2; i++) {
				iso.seek(pos[i]);
				iso.write(texIdx[i]);
			}
			result = text[48] + text[51] + text[50];
			if (print) System.out.println(result);
			else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
			//Spirit Bomb patch (corrects offset for the fire animation's EQUIPMENT01 bone)
			iso.seek(556445627);
			iso.write(66);
			result = text[48] + text[66] + text[50];
			if (print) System.out.println(result);
			else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
		} 
		if (patchBools[1]) {
			int[] pakPos = {489539584, 489949184}, pakSizes = {408560, 579040};
			//7 -> Dragon Ball Saga, 8 -> What-If Saga
			String[] pakNames = {"DragonHistory_7_US.cpak", "DragonHistory_8_US.cpak"};
			for (int i=0; i<2; i++) writePatchFile(iso, pakPos[i], pakSizes[i], pakNames[i]);
			int lineCnt=0;
			int[] txtPakIds = new int[29], txtPakPos = new int[29], txtPakSizes = new int[29];
			InputStream csvStream = Main.class.getResourceAsStream("/patch/txt-us-b-info.csv");
			Scanner sc = new Scanner(csvStream);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] lineArray = line.split(",");
				txtPakIds[lineCnt] = Integer.parseInt(lineArray[0]);
				txtPakPos[lineCnt] = Integer.parseInt(lineArray[1]);
				txtPakSizes[lineCnt] = Integer.parseInt(lineArray[2]);
				lineCnt++;
			}
			sc.close();
			for (int i=0; i<txtPakPos.length; i++) {
				String start = "TXT-US-B-";
				if (txtPakIds[i] < 10) start+="0";
				writePatchFile(iso, txtPakPos[i], txtPakSizes[i], start + txtPakIds[i] + ".pak");
			}
			result = text[48] + text[4] + text[50];
			if (print) System.out.println(result);
			else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
		}
		if (patchBools[2]) {
			//addresses to each SS2 Vegeta PAK's "021_voice_speaker.dat" file (pointing at Super 13)
			int[] addrs = {
				737438441, 738077689, 738720313, 739352249,
				739980041, 740619257, 741262329, 741926457
			};
			for (int i=0; i<8; i++) {
				iso.seek(addrs[i]);
				iso.write(0); //mistakenly set to FF, now corrected to 0 (disabled)
				iso.write(0xFF); //mistakenly set to 0, now corrected to FF (disabled)
			}
			result = text[48] + text[5] + text[50];
			if (print) System.out.println(result);
			else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
		}
		if (patchBools[3]) {
			//addresses to Pikkon's PAKs' halo model part IDs (0x71 is changed to 0x6F)
			int[] addrs = {854976010,856180234};
			for (int i=0; i<2; i++) {
				iso.seek(addrs[i]);
				iso.write(0x6F);
			}
			result = text[48] + text[6] + text[50];
			if (print) System.out.println(result);
			else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
		}
		if (patchBools[4]) {
			byte[] texId = new byte[6],
			faceTexId = {-112, -127, 64, -35, 101, 1}, mouthTexId = {-80, 1, 64, 85, -123, 1},
			oldFaceTexId = {-120, -127, 64, -35, 69, 1}, oldMouthTexId = {-88, 1, 64, 85, 101, 1};
			int selStart = 546988300, selEnd = 547120672;
			for (int pos=selStart; pos<selEnd; pos+=2) {
				iso.seek(pos);
				iso.read(texId);
				//overwrite old face texture ID with the valid ID
				if (Arrays.equals(texId, oldFaceTexId)) {
					iso.seek(pos);
					iso.write(faceTexId);
				}
				//overwrite old mouth texture ID with the valid ID
				if (Arrays.equals(texId, oldMouthTexId)) {
					iso.seek(pos);
					iso.write(mouthTexId);
				}
			}
			result = text[48] + text[7] + text[50];
			if (print) System.out.println(result);
			else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
		}
		if (patchBools[5]) {
			writePatchFile(iso, 1293348864, 338624, "Buu_A_Voice_JP.pak");
			result = text[48] + text[8] + text[50];
			if (print) System.out.println(result);
			else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
		}
		if (patchBools[6]) {
			byte[] dmgBytes = {-122, 106}, blockedDmgBytes = {-12, 31};
			//addresses to Krillin's PAKs' "023_blast_param.dat" (pointing at the Ultimate Blast ID)
			int[] addrs = {
				692574012, 693184028, 693804700, 694387036,
				695013180, 695623196, 696243868, 696826204
			};
			for (int i=0; i<8; i++) {
				iso.seek(addrs[i]);
				iso.write(-43); //overwrite upper byte of Ultimate Blast ID int (722 -> 725)
				iso.seek(addrs[i] + 368); //go to Ultimate Blast damage address
				iso.write(dmgBytes);
				iso.seek(addrs[i] + 380); //go to blocked Ultimate Blast damage address
				iso.write(blockedDmgBytes);
			}
			result = text[48] + text[9] + text[50];
			if (print) System.out.println(result);
			else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
		}
		if (patchBools[7]) {
			int[] addrs = {3290552, 3290676, 3290680, 3290684};
			int[] itemIds = {0x61, 0x62, 0x63, 0x7C};
			for (int i=0; i<4; i++) {
				iso.seek(addrs[i]);
				iso.write(itemIds[i]);
			}
			result = text[48] + text[1] + text[50];
			if (print) System.out.println(result);
			else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
		}
		if (patchBools[8]) {
			int[] addrs = {
				647025968, 647646864, 648225296, 648879952,
				649530640, 650149520, 650639312, 651294544, 658068176,
				889711952, 890363216, 891018576, 891669840
			};
			int blastComboVal = 3;
			for (int i=0; i<addrs.length; i++) {
				iso.seek(addrs[i] - 1);
				if (i > 8) blastComboVal = 0;
				else if (i == 8) {
					result = text[48] + text[57] + text[50];
					if (print) System.out.println(result);
					else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
				}
				for (int j=0; j<9; j++) iso.write(blastComboVal);
			}
			result = text[48] + text[79] + text[50];
			if (print) System.out.println(result);
			else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
		}
		if (patchBools[9]) {
			int[] addrs = {1066472262, 1067047750, 1067627334, 1068202822};
			for (int i=0; i<4; i++) {
				iso.seek(addrs[i]);
				iso.write(2); //set 3rd Rushing Technique to Blaster Wave (as it should be)
			}
			result = text[48] + text[63] + text[50];
			if (print) System.out.println(result);
			else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
		}
		if (patchBools[10]) {
			int pos = 2076122;
			byte[] asmInstrFooter = {0, 16}; //convert bnel to unconditional branch
			iso.seek(pos);
			iso.write(asmInstrFooter);
			result = text[48] + text[72];
			if (print) System.out.println(result);
			else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
		}
		if (patchBools[11]) {
			writePatchFile(iso, 1035132928, 762176, "Cell_0_anm.pak");
			int pos = 497756724;
			iso.seek(pos);
			iso.write(-111); //overwrite animation ID from 0x0601 to 0x9101 (401 in decimal)
			result = text[48] + text[75] + text[50];
			if (print) System.out.println(result);
			else ta.setText(ta.getText() + "[" + getPatchDateTime() + "] " + result + "\n");
		}
		//only write changes to ISO if at least one patch argument is valid
		if (patchCnt > 0) {
			iso.close();
			//there is no method to check if the ISO is closed, so I went from void to boolean
			return true;
		}
		else {
			System.out.println(text[49] + text[10]);
			return false;
		}
	}
	public static String getPatchDateTime() {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS");
		return ldt.format(dtf);
	}
	public static void writePatchFile(RandomAccessFile iso, int pos, int fileSize, String fileName)
	throws IOException {
		byte[] pakBytes = new byte[fileSize];
		InputStream stream = Main.class.getResourceAsStream("/patch/"+fileName);
		DataInputStream pakStream = new DataInputStream(stream);
		pakStream.readFully(pakBytes);
		pakStream.close();
		iso.seek(pos);
		iso.write(pakBytes);
	}
	public static void main(String[] args) {
		try {
			int[] patchDescIdx = {54, 58, 64, 73, 76, 22};
			//common variables
			Locale loc = Locale.getDefault(Locale.Category.FORMAT);
			String lang = loc.getLanguage(), version = "v2.2";
			String[] patchArgs = {
				"-fix-crash", "-fix-typos", "-fix-vegeta", "-fix-pikkon",
				"-fix-goku", "-fix-buutenks", "-fix-krillin", "-fix-sim", 
				"-fix-combos", "-fix-buu", "-fix-bgmlock", "-fix-cell", "-fix-all"
			};
			TranslatedText tt = new TranslatedText(lang);
			//terminal-only code
			if (args.length > 0) {
				String[] text = tt.getText();
				String[] patchDesc = new String[patchArgs.length];
				System.arraycopy(text, 15, patchDesc, 0, 7);
				for (int i=7; i<13; i++) patchDesc[i] = text[patchDescIdx[i-7]];
				if (args.length > 1) {
					File tmp = new File(args[0].replace("\"", ""));
					String tmpName = tmp.getName();
					if (tmp.isFile() && tmpName.toLowerCase().endsWith(".iso")) {
						RandomAccessFile iso = new RandomAccessFile(tmp, "rw");
						if (isHyperIso(iso)) {
							long start = System.currentTimeMillis();
							applyPatch(iso, args[1], patchArgs, text, true, null);
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
						for (int i=0; i<patchArgs.length; i++)
							System.out.println(patchArgs[i] + ":\n" + patchDesc[i]);
					}
				}
			} else App.setApp(patchArgs, version, tt);
		} catch (Exception e) {e.printStackTrace();}
	}
}