package ehaminian.gmail.com.stream1;
import moa.classifiers.rules.functions.TargetMean;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.jfree.data.xy.XYSeries;
import org.jfree.ui.RefineryUtilities;

import com.github.javacliparser.FlagOption;
import com.github.javacliparser.FloatOption;
import com.yahoo.labs.samoa.instances.Instance;

import Jama.Matrix;
import de.unknownreality.dataframe.DataFrame;
import moa.classifiers.rules.AMRulesRegressor;
import moa.classifiers.rules.functions.Perceptron;
import moa.core.Example;
import moa.core.InstanceExample;
import moa.evaluation.BasicRegressionPerformanceEvaluator;
import moa.streams.ArffFileStream;
import moa.classifiers.trees.FIMTDD;
import moa.classifiers.functions.SGD;
import moa.classifiers.meta.AdaptiveRandomForestRegressor;
public class App 
{
	protected static final int learnerno=1;
	protected static final String side="both"; //left //both //right
	protected static int choosen=12;
	protected static double israre_value=0.2;
	protected static double threshold=0.8;
	
	protected static String datasetnumber;
	protected static int warmingphasesamples=5000; 
	protected static int[] classindexdatasets= {30,41,13,9,64,13,19,11,9,9,33,39}; //start from 1
	protected static String[] datasetnames= {"2dplanes","ailerons","air","bank8","bike","cpusm","elevator","fried","house","kin8nm","puma32H","weatherHistory"};
//	protected static int[] numInstancesindex= {81536,44924,56991,81920,52137,49152,85430,94376,69831,44800,49530,40960,144997};
	protected static String datasetname=datasetnames[choosen]; 
//	protected static int numInstances=numInstancesindex[choosen]; 
	protected static int classindex=classindexdatasets[choosen];
	protected static String root="C:\\Users\\Ehsan\\Desktop\\conference\\Data\\";
	protected static String workspace=root+datasetname+"\\";
	protected static String f_tail_name="";
	protected static String path=workspace+datasetname+"_"+datasetnumber+".arff";
	protected static String storefilename=datasetname+"_"+datasetnumber;
	final protected static boolean debug=false;
	protected static double target_mean;
	protected static double target_variance;
    protected static double Starget;
	protected static Matrix _mean;
	protected static Matrix _max;
	protected static Matrix _min;
	protected static int numberofseensamples;
	protected static Matrix covmatrix;
    protected static List<Integer> _indexs = new ArrayList<Integer>();
    protected static Matrix S;
    protected static int size;
    protected static int noes=0;
    protected static int base=numberofseensamples;
    protected static final XYSeries series = new XYSeries("MeanError");
    protected static Instance trainInst=null;
    protected static double[] OurPrediction = new double[1];
    protected static double[] NormalPrediction = new double[1];
    protected static double[] OurPrediction2 = new double[1];
    protected static double[] NormalPrediction2 = new double[1];
    protected static double prob;
    protected static DataFrame dataFrame ;
    protected static DataFrame dataFrame2 ;
    
    
    
    protected static void variable_initialization()
    {

    	datasetname=datasetnames[choosen]; 
//    	numInstances=numInstancesindex[choosen]; 
    	classindex=classindexdatasets[choosen];
    	root="C:\\Users\\Ehsan\\Desktop\\conference\\Data\\";
    	workspace=root+datasetname+"\\";
    	f_tail_name="";
    	path=workspace+datasetname+"_"+datasetnumber+".arff";
    	storefilename=datasetname+"_"+datasetnumber;
        noes=0;
        base=numberofseensamples;
        trainInst=null;
    }


    
    protected static String getdatasetnumber()
    {
		return datasetnumber;
    }
    protected static FIMTDD getFIMTDD(ArffFileStream stream)
    {
    	FIMTDD Learner=new FIMTDD();
    	Learner.setModelContext(stream.getHeader());
    	Learner.prepareForUse();
		return Learner;
    }
    
    protected static void ShowGraph()
    {
    	final String title="Mean Squre Error";
        final drawchart meansquaregraph = new drawchart(title,series);
        meansquaregraph.pack();
        RefineryUtilities.centerFrameOnScreen(meansquaregraph);
        meansquaregraph.setVisible(true);
    }
    
