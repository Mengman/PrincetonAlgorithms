import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Created by yucai on 2018/6/18.
 * Email: corrsboy@gmail.com
 */
public class Percolation {
    private static final int VIRTUAL_TOP_SITE = 0;
    private final int virtualBottomSite;

    private final WeightedQuickUnionUF uf;
    private boolean[] siteOpened;
    private final int edgeLength;
    private int openSiteCounter = 0;

    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }

        uf = new WeightedQuickUnionUF(n * n + 2); // add a virtual top site and a virtual bottom site
        siteOpened = new boolean[n * n + 2];
        siteOpened[0] = true;
        virtualBottomSite = n * n + 1;
        siteOpened[virtualBottomSite] = true;
        edgeLength = n;
    }

    public void open(int row, int col) {
        int site = coordinate2Id(row, col);

        if (siteOpened[site]) {
            return;
        }

        if (row - 1 > 0 && isOpen(row - 1, col)) {
            uf.union(site, coordinate2Id(row - 1, col));
        } else if (row - 1 == 0) {
            uf.union(site, VIRTUAL_TOP_SITE);
        }

        if (col - 1 > 0 && isOpen(row, col - 1)) {
            uf.union(site, coordinate2Id(row, col -1));
        }

        if (col < edgeLength && isOpen(row, col + 1)) {
            uf.union(site, coordinate2Id(row, col + 1));
        }

        if (row < edgeLength  && isOpen(row + 1, col)) {
            uf.union(site, coordinate2Id(row + 1, col));
        } else if (row == edgeLength && uf.connected(site, VIRTUAL_TOP_SITE)) { // MOST IMPORTANT LINE.
            // Connect to the virtual bottom site, only if this site has already connected to virtual up site(system has been percolated!!).
            // By doing this connecting to mark the system has been percolated.
            uf.union(site, virtualBottomSite);
        }

        siteOpened[site] = true;
        openSiteCounter += 1;
    }

    public boolean isOpen(int row, int col) {
        return siteOpened[coordinate2Id(row, col)];
    }

    public boolean isFull(int row, int col) {
        return uf.connected(coordinate2Id(row, col), VIRTUAL_TOP_SITE);
    }

    public int numberOfOpenSites() {
        return openSiteCounter;
    }

    /**
     * Check if system has been percolated
     *
     * Return true if virtual bottom site has connected to virtual top site.
     * If not, check all the last row site if any of them has connected to virtual top site.
     * If any of them has connected to virtual top site, then connect it to virtual bottom site and return true.
     * Otherwise return false
     *
     * @return return true if system has percolated, otherwise return false
     */
    public boolean percolates() {
        if (uf.connected(virtualBottomSite, VIRTUAL_TOP_SITE)) {
            return true;
        } else {
            for (int i = siteOpened.length - edgeLength - 1; i < siteOpened.length - 1; i++) {
                if (siteOpened[i] && uf.connected(i, VIRTUAL_TOP_SITE)) {
                    uf.union(i, virtualBottomSite);
                    return true;
                }
            }
            return false;
        }
    }

    private int coordinate2Id(int row, int col) {
        if (row < 1 || row > edgeLength) {
            throw new IllegalArgumentException();
        }

        if (col < 1 || col > edgeLength) {
            throw new IllegalArgumentException();
        }

        row -= 1;
        col -= 1;
        return row * edgeLength + col + 1;
    }

    public static void main(String[] args) {
        Percolation percolation = new Percolation(6);
        System.out.println("6, 2 is open: " + percolation.isOpen(6, 2));
        percolation.open(6, 2);
        System.out.println("6, 2 is open: " + percolation.isOpen(6, 2));
    }
}
