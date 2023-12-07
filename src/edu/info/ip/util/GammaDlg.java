package edu.info.ip.util;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

public class GammaDlg extends AbstractSettingsDialog{

    private JSlider slider1;
    private JTextField textField1;

    public GammaDlg() {
        super();
        setTitle("Gamma Contrast");

        mainPanel.setLayout(new GridLayout(1,1));

        JPanel panel1 = new JPanel();
        textField1 = new JTextField(5);
        slider1 = new JSlider(0,500,100);
        slider1.setPreferredSize(new Dimension(400,50));
//        slider1.setMajorTickSpacing(51);
        slider1.setMinorTickSpacing(20);

        Hashtable<Integer,JLabel> lables = new Hashtable();
        lables.put(0, new JLabel("0.0"));
        lables.put(100, new JLabel("1.0"));
        lables.put(500, new JLabel("5.0"));

        slider1.setLabelTable(lables);

        slider1.setPaintLabels(true);
        slider1.setPaintTicks(true);

        panel1.add(textField1);
        panel1.add(slider1);

        mainPanel.add(panel1);

        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                onSlide();
            }
        });
    }

    private void onSlide(){
        double val = (double)slider1.getValue()/100.0;
        textField1.setText(""+val);

        BufferedImage img = ImageUtil.contrastGamma(originalImg, val);
        imagePanel.setImage(img);
    }
}
