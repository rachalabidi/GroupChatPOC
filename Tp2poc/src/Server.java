
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;



public class Server extends javax.swing.JFrame {

    ServerSocket server;
    ArrayList<PrintWriter> outToClients;
    ArrayList<String> users;
    Thread serverRun;
    ArrayList<Thread> clientHandleList;
    boolean close = true;
    
    public Server() {
        initComponents();
        outToClients = new ArrayList<>();
        users = new ArrayList<>();
        serverRun = new Thread(new ServerStart());
        close = false;
        serverRun.start();
        messageArea.append("Server Starting........\n******************** \n");
        System.out.println("Server Starting...\n******************** \n");

        setTitle("Server");
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                tellEveryClient("server:close");
                System.exit(0);
            }
            
        });
    }

    
    private void initComponents() {

        jScrollPane1 = new JScrollPane();
        messageArea = new JTextArea();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        messageArea.setEditable(false);
        messageArea.setColumns(20);
        messageArea.setFont(new Font("Tahoma", 0, 13));
        messageArea.setRows(5);
        jScrollPane1.setViewportView(messageArea);

    

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 329, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                       .addGap(20, 20, 20)
                      .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 302, GroupLayout.PREFERRED_SIZE))
                 .addGroup(layout.createSequentialGroup()
                      .addGap(31, 31, 31)
                      ))
               .addContainerGap(35, Short.MAX_VALUE))
      );

        pack();
    }                      

                                       
    
    class ServerStart implements Runnable {

        @Override
        public void run() {
            try {
            	// KIMA CODE LIFAT
                server = new ServerSocket(4000);
            } catch (IOException ex) {
                messageArea.append("Cannot open server\n");
            }
            clientHandleList = new ArrayList<>();
            while(true) {
                try {
                	// SOCKET T3 CLIENT
                    Socket clientSoc = server.accept();
                    PrintWriter out = new PrintWriter(clientSoc.getOutputStream());
                    outToClients.add(out);
                    
                    Thread t = new Thread(new ClientHandler(clientSoc, out));
                    clientHandleList.add(t);
                    t.start();
                    messageArea.append("get a new conx\n");
                    System.out.println("get a new conx\n");
                } catch (IOException ex) {
                    messageArea.append("Server cannot accept client\n");
                }

            }
        }
        
    }
    
    class ClientHandler implements Runnable {
        
        BufferedReader inFromClient;
        PrintWriter outToClient;
        
        ClientHandler(Socket client, PrintWriter outToClient) {
            try {
                inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                this.outToClient = outToClient;
            } catch (IOException ex) {
                messageArea.append("Cannot read input stream...\n");
                System.out.println("Cannot read input stream...\n");
            }
        }
        
        
        @Override
        public void run() {
            try {
                final String connect = "Connect", disconnect = "Disconnect", chat = "Chat" ;
                String message;
                String[] data;
                while((message = inFromClient.readLine()) != null) {
                    messageArea.append(message+"\n");
                    data = message.split(":");
                    if(data[2].equals(connect)) {
                        users.add(data[0]);
                        tellEveryClient(message+":"+getOnlineUsersString());
                    }else if(data[2].equals(disconnect)) {
                        users.remove(data[0]);
                        tellEveryClient(message+":"+getOnlineUsersString());
                        
                    }else if(data[2].equals(chat)) {
                        tellEveryClient(message);
                    }
                }
            } catch (IOException ex) {
                messageArea.append("Lost a Connection\n");
                System.out.println("Lost a Connection\n");
            }
        }
        
    }
    
    String getOnlineUsersString() {
        if(users.isEmpty()) return "";
        String temp=users.get(0);
        int i = 0;
        for (String username: users) {
            if(i==0) {
                i++;
                continue;
            }
            temp+="-"+username;
            i++;
        }
        return temp;
    }
    
    void tellEveryClient(String message) {
        for (PrintWriter out : outToClients) {
            //System.out.println(message);
            out.println(message);
            out.flush();
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
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
      

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Server().setVisible(true);
            }
        });
    }

    // Variables declaration                     
   
    private JScrollPane jScrollPane1;
    private JTextArea messageArea;
                       
}
