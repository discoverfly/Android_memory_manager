/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

/**
 *
 * @author discover
 */


public class STACK<T> {

    final int N = 10000;
    private Object[] element;
    private int top, maxSize;

    STACK() {
        top = -1;
        element = new Object[N];
        maxSize = N;
    }




    STACK(int cap) {
        top = -1;
        element = new Object[cap];
        maxSize = cap;
    }

    public boolean empty() {
        return top == -1;
    }

    public boolean push(T v) {
        if (top + 1 < maxSize) {
            element[++top] = v;
            return true;
        }
        System.out.println("The stack is full");
        return false;
    }

    public T top() {
        if (empty()) {
            System.out.println("The stack is empty");
            return null;
        }
        return (T)element[top];
    }

    public boolean pop() {
        if (empty()) {
            System.out.println("The stack is empty");
            return false;
        }
        --top;
        return true;
    }
}
