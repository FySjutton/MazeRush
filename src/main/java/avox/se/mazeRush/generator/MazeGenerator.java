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

        int cx = (width % 2 == 0) ? (width / 2 - 1) : (width / 2);
        int cz = (height % 2 == 0) ? (height / 2 - 1) : (height / 2);

        for (int x = cx - 1; x <= cx + 1; x++)
            for (int z = cz - 1; z <= cz + 1; z++)
                visited[x][z] = true;

        carve(0, 0, walls, visited, width, height, random);

        walls[cx][cz - 1][2] = true; // south -> into center
        walls[cx][cz][0] = true; // center north -> toward Y
        ensureLabyrinthConnection(cx, cz - 1, 0, walls, visited, width, height, random); // north exit

        // South Y: (cx, cz+1)
        walls[cx][cz + 1][0] = true; // north -> into center
        walls[cx][cz][2] = true; // center south -> toward Y
        ensureLabyrinthConnection(cx, cz + 1, 2, walls, visited, width, height, random); // south exit

        // West Y: (cx-1, cz)
        walls[cx - 1][cz][1] = true; // east -> into center
        walls[cx][cz][3] = true; // center west -> toward Y
        ensureLabyrinthConnection(cx - 1, cz, 3, walls, visited, width, height, random); // west exit

        // East Y: (cx+1, cz)
        walls[cx + 1][cz][3] = true; // west -> into center
        walls[cx][cz][1] = true; // center east -> toward Y
        ensureLabyrinthConnection(cx + 1, cz, 1, walls, visited, width, height, random); // east exit

        // Loop chance pass, skip the entire 3x3 center zone
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                if (isCenterZone(x, z, cx, cz)) continue;
                for (int d = 0; d < 4; d++) {
                    if (walls[x][z][d]) continue;
                    int nx = x + DX[d], nz = z + DZ[d];
                    if (nx < 0 || nz < 0 || nx >= width || nz >= height) continue;
                    if (isCenterZone(nx, nz, cx, cz)) continue;
                    if (random.nextFloat() < loopChance) {
                        walls[x][z][d] = true;
                        walls[nx][nz][OPP[d]] = true;
                    }
                }
            }
        }

        return walls;
    }

    private static void ensureLabyrinthConnection(int x, int z, int dir, boolean[][][] walls, boolean[][] visited, int width, int height, Random random) {
        if (walls[x][z][dir]) return; // already connected

        int nx = x + DX[dir], nz = z + DZ[dir];
        if (nx < 0 || nz < 0 || nx >= width || nz >= height) return;
        if (isCenterZone(nx, nz, width / 2 - (width % 2 == 0 ? 1 : 0), height / 2 - (height % 2 == 0 ? 1 : 0))) return;

        walls[x][z][dir] = true;
        walls[nx][nz][OPP[dir]] = true;
    }

    private static boolean isCenterZone(int x, int z, int cx, int cz) {
        return Math.abs(x - cx) <= 1 && Math.abs(z - cz) <= 1;
    }

    public static int[] getCenterCell(int width, int height) {
        int cx = (width % 2 == 0) ? (width / 2 - 1) : (width / 2);
        int cz = (height % 2 == 0) ? (height / 2 - 1) : (height / 2);
        return new int[]{cx, cz};
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