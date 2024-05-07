package weblogic.wsee.security.serviceref;

import java.security.AccessController;
import javax.resource.spi.security.PasswordCredential;
import weblogic.security.UsernameAndPassword;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrivilegedActions;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;

public class ServiceRefUNTCredProv implements CredentialProvider {
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public String[] getValueTypes() {
      return WSSConstants.UNT_VALUETYPES;
   }

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4) {
      if (var4 != null && var4.equals(Purpose.IDENTITY)) {
         Object var5 = ServiceRefUtils.getCredential(kernelID, "weblogic.UserPassword", var2, var3);
         if (var5 == null) {
            return null;
         } else {
            PasswordCredential var6 = (PasswordCredential)var5;
            return new UsernameAndPassword(var6.getUserName(), var6.getPassword());
         }
      } else {
         return null;
      }
   }
}
