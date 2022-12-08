package reseau;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public abstract class Fenetre extends JFrame {

	private static final long serialVersionUID = -8249567620182176669L;
	private JTextArea jta1;
	private JTextArea jta2;
	protected JTextField jtf;

	private String texte_clair = "";
	private String texte_code = "";

	public Fenetre(String s) {
		super(s);
		this.setSize(500, 600);
		this.setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel p1 = new JPanel(new GridLayout(1, 2));
		JPanel p2 = new JPanel(new GridLayout(1, 2));

		this.jta1 = new JTextArea();
		this.jta2 = new JTextArea();
		this.jtf = new JTextField();

		this.jtf.setToolTipText("Entrez votre message.");

		this.jta1.setEditable(false);
		this.jta2.setEditable(false);
		this.jta1.setLineWrap(true);
		this.jta2.setLineWrap(true);

		JScrollPane s1 = new JScrollPane(jta1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollPane s2 = new JScrollPane(jta2, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		p1.add(s1);
		p1.add(s2);
		p2.add(jtf);

		this.add(p1, BorderLayout.CENTER);
		this.add(p2, BorderLayout.SOUTH);

		this.setVisible(true);
	}

	protected void ajouter_ligne(String s1, String s2) {
		this.texte_clair += s1 + "\n";
		this.texte_code += s2 + "\n";
		this.jta1.setText(this.texte_clair);
		this.jta2.setText(this.texte_code);
	}

	protected void stop() {
		System.exit(0);
	}

}
