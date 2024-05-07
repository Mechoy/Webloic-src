package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.dispatcher.DispatcherWrapper;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public class DDMembershipRequest extends Request implements Externalizable {
   private static final int EXTVERSIONDIABLO = 1;
   private static final int VERSION_MASK = 255;
   private String ddConfigName;
   private String ddJndiName;
   DispatcherWrapper dispatcherWrapper;

   public DDMembershipRequest(String var1, String var2, DispatcherWrapper var3) {
      InvocableManagerDelegate var10002 = InvocableManagerDelegate.delegate;
      super((JMSID)null, 18455);
      this.ddConfigName = var1;
      this.ddJndiName = var2;
      this.dispatcherWrapper = var3;
      if (var1 == null) {
         throw new Error(" Call BEA Support. DDMembershipRequest.ddConfigName = null");
      } else if (var2 == null) {
         throw new Error(" Call BEA Support. DDMembershipRequest.ddJndiName = null");
      }
   }

   public DispatcherWrapper getDispatcherWrapper() {
      return this.dispatcherWrapper;
   }

   public String getDDConfigName() {
      return this.ddConfigName;
   }

   public String getDDJndiName() {
      return this.ddJndiName;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return new DDMembershipResponse();
   }

   public DDMembershipRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2 = 1;
      var1.writeInt(var2);
      super.writeExternal(var1);
      this.dispatcherWrapper.writeExternal(var1);
      var1.writeUTF(this.ddConfigName);
      var1.writeUTF(this.ddJndiName);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.dispatcherWrapper = new DispatcherWrapper();
         this.dispatcherWrapper.readExternal(var1);
         this.ddConfigName = var1.readUTF();
         this.ddJndiName = var1.readUTF();
      }
   }
}
