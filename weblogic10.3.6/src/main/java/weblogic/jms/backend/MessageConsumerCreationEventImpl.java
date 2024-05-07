package weblogic.jms.backend;

import javax.transaction.xa.Xid;
import weblogic.messaging.kernel.Destination;
import weblogic.messaging.kernel.internal.events.DestinationEventImpl;

public class MessageConsumerCreationEventImpl extends DestinationEventImpl implements MessageConsumerCreationEvent {
   private String selector = null;
   private String userBlob = null;

   public MessageConsumerCreationEventImpl(String var1, Destination var2, String var3, String var4) {
      super(var1, var2, (Xid)null);
      this.selector = var3;
      this.userBlob = var4;
   }

   public String getSelector() {
      return this.selector;
   }

   public String getUserBlob() {
      return this.userBlob;
   }
}
