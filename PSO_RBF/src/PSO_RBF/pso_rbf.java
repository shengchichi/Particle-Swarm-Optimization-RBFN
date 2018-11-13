package PSO_RBF;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class pso_rbf extends JFrame implements ActionListener{

	int size =1000;//必須為偶數
	int loop=1000;//迭代次數
	double error=0;
	double err_min = 1000;
	double print;
	double cognition = 0.6;
	double social = 0.4;
	double globalFitness = 1000;
	double vmax =0.5;
	double vmin =-0.5;
	boolean type=false;//4維是true
	int J=3;
	int p=3;
	int dim =4;
	ArrayList<Double> err;
	ArrayList<Double> global_p;
	ArrayList<Double> y = new ArrayList<Double>();//期望輸出
	ArrayList<ArrayList<Double>> x = new ArrayList<ArrayList<Double>>();
	ArrayList<Double> x_row = new ArrayList<Double>();
	int data[];
	String s[];//all files
	ArrayList<String> temp = new ArrayList<String>();
	ArrayList<String> temp2 = new ArrayList<String>();
	JButton enter = new JButton("輸入");
	JTextField lp = new JTextField("迭代次數");
	JTextField pop = new JTextField("族群大小");
	String[] str = {"4維","6維"};
	JComboBox cb = new JComboBox(str);
	Individual ind[];
	
	pso_rbf()
	{
		this.setSize(400,300);
		this.add(lp);
		 lp.setBounds(20,20,100,25);
		this.add(pop);
		 pop.setBounds(20,60,100,25);
		this.add(cb);
		 cb.setBounds(20,100,100,25);
		this.add(enter);
		 enter.setBounds(200,160,100,30);
		 enter.addActionListener(this);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setResizable(false);
	    this.setLocationRelativeTo(null);
	    this.setLayout(null);
	    this.setVisible(true);
	}
	public void start()
	{
		scan();
		func();//正規畫 y
		create();
		int count=0;
		double t=0;
		for(int i=0;i<loop;i++)
		{
			calculate();//計算適應函
			update();
			if(count == 100)
				break;
			if(err_min<0.04)
				break;
			if(t==err_min)
				count++;
			else
				count=0;
			t = err_min;
			
		}
		System.out.println("err: "+err_min +" "+globalFitness+" "+ global_p);
		new run(global_p,type);
	}
	public void update()
	{
		for(int q=0;q<size;q++)
		{
			ArrayList<Double> v = new ArrayList<Double>(ind[q].get_v());
			ArrayList<Double> local_p = new ArrayList<Double>(ind[q].getlocal());
			ArrayList<Double> x = new ArrayList<Double>(ind[q].get_x());
			//System.out.println("v : "+v);
			//System.out.println("l : "+local_p);
			//System.out.println("x : "+x);
			//System.out.println("b: "+x);
			//System.out.println(global_p);
			for(int i=0;i<v.size();i++)
			{
				
				double temp = v.get(i)+ cognition*(local_p.get(i)-x.get(i))+ social*(global_p.get(i)-x.get(i));
				//System.out.println("temp:"+temp);
				if(i==0)
				{
					if(temp>vmax)
						temp = vmax;
					else if(temp < vmin)
						temp = vmin;
				}
				else if(i>=1&&i<=J)
				{
					if(temp>vmax)
						temp = vmax;
					else if(temp < vmin)
						temp = vmin;
				}
				else if(i>=p&&i<=p*J)
				{
					if(temp>vmax*30)
						temp = vmax;
					else if(temp < vmin*30)
						temp = vmin;
				}
				else
				{
					if(temp>vmax*10)
						temp = vmax;
					else if(temp < vmin*10)
						temp = vmin;
				}
				//System.out.println(temp);
				v.set(i,temp);
				x.set(i, x.get(i)+v.get(i));
			}
			//System.out.println("a: "+x);
			ind[q].setx(x);
			ind[q].setv(v);
			//System.out.println("x : "+ind[q].get_x());
		}
		
	}
	public void create()
	{
		ind = new Individual[size];
		for(int i=0;i<size;i++)
		{
			ind[i] = new Individual();
			ind[i].setp(p);
			ind[i].random();
			//System.out.println(ind[i].get_x());
		}
	}
	public void calculate()
	{
		for(int i=0;i<size;i++)
		{
			double theta;
			double[][] m = new double[J+1][p+1];
			double[] w = new double[J+1];
			double[] fi = new double[J+1];
			theta = ind[i].gettheta();
			m = ind[i].getm();
			w = ind[i].getw();
			fi = ind[i].getfi();
			double delta = 0;
			double temp = 0;//f
			double fitness = 0;
			double out = 0;//rbf輸出=>F
			error =0;
			for(int list=0;list<x.size();list++)
			{
				out =0;
				for(int j=1;j<=J;j++)
				{
					temp = 0;
					for(int d=1;d<=p;d++)
					{
						//System.out.println(x.get(list).get(d-1));
						temp += (x.get(list).get(d-1)-m[j][d])*(x.get(list).get(d-1)-m[j][d]);
					}
					delta = Math.exp(-temp/(2*fi[j]*fi[j]));
					out += w[j]*delta;
				}
				out += theta;
				
				fitness += (y.get(list)-out)*(y.get(list)-out)/2;
				error += Math.abs((y.get(list)-out)/y.size());
			}
			if(fitness < ind[i].localFitness)//紀錄局部最佳值
			{
				ind[i].setFitness(fitness);
			}
			//System.out.println(error);
			if(error<err_min){
				err_min = error;
				err = new ArrayList<Double>(ind[i].get_x());
			}
		}
		//更新全域極小值
		
		for(int i=0;i<size;i++)
		{
			double a = ind[i].localFitness;
			//System.out.print(a+" ");
			if(a<globalFitness){
				
				globalFitness = a;
				global_p = new ArrayList<Double>(ind[i].getlocal());
			}
		}
		//System.out.println();
		//System.out.println("g: "+globalFitness);
		
	}
	
	public void func()
	{
		for(int i=0;i<y.size();i++)
		{
			y.set(i, (y.get(i)+40)/80);
		}
			
	}
	public void scan()
	{
		File f ;
		String ss;
		if(type==true)
		{
			f = new File("data3d");
			ss = "data3d/";
		}
		else
		{
			f = new File("data5d");
			ss = "data5d/";
		}
		if(f.isDirectory())//若讀取到的是資料夾
		{
			s=f.list();//檔案list
			//System.out.println(s.length+s[0]);
		}
		data = new int[s.length];
		for(int i=0;i<s.length;i++)
		{
			File file = new File(ss+s[i]);
			
	        Scanner sc = null;
			try {
				sc = new Scanner(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        int count = 0;
	        while (sc.hasNext()) 
	        {
	        	count++;
	        	String str = sc.next();
	        	//System.out.println(str);
	        	if(count%dim==0)
	        		temp.add(str);
	        	else
	        		temp2.add(str);
	        }
	        data[i] = count/dim;
	        int index=0;
	        for(int j=0;j<data[i];j++)
	        {
	        	y.add(Double.parseDouble(temp.get(j)));
	        	x_row = new ArrayList<Double>();
	        	for(int k=1;k<p+1;k++)
	        	{
	        		x_row.add(Double.parseDouble(temp2.get(index)));
	        		index++;
	        	}
	        	x.add(x_row);
	        }
	        
	        temp2.clear();
	        temp.clear();
		}
		//System.out.println(x.get(60).get(2));
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==enter)
		{
			enter.setEnabled(false);
			loop = Integer.parseInt(lp.getText());
			size = Integer.parseInt(pop.getText());
			if(cb.getSelectedIndex()==0)
			{
				type = true;
				dim=4;
				p=3;
			}
			else
			{
				type = false;
				dim = 6;
				p=5;
			}
			//System.out.println(type);
			start();
		}
		
	}
}
