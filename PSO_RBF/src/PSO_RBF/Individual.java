package PSO_RBF;

import java.util.ArrayList;
import java.util.Random;

public class Individual {
	private int J = 3;
	private int p = 3;
	private double theta;
	private double w[];
	private double m[][];
	private double fi[];
	double localFitness=1000;
	private ArrayList<Double> x = new ArrayList<Double>();
	ArrayList<Double> velocities = new ArrayList<Double>();
	private ArrayList<Double> local_p = new ArrayList<Double>();
	Random ran = new Random();
	public Individual() 
	{
		
	}
	public void random()
	{
		double v;
		w = new double[J+1];
		m = new double[J+1][p+1];
		fi = new double[J+1];
		theta = ran.nextDouble();
		x.add(theta);
		v = ran.nextDouble()*0.4-0.2;
		velocities.add(v);
		for(int i=1;i<J+1;i++)
		{
			 w[i] = ran.nextDouble();//0~1
			 x.add(w[i]);
			 v = ran.nextDouble()*0.4-0.2;
			 velocities.add(v);
		}
		for(int i=1;i<J+1;i++)
		{
			for(int j=1;j<=p;j++)
			{
				 m[i][j] = ran.nextDouble()*30;//0~30
				 x.add(m[i][j]);
				 v = ran.nextDouble()*12-6;
				 velocities.add(v);
			}
		}
		for(int i=1;i<J+1;i++)
		{
			 fi[i] = ran.nextDouble()*10;//0~10
			 x.add(fi[i]);
			 v = ran.nextDouble()*4-2;
			 velocities.add(v);
		}
		
	}
	public void setx(ArrayList<Double> x)
	{
		this.x = new ArrayList<Double>(x);
		x_decode();
	}
	private void x_decode()
	{
		theta = x.get(0);
		if(theta>1){
			theta = 1;
			x.set(0,1.0);
		}
		else if(theta<0){
			theta = 0;
			x.set(0,0.0);
		}
		int index=1;
		for(int j=1;j<=J;j++){
			if(x.get(index)>1)
			{
				w[j] = 1;
				x.set(index,1.0);
			}	
			else if(x.get(index)<0){
				x.set(index,0.0);
				w[j] = 0;
			}
			else
				w[j] = x.get(index);
			index++;
		}
		for(int s=1;s<J+1;s++)
		{
			for(int t=1;t<=p;t++)
			{
				if(x.get(index)>30){
					x.set(index,30.0);
					m[s][t] = 30;
				}
				else if(x.get(index)<0){
					x.set(index,0.0);
					m[s][t] = 0;
				}
				else
					m[s][t] = x.get(index);
				index++;
			}
		}
		for(int k=1;k<=J;k++){
			if(x.get(index)>10){
				x.set(index,10.0);
				fi[k] = 10;
			}
			else if(x.get(index)<0){
				x.set(index,0.0);
				fi[k] = 0;
			}
			else
				fi[k] = x.get(index);
			index++;
		}

	}
	public void setFitness(double fitness)
	{
		
		localFitness = fitness;
		local_p = x;
	}
	public void setp(int p)
	{
		this.p = p;
	}
	public ArrayList<Double> get_x()
	{
		return x;
	}

	public double[] getw()
	{
		return w; 
	}
	public double[] getfi()
	{
		return fi; 
	}
	public double[][] getm()
	{
		return m; 
	}
	public double gettheta()
	{
		return theta;
	}
	public ArrayList<Double> getlocal()
	{
		return local_p;
	}
	public ArrayList<Double> get_v()
	{
		return velocities;
	}
	public void setv(ArrayList<Double> velocities)
	{
		this.velocities = new ArrayList<Double>(velocities);
	}
}
