package cl.automind.control.server.examples;

import java.awt.Dimension;

import javax.swing.JFrame;

import cl.automind.control.server.GameControlServer;

/**
 * Ejemplo de como recibir en un juego las peticiones enviadas desde los celulares.
 * @author manuel
 *
 */
public class Example {

	public static final int PORT = 9999;
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		GameControlServer gcs = new GameControlServer(9999,12);
		GameLogic gl = new GameLogic();
		gcs.setControlListener(gl);
		gcs.start();
		
		GamePanel gp = new GamePanel(gl);
		Dimension size = new Dimension(800,600);
		JFrame frame = new JFrame("Bichos");
		frame.setMinimumSize(size);
		frame.getContentPane().add(gp);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.pack();
	    frame.setVisible(true);
	    frame.setResizable(false);
	    gp.run();
		while(true){
			Thread.yield();
		}
		
		
	}

}
