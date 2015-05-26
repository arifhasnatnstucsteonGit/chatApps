/**
 * @author Masoom Shaikh
 * masoomshaikh@users.sourceforge.net
 */
package chatserver;

import java.io.*;
import java.net.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import common.*;

public class ServerAppFrame
    extends JFrame
    implements ActionListener, MessageReceiver
{
  /**
   * This Hashtable maintains a list of users and their passwords  who are
   * registered with server
   * (key,value)=(username,password)
   */
  private Hashtable user_pass = new Hashtable();
  /**
   * users currently logged in server
   * (key,value) = (username,Socket)
   */
  private Hashtable loggedUsers = new Hashtable();
  /**
   * MessageReader threads dispatched so far for users logged.
   * (key,value) = (Socket,MessageReader)
   */
  private Hashtable threadTable = new Hashtable();
  private ServerSocket listeningPort;
  private AcceptConnection acceptor;

  private JList chatList = new JList();
  private JScrollPane SP = new JScrollPane();
  private JLabel status = new JLabel();
  private JMenuBar menu = new JMenuBar();

  
  
  
  
  public ServerAppFrame(String str)
  {
    super(str);
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    																			initMenuBar();
    setJMenuBar(menu);
    SP.getViewport().add(chatList);
    chatList.setModel(new DefaultListModel());
    cp.add(SP, BorderLayout.CENTER);
    cp.add(status, BorderLayout.SOUTH);
    
    
    
    
    
    
    
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent we)
      {
        int choice = JOptionPane.showConfirmDialog(null,
            "Are you sure you want to exit ?",
            "Exit",
            JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION)
        {
        																							disconnect();
          System.exit(0);
        }
      }
    });
    
    
    
    
    
    
    setIconImage(new ImageIcon("resources\\SERVER.GIF").getImage());
    setSize(400, 500);
    setLocation(350, 170);
    setVisible(true);
    																				connect();
    showStatus("Ready");
  }

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  /**
   * This function authenticates a user based on username and password.It
   * returns error codes based on whether username was found or not and if
   * username was found but password was false.
   * @param user username to be verified
   * @param pass pass for authentication
   * @return return type base on type of error.
   * e.g. unknown username or bad password error codes(i.e. constants from
   * JCConstants) are returned
   * @see common.JCConstants
   */
  private int authenticate(String user, String pass)
  {
    try
    {
      String tmpPass = (String) user_pass.get(user);
      if (tmpPass.equals(pass))
      {
        return JCConstants.USER_AUTHENTICATED;
      }
      return JCConstants.CSM_ERROR_BAD_PASSWORD;
    }
    catch (NullPointerException ex)
    {
      return JCConstants.CSM_ERROR_UNKNOWN_USERNAME;
    }
  }

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  /**
   * This function broadcast to all connected clients that it is shutting down
   * kills all threads spawned so far,clears all data structures and finally
   * kills the listener thread which is used to accept connection from clients.
   */
  private void disconnect()
  {
    /**
     * we are going to Shut down,so we must broadcast this to every client.
     */
    Message msg = new Message(JCConstants.CSM_SERVER_SHUTDOWN, null, null, null, null, null);
    broadcastMessage(msg);
    /**
     * First of all we must stop accepting any more connections.For this to
     * happen we stop the AcceptConnection thread.
     */
    try
    {
      listeningPort.close();
    }
    catch (IOException ex)
    {
      System.err.println("ServerAppFrame:77" + ex.toString());
    }

    /**
     * Server maintains a Hashtable of (key,value)=(username,Socket) pair
     * so we obtain each key (i.e. username) and use it to obtain the value
     * (i.e. SOcket) and subsequently close it.
     * Similarily Server also maintains a Hashtable of
     * (key,value)=(Socket,MessageReader) pair.Thus if we obtain the Socket
     * object associated (as told earlier in this commnet above) we can obtain
     * the MessageReader thread associated with this user and stop that too.
     */
    Enumeration enumm = loggedUsers.keys();
    while (enumm.hasMoreElements())
    {
    	
      /**
       * obtain username
       */
      String usr = (String) enumm.nextElement();
      /**
       * obtain Socket object,key=usr
       */
      Socket conn = (Socket) loggedUsers.get(usr);
      /**
       * finally get the MessageReader thread and stop that,key=conn
       */
      MessageReader reader = (MessageReader) threadTable.get(conn);
      reader.stop();
      try
      {
        conn.close();
      }
      catch (IOException ex1)
      {
        System.err.println("ServerAppFrame:111" + ex1.toString());
      }
    }
  }

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  /**
   * This method spawns a Acceptor thread and makes server ready.
   */
  private void connect()
  {
    try
    {
      listeningPort = new ServerSocket(JCConstants.SERVER_LISTENING_PORT);
    }
    catch (BindException bxp)
    {
      JOptionPane.showMessageDialog(this,
          "Only one instance of Server can be active at a given time",
          "Error", JOptionPane.CANCEL_OPTION);
      System.exit(0);
    }
    catch (IOException ex)
    {
      System.err.println("ServerAppFrame:131");
    }
    /**
     * Dispath a Accepter Thread Connection for getting client Sockets.
     */
    acceptor = new AcceptConnection(this, listeningPort);
    /**
     * now we are done in sucessfully starting the server.
     */
  }

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public static void main(String[] args)
  {
    ServerAppFrame server = new ServerAppFrame("JavaChat Server Frame");
  }

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  private void initChatList()
  {
    chatList.setModel(new DefaultListModel());
    SP.getViewport().add(chatList);
    chatList.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent me)
      {
        if (me.getClickCount() == 2)
        {
          String user = (String) chatList.getSelectedValue();
          showStatus("User:" + user);
        }
      }
    });
  }

  public void showStatus(String str)
  {
    status.setText(str);
  }

  private void initMenuBar()
  {
    ButtonGroup group = new ButtonGroup();
    JMenu action = new JMenu("Action");
    JMenu theme = new JMenu("Theme");
    JMenuItem tmp;
    action.setMnemonic('A');
    theme.setMnemonic('T');

    JMenuItem adduser = new JMenuItem("Add New User");
    adduser.setMnemonic('A');
    adduser.addActionListener(this);
    action.add(adduser);

    JMenuItem deluser = new JMenuItem("Delete User");
    deluser.setMnemonic('D');
    deluser.addActionListener(this);
    action.add(deluser);

    JMenuItem exit = new JMenuItem("Exit");
    exit.setMnemonic('x');
    exit.addActionListener(this);
    action.add(exit);

    tmp = theme.add(new JRadioButtonMenuItem("Windows"));
    group.add(tmp);
    tmp.setMnemonic('W');
    tmp.addActionListener(this);

    tmp = theme.add(new JRadioButtonMenuItem("Motif"));
    group.add(tmp);
    tmp.setMnemonic('E');
    tmp.addActionListener(this);

    tmp = theme.add(new JRadioButtonMenuItem("Metal"));
    group.add(tmp);
    tmp.setSelected(true);
    tmp.setMnemonic('M');
    tmp.addActionListener(this);

    menu.add(action);
    menu.add(theme);
  }

  
  
  
  
  
  
  
  
  private void setUI(String str)
  {
    try
    {
      UIManager.setLookAndFeel(str);
      SwingUtilities.updateComponentTreeUI(this);
      repaint();
    }
    catch (UnsupportedLookAndFeelException ex)
    {
      JOptionPane.showMessageDialog(this, "UnsupportedLookAndFeelException",
                                    "Exception",
                                    JOptionPane.OK_OPTION);
    }
    catch (IllegalAccessException ex)
    {
      JOptionPane.showMessageDialog(this, "IllegalAccessException", "Exception",
                                    JOptionPane.OK_OPTION);
    }
    catch (InstantiationException ex)
    {
      JOptionPane.showMessageDialog(this, "InstantiationException", "Exception",
                                    JOptionPane.OK_OPTION);
    }
    catch (ClassNotFoundException ex)
    {
      JOptionPane.showMessageDialog(this, "ClassNotFoundException", "Exception",
                                    JOptionPane.OK_OPTION);
    }
  }

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  /**
   * This method is called by AcceptConnection.run() whenever a new Socket
   * is accepted via conn.accept().
   * this method will dispatch a new Thread(of type MessageReader) to read
   * from this client.
   */
 
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public void addNewUser(Socket conn)
  {
    MessageReader newReader = new MessageReader(this, conn);
    threadTable.put(conn, newReader);
    showStatus("New Connection Accepted");
  }

  private void broadcastMessage(Message m)
  {
    /**
     * Obtain Sockets for each user and send them message.
     * iter now contains Sockets to clients.
     */
    Iterator iter = loggedUsers.values().iterator();
    while (iter.hasNext())
    {
      Socket soc = (Socket) iter.next();
      sendToClient(m, soc);
    }
  }

  /**
   * This method simply sends every connected client current chatList.
   */
 
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  private void broadcastUpdatedChatList()
  {
    Message m = new Message(JCConstants.CSM_UPDATE_USER_LIST, new Vector(), null, null, null, null);
    Enumeration enumm = loggedUsers.keys();
    while (enumm.hasMoreElements())
    {
      m.userList.add(enumm.nextElement());
    }
    /**
     * Obtain Sockets for each user and send them the updated list.
     * iter now contains Sockets to clients.
     */
    Iterator iter = loggedUsers.values().iterator();
    while (iter.hasNext())
    {
      Socket soc = (Socket) iter.next();
      sendToClient(m, soc);
    }
  }

  
  
  
  
  
  
  
  
  
  
  /**
   * this function is handy in sending Message object to client.
   * @param m Message object to be sent to client.
   * @param soc Socket opened to client.
   */
 
  
  
  
  
  
  
  private void sendToClient(Message m, Socket soc)
  {
    try
    {
      ObjectOutputStream objOut = new ObjectOutputStream(soc.getOutputStream());
      objOut.writeObject(m);
    }
    catch (IOException ex)
    {
      ex.printStackTrace(System.out);
    }
  }

 
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  /**
   * This method is for threads running on server side on behalf of each user.
   * Threads use this method to notify the server whenever they read a Message
   * object.
   * @see chatserver.MessageReader
   * @param msg Message object received by the thread corresponding to a
   * user.(each user has a thread executing for him on the server side)
   * @param conn msg was received from this Socket connection.so replies to
   * client will be made to this connection.
   */
  public synchronized void receiveMessage(Message msg, Socket conn)
  {
    System.out.println("CLIENT ---> SERVER");
    msg.printMessage();

    if (msg.type == JCConstants.CR_FORWARD_MESSAGE)
    {
      broadcastMessage(msg);
      return;
    }

    /**
     * user has cancled login.but we have already spawned a thread to for this
     * connection and have a Socket object and we are required to close that.
     */
    if (msg.type == JCConstants.CSM_USER_CANCELED_LOGIN)
    {
      try
      {
        MessageReader reader = (MessageReader) threadTable.get(conn);
        threadTable.remove(conn);
        loggedUsers.remove(msg.username);
        user_pass.remove(msg.username);
        reader.stop();
        conn.close();
        return;
      }
      catch (IOException ex1)
      {
        return;
      }
      catch (NullPointerException ex1)
      {
        return;
      }
    }
    if (msg.type == JCConstants.CSM_USER_LOGIN)
    {
      int tp = authenticate(msg.username, msg.password);
      if (tp != JCConstants.USER_AUTHENTICATED)
      {
        /**
         * send the error message depending upon error code returned.
         */
        Message ms = new Message(tp, null, null, null, null, null);
        sendToClient(ms, conn);
        return;
      }
      /**
       * user is now authenticated.He must be made online now.
       * Update our loggedUsers(Hashtable) and chatList(JList) so that we can
       * display.
       */
      if (loggedUsers.containsKey(msg.username))
      {
        /**
         * user is trying to log in on a different machine without logging out
         * on the previous machine.we will notify the previous client of
         * this fact and to the new client ,we will send chatList.
         */
        /**
         * Tell previous client to log off.we will kill the thread associated
         * with this user.
         */
        int tmp = JCConstants.CSM_USER_LOGGED_ON_DIFFERENT_MACHINE;
        Message ms = new Message(tmp, null, null, null, null, null);
        Socket soc = (Socket) loggedUsers.get(msg.username);
        loggedUsers.remove(msg.username);
        DefaultListModel model = (DefaultListModel) chatList.getModel();
        model.removeElement(msg.username);
        sendToClient(ms, soc);
        try
        {
          MessageReader reader = (MessageReader) threadTable.get(soc);
          reader.stop();
          soc.close();
        }
        catch (IOException ex)
        {
        }
        /**
         * we are sending chatList to new client.
         */
        tp = JCConstants.CSM_UPDATE_USER_LIST;
        Vector vect = new Vector();
        Enumeration enumm = loggedUsers.keys();
        while (enumm.hasMoreElements())
        {
          vect.add(enumm.nextElement());
        }
        ms = new Message(tp, vect, null, null, null, null);
        sendToClient(ms, conn);
      }
      /**
       * we reached here,this means everything is fine untill now.We will
       * proceed to make him online now.
       */
      loggedUsers.put(msg.username, conn);
      DefaultListModel model = (DefaultListModel) chatList.getModel();
      model.addElement(msg.username);
      broadcastUpdatedChatList();
      return;
    }
    if (msg.type == JCConstants.CSM_REGISTER_USERNAME)
    {
      /**
       * a new user is making request to register.
       * Update our loggedUsers(Hashtable),user_pass Hashtable, and
       * chatList(JList) so that we can display.First we will make sure that
       * the user is not logging in with a name which is already occupied.
       */
      if (user_pass.containsKey(msg.username))
      {
        /**
         * username given by the user is already registered.
         * Tell him to log in with a diffrent name.
         */
        Message ms = new Message(JCConstants.CSM_ERROR_USERNAME_ALREADY_BOUND, null, null, null, null, null);
        sendToClient(ms, conn);
        return;
      }
      user_pass.put(msg.username, msg.password);
      loggedUsers.put(msg.username, conn);
      DefaultListModel model = (DefaultListModel) chatList.getModel();
      model.addElement(msg.username);
      broadcastUpdatedChatList();
      return;
    }
    /**
     * A client is making log out request.
     */
    if (msg.type == JCConstants.CSM_USER_LOGOFF)
    {
      /**
       * we need to close the Socket object for this user as well as to stop
       * the thread associated with this user
       */
      Socket socObj = (Socket) loggedUsers.get(msg.username);
      MessageReader reader = (MessageReader) threadTable.get(socObj);
      reader.stop();
      try
      {
        socObj.close();
      }
      catch (IOException ex2)
      {
        ex2.printStackTrace(System.out);
      }
      /**
       * we must update our chatList(JList),loggedUsers(Hashtable).
       */
      loggedUsers.remove(msg.username);
      DefaultListModel model = (DefaultListModel) chatList.getModel();
      model.removeElement(msg.username);
      broadcastUpdatedChatList();
      return;
    }
    /**
     * we have received a message meant for someone,so forward it to it's
     * destination
     */
    if (msg.type == JCConstants.CSM_FORWARD_MESSAGE_TO_RECEPIENT)
    {
      /**
       * based on key=recepient we obtain the corresponding destination
       * value=Socket from Hashtable
       */
      Socket dest = (Socket) loggedUsers.get(msg.recepient);
      sendToClient(msg, dest);
      return;
    }
  }

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public void actionPerformed(ActionEvent e)
  {
    String command = e.getActionCommand();
    if (command.equals("Add New User"))
    {
      String user = JOptionPane.showInputDialog(this, "Username",
                                                "Add New User",
                                                JOptionPane.OK_OPTION);
      String pass = JOptionPane.showInputDialog(this, "Password",
                                                "Add New User",
                                                JOptionPane.OK_OPTION);
      if (user.equals("") || pass.equals(""))
      {
        showStatus("Error...User Was Not Added");
        return;
      }
      user_pass.put(user, pass);
      showStatus("New User Added");
    }
    if (command.equals("Delete User"))
    {
      int choice = JOptionPane.showConfirmDialog(this,
          "Are you sure you want to delete the selected user ?",
          "Delete User",
          JOptionPane.YES_NO_OPTION);
      if (choice == JOptionPane.YES_OPTION)
      {
        int index = chatList.getSelectedIndex();
        DefaultListModel model = (DefaultListModel) chatList.getModel();
        String user = (String) model.getElementAt(index);
        Socket soc = (Socket) loggedUsers.get(user);
        Message msg = new Message(JCConstants.CSM_USER_LOGOFF, null, null, null, null, null);
        sendToClient(msg, soc);
        user_pass.remove(user);
        loggedUsers.remove(user);
        model.removeElementAt(index);
        broadcastUpdatedChatList();
        MessageReader reader = (MessageReader) threadTable.get(user);
        try
        {
          reader.stop();
          soc.close();
          showStatus("One User Deleted");
        }
        catch (IOException ex)
        {
        }
        catch (NullPointerException np)
        {
        }
      }
    }
    if (command.equals("Exit"))
    {
      int choice = JOptionPane.showConfirmDialog(this,
          "Are you sure you want to exit ?",
          "Exit",
          JOptionPane.YES_NO_OPTION);
      if (choice == JOptionPane.YES_OPTION)
      {
        disconnect();
        System.exit(0);
      }
    }
    if (command.equals("Windows"))
    {
      setUI("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    }
    if (command.equals("Motif"))
    {
      setUI("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
    }
    if (command.equals("Metal"))
    {
      setUI("javax.swing.plaf.metal.MetalLookAndFeel");
    }
  }
}
