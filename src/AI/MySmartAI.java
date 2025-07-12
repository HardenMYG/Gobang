package AI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;

class Node {
    public Node bestchild; // 最佳落子点
    public ArrayList<Node> child = new ArrayList<Node>(); // 孩子节点数组
    public Point point = new Point(); // 该节点代表的点
    public int alpha; // Alpha值
    public int beta; // Beta值

    public Node() {
        this.bestchild = null;
        alpha = Integer.MIN_VALUE;
        beta = Integer.MAX_VALUE;
    }
}

public class MySmartAI {
    int[][] chessboard;
    int AIChessColor;

    public static final int MAX = 222222;

    // 八个方位
    public static int[] X = { 1, 1, 0, -1, -1, -1, 0, 1 };
    public static int[] Y = { 0, 1, 1, 1, 0, -1, -1, -1 };
    public static HashSet<Point> NeedJudge = new HashSet<Point>();
    public static int SearchDeep = 2;

    public MySmartAI(int[][] chessboard, int chess) {
        this.chessboard = new int[chessboard.length][chessboard[0].length];
        for (int i = 0; i < chessboard.length; i++) {
            System.arraycopy(chessboard[i], 0, this.chessboard[i], 0, chessboard[i].length);
        }
        AIChessColor = chess;
    }

    public Point AIStart() {
        Node e = new Node();
        Alpha_Beta_Dfs(0, e);
        return e.bestchild.point;
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < chessboard.length && y >= 0 && y < chessboard[0].length;
    }

    public void Alpha_Beta_Dfs(int deep, Node e) {
        if (isEnd() || deep == SearchDeep) {
            e.alpha = getScore();
            e.beta = e.alpha;
            return;
        }

        NeedJudge.clear();

        for (int i = 0; i < chessboard.length; i++) {
            for (int j = 0; j < chessboard[i].length; j++) {
                if (chessboard[i][j] != 0) {
                    for (int k = 0; k < X.length; k++) {
                        for(int step=1;step<=4;step++) {
                            int newX = i + step*X[k];
                            int newY = j + step*Y[k];
                            if (isValidPosition(newX, newY) && chessboard[newX][newY] == 0) {
                                NeedJudge.add(new Point(newX, newY));
                            }

                        }
                    }
                }
            }
        }



        for (Point point : NeedJudge) {
            Node childNode = new Node();
            childNode.point = point;
            e.child.add(childNode);
        }

        for (Node child : e.child) {
            chessboard[child.point.x][child.point.y] = (deep % 2 == 0) ? AIChessColor : -AIChessColor;

            if (deep % 2 == 0) {
                child.alpha=e.alpha;
            }
            else{
                child.beta=e.beta;
            }

            Alpha_Beta_Dfs(deep + 1, child);
            chessboard[child.point.x][child.point.y] = 0;

            if (e.alpha >= e.beta) {
                break;
            }

            if (deep % 2 == 0) {
                int temp=e.alpha;
                e.alpha = Math.max(e.alpha, child.beta);
                if (deep == 0 && e.alpha > temp) {
                    e.bestchild = child;
                }
            } else {
                e.beta = Math.min(e.beta, child.alpha);
            }


        }
    }


    public int getScore() {
        int res = 0;
        for (int i = 0; i < chessboard.length; i++) {
            for (int j = 0; j < chessboard[i].length; j++) {
                if (chessboard[i][j] != 0) {
                    res += evaluateAllDirections(i, j, chessboard[i][j]);
                }
            }
        }
        return res;
    }

    private int evaluateAllDirections(int x, int y, int player) {
        int score = 0;
        score += evaluateDirection(x, y, player, 1, 0); // 行
        score += evaluateDirection(x, y, player, 0, 1); // 列
        score += evaluateDirection(x, y, player, 1, 1); // 左对角线
        score += evaluateDirection(x, y, player, 1, -1); // 右对角线
        return score;
    }

    private int evaluateDirection(int x, int y, int player, int dx, int dy) {
        int count = 1; // 当前棋子
        boolean[] flags = new boolean[2];
        int[] boundaries = new int[2];

        for (int step = 1; step < 5; step++) {
            int newX = x + step * dx;
            int newY = y + step * dy;
            if (isValidPosition(newX, newY)) {
                if (chessboard[newX][newY] == player) {
                    count++;
                } else {
                    boundaries[0] = step;
                    break;
                }
            }
        }
        for (int step = 1; step < 5; step++) {
            int newX = x - step * dx;
            int newY = y - step * dy;
            if (isValidPosition(newX, newY)) {
                if (chessboard[newX][newY] == player) {
                    count++;
                } else {
                    boundaries[1] = step;
                    break;
                }
            }
        }

        if (boundaries[0] > 0 && boundaries[1] > 0) {
            flags[0] = chessboard[x + boundaries[0] * dx][y + boundaries[0] * dy] == 0;
            flags[1] = chessboard[x - boundaries[1] * dx][y - boundaries[1] * dy] == 0;
        } else if (boundaries[0] > 0 || boundaries[1] > 0) {
            flags[0] = boundaries[0] > 0 && chessboard[x + boundaries[0] * dx][y + boundaries[0] * dy] == 0;
            flags[1] = boundaries[1] > 0 && chessboard[x - boundaries[1] * dx][y - boundaries[1] * dy] == 0;
        }

        int score = Situation(player) * count * count;
        if (flags[0] && flags[1]) {
            score *= 3; // 两端均开放
        } else if (flags[0] || flags[1]) {
            score *= 1.5; // 一端开放
        }

        if (count >= 5) {
            return MAX * Situation(player);
        }
        if ((flags[0] && flags[1]) && count == 4) {
            return MAX * Situation(player) / 5;
        }
        if ((flags[0] || flags[1]) && count == 4) {
            return MAX * Situation(player) / 25;
        }
        if ((flags[0] && flags[1]) && count == 3) {
            return MAX * Situation(player) / 125;
        }
        return score;
    }

    private int Situation(int chess_color) {
        return (chess_color == AIChessColor) ? 1 : -10;
    }

    private boolean isEnd() {
        for (int i = 0; i < chessboard.length; i++) {
            for (int j = 0; j < chessboard[i].length; j++) {
                if (chessboard[i][j] != 0) {
                    if (checkWinner(i, j, chessboard[i][j])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkWinner(int x, int y, int player) {
        return checkDirection(x, y, player, 1, 0) || // 行
                checkDirection(x, y, player, 0, 1) || // 列
                checkDirection(x, y, player, 1, 1) || // 左对角线
                checkDirection(x, y, player, 1, -1);  // 右对角线
    }

    private boolean checkDirection(int x, int y, int player, int dx, int dy) {
        int count = 1;
        for (int step = 1; step < 5; step++) {
            int newX = x + step * dx;
            int newY = y + step * dy;
            if (isValidPosition(newX, newY) && chessboard[newX][newY] == player) {
                count++;
            } else {
                break;
            }
        }
        for (int step = 1; step < 5; step++) {
            int newX = x - step * dx;
            int newY = y - step * dy;
            if (isValidPosition(newX, newY) && chessboard[newX][newY] == player) {
                count++;
            } else {
                break;
            }
        }
        return count >= 5; // 检查是否有五个相连
    }
}
