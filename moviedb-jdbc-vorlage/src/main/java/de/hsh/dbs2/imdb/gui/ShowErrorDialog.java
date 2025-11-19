package de.hsh.dbs2.imdb.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ShowErrorDialog extends JDialog {
	private static final long serialVersionUID = 1L;

    public ShowErrorDialog(Window owner, String msg, Exception e) {
		super(owner);
		
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));

		setTitle(msg);
		setSize(200, 200);
		setModal(true);

		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		GridBagConstraints gbc = new GridBagConstraints();

		// Festlegen, dass die GUI-Elemente die Gitterfelder in
		// waagerechter Richtung ausfuellen:
		gbc.fill = GridBagConstraints.BOTH;

		// Die Abstaende der einzelnen GUI-Elemente zu den gedachten
		// Gitterlinien festgelegen:
		gbc.insets = new Insets(2, 2, 2, 2);

		gbc.gridx = 0;
		gbc.gridy = 0;
        JTextArea txtError = new JTextArea(sw.toString());
		JScrollPane scroll = new JScrollPane(txtError);
		scroll.setPreferredSize(new Dimension(800, 400));
		layout.setConstraints(scroll, gbc);
		add(scroll);


		gbc.gridx = 0;
		gbc.gridy = 1;
        JButton btnOk = new JButton("OK");
		layout.setConstraints(btnOk, gbc);
		add(btnOk);
		
		btnOk.addActionListener(e1 -> setVisible(false));

		this.setResizable(false);
		this.setSize(new Dimension(800, 400));

		pack();
		
		setLocationRelativeTo(null);
		setVisible(true);
	}


}
