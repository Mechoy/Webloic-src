package weblogic.wsee.security.unt;

import java.io.Serializable;
import weblogic.security.UsernameAndPassword;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;

public class ClientUNTCredentialProvider implements CredentialProvider, Serializable {
   UsernameAndPassword textCredential;

   /** @deprecated */
   public ClientUNTCredentialProvider(String var1, String var2) {
      this.textCredential = new UsernameAndPassword(var1, var2 != null ? var2.toCharArray() : null);
   }

   public ClientUNTCredentialProvider(byte[] var1, byte[] var2) {
      this.textCredential = new UsernameAndPassword(var1 != null ? new String(var1) : null, var2 != null ? (new String(var2)).toCharArray() : null);
   }

   public String[] getValueTypes() {
      return WSSConstants.UNT_VALUETYPES;
   }

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4) {
      return this.textCredential;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[ClientUNTCredentialProvider: username=");
      var1.append(this.textCredential != null && this.textCredential.isUsernameSet() ? this.textCredential.getUsername() : "none");
      var1.append(" password=");
      var1.append(this.textCredential != null && this.textCredential.isPasswordSet() ? "is set" : "is not set");
      var1.append("]");
      return var1.toString();
   }
}
