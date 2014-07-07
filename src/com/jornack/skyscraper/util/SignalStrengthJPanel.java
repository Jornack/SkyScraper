package com.jornack.skyscraper.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class SignalStrengthJPanel extends JPanel
{

    private int strength = 0;
    private final JTextField[] bars = new JTextField[4];
    private final Color[] colors =
    {
        Color.red, Color.orange, Color.yellow, Color.green
    };

    public SignalStrengthJPanel()
    {

        SpringLayout spring = new SpringLayout();
        setLayout(spring);
//        JComponent last = this;
//        for(int i = 0 ; i < bars.length-3;i++){
//        	bars[i] = new JTextField("A"+i);
//        	bars[i].setBackground(colors[i]);
//        	bars[i].setForeground(colors[i]);
//
//        	setBackground(Color.white);
////        	spring.putConstraint(SpringLayout.NORTH, bars[i], 0, SpringLayout.NORTH, this);
////        	spring.putConstraint(SpringLayout.WEST, bars[i],5, SpringLayout.WEST, last);
////        	spring.putConstraint(SpringLayout.EAST, bars[i], 15, SpringLayout.WEST,  bars[i]);
////        	spring.putConstraint(SpringLayout.SOUTH, bars[i], 50 +(i*2), SpringLayout.NORTH, this);
//            add(bars[i]);
//            last = bars[i];
//        }

        setPreferredSize(new Dimension(30, 30));
        //setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

    }

    private Color getColor(int bar)
    {
        Color c = null;

        if (strength == 0)
        {
            return Color.GRAY;
        }

        if (bar == 1 && strength <= 25)
        {
            return Color.RED;
        } else if (strength <= 25)
        {
            return Color.GRAY;
        }
        if (bar <= 2 && strength <= 50)
        {
            return Color.ORANGE;
        } else if (strength <= 50)
        {
            return Color.GRAY;
        }
        if (bar <= 3 && strength <= 75)
        {
            return Color.YELLOW;
        } else if (strength <= 75)
        {
            return Color.GRAY;
        }
        if (bar <= 4 && strength <= 100)
        {
            return Color.GREEN;
        }

        return null;
    }

    @Override
    public void paint(Graphics g)
    {

        g.setColor(getColor(1));
        g.fillRect(0, 6, 4, 8);
        g.setColor(getColor(2));
        g.fillRect(5, 4, 4, 10);
        g.setColor(getColor(3));
        g.fillRect(10, 2, 4, 12);
        g.setColor(getColor(4));
        g.fillRect(15, 0, 4, 14);

    }

    public int getStrength()
    {
        return strength;
    }

    public void setStrength(int strength)
    {
        this.strength = strength;
//		// for debugging
//		if (this.strength ==100){
//			this.strength = 0;
//		}else
//		this.strength += 25;
//
        paint(getGraphics());
    }
}
