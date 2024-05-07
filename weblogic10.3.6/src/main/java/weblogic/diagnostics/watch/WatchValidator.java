package weblogic.diagnostics.watch;

import java.security.AccessController;
import java.util.HashSet;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import weblogic.diagnostics.descriptor.WLDFNotificationBean;
import weblogic.diagnostics.descriptor.WLDFWatchBean;
import weblogic.diagnostics.descriptor.WLDFWatchNotificationBean;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.query.QueryFactory;
import weblogic.diagnostics.query.QueryParsingException;
import weblogic.diagnostics.query.VariableIndexResolver;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MailSessionMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class WatchValidator {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static void validateWatch(WLDFWatchBean var0) {
      validateWatchRule(var0.getName(), var0.getRuleType(), var0.getRuleExpression());
   }

   public static void validateWatchRule(String var0, String var1) {
      validateWatchRule((String)null, var0, var1);
   }

   public static void validateWatchRule(String var0, String var1, String var2) {
      try {
         Object var3 = null;
         if (var1.equals("Harvester")) {
            var3 = new HarvesterVariableValidator(var0);
         } else if (var1.equals("Log")) {
            var3 = new FixedSetVariableValidator(3);
         } else if (var1.equals("EventData")) {
            var3 = new FixedSetVariableValidator(1);
         }

         QueryFactory.createQuery((VariableIndexResolver)var3, var2);
      } catch (QueryParsingException var5) {
         String var4 = DiagnosticsLogger.logInvalidWatchRuleExpressionLoggable(var2, var5).getMessage();
         throw new IllegalArgumentException(var4);
      }
   }

   public static void validateRecipients(String[] var0) {
      if (var0 != null) {
         StringBuffer var1 = new StringBuffer();

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1.append(var0[var2]);
            if (var2 < var0.length - 1) {
               var1.append(',');
            }
         }

         try {
            InternetAddress.parse(var1.toString(), false);
         } catch (AddressException var3) {
            throw new IllegalArgumentException(var3);
         }
      }
   }

   public static void validateWatchNotificationBean(WLDFWatchNotificationBean var0) {
      HashSet var1 = new HashSet();
      WLDFNotificationBean[] var2 = var0.getNotifications();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            String var4 = var2[var3].getName();
            if (var1.contains(var4)) {
               String var5 = DiagnosticsLogger.logNotificationNameExistingLoggable(var4).getMessage();
               throw new IllegalArgumentException(var5);
            }

            var1.add(var4);
         }

      }
   }

   private static MailSessionMBean getMailSessionMBean(String var0) {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      DomainMBean var2 = var1.getDomain();
      MailSessionMBean[] var3 = var2.getMailSessions();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         MailSessionMBean var5 = var3[var4];
         String var6 = var5.getJNDIName();
         if (var0.equals(var6)) {
            return var5;
         }
      }

      return null;
   }
}
