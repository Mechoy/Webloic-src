package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.Response;

public final class FERemoveSubscriptionRequest extends Request implements Externalizable {
   static final long serialVersionUID = 4562660717591850604L;
   private String clientId;
   private String name;
   private int clientIdPolicy;
   private JMSServerId backEndId;
   private String destinationName;
   private DestinationImpl destination;
   private static final int EXTVERSION_PRE_1033 = 1;
   private static final int EXTVERSION_1033 = 2;
   private static final int EXTVERSION_1034 = 3;
   private static final int EXTVERSION = 3;
   private static final int VERSION_MASK = 255;
   private static final int HAS_JMSSERVER_ID = 256;
   private static final int HAS_DESTINATION_NAME = 512;
   private static final int HAS_DESTINATION = 1024;
   static final int START = 0;
   static final int WAIT = 1;

   public FERemoveSubscriptionRequest(String var1, String var2) {
      super((JMSID)null, 5377);
      this.clientIdPolicy = 0;
      this.clientId = var1;
      this.name = var2;
   }

   public FERemoveSubscriptionRequest(String var1, String var2, int var3, JMSServerId var4, String var5) {
      this(var1, var2);
      this.clientIdPolicy = var3;
      this.destinationName = var5;
      this.backEndId = var4;
   }

   public FERemoveSubscriptionRequest(String var1, String var2, int var3, DestinationImpl var4) {
      this(var1, var2);
      this.clientIdPolicy = var3;
      this.destination = var4;
      this.backEndId = var4.getBackEndId();
      this.destinationName = var4.getTopicName();
   }

   public String getClientId() {
      return this.clientId;
   }

   public String getName() {
      return this.name;
   }

   public String getDestinationName() {
      return this.destinationName;
   }

   public int getClientIdPolicy() {
      return this.clientIdPolicy;
   }

   public JMSServerId getBackEndId() {
      return this.backEndId;
   }

   void setBackEndId(JMSServerId var1) {
      this.backEndId = var1;
   }

   public DestinationImpl getDestination() {
      return this.destination;
   }

   void setDestination(DestinationImpl var1) {
      this.destination = var1;
      this.backEndId = var1.getBackEndId();
      this.destinationName = var1.getTopicName();
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public FERemoveSubscriptionRequest() {
      this.clientIdPolicy = 0;
   }

   private byte getVersion(Object var1) throws IOException {
      if (var1 instanceof PeerInfoable) {
         PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
         if (var2.compareTo(PeerInfo.VERSION_1033) < 0) {
            return 1;
         }

         if (var2.compareTo(PeerInfo.VERSION_1034) < 0) {
            return 2;
         }
      }

      return 3;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      byte var2 = this.getVersion(var1);
      int var3 = var2;
      if (var2 >= 3) {
         if (this.destination != null) {
            var3 = var2 | 1024;
         }
      } else if (var2 >= 2) {
         if (this.backEndId != null) {
            var3 = var2 | 256;
         }

         if (this.destinationName != null) {
            var3 |= 512;
         }
      }

      var1.writeInt(var3);
      super.writeExternal(var1);
      var1.writeUTF(this.clientId);
      var1.writeUTF(this.name);
      if (var2 >= 3) {
         var1.writeInt(this.clientIdPolicy);
         if (this.destination != null) {
            this.destination.writeExternal(var1);
         }
      } else if (var2 >= 2) {
         var1.writeInt(this.clientIdPolicy);
         if (this.backEndId != null) {
            this.backEndId.writeExternal(var1);
         }

         if (this.destinationName != null) {
            var1.writeUTF(this.destinationName);
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1 && var3 != 2 && var3 != 3) {
         throw JMSUtilities.versionIOException(var3, 1, 3);
      } else {
         super.readExternal(var1);
         this.clientId = var1.readUTF();
         this.name = var1.readUTF();
         if (var3 >= 3) {
            this.clientIdPolicy = var1.readInt();
            if ((var2 & 1024) != 0) {
               this.destination = new DestinationImpl();
               this.destination.readExternal(var1);
               this.backEndId = this.destination.getBackEndId();
               this.destinationName = this.destination.getTopicName();
            }
         } else if (var3 >= 2) {
            this.clientIdPolicy = var1.readInt();
            if ((var2 & 256) != 0) {
               this.backEndId = new JMSServerId();
               this.backEndId.readExternal(var1);
            }

            if ((var2 & 512) != 0) {
               this.destinationName = var1.readUTF();
            }
         }

      }
   }
}
