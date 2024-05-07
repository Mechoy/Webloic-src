package weblogic.management.internal;

import java.beans.MethodDescriptor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.security.AccessController;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.WebLogicObjectName;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.Security;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.Auditor;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.security.spi.AuditSeverity;
import weblogic.utils.StringUtils;

public class ConfigurationAuditor implements PropertyChangeListener {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationEdit");
   private static ConfigurationAuditor instance;
   private static boolean instantiated = false;
   private static boolean initialized = false;
   private Auditor auditor = null;
   private static AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private boolean oldChangeLoggingAttr = Boolean.getBoolean("weblogic.AdministrationMBeanAuditingEnabled");
   private boolean newChangeLoggingAttr = false;
   private boolean changeLoggingEnabled = false;
   private boolean changeAuditingEnabled = false;
   private WebLogicObjectName domainName;
   private static final String OLD_AUDIT_ENABLED_ATTRIBUTE = "AdministrationMBeanAuditingEnabled";
   private static final String NEW_AUDIT_ENABLED_ATTRIBUTE = "ConfigurationAuditType";
   private static String domain;
   private static final String ARRAY_DELIMITOR_FOR_PARAMS_STRING = "; ";
   private static final String PARAMS_TOKENIZER_STRING = ";";

   private ConfigurationAuditor() {
   }

   public void propertyChange(PropertyChangeEvent var1) {
      if (var1.getPropertyName().equals("ConfigurationAuditType")) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Property change for ConfigurationAuditType");
         }

         String var2 = (String)var1.getNewValue();
         boolean var3 = this.changeAuditingEnabled;
         this.setConfiguredAuditing(var2);
         if (var3 && !this.changeAuditingEnabled) {
            this.auditModify(this.domainName.toString(), var1.getPropertyName(), var1.getOldValue(), var2, (Exception)null);
         }

