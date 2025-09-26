package gui;
//DBZ Sparking! HYPER Patcher by ViveTheJoestar
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import cmd.Main;

public class App {
	private static RandomAccessFile getIsoFromChooser(JFileChooser chooser, String title, Toolkit defToolkit) throws IOException {
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
					errorBeep(defToolkit);
					JOptionPane.showMessageDialog(chooser, "Invalid DBZ Sparking! HYPER ISO "
					+ "(does not match original copy).", title, JOptionPane.ERROR_MESSAGE);
				}
			}
			else if (result == JFileChooser.CANCEL_OPTION) break;
		}
		return iso;
	}
	private static void errorBeep(Toolkit defToolkit)
	{
		Runnable runWinErrorSnd = (Runnable) defToolkit.getDesktopProperty("win.sound.exclamation");
		if (runWinErrorSnd!=null) runWinErrorSnd.run();
	}
	public static void setApp(String[] patchArgs, String[] patchDesc) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			String[] patchTypes = {
				"Fix Game Crashes", "Fix Story Mode Typos", "Fix Vegeta Victory Quote", "Fix All"
			};
			String title = "DBZ Sparking! HYPER Patcher";
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
			//set component properties
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			iconLabel.setIcon(icon);
			patchBox.setBackground(new Color(201, 136, 211));
			patchBox.setOpaque(true);
			patchBox.setFont(tahoma);
			patchBox.setToolTipText(patchDesc[0]);
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
			patch.setToolTipText("<html>Patch Types have been ordered from most to least important, but "
			+ "please<br> apply all of them at once, to an untouched DBZ Sparking! HYPER copy.</html>");
			//properly display background color on Windows 10 or higher
			patch.setContentAreaFilled(false);
			patch.setOpaque(true);
			//change JComboBox tooltip text based on selected item
			patchBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int index = patchBox.getSelectedIndex();
					patchBox.setToolTipText(patchDesc[index]);
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
			frame.setSize(768, 512);
			frame.setTitle(title);
			frame.setVisible(true);
		} catch (ClassNotFoundException | InstantiationException 
			| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
}