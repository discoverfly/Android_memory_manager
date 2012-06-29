/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sqltest;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LoginForm extends JFrame implements ActionListener {

    private JLabel labTitle, labUsername, labPassword;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnSubmit, btnReset, btnRegister;

    public LoginForm() {
        JPanel jp1 = new JPanel();
        labTitle = new JLabel("登录窗口");
        jp1.add(labTitle);

        JPanel jp2 = new JPanel();
        jp2.setLayout(new GridLayout(2, 2));
        labUsername = new JLabel("登录账号：");
        labPassword = new JLabel("登录密码：");
        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        jp2.add(labUsername);
        jp2.add(txtUsername);
        jp2.add(labPassword);
        jp2.add(txtPassword);

        JPanel jp3 = new JPanel();
        btnSubmit = new JButton("登录");
        btnReset = new JButton("清空");
        btnRegister = new JButton("注册");

        btnSubmit.addActionListener(this);
        btnReset.addActionListener(this);
        btnRegister.addActionListener(this);

        jp3.add(btnSubmit);
        jp3.add(btnReset);
        jp3.add(btnRegister);

        getContentPane().add(jp1, BorderLayout.NORTH);
        getContentPane().add(jp2, BorderLayout.CENTER);
        getContentPane().add(jp3, BorderLayout.SOUTH);

        pack();
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        JButton btn = (JButton) e.getSource();
        System.out.println("---" + e.getActionCommand());
        System.out.println("btn.equals(btnReset)=" + btn.equals(btnReset));
        if (btn.equals(btnReset)) {
            txtUsername.setText("");
            txtPassword.setText("");
        } else if (btn.equals(btnSubmit)) {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            UserDAO userdao = new UserDAO();
            if (userdao.getUser(username, password)) {
                UserManagerForm umf = new UserManagerForm();
                this.dispose();
            }
        } else if (btn.equals(btnRegister)) {
        }
    }

    public static void main(String args[]) {
        LoginForm lf = new LoginForm();
    }
}
