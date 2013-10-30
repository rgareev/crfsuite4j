/**
 * 
 */
package ru.kfu.itis.issst.crfsuite4j;

import static ru.kfu.itis.issst.crfsuite4j.PUtil.toAttribute2dArray;

import java.util.Arrays;
import java.util.List;

/**
 * @author Rinat Gareev (Kazan Federal University)
 * 
 */
public class CrfSuiteTrainer {

	// models
	public static final String MODEL_CRF1D = "crf1d";
	// algorithms
	public static final String ALG_LBFGS = "lbfgs";
	// model parameters
	public static final String MODEL_PARAM_FEATURE_MINFREQ = "feature.minfreq";
	public static final String MODEL_PARAM_POSSIBLE_STATES = "feature.possible_states";
	public static final String MODEL_PARAM_POSSIBLE_TRANSITIONS = "feature.possible_transitions";
	// algorithm parameters
	public static final String ALG_PARAM_MAX_ITERATIONS = "max_iterations";
	public static final String ALG_PARAM_DELTA = "delta";

	static {
		NativeLibrary.load();
	}

	private long nativeHandle;

	public CrfSuiteTrainer() {
		init();
	}

	public native void dispose();

	public void append(List<List<Attribute>> itemSeq, List<String> labelSeq, int group) {
		Attribute[][] itemSeqArr = toAttribute2dArray(itemSeq);
		String[] labelSeqArr = labelSeq.toArray(new String[labelSeq.size()]);
		append(itemSeqArr, labelSeqArr, group);
	}

	public native void append(Attribute[][] itemSeq, String[] labelSeq, int group);

	public void select(String trAlgorithm, String modelType) {
		if (!doSelect(trAlgorithm, modelType)) {
			throw new IllegalStateException(String.format(
					"Can't select algorithm '%s' and model type '%s'",
					trAlgorithm, modelType));
		}
	}

	public void trainWithoutHoldout(String modelFile) {
		train(modelFile, -1);
	}

	public void train(String modelFile, int holdout) {
		int status = doTrain(modelFile, holdout);
		if (status != 0) {
			throw new IllegalStateException(String.format(
					"crfsuite Trainer returned status code '%s'", status));
		}
	}

	public List<String> getParams() {
		return Arrays.asList(params());
	}

	public native String[] params();

	public native void set(String name, String val);

	public native String get(String name);

	public native String help(String name);

	private native void init();

	private native boolean doSelect(String alg, String gmType);

	private native int doTrain(String modelFile, int holdout);

	private void message(String msg) {
		// TODO
		msg = "CrfSuiteTrainer: " + msg;
		if (msg.endsWith("\n")) {
			System.out.print(msg);
		} else {
			System.out.println(msg);
		}
	}
}