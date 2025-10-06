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
import java.net.URISyntaxException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import cmd.Main;

public class App {
	private static RandomAccessFile getIsoFromChooser(JFileChooser chooser, String title, Toolkit tk) throws IOException {
		RandomAccessFile iso = null;
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CD image (*.ISO)", "iso");
		chooser.addChoosableFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Open DBZ Sparking! HYPER ISO...");
		while (iso == null) {
			int result = chooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				iso = new RandomAccessFile(chooser.getSelectedFile(), "rw");
				if (!Main.isHyperIso(iso)) {
					iso = null;
					errorBeep(tk);
					JOptionPane.showMessageDialog(chooser, "Invalid DBZ Sparking! HYPER ISO "
					+ "(does not match original copy).", title, JOptionPane.ERROR_MESSAGE);
				}
			}
			else if (result == JFileChooser.CANCEL_OPTION) break;
		}
		return iso;
	}
	private static void errorBeep(Toolkit defToolkit) {
		Runnable runWinErrorSnd = (Runnable) defToolkit.getDesktopProperty("win.sound.exclamation");
		if (runWinErrorSnd!=null) runWinErrorSnd.run();
	}
	public static void setApp(String[] patchArgs, String[] patchDesc, String version) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			String[] patchTypes = {
				"Fix Game Crashes", "Fix Story Mode Typos", "Fix Vegeta Victory Quote",
				"Fix Pikkon Permanent Halo", "Fix Kaio-ken Goku Face", "Fix JP Buutenks Special Attack SFX", 
				"Fix Krillin Spirit Bomb" ,"Fix All"
			};
			String title = "DBZ Sparking! HYPER Patcher "+version;
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
			JButton patch = new JButton("Apply Patch");
			JFrame frame = new JFrame();
			JFileChooser chooser = new JFileChooser();
			JLabel iconLabel = new JLabel(""), patchLabel = new JLabel("Patch Type");
			JMenu greets = new JMenu("Greetings"), update = new JMenu("Update");
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
			//give menus their own listeners
			greets.addMenuListener(new MenuListener() {
				@Override
				public void menuSelected(MenuEvent e) {
					String[] users = {"Bεzzo", "Kyo MODS", "Valen2006", "VSVIDEOSFC", "Xeno Carmesin"};
					String[] links = {
						"https://bsky.app/profile/did:plc:4hh7ubpzmyq5raktz5sxc6ig",
						"https://www.youtube.com/@kyokomodsbt3","https://www.youtube.com/@valen2006",
						"https://www.youtube.com/@VSVIDEOSOFC","https://www.youtube.com/@XenoCarmesin"
					};
					String[] desc = {
						"providing the Sim Dragon crash fix",
						"playtesting and pointing out Krillin's Spirit Bomb damage","pointing out the Pikkon halo bug",
						"pointing out the Kaio-ken Goku face bug", "pointing out overlooked Dragon History typos"
					};
					Box mainBox = Box.createVerticalBox();
					Box[] userBoxes = new Box[users.length];
					for (int i=0; i<users.length; i++) {
						final int index = i;
						JLabel userLabel = new JLabel(users[i]);
						JLabel descLabel = new JLabel(" for " + desc[i]);
						descLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
						userLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
						userLabel.setForeground(new Color(0x74,0x31,0xdd));
						userLabel.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								try {
									Desktop.getDesktop().browse(new URI(links[index]));
								} catch (URISyntaxException e1) {
									e1.printStackTrace();
								} catch (IOException e1) {
									errorBeep(defToolkit);
									String msg = "Link could not be opened. Check your Internet connection!";
									JOptionPane.showMessageDialog(null, msg, title, JOptionPane.WARNING_MESSAGE);
								}
							}
						});
						userBoxes[i] = Box.createHorizontalBox();
						userBoxes[i].add(userLabel);
						userBoxes[i].add(descLabel);
						mainBox.add(userBoxes[i]);
					}
					ImageIcon iconSmall = new ImageIcon(img.getScaledInstance(256, 128, Image.SCALE_SMOOTH));
					JOptionPane.showMessageDialog(null, mainBox, title, JOptionPane.INFORMATION_MESSAGE, iconSmall);
				}
				@Override
				public void menuDeselected(MenuEvent e) {}
				@Override
				public void menuCanceled(MenuEvent e) {}
			});
			update.addMenuListener(new MenuListener() {
				@Override
				public void menuSelected(MenuEvent e) {
					String link = "https://github.com/ViveTheModder/dbzs-hyper-patcher/releases";
					try {
						Desktop.getDesktop().browse(new URI(link));
						frame.dispose();
					} catch (IOException e1) {
						errorBeep(defToolkit);
						String msg = "Tool could not be updated. Check your Internet connection!";
						JOptionPane.showMessageDialog(null, msg, title, JOptionPane.WARNING_MESSAGE);
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				@Override
				public void menuDeselected(MenuEvent e) {}
				@Override
				public void menuCanceled(MenuEvent e) {}
			});
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
			patch.setToolTipText("<html>Patch Types are chronologically ordered, but please make sure "
			+ "to apply all of them<br>at once, to an untouched (or already patched) DBZ Sparking! HYPER copy.</html>");
			//properly display background color on Windows 10 or higher
			patch.setContentAreaFilled(false);
			patch.setOpaque(true);
			//change JComboBox tooltip text based on selected item
			patchBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int index = patchBox.getSelectedIndex();
					patchBox.setToolTipText("<html>"+patchDesc[index].replaceAll("\n", "<br>")+"</html>");
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
							long start = System.currentTimeMillis();
							Main.applyPatch(iso, patchArgs[patchBox.getSelectedIndex()]);
							long end = System.currentTimeMillis();
							String message = String.format("ISO patched in %.3f seconds!", (end-start)/1000.0);
							defToolkit.beep();
							JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			//add components
			menuBar.add(greets);
			menuBar.add(update);
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
			frame.setLocationRelativeTo(null);
			frame.setJMenuBar(menuBar);
			frame.setSize(768, 512);
			frame.setTitle(title);
			frame.setVisible(true);
		} catch (HeadlessException e) {
			String os = System.getProperty("os.name");
			System.out.println("ERROR: This OS ("+os+") does not support the Swing library.\n"
			+ "Run the program from the command line instead. Use -h for help.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}