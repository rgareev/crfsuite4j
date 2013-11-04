/**
 * 
 */
package ru.kfu.itis.issst.crfsuite4j;

import java.util.List;

/**
 * @author Rinat Gareev (Kazan Federal University)
 * 
 */
public class PUtil {

	public static Attribute[][] toAttribute2dArray(List<List<Attribute>> itemSeq) {
		Attribute[][] itemSeqArr = new Attribute[itemSeq.size()][];
		int i = 0;
		for (List<Attribute> item : itemSeq) {
			Attribute[] itemArr = item.toArray(new Attribute[item.size()]);
			itemSeqArr[i] = itemArr;
			i++;
		}
		return itemSeqArr;
	}

	private PUtil() {
	};
}
