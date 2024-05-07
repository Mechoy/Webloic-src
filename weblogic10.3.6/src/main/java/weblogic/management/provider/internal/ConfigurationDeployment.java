package weblogic.management.provider.internal;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.util.List;
import weblogic.deploy.service.Version;
import weblogic.deploy.service.internal.BaseDeploymentImpl;
import weblogic.management.ManagementLogger;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.URLManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ConfigurationDeployment extends BaseDeploymentImpl {
   private static final long serialVersionUID = 4191427923503258738L;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public ConfigurationDeployment() {
   }

   ConfigurationDeployment(String var1) {
      super(var1, var1, (Version)null, (List)null, (List)null, (String)null, (List)null);
   }

   public void addServersToBeRestarted(String[] var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (isServerRunning(var1[var2])) {
               ManagementLogger.logServerNeedsReboot(var1[var2]);
            }

            this.addServerToBeRestarted(var1[var2]);
         }

      }
   }

   private static boolean isServerRunning(String var0) {
      String var1 = ManagementService.getRuntimeAccess(KERNEL_ID).getServerName();
      if (var1.equals(var0)) {
         return true;
      } else {
         try {
            return URLManager.findAdministrationURL(var0) != null;
         } catch (UnknownHostException var3) {
            return false;
         }
      }
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
   }
}
