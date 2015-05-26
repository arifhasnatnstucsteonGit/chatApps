/**
 * @author Masoom Shaikh
 * masoomshaikh@users.sourceforge.net
 */
package common;

/*
 * CSM stands for Client-Server-Message.These messages are meant for
 * communication between Client and Server.
 * CM stands for Client-Message.These message are meant for
 * communication between ClientAppFrame object and ChatFrame object.
 */

public interface JCConstants
{
  public static final int CSM_FORWARD_MESSAGE_TO_RECEPIENT = 0;
  public static final int CSM_USER_LOGIN = 1;
  public static final int CSM_REGISTER_USERNAME = 2;
  public static final int CSM_USER_LOGOFF = 3;
  public static final int CSM_USER_LOGGED_ON_DIFFERENT_MACHINE = 4;
  public static final int CSM_UPDATE_USER_LIST = 5;
  public static final int CSM_ERROR_USERNAME_ALREADY_BOUND = 6;
  public static final int CSM_ERROR_UNKNOWN_USERNAME = 7;
  public static final int CSM_ERROR_BAD_PASSWORD = 8;
  public static final int CSM_SERVER_SHUTDOWN = 9;
  public static final int CSM_USER_CANCELED_LOGIN = 10;

  public static final int CM_FORWARD_MESSAGE_TO_RECEPIENT = 11;
  public static final int CM_REMOVE_ENTRY = 12;
  public static final int CR_FORWARD_MESSAGE = 13;

  public static final int USER_AUTHENTICATED = 14;
  public static final int SERVER_LISTENING_PORT = 8000;
}
