import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.util.ArrayList;



//Fatema Hesham Abdulrahman -Data Science Track 

public class MyPaint extends Applet{
	
	Button rectangle,redButton,line,blueButton,greenButton,oval,clearButton,freeHand,earase;
	Checkbox checkBox;
	
	
	//saved shapes to use it in repaint() method 
	ArrayList<GeoShape> geoShapes = new ArrayList<GeoShape>();
	
	//My coordinates and options 
	int x1,y1,x2,y2;
	boolean solid = false;
	
	//MY constants 
	public static final int RECTANGLE=1;
	public static final int LINE=2;
	public static final int OVAL=3;
	public static final int FREE_HAND=4;
	public static final int EARASE=5;
	public static final int CLEAR_ALL=6;
	//My current states to transfere frome mood to another or from color to another
	Color currentColor;
	Color previousColor;
	int currentMood;
	boolean getPrevColor;
	
	public void init (){
		
		currentColor = Color.BLACK;
		
		//ClearAll button
		clearButton = new Button("Clear All");
		clearButton.setBackground(Color.white);
		clearButton.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent ev){
				previousColor = currentColor;
				currentMood = CLEAR_ALL;
				geoShapes.clear();
				repaint();
			}
		});
		add(clearButton);
		
		//Red button
		redButton = new Button("Red");
		redButton.setBackground(Color.red);
		redButton.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent ev){
				currentColor = Color.RED;
			}
		});
		add(redButton);
		
		//Blue button
		blueButton = new Button("Blue");
		blueButton.setBackground(Color.blue);
		blueButton.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent ev){
				currentColor = Color.BLUE;
			}
		});
		add(blueButton);
		
		//Green button
		greenButton = new Button("Green");
		greenButton.setBackground(Color.green);
		greenButton.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent ev){
				currentColor = Color.GREEN;
			}
		});
		add(greenButton);
		
		//Buttonn rectangle
		rectangle = new Button("Rectangle");
		rectangle.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent ev){
				addMouseListener(new MyPress());
				addMouseMotionListener(new MyDrag());
				currentMood=RECTANGLE;
				x1=0;x2=0;y1=0;y2=0;
				repaint();
			}
		});
		add(rectangle);
		
		//Button Line
		line = new Button("Line");
		line.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent ev){
				addMouseListener(new MyPress());
				addMouseMotionListener(new MyDrag());
				currentMood=LINE;
				x1=0;x2=0;y1=0;y2=0;
				repaint();
			}
		});
		add(line);
		
		//Button Oval
		oval = new Button("Oval");
		oval.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent ev){
				addMouseListener(new MyPress());
				addMouseMotionListener(new MyDrag());
				currentMood=OVAL;
				x1=0;x2=0;y1=0;y2=0;
				repaint();
			}
		});
		add(oval);
		
		//Button Free Hand
		freeHand = new Button("Free Hand");
		freeHand.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent ev){
				addMouseListener(new MyPress());
				addMouseMotionListener(new MyDrag());
				
				currentMood=FREE_HAND;
			}
		});
		add(freeHand);
		
		//Button Erase
		earase = new Button("Erase");
		earase.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent ev){
				addMouseListener(new MyPress());
				addMouseMotionListener(new MyDrag());
				previousColor = currentColor;
				currentColor = Color.WHITE;
				getPrevColor = true;
				currentMood=EARASE;
			}
		});
		add(earase);
		
		//checkBox solid
		checkBox = new Checkbox ("Solid");
		checkBox.addItemListener(new ItemListener() {    
             public void itemStateChanged(ItemEvent e) {                 
                solid = (e.getStateChange()==1)?true:false;
             }    
          });   
		add(checkBox);
		
		
		
	}
	//to get the min x and min y then absolute of differences cause the fuction always begin its draw from the top left corner
	public int [] topLeftCorner (){
		int minX = Math.min(x1,x2);
		int minY = Math.min(y1,y2);
		int width = Math.abs(x1-x2);
		int height = Math.abs(y1-y2);
		int [] arr = {minX,minY,width,height};
		return arr;
	}
	
	public void paint(Graphics g){
		for (GeoShape i: geoShapes){
			g.setColor(i.color);
			i.draw(g);		
		}
		g.setColor(currentColor);
		
		//get previouse color if i clicked the erase button 
		if(getPrevColor){
			currentColor = previousColor;
			getPrevColor = false;
		}
		//Get top left corner
		int [] point = topLeftCorner();
		switch (currentMood){
			
			case RECTANGLE:
				if(solid)
					g.fillRect(point[0], point[1], point[2], point[3]);
				else
					g.drawRect(point[0], point[1], point[2], point[3]);
			break;
			case LINE:
				g.drawLine(x1,y1,x2,y2);
			break;
			case OVAL:
				if(solid)
					g.fillOval(point[0], point[1], point[2], point[3]);
				else
					g.drawOval(point[0], point[1], point[2], point[3]);
			break;
			case FREE_HAND:
				g.drawLine(x1,y1,x2,y2);
			break;
			
			
			
		}
		
	}
	
	
	abstract class GeoShape{
		protected int x1,x2,y1,y2;
		protected boolean solid;
		protected Color color;
		String shapeType;
		
		public GeoShape(int x1, int y1, int x2, int y2,boolean s){
			this.x1=x1;
			this.x2=x2;
			this.y1=y1;
			this.y2=y2;
			solid=s;
			this.color=currentColor;
		}
		public GeoShape(int x1, int y1, int x2, int y2){
			this.x1=x1;
			this.x2=x2;
			this.y1=y1;
			this.y2=y2;
			this.color=currentColor;
		}
		
		// this function for each object to get the top left corner if i need "Rectangle,Oval"
		public int [] topLeftCorner (){
			int minX = Math.min(getX1(),getX2());
			int minY = Math.min(getY1(),getY2());
			int width = Math.abs(getX1()-getX2());
			int height = Math.abs(getY1()-getY2());
			int [] arr = {minX,minY,width,height};
			return arr;
		}
		abstract protected void draw(Graphics g);
		//Setters and getters to prevent the data from the miss used
		protected void setX1(int x1){this.x1=x1;}
		protected void setY1(int y1){this.y1=y1;}
		protected void setX2(int x2){this.x2=x2;}
		protected void setY2(int y2){this.y2=y2;}
		protected int getX1(){return x1;}
		protected int getX2(){return x2;}
		protected int getY1(){return y1;} 
		protected int getY2(){return y2;} 
		protected void setPointOne(int x1,int y1){
			this.x1 = x1;
			this.y1 = y1;
		}
		protected void setPointTwo(int x2,int y2){
			this.x2 = x2;
			this.y2 = y2;
		}
		protected int []  getPointOne(){
			int [] p1 = {x1,y1};
			return p1;
		}
		protected int []  getPointTwo(){
			int [] p2 = {x2,y2};
			return p2;
		}
	}
	class Rectangle extends GeoShape{
		public Rectangle(int x1, int y1, int x2, int y2, boolean s){
			super(x1, y1, x2, y2, s);
			this.shapeType = "Rectangle";
			
		}
		public Rectangle(int x1, int y1, int x2, int y2, boolean s,Color color){
			super(x1, y1, x2, y2, s);
			this.shapeType = "Rectangle";
			this.color = color;
		}
		public void draw(Graphics g){
			int [] point = topLeftCorner();
			
			if(this.solid)
				g.fillRect(point[0], point[1], point[2], point[3]);
			else
				g.drawRect(point[0], point[1], point[2], point[3]);
		}
	}
	
	class Line extends GeoShape{
		public Line (int x1,int y1, int x2, int y2){
			super(x1,y1,x2,y2);
			this.shapeType = "Line";
		}
		
		public void draw(Graphics g){
			g.drawLine(x1,y1,x2,y2);
		}
	}
	
	class Oval extends GeoShape{
		public Oval(int x1, int y1, int x2, int y2, boolean s){
			super(x1, y1, x2, y2, s);
			this.shapeType = "Oval";
			
		}
		public void draw(Graphics g){
			
			//get top left corner same as rectangle
			int [] point = topLeftCorner();
			
			if(this.solid)
				g.fillOval(point[0], point[1], point[2], point[3]);
			
			else
				g.drawOval(point[0], point[1], point[2], point[3]);
		}
	}
	class MyPress implements MouseListener{
		public void mousePressed (MouseEvent e){
			//my first point
			x1 = e.getX();
			y1 = e.getY();
		}
		public void mouseReleased (MouseEvent e){
			
			//get the end point
			x2 = e.getX();
			y2 = e.getY();
			repaint();
			boolean s = solid;
			
			switch (currentMood){
				case RECTANGLE:
					geoShapes.add(new Rectangle(x1,y1,x2,y2,s)); // add new Rectangle cause the arr create refrences so i need to create object
					previousColor = currentColor;					
				break;
				case LINE:
					geoShapes.add(new Line(x1,y1,x2,y2));
					previousColor = currentColor;
				break;
				case OVAL:
					geoShapes.add(new Oval(x1,y1,x2,y2,s)); 
					previousColor = currentColor;
				break;
				case EARASE:
					currentColor = previousColor;
				break;
				case CLEAR_ALL:
					currentColor = previousColor;
				break;
			}
			
		}
		
		public void mouseClicked (MouseEvent e){}
		public void mouseEntered (MouseEvent e){}
		public void mouseExited (MouseEvent e){}
	}
	
	class MyDrag implements MouseMotionListener{
		public void mouseDragged (MouseEvent ev){
			
			//while dragging I need always the x2,y2 to paint the shape while this process
			x2 = ev.getX();
			y2 = ev.getY();
			repaint();
			
			if(currentMood == FREE_HAND){
				//we add the line as a new line from prev pixel to next pixel 
				geoShapes.add(new Line(x1,y1,x2,y2));
				//the beginning of next pixel while drawing will be the end of the prev pixel
				x1 = x2; 
				y1 = y2;
			}
			
			// Inside the mouseDragged method
			if (currentMood == EARASE) {
				geoShapes.add(new Rectangle(x2, y2, x2 + 50, y2 + 30, true, Color.WHITE));
			}

		}
		public void mouseMoved (MouseEvent e){}
	}
	
}