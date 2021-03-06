package cl.automind.control.server.examples;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GamePanel extends JPanel{

	private boolean running = true;
	private static final long serialVersionUID = -9095003889768687079L;
	private Image dbImage;
	private Graphics dbg;
	private GameLogic gl;
	
	
	public GamePanel(GameLogic gl){
		this.gl = gl;
	}

	public void run(){
		while(running){
			gameUpdate();
			gameRender();
			paintScreen();
//			try {
//				Thread.sleep(20);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}
	
	private void gameUpdate(){
	}
	
	private void gameRender(){
		if (dbImage == null){
			dbImage = new BufferedImage(this.getWidth(), this.getHeight(), Transparency.OPAQUE);
			if (dbImage == null){
				System.out.println("Error al crear imagen");
				return;
			}
			dbg = dbImage.getGraphics();
		}
		dbg.setColor(Color.WHITE);
		dbg.fillRect(0, 0, this.getWidth(), this.getHeight());
		dbg.setColor(Color.BLUE);
		dbg.fillRect(gl.bicho.getX(), gl.bicho.getY(), 15, 15);
	}
	
	
	private void paintScreen(){
		Graphics g;
		try {
			g = this.getGraphics();
			if (g!= null && dbImage != null){
				g.drawImage(dbImage, 0,0,null);
			}
			Toolkit.getDefaultToolkit().sync();
			g.dispose();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
}
