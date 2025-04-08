import java.util.*;

public class Main {
    // variables of maze(wall, path, size of maze, array, random digit, entrance, exit)
    private static final int W = 1;
    private static final int P = 0;
    private int n;
    private int[][] m;
    private Random r = new Random();
    private int sx = 0, sy = 1; // entrance's coordinates
    private int ex, ey; // exit's coordinates

    // constructor
    public Main(int n) {
        this.n = n + 2; // +2 because we have walls around
        this.m = new int[this.n][this.n];
        this.ex = this.n - 1;
        this.ey = this.n - 2;
        gen();
    }

    // generator
    private void gen() {
        // fill walls everywhere
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = W;
            }
        }

        // start from 1,1
        Stack<int[]> s = new Stack<>();
        int x = 1;
        int y = 1;
        m[x][y] = P;
        s.push(new int[]{x, y});

        // possible steps(left, right, down, up)
        int[][] d = {{0, 2}, {2, 0}, {-2, 0}, {0, -2}};

        while (!s.isEmpty()) {
            // check neighbors
            int[] c = s.peek();
            x = c[0];
            y = c[1];
            ArrayList<int[]> ns = new ArrayList<>(); // possible options

            for (int[] i : d) {
                //x or y offset
                int nx = x + i[0];
                int ny = y + i[1];
                // if the neighboring cell is inside the borders are it's a wall then add it to the list of possible list
                if (nx > 0 && ny > 0 && nx < n - 1 && ny < n - 1 && m[ny][nx] == W) {
                    int[] t = new int[2];
                    t[0] = nx;
                    t[1] = ny;
                    ns.add(t);
                }
            }

            // choose random neighbor
            if (!ns.isEmpty()) {
                // creates a random path there
                int[] nt = ns.get(r.nextInt(ns.size()));
                // breaks down all the walls between these passages
                int mx = (x + nt[0]) / 2;
                int my = (y + nt[1]) / 2;
                // removes walls replacing them with passages
                m[my][mx] = P;
                m[nt[1]][nt[0]] = P; // point (1, 1)
                s.push(nt); // add and continue
            }
            // if there aren't neighbors return to start
            else {
                s.pop();
            }
        }

        // entrance and exit
        m[sy][sx] = P;
        m[ey][ex] = P;
    }

    // find solution
    public ArrayList<int[]> sol() {
        // variables for solution
        ArrayList<int[]> p = new ArrayList<>(); // path from entrance to exit
        Queue<int[]> q = new LinkedList<>();
        boolean[][] v = new boolean[n][n]; // to avoid entering the same cells again
        HashMap<String, int[]> cm = new HashMap<>(); // you can store information about which cell you came from
        // say that entrance is true
        int[] start = {sx, sy};
        q.offer(start);
        v[sy][sx] = true;

        int[][] d = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        while (!q.isEmpty()) {
            int[] c = q.poll();
            int x = c[0], y = c[1];

            // if  we reach the exit, we restore the path by moving back
            if (x == ex && y == ey) {
                while (c != null) {
                    p.add(c);
                    String key = Arrays.toString(c);
                    c = cm.get(key);
                }
                Collections.reverse(p);
                break;
            }

            // add unvisited passable cells to the queue
            for (int i = 0; i < d.length; i++) {
                int nx = x + d[i][0];
                int ny = y + d[i][1];
                if (nx >= 0 && ny >= 0 && nx < n && ny < n) {
                    if (!v[ny][nx] && m[ny][nx] == P) {
                        int[] next = {nx, ny};
                        q.add(next);
                        v[ny][nx] = true;
                        cm.put(Arrays.toString(next), c);
                    }
                }
            }
        }
        return p;
    }

    // print the maze
    public void prt(ArrayList<int[]> p) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    boolean isp = false;
                    for (int k = 0; k < p.size(); k++) {
                        int[] s = p.get(k);
                        if (s[0] == j && s[1] == i) {
                            isp = true;
                            break;
                        }
                    }
                    if (isp) {
                        System.out.print("* ");
                    } else if (m[i][j] == W) {
                        System.out.print("[]");
                    } else {
                        System.out.print("  ");
                    }
                }
                System.out.println();
        }
    }

    public static void main(String[] args) {
        int n = 11;
        Main mg = new Main(n);
        ArrayList<int[]> p = mg.sol();
        mg.prt(p);
    }
}
