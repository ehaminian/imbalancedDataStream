package ehaminian.gmail.com.stream1;
import moa.classifiers.trees.FIMTDD;
import moa.classifiers.rules.functions.TargetMean;
import moa.classifiers.functions.SGD;
import moa.classifiers.meta.AdaptiveRandomForestRegressor;
import moa.classifiers.rules.AMRulesRegressor;
import moa.classifiers.rules.functions.Perceptron;
import moa.streams.ArffFileStream;

public class RsponseWarmingphase {
	
	private static Perceptron learner;
	private static FIMTDD learnerfidtmm;
	private static TargetMean learnerTargetMean;
	private static AMRulesRegressor learnerAMR;
	private static AdaptiveRandomForestRegressor learnerRFR;
	private static SGD learnerSGD;
	
	
	private static ArffFileStream stream;

	public static Perceptron getLearner() {
		return learner;
	}
	public static FIMTDD getlearnerfidtmm() {
		return learnerfidtmm;
	}
	public static TargetMean getlearnerTargetMean() {
		return learnerTargetMean;
	}
	public static AMRulesRegressor getlearnerAMR() {
		return learnerAMR;
	}
	public static AdaptiveRandomForestRegressor getlearnerRFR() {
		return learnerRFR;
	}
	public static SGD getLearnerSGD() {
		return learnerSGD;
	}
	public static void setLearner(SGD learnerSGD) {
		RsponseWarmingphase.learnerSGD = learnerSGD;
	}
	public static void setLearner(Perceptron learner) {
		RsponseWarmingphase.learner = learner;
	}
	public static void setLearner(FIMTDD learner) {
		RsponseWarmingphase.learnerfidtmm = learner;
	}
	public static void setLearner(TargetMean learner) {
		RsponseWarmingphase.learnerTargetMean = learner;
	}
	public static void setLearner(AMRulesRegressor learner)  {
		RsponseWarmingphase.learnerAMR = learner;
	}
	public static void setLearner(AdaptiveRandomForestRegressor learner) {
		RsponseWarmingphase.learnerRFR = learner;
	}
	public static ArffFileStream getStream() {
		return stream;
	}

	public static void setStream(ArffFileStream stream) {
		RsponseWarmingphase.stream = stream;
	}
	

}
