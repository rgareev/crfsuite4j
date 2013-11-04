/**
 * 
 */
package ru.kfu.itis.issst.crfsuite4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.StrTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rinat Gareev (Kazan Federal University)
 * 
 */
public class CrfSuiteTraining {

	private static final Logger log = LoggerFactory.getLogger(CrfSuiteTraining.class);

	private File modelFile;
	private String trainingAlgorithm;
	private Map<String, String> params = new HashMap<String, String>();

	public File getModelFile() {
		return modelFile;
	}

	/**
	 * @param modelFile
	 *            output file of a resulting model
	 */
	public void setModelFile(File modelFile) {
		this.modelFile = modelFile;
	}

	public String getTrainingAlgorithm() {
		return trainingAlgorithm;
	}

	/**
	 * @param trainingAlgorithm
	 *            name of a training algorithm
	 */
	public void setTrainingAlgorithm(String trainingAlgorithm) {
		this.trainingAlgorithm = trainingAlgorithm;
	}

	/**
	 * Set a parameter of the training algorithm or the graphical model.
	 * 
	 * @param pName
	 *            parameter name
	 * @param pValue
	 *            parameter value
	 */
	public void setParameterValue(String pName, String pValue) {
		params.put(pName, pValue);
	}

	public String getParameterValue(String pName) {
		return params.get(pName);
	}

	public void setParameters(Map<String, String> params) {
		this.params = params;
	}

	private void validateConfig() {
		if (modelFile == null) {
			throw new NullPointerException("modelFile");
		}
		if (trainingAlgorithm == null) {
			throw new NullPointerException("trainingAlgorithm");
		}
	}

	/**
	 * 
	 * @param trainingDataReader
	 *            a training data containing sequence instances formatted
	 *            according to crfsuite CLI front-end
	 * @throws IOException
	 */
	public void run(Reader trainingDataReader) throws IOException {
		validateConfig();
		BufferedReader inReader = trainingDataReader instanceof BufferedReader
				? (BufferedReader) trainingDataReader
				: new BufferedReader(trainingDataReader);
		// create trainer instance
		CrfSuiteTrainer trainer = new CrfSuiteTrainer();
		try {
			// configure
			trainer.select(trainingAlgorithm, CrfSuiteTrainer.MODEL_CRF1D);
			for (String paramName : params.keySet()) {
				String paramVal = params.get(paramName);
				trainer.set(paramName, paramVal);
			}
			// parse training data
			log.info("Parsing training data...");
			String line;
			int lineNumber = 0;
			List<List<Attribute>> items = newList();
			List<String> labels = newList();
			int instancesCounter = 0;
			while ((line = inReader.readLine()) != null) {
				lineNumber++;
				if (line.isEmpty()) {
					if (items.size() != labels.size()) {
						throw new IllegalStateException();
					}
					if (items.isEmpty()) {
						log.warn("Empty instance at line {}", lineNumber);
					} else {
						trainer.append(items, labels, 0);
						instancesCounter++;
					}
					items = newList();
					labels = newList();
				} else {
					StrTokenizer fSplitter = getFeatureSplitter(line);
					if (!fSplitter.hasNext()) {
						log.warn("Empty item at line {}", lineNumber);
						continue;
					}
					String label = fSplitter.next();
					List<Attribute> features = toAttributes(fSplitter, lineNumber);
					labels.add(label);
					items.add(features);
				}
			}
			// add last instance if any
			if (items.size() != labels.size()) {
				throw new IllegalStateException();
			}
			if (!items.isEmpty()) {
				trainer.append(items, labels, 0);
				instancesCounter++;
				items = null;
				labels = null;
			}
			// report
			log.info("{} instances have been read", instancesCounter);
			// train
			log.info("Starting crfsuite Trainer training...");
			trainer.trainWithoutHoldout(modelFile.getPath());
		} finally {
			trainer.dispose();
		}
	}

	private static StrTokenizer getFeatureSplitter(String src) {
		StrTokenizer result = new StrTokenizer(src);
		result.setDelimiterChar('\t');
		result.setIgnoreEmptyTokens(true);
		return result;
	}

	private static <E> List<E> newList() {
		return new LinkedList<E>();
	}

	private static List<Attribute> toAttributes(Iterator<String> iter, final int line) {
		List<Attribute> result = newList();
		while (iter.hasNext()) {
			String attrStr = iter.next();
			int colonIdx = attrStr.indexOf(':');
			if (colonIdx != attrStr.lastIndexOf(':')) {
				throw new IllegalStateException(String.format(
						"Illegal colon usage in attribute string '%s' at line %s",
						attrStr, line));
			}
			Attribute attr;
			if (colonIdx < 0) {
				attr = new Attribute(attrStr);
			} else {
				String attrName = attrStr.substring(0, colonIdx);
				Double attrWeight;
				try {
					attrWeight = Double.valueOf(attrStr.substring(colonIdx + 1));
				} catch (NumberFormatException e) {
					throw new IllegalStateException(String.format(
							"For attribute '%s' at line %s", attrStr, line), e);
				}
				attr = new Attribute(attrName, attrWeight);
			}
			result.add(attr);
		}
		return result;
	}
}