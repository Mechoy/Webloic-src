package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.Response;

public final class FEConnectionSetClientIdRequest extends Request implements Externalizable {
   static final long serialVersionUID = -2181075251492512714L;
   private String clientId;
   private static final int EXTVERSION_PRE_1033 = 1;
   private static final int EXTVERSION_1033 = 2;
   private static final int EXTVERSION = 2;
   private static final int VERSION_MASK = 255;
   public static final int CONTINUE = 1;
   private int clientIdPolicy = 0;
   private transient Object myFocascia;

   public FEConnectionSetClientIdRequest(JMSID var1, String var2, int var3) {
      super(var1, 1799);
      this.clientId = var2;
      this.clientIdPolicy = var3;
   }

   public final String getClientId() {
      return this.clientId;
   }

   public final int getClientIdPolicy() {
      return this.clientIdPolicy;
   }

   public void setFocascia(Object var1) {
      this.myFocascia = var1;
   }

   public Object getFocascia() {
      return this.myFocascia;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public FEConnectionSetClientIdRequest() {
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
      var1 = this.getVersionedStream(var1);
      byte var2 = this.getVersion(var1);
      var1.writeInt(var2);
      super.writeExternal(var1);
      var1.writeUTF(this.clientId);
      if (var2 >= 2) {
         var1.writeInt(this.clientIdPolicy);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1 && var3 != 2) {
         throw JMSUtilities.versionIOException(var3, 1, 2);
      } else {
         super.readExternal(var1);
         this.clientId = var1.readUTF();
         if (var3 >= 2) {
            this.clientIdPolicy = var1.readInt();
         }

      }
   }
}
