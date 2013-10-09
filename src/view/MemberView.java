package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.ColorScheme;

@SuppressWarnings("serial")
public class MemberView extends JPanel{
	private JLabel name;
	private JLabel score;
	private static ArrayList<String> members = new ArrayList<String>();;

	public MemberView(String name, int score){
		members.add( name );
		
		initPanel();
		initComponents(name, score);
	}
	
	private void initPanel(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(ColorScheme.getInstance().getUniqueColor());
		setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
	}
	
	private void initComponents(String n, int s){
		name = new JLabel(n);
		name.setFont(new Font("Segoe UI", 0, 18));
		name.setAlignmentX(Component.CENTER_ALIGNMENT);
		name.setForeground(Color.WHITE);
		add(name);

		add(Box.createRigidArea(new Dimension(0,5)));
		
		score = new JLabel(s + " pts");
		score.setFont(new Font("Segoe UI", 0, 18));
		score.setAlignmentX(Component.CENTER_ALIGNMENT);
		score.setForeground(Color.WHITE);
		add(score);		
	}
	
	public ArrayList<String> getMembers() {
		return members;
	}
}
