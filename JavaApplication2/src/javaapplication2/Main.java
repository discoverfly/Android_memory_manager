package javaapplication2;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        int yes = 0;
        while (yes == 0) {
            Calculator calculator = new Calculator();
            calculator.pocess();
            yes = JOptionPane.showConfirmDialog(null, "继续输入？");
        }
        System.exit(0);
    }
}
