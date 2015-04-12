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

    public GUI() {
        LogHandler.log("Initiating new UI");
        initUI();
    }
    private void initUI() {

        setTitle("Simple example");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void display(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //LogHandler.log("MAKING NEW   "+num1+"   "+num2);
                GUI ex = new GUI();
                //LogHandler.log("MADE NEW");
                ex.setVisible(true);
                //LogHandler.log("SET VISIBLE");
                //num2++;
            }
        });
    }
}
