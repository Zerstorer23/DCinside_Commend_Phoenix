package lexington.GalleryAPI.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class mainScene {
    private JPanel mainPanel;
    int col = 1;
    private ArrayList<JSlider> sliders = new ArrayList<>();
    private ArrayList<JLabel> labels = new ArrayList<>();
    JFrame frame;
    public void initUI() {
        frame = new JFrame("Main Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(mainPanel);
        //frame.pack();
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public void addLabel(int coreID,int max){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridx = 0;
        constraints.gridy = col;
        JLabel label = new JLabel("#Core "+coreID);
        col+=3;
        //int min, int max, int value
        mainPanel.add(label,constraints);
        labels.add(label);
        addSlider(max);
    }

    public void addSlider(int max){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.weightx =1;
        constraints.gridx = 0;
        constraints.gridy = col;
        col+=3;
        //int min, int max, int value
        JSlider jSlider = new JSlider(0,max,0);
        mainPanel.add(jSlider,constraints);
        sliders.add(jSlider);
    }

    public void updateValue(int slidernumber, int value){
        labels.get(slidernumber).setText("#Core "+slidernumber+" | "+value+"個");
        sliders.get(slidernumber).setValue(value);
    }
    public void setMaxValue(int slidernumber, int value){
        sliders.get(slidernumber).setMaximum(value);
    }
    public void dispose(){
        frame.dispose();
    }
}
