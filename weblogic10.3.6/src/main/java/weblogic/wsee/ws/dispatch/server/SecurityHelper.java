package weblogic.wsee.ws.dispatch.server;

import java.security.AccessController;
import javax.xml.rpc.handler.MessageContext;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

class SecurityHelper {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   protected static void setSubject(MessageContext var0) {
      AuthenticatedSubject var1 = (AuthenticatedSubject)var0.getProperty("weblogic.wsee.wss.subject");
      if (var1 != null) {
         AuthenticatedSubject var2 = switchSubject(var1);
         var0.setProperty("weblogic.wsee.subject", var2);
      }

   }

   protected static void resetSubject(MessageContext var0) {
      AuthenticatedSubject var1 = (AuthenticatedSubject)var0.getProperty("weblogic.wsee.subject");
      if (var1 != null) {
         switchSubject(var1);
         var0.removeProperty("weblogic.wsee.subject");
      }

   }

   private static AuthenticatedSubject switchSubject(AuthenticatedSubject var0) {
      AuthenticatedSubject var1 = SecurityServiceManager.getCurrentSubject(kernelId);
      SecurityServiceManager.popSubject(kernelId);
      SecurityServiceManager.pushSubject(kernelId, var0);
      return var1;
   }
}
