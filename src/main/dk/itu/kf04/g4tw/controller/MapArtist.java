package dk.itu.kf04.g4tw.controller;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Alexander
 * Date: 01-04-12
 * Time: 15:03
 */
public class MapArtist {
	JFrame mainFrame;
	JPanel drawPanel;

	public MapArtist() {
		mainFrame = new JFrame("G4TW Map Drawer");
		drawPanel = new JPanel(true);
		Rectangle rect = new Rectangle(800,600);

		mainFrame.add(drawPanel);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setBounds(rect);
		drawPanel.setBounds(rect);
		
		mainFrame.setVisible(true);
	}

	public static boolean initMapArtist() {
		try {
			new MapArtist();
			return true;
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			return false;
		}
	}
}