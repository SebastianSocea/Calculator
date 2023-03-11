import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodListener;
import java.util.ArrayList;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


class GUI implements ActionListener {

    double num1 = 0, num2 = 0, result = 0, savenum = 0;
    char operator;
    int ok = 0, okdec = 0,okp = 0, prec, asoc;
    String num = "";
    ArrayList<String> exp = new ArrayList<String>();
    String temp = "";
    ArrayList<String> pexp = new ArrayList<String>();
    ArrayList<String> stack = new ArrayList<String>();
    ArrayList<String> v = new ArrayList<String>();

    private JLabel label;
    private JButton plus, minus, div, prod, equal, decimal, delete, clear, neg;
    private JButton[] functionButtons = new JButton[9];
    private JButton[] numberButtons = new JButton[10];
    private JFrame frame;
    private JTextField in, out;
    private JPanel panel;
    private JList history;

    public GUI() {

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(620,600);
        frame.setLayout(null);

        in = new JTextField();
        in.setBounds(50,75,300,50);
        in.setEditable(false);

        out = new JTextField();
        out.setBounds(50,20,300,50);
        out.setEditable(false);

        plus = new JButton("+");
        minus = new JButton("-");
        div = new JButton("/");
        prod = new JButton("*");
        equal = new JButton("=");
        decimal = new JButton(".");
        clear = new JButton("clear");
        delete = new JButton("delete");
        neg = new JButton("(-)");

        functionButtons[0] = plus;
        functionButtons[1] = minus;
        functionButtons[2] = div;
        functionButtons[3] = prod;
        functionButtons[4] = decimal;
        functionButtons[5] = delete;
        functionButtons[6] = clear;
        functionButtons[7] = equal;
        functionButtons[8] = neg;

        for(int i = 0; i < 9; i++){

            functionButtons[i].addActionListener(this);
            functionButtons[i].setFocusable(false);
        }

        for(int i = 0; i < 10; i++){

            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberButtons[i].setFocusable(false);
        }

        neg.setBounds(50,480,100,50);
        delete.setBounds(150,480,100,50);
        clear.setBounds(250,480,100,50);


        panel  = new JPanel();
        panel.setBounds(50,150,300,300);
        panel.setLayout(new GridLayout(4,4,10,10));

        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(plus);
        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(minus);
        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(prod);
        panel.add(decimal);
        panel.add(numberButtons[0]);
        panel.add(equal);
        panel.add(div);

        frame.add(panel);
        frame.add(neg);
        frame.add(delete);
        frame.add(clear);
        frame.add(in);
        frame.add(out);
        frame.setVisible(true);
    }

