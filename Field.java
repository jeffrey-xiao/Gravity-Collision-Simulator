import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JPanel;


public class Field extends JPanel implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// ArrayList holding the active circles in the window
	private ArrayList<Circle> active = new ArrayList<Circle>();
	
	// some containers
	private double prevX;
	private double prevY;
	private long prevTime;
	
	Field () {
		addMouseListener(this);
	}
	// painting all the active balls
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		for (Circle c : active) {
			g.drawOval((int)(c.x - c.radius), (int)(c.y - c.radius), (int)(c.radius*2), (int)(c.radius*2));
		}
	}
	// updating all the active circles
	public void update () {
		for (int i = 0; i < active.size(); i++) {
			Circle curr = active.get(i);
			curr.ys += Window.GRAVITY / Window.FRAMESPERSEC;
			curr.move();
			if (curr.x < curr.radius) {
				curr.x = curr.radius;
				curr.xs *= -Window.RESISTITION;
			} else if (curr.x > this.getWidth() - curr.radius) {
				curr.x = this.getWidth() - curr.radius;
				curr.xs *= -Window.RESISTITION;
			}
			if (curr.y < curr.radius) {
				curr.y = curr.radius;
				curr.ys *= -Window.RESISTITION;
			} else if (curr.y > this.getHeight() - curr.radius) {
				curr.y = this.getHeight() - curr.radius;
				curr.ys *= -Window.RESISTITION;
			}
		}
		for (int i = 0; i < active.size(); i++) {
			for (int j = i + 1; j < active.size(); j++) {
				Circle c1 = active.get(i);
				Circle c2 = active.get(j);
				if (c1.isCollide(c2)) {
					handleCollision(c1, c2);
				} else {
				}
				
			}
		}
	}
	// handling the collisions and updating the velocity (x and y components)
	private void handleCollision (Circle c1, Circle c2) {
		double nxs = c2.x - c1.x;
		double nys = c2.y - c1.y;
		double unxs = nxs / Math.sqrt((nxs*nxs + nys*nys));
		double unys = nys / Math.sqrt((nxs*nxs + nys*nys));
		double utxs = -unys;
		double utys = unxs;
		double n1 = unxs * c1.xs + unys * c1.ys;
		double nt1 = utxs * c1.xs + utys * c1.ys;
		double n2 = unxs * c2.xs + unys * c2.ys;
		double nt2 = utxs * c2.xs + utys * c2.ys;
		
//		double nn1 = ((Window.RESISTITION+1)*c2.area*n2 + n1*(c1.area - Window.RESISTITION*c2.area))/(c1.area + c2.area);
//		double nn2 = ((Window.RESISTITION+1)*c1.area*n1 - n2*(c1.area - Window.RESISTITION*c2.area))/(c1.area + c2.area);
		double nn1 = (c2.area*(n2 - n1)*Window.RESISTITION + c2.area * n2 + c1.area * n1)/(c1.area + c2.area);
		double nn2 = (c1.area*(n1 - n2)*Window.RESISTITION + c2.area * n2 + c1.area * n1)/(c1.area + c2.area);
		c1.xs = nn1 * unxs + nt1 * utxs;
		c1.ys = nn1 * unys + nt1 * utys;
		
		c2.xs = nn2 * unxs + nt2 * utxs;
		c2.ys = nn2 * unys + nt2 * utys;
		
	}
	@Override
	public void mouseClicked (MouseEvent e) {
		
	}
	// get the location of the new circle
	@Override
	public void mousePressed (MouseEvent e) {
		prevX = e.getX();
		prevY = e.getY();
		prevTime = System.currentTimeMillis();
	}
	// "spawn" the new circle and determine its speed and size
	@Override
	public void mouseReleased (MouseEvent e) {
		Circle curr = new Circle(prevX, prevY, Math.min(30, 10 + (System.currentTimeMillis() - prevTime) / 100), (prevX - e.getX()) / Window.FRAMESPERSEC, (prevY - e.getY()) / Window.FRAMESPERSEC); 
		boolean valid = true;
		for (Circle c : active)
			if (c.isCollide(curr))
				valid = false;
		if (valid)
			active.add(curr);
	}

	@Override
	public void mouseEntered (MouseEvent e) {
	}

	@Override
	public void mouseExited (MouseEvent e) {
	}
	static class Circle {
		// coordinate representing the center of the circle
		double x, y;

		double radius;
		double xs, ys;
		double area;
		Circle (double x, double y, double radius) {
			this(x, y, radius, 0, 0);
		}
		Circle (double x, double y, double radius, double xs, double ys) {
			this.x = x;
			this.y = y;
			this.radius = radius;
			this.xs = xs;
			this.ys = ys;
			area = radius*radius*Math.PI;
		}
		// check if two circles are colliding
		public boolean isCollide (Circle c) {
			return distCircle(c) <= 0;
		}
		// distance between two circles
		public double distCircle (Circle c) {
			return Math.sqrt((x - c.x)*(x - c.x) + (y - c.y)*(y - c.y)) - radius - c.radius;
		} 
		// stimulate one tick
		public void move () {
			x += xs;
			y += ys;
		}
		// go back one tick
		public void back () {
			x -= xs;
			y -= ys;
		}
	}
}
