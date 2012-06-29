/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.util.*;
import javax.swing.*;

/**
 *
 * @author discover
 */
public class Calculator {

    private String s, gs, temp;
    private Scanner cin;
    private int a, b;
    private STACK<Integer> stkI;
    private STACK<String> stkC;

    Calculator() {
        s = null;
        cin = new Scanner(System.in);
    }

    public void pocess() {
        read();
        if (judge() == false) {
            JOptionPane.showMessageDialog(null, "表达式格式不正确");
            return;
        }
        trans();
        JOptionPane.showMessageDialog(null, "表达式: " + s + "\n" + "的结果为：\n" + cal());
        System.out.println(cal());
    }

    void read() {
        s = JOptionPane.showInputDialog("请输入一个\n只含整数的算术表达式：");
    }

    boolean judge() {
        int cnt = 0;
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '(') {
                ++cnt;
            } else if (s.charAt(i) == ')') {
                --cnt;
            }
        }
        if (cnt != 0) {
            return false;
        }
        boolean flag = true;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '+'
                    || s.charAt(i) == '-'
                    || s.charAt(i) == '*'
                    || s.charAt(i) == '/') {
                if (flag == true)
                    return false;
                flag = true;
            } else if (s.charAt(i) == '('
                    || s.charAt(i) == ')'
                    || s.charAt(i) >= '0'
                    || s.charAt(i) <= '9')
                flag = false;
            else
                return false;
        }
        if (flag == true) {
            return false;
        }
        return true;
    }

    int priority(String t) {
        if (t.charAt(0) == '+' || t.charAt(0) == '-')
            return 0;
        if (t.charAt(0) == '*' || t.charAt(0) == '/')
            return 1;
        if (t.charAt(0) == '(' || t.charAt(0) == ')')
            return -1;
        return 0;
    }

    void trans() {
        gs = "";
        stkC = new STACK<String>();
        int i = 0;
        while (i < s.length()) {
            if (s.charAt(i) == '(') {
                stkC.push(s.substring(i, i + 1));
                i++;
            } else if (s.charAt(i) == ')') {
                while (priority(stkC.top()) > priority(s.substring(i, i + 1))) {
                    gs += stkC.top();
                    gs += " ";
                    stkC.pop();
                }
                stkC.pop();
                ++i;
            } else if (s.charAt(i) == '+' || s.charAt(i) == '-') {
                while (stkC.empty() == false
                        && priority(stkC.top()) >= priority(s.substring(i, i + 1))) {
                    gs += stkC.top();
                    gs += " ";
                    stkC.pop();
                }
                stkC.push(s.substring(i, i + 1));
                ++i;

            } else if (s.charAt(i) == '*' || s.charAt(i) == '/') {
                while (stkC.empty() == false
                        && priority(stkC.top()) >= priority(s.substring(i, i + 1))) {
                    gs += stkC.top();
                    gs += " ";
                    stkC.pop();
                }
                stkC.push(s.substring(i, i + 1));
                ++i;
            } else {
                int v = 0, start = i;
                while (i < s.length() && s.charAt(i) >= '0' && s.charAt(i) <= '9')
                    ++i;
                // stkI.push(Integer.parseInt(s.substring(start, i)));
                gs += s.substring(start, i);
                gs += " ";
            }
        }
        while (stkC.empty() == false) {
            gs += stkC.top();
            gs += " ";
            stkC.pop();
        }
        //  System.out.println(gs);
    }

    int cal() {
        int i = 0, v;
        stkI = new STACK<Integer>();
        while (i < gs.length()) {
            if (gs.charAt(i) == ' ')
                ++i;
            else if (gs.charAt(i) >= '0' && gs.charAt(i) <= '9') {
                int start = i;
                while (gs.charAt(i) >= '0' && gs.charAt(i) <= '9')
                    ++i;
                stkI.push(Integer.parseInt(gs.substring(start, i)));
                ++i;
            } else {
                temp = gs.substring(i, i + 1);
                ++i;
                calcu();
            }
        }
        return stkI.top();
    }

    void calcu() {
        a = stkI.top();
        stkI.pop();
        b = stkI.top();
        stkI.pop();
        // System.out.println(b +temp + a);
        if (temp.charAt(0) == '+')
            stkI.push(a + b);
        else if (temp.charAt(0) == '-')
            stkI.push(b - a);
        else if (temp.charAt(0) == '*')
            stkI.push(a * b);
        else if (temp.charAt(0) == '/')
            stkI.push(b / a);
    }
}
