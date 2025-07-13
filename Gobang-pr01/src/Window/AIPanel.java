package Window;


import Button.MenuButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static Window.AIBattlePanel.CHESSBOARD_SIZE;

public class AIPanel extends JPanel {

    private int difficult = 1;			//难度（1简单		2困难）
    private int PersonChessColor = 1;		//人类执棋颜色(1黑		-1白)
    private int ComputerChessColor = -1;	//电脑执棋颜色
    private JPanel selectPanel = new JPanel();	//设置难度和谁先下棋的面板
    private AIBattlePanel AIBattlePanel = new AIBattlePanel();	//游戏界面
    private JPanel ToolPanel = new JPanel();
    private ImageIcon imageIcondifficult;
    private ImageIcon imageIconcolor;

    public AIPanel() {
        //搭建界面
        setSize(MainWindow.WIDTH, MainWindow.HEIGHT);
        setLayout(null);

        //设置背景图片
        JLabel BgImage = new JLabel();
        BgImage.setSize(MainWindow.WIDTH, MainWindow.HEIGHT);
        ImageIcon imageIcon = new ImageIcon("image/bg_game.jpg");
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(MainWindow.WIDTH, MainWindow.HEIGHT, Image.SCALE_DEFAULT));//设置图片大小适应label

        add(selectPanel);
        add(AIBattlePanel);
        add(ToolPanel);
        AIBattlePanel.setOpaque(false);

        ToolPanel.setOpaque(false);
        ToolPanel.setLayout(null);
        ToolPanel.setBounds(0, 0, MainWindow.WIDTH/2-CHESSBOARD_SIZE / 2, MainWindow.HEIGHT);

        MenuButton huiqibutton=new MenuButton();
        huiqibutton.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/huiqibutton.png"));
        huiqibutton.setBounds(20, 230, 350, 350);
        ToolPanel.add(huiqibutton);
        huiqibutton.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                AIBattlePanel.ChessBack();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        selectPanel.setLayout(null);
        selectPanel.setOpaque(false);
        selectPanel.setBounds(MainWindow.WIDTH/ 2+CHESSBOARD_SIZE / 2, 0, 800, 800);


        JLabel label = new JLabel("选择  难度");
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setFont(new Font("华文新魏", Font.PLAIN, 40));
        label.setForeground(Color.WHITE);
        label.setBounds(80, 100, 421, 100);
        selectPanel.add(label);

        JLabel label1 = new JLabel("选择棋色(黑棋先手)");
        label1.setHorizontalAlignment(SwingConstants.LEFT);
        label1.setFont(new Font("华文新魏", Font.PLAIN, 40));
        label1.setForeground(Color.WHITE);
        label1.setBounds(20, 350, 421, 100);
        selectPanel.add(label1);

        MenuButton buttondifficult = new MenuButton();
        imageIcondifficult = new ImageIcon(System.getProperty("user.dir")+"/image/button_select_easy.png");
        imageIcondifficult.setImage(imageIcondifficult.getImage().getScaledInstance(300, 80, Image.SCALE_DEFAULT));
        buttondifficult.setIcon(imageIcondifficult);
        buttondifficult.setBounds(30, 225, 320, 90);
        selectPanel.add(buttondifficult);

        MenuButton buttoncolor = new MenuButton();
        imageIconcolor = new ImageIcon(System.getProperty("user.dir")+"/image/button_select_black.png");
        imageIconcolor.setImage(imageIconcolor.getImage().getScaledInstance(300, 80, Image.SCALE_DEFAULT));
        buttoncolor.setIcon(imageIconcolor);
        buttoncolor.setBounds(30, 475, 320, 90);
        selectPanel.add(buttoncolor);
        buttoncolor.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {

                if(PersonChessColor == 1){
                    imageIconcolor=new ImageIcon(System.getProperty("user.dir")+"/image/button_select_white.png");
                    imageIconcolor.setImage(imageIconcolor.getImage().getScaledInstance(300, 80, Image.SCALE_DEFAULT));
                    buttoncolor.setIcon(imageIconcolor);
                }
                else {
                    imageIconcolor=new ImageIcon(System.getProperty("user.dir")+"/image/button_select_black.png");
                    imageIconcolor.setImage(imageIconcolor.getImage().getScaledInstance(300, 80, Image.SCALE_DEFAULT));
                    buttoncolor.setIcon(imageIconcolor);
                }
                PersonChessColor = -PersonChessColor;
                ComputerChessColor = -PersonChessColor;
            }

