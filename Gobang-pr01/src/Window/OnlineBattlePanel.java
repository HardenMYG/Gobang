package Window;

import Tool.Music;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import static Window.OnlinePanel.net;

public class OnlineBattlePanel extends JPanel {
        private int LocalChessColor=1;		//1黑  -1白
        private int OtherChessColor=-1;

        public boolean isAllow=false;
        public static final int CHESSBOARD_SIZE = 611;						//棋盘图片大小
        public static final int BORDER_SIZE = 15;							//棋盘横向和纵向能下子的个数
        public static final int CHESS_SIZE = CHESSBOARD_SIZE / BORDER_SIZE;	//棋子的宽和高
        public static final int CHESS_OFFSET = 7;							//棋子位置偏移量
        public static final int BLACK = 1;									//黑棋
        public static final int WHITE = -1;									//白棋
        private int[][] chessboard;				//存储棋盘上的棋子

        private BufferedImage chess_border;			//棋盘图片
        private BufferedImage chess_black;			//黑棋图片
        private BufferedImage chess_white;			//白棋图片
        private BufferedImage chess_select;			//选择框图片

        private int select_x = -1;					//选择框横索引
        private int select_y = -1;					//选择框纵索引

        private Music clickchess;				//下棋音效


        //构造函数
        public OnlineBattlePanel() {

            String audioFilePath = System.getProperty("user.dir") + "/sound/clickchess.wav";
            clickchess = new Music(audioFilePath);
            setBounds(MainWindow.WIDTH / 2 - CHESSBOARD_SIZE / 2, MainWindow.HEIGHT / 2 - CHESSBOARD_SIZE / 2, CHESSBOARD_SIZE, CHESSBOARD_SIZE);

            chessboard = new int[BORDER_SIZE][BORDER_SIZE]; // 15x15大小的棋盘

            try {
                chess_border = ImageIO.read(new File(System.getProperty("user.dir") + "/image/chessboard_bg.png"));
                chess_black = ImageIO.read(new File(System.getProperty("user.dir") + "/image/chess_black.png"));
                chess_white = ImageIO.read(new File(System.getProperty("user.dir") + "/image/chess_white.png"));
                chess_select = ImageIO.read(new File(System.getProperty("user.dir") + "/image/select_box.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }


            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    // 鼠标移动时触发
                    select_x = (e.getX() - CHESS_OFFSET) / CHESS_SIZE;
                    select_y = (e.getY() - CHESS_OFFSET) / CHESS_SIZE;
                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    // 鼠标移出时触发
                    select_x = -1;
                    select_y = -1;
                    repaint();
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if(isAllow) {
                        int pos_y = (e.getX() - CHESS_OFFSET) / CHESS_SIZE;
                        int pos_x = (e.getY() - CHESS_OFFSET) / CHESS_SIZE;
                        if (chessboard[pos_x][pos_y] != 0) {
                            return;
                        }
                        LocalPutChess(pos_x, pos_y);
                    }
                }
            });
        }
        public void otherPutChess(int row,int col){
            chessboard[row][col] = OtherChessColor;
            isAllow = true;
            repaint();
            clickchess.play();
            if(CheckWin(row,col,OtherChessColor)){
                reset();
                JOptionPane.showMessageDialog(null, "LOSE!");
                repaint();
            }else if(CheckWin(row,col,LocalChessColor)){
                reset();
                JOptionPane.showMessageDialog(null, "WIN!");
                repaint();
            }
        }
        public void LocalPutChess(int row,int col){
            net.sendChess(row,col);
            chessboard[row][col] = LocalChessColor;
            isAllow = false;
            repaint();
            clickchess.play();
            if(CheckWin(row,col,OtherChessColor)){
                reset();
                JOptionPane.showMessageDialog(null, "LOSE!");
                repaint();
            }else if(CheckWin(row,col,LocalChessColor)){
                reset();
                JOptionPane.showMessageDialog(null, "WIN!");
                repaint();
            }
        }
        private boolean CheckWin(int r, int c, int color) {
            int count = 0;		//计数器
            //行左
            for(int i = c;i >= 0; i--) {
                if(chessboard[r][i] == color) {
                    count++;
                }
                else {
                    break;
                }
            }
            //行右
            for(int i = c+1;i < chessboard[0].length; i++) {
                if(chessboard[r][i] == color) {
                    count++;
                }
                else {
                    break;
                }
            }
            if(count >= 5) {
                return true;
            }
            count = 0;
            //列上
            for(int i = r;i >= 0; i--) {
                if(chessboard[i][c] == color) {
                    count++;
                }
                else {
                    break;
                }
            }
            //列下
            for(int i = r+1;i < chessboard.length; i++) {
                if(chessboard[i][c] == color) {
                    count++;
                }
                else {
                    break;
                }
            }
            if(count >= 5) {
                return true;
            }
            count = 0;
            //斜左上
            for(int i = r,j = c;i>=0 && j>=0;i--,j--) {
                if(chessboard[i][j] == color) {
                    count++;
                }
                else {
                    break;
                }
            }
            //斜右下
            for(int i = r+1,j = c+1;i<chessboard.length && j<chessboard[0].length;i++,j++) {
                if(chessboard[i][j] == color) {
                    count++;
                }
                else {
                    break;
                }
            }
            if(count >= 5) {
                return true;
            }
            count = 0;
            //斜右上
            for(int i = r,j = c;i>=0 && j<chessboard[0].length;i--,j++) {
                if(chessboard[i][j] == color) {
                    count++;
                }
                else {
                    break;
                }
            }
            //斜左下
            for(int i = r+1,j = c-1;i<chessboard.length && j>=0;i++,j--) {
                if(chessboard[i][j] == color) {
                    count++;
                }
                else {
                    break;
                }
            }
            if(count >= 5) {
                return true;
            }
            return false;
        }


        @Override
        public void paint(Graphics g) {
            //绘制棋盘
            g.drawImage(chess_border, 0, 0, null);

            //绘制选择框
            if(select_x >= 0 && select_y >= 0&&isAllow){
                g.drawImage(chess_select, select_x * CHESS_SIZE + CHESS_OFFSET, select_y * CHESS_SIZE + CHESS_OFFSET, null);
            }

            //绘制棋子
            for(int i = 0; i < BORDER_SIZE; i++){
                for(int j = 0; j < BORDER_SIZE; j++){
                    if(chessboard[i][j] == BLACK){
                        //绘制黑棋
                        g.drawImage(chess_black, CHESS_OFFSET + j * CHESS_SIZE, CHESS_OFFSET + i * CHESS_SIZE, null);
                    }
                    else if(chessboard[i][j] == WHITE){
                        //绘制白棋
                        g.drawImage(chess_white, CHESS_OFFSET + j * CHESS_SIZE, CHESS_OFFSET + i * CHESS_SIZE, null);
                    }
                }
            }
        }

        public void surrender(){
            if(isAllow) {
                reset();
                net.sendWIN();
                JOptionPane.showMessageDialog(null, "LOSE!");
                repaint();

            }
        }

        public void reset(){
            for(int i = 0; i < AIBattlePanel.BORDER_SIZE; i++){
                for(int j = 0; j < AIBattlePanel.BORDER_SIZE; j++){
                    chessboard[i][j] = 0;
                }
            }
        }
    public void setColor(int i) {
            LocalChessColor=i;
            OtherChessColor=-i;
    }

    public void setAllow(boolean b) {
            isAllow=b;
    }
}
