/**
 * @author Masoom Shaikh
 * masoomshaikh@users.sourceforge.net
 */
package chatclient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JavaChatLogin
    extends JDialog
    implements ActionListener
{
  private JTextField userTF = new JTextField(25);
  private JTextField addressTF = new JTextField(25);
  private JPasswordField passTF = new JPasswordField(25);
  private JPanel userPane = new JPanel();
  private JPanel passPane = new JPanel();
  private JPanel addressPane = new JPanel();
  private JButton login = new JButton("Login");
  private JButton cancel = new JButton("Cancel");
  private JPanel infoPane = new JPanel();
  private JPanel buttonPane = new JPanel();
  private JPanel centrePane = new JPanel();
  private JLabel userPromptLabel = new JLabel();
  private String command;
  public JavaChatLogin(JFrame parent, String str, String usrPrmt)
  {
    super(parent, "JavaChat: " + str, true);
    userPromptLabel.setText(usrPrmt);
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());

    infoPane.add(userPromptLabel);
    infoPane.add(new JSeparator());
    userPane.add(new JLabel("Username"));
    userPane.add(userTF);
    passPane.add(new JLabel("Password"));
    passPane.add(passTF);
    passTF.setEchoChar('*');
    addressPane.add(new JLabel("Server I.P."));
    addressPane.add(addressTF);
    addressTF.setText("127.0.0.1");
    centrePane.setLayout(new GridLayout(3, 1));
    centrePane.add(userPane);
    centrePane.add(passPane);
    centrePane.add(addressPane);

    buttonPane.setLayout(new FlowLayout());
    buttonPane.add(login);
    login.addActionListener(this);
    buttonPane.add(cancel);
    cancel.addActionListener(this);

    cp.add(centrePane, BorderLayout.CENTER);
    cp.add(infoPane, BorderLayout.NORTH);
    cp.add(buttonPane, BorderLayout.SOUTH);
    setSize(375, 200);
    setResizable(false);
    Point loc = parent.getLocation();
    setLocation(loc.x,loc.y+100);
    setVisible(true);

    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent we)
      {
        setVisible(false);
      }
    });
  }

  public String getPressedButton()
  {
    return command;
  }

  public String getUsername()
  {
    return userTF.getText();
  }

  public String getPassword()
  {
    return new String(passTF.getPassword());
  }

  public String getAddress()
  {
    return addressTF.getText();
  }

  public void actionPerformed(ActionEvent e)
  {
    command = e.getActionCommand();
    String user = userTF.getText();
    String pass = new String(passTF.getPassword());
    if (command.equals("Login"))
    {
      if (user.equals(""))
      {
        JOptionPane.showMessageDialog(this, "Username Field Cannot Be Empty",
                                      "Username Required",
                                      JOptionPane.ERROR_MESSAGE);
        return;
      }
      if (pass.equals(""))
      {
        JOptionPane.showMessageDialog(this, "Anonymous Login Is Not Allowed",
                                      "Password Required",
                                      JOptionPane.ERROR_MESSAGE);
        return;
      }
    }
    setVisible(false);
  }
}
