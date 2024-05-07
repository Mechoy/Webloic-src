package weblogic.deploy.internal.diagnostics;

import java.security.AccessController;
import java.util.Iterator;
import weblogic.application.utils.XMLWriter;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public abstract class ImageProvider {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static final boolean isAdminServer;
   public boolean timedOut = false;

   public abstract void writeDiagnosticImage(XMLWriter var1);

   public void timeoutImageCreation() {
      this.timedOut = true;
   }

   public void writeCollection(XMLWriter var1, Iterator var2, String var3) {
      StringBuffer var4 = new StringBuffer();

      while(var2.hasNext()) {
         var4.append((String)var2.next());
         if (var2.hasNext()) {
            var4.append(", ");
         }
      }

      var1.addElement(var3, var4.toString());
   }

   static {
      isAdminServer = ManagementService.getRuntimeAccess(KERNEL_ID).isAdminServer();
   }
}