    protected static BasicRegressionPerformanceEvaluator UpdateMeanErrAndSeris(BasicRegressionPerformanceEvaluator eval)
    {
    	Example<Instance> example=new  InstanceExample(trainInst);
        eval.addResult(example, NormalPrediction);
        series.add(numberofseensamples-base, eval.getMeanError());
		return eval;
    }
    
    protected static void warmingPhase(Perceptron Our_Perceptron ,FIMTDD Our_fimtdd,TargetMean Our_TargetMean,AMRulesRegressor our_AMR,AdaptiveRandomForestRegressor Our_RFR,SGD Our_SGD,ArffFileStream stream,int sizes,int learnerno)
    {
    	switch (learnerno) {
        case 1:
        	warmingPhase(Our_Perceptron,stream,sizes);
        	Our_Perceptron=RsponseWarmingphase.getLearner();
        	break;
        case 2:
        	warmingPhase(Our_fimtdd,stream,sizes);
        	Our_fimtdd=RsponseWarmingphase.getlearnerfidtmm();
           break;
        case 3:
        	warmingPhase(Our_TargetMean,stream,sizes);
        	Our_TargetMean=RsponseWarmingphase.getlearnerTargetMean();
           break;
        case 4:
        	warmingPhase(our_AMR,stream,sizes);
        	our_AMR=RsponseWarmingphase.getlearnerAMR();
           break;
        case 5:
        	warmingPhase(Our_RFR,stream,sizes);
        	Our_RFR=RsponseWarmingphase.getlearnerRFR();
           break;
        case 6:
        	warmingPhase(Our_SGD,stream,sizes);
        	Our_RFR=RsponseWarmingphase.getlearnerRFR();
           break;
        }
    	
    }
    protected static void warmingPhase(SGD Learner ,ArffFileStream stream,int num)
    {
    	stream.restart();
    	Instance trainInst=null;
    	trainInst = stream.nextInstance().getData();
    	updateTargetmeanandVariance(trainInst);
        for(int i=0;i<num;i++)
        {
        	trainInst = stream.nextInstance().getData();
        	updateTargetmeanandVariance(trainInst);	
        	Learner.trainOnInstanceImpl(trainInst);
        }
        
        RsponseWarmingphase.setLearner(Learner);
        RsponseWarmingphase.setStream(stream);
    }
    protected static void warmingPhase(AdaptiveRandomForestRegressor Learner ,ArffFileStream stream,int num)
    {
    	stream.restart();
    	Instance trainInst=null;
    	trainInst = stream.nextInstance().getData();
    	updateTargetmeanandVariance(trainInst);
        for(int i=0;i<num;i++)
        {
        	trainInst = stream.nextInstance().getData();
        	updateTargetmeanandVariance(trainInst);	
        	Learner.trainOnInstanceImpl(trainInst);
        }
        
        RsponseWarmingphase.setLearner(Learner);
        RsponseWarmingphase.setStream(stream);
    }
    protected static void warmingPhase(Perceptron Learner ,ArffFileStream stream,int num)
    {
    	stream.restart();
    	Instance trainInst=null;
    	trainInst = stream.nextInstance().getData();
    	updateTargetmeanandVariance(trainInst);	
    	Learner.trainOnInstanceImpl(trainInst);
    	double[] w=Learner.getWeights();
    	double[] t1=new double[w.length];
    	for(int i=0;i<w.length;i++)
    		t1[i]=Math.random();
    	Learner.setWeights(t1);
        for(int i=0;i<num;i++)
        {
        	trainInst = stream.nextInstance().getData();
        	if((Double.isNaN(trainInst.classValue())))
        	{
        		continue;
        	}
        	updateTargetmeanandVariance(trainInst);	
        	Learner.trainOnInstanceImpl(trainInst);
        }
        
        RsponseWarmingphase.setLearner(Learner);
        RsponseWarmingphase.setStream(stream);
    }
    protected static void warmingPhase(AMRulesRegressor Learner ,ArffFileStream stream,int num)
    {
    	stream.restart();
    	Instance trainInst=null;
    	trainInst = stream.nextInstance().getData();
    	updateTargetmeanandVariance(trainInst);
        for(int i=0;i<num;i++)
        {
        	trainInst = stream.nextInstance().getData();
        	updateTargetmeanandVariance(trainInst);	
        	Learner.trainOnInstanceImpl(trainInst);
        }
        
        RsponseWarmingphase.setLearner(Learner);
        RsponseWarmingphase.setStream(stream);
    }
    protected static void warmingPhase(FIMTDD Learner ,ArffFileStream stream,int num)
    {
    	stream.restart();
    	Instance trainInst=null;
    	trainInst = stream.nextInstance().getData();
    	updateTargetmeanandVariance(trainInst);
        for(int i=0;i<num;i++)
        {
        	trainInst = stream.nextInstance().getData();
        	updateTargetmeanandVariance(trainInst);	
        	Learner.trainOnInstanceImpl(trainInst);
        }
        
        RsponseWarmingphase.setLearner(Learner);
        RsponseWarmingphase.setStream(stream);
    }
    protected static void warmingPhase(TargetMean Learner ,ArffFileStream stream,int num)
    {
    	stream.restart();
    	Instance trainInst=null;
    	trainInst = stream.nextInstance().getData();
    	updateTargetmeanandVariance(trainInst);
        for(int i=0;i<num;i++)
        {
        	trainInst = stream.nextInstance().getData();
        	updateTargetmeanandVariance(trainInst);	
        	Learner.trainOnInstanceImpl(trainInst);
        }
        
        RsponseWarmingphase.setLearner(Learner);
        RsponseWarmingphase.setStream(stream);
    }
    protected static Perceptron init()
    {
    	Perceptron p=new Perceptron();
    	ArffFileStream stream=getStream();
    	p.setModelContext(stream.getHeader());
    	p.prepareForUse();
    	Instance inst=stream.nextInstance().getData();
    	p.trainOnInstanceImpl(inst);
    	LinkedList<Integer> numericIndices= new LinkedList<Integer>();
		for (int i = 0; i < inst.numAttributes(); i++)
			if(inst.attribute(i).isNumeric() && i!=inst.classIndex())
				numericIndices.add(i);
		int[] numericAttributesIndex=new int[numericIndices.size()];
		int j=0;
		for(Integer index : numericIndices)
			numericAttributesIndex[j++]=index;
		double[] weightAttribute = new double[numericAttributesIndex.length+1];
		p.classifierRandom = new Random();
		for (int i = 0; i < numericAttributesIndex.length+1; i++) {
			//if (inst.attribute(i).isNumeric())
			weightAttribute[i] = 2 * p.classifierRandom.nextDouble() - 1;
		}
		p.constantLearningRatioDecayOption=new FlagOption(
				"learningRatio_Decay_set_constant", 'd',
				"Learning Ratio Decay in Perceptron set to be constant. (The next parameter).");
		p.learningRatioOption=new FloatOption(
				"learningRatio", 'l', 
				"Constante Learning Ratio to use for training the Perceptrons in the leaves.", Math.abs(0.04*Math.random()));
		p.learningRateDecayOption = new FloatOption(
				"learningRateDecay", 'm', 
				" Learning Rate decay to use for training the Perceptron.", 0.001);
		p.fadingFactorOption=new FloatOption(
				"fadingFactor", 'e', 
				"Fading factor for the Perceptron accumulated error", 0.99, 0, 1);
		p.setWeights(weightAttribute);
    	return p;
    }
    protected static SGD getSGD(ArffFileStream stream)
    {
    	SGD Learner=new SGD();
    	Learner.setModelContext(stream.getHeader());
    	Learner.prepareForUse();
        return Learner;
    }
    
