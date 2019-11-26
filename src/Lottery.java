import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class Lottery extends JFrame {
    private JButton b1, b2;
    private JLabel l2;
    private JTextField[] t = new JTextField[7];
    private ArrayList<Byte> list1 = new ArrayList<>();
    private ArrayList<Byte> list2 = new ArrayList<>();
    private int[] lottery = new int[7];

    private Lottery(String s) {
        super(s);
        setLayout(new FlowLayout());
        b1 = new JButton("Guess");
        b2 = new JButton("Reset");
        JLabel l1 = new JLabel("Please print numbers 0-99:");
        l2 = new JLabel("");
        add(b1);
        add(b2);
        add(l1);
        for (int i = 0; i < 7; ++i) {
            this.t[i] = new JTextField(10);
            add(t[i]);
        }
        add(l2);

        eHandler handler = new eHandler();
        b1.addActionListener(handler);
        b2.addActionListener(handler);
    }

    public class eHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            list1.clear();
            list2.clear();
            for (int i = 0; i < lottery.length; i++) {
                int randomNum = (int) (Math.random() * 99); //Random number created here.
                for (int x = 0; x < i; x++) {
                    if (lottery[x] == randomNum) // Here, code checks if same random number generated before.
                    {
                        randomNum = (int) (Math.random() * 99);// If random number is same, another number generated.
                        x = -1; // restart the loop
                    }

                }
                lottery[i] = randomNum;
                list1.add((byte) randomNum);
            }

            try (BufferedWriter writer1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("E:\\lottery.txt"), StandardCharsets.UTF_8))) {

                Collections.sort(list1);
                for (Byte value : list1) {
                    writer1.write(value + " ");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            byte[] number = new byte[7];
            byte num;
            int count = 0;

            try {
                if (e.getSource() == b1) {
                    for (int j = 0; j < list1.size(); j++) {
                        num = Byte.parseByte(t[j].getText());
                        for (int x = 0; x < j; x++) {

                            if (number[x] == num) // Here, code checks if same random number generated before.
                            {
                                num = (byte) (Math.random() * 99);// If random number is same, another number generated.
                                x = -1; // restart the loop
                            }
                        }
                        number[j] = num;
                        list2.add(num);
                    }

                    try (BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("E:\\mynumbers.txt"), StandardCharsets.UTF_8))) {
                        Collections.sort(list2);
                        for (Byte aByte : list2) {
                            writer2.write(aByte + " ");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    for (Byte aByte : list2) {
                        int index = Collections.binarySearch(list1, aByte);
                        if (index == 0) {
                            count++;
                        }
                    }

                    if (count < 7) {
                        l2.setText("You have guessed " + count + " number(s)! Try again!");
                    }
                    if (count == 7) {
                        l2.setText("You have guessed " + count + " number(s)! Congratulations!");
                    }

                }
                if (e.getSource() == b2) {
                    for (JTextField jTextField : t) {
                        jTextField.setText(null);
                    }
                    list1.clear();
                    list2.clear();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Only numbers 0-99");
                list1.clear();
                list2.clear();

            }
        }
    }

    public static void main(String[] args) {
        Lottery r = new Lottery("Lottery");
        r.setVisible(true);
        r.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        r.setSize(300, 600);
    }
}