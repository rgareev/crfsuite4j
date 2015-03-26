/**
 *
 */
package ru.kfu.itis.issst.crfsuite4j;

import org.apache.commons.lang3.text.StrTokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Rinat Gareev (Kazan Federal University)
 */
public class CrfSuiteTraining extends AbstractCrfSuiteTraining {

    private Reader trainingDataReader;

    public Reader getTrainingDataReader() {
        return trainingDataReader;
    }

    /**
     * @param trainingDataReader a training data containing sequence instances formatted
     *                           according to crfsuite CLI front-end
     */
    public void setTrainingDataReader(Reader trainingDataReader) {
        this.trainingDataReader = trainingDataReader;
    }

    @Override
    protected void validateConfig() {
        super.validateConfig();
        if (trainingDataReader == null) {
            throw new NullPointerException("trainingDataReader");
        }
    }

    @Override
    protected void appendTrainingData(CrfSuiteTrainer trainer) throws IOException {
        BufferedReader inReader = trainingDataReader instanceof BufferedReader
                ? (BufferedReader) trainingDataReader
                : new BufferedReader(trainingDataReader);
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