    protected static AdaptiveRandomForestRegressor getRFR(ArffFileStream stream)
    {
    	AdaptiveRandomForestRegressor Learner=new AdaptiveRandomForestRegressor();
    	Learner.setModelContext(stream.getHeader());
    	Learner.prepareForUse();
        return Learner;
    }
    
    protected static AMRulesRegressor getAMR(ArffFileStream stream)
    {
    	AMRulesRegressor Learner=new AMRulesRegressor();
    	Learner.setModelContext(stream.getHeader());
    	Learner.prepareForUse();
        return Learner;
    }
    
    protected static Perceptron getPerceptron(ArffFileStream stream)
    {
    	Perceptron Learner=new Perceptron(init());
    	Learner.setModelContext(stream.getHeader());
    	Learner.prepareForUse();
        return Learner;
    }
    
    protected static TargetMean getTargetmean(ArffFileStream stream)
    {
    	TargetMean Learner=new TargetMean();
    	Learner.setModelContext(stream.getHeader());
    	Learner.prepareForUse();
        return Learner;
    }
    protected static ArffFileStream getStream()
    {
    	ArffFileStream stream = new ArffFileStream(path,classindex);
		stream.prepareForUse();
		return stream;
    }
    protected static void deletefile() 
    		  throws IOException {
    	try
    	{
    	File file = new File(workspace+storefilename+".csv");
    	file.delete();
    	}catch (Exception e) {
			System.out.println("File does not exist or there is some other problems");
		}
    		}
    
