import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
// import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by yucai on 2018/6/14.
 * Email: corrsboy@gmail.com
 */
public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private double[] trialsResults;

    public PercolationStats(int n, int trials) {
        if (!(n > 0)) {
            throw new IllegalArgumentException();
        }

        if (!(trials > 0)) {
            throw new IllegalArgumentException();
        }

        trialsResults = new double[trials];
        runTrial(n);
    }

    private void runTrial(int n) {
        int row, col;
        for (int i = 0; i < trialsResults.length; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {

                do {
                    row = StdRandom.uniform(n) + 1;
                    col = StdRandom.uniform(n) + 1;
                } while (percolation.isOpen(row, col));

                percolation.open(row, col);
            }

            trialsResults[i] = percolation.numberOfOpenSites() / (double) (n*n);
        }
    }

    public double mean() {
        return StdStats.mean(trialsResults);
    }

    public double stddev() {
        return StdStats.stddev(trialsResults);
    }

    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev()) / Math.sqrt(trialsResults.length);
    }

    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev()) / Math.sqrt(trialsResults.length);
    }

    public static void main(String[] args) {
//        Stopwatch stopwatch = new Stopwatch();
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
//        System.out.println("Time cost: " + stopwatch.elapsedTime() + " seconds");

        System.out.printf("mean                    = %f%n", stats.mean());
        System.out.printf("stddev                  = %f%n", stats.stddev());
        System.out.printf("95%% confidence interval = [%f, %f]", stats.confidenceLo(), stats.confidenceHi());
    }
}
