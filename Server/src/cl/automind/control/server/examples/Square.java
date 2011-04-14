package cl.automind.control.server.examples;

public class Square {

	private int x,y;
	private Object lock = new Object();
	
	public Square(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void move(int dx, int dy){
		synchronized(lock){
			this.x += dx;
			this.y += dy;
			if (x > 800-15)
				x = 800-15;
			if (x < 0)
				x = 0;
			if (y > 600-45)
				y = 600-45;
			if (y < 0)
				y = 0;
		}
	}
	
	public int getX(){
		synchronized(lock){
			return this.x;
		}
	}
	
	public int getY(){
		synchronized(lock){
			return this.y;
		}
	}
	
	
}
