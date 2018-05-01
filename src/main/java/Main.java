import nz.ac.waikato.cms.adams.multiway.algorithm.PARAFAC;
import nz.ac.waikato.cms.adams.multiway.algorithm.stopping.CriterionUtils;
import nz.ac.waikato.cms.adams.multiway.data.DataReader;
import nz.ac.waikato.cms.adams.multiway.data.tensor.Tensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws IOException {
    // Options
    final int numStarts = 1;
    final int maxIters = 2500;
    final double improvementTol = 10e-6;

    for (PARAFAC.Initialization init :
        new PARAFAC.Initialization[] {
          PARAFAC.Initialization.SVD, PARAFAC.Initialization.RANDOM_ORTHOGONALIZED
        }) {
      for (int i = 3; i <= 10; i++) {
        PARAFAC parafac = buildParafacModel(numStarts, i, maxIters, improvementTol, init);
        logger.info("(" + init + ") Number of components = " + i);
        printFinalLossPerRun(parafac);
      }
    }

    // Output

    System.exit(0);
  }

  private static PARAFAC buildParafacModel(
      int numStarts,
      int numComponents,
      int maxIters,
      double improvementTol,
      PARAFAC.Initialization initMethod)
      throws IOException {
    // Setup PARAFAC
    PARAFAC parafac = new PARAFAC();
    parafac.setNumStarts(numStarts);
    parafac.setNumComponents(numComponents);
    parafac.setInitMethod(initMethod);
    parafac.addStoppingCriterion(CriterionUtils.iterations(maxIters));
    parafac.addStoppingCriterion(CriterionUtils.improvement(improvementTol));
    final Tensor tensor = loadData();

    // Run PARAFAC
    parafac.build(tensor);
    return parafac;
  }

  /**
   * Print final loss for each start.
   *
   * @param parafac PARAFAC object
   */
  private static void printFinalLossPerRun(PARAFAC parafac) {
    final List<List<Double>> lossHistory = parafac.getLossHistory();
    for (List<Double> loss : lossHistory) {
      int lastIdx = loss.size() - 1;
      logger.info("Loss = " + loss.get(lastIdx));
    }
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
    return Tensor.create(data);
  }
}
