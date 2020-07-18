package ehaminian.gmail.com.stream1;
import de.unknownreality.dataframe.DataFrame;
import moa.classifiers.rules.functions.TargetMean;
import moa.classifiers.trees.FIMTDD;
import moa.classifiers.rules.functions.Perceptron;
import moa.classifiers.rules.AMRulesRegressor;
import moa.classifiers.functions.SGD;
import moa.streams.ArffFileStream;
import java.lang.Math;

import com.yahoo.labs.samoa.instances.Instance;

import java.io.File;
import java.io.IOException;
import moa.classifiers.meta.AdaptiveRandomForestRegressor;
import moa.evaluation.BasicRegressionPerformanceEvaluator;

public class Prequential extends App {
	public static void main(String[] args) throws IOException {
		for(int counter=0;counter<10;counter++)
		{
			
			dataFrame = DataFrame.create()
	                .addStringColumn("True value")
	                .addStringColumn("K / probability")
	                .addStringColumn("NormalPrediction")
	                .addStringColumn("OurPrediction")
	                .addStringColumn("target_mean")
	                .addStringColumn("target_variance");
			dataFrame2 = DataFrame.create()
	                .addStringColumn("True value")
	                .addStringColumn("K / probability")
	                .addStringColumn("NormalPrediction")
	                .addStringColumn("OurPrediction")
	                .addStringColumn("target_mean")
	                .addStringColumn("target_variance");
			
			datasetnumber=Integer.toString(counter);
			variable_initialization();
			show(storefilename);
			show("Initialization phasse");
			BasicRegressionPerformanceEvaluator eval= getBasicRegressionPerformanceEvaluator();
			ArffFileStream stream=getStream();
			Instance insttmp=stream.nextInstance().getData();
			
			Perceptron Our_Perceptron=getPerceptron(stream);
			FIMTDD Our_fimtdd=getFIMTDD(stream);
			TargetMean Our_TargetMean=getTargetmean(stream);
			AMRulesRegressor Our_AMR=getAMR(stream);
			
			AdaptiveRandomForestRegressor Our_RFR=getRFR(stream);
			SGD Our_SGD=getSGD(stream);
//			Our_SGD.setLearningRate(0.0000001);
//			show(Our_SGD.getLearningRate());
//			System.exit(0);
			
			_initialize(Our_Perceptron,Our_fimtdd,Our_TargetMean,Our_AMR,Our_RFR,Our_SGD,learnerno);
	        show("Start warming phasse");
	        warmingPhase(Our_Perceptron ,Our_fimtdd,Our_TargetMean,Our_AMR,Our_RFR,Our_SGD,stream,warmingphasesamples,learnerno);
	        
	        
	        
	        switch (learnerno) {
	        case 1:
	        	Our_Perceptron=RsponseWarmingphase.getLearner();
	        	break;
	        case 2:
	        	Our_fimtdd=RsponseWarmingphase.getlearnerfidtmm();
	           break;
	        case 3:
	        	Our_TargetMean=RsponseWarmingphase.getlearnerTargetMean();
	           break;
	        case 4:
	        	Our_AMR=RsponseWarmingphase.getlearnerAMR();
	           break;
	        case 5:
	        	Our_RFR=RsponseWarmingphase.getlearnerRFR();
	           break;
	        case 6:
	        	Our_SGD=RsponseWarmingphase.getLearnerSGD();
	           break;
	        }
	        stream=RsponseWarmingphase.getStream();
	        ////////////////////////////////////////////////////////////////////////////
	        Perceptron NormalPerceptron=(Perceptron) Our_Perceptron.copy();
	        FIMTDD Normalfimtdd=(FIMTDD) Our_fimtdd.copy();
	        TargetMean NormalTargetMean=(TargetMean) Our_TargetMean.copy();
	        AMRulesRegressor NormalAMR=(AMRulesRegressor) Our_AMR.copy();
	        AdaptiveRandomForestRegressor NormalRFR=(AdaptiveRandomForestRegressor) Our_RFR.copy();
	        SGD NormalSGD=(SGD) Our_SGD.copy();
	        
	        Perceptron Our_Perceptron_2=(Perceptron) Our_Perceptron.copy();
	        FIMTDD Our_fimtdd_2=(FIMTDD) Our_fimtdd.copy();
			TargetMean Our_TargetMean_2=(TargetMean) Our_TargetMean.copy();
			AMRulesRegressor Our_AMR_2=(AMRulesRegressor) Our_AMR.copy();
			AdaptiveRandomForestRegressor Our_RFR_2=(AdaptiveRandomForestRegressor) Our_RFR.copy();
			SGD Our_SGD_2=(SGD) Our_SGD.copy();
			////////////////////////////////////////////////////////////////////////////
	        noes=warmingphasesamples;
//	        show("+++++++++++++");
//	        System.out.println(stream.hasMoreInstances());
//	        show(numInstances);
//	        show(numberofseensamples);
	        show("Start Training phasse");
	        while (stream.hasMoreInstances()) {
	        	trainInst = stream.nextInstance().getData();
	        	
	        	updateTargetmeanandVariance(trainInst);
	        	switch (learnerno) {
	            case 1:
	            	OurPrediction=Our_Perceptron.getVotesForInstance(trainInst);
	            	OurPrediction2=Our_Perceptron_2.getVotesForInstance(trainInst);
		        	NormalPrediction=NormalPerceptron.getVotesForInstance(trainInst);
		        	NormalPerceptron.trainOnInstanceImpl(trainInst);
	            	break;
	            case 2:
	            	OurPrediction=Our_fimtdd.getVotesForInstance(trainInst);
	            	OurPrediction2=Our_fimtdd_2.getVotesForInstance(trainInst);
		        	NormalPrediction=Normalfimtdd.getVotesForInstance(trainInst);
		        	Normalfimtdd.trainOnInstanceImpl(trainInst);
	               break;
	            case 3:
	            	OurPrediction=Our_TargetMean.getVotesForInstance(trainInst);
	            	OurPrediction2=Our_TargetMean_2.getVotesForInstance(trainInst);
		        	NormalPrediction=NormalTargetMean.getVotesForInstance(trainInst);
		        	NormalTargetMean.trainOnInstanceImpl(trainInst);
	               break;
	            case 4:
	            	OurPrediction=Our_AMR.getVotesForInstance(trainInst);
	            	OurPrediction2=Our_AMR_2.getVotesForInstance(trainInst);
		        	NormalPrediction=NormalAMR.getVotesForInstance(trainInst);
		        	NormalAMR.trainOnInstanceImpl(trainInst);
	               break;
	            case 5:
	            	OurPrediction=Our_RFR.getVotesForInstance(trainInst);
	            	OurPrediction2=Our_RFR_2.getVotesForInstance(trainInst);
		        	NormalPrediction=NormalRFR.getVotesForInstance(trainInst);
		        	NormalRFR.trainOnInstanceImpl(trainInst);
	               break;
	            case 6:
	            	OurPrediction=Our_SGD.getVotesForInstance(trainInst);
	            	OurPrediction2=Our_SGD_2.getVotesForInstance(trainInst);
		        	NormalPrediction=NormalSGD.getVotesForInstance(trainInst);
		        	NormalSGD.trainOnInstanceImpl(trainInst);
	               break;
	            }
	        	eval=UpdateMeanErrAndSeris(eval);
	        	boolean israre;
	        	if(side=="both") israre=true;
	        	else if(side=="left") israre=trainInst.classValue()<israre_value;
	        	else if(side=="right") israre=trainInst.classValue()>israre_value;
	        	else System.exit(1);
//	        	
	        	prob=1-getChebyshev_prob_target(trainInst);
//	        	 
//	        	if(prob>=threshold && israre)
	        	if(prob>=Math.random() && israre)
	        	{
	        		for(int ii=0;ii<1;ii++)
		            {
		        		switch (learnerno) {
		                case 1:
		                	Our_Perceptron.trainOnInstanceImpl(trainInst);
		                	break;
		                case 2:
		                	Our_fimtdd.trainOnInstanceImpl(trainInst);
		                    break;
		                case 3:
		                	Our_TargetMean.trainOnInstanceImpl(trainInst);
		                    break;
		                case 4:
		                	Our_AMR.trainOnInstanceImpl(trainInst);
		                    break;
		                case 5:
		                	Our_RFR.trainOnInstanceImpl(trainInst);
		                    break;
		                case 6:
		                	Our_SGD.trainOnInstanceImpl(trainInst);
		                    break;
		                }
		        		noes++;
		            }
	        	}
	        	int k;
	            if(israre) {k=getChebyshev_prob_target_k(trainInst);}else {k=1;}
	            for(int ii=0;ii<k;ii++)
	            {
	            	switch (learnerno) {
		              case 1:
			              	Our_Perceptron_2.trainOnInstanceImpl(trainInst);
			              	break;
		              case 2:
							Our_fimtdd_2.trainOnInstanceImpl(trainInst);
							break;
		              case 3:
			              	Our_TargetMean_2.trainOnInstanceImpl(trainInst);
			                break;
		              case 4:
		                	Our_AMR_2.trainOnInstanceImpl(trainInst);
		                    break;
		              case 5:
		            	  Our_RFR_2.trainOnInstanceImpl(trainInst);
		                    break;
		              case 6:
		            	  Our_SGD_2.trainOnInstanceImpl(trainInst);
		                    break;
	              }
//	            	noes++;
	        	}
//	            saveinfile(trainInst.classValue()+","+k+","+NormalPrediction[0]+","+OurPrediction[0]+","+target_mean+","+target_variance);
	            dataFrame.append(String.valueOf(trainInst.classValue()),String.valueOf(prob),String.valueOf(NormalPrediction[0]),String.valueOf(OurPrediction[0]),String.valueOf(target_mean),String.valueOf(target_variance));
	            dataFrame2.append(String.valueOf(trainInst.classValue()),String.valueOf(k),String.valueOf(NormalPrediction[0]),String.valueOf(OurPrediction2[0]),String.valueOf(target_mean),String.valueOf(target_variance));
	        }
	        
	        show("Number of effected samples: "+noes);
	//        ShowGraph();
	//        Our_fimtdd.getModelDescription(out, 4);
//	          StringBuilder out = new StringBuilder();
	//        show(out.toString());
	//        show("***************************************");
	//        Normalfimtdd.getModelDescription(out, 4);
	//        show(out.toString());
			dataFrame.writeCSV(new File(workspace+"1"+storefilename+f_tail_name+".csv"), ',',true);
			dataFrame.clear();
			dataFrame2.writeCSV(new File(workspace+"2"+storefilename+f_tail_name+".csv"), ',',true);
			dataFrame2.clear();
			show("***************************************");
		}
	}

}
