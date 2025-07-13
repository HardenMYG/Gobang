package Window;


import AI.MyComputerAI;
import AI.MySmartAI;
import Tool.Music;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;


@SuppressWarnings("serial")
public class AIBattlePanel extends JPanel {
    private int difficult;			    //难度（1简单		2困难）
    private int PersonChessColor;		//人类执棋颜色(1黑	 -1白)
    private int ComputerChessColor;		//电脑执棋颜色

    public int is_selected=0;
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

    private int round;							//标记轮到谁下棋了（1玩家	2电脑）
    private Stack<Point> chessboardHistory; // 用于存储棋盘历史状态的栈，方便悔棋时恢复状态
    private Music clickchess;				//下棋音效


    //构造函数
    public AIBattlePanel() {

        String audioFilePath = System.getProperty("user.dir") + "/sound/clickchess.wav";
        clickchess = new Music(audioFilePath);
        setBounds(MainWindow.WIDTH/ 2 - CHESSBOARD_SIZE / 2, MainWindow.HEIGHT / 2 - CHESSBOARD_SIZE / 2, CHESSBOARD_SIZE, CHESSBOARD_SIZE);

        chessboard = new int[BORDER_SIZE][BORDER_SIZE]; // 15x15大小的棋盘
        chessboardHistory = new Stack<>(); // 初始化棋盘历史状态栈

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

        AIBattlePanel message = this;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // 点击后释放时触发
                if (is_selected==1) {
                    if (round == 2) {
                        // 处于电脑回合，玩家不能下棋
                        return;
                    }
                    int pos_y = (e.getX() - CHESS_OFFSET) / CHESS_SIZE;
                    int pos_x = (e.getY() - CHESS_OFFSET) / CHESS_SIZE;
                    if (chessboard[pos_x][pos_y]!= 0) {
                        return;
                    }

                    chessboardHistory.push(new Point(pos_x, pos_y));

                    chessboard[pos_x][pos_y] = PersonChessColor;
                    repaint();
                    clickchess.play(); // 播放音效
                    if (CheckWin(pos_x, pos_y, PersonChessColor)) {
                        JOptionPane.showMessageDialog(message, "WIN!");
                        reset(1);
                        if (difficult == 2) {
                            MySmartAI.NeedJudge.clear();
                        }
                        if (ComputerChessColor == BLACK) {
                            ComputerRound();
                        }
                        return;
                    }
                    // 电脑下棋
                    round = 3 - round;
                    ComputerRound();
                }
            }
        });
    }


    boolean first = true;	//电脑先手第一次落子

    private void ComputerRound(){
        AIBattlePanel message = this;
        Point point = new Point(0, 0);
        if(difficult == 1){
            //简单难度，权值法
            if(first && ComputerChessColor == BLACK){
                point.x = BORDER_SIZE/2;
                point.y = BORDER_SIZE/2;
                first = false;
            }
            else {
                new MyComputerAI().FillWeight(chessboard, point, ComputerChessColor);
            }
        }
        else {
            //困难难度，博弈树
            if(first && ComputerChessColor == BLACK){
                point.x = BORDER_SIZE/2;
                point.y = BORDER_SIZE/2;
                first = false;
            }
            else {
                point = new MySmartAI(chessboard, ComputerChessColor).AIStart();
            }
        }
        if(point == null){
            return;
        }
        chessboardHistory.push(point);
        chessboard[point.x][point.y] = ComputerChessColor;
        repaint();
        clickchess.play();
        if(CheckWin(point.x, point.y, ComputerChessColor)){
            JOptionPane.showMessageDialog(message, "LOSE!");
            reset(1);
            if(difficult == 2){
                MySmartAI.NeedJudge.clear();
            }
            if(ComputerChessColor == BLACK){
                ComputerRound();
            }
            return;
        }
        round = 3 - round;
    }


    public void setAttribute(int difficult, int person_color, int computer_color){
        is_selected=1;
        this.difficult = difficult;
        this.PersonChessColor = person_color;
        this.ComputerChessColor = computer_color;

        first = false;
        //判断谁是先手
        if(person_color == 1){
            round = 1;
        }
        else if(person_color == -1){
            round = 2;
            first = true;
            MySmartAI.NeedJudge.clear();
            ComputerRound();
        }
    }

    public void reset(int flag){
        while(chessboardHistory.size() > 0){
            chessboardHistory.pop();
        }
        //清空棋盘
        for(int i = 0; i < AIBattlePanel.BORDER_SIZE; i++){
            for(int j = 0; j < AIBattlePanel.BORDER_SIZE; j++){
                chessboard[i][j] = 0;
            }
        }
        first = true;
        MySmartAI.NeedJudge.clear();
        //判断谁是先手
        if(PersonChessColor == 1){
            round = 1;
        }
        else if(PersonChessColor == -1){
            round = 2;
        }
        if(flag==0) {
            is_selected = 0;
            difficult = 1;
            PersonChessColor = 1;
            ComputerChessColor = -1;
        }
        else
            is_selected = 1;
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
        if(select_x >= 0 && select_y >= 0&&is_selected==1){
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
    public void ChessBack() {

        if (chessboardHistory.size() > 0) {
            Point point1=chessboardHistory.pop();
            chessboard[point1.x][point1.y] = 0;
            Point point2=chessboardHistory.pop();
            chessboard[point2.x][point2.y] = 0;
            repaint();
        }
    }
}

