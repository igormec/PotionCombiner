package potioncombiner;

import org.tbot.internal.handlers.LogHandler;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.*;
import java.awt.event.ItemListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;




/**
 * Created by Igor on 011. Apr 11, 15.
 */
public class GUI extends JFrame implements ItemListener {

    private JComboBox<String> box;
    private String[] potionList = {"Attack potion(3)", "Antipoison(3)", "Strength potion(3)", "Restore potion(3)", "Guthix Balance(3)?", "Energy potion(3)",
            "Defence potion(3)", "Agility potion(3)", "Combat potion(3)", "Prayer potion(3)", "Super attack(3)", "Superantipoison(3)", "Fishing potion(3)",
            "Super energy(3)", "Hunter potion(3)", "Super strength(3)", "Super restore(3)", "Super defence(3)", "Antifire potion(3)",
            "Magic potion(3)", "Stamina potion(3)", "Zamorak brew(3)", "Saradomin brew(3)", "Extended antifire(3)"};



    public GUI() {
        LogHandler.log("Initiating new UI");
        initUI();
    }
    private void initUI() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(new Insets(25, 20, 25, 20)));
        add(panel);


        JLabel label = new JLabel("Select potion to combine");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(240, 240, 240));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
        top.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
        top.add(label);


        JPanel middle = new JPanel();
        middle.setLayout(new BoxLayout(middle, BoxLayout.X_AXIS));
        top.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
        box = new JComboBox<>(potionList);
        box.addItemListener(this);
        middle.add(box);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        bottom.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
        JButton start = new JButton("START");
        //start.addItemListener(this);
        bottom.add(start);

        panel.add(top);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(middle);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(bottom);


        setTitle("Select Potion");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        pack();

    }

    public void itemStateChanged(ItemEvent e) {

        if (e.getStateChange() == ItemEvent.SELECTED) {
            //display.setText(e.getItem().toString());
            LogHandler.log("Item state changed to: "+e.getItem().toString());
        }
    }


    /*public void display(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI ex = new GUI();
                ex.setVisible(true);
            }
        });
    }*/
}
