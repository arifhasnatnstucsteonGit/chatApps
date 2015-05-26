/**
 * @author Masoom Shaikh
 * masoomshaikh@users.sourceforge.net
 */
package common;
/**
 * This interface is implemented by classes who wish to receive Message
 * objects by spawning MessageReader thread.
 * Here they are implemented by ServerAppFrame and ClientAppFrame.
 */
import java.net.*;

public interface MessageReceiver
{
   public void receiveMessage(Message msg,Socket conn);
}
