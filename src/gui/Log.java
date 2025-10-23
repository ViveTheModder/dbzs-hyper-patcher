package gui;
//DBZ Sparking! HYPER Patcher by ViveTheJoestar
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.RandomAccessFile;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import cmd.Main;

public class Log {
	private JTextArea textArea;	
	public Log(JFrame frame, Image img, RandomAccessFile[] iso, String title, 
	String[] args, Toolkit tk, int idx, boolean[] res) {
		//initialize components
		Box scrollBox = Box.createHorizontalBox();
		Font tahoma = new Font("Tahoma", Font.PLAIN, 20);
		Font tahomaBold = new Font("Tahoma", Font.BOLD, 30);
		JDialog dialog = new JDialog();
		JLabel label = new JLabel("HYPER LOG");
		textArea = new JTextArea(20, 20);
		JScrollPane scroll = new JScrollPane(textArea);
		JPanel panel = new JPanel();
		//set component properties
		label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		label.setForeground(new Color(199, 24, 24));
		label.setFont(tahomaBold);
		label.setHorizontalAlignment(JLabel.CENTER);
		panel.setBackground(new Color(153, 158, 185));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		scroll.setBorder(null);
		textArea.setBackground(new Color(27, 66, 142));
		textArea.setBorder(BorderFactory.createLineBorder(new Color(89, 86, 88), 4, false));
		textArea.setEditable(false);
		textArea.setForeground(new Color(69, 210, 177));
		textArea.setFont(tahoma);
		textArea.setLineWrap(true);
		//add components
		scrollBox.add(Box.createHorizontalGlue());
		scrollBox.add(scroll);
		scrollBox.add(Box.createHorizontalGlue());
		panel.add(Box.createVerticalGlue());
		panel.add(label);
		panel.add(new JLabel(" "));
		panel.add(scrollBox);
		panel.add(Box.createVerticalGlue());
		dialog.add(panel);
		//set dialog properties
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setIconImage(img);
		dialog.setSize(450, 800);
		dialog.setLocationRelativeTo(frame);
		dialog.setTitle(label.getText());
		dialog.setVisible(true);
		//set and execute background worker
		setBgWorker(iso, title, args, tk, idx, res);
	}
	public void setBgWorker(RandomAccessFile[] iso, String title, 
	String[] args, Toolkit tk, int idx, boolean[] res) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				textArea.setText("");
				long start = System.currentTimeMillis();
				res[0] = Main.applyPatch(iso[0], args[idx], args, App.text, false, textArea);
				long end = System.currentTimeMillis();
				double time = (end-start)/1000.0;
				if (res[0]) {
					tk.beep();
					JOptionPane.showMessageDialog(null, App.text[46].replace("[time]", ""+time), 
					title, JOptionPane.INFORMATION_MESSAGE);
				}
				return null;
			}
		};
		worker.execute();
	}
}