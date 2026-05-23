package avox.se.mazeRush.generator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MazeGenerator {
    private static final int[] DX = { 0, 1, 0, -1};
    private static final int[] DZ = {-1, 0, 1, 0};
    private static final int[] OPP = { 2, 3, 0, 1};

    // walls[x][z][direction] -> true = opening, 0=N 1=E 2=S 3=W
    public static boolean[][][] generate(int width, int height, float loopChance, Random random) {
        boolean[][][] walls = new boolean[width][height][4];
        boolean[][] visited = new boolean[width][height];

        carve(0, 0, walls, visited, width, height, random);

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                for (int d = 0; d < 4; d++) {
                    if (walls[x][z][d]) continue;
                    int nx = x + DX[d], nz = z + DZ[d];
                    if (nx < 0 || nz < 0 || nx >= width || nz >= height) continue;
                    if (random.nextFloat() < loopChance) {
                        walls[x][z][d] = true;
                        walls[nx][nz][OPP[d]] = true;
                    }
                }
            }
        }

        return walls;
    }

    private static void carve(int x, int z, boolean[][][] walls, boolean[][] visited, int width, int height, Random random) {
        visited[x][z] = true;
        Integer[] dirs = {0, 1, 2, 3};
        Collections.shuffle(Arrays.asList(dirs), random);
        for (int dir : dirs) {
            int nx = x + DX[dir], nz = z + DZ[dir];
            if (nx < 0 || nz < 0 || nx >= width || nz >= height) continue;
            if (visited[nx][nz]) continue;
            walls[x][z][dir] = true;
            walls[nx][nz][OPP[dir]] = true;
            carve(nx, nz, walls, visited, width, height, random);
        }
    }
}