import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;


public class Client extends javax.swing.JFrame {

    /**
     * Creates new form Client
     */
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    boolean isConnect;
    String username;
    String[] users;
    
    public Client() {
        initComponents();
        setLocationRelativeTo(this);
        btDisconnect.setEnabled(false);
        btChat.setEnabled(false);
        chatWindow.setEditable(false);
        isConnect = false;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Client");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                btDisconnectActionPerformed(null);
                System.exit(0);
            }
            
        });
    }

    void listener() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String connect = "Connect", disconnect = "Disconnect", chat = "Chat" ;
                    String[] data;
                    String messReceive;
                    
                    while((messReceive=in.readLine())!=null){
                        data = messReceive.split(":");
                        if(data[0].equals("server") && data[1].equals("close")) {
                            socket.close();
                            setTitle("Client");
                            txtUsername.setEditable(true);
                            txtAddress.setEditable(true);
                            btConnect.setEnabled(true);
                            btDisconnect.setEnabled(false);
                            btChat.setEnabled(false);
                            chatWindow.setText("Server has closed");
                            isConnect = false;
                        }else if(data[2].equals(connect)) {
                            users = data[3].split("-");
                            chatWindow.append(data[0]+" "+data[1]+"\n");
                            System.out.println(data[0]+" "+data[1]+"\n");
                        }else if(data[2].equals(disconnect)) {
                            users = data[3].split("-");                 
                            chatWindow.append(data[0]+" "+data[1]+"\n");
                            System.out.println(data[0]+" "+data[1]+"\n");

                        }else if(data[2].equals(chat)) {
                            chatWindow.append(data[0]+": "+data[1]+"\n");
                            System.out.println(data[0]+" "+data[1]+"\n");

                        }
                        
                    }
                } catch (IOException ex) {
                    System.out.println("Cannot read message from server!");
                }
            }
        }).start();
    }
  
    
    
  
    private void initComponents() {

        btConnect = new JButton();
        jLabel2 = new JLabel();
        txtUsername = new JTextField();
        jScrollPane1 = new JScrollPane();
        chatWindow = new JTextArea();
        jLabel3 = new JLabel();
        btDisconnect = new JButton();
        txtChat = new JTextField();
        btChat = new JButton();
        txtAddress = new JTextField();
        jLabel1 = new JLabel();
       

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        btConnect.setText("Connect");
        btConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btConnectActionPerformed(evt);
            }
        });

        jLabel2.setText("Username");

        chatWindow.setColumns(20);
        chatWindow.setFont(new Font("Tahoma", 0, 13));
        chatWindow.setRows(5);
        jScrollPane1.setViewportView(chatWindow);

        
        btDisconnect.setText("Disconnect");
        btDisconnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btDisconnectActionPerformed(evt);
            }
        });

      

        btChat.setText("Send");
        btChat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btChatActionPerformed(evt);
            }
        });

        txtAddress.setText("localhost"); 
        jLabel1.setText("Server address");

       

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(txtUsername)
                            .addComponent(txtAddress))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(btDisconnect, GroupLayout.Alignment.TRAILING)
                            .addComponent(btConnect, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtChat, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btChat, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)))))
                .addGap(41, 41, 41))
             
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btConnect)
                    .addComponent(txtAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btDisconnect)
                    .addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                   .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3)
                    
                    .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
               .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(txtChat, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btChat))
                .addGap(28, 28, 28))
        );

        pack();
    }

    private void btConnectActionPerformed(java.awt.event.ActionEvent evt) {
        if(!isConnect) {
            username = txtUsername.getText().trim();
            if(username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your username");
                txtUsername.requestFocus();
                return;
            }
            
            try {
                socket = new Socket(txtAddress.getText(), 4000);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());
                chatWindow.setText("");
                out.println(username+":"+" ONLINE      chat:Connect");
                out.flush();
                listener();
                isConnect = true;
                btConnect.setEnabled(false);
                btDisconnect.setEnabled(true);
                btChat.setEnabled(true);
                txtChat.requestFocus();
                txtUsername.setEditable(false);
                txtAddress.setEditable(false);
                setTitle("User: "+username);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "The server is closed or connection is error");
            }
        }else {
            System.out.println("You have already connect");
        }
    }

    private void btDisconnectActionPerformed(ActionEvent evt) {
        if(isConnect) {
            try {
                out.println(username+":"+" chat:Disconnect");
                out.flush();
                socket.close();
                txtUsername.setEditable(true);
                txtAddress.setEditable(true);
                btConnect.setEnabled(true);
                btDisconnect.setEnabled(false);
                btChat.setEnabled(false);
                setTitle("Client");
               chatWindow.setText("disconnected ");
                isConnect = false;
            } catch (IOException ex) {
                System.out.println("Failed to disconnect");
            }
        }
    }

    private void btChatActionPerformed(ActionEvent evt) {
        if(isConnect) {
            String message = txtChat.getText().trim();
            if(message.isEmpty()) return;
            out.println(username+":"+message+"                :Chat");
            out.flush();
            txtChat.setText("");
            txtChat.requestFocus();
        }
    }

   
    
    
    public static void main(String args[]) {
       
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
    }

    // Variables declaration 
    private JButton btChat;
    private JButton btConnect;
    private JButton btDisconnect;
    private JTextArea chatWindow;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JScrollPane jScrollPane1;
    private JTextField txtAddress;
    private JTextField txtChat;
    private JTextField txtUsername;
   
}
