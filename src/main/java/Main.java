import nz.ac.waikato.cms.adams.multiway.algorithm.PARAFAC;
import nz.ac.waikato.cms.adams.multiway.algorithm.stopping.CriterionUtils;
import nz.ac.waikato.cms.adams.multiway.data.DataReader;
import nz.ac.waikato.cms.adams.multiway.data.tensor.Tensor;

import java.io.IOException;
import java.util.List;

public class Main {
  public static void main(String[] args) throws IOException {

    // Options
    final int numStarts = 10;
    final int numComponents = 4;
    final PARAFAC.Initialization initMethod = PARAFAC.Initialization.RANDOM_ORTHOGONALIZED;

    // Setup PARAFAC
    PARAFAC parafac = new PARAFAC();
    parafac.setNumStarts(numStarts);
    parafac.setNumComponents(numComponents);
    parafac.setInitMethod(initMethod);
    parafac.addStoppingCriterion(CriterionUtils.iterations(2500));
    parafac.addStoppingCriterion(CriterionUtils.improvement(10e-5));
    final Tensor tensor = loadData();

    // Run PARAFAC
    parafac.build(tensor);

    // Output
    printFinalLossPerRun(parafac);
  }

  /**
   * Print final loss for each start.
   *
   * @param parafac PARAFAC object
   */
  private static void printFinalLossPerRun(PARAFAC parafac) {
    final List<List<Double>> lossHistory = parafac.getLossHistory();
    for (int i = 0; i < lossHistory.size(); i++) {
      List<Double> loss = lossHistory.get(i);
      int lastIdx = loss.size() - 1;
      System.out.println("loss at run " + i + " = " + loss.get(lastIdx));
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
