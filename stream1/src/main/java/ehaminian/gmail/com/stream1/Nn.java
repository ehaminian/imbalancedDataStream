package ehaminian.gmail.com.stream1;

import moa.classifiers.rules.functions.Perceptron;
import moa.streams.ArffFileStream;
import moa.core.Example;
import moa.core.InstanceExample;
import moa.evaluation.BasicRegressionPerformanceEvaluator;

import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import com.github.javacliparser.FloatOption;
import com.yahoo.labs.samoa.instances.Instance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.xy.XYSeries;


public class Nn extends App{
		
		public static void main(String[] args) throws IOException {
		customelearningRatioOption.setValue(0.01);
		BasicRegressionPerformanceEvaluator eval= getBasicRegressionPerformanceEvaluator();
		XYSeries series = new XYSeries("MeanError");
		XYSeries series1 = new XYSeries("Prediction value");
		XYSeries series2 = new XYSeries("True value");
		XYSeries series12 = new XYSeries("True value");
		XYSeries series11 = new XYSeries("Prediction value");
		XYSeries series13 = new XYSeries("Normal value");
//		ArffFileStream stream=new ArffFileStream("C:\\Users\\Ehsan\\Desktop\\WORK\\Concrete_Data1.arff",9);
//		ArffFileStream stream=new ArffFileStream("C:\\Users\\Ehsan\\Desktop\\GERMANY\\2dplanes.arff",11);
		ArffFileStream stream=new ArffFileStream("C:\\Users\\Ehsan\\Desktop\\conference\\Data\\mv_shuffeled.arff",11);
		stream.prepareForUse();
		
		Perceptron Learner=new Perceptron();
		Learner.setModelContext(stream.getHeader());
		Learner.prepareForUse();
		Example<Instance> example=null;
		int i=0;
		final List list = new ArrayList();
		Learner.learningRatioOption=customelearningRatioOption;
		while (stream.hasMoreInstances()) {
			InstanceExample tmp = stream.nextInstance();
			trainInst = tmp.getData();
//			System.out.println(trainInst);
			OurPrediction=Learner.getVotesForInstance(trainInst);
			Learner.trainOnInstance(tmp);
			example=new  InstanceExample(trainInst);
	        eval.addResult(example, OurPrediction);
	        series.add(i, eval.getMeanError());
	        series1.add(i,OurPrediction[0]);
	        series2.add(i,trainInst.classValue());
	        list.add(new Double(trainInst.classValue()));
//	        showArray(Learner.getWeights());
//	        System.out.println(trainInst.classValue()+"=>"+OurPrediction[0]);
	        i=i+1;
	        if (i>800)
	        {break;}
		}
		i=1;
		Perceptron baseLearner=new Perceptron();
		baseLearner.setModelContext(stream.getHeader());
		baseLearner.prepareForUse();
		while (stream.hasMoreInstances()) {
			InstanceExample tmp = stream.nextInstance();
			trainInst = tmp.getData();
			OurPrediction=Learner.getVotesForInstance(trainInst);
			NormalPrediction=baseLearner.getVotesForInstance(trainInst);
			baseLearner.trainOnInstance(tmp);
	        series12.add(i,trainInst.classValue());
	        series11.add(i,OurPrediction[0]);
	        series13.add(i,NormalPrediction[0]);
	        i=i+1;
		}
		String title="Mean Squre Error";
        drawchart meansquaregraph = new drawchart(title,series);
        meansquaregraph.pack();
        RefineryUtilities.centerFrameOnScreen(meansquaregraph);
        meansquaregraph.setVisible(true);
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        LineChartEx2 tmp=new LineChartEx2(dataset);
        tmp.setVisible(true);
        
        XYSeriesCollection dataset1 = new XYSeriesCollection();
        dataset1.addSeries(series11);
        dataset1.addSeries(series12);
        dataset1.addSeries(series13);
        LineChartEx2 tmp1=new LineChartEx2(dataset1);
        tmp1.setVisible(true);
        
        final DefaultBoxAndWhiskerCategoryDataset dataBoxplot = new DefaultBoxAndWhiskerCategoryDataset();
        dataBoxplot.add(list, "TargetValue " + 1, " Type " + 1);
        final BoxAndWhiskerDemo demo = new BoxAndWhiskerDemo("Box-and-Whisker Chart Demo",dataBoxplot);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
//        System.out.println(list);
		
		
		
	}
}
