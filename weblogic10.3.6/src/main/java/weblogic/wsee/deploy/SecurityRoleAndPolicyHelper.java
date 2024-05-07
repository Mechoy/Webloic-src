package weblogic.wsee.deploy;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.SecurityRole;
import weblogic.jws.CallbackMethod;
import weblogic.jws.security.CallbackRolesAllowed;
import weblogic.jws.security.RolesAllowed;
import weblogic.jws.security.RolesReferenced;
import weblogic.jws.security.SecurityRoleRef;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ConsumptionException;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.WSPolicyConsumer;
import weblogic.security.service.WSPolicyHandler;
import weblogic.security.service.WSRoleConsumer;
import weblogic.security.service.WSRoleHandler;
import weblogic.security.service.WebServiceResource;
import weblogic.wsee.jws.util.Util;

final class SecurityRoleAndPolicyHelper {
   private static final AuthenticatedSubject kernelId = getKernelID();
   private static final String OLD_VERSION = "9.2.0.0";
   private static final String VERSION = "10.0.0.0";
   private static final boolean isInProductionMode = isProduction();
   private static final SimpleDateFormat timeFormatter = getTimeFormatter();
   private Class<?> webService;
   private Class<?> eiClass;
   private boolean policyConsumerEnabled = false;
   private WSPolicyConsumer policyConsumer;
   private WSPolicyHandler policyHandler;
   private boolean roleConsumerEnabled = false;
   private WSRoleConsumer roleConsumer;
   private WSRoleHandler roleHandler;
   private String serviceName;
   private String appName;
   private String contextPath;
   private ApplicationContextInternal appCtx;
   private boolean initted = false;

   SecurityRoleAndPolicyHelper(DeployInfo var1) {
      this.appName = var1.getApplication();
      this.serviceName = var1.getServiceName();
      this.contextPath = var1.getContextPath();
      this.webService = var1.getJwsClass();
      this.appCtx = var1.getApplicationContext();
      this.eiClass = Util.getEIClass(this.webService);
   }

   public void deploySecurityRolesAndPolicies() {
      try {
         this.deployWebMethodRolesAndPolicies();
         this.deployCallbackMethodRolesAndPolicies();
      } finally {
         this.cleanUp();
      }

   }

   private void init() {
      if (!this.initted) {
         try {
            String var1 = timeFormatter.format(new Date());
            this.policyConsumer = SecurityServiceManager.getWSPolicyConsumer(kernelId);
            if (this.policyConsumer != null) {
               this.policyConsumerEnabled = this.policyConsumer.isEnabled();
            }

            String var2 = this.appName + ":" + this.contextPath + ":" + this.serviceName;
            if (this.policyConsumerEnabled) {
               WSPolicyHandler var3 = this.policyConsumer.getWSPolicyHandler(this.appName, "9.2.0.0", var1);
               if (var3 != null) {
                  var3.done();
               }

               this.policyHandler = this.policyConsumer.getWSPolicyHandler(var2, "10.0.0.0", var1);
            }

            this.roleConsumer = SecurityServiceManager.getWSRoleConsumer(kernelId);
            if (this.roleConsumer != null) {
               this.roleConsumerEnabled = this.roleConsumer.isEnabled();
            }

            if (this.roleConsumerEnabled) {
               WSRoleHandler var5 = this.roleConsumer.getWSRoleHandler(this.appName, "9.2.0.0", var1);
               if (var5 != null) {
                  var5.done();
               }

               this.roleHandler = this.roleConsumer.getWSRoleHandler(var2, "10.0.0.0", var1);
            }
         } catch (ConsumptionException var4) {
            throw new WSEEServletEndpointException(var4);
         }

         this.initted = true;
      }
   }

   private void deployWebMethodRolesAndPolicies() {
      Map var1 = getRolesAllowed(this.webService);
      Method[] var2 = this.webService.getMethods();
      Method[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method var6 = var3[var5];
         if (Util.isWebMethod(var6, this.eiClass)) {
            Map var7 = getRolesAllowed(var6);
            if (var7.size() == 0) {
               if (var1.size() == 0) {
                  continue;
               }

               var7 = var1;
            }

            this.deployMethodRolesAndPolicies(var6, var7);
         }
      }

   }

   private void deployCallbackMethodRolesAndPolicies() {
      Method[] var1 = this.webService.getMethods();
      Method[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Method var5 = var2[var4];
         CallbackMethod var6 = (CallbackMethod)var5.getAnnotation(CallbackMethod.class);
         if (var6 != null) {
            Map var7 = getCallbackRolesAllowed(var5);
            if (var7.size() == 0) {
               try {
                  Field var8 = this.webService.getDeclaredField(var6.target());
                  var7 = getCallbackRolesAllowed(var8);
               } catch (NoSuchFieldException var9) {
                  throw new WSEEServletEndpointException(var9);
               }
            }

            if (var7.size() > 0) {
               this.deployMethodRolesAndPolicies(var5, var7);
            }
         }
      }

   }

