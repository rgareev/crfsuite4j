/**
 * 
 */
package ru.kfu.itis.issst.crfsuite4j;

import java.util.Arrays;
import java.util.List;

/**
 * @author Rinat Gareev (Kazan Federal University)
 * 
 */
public class CrfSuiteTrainer {

	static {
		System.loadLibrary("crfsuite-jni");
	}

	private long nativeHandle;

	public CrfSuiteTrainer() {
		init();
	}

	public native void dispose();

	public void append(List<List<Attribute>> itemSeq, List<String> labelSeq, int group) {
		Attribute[][] itemSeqArr = new Attribute[itemSeq.size()][];
		int i = 0;
		for (List<Attribute> item : itemSeq) {
			Attribute[] itemArr = new Attribute[item.size()];
			int j = 0;
			for (Attribute attr : item) {
				itemArr[j] = attr;
				j++;
			}
			itemSeqArr[i] = itemArr;
			i++;
		}
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

	public native int train(String modelFile, int holdout);

	public List<String> getParams() {
		return Arrays.asList(params());
	}

	private native String[] params();

	private native void set(String name, String val);

	private native String get(String name);

	private native String help(String name);

	private native void init();

	private native boolean doSelect(String alg, String gmType);

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