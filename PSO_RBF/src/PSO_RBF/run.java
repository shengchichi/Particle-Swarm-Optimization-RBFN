package PSO_RBF;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class run extends JFrame{
	int J=3;
	int p =3;
	double x_direction[];
	double x=0;
	double y=0;
	double x_d;
	double x_l;
	double x_r;
	double y_d;
	double y_l;
	double y_r;
	double d1=22;//直線距離
	double d2=8.4852814;//left distance
	double d3=8.4852814;//right distance
	double[][] m;
	double[] w;
	double[] fi;
	double all[][] = new double[10000][2];
	double degree = 0;//旋轉角度
	double f=90;//與水平角度
	double delta = 0;
	double temp = 0;
	double fitness = 0;
	double out =0;
	double theta;
	int count =0;
	boolean type;
	
	JLabel label = new JLabel("旋轉角度(測試用)");
	JLabel err = new JLabel();
	
	JButton start = new JButton("開始");
	Line2D line_bot = new Line2D.Double(-400, 0, 400, 0);
    Line2D line_top = new Line2D.Double(-400, -500, 400, -500);
    Line2D line_end = new Line2D.Double(180, -370, 300, -370);
    Line2D line1 = new Line2D.Double(-60, 0, -60, -220);
    Line2D line2 = new Line2D.Double(60, 0, 60, -100);
    Line2D line3 = new Line2D.Double(-60, -220, 180, -220);
    Line2D line4 = new Line2D.Double(60, -100, 300, -100);
    Line2D line5 = new Line2D.Double(180, -220, 180, -500);
    Line2D line6 = new Line2D.Double(300, -100, 300, -500);
	run(ArrayList<Double> dna, boolean type) 
	{
		super("MAP");
	    this.setSize(800,800);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setResizable(false);
	    this.setLocationRelativeTo(null);
	    this.setLayout(null);
	    this.setVisible(true);
	    this.type = type;
	    
	    if(type == true)
	    {
	    	x_direction = new double[4];
	    	p=3;
	    	x_direction[1] = d1;
			x_direction[2] = d3;
			x_direction[3] = d2;
	    }
	    else
	    {
	    	x_direction = new double[6];
	    	p=5;
	    	x_direction[1] = x;
			x_direction[2] = y;
	    	x_direction[3] = d1;
			x_direction[4] = d3;
			x_direction[5] = d2;
	    }
	    
		
		decode(dna);
		display();
	}
public void display() {
	while(true)
	{
		count++;
		//System.out.println(x+" "+y);
		//System.out.println(x_direction[1]+" "+x_direction[2]+" "+x_direction[3]);
		cal_degree();
		move();
		get_x();
		
		if(y>=34)
			break;
		if(y<0)
			break;
		if(x<-6)
			break;
		if(x>30)
			break;
		repaint();
	}	
	
}
public void update(Graphics g) { 
    	
        this.paint(g);  // 單純呼叫paint() 
    } 
    public void paint(Graphics g) { 
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g.translate(400, 770);
        g2.draw(line_top);
        g2.draw(line_bot);
        g2.draw(line1);
        g2.draw(line2);
        g2.draw(line3);
        g2.draw(line4);
        g2.draw(line5);
        g2.draw(line6);
        g2.setColor(Color.RED);
        g2.draw(line_end);
        
        for(int j=0;j<count;j++)
        {
        	g2.draw(new Ellipse2D.Double(10*(all[j][0]-3), -10*(all[j][1]+3), 60,60 ));
        }
        g2.draw(new Ellipse2D.Double(10*(x-3), -10*(y+3), 60,60 ));
        g2.setColor(Color.BLUE);
        g.fillOval(237, -373, 6, 6);//END
        // 繪圖動作 
    } 
	public void move()
	{
		f = f - Math.toDegrees(Math.asin(2*Math.sin(Math.toRadians(degree))/3));
		x = x + Math.cos(Math.toRadians(degree+f)) + Math.sin(Math.toRadians(degree))*Math.sin(Math.toRadians(f));
		y = y + Math.sin(Math.toRadians(degree+f)) - Math.sin(Math.toRadians(degree))*Math.cos(Math.toRadians(f));
		all[count][0] = x;
        all[count][1] = y;
	}
	public double dis(double x1,double y1,double x2,double y2)
	{
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	public void get_x()
	{
		
		if(f==90)
		{
			if(x<=18&&x>-6)
			{
				x_d = x;
				y_d = 22;
			}
			else if(x>18&&x<30)
			{
				x_d = x;
				y_d = 30;
			}
			d1 = 22 - y;
		}
		else if(f > 90)
		{
			
			double temp = Math.tan(Math.toRadians(f)) * (-10 - x) + y;
			double min = 1000; 
			Line2D check = new Line2D.Double(10*x, -y*10, -100, -temp*10);
			boolean result1 = line1.intersectsLine(check);
			boolean result3 = line3.intersectsLine(check);
			boolean result5 = line5.intersectsLine(check);
			boolean resultt = line_top.intersectsLine(check);
			//System.out.println(x+" "+y+" "+temp);
			if(result1 == true)
			{
				temp = Math.tan(Math.toRadians(f)) * (-6 - x) + y;
				if(dis(x,y,-6,temp) < min){
					min = dis(x,y,-6,temp);
					x_d =-6;
					y_d = temp;
				}
			}
			
			if(result3 == true)
			{
				temp = (22-y) / Math.tan(Math.toRadians(f)) +x;
				if(dis(x,y,temp,22) < min){
					min = dis(x,y,temp,22);
					x_d =temp;
					y_d = 22;
				}
			}
			
			if(result5 == true)
			{
				temp = Math.tan(Math.toRadians(f)) * (18 - x) + y;
				if(dis(x,y,18,temp) < min){
					min = dis(x,y,18,temp);
					x_d = 18;
					y_d = temp;
				}
			}
			if(resultt == true)
			{
				temp = (37-y) / Math.tan(Math.toRadians(f)) + x;
				if(dis(x,y,temp,37) < min){
					min = dis(x,y,temp,37);
					x_d =temp;
					y_d = 37;
				}
			}
			d1 = min;
		}
		else
		{
			double temp = Math.tan(Math.toRadians(f)) * (35 - x) + y;
			double min=1000; 
			Line2D check = new Line2D.Double(10*x, -y*10, 350, -temp*10);
			boolean result2 = line2.intersectsLine(check);
			boolean result3 = line3.intersectsLine(check);
			boolean result4 = line4.intersectsLine(check);
			boolean result6 = line6.intersectsLine(check);
			boolean resultt = line_top.intersectsLine(check);
			if(result2 == true)
			{
				temp = Math.tan(Math.toRadians(f)) * (6 - x) + y;
				if(dis(x,y,6,temp) < min){
					min = dis(x,y,6,temp);
					x_d = 6;
					y_d = temp;
				}
			}
			if(result3 == true)
			{
				temp = (22-y) / Math.tan(Math.toRadians(f)) + x;
				if(dis(x,y,temp,22) < min){
					min = dis(x,y,temp,22);
					x_d =temp;
					y_d = 22;
				}
			}
			if(result4 == true)
			{
				temp = (10-y) / Math.tan(Math.toRadians(f)) + x;
				if(dis(x,y,temp,10) < min){
					min = dis(x,y,temp,10);
					x_d =temp;
					y_d = 10;
				}
			}
			if(result6 == true)
			{
				temp = Math.tan(Math.toRadians(f)) * (30 - x) + y;
				if(dis(x,y,30,temp) < min){
					min = dis(x,y,30,temp);
					x_d = 30;
					y_d = temp;
				}
			}
			if(resultt == true)
			{
				temp = (37-y) / Math.tan(Math.toRadians(f)) + x;
				if(dis(x,y,temp,37) < min){
					min = dis(x,y,temp,37);
					x_d =temp;
					y_d = 37;
				}
			}
			d1 = min;
			
			
		}
		if(f==45)
		{
			
		}
		else if(f>45)
		{
			double temp = Math.tan(Math.toRadians(f+45)) * (-10 - x) + y;
			double min = 1000; 
			Line2D check = new Line2D.Double(10*x, -y*10, -100, -temp*10);
			boolean result1 = line1.intersectsLine(check);
			boolean result3 = line3.intersectsLine(check);
			boolean result5 = line5.intersectsLine(check);
			//System.out.println(x+" "+y+" "+temp);
			if(result1 == true)
			{
				temp = Math.tan(Math.toRadians(f+45)) * (-6 - x) + y;
				if(dis(x,y,-6,temp) < min){
					min = dis(x,y,-6,temp);
					x_l =-6;
					y_l = temp;
				}
			}
			
			if(result3 == true)
			{
				temp = (22-y) / Math.tan(Math.toRadians(f+45)) +x;
				if(dis(x,y,temp,22) < min){
					min = dis(x,y,temp,22);
					x_l =temp;
					y_l = 22;
				}
			}
			
			if(result5 == true)
			{
				temp = Math.tan(Math.toRadians(f+45)) * (18 - x) + y;
				if(dis(x,y,18,temp) < min){
					min = dis(x,y,18,temp);
					x_l = 18;
					y_l = temp;
				}
			}
			d2 = min;
		}
		else
		{
			double temp = Math.tan(Math.toRadians(f+45)) * (35 - x) + y;
			double min=1000; 
			Line2D check = new Line2D.Double(10*x, -y*10, 350, -temp*10);
			boolean result2 = line2.intersectsLine(check);
			boolean result3 = line3.intersectsLine(check);
			boolean result4 = line4.intersectsLine(check);
			boolean result6 = line6.intersectsLine(check);
			boolean resultt = line_top.intersectsLine(check);
			if(result2 == true)
			{
				temp = Math.tan(Math.toRadians(f+45)) * (6 - x) + y;
				if(dis(x,y,6,temp) < min){
					min = dis(x,y,6,temp);
					x_l = 6;
					y_l = temp;
				}
			}
			if(result3 == true)
			{
				temp = (22-y) / Math.tan(Math.toRadians(f+45)) + x;
				if(dis(x,y,temp,22) < min){
					min = dis(x,y,temp,22);
					x_l =temp;
					y_l = 22;
				}
			}
			if(result4 == true)
			{
				temp = (10-y) / Math.tan(Math.toRadians(f+45)) + x;
				if(dis(x,y,temp,10) < min){
					min = dis(x,y,temp,10);
					x_l =temp;
					y_l = 10;
				}
			}
			if(result6 == true)
			{
				temp = Math.tan(Math.toRadians(f+45)) * (30 - x) + y;
				if(dis(x,y,30,temp) < min){
					min = dis(x,y,30,temp);
					x_l = 30;
					y_l = temp;
				}
			}
			if(resultt == true)
			{
				temp = (37-y) / Math.tan(Math.toRadians(f+45)) + x;
				if(dis(x,y,temp,37) < min){
					min = dis(x,y,temp,37);
					x_l =temp;
					y_l = 37;
				}
			}
			d2 = min;
		}
		if(f == 135)
		{
			
		}
		else if(f >135)
		{
			double temp = Math.tan(Math.toRadians(f-45)) * (-10 - x) + y;
			double min = 1000; 
			Line2D check = new Line2D.Double(10*x, -y*10, -100, -temp*10);
			boolean result1 = line1.intersectsLine(check);
			boolean result3 = line3.intersectsLine(check);
			boolean result5 = line5.intersectsLine(check);
			//System.out.println(x+" "+y+" "+temp);
			if(result1 == true)
			{
				temp = Math.tan(Math.toRadians(f-45)) * (-6 - x) + y;
				if(dis(x,y,-6,temp) < min){
					min = dis(x,y,-6,temp);
					x_r =-6;
					y_r = temp;
				}
			}
			
			if(result3 == true)
			{
				temp = (22-y) / Math.tan(Math.toRadians(f-45)) +x;
				if(dis(x,y,temp,22) < min){
					min = dis(x,y,temp,22);
					x_r =temp;
					y_r = 22;
				}
			}
			
			if(result5 == true)
			{
				temp = Math.tan(Math.toRadians(f-45)) * (18 - x) + y;
				if(dis(x,y,18,temp) < min){
					min = dis(x,y,18,temp);
					x_r = 18;
					y_r = temp;
				}
			}
			d3 = min;
		}
		else
		{
			double temp = Math.tan(Math.toRadians(f-45)) * (35 - x) + y;
			double min=1000; 
			Line2D check = new Line2D.Double(10*x, -y*10, 350, -temp*10);
			boolean result2 = line2.intersectsLine(check);
			boolean result3 = line3.intersectsLine(check);
			boolean result4 = line4.intersectsLine(check);
			boolean result6 = line6.intersectsLine(check);
			boolean resultt = line_top.intersectsLine(check);
			if(result2 == true)
			{
				temp = Math.tan(Math.toRadians(f-45)) * (6 - x) + y;
				if(dis(x,y,6,temp) < min){
					min = dis(x,y,6,temp);
					x_r = 6;
					y_r = temp;
				}
			}
			if(result3 == true)
			{
				temp = (22-y) / Math.tan(Math.toRadians(f-45)) + x;
				if(dis(x,y,temp,22) < min){
					min = dis(x,y,temp,22);
					x_r =temp;
					y_r = 22;
				}
			}
			if(result4 == true)
			{
				temp = (10-y) / Math.tan(Math.toRadians(f-45)) + x;
				if(dis(x,y,temp,10) < min){
					min = dis(x,y,temp,10);
					x_r =temp;
					y_r = 10;
				}
			}
			if(result6 == true)
			{
				temp = Math.tan(Math.toRadians(f-45)) * (30 - x) + y;
				if(dis(x,y,30,temp) < min){
					min = dis(x,y,30,temp);
					x_r = 30;
					y_r = temp;
				}
			}
			if(resultt == true)
			{
				temp = (37-y) / Math.tan(Math.toRadians(f-45)) + x;
				if(dis(x,y,temp,37) < min){
					min = dis(x,y,temp,37);
					x_r =temp;
					y_r = 37;
				}
			}
			d3 = min;
		}
		if(type == true)
		{
			x_direction[1] = d1;
			x_direction[2] = d3;
			x_direction[3] = d2;
		}
		else
		{
			x_direction[1] = x;
			x_direction[2] = y;
			x_direction[3] = d1;
			x_direction[4] = d3;
			x_direction[5] = d2;
		}
		
		//System.out.println("前: "+x_direction[1]+" 右: "+x_direction[2]+" 左: "+x_direction[3]);
			
		
	}
	public void cal_degree()
	{
		out =0;
		for(int j=1;j<=J;j++)
		{
			temp = 0;
			for(int d=1;d<=p;d++)
			{
				temp += (x_direction[d]-m[j][d])*(x_direction[d]-m[j][d]);
			}
			delta = Math.exp(-temp/(2*fi[j]*fi[j]));
			out += w[j]*delta;
		}
		out += theta;
		
		if(out>1)
			out=1;
		else if(out<0)
			out = 0;
		degree = out*80-40;
		//System.out.println(out+" deg: "+degree);
		//System.out.println();
	}
	public void decode(ArrayList<Double> dna)
	{
		m = new double[J+1][p+1];
		w = new double[J+1];
		fi = new double[J+1];
		theta = dna.get(0);
		int index=1;
		for(int i=1;i<=J;i++)
		{
			w[i] = dna.get(index);
			index++;
		}
		for(int i=1;i<J+1;i++)
		{
			for(int j=1;j<=p;j++)
			{
				m[i][j] = dna.get(index);
				index++;
			}
		}
		for(int i=1;i<J+1;i++)
		{
			fi[i] = dna.get(index);
			index++;
		}
	}

}
