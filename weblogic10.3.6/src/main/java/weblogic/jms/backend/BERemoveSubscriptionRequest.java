package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.Response;

public final class BERemoveSubscriptionRequest extends Request implements Externalizable {
   static final long serialVersionUID = -2710563626585921758L;
   private String clientId;
   private String name;
   private int clientIdPolicy;
   private JMSServerId backEndId;
   private String destinationName;
   private static final int EXTVERSION_PRE_1033 = 1;
   private static final int EXTVERSION_1033 = 2;
   private static final int EXTVERSION = 2;
   private static final int VERSION_MASK = 255;
   private static final int HAS_DESTINATION_NAME = 256;

   public BERemoveSubscriptionRequest(JMSServerId var1, String var2, String var3, int var4, String var5) {
      super((JMSID)null, 14850);
      this.backEndId = var1;
      this.destinationName = var2;
      this.clientId = var3;
      this.clientIdPolicy = var4;
      this.name = var5;
   }

   public JMSServerId getBackEndId() {
      return this.backEndId;
   }

   public String getDestinationName() {
      return this.destinationName;
   }

   public String getClientId() {
      return this.clientId;
   }

   public int getClientIdPolicy() {
      return this.clientIdPolicy;
   }

   public String getName() {
      return this.name;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public BERemoveSubscriptionRequest() {
   }

   private byte getVersion(Object var1) throws IOException {
      if (var1 instanceof PeerInfoable) {
         PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
         if (var2.compareTo(PeerInfo.VERSION_1033) < 0) {
            return 1;
         }
      }

      return 2;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2 = this.getVersion(var1);
      int var3 = var2;
      if (var2 >= 2 && this.destinationName != null) {
         var3 = var2 | 256;
      }

      var1.writeInt(var3);
      super.writeExternal(var1);
      this.backEndId.writeExternal(var1);
      var1.writeUTF(this.clientId);
      var1.writeUTF(this.name);
      if (var2 >= 2) {
         var1.writeInt(this.clientIdPolicy);
         if (this.destinationName != null) {
            var1.writeUTF(this.destinationName);
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1 && var3 != 2) {
         throw JMSUtilities.versionIOException(var3, 1, 2);
      } else {
         super.readExternal(var1);
         this.backEndId = new JMSServerId();
         this.backEndId.readExternal(var1);
         this.clientId = var1.readUTF();
         this.name = var1.readUTF();
         if (var3 >= 2) {
            this.clientIdPolicy = var1.readInt();
            if ((var2 & 256) != 0) {
               this.destinationName = var1.readUTF();
            }
         }

      }
   }
}