    public static void main(String args[])  {

        new GUI();
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static double Rez(double a, double b, String c){

        double rez = 0;
        switch(c){

            case " + ":
                rez = a + b;
                break;
            case " - ":
                rez =  a-b;
                break;
            case " * ":
                rez = a*b;
                break;
            case " / ":
                rez = a/b;
                break;
        }
        return rez;
    }

    @Override
    public void actionPerformed(ActionEvent a) {

        for(int i = 0; i < 10; i++){

            if(a.getSource() == numberButtons[i]){
                in.setText(in.getText().concat(String.valueOf(i)));
                out.setText(out.getText().concat(String.valueOf(i)));
                num = num + i;
                int ok = 1;
            }
        }

        if(a.getSource() == decimal && okdec == 0){

            in.setText(in.getText().concat("."));
            okdec++;
        }

        if(a.getSource() == plus){

            out.setText(out.getText().concat(" + "));
            exp.add(num);
            exp.add(" + ");
            num = "";
            in.setText("");
            okdec = 0;
        }

        if(a.getSource() == minus){

            out.setText(out.getText().concat(" - "));
            exp.add(num);
            exp.add(" - ");
            num = "";
            in.setText("");
            okdec = 0;
        }

        if(a.getSource() == prod){

            out.setText(out.getText().concat(" * "));
            exp.add(num);
            exp.add(" * ");
            num = "";
            in.setText("");
            okdec = 0;
        }

        if(a.getSource() == div){

            out.setText(out.getText().concat(" / "));
            exp.add(num);
            exp.add(" / ");
            num = "";
            in.setText("");
            okdec = 0;
        }

        if(a.getSource() == equal) {

            exp.add(num);


            System.out.println(exp);
            for(int i = 0; i < exp.size(); i++){

                if(isNumeric(exp.get(i))){
                    pexp.add(exp.get(i));
                }

                else {

                    switch (exp.get(i)) {

                        case " + " :
                            do{

                                if (stack.size() == 0) {
                                    stack.add(exp.get(i));
                                }

                                else if (stack.get(stack.size() - 1) == " - ") {

                                    pexp.add(stack.get(stack.size() - 1));
                                    stack.remove(stack.size() - 1);
                                    stack.add(exp.get(i));
                                }

                                else if (stack.get(stack.size() - 1) == " * ") {
                                    int x = stack.size();
                                    for(int j = 0; j < x; j++) {
                                        pexp.add(stack.get(stack.size() - 1));
                                        stack.remove(stack.size() - 1);
                                    }
                                    stack.add(exp.get(i));
                                }

                                else if(stack.get(stack.size() - 1) == " / ") {
                                    int x = stack.size();
                                    for(int j = 0; j < x; j++) {
                                        pexp.add(stack.get(stack.size() - 1));
                                        stack.remove(stack.size() - 1);
                                    }
                                    stack.add(exp.get(i));
                                }
                                else {

                                    pexp.add(stack.get(stack.size() - 1));
                                    stack.remove(stack.size() - 1);
                                    stack.add(exp.get(i));
                                }
                            } while(stack.size() > 1);

                            break;
                        case " - " :
                            do {

                                if (stack.size() == 0) {

                                    stack.add(exp.get(i));
                                }

                                else if (stack.get(stack.size() - 1) == " + ") {

                                    pexp.add(stack.get(stack.size() - 1));
                                    stack.remove(stack.size() - 1);
                                    stack.add(exp.get(i));
                                }



                                else if (stack.get(stack.size() - 1) == " * ") {

                                    int x = stack.size();
                                    for(int j = 0; j < x; j++) {
                                        pexp.add(stack.get(stack.size() - 1));
                                        stack.remove(stack.size() - 1);
                                    }
                                    stack.add(exp.get(i));
                                }

                                else if(stack.get(stack.size() - 1) == " / "){

                                    int x = stack.size();
                                    for(int j = 0; j < x; j++) {
                                        pexp.add(stack.get(stack.size() - 1));
                                        stack.remove(stack.size() - 1);
                                    }
                                    stack.add(exp.get(i));
                                }
                                else {

                                    pexp.add(stack.get(stack.size() - 1));
                                    stack.remove(stack.size() - 1);
                                    stack.add(exp.get(i));
                                }
                            } while(stack.size() > 1);

                            break;
                        case " * " :
                            do{

                                if (stack.size() == 0) {

                                    stack.add(exp.get(i));
                                }

                                else if (stack.get(stack.size() - 1) == " + ") {
                                    stack.add(exp.get(i));
                                }

                                else if (stack.get(stack.size() - 1) == " - ") {
                                    stack.add(exp.get(i));
                                }

                                else if(stack.get(stack.size() - 1) == " / "){

                                    pexp.add(stack.get(stack.size() - 1));
                                    stack.remove(stack.size() - 1);
                                    stack.add(exp.get(i));
                                }

                                else {

                                    pexp.add(stack.get(stack.size() - 1));
                                    stack.remove(stack.size() - 1);
                                    stack.add(exp.get(i));
                                }
                            } while(stack.size() > 2);

                            break;
                        case " / " :
                            do{

                                if (stack.size() == 0) {

                                    stack.add(exp.get(i));
                                }

                                else if (stack.get(stack.size() - 1) == " + ") {

                                    stack.add(exp.get(i));
                                }

                                else if (stack.get(stack.size() - 1) == " - ") {

                                    stack.add(exp.get(i));
                                }

                                else if(stack.get(stack.size() - 1) == " * "){

                                    pexp.add(stack.get(stack.size() - 1));
                                    stack.remove(stack.size() - 1);
                                    stack.add(exp.get(i));
                                }
                                else {

                                    pexp.add(stack.get(stack.size() - 1));
                                    stack.remove(stack.size() - 1);
                                    stack.add(exp.get(i));
                                }
                            } while(stack.size() > 2);

                            break;
                    }
                }

            }
            int x = stack.size();
            for(int j = 0; j < x; j++) {
                pexp.add(stack.get(stack.size() - 1));
                stack.remove(stack.size() - 1);
            }
            stack.clear();
            System.out.println(pexp);

            for(int i = 0; i < pexp.size(); i++){

                if(isNumeric(pexp.get(i))){
                    stack.add(pexp.get(i));
                }

                else{

                    double op2 = Double.parseDouble(stack.get(stack.size()-1));
                    double op1 = Double.parseDouble(stack.get(stack.size()-2));

                    temp = Double.toString(Rez(op1,op2,pexp.get(i)));

                    stack.remove(stack.size()-1);
                    stack.remove(stack.size()-1);

                    stack.add(temp);

                }
            }

            System.out.println(stack);
            okdec = 0;
            in.setText(stack.get(0));
        }

        if(a.getSource() == clear){

            in.setText("");
            out.setText("");

            exp.clear();
            pexp.clear();
            stack.clear();
            num = "";

            okdec = 0;
        }

        if(a.getSource() == delete){

            String stringout = out.getText();
            out.setText("");
            in.setText("");

            for(int i = 0; i < stringout.length() - 1; i++){

                out.setText(out.getText() + stringout.charAt(i));
            }
        }

        if(a.getSource() == neg){
            if(okp == 0) {
                out.setText(out.getText() + '(');
                okp = 1;
            }
            else {

                out.setText(out.getText()+')');
                okp = 0;
            }
        }
    }

}
