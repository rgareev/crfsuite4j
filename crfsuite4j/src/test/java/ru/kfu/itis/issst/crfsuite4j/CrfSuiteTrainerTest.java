/**
 * 
 */
package ru.kfu.itis.issst.crfsuite4j;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
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
		trainer.select(CrfSuiteTrainer.ALG_LBFGS, CrfSuiteTrainer.MODEL_CRF1D);
		trainer.set(CrfSuiteTrainer.MODEL_PARAM_FEATURE_MINFREQ, "1");
		trainer.set(CrfSuiteTrainer.ALG_PARAM_MAX_ITERATIONS, "20");
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
		trainer.train("test.model", -1);
		trainer.dispose();
		// load tagger
		CrfSuiteTagger tagger = new CrfSuiteTagger(new File("test.model"));
		{
			List<List<Attribute>> itemSeq = new LinkedList<List<Attribute>>();
			itemSeq.add(Arrays.asList(
					new Attribute("11"), new Attribute("3")));
			itemSeq.add(Arrays.asList(
					new Attribute("101"), new Attribute("102")));
			itemSeq.add(Arrays.asList(
					new Attribute("2"), new Attribute("12")));
			List<String> labelSeq = tagger.tag(itemSeq);
			assertNotNull(labelSeq);
			assertTrue(labelSeq.size() == 3);
		}
		tagger.dispose();
	}
}