package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class LeaderBindRequest extends Request implements Externalizable {
   static final long serialVersionUID = -5214437563807672399L;
   private static final byte EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private String serverName;
   private String jndiName;

   public LeaderBindRequest(String var1, String var2) {
      super((JMSID)null, 16405);
      this.serverName = var1;
      this.jndiName = var2;
   }

   public final String getServerName() {
      return this.serverName;
   }

   public int remoteSignature() {
      return 34;
   }

   public Response createResponse() {
      return new LeaderBindResponse();
   }

   public final String getJNDIName() {
      return this.jndiName;
   }

   public String toString() {
      return "LeaderBindRequest(" + this.serverName + ":" + this.jndiName + ")";
   }

   public LeaderBindRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2 = 1;
      var1.writeInt(var2);
      super.writeExternal(var1);
      var1.writeUTF(this.serverName);
      var1.writeUTF(this.jndiName);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.serverName = var1.readUTF();
         this.jndiName = var1.readUTF();
      }
   }
}
