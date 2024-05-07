package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.JMSBrowserCreateResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class FEBrowserCreateRequest extends Request implements Externalizable {
   static final long serialVersionUID = 7024696476454603978L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int SELECTOR_MASK = 256;
   private static final int DISTRIBUTEDDESTINATION_MASK = 8192;
   public static final int START = 0;
   public static final int CONTINUE = 1;
   public static final int CONTINUE_FURTHER = 2;
   private transient JMSDispatcher dispatcher;
   private JMSID connectionId;
   private DestinationImpl destination;
   private String messageSelector;
   private transient int numberOfRetries;
   private static final PeerInfo queueBrowserChildOfSession;

   public FEBrowserCreateRequest(JMSID var1, JMSID var2, DestinationImpl var3, String var4) {
      super(var2, 520);
      this.connectionId = var1;
      this.destination = var3;
      this.messageSelector = var4;
   }

   public DestinationImpl getDestination() {
      return this.destination;
   }

   public String getMessageSelector() {
      return this.messageSelector;
   }

   public JMSDispatcher getDispatcher() {
      return this.dispatcher;
   }

   void setDestination(DestinationImpl var1) {
      this.destination = var1;
   }

   public void setDispatcher(JMSDispatcher var1) {
      this.dispatcher = var1;
   }

   public int remoteSignature() {
      return 32;
   }

   public Response createResponse() {
      return new JMSBrowserCreateResponse();
   }

   int getNumberOfRetries() {
      return this.numberOfRetries;
   }

   void setNumberOfRetries(int var1) {
      this.numberOfRetries = var1;
   }

   public FEBrowserCreateRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      int var2 = 1;
      var1 = this.getVersionedStream(var1);
      PeerInfo var3 = ((PeerInfoable)var1).getPeerInfo();
      if (var3.compareTo(queueBrowserChildOfSession) < 0) {
         this.setMethodId(519);
         this.setInvocableId(this.connectionId);
      }

      if (this.messageSelector != null) {
         var2 |= 256;
      }

      if (this.destination instanceof DistributedDestinationImpl) {
         var2 |= 8192;
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      this.destination.writeExternal(var1);
      if (this.messageSelector != null) {
         var1.writeUTF(this.messageSelector);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         if ((var2 & 8192) != 0) {
            this.destination = new DistributedDestinationImpl();
            this.destination.readExternal(var1);
         } else {
            this.destination = new DestinationImpl();
            this.destination.readExternal(var1);
         }

         if ((var2 & 256) != 0) {
            this.messageSelector = var1.readUTF();
         }

      }
   }

   static {
      queueBrowserChildOfSession = PeerInfo.VERSION_614;
   }
}
