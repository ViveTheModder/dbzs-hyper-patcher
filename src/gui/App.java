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
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import cmd.Main;
import cmd.TranslatedText;

public class App {
	private static String[] text; //the only static variable in the program, and for good reason
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
	private static RandomAccessFile getIsoFromChooser(JFileChooser chooser, String title, Toolkit tk)
	throws IOException {
		RandomAccessFile iso = null;
		FileNameExtensionFilter filter = new FileNameExtensionFilter(text[23]+" (*.ISO)", "iso");
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
					JOptionPane.showMessageDialog(chooser, text[25], title, 
					JOptionPane.ERROR_MESSAGE);
				}
			}
			else if (result == JFileChooser.CANCEL_OPTION) break;
		}
		return iso;
	}
	private static void changeLanguage(JComboBox<String> cb, JButton pb, JFrame f, JLabel pl, 
	JMenu[] menus, String ver) {
		String[] patchTypes = new String[9];
		System.arraycopy(text, 26, patchTypes, 0, 7);
		patchTypes[7] = text[53];
		patchTypes[8] = text[33];
		cb.setModel(new DefaultComboBoxModel<String>(patchTypes));
		cb.setToolTipText(null); //disable tooltip until a patch type is selected
		pb.setText(text[34]);
		pl.setText(text[35]);
		int[] menuIdx = {36, 37, 52};
		for (int i=0; i<menus.length; i++) menus[i].setText(text[menuIdx[i]]);
		f.setTitle(text[0]+" "+ver);
	}
	private static void errorBeep(Toolkit defToolkit) {
		Runnable runWinErrorSnd = (Runnable) defToolkit.getDesktopProperty("win.sound.exclamation");
		if (runWinErrorSnd!=null) runWinErrorSnd.run();
	}
	public static void setApp(String[] patchArgs, String version, TranslatedText tt) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//look, it was either this, or passing the text array from Main as a param
			text = tt.getText();
			String[] langs = tt.getLangs();
			String[] patchDesc = new String[patchArgs.length];
			String[] patchTypes = new String[patchArgs.length];
			System.arraycopy(text, 15, patchDesc, 0, 7);
			patchDesc[7] = text[54];
			patchDesc[8] = text[22];
			System.arraycopy(text, 26, patchTypes, 0, 7);
			patchTypes[7] = text[53];
			patchTypes[8] = text[33];
			String title = text[0]+" "+version;
			Toolkit defToolkit = Toolkit.getDefaultToolkit();
			//initialize components
			Color bgColor = new Color(109, 103, 202);
			GridBagConstraints gbc = new GridBagConstraints();
			Font tahoma = new Font("Tahoma", Font.PLAIN, 14);
			Font tahomaBold = new Font("Tahoma", Font.BOLD, 20);
			Image img = defToolkit.getImage(ClassLoader.getSystemResource("img/hyper.png"));
			Image imgSmall = img.getScaledInstance(384, 192, Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(imgSmall);
			JComboBox<String> patchBox = new JComboBox<String>(patchTypes);
			JButton patch = new JButton(text[34]);
			JFrame frame = new JFrame();
			JFileChooser chooser = new JFileChooser();
			JLabel iconLabel = new JLabel(""), patchLabel = new JLabel(text[35]);
			JMenu[] menus = {new JMenu(text[36]), new JMenu(text[37]), new JMenu(text[52])};
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
			JMenuItem[] langItems = new JMenuItem[langs.length];
			for (int i=0; i<langs.length; i++) langItems[i] = new JMenuItem(langs[i]);
			//give menus their own listeners
			menus[0].addMenuListener(new MenuListener() {
				@Override
				public void menuSelected(MenuEvent e) {
					String[] users = {
						"Bεzzo", "Kyo MODS", "Valen2006", "VSVIDEOSFC", "Xeno Carmesin", "Es"
					};
					String[] links = {
						"https://bsky.app/profile/did:plc:4hh7ubpzmyq5raktz5sxc6ig",
						"https://www.youtube.com/@kyokomodsbt3",
						"https://www.youtube.com/@valen2006", "https://www.youtube.com/@VSVIDEOSOFC",
						"https://www.youtube.com/@XenoCarmesin",
						"https://www.youtube.com/channel/UCI6ZwJwlDVam6nUayFB9h7w"
					};
					String[] desc = new String[users.length];
					System.arraycopy(text, 38, desc, 0, 5);
					desc[5] = text[55];
					Box mainBox = Box.createVerticalBox();
					Box[] userBoxes = new Box[users.length];
					for (int i=0; i<users.length; i++) {
						final int index = i;
						JLabel userLabel = new JLabel(users[i]);
						JLabel descLabel = new JLabel(" " + desc[i]);
						descLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
						userLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
						userLabel.setForeground(new Color(0x74,0x31,0xdd));
						userLabel.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								try {
									URI uri = new URI(links[index]);
									if (isInternetAvailable(uri)) {
										Desktop.getDesktop().browse(uri);
										frame.dispose();
									}
									else {
										errorBeep(defToolkit);
										JOptionPane.showMessageDialog(null, text[43], title, 
										JOptionPane.WARNING_MESSAGE);
									}
								} catch (Exception e1) {e1.printStackTrace();}
							}
						});
						userBoxes[i] = Box.createHorizontalBox();
						userBoxes[i].add(userLabel);
						userBoxes[i].add(descLabel);
						mainBox.add(userBoxes[i]);
					}
					ImageIcon iconSmall = new ImageIcon(img.getScaledInstance(256, 128,
					Image.SCALE_SMOOTH));
					JOptionPane.showMessageDialog(null, mainBox, title, 
					JOptionPane.INFORMATION_MESSAGE, iconSmall);
				}
				@Override
				public void menuDeselected(MenuEvent e) {}
				@Override
				public void menuCanceled(MenuEvent e) {}
			});
			menus[1].addMenuListener(new MenuListener() {
				@Override
				public void menuSelected(MenuEvent e) {
					String link = "https://github.com/ViveTheModder/dbzs-hyper-patcher/releases";
					try {
						URI uri = new URI(link);
						if (isInternetAvailable(uri)) {
							Desktop.getDesktop().browse(uri);
							frame.dispose();
						}
						else {
							errorBeep(defToolkit);
							JOptionPane.showMessageDialog(null, text[44], title, 
							JOptionPane.WARNING_MESSAGE);
						}
					} catch (Exception e1) {e1.printStackTrace();}
				}
				@Override
				public void menuDeselected(MenuEvent e) {}
				@Override
				public void menuCanceled(MenuEvent e) {}
			});
			//give menu items their own listeners
			for (int i=0; i<langs.length; i++) {
				final int index = i;
				langItems[i].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String[] abbrs = TranslatedText.getAbbrs();
						String[] newText = new TranslatedText(abbrs[index]).getText();
						text = newText;
						changeLanguage(patchBox, patch, frame, patchLabel, menus, version);
					}
				});
			}
			//set component properties
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			iconLabel.setIcon(icon);
			patchBox.setFont(tahoma);
			patchBox.setToolTipText("<html>"+patchDesc[0].replaceAll("\n", "<br>")+"</html>");
			((JLabel)patchBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
			patchLabel.setForeground(Color.WHITE);
			patchLabel.setFont(tahomaBold);
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
					int[] indexArray = {15, 16, 17, 18, 19, 20, 21, 54, 22};
					int index = indexArray[patchBox.getSelectedIndex()];
					patchBox.setToolTipText("<html>"+text[index].replaceAll("\n", "<br>")
					+"</html>");
				}
			});
			//give button its action listener
			patch.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						RandomAccessFile iso = getIsoFromChooser(chooser, title, defToolkit);
						if (iso != null) {
							chooser.setCurrentDirectory(chooser.getSelectedFile());
							int patchIndex = patchBox.getSelectedIndex();
							long start = System.currentTimeMillis();
							Main.applyPatch(iso, patchArgs[patchIndex], patchArgs, text);
							long end = System.currentTimeMillis();
							double time = (end-start)/1000.0;
							defToolkit.beep();
							JOptionPane.showMessageDialog(null, text[46].replace("[time]", ""+time), 
							title, JOptionPane.INFORMATION_MESSAGE);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			//add components
			menuBar.add(menus[0]);
			menuBar.add(menus[1]);
			menuBar.add(menus[2]);
			for (int i=0; i<langs.length; i++) menus[2].add(langItems[i]);
			panel.add(iconLabel,gbc);
			panel.add(patchLabel,gbc);
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