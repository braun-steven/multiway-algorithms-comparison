import nz.ac.waikato.cms.adams.multiway.algorithm.PARAFAC;
import nz.ac.waikato.cms.adams.multiway.algorithm.stopping.ImprovementCriterion;
import nz.ac.waikato.cms.adams.multiway.algorithm.stopping.IterationCriterion;
import nz.ac.waikato.cms.adams.multiway.data.DataReader;
import nz.ac.waikato.cms.adams.multiway.data.tensor.Tensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ReplicateInitialScript {

  private static final Logger logger = LoggerFactory.getLogger(ReplicateInitialScript.class);

  public static void main(String[] args) throws IOException {
    // Options
    final int numStarts = 1;
    final int maxIters = 2500;
    final double improvementTol = 10e-5;
    final int numComponents = 4;
    final PARAFAC.Initialization initMethod = PARAFAC.Initialization.SVD;

    // Data
    final Tensor data = loadData();

    // Setup stopping criteria (can also be done with CriterionUtils.time/iteration/improvement)
    final IterationCriterion iterCrit = new IterationCriterion();
    iterCrit.setMaxIterations(maxIters);
    final ImprovementCriterion impCrit = new ImprovementCriterion();
    impCrit.setTol(improvementTol);

    // Initialize PARAFAC
    final PARAFAC parafac = new PARAFAC();
    parafac.setNumComponents(numComponents);
    parafac.setInitMethod(initMethod);
    parafac.setNumStarts(numStarts);
    parafac.addStoppingCriterion(iterCrit);
    parafac.addStoppingCriterion(impCrit);

    // Run PARAFAC
    parafac.build(data);

    // Get loss
    final List<List<Double>> lossHistory = parafac.getLossHistory();
    final List<Double> firstRoundLoss =
        lossHistory.get(0); // Get first round losses since numStart = 1
    final double loss = firstRoundLoss.get(firstRoundLoss.size() - 1);

    // Get loadings
    final Map<String, Tensor> loadings = parafac.getLoadingMatrices();

    // Print results
    loadings.forEach((name, loading) -> logger.info("Loading " + name + ":\n" + loading));
    logger.info("Loss: " + loss);

    System.exit(0);
  }

  /**
   * Load the data.
   *
   * @return Data tensor
   * @throws IOException Could not read data
   */
  private static Tensor loadData() throws IOException {
    String prefix = "data/data";
    String suffix = ".csv";
    int startIdx = 1;
    int endIdx = 121;

    final double[][][] data =
        DataReader.read3WayMultiCsv(prefix, suffix, startIdx, endIdx, ",", false);
    final Tensor tensor = Tensor.create(data);
    return tensor;
  }
}
