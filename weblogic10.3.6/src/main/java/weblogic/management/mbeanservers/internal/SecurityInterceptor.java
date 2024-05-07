package weblogic.management.mbeanservers.internal;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.security.AccessController;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import weblogic.common.internal.VersionInfo;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.logging.Loggable;
import weblogic.management.NoAccessRuntimeException;
import weblogic.management.WebLogicObjectName;
import weblogic.management.commo.StandardInterface;
import weblogic.management.internal.ConfigurationAuditor;
import weblogic.management.internal.SecurityHelper;
import weblogic.management.jmx.JMXLogger;
import weblogic.management.jmx.mbeanserver.WLSMBeanServer;
import weblogic.management.jmx.mbeanserver.WLSMBeanServerInterceptorBase;
import weblogic.management.jmx.modelmbean.WLSModelMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.beaninfo.BeanInfoAccess;
import weblogic.management.security.RealmMBean;
import weblogic.management.security.audit.AuditorMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.ConsumptionException;
import weblogic.security.service.JMXPolicyConsumer;
import weblogic.security.service.JMXPolicyHandler;
import weblogic.security.service.JMXResource;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.MBeanResource.ActionType;
import weblogic.security.service.SecurityService.ServiceType;

public class SecurityInterceptor extends WLSMBeanServerInterceptorBase {
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXCore");
   WLSMBeanServer wlsMBeanServer;
   private boolean useSecurityFramework;
   private boolean auditorsConfigured;
   private AuthorizationManager authorizer;
   private JMXPolicyConsumer policyConsumer;
   static boolean registeredPolicies;
   private static Map securityInterceptors = Collections.synchronizedMap(new HashMap());
   private static final String[] APP_SCOPED_TYPES = new String[]{"ApplicationRuntime", "JDBCSystemResource", "JMSSystemResource", "WLDFSystemResource", "CustomResource"};
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static String BEA_DOMAIN = "com.bea";

   public SecurityInterceptor(WLSMBeanServer var1) {
      this(var1, (String)null);
   }

   public SecurityInterceptor(WLSMBeanServer var1, String var2) {
      this.wlsMBeanServer = var1;
      RealmMBean var3 = ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurityConfiguration().getDefaultRealm();
      this.useSecurityFramework = var3.isDelegateMBeanAuthorization();
      AuditorMBean[] var4 = var3.getAuditors();
      if (this.useSecurityFramework && var4 != null && var4.length > 0) {
         this.auditorsConfigured = true;
      }

      this.authorizer = (AuthorizationManager)SecurityServiceManager.getSecurityService(kernelId, "weblogicDEFAULT", ServiceType.AUTHORIZE);
      this.policyConsumer = SecurityServiceManager.getJMXPolicyConsumer(kernelId);
      if (var2 != null) {
         securityInterceptors.put(var2, this);
      }

   }

