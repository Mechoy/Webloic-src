package weblogic.management.mbeans.custom;

import java.io.File;
import java.security.AccessController;
import weblogic.management.DomainDir;
import weblogic.management.configuration.LogFileMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class LogFile extends ConfigurationMBeanCustomizer {
   private static final long serialVersionUID = 7988658427199784266L;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public LogFile() {
      this((ConfigurationMBeanCustomized)null);
   }

   public LogFile(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public String computeLogFilePath() {
      LogFileMBean var1 = (LogFileMBean)this.getMbean();
      String var2 = var1.getFileName();
      if (var2 == null) {
         var2 = "logs/" + var1.getName() + ".log";
      }

      File var3 = new File(var2);
      if (var3.isAbsolute()) {
         return var3.getAbsolutePath();
      } else {
         String var4 = ManagementService.getPropertyService(kernelId).getServerName();
         return DomainDir.getPathRelativeServerDir(var4, var2);
      }
   }
}