   private void deployMethodRolesAndPolicies(Method var1, Map<String, String[]> var2) {
      String var3 = var1.getName();
      ArrayList var4 = new ArrayList();
      Class[] var5 = var1.getParameterTypes();
      Class[] var6 = var5;
      int var7 = var5.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         Class var9 = var6[var8];
         var4.add(var9.getCanonicalName());
      }

      String[] var10 = (String[])var4.toArray(new String[var4.size()]);
      WebServiceResource var11 = createWebServiceResource(this.appName, this.contextPath, this.serviceName, var3, var10);
      Iterator var12 = var2.entrySet().iterator();

      while(var12.hasNext()) {
         Map.Entry var13 = (Map.Entry)var12.next();
         this.deployRole(var11, (String)var13.getKey(), (String[])var13.getValue());
      }

      String[] var14 = (String[])var2.keySet().toArray(new String[var2.size()]);
      this.deployPolicy(var11, var14);
   }

   private final void cleanUp() {
      try {
         if (this.policyConsumerEnabled && this.policyHandler != null) {
            this.policyHandler.done();
         }

         if (this.roleConsumerEnabled && this.roleHandler != null) {
            this.roleHandler.done();
         }

      } catch (ConsumptionException var2) {
         throw new WSEEServletEndpointException(var2);
      }
   }

   private void deployRole(WebServiceResource var1, String var2, String[] var3) {
      this.init();
      String[] var4 = var3;
      if (var3.length == 0 && this.appCtx != null) {
         SecurityRole var5 = this.appCtx.getSecurityRole(var2);
         if (var5 != null && !var5.isExternallyDefined()) {
            var4 = var5.getPrincipalNames();
            if (var4 == null) {
               return;
            }
         }
      }

      if (var4.length != 0) {
         if (!this.roleConsumerEnabled) {
            if (isInProductionMode) {
               throw new WSEEServletEndpointException("Role consumer  is not supported");
            }
         } else {
            try {
               if (this.roleHandler != null) {
                  this.roleHandler.setRole(var1, var2, var4);
               }

            } catch (ConsumptionException var6) {
               throw new WSEEServletEndpointException(var6);
            }
         }
      }
   }

   private void deployPolicy(WebServiceResource var1, String[] var2) {
      this.init();
      if (!this.policyConsumerEnabled) {
         if (isInProductionMode) {
            throw new WSEEServletEndpointException("Policy consumer is not supported");
         }
      } else {
         try {
            if (this.policyHandler != null) {
               this.policyHandler.setPolicy(var1, var2);
            }

         } catch (ConsumptionException var4) {
            throw new WSEEServletEndpointException(var4);
         }
      }
   }

   private static final WebServiceResource createWebServiceResource(String var0, String var1, String var2, String var3, String[] var4) {
      return new WebServiceResource(var0, var1, var2, var3, var4);
   }

   private static Map<String, String[]> getRolesAllowed(AnnotatedElement var0) {
      HashMap var1 = new HashMap();
      RolesAllowed var2 = (RolesAllowed)var0.getAnnotation(RolesAllowed.class);
      if (var2 != null) {
         weblogic.jws.security.SecurityRole[] var3 = var2.value();
         weblogic.jws.security.SecurityRole[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            weblogic.jws.security.SecurityRole var7 = var4[var6];
            var1.put(var7.role(), var7.mapToPrincipals());
         }
      }

      return var1;
   }

   private static Map<String, String[]> getCallbackRolesAllowed(AnnotatedElement var0) {
      HashMap var1 = new HashMap();
      CallbackRolesAllowed var2 = (CallbackRolesAllowed)var0.getAnnotation(CallbackRolesAllowed.class);
      if (var2 != null) {
         weblogic.jws.security.SecurityRole[] var3 = var2.value();
         weblogic.jws.security.SecurityRole[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            weblogic.jws.security.SecurityRole var7 = var4[var6];
            var1.put(var7.role(), var7.mapToPrincipals());
         }
      }

      return var1;
   }

   private static Map<String, String> getRolesReferenced(AnnotatedElement var0) {
      HashMap var1 = new HashMap();
      RolesReferenced var2 = (RolesReferenced)var0.getAnnotation(RolesReferenced.class);
      if (var2 != null) {
         SecurityRoleRef[] var3 = var2.value();
         SecurityRoleRef[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            SecurityRoleRef var7 = var4[var6];
            String var8 = var7.role();
            String var9 = var7.link();
            if (var9.length() == 0) {
               var9 = var8;
            }

            var1.put(var8, var9);
         }
      }

      return var1;
   }

   private static AuthenticatedSubject getKernelID() {
      return (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   private static final boolean isProduction() {
      return ManagementService.getRuntimeAccess(kernelId).getDomain().isProductionModeEnabled();
   }

   private static final SimpleDateFormat getTimeFormatter() {
      SimpleDateFormat var0 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      var0.setTimeZone(TimeZone.getTimeZone("GMT"));
      return var0;
   }
}
