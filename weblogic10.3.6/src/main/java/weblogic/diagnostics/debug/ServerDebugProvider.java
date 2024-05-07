package weblogic.diagnostics.debug;

import com.bea.logging.LoggingService;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.AccessController;
import java.util.logging.Logger;
import weblogic.kernel.KernelLogManager;
import weblogic.management.configuration.ServerDebugMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ServerDebugProvider implements DebugProvider {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private DebugScopeTree debugTree = null;
   private ServerDebugMBean myDebugBean = null;

   public String getName() {
      return this.getClass().getName();
   }

   public String getCommandLineOverridePrefix() {
      return "weblogic.debug.";
   }

   public void intializeDebugScopes() throws DebugScopeInitializationException {
      try {
         InputStream var1 = this.getClass().getResourceAsStream("DebugScopeTree.ser");
         ObjectInputStream var2 = new ObjectInputStream(var1);
         this.debugTree = (DebugScopeTree)var2.readObject();
      } catch (Exception var3) {
         throw new DebugScopeInitializationException(var3.getMessage(), var3);
      }
   }

   public DebugScopeTree getDebugScopeTree() throws DebugScopeInitializationException {
      if (this.debugTree == null) {
         this.intializeDebugScopes();
      }

      return this.debugTree;
   }

   public Object getDebugConfiguration() throws DebugBeanConfigurationException {
      if (this.myDebugBean == null) {
         this.myDebugBean = ManagementService.getRuntimeAccess(KERNEL_ID).getServer().getServerDebug();
      }

      return this.myDebugBean;
   }

   public Logger getLogger() {
      Logger var1 = KernelLogManager.getLogger();
      String var2 = var1.getClass().getName();
      return var2.equals("weblogic.logging.log4j.JDKLog4jAdapter") ? var1 : LoggingService.getInstance().getDebugDelegateLogger();
   }
}
