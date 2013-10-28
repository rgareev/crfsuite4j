/**
 * 
 */
package ru.kfu.itis.issst.crfsuite4j;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * @author Rinat Gareev (Kazan Federal University)
 * 
 */
public class CrfSuiteTrainerTest {

	@Test
	public void test() {
		CrfSuiteTrainer trainer = new CrfSuiteTrainer();
		{
			List<List<Attribute>> itemSeq = new LinkedList<List<Attribute>>();
			List<String> labelSeq = new LinkedList<String>();
			itemSeq.add(Arrays.asList(
					new Attribute("1"), new Attribute("2")));
			labelSeq.add("A");

			itemSeq.add(Arrays.asList(
					new Attribute("10"), new Attribute("11")));
			labelSeq.add("B");

			itemSeq.add(Arrays.asList(
					new Attribute("100"), new Attribute("101")));
			labelSeq.add("C");
			trainer.append(itemSeq, labelSeq, 0);
		}
		{
			List<List<Attribute>> itemSeq = new LinkedList<List<Attribute>>();
			List<String> labelSeq = new LinkedList<String>();

			itemSeq.add(Arrays.asList(
					new Attribute("11"), new Attribute("12")));
			labelSeq.add("B");

			itemSeq.add(Arrays.asList(
					new Attribute("2"), new Attribute("3")));
			labelSeq.add("A");

			itemSeq.add(Arrays.asList(
					new Attribute("101"), new Attribute("102")));
			labelSeq.add("C");
			trainer.append(itemSeq, labelSeq, 0);
		}
		trainer.select("lbfgs", "crf1d");
		trainer.train("test.model", -1);
		trainer.dispose();
	}
}