         this.logStatus(true);
      } else if (var1.getPropertyName().equals("AdministrationMBeanAuditingEnabled")) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Property change for AdministrationMBeanAuditingEnabled");
         }

         Boolean var4 = (Boolean)var1.getNewValue();
         this.oldChangeLoggingAttr = var4;
         this.setLoggingEnabled(this.newChangeLoggingAttr || this.oldChangeLoggingAttr);
         this.logStatus(true);
      }

   }

   void setLoggingEnabled(boolean var1) {
      this.changeLoggingEnabled = var1;
   }

   void intialize(DomainMBean var1) {
      if (initialized) {
         throw new AssertionError("The auditor can only be initialized once");
      } else if (ManagementService.getRuntimeAccess(KERNEL_ID).isAdminServer()) {
         initialized = true;
         this.domainName = var1.getObjectName();
         this.oldChangeLoggingAttr = this.oldChangeLoggingAttr || var1.isAdministrationMBeanAuditingEnabled();
         this.setConfiguredAuditing(var1.getConfigurationAuditType());
         this.logStatus(false);
         String var2 = "weblogicDEFAULT";
         this.auditor = (Auditor)SecurityServiceManager.getSecurityService(KERNEL_ID, var2, ServiceType.AUDIT);
         var1.addPropertyChangeListener(this);
      }
   }

   public static final ConfigurationAuditor getInstance() {
      if (!instantiated) {
         Class var0 = ConfigurationAuditor.class;
         synchronized(ConfigurationAuditor.class) {
            if (!instantiated) {
               instance = new ConfigurationAuditor();
            }

            instantiated = true;
         }
      }

      return instance;
   }

   public void create(ObjectName var1, Exception var2) {
      if (this.isAuditable(var1)) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Config auditor create - object name " + var1);
         }

         if (this.changeLoggingEnabled) {
            if (var2 == null) {
               ConfigAuditorLogger.logInfoAuditCreateSuccess(this.getSubjectUser(), var1.toString());
            } else {
               ConfigAuditorLogger.logInfoAuditCreateFailure(this.getSubjectUser(), var1.toString(), var2);
            }
         }

         if (this.changeAuditingEnabled) {
            this.auditCreate(var1.toString(), var2);
         }

      }
   }

   public void remove(ObjectName var1, Exception var2) {
      if (this.isAuditable(var1)) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Config auditor remove - object name " + var1);
         }

         if (this.changeLoggingEnabled) {
            if (var2 == null) {
               ConfigAuditorLogger.logInfoAuditRemoveSuccess(this.getSubjectUser(), var1.toString());
            } else {
               ConfigAuditorLogger.logInfoAuditRemoveFailure(this.getSubjectUser(), var1.toString(), var2);
            }
         }

         if (this.changeAuditingEnabled) {
            this.auditDelete(var1.toString(), var2);
         }

      }
   }

   public void modify(ObjectName var1, Object var2, Attribute var3, PropertyDescriptor var4, Exception var5) {
      if (this.isAuditable(var1)) {
         this.auditJMXAttribute(var1, var2, var3, var4, var5);
      }
   }

   public void modify(ObjectName var1, AttributeList var2, AttributeList var3, PropertyDescriptor[] var4, Exception var5) {
      if (this.isAuditable(var1)) {
         Iterator var6 = var3.iterator();
         Iterator var7 = var2.iterator();

         for(int var8 = 0; var6.hasNext(); ++var8) {
            Attribute var9 = (Attribute)var6.next();
            Attribute var10 = (Attribute)var7.next();
            this.auditJMXAttribute(var1, var10, var9, var4[var8], var5);
         }

      }
   }

   private void auditJMXAttribute(ObjectName var1, Object var2, Attribute var3, PropertyDescriptor var4, Exception var5) {
      String var6 = null;
      String var7 = null;
      String var8 = var3.getName();
      boolean var9 = this.isProtectedAttribute(var1, var8, var4);
      if (var9) {
         var6 = "****";
         var7 = "****";
      } else {
         var6 = this.convertParamsToHumanReadableString(var3);
         var7 = this.convertParamsToHumanReadableString(var2);
      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Config auditor modify - object name " + var1 + " old " + var7 + " new " + var6);
      }

      if (this.changeLoggingEnabled) {
         if (var5 == null) {
            ConfigAuditorLogger.logInfoAuditModifySuccess(this.getSubjectUser(), var1.toString(), var8, var7, var6);
         } else {
            ConfigAuditorLogger.logInfoAuditModifyFailure(this.getSubjectUser(), var1.toString(), var8, var7, var6, var5);
         }
      }

      if (this.changeAuditingEnabled) {
         this.auditModify(var1.toString(), var8, var7, var6, var5);
      }

   }

   public void invoke(ObjectName var1, MethodDescriptor var2, String var3, Object[] var4, Exception var5) {
      if (this.isAuditable(var1)) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Config auditor invoke - object name " + var1 + " action " + var3);
         }

         if (!this.isFilteredMethod(var3)) {
            if (!var3.startsWith("createMBean") && !var3.startsWith("registerMBean")) {
               if (var3.startsWith("create") && var4 != null && var4.length == 1) {
                  try {
                     this.create(this.getObjectNameFromAction(var3, var4[0]), var5);
                     return;
                  } catch (MalformedObjectNameException var8) {
                  }
               }

               if (var3.startsWith("unregisterMBean")) {
                  this.remove(var1, var5);
               } else if (var3.startsWith("destroy") && var4 != null && var4.length == 1 && var4[0] instanceof ObjectName) {
                  this.remove((ObjectName)var4[0], var5);
               } else {
                  String var6 = null;
                  if (var2 != null) {
                     var6 = (String)var2.getValue("wls:auditProtectedArgs");
                  }

                  String var7 = null;
                  if (this.changeLoggingEnabled || this.changeAuditingEnabled) {
                     var7 = this.convertParamsToHumanReadableString(var4);
                     if (var6 != null) {
                        var7 = this.replaceClearTextPasswords(var7, var6);
                     }

                     if (this.changeLoggingEnabled) {
                        if (var5 == null) {
                           ConfigAuditorLogger.logInfoAuditInvokeSuccess(this.getSubjectUser(), var1.toString(), var3, var7);
                        } else {
                           ConfigAuditorLogger.logInfoAuditInvokeFailure(this.getSubjectUser(), var1.toString(), var3, var7, var5);
                        }
                     }

                     if (this.changeAuditingEnabled) {
                        this.auditInvoke(var1.toString(), var3, var7, var5);
                     }
                  }

               }
            } else {
               this.create(var1, var5);
            }
         }
      }
   }

   private boolean isProtectedAttribute(ObjectName var1, String var2, PropertyDescriptor var3) {
      return SecurityHelper.isProtectedAttribute(var1, var2, var3);
   }

   private String convertParamsToHumanReadableString(Object var1) {
      if (var1 == null) {
         return "";
      } else {
         if (var1 instanceof Attribute) {
            Attribute var2 = (Attribute)var1;
            String var3 = var2.getName();
            var1 = var2.getValue();
            if (var1 == null) {
               return "";
            }
         }

         if (var1.getClass().isArray()) {
            StringBuffer var6 = new StringBuffer();
            Object[] var7 = (Object[])((Object[])var1);
            int var4 = 0;

            for(int var5 = var7.length; var4 < var5; ++var4) {
               if (var7[var4] != null) {
                  var6.append(var7[var4].toString());
               }

               if (var4 < var5 - 1) {
                  var6.append("; ");
               }
            }

            return var6.toString();
         } else {
            return var1.toString();
         }
      }
   }

   private String replaceClearTextPasswords(String var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      String[] var4 = StringUtils.split(var2, ',');
      if (var4 != null && var4.length != 0) {
         int[] var5 = new int[var4.length];

         for(int var6 = 0; var6 < var4.length; ++var6) {
            try {
               var5[var6] = Integer.parseInt(var4[var6].trim());
            } catch (NumberFormatException var10) {
               ConfigAuditorLogger.logInvalidNumberReplacingClearText(var4[var6].trim(), var10);
            }
         }

         StringTokenizer var11 = new StringTokenizer(var1, ";");

         for(int var7 = 1; var11.hasMoreTokens(); ++var7) {
            String var8 = var11.nextToken();

            for(int var9 = 0; var9 < var5.length; ++var9) {
               if (var5[var9] == var7) {
                  var8 = " ****";
                  break;
               }
            }

            var3.append(var8);
            if (var11.hasMoreElements()) {
               var3.append(";");
            }
         }

         return var3.toString();
      } else {
         return var1;
      }
   }

   public boolean isAuditable(ObjectName var1) {
      return this.private_isAuditable(var1);
   }

   private boolean private_isAuditable(ObjectName var1) {
      if (!initialized) {
         return false;
      } else if (!this.changeLoggingEnabled && !this.changeAuditingEnabled) {
         return false;
      } else if (var1.getKeyProperty("Type") == null) {
         return true;
      } else {
         return var1.getKeyProperty("Location") == null;
      }
   }

   private boolean isFilteredMethod(String var1) {
      if (var1.startsWith("lookup")) {
         return true;
      } else if (var1.startsWith("find")) {
         return true;
      } else if (var1.startsWith("getMBean")) {
         return true;
      } else if (var1.startsWith("getXml")) {
         return true;
      } else if (var1.equals("preDeregister")) {
         return true;
      } else if (var1.equals("userExists")) {
         return true;
      } else if (var1.equals("groupExists")) {
         return true;
      } else if (var1.equals("advance")) {
         return true;
      } else if (var1.equals("haveCurrent")) {
         return true;
      } else if (var1.equals("close")) {
         return true;
      } else if (var1.equals("saveDomain")) {
         return true;
      } else {
         return var1.endsWith("DescriptorValue");
      }
   }

   private void logStatus(boolean var1) {
      if (!this.changeLoggingEnabled && !this.changeAuditingEnabled) {
         if (var1) {
            ConfigAuditorLogger.logInfoConfigurationAuditingDisabled(this.getSubjectUser());
         }
      } else {
         ConfigAuditorLogger.logInfoConfigurationAuditingEnabled(this.getSubjectUser());
      }

   }

   private String getSubjectUser() {
      String var1 = SubjectUtils.getUsername(Security.getCurrentSubject());
      if (var1 == null) {
         var1 = new String("<UNKNOWN>");
      }

      return var1;
   }

   private void auditCreate(String var1, Exception var2) {
      if (this.auditor != null) {
         AuditCreateConfigurationEventImpl var3;
         if (var2 == null) {
            var3 = new AuditCreateConfigurationEventImpl(AuditSeverity.SUCCESS, SecurityServiceManager.getCurrentSubject(KERNEL_ID), var1);
         } else {
            var3 = new AuditCreateConfigurationEventImpl(AuditSeverity.FAILURE, SecurityServiceManager.getCurrentSubject(KERNEL_ID), var1);
            var3.setFailureException(var2);
         }

         this.auditor.writeEvent(var3);
      }

   }

   private void auditDelete(String var1, Exception var2) {
      if (this.auditor != null) {
         AuditDeleteConfigurationEventImpl var3;
         if (var2 == null) {
            var3 = new AuditDeleteConfigurationEventImpl(AuditSeverity.SUCCESS, SecurityServiceManager.getCurrentSubject(KERNEL_ID), var1);
         } else {
            var3 = new AuditDeleteConfigurationEventImpl(AuditSeverity.FAILURE, SecurityServiceManager.getCurrentSubject(KERNEL_ID), var1);
            var3.setFailureException(var2);
         }

         this.auditor.writeEvent(var3);
      }

   }

   private void auditInvoke(String var1, String var2, String var3, Exception var4) {
      if (this.auditor != null) {
         AuditInvokeConfigurationEventImpl var5;
         if (var4 == null) {
            var5 = new AuditInvokeConfigurationEventImpl(AuditSeverity.SUCCESS, SecurityServiceManager.getCurrentSubject(KERNEL_ID), var1, var2, var3);
         } else {
            var5 = new AuditInvokeConfigurationEventImpl(AuditSeverity.FAILURE, SecurityServiceManager.getCurrentSubject(KERNEL_ID), var1, var2, var3);
            var5.setFailureException(var4);
         }

         this.auditor.writeEvent(var5);
      }

   }

   private void auditModify(String var1, String var2, Object var3, Object var4, Exception var5) {
      if (this.auditor != null) {
         AuditSetAttributeConfigurationEventImpl var6;
         if (var5 == null) {
            var6 = new AuditSetAttributeConfigurationEventImpl(AuditSeverity.SUCCESS, SecurityServiceManager.getCurrentSubject(KERNEL_ID), var1, var2, var3, var4);
         } else {
            var6 = new AuditSetAttributeConfigurationEventImpl(AuditSeverity.FAILURE, SecurityServiceManager.getCurrentSubject(KERNEL_ID), var1, var2, var3, var4);
            var6.setFailureException(var5);
         }

         this.auditor.writeEvent(var6);
      }

   }

   private void setConfiguredAuditing(String var1) {
      if (var1.equalsIgnoreCase("audit")) {
         this.changeAuditingEnabled = true;
         this.newChangeLoggingAttr = false;
      } else if (var1.equalsIgnoreCase("logaudit")) {
         this.changeAuditingEnabled = true;
         this.newChangeLoggingAttr = true;
      } else if (var1.equalsIgnoreCase("log")) {
         this.changeAuditingEnabled = false;
         this.newChangeLoggingAttr = true;
      } else {
         this.changeAuditingEnabled = false;
         this.newChangeLoggingAttr = false;
      }

      this.setLoggingEnabled(this.newChangeLoggingAttr || this.oldChangeLoggingAttr);
   }

   private ObjectName getObjectNameFromAction(String var1, Object var2) throws MalformedObjectNameException {
      String var3 = domain + ":Name=" + var2.toString() + ",Type=" + var1.substring(6);
      return new ObjectName(var3);
   }

   static {
      domain = ManagementService.getRuntimeAccess(KERNEL_ID).getDomainName();
   }
}
