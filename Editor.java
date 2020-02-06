
/*Java class to create a text editor UI*/
import javax.swing.*;
import java.io.*;
import java.awt.FileDialog;
import java.awt.event.*;
import java.awt.print.PrinterException;

class Editor extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextArea t; // Text component
	private JFrame f; // Frame
	
	// Constructor
	public Editor() {
		f = new JFrame("Secure Text Editor"); // Create a frame
		f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		t = new JTextArea(); // Text component
		t.setLineWrap(true);
		t.setWrapStyleWord(true);
		JMenuBar mb = new JMenuBar(); // Create a menubar
		JMenu m1 = new JMenu("File"); // Create a menu for menu
		// Create menu items
		JMenuItem mi1 = new JMenuItem("New");
		JMenuItem mi2 = new JMenuItem("Open");
		JMenuItem mi3 = new JMenuItem("Save w/o Encryption");
		JMenuItem mi4 = new JMenuItem("Save w/ AES");
		JMenuItem mi5 = new JMenuItem("Save w/ Vigenere");
		JMenuItem mi6 = new JMenuItem("Print");
		JMenuItem mc = new JMenuItem("Close");
		// Add action listener
		mi1.addActionListener(this);
		mi2.addActionListener(this);
		mi3.addActionListener(this);
		mi4.addActionListener(this);
		mi5.addActionListener(this);
		mi6.addActionListener(this);
		mc.addActionListener(this);
		// Add menu items
		m1.add(mi1);
		m1.add(mi2);
		m1.add(mi3);
		m1.add(mi4);
		m1.add(mi5);
		m1.add(mi6);
		m1.add(mc);
		// Create amenu for menu
		JMenu m2 = new JMenu("Edit");
		// Create menu items
		JMenuItem mi7 = new JMenuItem("Cut");
		JMenuItem mi8 = new JMenuItem("Copy");
		JMenuItem mi9 = new JMenuItem("Paste");
		// Add action listener
		mi7.addActionListener(this);
		mi8.addActionListener(this);
		mi9.addActionListener(this);
		// Add menu items
		m2.add(mi7);
		m2.add(mi8);
		m2.add(mi9);

		// Add menus to menu bar & set bar & window size
		mb.add(m1);
		mb.add(m2);
		f.setJMenuBar(mb);
		f.add(t);
		f.setSize(1280, 720);
		f.setVisible(true);
	}

	/* Action listener method: Receive & execute commands from buttons */
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand(); // Get command from buttons

		// 1.New file button: Reset text area & title
		if (s.equals("New")) {
			t.setText("");
			f.setTitle("Secure Text Editor");
		}
		// 2.Open file button: Load file via FileDialog
		else if (s.equals("Open"))
			loadFile();
		// 3.Normal save file button: Save file as .txt
		else if (s.equals("Save w/o Encryption"))
			saveFile("txt", t.getText());
		// 4.AES save file button: Save file as .txta for AES encryption
		else if (s.equals("Save w/ AES"))
			saveFile("txta", encryptText("txta", t.getText()));
		// 5.Vigenere save file button: Save file as .txtv for Vigenere encryption
		else if (s.equals("Save w/ Vigenere"))
			saveFile("txtv", encryptText("txtv", t.getText()));
		// 6.Print file button: Print the current text in text area
		else if (s.equals("Print")) {
			try {
				t.print();
			} catch (PrinterException e1) {
				e1.printStackTrace();
			}
		} else if (s.equals("Cut")) // 7.Cut text button
			t.cut();
		else if (s.equals("Copy")) // 8.Copy text button
			t.copy();
		else if (s.equals("Paste")) // 9.Paste text button
			t.paste();
		else if (s.equals("Close")) // 10.Close program button
			System.exit(0);
	}

	/* Method to load file with FileDialog & decrypt according to file type */
	public void loadFile() {
		// Create a FileDialog object
		FileDialog fdOpenFile = new FileDialog(f, "Open file", FileDialog.LOAD);
		fdOpenFile.setResizable(true);
		fdOpenFile.setVisible(true);

		// Get file name & directory, then load the text with BufferedReader
		String fileName = fdOpenFile.getFile(); // Get the file name
		String fileDirectory = fdOpenFile.getDirectory() + fileName; // Get the file's directory
		FileReader reader = null;
		try {
			reader = new FileReader(fileDirectory);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		BufferedReader bReader = new BufferedReader(reader);

		// Try to read from the file one line at a time.
		StringBuffer textBuffer = new StringBuffer();
		try {
			String textLine = bReader.readLine();
			while (textLine != null) {
				textBuffer.append(textLine + '\n');
				textLine = bReader.readLine();
			}
			bReader.close();
			reader.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		}
		String fileContent = textBuffer.toString().trim(); // Trimming out EOF
		System.out.println(fileName + "\n\n" + fileContent);

		// Update the frame's title & textbox contents
		String[] words = fileName.split("\\.");
		if (words[1].equals("txta") || words[1].equals("txtv"))
			t.setText(decryptText(words[1], fileContent));
		else
			t.setText(fileContent);
		f.setTitle("Secure Text Editor: " + fileName);
	}

	/* Method to save file (after encryption or without encryption) */
	public void saveFile(String type, String text) {
		// Create a FileDialog object
		FileDialog fdSaveFile = new FileDialog(f, "Save file as", FileDialog.SAVE);
		fdSaveFile.setResizable(true);
		fdSaveFile.setVisible(true);

		// Get & set file name to the selected file in fdSaveFile
		String fileName = fdSaveFile.getFile();
		fdSaveFile.setFile(fileName);

		// File stream for writing file
		FileWriter fileCharStream = null;
		try {
			File f = new File(fdSaveFile.getDirectory() + "\\" + fileName + "." + type);
			fileCharStream = new FileWriter(f); // May throw IOException
			fileCharStream.write(text); // Use file output stream
		} catch (IOException excpt) {
			excpt.printStackTrace();
		} finally {
			try {
				fileCharStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Update title to saved file's name
		f.setTitle("Secure Text Editor: " + fileName + "." + type);
	}

	/* Method to decrypt text according to file type & algorithm */
	public String decryptText(String type, String text) {
		String decryptedText = "", key = "";

		// Get key using InputDialog & decrypt according to algorithm
		if (type.equals("txta")) {
			key = JOptionPane.showInputDialog(f, "AES Encryption detected\nEnter key:");
			System.out.println(key);
			decryptedText = AES.decrypt(text, key);
			return decryptedText;
		} else if (type.equals("txtv")) {
			key = JOptionPane.showInputDialog(f, "Vigenere Encryption detected\nEnter key:");
			System.out.println(key);
			decryptedText = VigenereCipher.decrypt(text, key);
			return decryptedText;
		}
		return null;
	}

	/* Method to encrypt text according to file type & algorithm */
	public String encryptText(String type, String text) {
		String encryptedText = "", key = "";

		// Get key using InputDialog & encrypt according to algorithm
		if (type.equals("txta")) {
			key = JOptionPane.showInputDialog(f, "AES Encryption selected\nEnter key:");
			System.out.println(key);
			encryptedText = AES.encrypt(text, key);
			return encryptedText;
		} else if (type.equals("txtv")) {
			key = JOptionPane.showInputDialog(f, "Vigenere Encryption selected\nEnter key:");
			System.out.println(key);
			encryptedText = VigenereCipher.encrypt(text, key);
			return encryptedText;
		}
		return null;
	}

	/* Main class */
	public static void main(String args[]) {
		Editor e = new Editor();
	}
}
