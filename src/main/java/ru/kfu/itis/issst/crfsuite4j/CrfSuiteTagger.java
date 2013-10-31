/**
 * 
 */
package ru.kfu.itis.issst.crfsuite4j;

import static ru.kfu.itis.issst.crfsuite4j.PUtil.toAttribute2dArray;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author Rinat Gareev (Kazan Federal University)
 * 
 */
public class CrfSuiteTagger {

	static {
		NativeLibrary.load();
	}

	private long nativeHandle;

	public CrfSuiteTagger(File modelFile) {
		if (!modelFile.isFile()) {
			throw new IllegalArgumentException(String.format(
					"Model file %s does not exist", modelFile));
		}
		init();
		try {
			open(modelFile.getPath());
		} catch (Exception e) {
			dispose();
			throw new RuntimeException(e);
		}
	}

	public native void dispose();

	public List<String> tag(List<List<Attribute>> itemSeq) {
		Attribute[][] itemSeqArr = toAttribute2dArray(itemSeq);
		String[] labelArr = doTag(itemSeqArr);
		return Arrays.asList(labelArr);
	}

	private native void init();

	private void open(String filePath) {
		if (!doOpen(filePath)) {
			throw new IllegalStateException(String.format(
					"Can't open model file %s", filePath));
		}
	}

	private native boolean doOpen(String filePath);

	private native String[] doTag(Attribute[][] itemSeq);
}