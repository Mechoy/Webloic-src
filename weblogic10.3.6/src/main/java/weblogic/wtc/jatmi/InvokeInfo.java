package weblogic.wtc.jatmi;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.internal.TCSecurityManager;
import com.bea.core.jatmi.intf.TCAuthenticatedUser;
import java.io.Serializable;

public final class InvokeInfo extends TPServiceInformation {
   private static final long serialVersionUID = 5871100598307211336L;
   private tfmh service_message;
   private Serializable service_reqid;
   private TCAuthenticatedUser mySubject = null;

   public InvokeInfo() {
   }

   public InvokeInfo(String var1, TypedBuffer var2, int var3, tfmh var4, Serializable var5, int var6, int var7) {
      super(var1, var2, var3, var6, var7);
      this.service_message = var4;
      this.service_reqid = var5;
   }

   public tfmh getServiceMessage() {
      return this.service_message;
   }

   public Serializable getReqid() {
      return this.service_reqid;
   }

   public String toString() {
      return new String(super.toString() + ":" + this.service_message + ":" + this.service_reqid);
   }

   public void setUser() {
      if (this.mySubject != null) {
         TCSecurityManager.setAsCurrentUser(this.mySubject);
      }

   }

   public void removeUser() {
      if (this.mySubject != null) {
         TCSecurityManager.removeCurrentUser();
      }

   }

   public void setTargetSubject(TCAuthenticatedUser var1) {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         if (var1 != null) {
            ntrace.doTrace("[/InvokeInfo/setTargetSubject/(" + var1.toString() + ")");
         } else {
            ntrace.doTrace("[/InvokeInfo/setTargetSubject/subj is null");
         }
      }

      this.mySubject = var1;
      if (var2) {
         ntrace.doTrace("]/InvokeInfo/setTargetSubject/10");
      }

   }

   public TCAuthenticatedUser getUser() {
      return this.mySubject;
   }
}
