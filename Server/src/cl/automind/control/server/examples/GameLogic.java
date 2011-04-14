package cl.automind.control.server.examples;

import cl.automind.control.server.ControlListenerInterface;

public class GameLogic implements ControlListenerInterface {

	public Square bicho;
	
	public GameLogic(){
		bicho = new Square(400,300);
	}
	
	@Override
	public void updateState(int playerId, float angle, float pitch) {
		//calibrar el aceler—metro
		int dx = -1 * (int)(angle - 1.234375);
		int dy = -1 * (int)(pitch - 2.328125);
		System.out.println("raw: " + angle + "," + pitch + "\t bicho: " + dx + ";" + dy) ;
		bicho.move(dx,dy);
	}

}
