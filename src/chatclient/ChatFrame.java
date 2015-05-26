/**
 * @author Masoom Shaikh
 * masoomshaikh@users.sourceforge.net
 */
package chatclient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import common.*;

public class ChatFrame
    extends JFrame
    implements ActionListener
{
  private JPanel TAPane = new JPanel();
  private JPanel logPane = new JPanel();
  private JPanel wrtPane = new JPanel();
  private JScrollPane logSP = new JScrollPane();
  private JScrollPane wrtSP = new JScrollPane();
  private JTextArea logTA = new JTextArea();
  private JTextArea wrtTA = new JTextArea();

  private JPanel buttonPane = new JPanel();
  private JButton send = new JButton("Send");
  private JButton close = new JButton("Close");

  /**
   * we are chatting with this user
   */
  private String username, recepient;
  /**
   * This is a reference to client.we will use this reference to notify
   * client when we want to send text to recepient and also to notify when we
   * close chat session with recepient
   */
  public ClientAppFrame client;

  public ChatFrame(String usr, String rec)
  {
    super("JavaChat: " + rec);
    /**
     * username for whom we have this ChatFrame open.
     */
    recepient = rec;
    /**
     * we ourselves.
     */
    username = usr;

    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    initMenu();
    initButtonPane();
    initLayout();
    cp.add(TAPane, BorderLayout.CENTER);
    cp.add(buttonPane, BorderLayout.SOUTH);

    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent we)
      {
        /**
         * notify client that we have ended chat session with recepient
         * so that it can remove the entry in Hashtable for it.
         */
        setVisible(false);
        int type = JCConstants.CM_REMOVE_ENTRY;
        Message msg = new Message(type, null, null, null, recepient, null);
        client.notifyClient(msg);
      }
    });
    setIconImage(new ImageIcon("resources\\CHAT.GIF").getImage());
    setLocation(300, 200);
    setSize(500, 400);
    setVisible(true);
  }

  private void initLayout()
  {
    TAPane.setLayout(new GridLayout(2, 1));
    logPane.setLayout(new BorderLayout());
    wrtPane.setLayout(new BorderLayout());
    logPane.add(new JLabel("Chat Log Area", JLabel.CENTER), BorderLayout.NORTH);
    logSP.getViewport().add(logTA);
    logPane.add(logSP, BorderLayout.CENTER);
    wrtPane.add(new JLabel("Chat Edit Area", JLabel.CENTER), BorderLayout.NORTH);
    wrtSP.getViewport().add(wrtTA);
    wrtPane.add(wrtSP, BorderLayout.CENTER);
    TAPane.add(logPane);
    TAPane.add(wrtPane);
    logTA.setEditable(false);
  }

  private void initButtonPane()
  {
    buttonPane.setLayout(new FlowLayout());
    buttonPane.add(send);
    buttonPane.add(close);
    send.addActionListener(this);
    send.setMnemonic('S');
    close.addActionListener(this);
    close.setMnemonic('E');
  }

  private void initMenu()
  {
    JMenuItem tmp;
    JMenuBar mainMenu = new JMenuBar();
    JMenu file = new JMenu("File");
    JMenu theme = new JMenu("Theme");

    ButtonGroup group = new ButtonGroup();

    file.setMnemonic('F');
    theme.setMnemonic('T');

    tmp = file.add(new JMenuItem("Send"));
    tmp.setMnemonic('S');
    tmp.addActionListener(this);

    file.addSeparator();

    tmp = file.add(new JMenuItem("Close"));
    tmp.setMnemonic('C');
    tmp.addActionListener(this);

    tmp = theme.add(new JRadioButtonMenuItem("Windows"));
    group.add(tmp);
    tmp.setMnemonic('W');
    tmp.addActionListener(this);

    tmp = theme.add(new JRadioButtonMenuItem("Motif"));
    group.add(tmp);
    tmp.setMnemonic('M');
    tmp.addActionListener(this);

    tmp = theme.add(new JRadioButtonMenuItem("Metal"));
    group.add(tmp);
    tmp.setSelected(true);
    tmp.setMnemonic('E');
    tmp.addActionListener(this);

    mainMenu.add(file);
    mainMenu.add(theme);
    setJMenuBar(mainMenu);
  }

  /**
   * This method is called by ChatAppFrame to update the log text area of
   * ChatFrame by appending the text sent by recepient.
   * @param str text sent by recepient.
   * @param who this boolean values indentifies who is inserting the text.
   * client on behalf of recepient or client himself to notify something
   * regarding this particular user.who = true implies client is inserting the
   * text.
   */
  public void appendToLogTA(String str, boolean who)
  {
    if (who)
    {
      logTA.append(str + "\n");
      return;
    }
    logTA.append(recepient.toUpperCase() + " : " + str + "\n");
  }

  public void actionPerformed(ActionEvent ae)
  {
    String command = ae.getActionCommand();

    if (command.equals("Send"))
    {
      String str = wrtTA.getText();
      if (str.equals(""))
      {
        return;
      }
      wrtTA.setText("");
      logTA.append(username.toUpperCase() + " : " + str + "\n");
      int type = JCConstants.CM_FORWARD_MESSAGE_TO_RECEPIENT;
      Message msg = new Message(type, null, str, username, recepient, null);
      client.notifyClient(msg);
    }
    if (command.equals("Close"))
    {
      /**
       * notify client that we have ended chat session with recepient
       * so that it can remove the entry in Hashtable for it.
       */
      setVisible(false);
      int type = JCConstants.CM_REMOVE_ENTRY;
      Message msg = new Message(type, null, null, null, recepient, null);
      client.notifyClient(msg);
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
}