   public ObjectInstance createMBean(String var1, ObjectName var2) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
      try {
         this.checkForBEADomain(var2);
         this.checkCreateSecurity(var1, var2);
         ObjectInstance var3 = super.createMBean(var1, var2);
         ConfigurationAuditor.getInstance().create(var2, (Exception)null);
         return var3;
      } catch (NoAccessRuntimeException var4) {
         ConfigurationAuditor.getInstance().create(var2, var4);
         throw var4;
      } catch (ReflectionException var5) {
         ConfigurationAuditor.getInstance().create(var2, var5);
         throw var5;
      } catch (InstanceAlreadyExistsException var6) {
         ConfigurationAuditor.getInstance().create(var2, var6);
         throw var6;
      } catch (MBeanRegistrationException var7) {
         ConfigurationAuditor.getInstance().create(var2, var7);
         throw var7;
      } catch (MBeanException var8) {
         ConfigurationAuditor.getInstance().create(var2, var8);
         throw var8;
      } catch (NotCompliantMBeanException var9) {
         ConfigurationAuditor.getInstance().create(var2, var9);
         throw var9;
      } catch (IOException var10) {
         ConfigurationAuditor.getInstance().create(var2, var10);
         throw var10;
      }
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
      try {
         this.checkForBEADomain(var2);
         this.checkCreateSecurity(var1, var2, var3);
         ObjectInstance var4 = super.createMBean(var1, var2, var3);
         ConfigurationAuditor.getInstance().create(var2, (Exception)null);
         return var4;
      } catch (NoAccessRuntimeException var5) {
         ConfigurationAuditor.getInstance().create(var2, var5);
         throw var5;
      } catch (ReflectionException var6) {
         ConfigurationAuditor.getInstance().create(var2, var6);
         throw var6;
      } catch (InstanceAlreadyExistsException var7) {
         ConfigurationAuditor.getInstance().create(var2, var7);
         throw var7;
      } catch (MBeanRegistrationException var8) {
         ConfigurationAuditor.getInstance().create(var2, var8);
         throw var8;
      } catch (MBeanException var9) {
         ConfigurationAuditor.getInstance().create(var2, var9);
         throw var9;
      } catch (NotCompliantMBeanException var10) {
         ConfigurationAuditor.getInstance().create(var2, var10);
         throw var10;
      } catch (InstanceNotFoundException var11) {
         ConfigurationAuditor.getInstance().create(var2, var11);
         throw var11;
      } catch (IOException var12) {
         ConfigurationAuditor.getInstance().create(var2, var12);
         throw var12;
      }
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, Object[] var3, String[] var4) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
      try {
         this.checkForBEADomain(var2);
         this.checkCreateSecurity(var1, var2, var3, var4);
         ObjectInstance var5 = super.createMBean(var1, var2, var3, var4);
         ConfigurationAuditor.getInstance().create(var2, (Exception)null);
         return var5;
      } catch (NoAccessRuntimeException var6) {
         ConfigurationAuditor.getInstance().create(var2, var6);
         throw var6;
      } catch (ReflectionException var7) {
         ConfigurationAuditor.getInstance().create(var2, var7);
         throw var7;
      } catch (InstanceAlreadyExistsException var8) {
         ConfigurationAuditor.getInstance().create(var2, var8);
         throw var8;
      } catch (MBeanRegistrationException var9) {
         ConfigurationAuditor.getInstance().create(var2, var9);
         throw var9;
      } catch (MBeanException var10) {
         ConfigurationAuditor.getInstance().create(var2, var10);
         throw var10;
      } catch (NotCompliantMBeanException var11) {
         ConfigurationAuditor.getInstance().create(var2, var11);
         throw var11;
      } catch (IOException var12) {
         ConfigurationAuditor.getInstance().create(var2, var12);
         throw var12;
      }
   }

   public ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3, Object[] var4, String[] var5) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
      try {
         this.checkForBEADomain(var2);
         this.checkCreateSecurity(var1, var2, var3, var4, var5);
         ObjectInstance var6 = super.createMBean(var1, var2, var3, var4, var5);
         ConfigurationAuditor.getInstance().create(var2, (Exception)null);
         return var6;
      } catch (NoAccessRuntimeException var7) {
         ConfigurationAuditor.getInstance().create(var2, var7);
         throw var7;
      } catch (ReflectionException var8) {
         ConfigurationAuditor.getInstance().create(var2, var8);
         throw var8;
      } catch (InstanceAlreadyExistsException var9) {
         ConfigurationAuditor.getInstance().create(var2, var9);
         throw var9;
      } catch (MBeanRegistrationException var10) {
         ConfigurationAuditor.getInstance().create(var2, var10);
         throw var10;
      } catch (MBeanException var11) {
         ConfigurationAuditor.getInstance().create(var2, var11);
         throw var11;
      } catch (NotCompliantMBeanException var12) {
         ConfigurationAuditor.getInstance().create(var2, var12);
         throw var12;
      } catch (InstanceNotFoundException var13) {
         ConfigurationAuditor.getInstance().create(var2, var13);
         throw var13;
      } catch (IOException var14) {
         ConfigurationAuditor.getInstance().create(var2, var14);
         throw var14;
      }
   }

   public Object getAttribute(ObjectName var1, String var2) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException {
      this.checkGetSecurity(var1, var2);
      Object var3 = super.getAttribute(var1, var2);
      return var3;
   }

   public AttributeList getAttributes(ObjectName var1, String[] var2) throws InstanceNotFoundException, ReflectionException, IOException {
      this.checkGetSecurity(var1, var2);
      return super.getAttributes(var1, var2);
   }

   public void unregisterMBean(ObjectName var1) throws InstanceNotFoundException, MBeanRegistrationException, IOException {
      try {
         if (this.isWLSMBean(var1)) {
            Loggable var2 = JMXLogger.logWLSMBeanUnregisterFailedLoggable(var1.getCanonicalName());
            throw new NoAccessRuntimeException(var2.getMessage());
         } else {
            this.checkUnregisterSecurity(var1);
            super.unregisterMBean(var1);
            ConfigurationAuditor.getInstance().remove(var1, (Exception)null);
         }
      } catch (NoAccessRuntimeException var3) {
         ConfigurationAuditor.getInstance().remove(var1, var3);
         throw var3;
      } catch (InstanceNotFoundException var4) {
         ConfigurationAuditor.getInstance().remove(var1, var4);
         throw var4;
      } catch (MBeanRegistrationException var5) {
         ConfigurationAuditor.getInstance().remove(var1, var5);
         throw var5;
      } catch (IOException var6) {
         ConfigurationAuditor.getInstance().remove(var1, var6);
         throw var6;
      }
   }

   public void setAttribute(ObjectName var1, Attribute var2) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
      Object var3 = null;
      PropertyDescriptor var4 = null;

      try {
         if (ConfigurationAuditor.getInstance().isAuditable(var1) || this.auditorsConfigured) {
            var4 = this.getPropertyDescriptor(var1, var2.getName());
            var3 = super.getAttribute(var1, var2.getName());
         }
      } catch (Exception var13) {
         System.out.println("Exception caught while performing getAttribute for setAttribute " + var13);
      }

      try {
         this.checkSetSecurity(var1, var2, var3);
         super.setAttribute(var1, var2);
         ConfigurationAuditor.getInstance().modify(var1, (Object)var3, (Attribute)var2, (PropertyDescriptor)var4, (Exception)null);
      } catch (NoAccessRuntimeException var6) {
         ConfigurationAuditor.getInstance().modify(var1, (Object)var3, (Attribute)var2, (PropertyDescriptor)var4, var6);
         throw var6;
      } catch (InstanceNotFoundException var7) {
         ConfigurationAuditor.getInstance().modify(var1, (Object)var3, (Attribute)var2, (PropertyDescriptor)var4, var7);
         throw var7;
      } catch (AttributeNotFoundException var8) {
         ConfigurationAuditor.getInstance().modify(var1, (Object)var3, (Attribute)var2, (PropertyDescriptor)var4, var8);
         throw var8;
      } catch (InvalidAttributeValueException var9) {
         ConfigurationAuditor.getInstance().modify(var1, (Object)var3, (Attribute)var2, (PropertyDescriptor)var4, var9);
         throw var9;
      } catch (MBeanException var10) {
         ConfigurationAuditor.getInstance().modify(var1, (Object)var3, (Attribute)var2, (PropertyDescriptor)var4, var10);
         throw var10;
      } catch (ReflectionException var11) {
         ConfigurationAuditor.getInstance().modify(var1, (Object)var3, (Attribute)var2, (PropertyDescriptor)var4, var11);
         throw var11;
      } catch (IOException var12) {
         ConfigurationAuditor.getInstance().modify(var1, (Object)var3, (Attribute)var2, (PropertyDescriptor)var4, var12);
         throw var12;
      }
   }

   public AttributeList setAttributes(ObjectName var1, AttributeList var2) throws InstanceNotFoundException, ReflectionException, IOException {
      AttributeList var3 = null;
      PropertyDescriptor[] var4 = null;
      if (ConfigurationAuditor.getInstance().isAuditable(var1) || this.auditorsConfigured) {
         try {
            Iterator var5 = var2.iterator();
            var4 = new PropertyDescriptor[var2.size()];
            String[] var6 = new String[var2.size()];

            for(int var7 = 0; var7 < var2.size(); ++var7) {
               Object var8 = var5.next();
               if (!(var8 instanceof Attribute)) {
                  throw new RuntimeException("AttributeList must contain instances of Attribute");
               }

               Attribute var9 = (Attribute)var8;
               var6[var7] = new String(var9.getName());
               var4[var7] = this.getPropertyDescriptor(var1, var9.getName());
            }

            var3 = super.getAttributes(var1, var6);
         } catch (Exception var14) {
         }
      }

      try {
         this.checkSetSecurity(var1, var2);
         AttributeList var15 = super.setAttributes(var1, var2);
         ConfigurationAuditor.getInstance().modify(var1, (AttributeList)var3, (AttributeList)var2, (PropertyDescriptor[])var4, (Exception)null);
         return var15;
      } catch (NoAccessRuntimeException var10) {
         ConfigurationAuditor.getInstance().modify(var1, (AttributeList)var3, (AttributeList)var2, (PropertyDescriptor[])var4, var10);
         throw var10;
      } catch (InstanceNotFoundException var11) {
         ConfigurationAuditor.getInstance().modify(var1, (AttributeList)var3, (AttributeList)var2, (PropertyDescriptor[])var4, var11);
         throw var11;
      } catch (ReflectionException var12) {
         ConfigurationAuditor.getInstance().modify(var1, (AttributeList)var3, (AttributeList)var2, (PropertyDescriptor[])var4, var12);
         throw var12;
      } catch (IOException var13) {
         ConfigurationAuditor.getInstance().modify(var1, (AttributeList)var3, (AttributeList)var2, (PropertyDescriptor[])var4, var13);
         throw var13;
      }
   }

   public Object invoke(ObjectName var1, String var2, Object[] var3, String[] var4) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
      Object var5 = null;
      MethodDescriptor var6 = this.getMethodDescriptor(var1, var2, var4);

      try {
         this.checkInvokeSecurity(var1, var2, var3, var4, var6);
         var5 = super.invoke(var1, var2, var3, var4);
         ConfigurationAuditor.getInstance().invoke(var1, var6, var2, var3, (Exception)null);
         return var5;
      } catch (NoAccessRuntimeException var8) {
         ConfigurationAuditor.getInstance().invoke(var1, var6, var2, var3, var8);
         throw var8;
      } catch (InstanceNotFoundException var9) {
         ConfigurationAuditor.getInstance().invoke(var1, var6, var2, var3, var9);
         throw var9;
      } catch (MBeanException var10) {
         ConfigurationAuditor.getInstance().invoke(var1, var6, var2, var3, var10);
         throw var10;
      } catch (ReflectionException var11) {
         ConfigurationAuditor.getInstance().invoke(var1, var6, var2, var3, var11);
         throw var11;
      } catch (IOException var12) {
         ConfigurationAuditor.getInstance().invoke(var1, var6, var2, var3, var12);
         throw var12;
      }
   }

   public static boolean isGetAccessAllowed(String var0, ObjectName var1, String var2) throws AttributeNotFoundException, InstanceNotFoundException {
      SecurityInterceptor var3 = (SecurityInterceptor)securityInterceptors.get(var0);
      if (var3 == null) {
         throw new InstanceNotFoundException("MBeanServer " + var0 + "does not exist");
      } else {
         try {
            var3.checkGetSecurity(var1, var2);
            return true;
         } catch (NoAccessRuntimeException var5) {
            return false;
         }
      }
   }

   private void checkCreateSecurity(String var1, ObjectName var2) {
      this.initDefaultPolicies();
      if (var2 != null) {
         if (this.useSecurityFramework) {
            this.isAccessAllowed(var2, "create", (String)null);
         } else if (!this.isWLSMBean(var2) && this.isCommoMBean(var2)) {
            SecurityHelper.isAccessAllowedCommo(var2, ActionType.REGISTER, (String)null, "createMBean", this.getBeanDescriptor(var2));
         } else {
            SecurityHelper.isAccessAllowed(var2, ActionType.REGISTER, (String)null, "createMBean", this.getBeanDescriptor(var2));
         }

      }
   }

   private void checkCreateSecurity(String var1, ObjectName var2, ObjectName var3) {
      this.initDefaultPolicies();
      if (var2 != null) {
         if (this.useSecurityFramework) {
            this.isAccessAllowed(var2, "create", (String)null);
         } else if (!this.isWLSMBean(var2) && this.isCommoMBean(var2)) {
            SecurityHelper.isAccessAllowedCommo(var2, ActionType.REGISTER, (String)null, "createMBean", this.getBeanDescriptor(var2));
         } else {
            SecurityHelper.isAccessAllowed(var2, ActionType.REGISTER, (String)null, "createMBean", this.getBeanDescriptor(var2));
         }

      }
   }

   private void checkCreateSecurity(String var1, ObjectName var2, Object[] var3, String[] var4) {
      this.initDefaultPolicies();
      if (this.useSecurityFramework) {
         this.isAccessAllowed(var2, "create", (String)null);
      } else if (!this.isWLSMBean(var2) && this.isCommoMBean(var2)) {
         SecurityHelper.isAccessAllowedCommo(var2, ActionType.REGISTER, (String)null, "createMBean", this.getBeanDescriptor(var2));
      } else {
         SecurityHelper.isAccessAllowed(var2, ActionType.REGISTER, (String)null, "createMBean", this.getBeanDescriptor(var2));
      }

   }

   private void checkCreateSecurity(String var1, ObjectName var2, ObjectName var3, Object[] var4, String[] var5) {
      this.initDefaultPolicies();
      if (this.useSecurityFramework) {
         this.isAccessAllowed(var2, "create", (String)null);
      } else if (!this.isWLSMBean(var2) && this.isCommoMBean(var2)) {
         SecurityHelper.isAccessAllowedCommo(var2, ActionType.REGISTER, (String)null, "createMBean", this.getBeanDescriptor(var2));
      } else {
         SecurityHelper.isAccessAllowed(var2, ActionType.REGISTER, (String)null, "createMBean", this.getBeanDescriptor(var2));
      }

   }

   private void checkGetSecurity(ObjectName var1, String var2) throws AttributeNotFoundException {
      this.initDefaultPolicies();
      PropertyDescriptor var3 = this.getPropertyDescriptor(var1, var2);
      Boolean var4 = null;
      Boolean var5 = null;
      if (var3 != null) {
         var4 = (Boolean)var3.getValue("encrypted");
         var5 = (Boolean)var3.getValue("sensitive");
      }

      if (var4 != null && var4 && !var2.endsWith("Encrypted")) {
         String var7 = System.getProperty("weblogic.management.clearTextCredentialAccessEnabled");
         boolean var6;
         if (var7 != null && var7.length() > 0) {
            var6 = Boolean.parseBoolean(var7);
         } else {
            var6 = ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurityConfiguration().isClearTextCredentialAccessEnabled();
         }

         if (!var6) {
            throw new NoAccessRuntimeException("Access to sensitive attribute in clear text is not allowed due to the setting of ClearTextCredentialAccessEnabled attribute in SecurityConfigurationMBean. Attr: " + var2 + ", MBean name: " + var1);
         }
      }

      if (this.useSecurityFramework) {
         String var8 = "get";
         if (var4 != null && var4 || var5 != null && var5) {
            var8 = "getEncrypted";
         }

         this.isAccessAllowed(var1, var8, var2);
      } else if (!this.isWLSMBean(var1) && this.isCommoMBean(var1)) {
         SecurityHelper.isAccessAllowedCommo(var1, ActionType.READ, var2, "getAttribute", this.getBeanDescriptor(var1), this.getPropertyDescriptor(var1, var2));
      } else {
         SecurityHelper.isAccessAllowed(var1, ActionType.READ, var2, "getAttribute", this.getBeanDescriptor(var1), this.getPropertyDescriptor(var1, var2));
      }

   }

   private void checkGetSecurity(ObjectName var1, String[] var2) {
      this.initDefaultPolicies();
      int var3;
      if (this.useSecurityFramework) {
         for(var3 = 0; var2 != null && var3 < var2.length; ++var3) {
            try {
               this.checkGetSecurity(var1, var2[var3]);
            } catch (AttributeNotFoundException var7) {
            }
         }
      } else if (!this.isWLSMBean(var1) && this.isCommoMBean(var1)) {
         for(var3 = 0; var2 != null && var3 < var2.length; ++var3) {
            try {
               SecurityHelper.isAccessAllowedCommo(var1, ActionType.READ, var2[var3], "getAttributes", this.getBeanDescriptor(var1), this.getPropertyDescriptor(var1, var2[var3]));
            } catch (AttributeNotFoundException var5) {
            }
         }
      } else {
         for(var3 = 0; var2 != null && var3 < var2.length; ++var3) {
            try {
               SecurityHelper.isAccessAllowed(var1, ActionType.READ, var2[var3], "getAttributes", this.getBeanDescriptor(var1), this.getPropertyDescriptor(var1, var2[var3]));
            } catch (AttributeNotFoundException var6) {
            }
         }
      }

   }

   private void checkUnregisterSecurity(ObjectName var1) {
      this.initDefaultPolicies();
      if (this.useSecurityFramework) {
         this.isAccessAllowed(var1, "unregister", (String)null);
      } else if (!this.isWLSMBean(var1) && this.isCommoMBean(var1)) {
         SecurityHelper.isAccessAllowedCommo(var1, ActionType.UNREGISTER, (String)null, "unregisterMBean", this.getBeanDescriptor(var1));
      } else {
         SecurityHelper.isAccessAllowed(var1, ActionType.UNREGISTER, (String)null, "unregisterMBean", this.getBeanDescriptor(var1));
      }

   }

   private void checkSetSecurity(ObjectName var1, Attribute var2, Object var3) throws AttributeNotFoundException {
      this.initDefaultPolicies();
      String var4 = var2.getName();
      if (this.useSecurityFramework) {
         PropertyDescriptor var5 = this.getPropertyDescriptor(var1, var4);
         String var6 = "set";
         Boolean var7 = null;
         Boolean var8 = null;
         String[] var9 = new String[1];
         Object[] var10 = new Object[]{var2.getValue()};
         var9[0] = "java.lang.Object";
         String var11 = null;
         if (var5 != null) {
            var7 = (Boolean)var5.getValue("encrypted");
            var8 = (Boolean)var5.getValue("sensitive");
            var9[0] = var5.getPropertyType().getName();
         }

         if (var7 != null && var7 || var8 != null && var8) {
            var6 = "setEncrypted";
            var11 = "1";
         }

         this.isAccessAllowedInvoke(var1, var6, var4, var10, var9, var11, var3);
      } else if (!this.isWLSMBean(var1) && this.isCommoMBean(var1)) {
         SecurityHelper.isAccessAllowedCommo(var1, ActionType.WRITE, var4, "setAttribute", this.getBeanDescriptor(var1), this.getPropertyDescriptor(var1, var4));
      } else {
         SecurityHelper.isAccessAllowed(var1, ActionType.WRITE, var4, "setAttribute", this.getBeanDescriptor(var1), this.getPropertyDescriptor(var1, var4));
      }

   }

   private void checkSetSecurity(ObjectName var1, AttributeList var2) {
      this.initDefaultPolicies();
      boolean var3 = true;
      if (this.isWLSMBean(var1) || !this.isCommoMBean(var1)) {
         var3 = false;
      }

      synchronized(var2) {
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            try {
               Object var6 = var5.next();
               if (!(var6 instanceof Attribute)) {
                  throw new RuntimeException("AttributeList must contain instances of Attribute");
               }

               Attribute var7 = (Attribute)var6;
               if (this.useSecurityFramework) {
                  PropertyDescriptor var8 = this.getPropertyDescriptor(var1, var7.getName());
                  String var9 = "set";
                  Boolean var10 = null;
                  Boolean var11 = null;
                  if (var8 != null) {
                     var10 = (Boolean)var8.getValue("encrypted");
                     var11 = (Boolean)var8.getValue("sensitive");
                  }

                  if (var10 != null && var10 || var11 != null && var11) {
                     var9 = "setEncrypted";
                  }

                  this.isAccessAllowed(var1, var9, var7.getName());
               } else if (!var3) {
                  SecurityHelper.isAccessAllowed(var1, ActionType.WRITE, var7.getName(), "setAttributes", this.getBeanDescriptor(var1), this.getPropertyDescriptor(var1, var7.getName()));
               } else {
                  SecurityHelper.isAccessAllowedCommo(var1, ActionType.WRITE, var7.getName(), "setAttributes", this.getBeanDescriptor(var1), this.getPropertyDescriptor(var1, var7.getName()));
               }
            } catch (AttributeNotFoundException var13) {
               var5.remove();
            } catch (NoAccessRuntimeException var14) {
               var5.remove();
            }
         }

      }
   }

   private void checkInvokeSecurity(ObjectName var1, String var2, Object[] var3, String[] var4, MethodDescriptor var5) {
      this.initDefaultPolicies();
      Object var6 = this.wlsMBeanServer.lookupObject(var1);
      String var7 = null;
      if (var6 != null && var6 instanceof WLSModelMBean) {
         WLSModelMBean var8 = (WLSModelMBean)var6;
         var7 = var8.getRole(var2, var3, var4);
      } else if (var5 != null) {
         var7 = (String)var5.getValue("role");
      }

      String var9;
      if (var7 != null && var7.equals("finder")) {
         if (this.useSecurityFramework) {
            var9 = null;
            if (var5 != null) {
               var9 = (String)var5.getValue("wls:auditProtectedArgs");
            }

            this.isAccessAllowedInvoke(var1, "find", var2, var3, var4, var9, (Object)null);
         } else if (!this.isWLSMBean(var1) && this.isCommoMBean(var1)) {
            SecurityHelper.isAccessAllowedCommo(var1, ActionType.FIND, var2, "invoke", this.getBeanDescriptor(var1), var5);
         } else {
            SecurityHelper.isAccessAllowed(var1, ActionType.FIND, var2, "invoke", this.getBeanDescriptor(var1), var5);
         }

      } else {
         if (this.useSecurityFramework) {
            var9 = null;
            if (var5 != null) {
               var9 = (String)var5.getValue("wls:auditProtectedArgs");
            }

            this.isAccessAllowedInvoke(var1, "invoke", var2, var3, var4, var9, (Object)null);
         } else if (!this.isWLSMBean(var1) && this.isCommoMBean(var1)) {
            SecurityHelper.isAccessAllowedCommo(var1, ActionType.EXECUTE, var2, "invoke", this.getBeanDescriptor(var1), var5);
         } else {
            SecurityHelper.isAccessAllowed(var1, ActionType.EXECUTE, var2, "invoke", this.getBeanDescriptor(var1), var5);
         }

      }
   }

   private boolean isCommoMBean(ObjectName var1) {
      Object var2 = this.wlsMBeanServer.lookupObject(var1);
      if (var2 != null && var2 instanceof WLSModelMBean) {
         WLSModelMBean var3 = (WLSModelMBean)var2;
         return StandardInterface.class.isAssignableFrom(var3.getManagedResourceClass());
      } else {
         return false;
      }
   }

   private boolean isWLSMBean(ObjectName var1) {
      if (var1 == null) {
         return false;
      } else if (var1 instanceof WebLogicObjectName) {
         return true;
      } else {
         return WLSObjectNameManager.isBEADomain(var1.getDomain());
      }
   }

   private BeanDescriptor getBeanDescriptor(ObjectName var1) {
      if (var1 == null) {
         return null;
      } else {
         Object var2 = this.wlsMBeanServer.lookupObject(var1);
         if (var2 != null && var2 instanceof WLSModelMBean) {
            WLSModelMBean var3 = (WLSModelMBean)var2;
            BeanInfo var4 = var3.getBeanInfo();
            if (var4 != null) {
               return var4.getBeanDescriptor();
            }
         }

         String var5 = var1.getKeyProperty("Type");
         return var5 != null ? SecurityHelper.getBeanDescriptor(var5) : null;
      }
   }

   private PropertyDescriptor getPropertyDescriptor(ObjectName var1, String var2) throws AttributeNotFoundException {
      if (var1 == null) {
         return null;
      } else {
         Object var3 = this.wlsMBeanServer.lookupObject(var1);
         if (var3 != null && var3 instanceof WLSModelMBean) {
            WLSModelMBean var5 = (WLSModelMBean)var3;
            return var5.getPropertyDescriptorForAttribute(var2);
         } else {
            String var4 = var1.getKeyProperty("Type");
            return var4 != null ? SecurityHelper.getPropertyDescriptor(var4, var2) : null;
         }
      }
   }

   private MethodDescriptor getMethodDescriptor(ObjectName var1, String var2, String[] var3) {
      Object var4 = this.wlsMBeanServer.lookupObject(var1);
      if (var4 != null && var4 instanceof WLSModelMBean) {
         WLSModelMBean var6 = (WLSModelMBean)var4;
         return var6.getMethodDescriptor(var2, var3);
      } else {
         String var5 = var1.getKeyProperty("Type");
         return var5 != null ? SecurityHelper.getMethodDescriptor(var5, var2) : null;
      }
   }

   private void initDefaultPolicies() {
      if (!registeredPolicies) {
         try {
            this.registerDefaultPolicies();
         } catch (ConsumptionException var2) {
            throw new RuntimeException(var2);
         }
      }

   }

   private synchronized void registerDefaultPolicies() throws ConsumptionException {
      registeredPolicies = true;
      if (this.useSecurityFramework) {
         String var1 = "WLSDefaultJMXResourcePolicies";
         Calendar var2 = Calendar.getInstance();
         var2.clear();
         var2.set(2005, 7, 11, 0, 0, 0);
         String var3 = "" + var2.getTime().getTime();
         String var4 = this.formatTimestamp(var3);
         String var5 = VersionInfo.theOne().getReleaseVersion();
         long var6 = 0L;
         if (debug.isDebugEnabled()) {
            debug.debug("Start registration of default JMX Resource policies.");
            var6 = System.currentTimeMillis();
         }

         boolean var8 = true;
         JMXPolicyHandler var9 = this.policyConsumer.getJMXPolicyHandler(var1, var5, var4);
         if (var9 != null) {
            this.setUncheckedPolicy(var9, new JMXResource("get", (String)null, (String)null, (String)null));
            this.setUncheckedPolicy(var9, new JMXResource("find", (String)null, (String)null, (String)null));
            this.setPolicy(var9, new JMXResource((String)null, (String)null, (String)null, (String)null), new String[0], (String[])null);
            var9.done();
         }

         BeanInfoAccess var10 = ManagementService.getBeanInfoAccess();
         String[] var11 = var10.getBeanInfoFactoryNames();

         for(int var12 = 0; var11 != null && var12 < var11.length; ++var12) {
            String var13 = var11[var12];
            String[] var14 = var10.getInterfacesWithRoleInfo(var13);
            if (var14 != null && var14.length != 0) {
               var3 = var10.getRoleInfoImplementationFactoryTimestamp(var13);
               var4 = this.formatTimestamp(var3);
               var9 = this.policyConsumer.getJMXPolicyHandler(var13, var5, var4);
               if (var9 != null) {
                  for(int var15 = 0; var15 < var14.length; ++var15) {
                     BeanInfo var16 = var10.getBeanInfoForInterface(var14[var15], true, var5);
                     if (var16 == null) {
                        if (debug.isDebugEnabled()) {
                           debug.debug("Beaninfo for interface is null - interface is " + var14[var15]);
                        }
                     } else {
                        BeanDescriptor var17 = var16.getBeanDescriptor();
                        Boolean var18 = (Boolean)var17.getValue("rolePermitAll");
                        if (var18 != null && var18) {
                           this.setUncheckedPolicy(var9, new JMXResource("set", (String)null, var14[var15], (String)null));
                           this.setUncheckedPolicy(var9, new JMXResource("invoke", (String)null, var14[var15], (String)null));
                           this.setUncheckedPolicy(var9, new JMXResource("create", (String)null, var14[var15], (String)null));
                           this.setUncheckedPolicy(var9, new JMXResource("unregister", (String)null, var14[var15], (String)null));
                        }

                        String[] var19 = (String[])((String[])var17.getValue("rolesAllowed"));
                        if (var19 != null) {
                           this.setPolicy(var9, new JMXResource("set", (String)null, var14[var15], (String)null), var19, (String[])null);
                           this.setPolicy(var9, new JMXResource("invoke", (String)null, var14[var15], (String)null), var19, (String[])null);
                           this.setPolicy(var9, new JMXResource("create", (String)null, var14[var15], (String)null), var19, (String[])null);
                           this.setPolicy(var9, new JMXResource("unregister", (String)null, var14[var15], (String)null), var19, (String[])null);
                        }

                        MethodDescriptor[] var20 = var16.getMethodDescriptors();

                        for(int var21 = 0; var20 != null && var21 < var20.length; ++var21) {
                           MethodDescriptor var22 = var20[var21];
                           var18 = (Boolean)var22.getValue("rolePermitAll");
                           if (var18 != null && var18) {
                              this.setUncheckedPolicy(var9, new JMXResource("invoke", (String)null, var14[var15], var22.getName()));
                           }

                           String[] var23 = (String[])((String[])var22.getValue("rolesAllowed"));
                           if (var23 != null) {
                              this.setPolicy(var9, new JMXResource("invoke", (String)null, var14[var15], var22.getName()), var19, var23);
                           }
                        }

                        PropertyDescriptor[] var30 = var16.getPropertyDescriptors();

                        for(int var31 = 0; var30 != null && var31 < var30.length; ++var31) {
                           PropertyDescriptor var32 = var30[var31];
                           String var24 = "get";
                           String var25 = "set";
                           Boolean var26 = (Boolean)var32.getValue("encrypted");
                           Boolean var27 = (Boolean)var32.getValue("sensitive");
                           if (var26 != null && var26 || var27 != null && var27) {
                              var24 = "getEncrypted";
                              var25 = "setEncrypted";
                           }

                           var18 = (Boolean)var32.getValue("rolePermitAllGet");
                           if (var18 != null && var18) {
                              this.setUncheckedPolicy(var9, new JMXResource(var24, (String)null, var14[var15], var32.getName()));
                           }

                           String[] var28 = (String[])((String[])var32.getValue("rolesAllowedGet"));
                           if (var28 != null) {
                              this.setPolicy(var9, new JMXResource(var24, (String)null, var14[var15], var32.getName()), var19, var28);
                           }

                           var18 = (Boolean)var32.getValue("rolePermitAllSet");
                           if (var18 != null && var18) {
                              this.setUncheckedPolicy(var9, new JMXResource(var25, (String)null, var14[var15], var32.getName()));
                           }

                           var28 = (String[])((String[])var32.getValue("rolesAllowedSet"));
                           if (var28 != null) {
                              this.setPolicy(var9, new JMXResource(var25, (String)null, var14[var15], var32.getName()), var19, var28);
                           }
                        }
                     }
                  }

                  var9.done();
               }
            }
         }

         if (debug.isDebugEnabled()) {
            long var29 = System.currentTimeMillis();
            debug.debug("End registration of default JMX Resource policies. Elasped time is " + (var29 - var6));
         }

      }
   }

   private void isAccessAllowed(ObjectName var1, String var2, String var3) throws NoAccessRuntimeException {
      if (this.isWLSMBean(var1) || this.isCommoMBean(var1) || var1 == null || var1.getKeyProperty("Type") != null) {
         String var4 = this.getBeanType(var1);
         String var5 = this.getAppName(var1);
         JMXResource var6 = new JMXResource(var2, var5, var4, var3);
         AuthenticatedSubject var7 = SecurityServiceManager.getCurrentSubject(kernelId);
         JMXContextHandler var8 = new JMXContextHandler(var1);
         if (!this.authorizer.isAccessAllowed(var7, var6, var8)) {
            String var9 = "Access not allowed for subject: " + var7 + ", on Resource " + var4 + " Operation: " + var2 + " , Target: " + var3;
            throw new NoAccessRuntimeException(var9);
         }
      }
   }

   private void isAccessAllowedInvoke(ObjectName var1, String var2, String var3, Object[] var4, String[] var5, String var6, Object var7) throws NoAccessRuntimeException {
      if (this.isWLSMBean(var1) || this.isCommoMBean(var1) || var1 == null || var1.getKeyProperty("Type") != null) {
         String var8 = this.getBeanType(var1);
         String var9 = this.getAppName(var1);
         JMXResource var10 = new JMXResource(var2, var9, var8, var3);
         AuthenticatedSubject var11 = SecurityServiceManager.getCurrentSubject(kernelId);
         JMXContextHandler var12 = new JMXContextHandler(var1, var4, var5, var6, var7);
         if (!this.authorizer.isAccessAllowed(var11, var10, var12)) {
            String var13 = "Access not allowed for subject: " + var11 + ", on Resource " + var8 + " Operation: " + var2 + " , Target: " + var3;
            throw new NoAccessRuntimeException(var13);
         }
      }
   }

   private String getBeanType(ObjectName var1) {
      if (var1 == null) {
         return null;
      } else {
         Object var2 = this.wlsMBeanServer.lookupObject(var1);
         BeanInfo var4;
         if (var2 != null && var2 instanceof WLSModelMBean) {
            WLSModelMBean var3 = (WLSModelMBean)var2;
            var4 = var3.getBeanInfo();
            if (var4 != null) {
               BeanDescriptor var5 = var4.getBeanDescriptor();
               if (var5 != null) {
                  return (String)var5.getValue("interfaceclassname");
               }
            }
         }

         String var7 = var1.getKeyProperty("Type");
         if (var7 != null) {
            var4 = null;
            String var8;
            if (var7.endsWith("Runtime")) {
               var8 = "weblogic.management.runtime." + var7 + "MBean";
            } else {
               var8 = "weblogic.management.configuration." + var7 + "MBean";
            }

            try {
               Class var9 = Class.forName(var8);
               return var8;
            } catch (Exception var6) {
            }
         }

         return var7;
      }
   }

   private String getAppName(ObjectName var1) {
      if (var1 == null) {
         return null;
      } else {
         for(int var2 = 0; var2 < APP_SCOPED_TYPES.length; ++var2) {
            String var3 = var1.getKeyProperty(APP_SCOPED_TYPES[var2]);
            if (var3 != null) {
               return var3;
            }

            String var4 = var1.getKeyProperty("Type");
            if (APP_SCOPED_TYPES[var2].equals(var4)) {
               return var1.getKeyProperty("Name");
            }
         }

         String var5 = var1.getKeyProperty("Path");
         if (var5 != null) {
            int var6 = var5.indexOf("[");
            int var7 = var5.indexOf("]");
            if (var6 != -1 && var7 != -1 && var6 < var7) {
               return var5.substring(var6 + 1, var7);
            } else {
               return var1.getKeyProperty("Name");
            }
         } else {
            return null;
         }
      }
   }

   private void setUncheckedPolicy(JMXPolicyHandler var1, JMXResource var2) throws ConsumptionException {
      if (debug.isDebugEnabled()) {
         debug.debug("Register unchecked policy " + var2);
      }

      var1.setUncheckedPolicy(var2);
   }

   private void setPolicy(JMXPolicyHandler var1, JMXResource var2, String[] var3, String[] var4) throws ConsumptionException {
      int var5 = var3 == null ? 0 : var3.length;
      int var6 = var4 == null ? 0 : var4.length;
      int var7 = var5 + var6;
      String[] var8 = new String[var7 + 1];

      int var9;
      for(var9 = 0; var9 < var5; ++var9) {
         var8[var9] = var3[var9];
      }

      for(var9 = 0; var9 < var6; ++var9) {
         var8[var9 + var5] = var4[var9];
      }

      var8[var7] = "Admin";
      if (debug.isDebugEnabled()) {
         String var11 = "";

         for(int var10 = 0; var8 != null && var10 < var8.length; ++var10) {
            if (var10 > 0) {
               var11 = var11 + ",";
            }

            var11 = var11 + var8[var10];
         }

         debug.debug("Register checked policy " + var2 + " roles " + var11);
      }

      var1.setPolicy(var2, var8);
   }

   private String formatTimestamp(String var1) {
      Date var2 = null;

      try {
         Long var3 = new Long(var1);
         var2 = new Date(var3);
      } catch (NumberFormatException var4) {
         var2 = new Date();
      }

      SimpleDateFormat var5 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      var5.setTimeZone(TimeZone.getTimeZone("GMT"));
      return var5.format(var2);
   }

   private void checkForBEADomain(ObjectName var1) {
      if (var1 != null && BEA_DOMAIN.equals(var1.getDomain())) {
         Loggable var2 = JMXLogger.logMBeanRegistrationFailedLoggable(var1.getCanonicalName());
         throw new NoAccessRuntimeException(var2.getMessage());
      }
   }
}
