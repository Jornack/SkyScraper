package com.jornack.skyscraper.window;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import com.jornack.skyscraper.util.PreferenceManager;

/**
 * 
 * Dialog for setting the filename used to save the data.
 * 
 * @author Jornack
 *
 */
public class SaveJDialog extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4812746407237831238L;
	
	private JTextField saveTxtField = null;
	private String oldValue = null;
	private String filename = null; 
	private boolean set = false;
	public SaveJDialog(Frame parent, String filename){
		super(parent, "Save", true);
		this.filename = filename;
		
		setBounds(100, 100, 500, 110);
		JPanel panel = new JPanel();
		
		
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));

		SpringLayout spring = new SpringLayout();
        //JLabel saveLbl = new JLabel("");
		panel.setLayout(spring);
        
		saveTxtField = new JTextField();
        saveTxtField.setText(getFilename());
        this. oldValue = getFilename();
        spring.putConstraint(SpringLayout.NORTH, saveTxtField, 10, SpringLayout.NORTH, panel);
        spring.putConstraint(SpringLayout.WEST, saveTxtField, 5, SpringLayout.WEST, panel);
        spring.putConstraint(SpringLayout.EAST, saveTxtField, -5, SpringLayout.EAST, panel);
        panel.add(saveTxtField);
        
        JButton okBtn = new JButton("OK");
        okBtn.setActionCommand("OK");
        okBtn.addActionListener(this);
        spring.putConstraint(SpringLayout.NORTH, okBtn, 4, SpringLayout.SOUTH, saveTxtField);
        spring.putConstraint(SpringLayout.EAST, okBtn, 0, SpringLayout.EAST, saveTxtField);
        //spring.putConstraint(SpringLayout.EAST, okBtn, -10, SpringLayout.EAST, panel);
        panel.add(okBtn);
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setActionCommand("CANCEL");
        cancelBtn.addActionListener(this);
        spring.putConstraint(SpringLayout.NORTH, cancelBtn, 4, SpringLayout.SOUTH, saveTxtField);
        spring.putConstraint(SpringLayout.EAST, cancelBtn, -5, SpringLayout.WEST, okBtn);
        //spring.putConstraint(SpringLayout.EAST, okBtn, -10, SpringLayout.EAST, panel);
        panel.add(cancelBtn);
       
		
		setContentPane(panel);
		
		
		PreferenceManager.getPreferences().put(PreferenceManager.SAVE, "false");
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand().equals("CANCEL")){
			PreferenceManager.getPreferences().put(PreferenceManager.SAVE_FILENAME, oldValue);
		}else{
			PreferenceManager.getPreferences().put(PreferenceManager.SAVE_FILENAME, saveTxtField.getText());
			PreferenceManager.getPreferences().put(PreferenceManager.SAVE, "true");
		}
		System.out.println("Filename :" + getFilename());
		dispose();
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	public boolean isSet() {
		return set;
	}
	public void setSet(boolean set) {
		this.set = set;
	}

}
