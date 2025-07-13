package Window;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import Button.MenuButton;
import org.w3c.dom.Text;

import static Window.AIBattlePanel.CHESSBOARD_SIZE;


public class OnlinePanel extends JPanel {

    public static boolean Listen=false;
    public static boolean Connect=false;
    public static boolean isConnected=false;
    public static Net net=new Net();
    public static OnlineBattlePanel onlinebattlePanel =new OnlineBattlePanel();
    private JPanel netPanel =new JPanel();
    private JPanel ToolPanel =new JPanel();
    private JPanel ChatPanel =new JPanel();
    public static TextArea textArea=new TextArea();
    public OnlinePanel() {
        // 搭建界面
        setSize(MainWindow.WIDTH, MainWindow.HEIGHT);
        setLayout(null);

        // 设置背景图片
        JLabel BgImage = new JLabel();
        BgImage.setSize(MainWindow.WIDTH, MainWindow.HEIGHT);
        ImageIcon imageIcon = new ImageIcon("image/bg_game.jpg");
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(MainWindow.WIDTH, MainWindow.HEIGHT, Image.SCALE_DEFAULT));
        BgImage.setIcon(imageIcon);
        onlinebattlePanel.setOpaque(false);
        add(onlinebattlePanel);
        add(netPanel);
        add(ToolPanel);
        add(ChatPanel);
        netPanel.setOpaque(false);
        netPanel.setLayout(null);
        netPanel.setBounds(0, 50, MainWindow.WIDTH, 80);
        JTextField ipTF = new JTextField(20);
        JTextField portTF = new JTextField(20);
        JButton connectBtn = new JButton("连接");
        JButton listenBtn = new JButton("监听");
        ipTF.setText("localhost");
        portTF.setText("8888");
        netPanel.setLayout(new FlowLayout());
        JLabel IP=new JLabel("IP");
        IP.setFont(new Font("华文新魏", Font.PLAIN, 40));
        netPanel.add(IP);
        JLabel PORT=new JLabel("端口号");
        PORT.setFont(new Font("华文新魏", Font.PLAIN, 40));
        netPanel.add(ipTF);
        netPanel.add(PORT);
        netPanel.add(portTF);
        netPanel.add(listenBtn);
        netPanel.add(connectBtn);

        listenBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                net.beginListen(Integer.parseInt(portTF.getText()));
                Listen=true;
                listenBtn.setEnabled(false);
                onlinebattlePanel.setColor(1);
                onlinebattlePanel.setAllow(true);
            }
        });
        connectBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = ipTF.getText();
                String p = portTF.getText();
                Connect=true;
                net.connect(ip, Integer.parseInt(p));
                net.sendConnect();
                onlinebattlePanel.setColor(-1);
                onlinebattlePanel.setAllow(false);
            }
        });

        ToolPanel.setOpaque(false);
        ToolPanel.setLayout(null);
        ToolPanel.setBounds(0, 0, MainWindow.WIDTH/2-CHESSBOARD_SIZE / 2, MainWindow.HEIGHT);

        MenuButton surrenderbutton=new MenuButton();
        ImageIcon imageIconsurrender = new ImageIcon(System.getProperty("user.dir")+"/image/surrender.png");
        imageIconsurrender.setImage(imageIconsurrender.getImage().getScaledInstance(200, 140, Image.SCALE_DEFAULT));
        surrenderbutton.setIcon(imageIconsurrender);
        surrenderbutton.setBounds(20, 230, 350, 350);
        ToolPanel.add(surrenderbutton);
        surrenderbutton.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                onlinebattlePanel.surrender();

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
// 返回按钮

        ChatPanel.setOpaque(false);
        ChatPanel.setLayout(new BoxLayout(ChatPanel, BoxLayout.Y_AXIS));
        ChatPanel.setBounds(MainWindow.WIDTH/2+CHESSBOARD_SIZE / 2+20,MainWindow.HEIGHT/2-CHESSBOARD_SIZE / 2+80,MainWindow.WIDTH/2-CHESSBOARD_SIZE / 2-50,CHESSBOARD_SIZE);
        JScrollPane jsp=new JScrollPane(textArea);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setBounds(MainWindow.WIDTH/2+CHESSBOARD_SIZE / 2+25,MainWindow.HEIGHT/2-CHESSBOARD_SIZE / 2,MainWindow.WIDTH/2-CHESSBOARD_SIZE / 2-60,CHESSBOARD_SIZE);
        textArea.setEditable(false);
        TextField textField=new TextField(20);
        JButton sendbutton=new JButton("发送");
        sendbutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(isConnected||Connect) {
                    textArea.append("Me:" + textField.getText() + System.lineSeparator());
                    net.sendMessage(textField.getText());
                    textField.setText("");
                }
            }
        });
        textField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER&&(isConnected||Connect)) {
                    textArea.append("Me:"+textField.getText()+System.lineSeparator());
                    net.sendMessage(textField.getText());
                    textField.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        JPanel southPanel=new JPanel();
        southPanel.setOpaque(false);
        southPanel.add(textField);
        southPanel.add(sendbutton);
        ChatPanel.add(jsp);
        ChatPanel.add(southPanel);

        MenuButton buttonbackButton = new MenuButton();
        buttonbackButton.setBounds(0, MainWindow.HEIGHT - 150, 100, 100);
        buttonbackButton.setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/buttonback.png"));
        this.add(buttonbackButton);

        buttonbackButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                buttonbackButton.setIcon(
                        new ImageIcon(System.getProperty("user.dir")+"/image/buttonback_enter.png"));
                onlinebattlePanel.reset();
                listenBtn.setEnabled(true);
                textArea.setText("");
                portTF.setText("8888");
                if(isConnected) {
                    net.sendNEEDTOEXIT();
                    net.endListen();
                    isConnected=false;
                    Listen=false;
                }
                else if(!isConnected&&Listen) {
                    net.endListen();
                    Listen=false;
                }else if(Connect) {
                    net.sendNoConnection();
                    net.endConnect();
                    Connect = false;
                }
                MainWindow.cardLayout.show(MainWindow.mainPanel, "主界面");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                buttonbackButton.setIcon(
                        new ImageIcon(System.getProperty("user.dir")+"/image/buttonback_pressed.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonbackButton
                        .setIcon(new ImageIcon(System.getProperty("user.dir")+"/image/buttonback.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buttonbackButton.setIcon(
                        new ImageIcon(System.getProperty("user.dir")+"/image/buttonback_enter.png"));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
        this.add(BgImage);
    }

}



