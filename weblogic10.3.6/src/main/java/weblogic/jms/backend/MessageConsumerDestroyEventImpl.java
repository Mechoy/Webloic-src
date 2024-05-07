package weblogic.jms.backend;

import javax.transaction.xa.Xid;
import weblogic.messaging.kernel.Destination;
import weblogic.messaging.kernel.internal.events.DestinationEventImpl;

public class MessageConsumerDestroyEventImpl extends DestinationEventImpl implements MessageConsumerDestroyEvent {
   private String userBlob = null;

   public MessageConsumerDestroyEventImpl(String var1, Destination var2, String var3) {
      super(var1, var2, (Xid)null);
      this.userBlob = var3;
   }

   public String getUserBlob() {
      return this.userBlob;
   }

   public String getSelector() {
      return null;
   }
}
