package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.dispatcher.DispatcherWrapper;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;
import weblogic.messaging.dispatcher.VoidResponse;

public class DDMembershipPushRequest extends Request implements Externalizable {
   private static final int EXTVERSIONDIABLO = 1;
   private static final int VERSION_MASK = 255;
   private static final int _HAS_MEMBER_LIST = 256;
   private String ddConfigName;
   private String ddJndiName;
   private DDMemberInformation[] memberList;
   private DispatcherWrapper dispatcherWrapper;

   public DDMembershipPushRequest(String var1, String var2, DDMemberInformation[] var3, DispatcherWrapper var4) {
      InvocableManagerDelegate var10002 = InvocableManagerDelegate.delegate;
      super((JMSID)null, 18711);
      this.ddConfigName = var1;
      this.ddJndiName = var2;
      this.memberList = var3;
      this.dispatcherWrapper = var4;
      if (var1 == null) {
         throw new Error(" Call BEA Support. DDMembershipPushRequest.ddConfigName = null");
      } else if (var2 == null) {
         throw new Error(" Call BEA Support. DDMembershipPushRequest.ddJndiName = null");
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

   public DDMemberInformation[] getMemberList() {
      return this.memberList;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public DDMembershipPushRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 0;
      int var3 = 1;
      if (this.memberList != null && (var2 = this.memberList.length) != 0) {
         var3 |= 256;
      }

      var1.writeInt(var3);
      super.writeExternal(var1);
      this.dispatcherWrapper.writeExternal(var1);
      var1.writeUTF(this.ddConfigName);
      var1.writeUTF(this.ddJndiName);
      if (var2 != 0) {
         var1.writeInt(var2);

         for(int var4 = 0; var4 < var2; ++var4) {
            this.memberList[var4].writeExternal(var1);
         }
      }

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
         if ((var2 & 256) != 0) {
            int var4 = var1.readInt();
            this.memberList = new DDMemberInformation[var4];

            for(int var5 = 0; var5 < var4; ++var5) {
               DDMemberInformation var6 = new DDMemberInformation();
               var6.readExternal(var1);
               this.memberList[var5] = var6;
            }
         }

      }
   }
}
