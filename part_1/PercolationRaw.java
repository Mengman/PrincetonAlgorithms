import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by yucai on 2018/6/14.
 * Email: corrsboy@gmail.com
 */
public class PercolationRaw {
    private static final int VIRTUAL_TOP = -1;
    private static final int BLOCKED = -2;

    private int[] siteStat;
    private int edgeLength = 0;
    private int openSiteCounter = 0;

    public PercolationRaw(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        siteStat = new int[n * n];
        edgeLength = n;
        for (int i = 0; i < siteStat.length; i++) {
            siteStat[i] = BLOCKED;
        }
    }

    public void open(int row, int col) {
        chkCoord(row, col);
        int coord = row * edgeLength + col;

        if (row - 1 < 0) {
            siteStat[coord] = VIRTUAL_TOP;
        } else {
            siteStat[coord] = coord;
        }
        int root = siteStat[coord] ;

        root = connectNeighbor(root, row - 1, col);
        root = connectNeighbor(root, row, col - 1);
        root = connectNeighbor(root, row, col + 1);
        connectNeighbor(root, row + 1, col);
        openSiteCounter += 1;
    }

    private int connectNeighbor(int root, int row, int col) {
        int neighborStat = chkSiteStat(row, col);
        if (neighborStat == BLOCKED) return root;
        else return connectRoot(root, neighborStat);
    }

    private int connectRoot(int rootA, int rootB) {
        if (rootA == rootB) {
            return rootA;
        }

        if (rootA == VIRTUAL_TOP) {
            siteStat[rootB] = VIRTUAL_TOP;
            return VIRTUAL_TOP;
        } else if (rootB == VIRTUAL_TOP) {
            siteStat[rootA] = VIRTUAL_TOP;
            return VIRTUAL_TOP;
        } else {
            siteStat[rootB] = rootA;
            return rootA;
        }
    }

    private int chkSiteStat(int row, int col) {
        if (row < 0) {
            return VIRTUAL_TOP;
        }

        if (col < 0 || col >= edgeLength || row >= edgeLength) {
            return BLOCKED;
        }

        try {
            if (isOpen(row, col)) {
                return root(row, col);
            }
        } catch (IllegalArgumentException ignored) {
        }
        return BLOCKED;
    }

    private int root(int row, int col) {
        int coord = row * edgeLength + col;
        int stat = siteStat[coord];

        if (stat == BLOCKED) {
            // site is blocked
            return BLOCKED;
        } else if (stat == VIRTUAL_TOP) {
            // site is directly connect to virtual root
            return VIRTUAL_TOP;
        } else if (stat == coord) {
            // site is open but doesn't connect to any other site
            return coord;
        } else {
            // site is open and connect to other site
            int parentCol = stat % edgeLength;
            int parentRow = stat / edgeLength;
            return root(parentRow, parentCol);
        }
    }

    public boolean isOpen(int row, int col) {
        chkCoord(row, col);
        return siteStat[row * edgeLength + col] != BLOCKED;
    }

    public boolean isFull(int row, int col) {
        return root(row, col) == VIRTUAL_TOP;
    }

    public int numberOfOpenSites() {
        return openSiteCounter;
    }

    public boolean percolates() {
        int row = edgeLength - 1;
        for (int i = 0; i < edgeLength; i++) {
            if (isOpen(row, i) && root(row, i) == VIRTUAL_TOP) {
                return true;
            }
        }
        return false;
    }

    private void chkCoord(int row, int col) {
        if (row >= edgeLength || col >= edgeLength) {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        int n = 100;

        PercolationRaw percolationRaw = new PercolationRaw(n);
        StdRandom.setSeed(41);
        int row = StdRandom.uniform(n);
        int col = StdRandom.uniform(n);

        for (int i = 0; i < 1000; i++) {

            while (!percolationRaw.percolates()) {
                while (percolationRaw.isOpen(row, col)) {
                    row = StdRandom.uniform(n);
                    col = StdRandom.uniform(n);
                }
                percolationRaw.open(row, col);
            }

            System.out.printf("System is percolates, p=%.3f\n", ( percolationRaw.numberOfOpenSites() / (double)(n*n) ));
            percolationRaw = new PercolationRaw(n);
        }

    }

}
