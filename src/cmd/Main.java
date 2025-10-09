package cmd;
//DBZ Sparking! HYPER Patcher by ViveTheJoestar
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import gui.App;

public class Main {
	public static boolean isHyperIso(RandomAccessFile iso) throws IOException {
		if (iso.length()==2658074624L) {
			iso.seek(32787); //first instance of DBZS-HYPER string in the ISO
			byte[] isoNameBytes = new byte[53];
			iso.read(isoNameBytes);
			iso.seek(0);
			String isoName = new String(isoNameBytes).replace(" ", "");
			if (isoName.equals("DBZS-HYPER")) return true;
		};
		return false;
	}
	public static void applyPatch(RandomAccessFile iso, String patchArg, String[] text) throws IOException {
		int patchCnt = 0;
		boolean[] patchBools = {
			patchArg.equals("-fix-crash"), patchArg.equals("-fix-typos"), 
			patchArg.equals("-fix-vegeta"), patchArg.equals("-fix-pikkon"),
			patchArg.equals("-fix-goku"), patchArg.equals("-fix-buutenks"),
			patchArg.equals("-fix-krillin")
		};
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
			System.out.println(text[48]+text[1]+text[50]);
			//Dragon History patch
			iso.seek(497837637);
			iso.write(14); //fix Goku costume offset to point to Costume 2 rather than invalid float
			System.out.println(text[48]+text[2]+text[50]);
			//Great Ape detransformation patch
			int[] addrs = {614007579,614529563,615053467,615527195,616000283,616545947,617070747,617544475};
			for (int i=0; i<8; i++) {
				iso.seek(addrs[i]);
				iso.write(0xFF); //disable detransformation
			}
			System.out.println(text[48]+text[3]+text[50]);
			//Raditz patch (invalid blink texture index would lead to a crash on console, not PCSX2)
			int[] pos = {912004428,912004436}, texIdx = {8,0}; //blink texture index first, then scouter texture index (absent)
			for (int i=0; i<2; i++) {
				iso.seek(pos[i]);
				iso.write(texIdx[i]);
			}
			System.out.println(text[48]+text[51]+text[50]);
		} 
		if (patchBools[1]) {
			int[] pakPos = {489539584,489949184}, pakSizes = {408560,579040};
			//7 -> Dragon Ball Saga, 8 -> What-If Saga
			String[] pakNames = {"DragonHistory_7_US.cpak","DragonHistory_8_US.cpak"};
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
				if (txtPakIds[i]<10) start+="0";
				writePatchFile(iso, txtPakPos[i], txtPakSizes[i], start+txtPakIds[i]+".pak");
			}
			System.out.println(text[48]+text[4]+text[50]);
		}
		if (patchBools[2]) {
			//addresses to each of SS2 Vegeta's PAKs' "021_voice_speaker.dat" file (pointing at Super A#13)
			int[] addrs = {737438441,738077689,738720313,739352249,739980041,740619257,741262329,741926457};
			for (int i=0; i<8; i++) {
				iso.seek(addrs[i]);
				iso.write(0); //mistakenly set to FF, now corrected to 0 (default value, same as false)
				iso.write(0xFF); //mistakenly set to 0, now corrected to FF (victory quote is disabled)
			}
			System.out.println(text[48]+text[5]+text[50]);
		}
		if (patchBools[3]) {
			//addresses to Pikkon's PAKs' halo model part IDs (0x71 is changed to 0x6F)
			int[] addrs = {854976010,856180234};
			for (int i=0; i<2; i++) {
				iso.seek(addrs[i]);
				iso.write(0x6F);
			}
			System.out.println(text[48]+text[6]+text[50]);
		}
		if (patchBools[4]) {
			byte[] texId = new byte[6],
			faceTexId = {-112,-127,64,-35,101,1}, mouthTexId = {-80,1,64,85,-123,1},
			oldFaceTexId = {-120,-127,64,-35,69,1}, oldMouthTexId = {-88,1,64,85,101,1};
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
			System.out.println(text[48]+text[7]+text[50]);
		}
		if (patchBools[5]) {
			writePatchFile(iso, 1293348864, 338624, "Buu_A_Voice_JP.pak");
			System.out.println(text[48]+text[8]+text[50]);
		}
		if (patchBools[6]) {
			byte[] dmgBytes = {-122,106}, blockedDmgBytes = {-12,31};
			//addresses to Krillin's PAKs' "023_blast_param.dat" (pointing at the Ultimate Blast ID)
			int[] addrs = {692574012,693184028,693804700,694387036,695013180,695623196,696243868,696826204};
			for (int i=0; i<8; i++) {
				iso.seek(addrs[i]);
				iso.write(-43); //overwrite upper byte of Ultimate Blast ID int (722 -> 725)
				iso.seek(addrs[i]+368); //go to Ultimate Blast damage address
				iso.write(dmgBytes);
				iso.seek(addrs[i]+380); //go to blocked Ultimate Blast damage address
				iso.write(blockedDmgBytes);
			}
			System.out.println(text[48]+text[9]+text[50]);
		}
		if (patchCnt>0) iso.close(); //only write changes to ISO if at least one patch argument is valid
		else System.out.println(text[49]+text[10]);
	}
	public static void writePatchFile(RandomAccessFile iso, int pos, int fileSize, String fileName) throws IOException {
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
			//common variables
			Locale loc = Locale.getDefault(Locale.Category.FORMAT);
			String lang = loc.getLanguage(), version = "v1.6";
			String[] patchArgs = {
				"-fix-crash","-fix-typos","-fix-vegeta","-fix-pikkon",
				"-fix-goku","-fix-buutenks","-fix-krillin","-fix-all"
			};
			TranslatedText tt = new TranslatedText(lang);
			//terminal-only code
			if (args.length > 0) {
				String[] text = tt.getText();
				String[] patchDesc = new String[8];
				System.arraycopy(text, 15, patchDesc, 0, 8);
				if (args.length > 1) {
					File tmp = new File(args[0].replace("\"", ""));
					if (tmp.isFile() && tmp.getName().toLowerCase().endsWith(".iso")) {
						RandomAccessFile iso = new RandomAccessFile(tmp,"rw");
						if (isHyperIso(iso)) {
							long start = System.currentTimeMillis();
							applyPatch(iso,args[1],text);
							long end = System.currentTimeMillis();
							double time = (end-start)/1000.0;
							System.out.printf(text[11].replace("[time]", ""+time));
						}
						else System.out.println(text[49]+text[12].replace("[iso-name]", tmp.getName()));
					}
				} else if (args.length == 1) {
					if (args[0].equals("-h")) {
						System.out.println("=== "+text[0]+" "+version+" ===\n"+text[13]+"\n--- "+text[14]+"---");
						for (int i=0; i<patchArgs.length; i++)
							System.out.println(patchArgs[i]+": "+patchDesc[i]);
					}
				}
			} else App.setApp(patchArgs,version,tt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}