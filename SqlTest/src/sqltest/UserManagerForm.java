package sqltest;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTable;

public class UserManagerForm extends JFrame implements ActionListener {
	private JTable table;
	private TableValues tv;
	public UserManagerForm(){
		tv=new TableValues();
		table=new JTable(tv);
		getContentPane().add(table,BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