    protected static void saveinfile(String text) 
  		  throws IOException {
  	try(FileWriter fw = new FileWriter(workspace+storefilename+f_tail_name+".csv", true);
  		    BufferedWriter bw = new BufferedWriter(fw);
  		    PrintWriter out = new PrintWriter(bw))
  		{
  		    out.println(text);
  		} catch (IOException e) {
  		    e.printStackTrace();
  		}
  		}
    protected static BasicRegressionPerformanceEvaluator getBasicRegressionPerformanceEvaluator()
    {
    	BasicRegressionPerformanceEvaluator eval=new BasicRegressionPerformanceEvaluator();
		eval.reset();
		return eval;
    }
    
    protected static void _initialize(Perceptron Our_Perceptron,FIMTDD Our_fimtdd,TargetMean Our_TargetMean,AMRulesRegressor Our_AMR,AdaptiveRandomForestRegressor Our_RFR,SGD Our_SGD,int learnerno)
    {
    	switch (learnerno) {
        case 1:
        	_initialize(Our_Perceptron);
        	break;
        case 2:
        	_initialize(Our_fimtdd);
           break;
        case 3:
        	_initialize(Our_TargetMean);
           break;
        case 4:
        	_initialize(Our_AMR);
           break;
        case 5:
        	_initialize(Our_RFR);
           break;
        case 6:
        	_initialize(Our_SGD);
           break;
        }	
    }
    protected static void _initialize(SGD Learner)
	{
		size=Learner.getModelContext().numInputAttributes();
		_mean=new Matrix(1,size);
        _min=new Matrix(1,size);
        _max=new Matrix(1,size);
         covmatrix=new Matrix(size,size);
         S=new Matrix(size,size);
         numberofseensamples=0;
         target_mean=0;
         target_variance=0;
         Starget=0;
	}
    protected static void _initialize(AdaptiveRandomForestRegressor Learner)
	{
		size=Learner.getModelContext().numInputAttributes();
		_mean=new Matrix(1,size);
        _min=new Matrix(1,size);
        _max=new Matrix(1,size);
         covmatrix=new Matrix(size,size);
         S=new Matrix(size,size);
         numberofseensamples=0;
         target_mean=0;
         target_variance=0;
         Starget=0;
	}
    protected static void _initialize(AMRulesRegressor Learner)
	{
		size=Learner.getModelContext().numInputAttributes();
		_mean=new Matrix(1,size);
        _min=new Matrix(1,size);
        _max=new Matrix(1,size);
         covmatrix=new Matrix(size,size);
         S=new Matrix(size,size);
         numberofseensamples=0;
         target_mean=0;
         target_variance=0;
         Starget=0;
	}
	protected static void _initialize(Perceptron Learner)
	{
		size=Learner.getModelContext().numInputAttributes();
		_mean=new Matrix(1,size);
        _min=new Matrix(1,size);
        _max=new Matrix(1,size);
         covmatrix=new Matrix(size,size);
         S=new Matrix(size,size);
         numberofseensamples=0;
         target_mean=0;
         target_variance=0;
         Starget=0;
	}
	protected static void _initialize(FIMTDD Learner)
	{
		size=Learner.getModelContext().numInputAttributes();
		_mean=new Matrix(1,size);
        _min=new Matrix(1,size);
        _max=new Matrix(1,size);
         covmatrix=new Matrix(size,size);
         S=new Matrix(size,size);
         numberofseensamples=0;
         target_mean=0;
         target_variance=0;
         Starget=0;
	}
	protected static void _initialize(TargetMean Learner)
	{
		size=Learner.getModelContext().numInputAttributes();
		_mean=new Matrix(1,size);
        _min=new Matrix(1,size);
        _max=new Matrix(1,size);
         covmatrix=new Matrix(size,size);
         S=new Matrix(size,size);
         numberofseensamples=0;
         target_mean=0;
         target_variance=0;
         Starget=0;
	}
    protected static void updateTargetmeanandVariance(Instance _ins)
    {
    	int n_1=numberofseensamples++;
    	int n=numberofseensamples;
    	double MUn_1=target_mean;
    	double Yn=_ins.classValue();
    	target_mean=(target_mean*n_1+Yn)/n;
    	double MUn=target_mean;
    	Starget=Starget+(Yn-MUn_1)*(Yn-MUn);
    	target_variance=Starget/n;
    	if(debug) 	show("Mean is: "+target_mean+" and Variance is :"+target_variance);
    	
    }
    protected static double getChebyshev_prob_target(Instance _ins)
	{
    	double sigma2=target_variance;
    	double sigma=Math.sqrt(target_variance);
    	double Yn=_ins.classValue();
    	double t=Math.abs(Yn-target_mean)/sigma ;
    	if(debug) show("Y is: "+ Yn+" and mean is : "+target_mean+" and sigma is: "+sigma+" and T is :"+t);;
    	if (t<1) return 1;
    	return 1/(t*t);
	}
    protected static int getChebyshev_prob_target_k(Instance _ins)
	{
    	double sigma2=target_variance;
    	double sigma=Math.sqrt(target_variance);
    	double Yn=_ins.classValue();
    	double t=Math.abs(Yn-target_mean)/sigma ;
    	if(debug) show("Y is: "+ Yn+" and mean is : "+target_mean+" and sigma is: "+sigma+" and T is :"+t);;
    	if (t<1) return 1;
    	return (int) Math.ceil(t);
	}
    

