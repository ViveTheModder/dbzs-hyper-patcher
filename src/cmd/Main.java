package cmd;
import java.io.DataInputStream;
//DBZ Sparking! HYPER Patcher by ViveTheJoestar
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
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
	public static void applyPatch(RandomAccessFile iso, String patchArg) throws IOException {
		int patchCnt = 0;
		boolean[] patchBools = {
			patchArg.equals("-fix-crash"), patchArg.equals("-fix-typos"), 
			patchArg.equals("-fix-vegeta"), patchArg.equals("-fix-pikkon"),
			patchArg.equals("-fix-goku")
		};
		//above patch bools are bound to be false if "fix-all" is used, so make all the bools true
		if (patchArg.equals("-fix-all")) {
			for (int i=0; i<patchBools.length; i++) patchBools[i] = true;
		}
		if (patchBools[0]) {
			patchCnt++;
			//Sim Dragon patch
			iso.seek(491374592);
			byte[] pakBytes = new byte[3488160];
			InputStream stream = Main.class.getResourceAsStream("/patch/Sim_Dragon_US.cpak");
			DataInputStream pakStream = new DataInputStream(stream);
			pakStream.readFully(pakBytes);
			pakStream.close();
			iso.write(pakBytes);
			System.out.println("SUCCESS: Sim Dragon is patched!");
			//Dragon History patch
			iso.seek(497837637);
			iso.write(14); //fix Goku costume offset to point to Costume 2 rather than invalid float
			System.out.println("SUCCESS: Dragon History is patched!");
			//Great Ape detransformation patch
			int[] addrs = {614007579,614529563,615053467,615527195,616000283,616545947,617070747,617544475};
			for (int i=0; i<8; i++) {
				iso.seek(addrs[i]);
				iso.write(0xFF); //disable detransformation
			}
			System.out.println("SUCCESS: Great Ape is patched!");
		} 
		if (patchBools[1]) {
			patchCnt++;
			int[] pakPos = {489539584,489949184}, pakSizes = {408560,579040};
			//7 -> Dragon Ball Saga, 8 -> What-If Saga
			String[] pakNames = {"DragonHistory_7_US.cpak","DragonHistory_8_US.cpak"};
			for (int i=0; i<2; i++) {
				byte[] pakBytes = new byte[pakSizes[i]];
				InputStream stream = Main.class.getResourceAsStream("/patch/"+pakNames[i]);
				DataInputStream pakStream = new DataInputStream(stream);
				pakStream.readFully(pakBytes);
				pakStream.close();
				iso.seek(pakPos[i]);
				iso.write(pakBytes);
			}
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
				byte[] pakBytes = new byte[txtPakSizes[i]];
				String start = "TXT-US-B-";
				if (txtPakIds[i]<10) start+="0";
				InputStream stream = Main.class.getResourceAsStream("/patch/"+start+txtPakIds[i]+".pak");
				DataInputStream pakStream = new DataInputStream(stream);
				pakStream.readFully(pakBytes);
				pakStream.close();
				iso.seek(txtPakPos[i]);
				iso.write(pakBytes);
			}
			System.out.println("SUCCESS: Dragon History typos are patched!");
		}
		if (patchBools[2]) {
			patchCnt++;
			//addresses to each of SS2 Vegeta's PAKs' "021_voice_speaker.dat" file (pointing at Super A#13)
			int[] addrs = {737438441,738077689,738720313,739352249,739980041,740619257,741262329,741926457};
			for (int i=0; i<8; i++) {
				iso.seek(addrs[i]);
				iso.write(0); //mistakenly set to FF, now corrected to 0 (default value, same as false)
				iso.write(0xFF); //mistakenly set to 0, now corrected to FF (victory quote is disabled)
			}
			System.out.println("SUCCESS: Super Saiyan 2 Vegeta is patched!");
		}
		if (patchBools[3]) {
			patchCnt++;
			//addresses to Pikkon's PAKs' halo model part IDs (0x71 is changed to 0x6F)
			int[] addrs = {854976010,856180234};
			for (int i=0; i<2; i++) {
				iso.seek(addrs[i]);
				iso.write(0x6F);
			}
			System.out.println("SUCCESS: Pikkon is patched!");
		}
		if (patchBools[4]) {
			patchCnt++;
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
			System.out.println("SUCCESS: Kaio-ken Goku is patched!");
		}
		//only close RAF (write changes to ISO) if at least one patch argument is valid
		if (patchCnt>0) iso.close();
		else System.out.println("ERROR: Invalid patch argument provided.");
	}
	public static void main(String[] args) {
		try {
			String version = "v1.3";
			String[] patchArgs = {"-fix-crash","-fix-typos","-fix-vegeta","-fix-pikkon","-fix-goku","-fix-all"};
			String[] patchDesc = {
				"Fixes Dragon History crash (which prevented \"The World's Strongest\" from being completed),"
				+ "\nSim Dragon crash (which would occur shortly after selecting a character),"
				+ "\nand Great Ape detransformation crash (by disabling it for all costumes).",
				"Fixes Dragon History subtitle errors (such as misspellings and incomplete sentences).",
				"Disables Super Saiyan 2 Vegeta's wrongly assigned victory quote against Super Android #13.",
				"Disables Pikkon's halo by default for his 2nd costume, as is the case for his 1st costume.",
				"Fixes Kaio-ken Goku's 3rd damaged costume's extra faces to point to the right texture.",
				"Applies all the patches listed above."
			};
			if (args.length>1) {
				File tmp = new File(args[0].replace("\"", ""));
				if (tmp.isFile() && tmp.getName().toLowerCase().endsWith(".iso")) {
					RandomAccessFile iso = new RandomAccessFile(tmp,"rw");
					if (isHyperIso(iso)) {
						long start = System.currentTimeMillis();
						applyPatch(iso,args[1]);
						long end = System.currentTimeMillis();
						System.out.printf("Time Elapsed: %.3f s",(end-start)/1000.0);
					}
					else System.out.println("ERROR: "+tmp.getName()+" is NOT a valid DBZ Sparking! HYPER ISO.");
				}
			} else if (args.length>0) {
				if (args[0].equals("-h")) {
					System.out.println("=== DBZ Sparking! HYPER Patcher "+version+" ===");
					System.out.println("Usage: java -jar dbzs-hyper-patcher.jar \"path/to/ISO\" [patch-arg]");
					System.out.println("--- Patch Arguments ---");
					for (int i=0; i<patchArgs.length; i++)
						System.out.println(patchArgs[i]+": "+patchDesc[i]);
				}
			} else App.setApp(patchArgs,patchDesc,version);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}