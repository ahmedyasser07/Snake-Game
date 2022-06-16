package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener  {

	static final int WIDTH=600;
	static final int HEIGHT=600;
	static final int UNIT_SIZE=25;
	static final int UNITS=(WIDTH*HEIGHT)/UNIT_SIZE;
	static final int DELAY=75;
	final int[] x=new int[UNITS];
	final int[] y=new int[UNITS];
	int bodyParts=6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction='R';
	boolean running =false;
	Timer timer;
	Random rand;

	public GamePanel() {
		rand=new Random();
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new Adapter());
		startGame(); 
	}
	
	public void startGame() {
		timer=new Timer(DELAY,this);
		running=true;
		newApple();
		timer.start();
	}
	public void paint(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		if(running) {
			g.setColor(Color.red);
			g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);
			
			for(int i=0;i<bodyParts;i++) {
				if(i==0) {
					g.setColor(new Color(0xF4BC1C));
					g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
				}
				else {
					g.setColor(new Color(0xFFFF00));
					g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free",Font.BOLD,30));
			FontMetrics m=getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten,(WIDTH-m.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}
	public void newApple() {
		appleX=rand.nextInt((int)(WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY=rand.nextInt((int)(HEIGHT/UNIT_SIZE))*UNIT_SIZE;

	}
	public void move() {
		for(int i=bodyParts;i>0;i--) {
			x[i]=x[i-1];
			y[i]=y[i-1];
		}
		switch(direction) {
		case 'U': y[0]=y[0]-UNIT_SIZE;break;
		case 'D': y[0]=y[0]+UNIT_SIZE;break;
		case 'R': x[0]=x[0]+UNIT_SIZE;break;
		case 'L': x[0]=x[0]-UNIT_SIZE;break;
		}
	}
	public void checkApple() {
		if(x[0]==appleX && y[0]==appleY) {
			bodyParts++;
			applesEaten++;
			newApple(); 
		}
	}
	public void checkCollisions() {
		for(int i=bodyParts;i>0;i--) {
			if(x[0]==x[i] && y[0]==y[i]) {
				running=false;
			}
		}
		if(x[0]<0) {
			x[0]=WIDTH;
		}
		if(x[0]>WIDTH) {
			x[0]=0;
		}
		if(y[0]<0) {
			y[0]=HEIGHT;
		}
		if(y[0]>HEIGHT) {
			y[0]=0;
		}
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD,30));
		FontMetrics m1=getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten,(WIDTH-m1.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD,80));
		FontMetrics m=getFontMetrics(g.getFont());
		g.drawString("Game Over",(WIDTH-m.stringWidth("Game Over"))/2,HEIGHT/2);
	}
	public void keepIn() {
		if(x[0]<0) {
			x[0]=WIDTH;
		}
		if(x[0]>WIDTH) {
			x[0]=0;
		}
		if(y[0]<0) {
			y[0]=HEIGHT;
		}
		if(y[0]>HEIGHT) {
			y[0]=0;
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(running) {
			move();
			checkApple();
			checkCollisions();
			
		}
		repaint();
	}

	class Adapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			
			switch(e.getKeyCode()) {
			case KeyEvent.VK_UP: 
				if(direction !='D') {
					direction='U';
					keepIn();
				}
				break;
			case KeyEvent.VK_DOWN: 
				if(direction !='U') {
					direction='D';
					keepIn();
				}
				break;
			case KeyEvent.VK_RIGHT: 
				if(direction !='L') {
					direction='R';
					keepIn();
				}
				break;
			case KeyEvent.VK_LEFT: 
				if(direction !='R') {
					direction='L';
					keepIn();
				}
				break;
			}
		}
	}
}