	protected static double getChebyshev_prob(Instance _ins)
	{
		//show("+++++++++++++++++++++++++getChebyshev_prob++++++++++++++++++++++++++++++");
		double t=0;
		Matrix CV=getabsCVmatrix();
		if(CV.det()!=0)
		{
			Matrix CVINV=CV.inverse();
			Matrix INS=new Matrix(Martixops.makeMatrix(getabsins(_ins)));
			Matrix MU=new Matrix(Martixops.makeMatrix(getabsins(_mean)));
			//show("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			Matrix temp1=INS.minus(MU);
			Matrix temp2=temp1.transpose();
			Matrix temp3=temp1.times(CVINV);
			Matrix temp4=temp3.times(temp2);
			t=Math.sqrt(temp4.get(0, 0));
		}
		else
		{
			show("The determinant is zeros for the following covariance Matrix");
			CV.print(CV.getRowDimension()-1, CV.getColumnDimension()-1);
			System.exit(1);
		}
		//show(CV.getColumnDimension()/(t*t));
		return (CV.getColumnDimension()/(t*t));
	}
	
	protected static Matrix getabsCVmatrix()
	{
		_indexs.clear();
		if(covmatrix.det()==0)
		{
			for(int i=0;i<covmatrix.getColumnDimension();i++)
			{
				int[] c= {i};
				double[] row=covmatrix.getMatrix(0,covmatrix.getRowDimension()-1, c).getRowPackedCopy();
				if(max(row)!=min(row))
				{_indexs.add(i);}
			}
			int[] c=new int[_indexs.size()];
			for(int i=0;i<_indexs.size();i++)
				c[i]=_indexs.get(i);

			Matrix absCV=covmatrix.getMatrix(c, c);
			if(absCV.rank()<_indexs.size())
			{
				boolean flag=true;
				for(int i=0;i<_indexs.size();i++)
				{
					flag=true;
					for(int j=i+1;j<_indexs.size();j++)
					{
						
						int[] r1= {i,j};
						Matrix t1=absCV.getMatrix(r1,0,absCV.getColumnDimension()-1);
						if(t1.rank()<2)
						{
							//show(i+"-"+j+"\t"+t1.rank());
							flag=false;
							break;
						}
					}
					if(!flag)
						_indexs.remove(i);
				}
				c=new int[_indexs.size()];
				for(int i=0;i<_indexs.size();i++)
					c[i]=_indexs.get(i);
				absCV=covmatrix.getMatrix(c, c);
			}
			return absCV;
		}
		return covmatrix;
	}
	protected static double[] getabsins(Matrix instance_in_Matrix_form)
	{
    	if(instance_in_Matrix_form.getRowDimension()>1)
    	{
    		show("Invalild use of getabsins. The row dimension should be one while is is "+instance_in_Matrix_form.getRowDimension());
    		System.exit(1);
    	}
		double[] meanIns=new double[_indexs.size()];
		double[] doublearr=instance_in_Matrix_form.getColumnPackedCopy();
		Iterator<Integer> myListIterator1 = _indexs.iterator();
		int i=0;
		while(myListIterator1.hasNext())
		{
			int j=myListIterator1.next();
			meanIns[i++]=doublearr[j];
		}
		return meanIns;
	}
    protected static double[] getabsins(double[] doublearr)
	{
		double[] meanIns=new double[_indexs.size()];
		Iterator<Integer> myListIterator1 = _indexs.iterator();
		int i=0;
		while(myListIterator1.hasNext())
		{
			int j=myListIterator1.next();
			meanIns[i++]=doublearr[j];
		}
		return meanIns;
	}
    
