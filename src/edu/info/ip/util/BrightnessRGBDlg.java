package edu.info.ip.util;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BrightnessRGBDlg extends AbstractSettingsDialog{

    private JSlider slider1, slider2,slider3;
    private JTextField textField1, textField2, textField3;

    public BrightnessRGBDlg() {
        super();
        setTitle("Brightness RGB");

        mainPanel.setLayout(new GridLayout(3,1));

        JPanel panel1 = new JPanel();
        textField1 = new JTextField(5);
        slider1 = new JSlider(-255,255,0);
        slider1.setPreferredSize(new Dimension(400,50));
        slider1.setMajorTickSpacing(51);
        slider1.setMinorTickSpacing(10);
        slider1.setPaintLabels(true);
        slider1.setPaintTicks(true);

        panel1.add(textField1);
        panel1.add(slider1);

        JPanel panel2 = new JPanel();
        textField2 = new JTextField(5);
        slider2 = new JSlider(-255,255,0);
        slider2.setPreferredSize(new Dimension(400,50));
        slider2.setMajorTickSpacing(51);
        slider2.setMinorTickSpacing(10);
        slider2.setPaintLabels(true);
        slider2.setPaintTicks(true);

        panel2.add(textField2);
        panel2.add(slider2);

        JPanel panel3 = new JPanel();
        textField3 = new JTextField(5);
        slider3 = new JSlider(-255,255,0);
        slider3.setPreferredSize(new Dimension(400,50));
        slider3.setMajorTickSpacing(51);
        slider3.setMinorTickSpacing(10);
        slider3.setPaintLabels(true);
        slider3.setPaintTicks(true);

        panel3.add(textField3);
        panel3.add(slider3);

        mainPanel.add(panel1);
        mainPanel.add(panel2);
        mainPanel.add(panel3);


        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                onSlide();
            }
        });
        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                onSlide();
            }
        });
        slider3.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                onSlide();
            }
        });
    }

    private void onSlide(){
        int valR = slider1.getValue();
        int valG = slider2.getValue();
        int valB = slider3.getValue();
        textField1.setText(""+valR);
        textField2.setText(""+valG);
        textField3.setText(""+valB);

        BufferedImage img = ImageUtil.brightnessRGB(originalImg, valR, valG, valB);
        imagePanel.setImage(img);
    }
}
