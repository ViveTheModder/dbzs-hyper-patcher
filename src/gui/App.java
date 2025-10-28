package gui;
//DBZ Sparking! HYPER Patcher by ViveTheJoestar
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import cmd.Main;
import cmd.TranslatedText;

public class App {
	static String[] text;
	//solution taken from Stephen C's answer in stackoverflow (https://stackoverflow.com/a/1402762)
	private static boolean isInternetAvailable(URI uri) {
		try {
			URL url = uri.toURL();
			URLConnection urlc = url.openConnection();
			urlc.connect();
			urlc.getInputStream().close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	private static RandomAccessFile getIsoFromChooser(JFileChooser chooser, Toolkit tk)
	throws IOException {
		RandomAccessFile iso = null;
		FileNameExtensionFilter filter = new FileNameExtensionFilter(text[23] + " (*.ISO)", "iso");
		chooser.addChoosableFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(filter);
		chooser.setDialogTitle(text[24]);
		while (iso == null) {
			int result = chooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				iso = new RandomAccessFile(chooser.getSelectedFile(), "rw");
				if (!Main.isHyperIso(iso)) {
					iso = null;
					errorBeep(tk);
					JOptionPane.showMessageDialog(chooser, text[25], text[49].replace(": ", ""), 
					JOptionPane.ERROR_MESSAGE);
				}
			}
			else if (result == JFileChooser.CANCEL_OPTION) break;
		}
		return iso;
	}
	private static void changeLanguage(JComboBox<String> cb, JButton pb, JFrame f, JLabel pl, 
	JMenu[] menus, String[] langs, String abbr, String ver, TranslatedText tt, int selIndex) 
	throws IOException {
		String[] patchTypes = new String[13];
		String[] translatedLangs = tt.getLangs(abbr);
		System.arraycopy(text, 26, patchTypes, 0, 7);
		int[] patchIdx = {53, 59, 65, 74, 77, 33};
		for (int i=7; i<patchTypes.length; i++) patchTypes[i] = text[patchIdx[i-7]];
		cb.setModel(new DefaultComboBoxModel<String>(patchTypes));
		cb.setSelectedIndex(selIndex); //preserve currently selected index
		cb.setToolTipText(null); //disable tooltip until a patch type is selected
		pb.setText(text[34]);
		pb.setToolTipText(text[45]);
		pl.setText(text[35]);
		int[] menuIdx = {67, 52, 62}, helpItemIdx = {37, 61, 36};
		menus[0].getItem(0).setText(text[68]);
		for (int i=0; i<menus.length; i++) menus[i].setText(text[menuIdx[i]]);
		for (int i=0; i<3; i++)	menus[2].getItem(i).setText(text[helpItemIdx[i]]);
		for (int i=0; i<TranslatedText.NUM_LANGS; i++)
			menus[1].getItem(i).setText(translatedLangs[i]);
		f.setTitle(text[0]+" "+ver);
	}
	static void errorBeep(Toolkit toolkit) {
		Runnable runWinErrorSnd = (Runnable) toolkit.getDesktopProperty("win.sound.exclamation");
		if (runWinErrorSnd!=null) runWinErrorSnd.run();
	}
	public static void setApp(String[] args, String ver, TranslatedText tt) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//workaround that assures these variables are NOT static
			final boolean[] res = new boolean[1];
			final int[] currPatchBoxIdx = new int[1];
			final Log[] log = new Log[1];
			final RandomAccessFile[] currIso = new RandomAccessFile[1];
			//look, it was either this, or passing the text array from Main as a param
			int[] patchDescIdx = {54, 58, 64, 73, 76, 22}, patchTypeIdx = {53, 59, 65, 74, 77, 33};
			text = tt.getText();
			String[] langs = tt.getLangs();
			String[] links = {
				"https://github.com/ViveTheModder/dbzs-hyper-patcher/releases",
				"https://dbzs-hyper.nekoweb.org"};
			String[] patchDesc = new String[args.length];
			String[] patchTypes = new String[args.length];
			System.arraycopy(text, 15, patchDesc, 0, 7);
			System.arraycopy(text, 26, patchTypes, 0, 7);
			for (int i=7; i<13; i++) {
				patchDesc[i] = text[patchDescIdx[i-7]];
				patchTypes[i] = text[patchTypeIdx[i-7]];
			}
			String title = text[0] + " " + ver;
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			//initialize components
			Color bgColor = new Color(109, 103, 202);
			GridBagConstraints gbc = new GridBagConstraints();
			Font tahoma = new Font("Tahoma", Font.PLAIN, 14);
			Font tahomaBold = new Font("Tahoma", Font.BOLD, 20);
			Image img = toolkit.getImage(ClassLoader.getSystemResource("img/hyper.png"));
			Image imgSmall = img.getScaledInstance(384, 192, Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(imgSmall);
			JComboBox<String> patchBox = new JComboBox<String>(patchTypes);
			JButton patch = new JButton(text[34]);
			JFrame frame = new JFrame();
			JFileChooser chooser = new JFileChooser();
			JLabel iconLabel = new JLabel(""), patchLbl = new JLabel(text[35]);
			JMenu[] menus = {
					new JMenu(text[67]), new JMenu(text[52]), new JMenu(text[62])
			};
			JMenuBar menuBar = new JMenuBar();
			JPanel panel = new JPanel(new GridBagLayout()) {
			    @Override //set gradient background
			    protected void paintComponent(Graphics g) {
			    	int height = getHeight();
			    	int width = getWidth();
			    	Graphics2D g2 = (Graphics2D) g.create();
			    	g2.setPaint(new GradientPaint(
			    		0, 0, new Color(51, 212, 184), 0, height, new Color(225, 108, 209))
			    	);
			    	g2.fillRect(0, 0, width, height);
			    	g2.dispose();
			    }
			};
			JMenuItem fileItem = new JMenuItem(text[68]);
			JMenuItem[] langItems = new JMenuItem[langs.length];
			JMenuItem[] helpItems = {
				new JMenuItem(text[37]), new JMenuItem(text[61]), new JMenuItem(text[36])
			};
			for (int i=0; i<langs.length; i++) langItems[i] = new JMenuItem(langs[i]);
			//give menu items their own listeners
			fileItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						RandomAccessFile iso = getIsoFromChooser(chooser, toolkit);
						if (iso != null) 
							frame.setTitle(title + " - " + chooser.getSelectedFile().getName());
						else frame.setTitle(title);
						currIso[0] = iso;
						log[0] = null;
						res[0] = false;
					} catch (IOException ex) {ex.printStackTrace();} 
				}
			});
			for (int i=0; i<2; i++) {
				final int index = i;
				helpItems[i].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							int textIdx = 44;
							if (index == 1) textIdx = 43;
							URI uri = new URI(links[index]);
							if (isInternetAvailable(uri)) Desktop.getDesktop().browse(uri);
							else {
								errorBeep(toolkit);
								JOptionPane.showMessageDialog(null, text[textIdx], text[71], 
								JOptionPane.WARNING_MESSAGE);
							}
						} catch (Exception e1) {e1.printStackTrace();}
					}
				});
			}
			helpItems[2].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String[] users = {
						"Bεzzo", "BrendaMarzipan", "Es", "Kyo MODS", "MetalFrieza3000",
						"Valen2006", "VSVIDEOSFC", "Xeno Carmesin", "Parmi"
					};
					String[] links = {
						"https://bsky.app/profile/did:plc:4hh7ubpzmyq5raktz5sxc6ig",
						"https://www.youtube.com/@BeeMarzipan",
						"https://www.youtube.com/channel/UCI6ZwJwlDVam6nUayFB9h7w",
						"https://www.youtube.com/@kyokomodsbt3", 
						"https://www.youtube.com/@MetalFreezer3000", 
						"https://www.youtube.com/@valen2006", 
						"https://www.youtube.com/@VSVIDEOSOFC",
						"https://www.youtube.com/@XenoCarmesin",
					};
					String[] desc = new String[users.length];
					System.arraycopy(text, 38, desc, 0, 5);
					int[] userDescIdx = {55, 56, 60, 78};
					for (int i=5; i<9; i++) desc[i] = text[userDescIdx[i-5]];
					Box mainBox = Box.createVerticalBox();
					Box[] userBoxes = new Box[users.length];
					for (int i=0; i<users.length; i++) {
						final int index = i;
						JLabel userLabel = new JLabel(users[i]);
						JLabel descLabel = new JLabel(" " + desc[i]);
						descLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
						userLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
						if (i != links.length) {
							userLabel.setForeground(new Color(0x74, 0x31, 0xdd));
							userLabel.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseClicked(MouseEvent e) {
									try {
										URI uri = new URI(links[index]);
										if (isInternetAvailable(uri)) 
											Desktop.getDesktop().browse(uri);
										else {
											errorBeep(toolkit);
											JOptionPane.showMessageDialog(null, text[43], text[71], 
											JOptionPane.WARNING_MESSAGE);
										}
									} catch (Exception e1) {e1.printStackTrace();}
								}
							});
						}
						else userLabel.setForeground(new Color(0xdd, 0x31, 0x74));
						userBoxes[i] = Box.createHorizontalBox();
						userBoxes[i].add(userLabel);
						userBoxes[i].add(descLabel);
						mainBox.add(userBoxes[i]);
					}
					ImageIcon iconSmall = new ImageIcon(img.getScaledInstance(256, 128,
					Image.SCALE_SMOOTH));
					JOptionPane.showMessageDialog(null, mainBox, text[36], 
					JOptionPane.INFORMATION_MESSAGE, iconSmall);
				}
			});
			for (int i=0; i<langs.length; i++) {
				final int index = i;
				langItems[i].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							String[] abbrs = TranslatedText.getAbbrs();
							TranslatedText ttNew = new TranslatedText(abbrs[index]);
							String[] newText = ttNew.getText();
							text = newText;
							changeLanguage(patchBox, patch, frame, patchLbl, 
							menus, langs, abbrs[index], ver, ttNew, currPatchBoxIdx[0]);
						} catch (IOException ex) {ex.printStackTrace();}
					}
				});
			}
			//set component properties
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			iconLabel.setIcon(icon);
			patchBox.setFont(tahoma);
			patchBox.setToolTipText("<html>"+patchDesc[0].replaceAll("\n", "<br>")+"</html>");
			((JLabel)patchBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
			patchLbl.setForeground(Color.WHITE);
			patchLbl.setFont(tahomaBold);
			patch.setBackground(bgColor);
			patch.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(68, 34, 39), 3, false), 
				BorderFactory.createLineBorder(bgColor, 6, false))
			);
			patch.setBorderPainted(true);
			patch.setForeground(Color.WHITE);
			patch.setFont(tahomaBold);
			patch.setToolTipText("<html>"+text[45]+"</html>");
			//properly display background color on Windows 10 or higher
			patch.setContentAreaFilled(false);
			patch.setOpaque(true);
			//change JComboBox tooltip text based on selected item
			patchBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int[] indexArray = {15, 16, 17, 18, 19, 20, 21, 54, 58, 64, 73, 76, 22};
					currPatchBoxIdx[0] = patchBox.getSelectedIndex();
					int index = indexArray[currPatchBoxIdx[0]];
					patchBox.setToolTipText("<html>" + text[index].replaceAll("\n", "<br>")
					+"</html>");
				}
			});
			//give button its action listener
			patch.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (currIso[0] != null) {
						chooser.setCurrentDirectory(chooser.getSelectedFile());
						int idx = patchBox.getSelectedIndex();
						if (res[0]) {
							errorBeep(toolkit);
							JOptionPane.showMessageDialog(null, text[70], 
							text[49].replace(": ", ""), JOptionPane.ERROR_MESSAGE);
						}
						else {
							if (log[0] == null) 
								log[0] = new Log(frame, img, currIso, title, args, toolkit, idx, res);
							else log[0].setBgWorker(currIso, title, args, toolkit, idx, res);	
						}
					}
					else {
						errorBeep(toolkit);
						JOptionPane.showMessageDialog(null, text[69], 
						text[49].replace(": ", ""), JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			//add components
			menus[0].add(fileItem);
			for (int i=0; i<3; i++) {
				menuBar.add(menus[i]);
				menus[2].add(helpItems[i]);
			}
			for (int i=0; i<langs.length; i++) menus[1].add(langItems[i]);
			panel.add(iconLabel,gbc);
			panel.add(patchLbl,gbc);
			panel.add(new JLabel(" "),gbc);
			panel.add(patchBox,gbc);
			panel.add(new JLabel(" "),gbc);
			panel.add(new JLabel(" "),gbc);
			panel.add(patch,gbc);
			frame.add(panel);
			//set frame properties
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			frame.setIconImage(img);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setJMenuBar(menuBar);
			frame.setSize(768, 512);
			frame.setTitle(title);
			frame.setVisible(true);
		} catch (HeadlessException e) {
			System.out.println(text[49] + text[47].replace("[os]", System.getProperty("os.name")));
		} catch (Exception e) {e.printStackTrace();}
	}
}