    protected static double max(double[] a)
    {
    	double max=a[0];
    	for(int i=1;i<a.length;i++)
    		max=Math.max(max, a[i]);
    	return max;
    }
    protected static double min(double[] a)
    {
    	double min=a[0];
    	for(int i=1;i<a.length;i++)
    		min=Math.min(min, a[i]);
    	return min;
    }
    protected static double[] getabsins(Instance ins)
	{
		double[] meanIns=new double[_indexs.size()];
		Iterator<Integer> myListIterator1 = _indexs.iterator();
		int i=0;
		while(myListIterator1.hasNext())
		{
			int j=myListIterator1.next();
			meanIns[i++]=ins.value(j);
		}
		return meanIns;
	}
    protected static Matrix smooth(Matrix M,double threshold)
	{
		for(int i=0;i<M.getRowDimension();i++)
			for(int j=0;j<M.getColumnDimension();j++)
			{
				M.set(i, j, (Math.abs(M.get(i, j))<threshold) ? 0:M.get(i, j));
			}
		return M;
	}

	protected static double[] ins2array(Instance ins)
	{

		int i=ins.numInputAttributes();
		double[] m=new double[i];
		for(int j=0;j<i;j++)
			m[j]=ins.value(j);
		return m;
	}
	
	protected static void _update_mean_covar(Instance ins)
	{
		++numberofseensamples;
		double n=1.0*numberofseensamples;
		double nINV=1.0/numberofseensamples;
		double n_1=1.0*(numberofseensamples-1.0);
		double n_1INV=1.0/n_1;
		
		///////////////////////////////////////////////////////////////////////////////////////////
		Matrix tempins=new Matrix(Martixops.makeMatrix(ins2array(ins)));
		_mean=(_mean.times(n_1).plus(tempins).times((nINV)));
		///////////////////////////////////////////////////////////////////////////////////////////
		Matrix insT=tempins.transpose();
		S.plusEquals(insT.times(tempins));
		Matrix meanT=_mean.transpose();
		Matrix T1=meanT.times(_mean).times(n);
		Matrix T2=S.minus(T1);
		covmatrix=smooth(T2.times(n_1INV),0.001);
	}
	

    protected static void  show_indexs()
    {
    	for(Integer value: _indexs)
    		System.out.print(value+"\t");
    	System.out.println();
    }
    protected static void  show(String s)
	{
		System.out.println(s);
	}
    protected static void  show(int s)
	{
		System.out.println(s);
	}
    protected static void  show(double s)
	{
		System.out.println(s);
	}
	protected static void  showMatrix(double[][] Matrix)
	{
		int n=Matrix.length;
		int m=Matrix[0].length;
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<m;j++)
			{
				System.out.print(Matrix[i][j]+"\t");
			}
			System.out.println();
		}
	}
	protected static void  showArray(double[] Arr)
	{
		for(int i=0;i<Arr.length;i++)
		{
				System.out.print(Arr[i]+"\t");
		}
		System.out.println();
	}
	protected static void  showArray(int[] Arr)
	{
		for(int i=0;i<Arr.length;i++)
		{
				System.out.print(Arr[i]+"\t");
		}
		System.out.println();
	}

	protected static FloatOption customelearningRatioOption = new FloatOption(
			"learningRatio", 'l', 
			"Constante Learning Ratio to use for training the Perceptrons in the leaves.", 0.025);
	
	
}
