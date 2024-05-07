package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSConsumerSetListenerResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class BEConsumerSetListenerRequest extends Request implements Externalizable {
   static final long serialVersionUID = 3829823272672918810L;
   private boolean hasListener;
   private long lastSequenceNumber;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int HAS_LISTENER_MASK = 256;

   public BEConsumerSetListenerRequest(JMSID var1, boolean var2, long var3) {
      super(var1, 11281);
      this.hasListener = var2;
      this.lastSequenceNumber = var3;
   }

   boolean getHasListener() {
      return this.hasListener;
   }

   long getLastSequenceNumber() {
      return this.lastSequenceNumber;
   }

   public int remoteSignature() {
      return 34;
   }

   public Response createResponse() {
      return new JMSConsumerSetListenerResponse();
   }

   public BEConsumerSetListenerRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1;
      if (this.hasListener) {
         var2 |= 256;
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      var1.writeLong(this.lastSequenceNumber);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.lastSequenceNumber = var1.readLong();
         this.hasListener = (var2 & 256) != 0;
      }
   }
}
