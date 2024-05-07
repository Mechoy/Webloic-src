package weblogic.security.provider;

import com.bea.common.logger.spi.LoggerSpi;
import java.security.AccessController;
import java.security.Principal;
import weblogic.management.provider.ManagementService;
import weblogic.management.security.RealmMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.shared.LoggerWrapper;
import weblogic.security.spi.PrincipalValidator;

/** @deprecated */
public class PrincipalValidatorImpl implements PrincipalValidator {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static LoggerSpi log = new LoggerAdapter(LoggerWrapper.getInstance("SecurityAtn"));
   private com.bea.common.security.provider.PrincipalValidatorImpl delegate = null;

   public PrincipalValidatorImpl() {
      byte[] var1 = ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurityConfiguration().getCredential().getBytes();
      RealmMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurityConfiguration().getDefaultRealm();
      boolean var3 = var2.isEnableWebLogicPrincipalValidatorCache();
      int var4 = 500;
      if (var3) {
         var4 = var2.getMaxWebLogicPrincipalsInCache();
         if (var4 <= 0) {
            var4 = 500;
         }
      }

      if (log.isDebugEnabled()) {
         log.debug("Initializing principal validator delegate");
      }

      this.delegate = new com.bea.common.security.provider.PrincipalValidatorImpl(log, var1, var3, var4);
   }

   public boolean validate(Principal var1) throws SecurityException {
      return this.delegate.validate(var1);
   }

   public boolean sign(Principal var1) {
      return this.delegate.sign(var1);
   }

   public Class getPrincipalBaseClass() {
      return this.delegate.getPrincipalBaseClass();
   }

   private static class LoggerAdapter implements LoggerSpi {
      private LoggerWrapper logger;

      LoggerAdapter(LoggerWrapper var1) {
         this.logger = var1;
      }

      public boolean isDebugEnabled() {
         return this.logger.isDebugEnabled();
      }

      public void debug(Object var1) {
         this.logger.debug(var1);
      }

      public void debug(Object var1, Throwable var2) {
         this.logger.debug(var1, var2);
      }

      public void info(Object var1) {
         this.logger.info(var1);
      }

      public void info(Object var1, Throwable var2) {
         this.logger.info(var1, var2);
      }

      public void warn(Object var1) {
         this.logger.warn(var1);
      }

      public void warn(Object var1, Throwable var2) {
         this.logger.warn(var1, var2);
      }

      public void error(Object var1) {
         this.logger.error(var1);
      }

      public void error(Object var1, Throwable var2) {
         this.logger.error(var1, var2);
      }

      public void severe(Object var1) {
         this.logger.severe(var1);
      }

      public void severe(Object var1, Throwable var2) {
         this.logger.severe(var1, var2);
      }
   }
}
