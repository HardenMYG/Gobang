package AI;

import java.awt.Point;

import Window.AIBattlePanel;

public class MyComputerAI {
    private int[] X = {1, 1, 0, -1, -1, -1, 0, 1};                // X方向数组
    private int[] Y = {0, 1, 1, 1, 0, -1, -1, -1};                // Y方向数组
    private int[][] weight = new int[AIBattlePanel.BORDER_SIZE][AIBattlePanel.BORDER_SIZE];          // 权重数组
    private int ComputerChessColor;      // 电脑执棋颜色

    // 检查位置是否在棋盘内
    private boolean isInBoard(int x, int y, int[][] ans) {
        return x >= 0 && x < ans.length && y >= 0 && y < ans[0].length;
    }

    private int getWeight(int x, int y, int[][] chessboard) {
        int sum = 0;
        for (int i = 0; i < X.length; i++) {
            int newX = x + X[i];
            int newY = y + Y[i];
            if (!isInBoard(newX, newY, chessboard)) {
                continue;
            }

            if (chessboard[newX][newY] == ComputerChessColor) {
                int positive = 0;
                while (isInBoard(newX, newY, chessboard) && chessboard[newX][newY] == ComputerChessColor) {
                    positive++;
                    newX += X[i];
                    newY += Y[i];
                }
                sum += positive;
            } else if (chessboard[newX][newY] == -ComputerChessColor) {
                int positive = 0;
                while (isInBoard(newX, newY, chessboard) && chessboard[newX][newY] == -ComputerChessColor) {
                    positive++;
                    newX += X[i];
                    newY += Y[i];
                }
                sum += positive*10;
            }
        }
        return sum;
    }

    public void FillWeight(int[][] chessboard, Point point, int computercolor) {
        this.ComputerChessColor = computercolor;
        int max = -1;
        point.x = -1;
        point.y = -1;
        for (int i = 0; i < weight.length; i++) {
            for (int j = 0; j < weight[i].length; j++) {
                if (chessboard[i][j] == 0) {
                    weight[i][j] = getWeight(i, j, chessboard);
                    if (max < weight[i][j]) {
                        max = weight[i][j];
                        point.x = i;
                        point.y = j;
                    }
                }
            }
        }
    }
}
