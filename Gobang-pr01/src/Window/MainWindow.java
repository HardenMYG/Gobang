package Window;

import Button.MenuButton;
import Tool.Music;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.BoxLayout;


public class MainWindow extends JFrame {

    public static final int WIDTH= 1400;
    public static final int HEIGHT = 850;
    public static final String TITLE = "Java五子棋";

    public static CardLayout cardLayout = new CardLayout();				//多界面布局

    public static JPanel mainPanel = new JPanel();						//总窗口panel
    private JPanel contentPanel = new JPanel();							//主界面
    private AIPanel aiPanel = new AIPanel();	//人机对战界面
    private OnlinePanel OnlinePanel = new OnlinePanel();//联机对战
    public Music music = new Music(System.getProperty("user.dir") + "/sound/BackGroundMusic.wav");

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainWindow frame = new MainWindow();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public MainWindow() throws IOException {
        Thread musicthread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    music.play();
                    try {
                        while (music.getIsPlaying()) {
                            Thread.sleep(100); // 等待音乐播放完成
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        musicthread.setDaemon(true);
        musicthread.start();
        MakeUpPanel();
    }
    public void MakeUpPanel() throws IOException {setIconImage(ImageIO.read(new File(System.getProperty("user.dir")+"/image/icon.png")));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(TITLE);

        setSize(MainWindow.WIDTH,MainWindow.HEIGHT);
        setLocationRelativeTo(null);
        contentPanel.setSize(MainWindow.WIDTH, MainWindow.HEIGHT);
        contentPanel.setLayout(null);

        mainPanel.setLayout(cardLayout);
        mainPanel.add(contentPanel);
        mainPanel.add(contentPanel, "主界面");
        mainPanel.add(aiPanel, "人机对战");
        mainPanel.add(OnlinePanel,"联机对战");
        setContentPane(mainPanel);

        //设置背景图片
        JLabel bgLabel = new JLabel();
        bgLabel.setSize(MainWindow.WIDTH, MainWindow.HEIGHT);
        ImageIcon imageIcon = new ImageIcon(System.getProperty("user.dir")+"/image/bg_main.jpeg");
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(MainWindow.WIDTH, MainWindow.HEIGHT, Image.SCALE_DEFAULT));
        bgLabel.setIcon(imageIcon);

        JLabel TitleLabel = new JLabel();
        TitleLabel.setSize(MainWindow.WIDTH/ 2, MainWindow.HEIGHT / 2);
        ImageIcon imageIcon1 = new ImageIcon(System.getProperty("user.dir") + "/image/Title.png");
        TitleLabel.setIcon(imageIcon1);
        TitleLabel.setBounds(100, 70, MainWindow.WIDTH/ 2, MainWindow.HEIGHT / 2);
        contentPanel.add(TitleLabel);

        JPanel panelMenu = new JPanel();
        panelMenu.setOpaque(false);
        panelMenu.setBounds((int)(MainWindow.WIDTH* 0.1), (int)(MainWindow.HEIGHT / 2), 302, MainWindow.HEIGHT - MainWindow.HEIGHT / 20 * 3);
        contentPanel.add(panelMenu);
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));


        //按钮
        JButton buttonAI = new MenuButton();
        buttonAI.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/button_bg.png"));
        panelMenu.add(buttonAI);
        buttonAI.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                buttonAI.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/button_bg_enter.png"));
                cardLayout.show(mainPanel, "人机对战");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                buttonAI.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/button_bg_pressed.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonAI.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/button_bg.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buttonAI.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/button_bg_enter.png"));
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });

        MenuButton buttonNet = new MenuButton();
        buttonNet.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/button_person_bg.png"));
        panelMenu.add(buttonNet);
        buttonNet.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                buttonNet.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/button_person_bg_enter.png"));
                cardLayout.show(mainPanel, "联机对战");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                buttonNet.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/button_person_bg_pressed.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonNet.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/button_person_bg.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buttonNet.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/button_person_bg_enter.png"));
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });



        JButton buttonExit = new MenuButton();
        buttonExit.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/button_exit_bg.png"));
        panelMenu.add(buttonExit);
        buttonExit.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                buttonExit.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/button_exit_bg_enter.png"));
                music.stop();
                dispose();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                buttonExit.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/button_exit_bg_pressed.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonExit.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/button_exit_bg.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buttonExit.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/button_exit_bg_enter.png"));
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });

        contentPanel.add(bgLabel);

    }
}
