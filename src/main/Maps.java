package main;

public class Maps {
    public static int[][] map1 = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1},
            {0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0},
            {1, 1, 1, 0, 1, 0, 1, 0, 3, 3, 3, 0, 1, 0, 1, 0, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 1, 0, 3, 3, 3, 0, 1, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1},
            {0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0},
            {1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    public static int[][] map3 = {
            {4, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 6},
            {3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
            {3, 1, 4, 2, 6, 1, 4, 2, 2, 2, 6, 1, 14, 1, 4, 2, 2, 2, 6, 1, 4, 2, 6, 1, 3},
            {3, 1, 5, 2, 7, 1, 5, 2, 2, 2, 7, 1, 15, 1, 5, 2, 2, 2, 7, 1, 5, 2, 7, 1, 3},
            {3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
            {3, 1, 13, 2, 12, 1, 14, 1, 13, 2, 2, 2, 2, 2, 2, 2, 12, 1, 14, 1, 13, 2, 12, 1, 3},
            {3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3},
            {5, 2, 2, 2, 6, 1, 9, 2, 12, 1, 13, 2, 2, 2, 12, 1, 13, 2, 8, 1, 4, 2, 2, 2, 7},
            {1, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1},
            {2, 2, 2, 2, 7, 1, 15, 1, 4, 2, 2, 12, 1, 13, 2, 2, 6, 1, 15, 1, 5, 2, 2, 2, 2},
            {1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1},
            {2, 2, 2, 2, 6, 1, 14, 1, 5, 2, 2, 2, 2, 2, 2, 2, 7, 1, 14, 1, 4, 2, 2, 2, 2},
            {1, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1},
            {4, 2, 2, 2, 7, 1, 15, 1, 13, 2, 2, 10, 2, 10, 2, 2, 12, 1, 15, 1, 5, 2, 2, 2, 6},
            {3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
            {3, 1, 13, 2, 6, 1, 13, 2, 2, 12, 1, 5, 2, 7, 1, 13, 2, 2, 12, 1, 4, 2, 12, 1, 3},
            {3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3},
            {9, 2, 6, 1, 3, 1, 14, 1, 4, 2, 2, 2, 2, 2, 2, 2, 6, 1, 14, 1, 3, 1, 4, 2, 8},
            {9, 2, 7, 1, 15, 1, 15, 1, 5, 2, 2, 6, 1, 4, 2, 2, 7, 1, 15, 1, 15, 1, 5, 2, 8},
            {3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
            {3, 1, 13, 2, 2, 2, 2, 2, 2, 12, 1, 5, 2, 7, 1, 13, 2, 2, 2, 2, 2, 2, 12, 1, 3},
            {3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
            {3, 1, 13, 2, 2, 2, 12, 1, 13, 2, 2, 2, 2, 2, 2, 2, 12, 1, 13, 2, 2, 2, 12, 1, 3},
            {3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
            {5, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 7}
    };

}
