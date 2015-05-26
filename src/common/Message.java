/**
 * @author Masoom Shaikh
 * masoomshaikh@users.sourceforge.net
 */
package common;

import java.io.*;
import java.util.*;

public class Message
    implements Serializable
{
  public int type;
  public Vector userList;
  public String message;
  public String username;
  public String recepient;
  public String password;

  public Message(int tp, Vector usrLst, String msg, String usrnam, String rec,
                 String pass)
  {
    type = tp;
    message = msg;
    username = usrnam;
    recepient = rec;
    password = pass;
    userList = (usrLst == null) ? usrLst : new Vector(usrLst);
  }

  /**
   * This function is not required for functioning of JavaChat but greatly
   * simplifies debugging procedures.
   */
  public void printMessage()
  {
    switch (type)
    {
      case JCConstants.CSM_FORWARD_MESSAGE_TO_RECEPIENT:
        System.out.println("CSM_FORWARD_MESSAGE_TO_RECEPIENT");
        break;
      case JCConstants.CSM_USER_LOGIN:
        System.out.println("CSM_USER_LOGIN");
        break;
      case JCConstants.CSM_REGISTER_USERNAME:
        System.out.println("CSM_REGISTER_USERNAME");
        break;
      case JCConstants.CSM_USER_LOGOFF:
        System.out.println("CSM_USER_LOGOFF");
        break;
      case JCConstants.CSM_USER_LOGGED_ON_DIFFERENT_MACHINE:
        System.out.println("CSM_USER_LOGGED_ON_DIFFERENT_MACHINE");
        break;
      case JCConstants.CSM_UPDATE_USER_LIST:
        System.out.println("CSM_UPDATE_USER_LIST");
        break;
      case JCConstants.CSM_ERROR_USERNAME_ALREADY_BOUND:
        System.out.println("CSM_ERROR_USERNAME_ALREADY_BOUND");
        break;
      case JCConstants.CSM_ERROR_UNKNOWN_USERNAME:
        System.out.println("CSM_ERROR_UNKNOWN_USERNAME");
        break;
      case JCConstants.CSM_ERROR_BAD_PASSWORD:
        System.out.println("CSM_ERROR_BAD_PASSWORD");
        break;
      case JCConstants.CSM_SERVER_SHUTDOWN:
        System.out.println("CSM_SERVER_SHUTDOWN");
        break;
      case JCConstants.CSM_USER_CANCELED_LOGIN:
        System.out.println("CSM_USER_CANCELED_LOGIN");
        break;
      case JCConstants.CM_FORWARD_MESSAGE_TO_RECEPIENT:
        System.out.println("CM_FORWARD_MESSAGE_TO_RECEPIENT");
        break;
      case JCConstants.CM_REMOVE_ENTRY:
        System.out.println("CM_REMOVE_ENTRY");
        break;
      case JCConstants.USER_AUTHENTICATED:
        System.out.println("USER_AUTHENTICATED");
        break;
      case JCConstants.CR_FORWARD_MESSAGE:
        System.out.println("CR_FORWARD_MESSAGE");
        break;
      case JCConstants.SERVER_LISTENING_PORT:
        System.out.println("SERVER_LISTENING_PORT");
        break;
    }
    System.out.println("username: " + username);
    System.out.println("password: " + password);
    System.out.println("recepient: " + recepient);
    System.out.println("message: " + message + "\n");
  }
}
