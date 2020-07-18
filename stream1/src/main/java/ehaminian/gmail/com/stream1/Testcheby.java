package ehaminian.gmail.com.stream1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import moa.classifiers.rules.functions.Perceptron;
import moa.classifiers.trees.FIMTDD;
import moa.evaluation.BasicRegressionPerformanceEvaluator;
import moa.streams.ArffFileStream;

public class Testcheby extends App{
	public static void main(String[] args) throws IOException {
		
		
		for(int j=0;j<10;j++)
		{
			datasetnumber=Integer.toString(j);
			variable_initialization();
			BasicRegressionPerformanceEvaluator eval= getBasicRegressionPerformanceEvaluator();
			ArffFileStream stream=getStream();
			int i=0;
			while (stream.hasMoreInstances()) {
				 trainInst = stream.nextInstance().getData();
				 if(trainInst.classValue()>0.4)
				 {
					i++; 
				 }
			 }
		 show(i);
		}
	}
}