            @Override
            public void mousePressed(MouseEvent e) {


            }

            @Override
            public void mouseExited(MouseEvent e) {


            }

            @Override
            public void mouseEntered(MouseEvent e) {


            }

            @Override
            public void mouseClicked(MouseEvent e) {


            }
        });
        buttondifficult.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {

                if(difficult == 1){
                    imageIcondifficult = new ImageIcon(System.getProperty("user.dir")+"/image/button_select_difficult.png");
                    imageIcondifficult.setImage(imageIcondifficult.getImage().getScaledInstance(300, 80, Image.SCALE_DEFAULT));
                    buttondifficult.setIcon(imageIcondifficult);
                }
                else {
                    imageIcondifficult = new ImageIcon(System.getProperty("user.dir")+"/image/button_select_easy.png");
                    imageIcondifficult.setImage(imageIcondifficult.getImage().getScaledInstance(300, 80, Image.SCALE_DEFAULT));
                    buttondifficult.setIcon(imageIcondifficult);
                }
                difficult = 3 - difficult;
            }

            @Override
            public void mouseClicked(MouseEvent e) {


            }

            @Override
            public void mouseEntered(MouseEvent e) {


            }

            @Override
            public void mouseExited(MouseEvent e) {


            }

            @Override
            public void mousePressed(MouseEvent e) {

            }
        });

        MenuButton buttonyes = new MenuButton();
        buttonyes.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/buttonyes.png"));
        buttonyes.setBounds(130, 600, 100, 100);
        buttonyes.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                buttonyes.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/buttonyes_enter.png"));
                AIBattlePanel.reset(1);
                AIBattlePanel.setAttribute(difficult, PersonChessColor, ComputerChessColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                buttonyes.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/buttonyes_pressed.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonyes.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/buttonyes.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buttonyes.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/buttonyes_enter.png"));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
        selectPanel.add(buttonyes);

        MenuButton buttonback = new MenuButton();
        buttonback.setBounds(0, MainWindow.HEIGHT - 150, 100, 100);
        buttonback.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/buttonback.png"));
        add(buttonback);
        buttonback.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {

                buttonback.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/buttonback_enter.png"));
                MainWindow.cardLayout.show(MainWindow.mainPanel, "主界面");
                AIBattlePanel.reset(0);
                AIBattlePanel.repaint();
                imageIconcolor = new ImageIcon(System.getProperty("user.dir")+"/image/button_select_black.png");
                imageIconcolor.setImage(imageIconcolor.getImage().getScaledInstance(300, 80, Image.SCALE_DEFAULT));
                buttoncolor.setIcon(imageIconcolor);
                imageIcondifficult = new ImageIcon(System.getProperty("user.dir")+"/image/button_select_easy.png");
                imageIcondifficult.setImage(imageIcondifficult.getImage().getScaledInstance(300, 80, Image.SCALE_DEFAULT));
                buttondifficult.setIcon(imageIcondifficult);
                selectPanel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

                buttonback.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/buttonback_pressed.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {

                buttonback.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/buttonback.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {

                buttonback.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/buttonback_enter.png"));
            }

            @Override
            public void mouseClicked(MouseEvent e) {


            }
        });

        BgImage.setIcon(imageIcon);
        this.add(BgImage);
    }
}

