package ru.kfu.itis.issst.crfsuite4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rinat Gareev
 */
public abstract class AbstractCrfSuiteTraining {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected File modelFile;
    protected String trainingAlgorithm;
    protected Map<String, String> params = new HashMap<String, String>();

    public void run() throws IOException {
        validateConfig();
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
            appendTrainingData(trainer);
            // train
            log.info("Starting crfsuite training...");
            trainer.trainWithoutHoldout(modelFile.getPath());
        } finally {
            trainer.dispose();
        }
    }

    protected void validateConfig() {
        if (modelFile == null) {
            throw new NullPointerException("modelFile");
        }
        if (trainingAlgorithm == null) {
            throw new NullPointerException("trainingAlgorithm");
        }
    }

    protected abstract void appendTrainingData(CrfSuiteTrainer trainer) throws IOException;

    public File getModelFile() {
        return modelFile;
    }

    /**
     * @param modelFile output file of a resulting model
     */
    public void setModelFile(File modelFile) {
        this.modelFile = modelFile;
    }

    public String getTrainingAlgorithm() {
        return trainingAlgorithm;
    }

    /**
     * @param trainingAlgorithm name of a training algorithm
     */
    public void setTrainingAlgorithm(String trainingAlgorithm) {
        this.trainingAlgorithm = trainingAlgorithm;
    }

    /**
     * Set a parameter of the training algorithm or the graphical model.
     *
     * @param pName  parameter name
     * @param pValue parameter value
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
}
