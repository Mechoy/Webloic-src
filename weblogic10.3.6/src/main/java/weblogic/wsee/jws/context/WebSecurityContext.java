package weblogic.wsee.jws.context;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import weblogic.jws.CallbackMethod;
import weblogic.security.SubjectUtils;
import weblogic.security.WLSPrincipals;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.RoleManager;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.WebServiceResource;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.utils.Debug;
import weblogic.wsee.connection.transport.servlet.HttpTransportUtils;
import weblogic.wsee.jws.util.Util;
import weblogic.wsee.message.WlMessageContext;

public class WebSecurityContext implements JwsSecurityContext {
   private static final String DEBUG_PROPERTY = "weblogic.wsee.security.debug";
   private static final boolean DEBUG = Boolean.getBoolean("weblogic.wsee.security.debug");
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private boolean initted = false;
   private WlMessageContext messageContext;
   private Class jws;
   private Class<?> eiClass;
   private HashMap<WebServiceResource, Map> roles = new HashMap();

   public WebSecurityContext(WlMessageContext var1, Class var2) {
      this.messageContext = var1;
      this.jws = var2;
      this.eiClass = Util.getEIClass(var2);
   }

   public void setMessageContext(WlMessageContext var1) {
      this.messageContext = var1;
   }

   public Principal getCallerPrincipal() {
      Principal var1 = null;
      AuthenticatedSubject var2 = getSubject();
      if (var2 != null) {
         var1 = SubjectUtils.getUserPrincipal(var2);
      }

      if (var1 == null) {
         var1 = WLSPrincipals.getAnonymousUserPrincipal();
      }

      return var1;
   }

   public boolean isCallerInRole(String var1) {
      if (var1 == null) {
         return false;
      } else {
         AuthenticatedSubject var2 = getSubject();
         this.initRoleMaps(var2);
         Iterator var3 = this.roles.entrySet().iterator();

         Map.Entry var4;
         do {
            if (!var3.hasNext()) {
               HttpServletRequest var5 = HttpTransportUtils.getHttpServletRequest(this.messageContext);
               if (var5 != null && var5 instanceof ServletRequestImpl) {
                  return ((ServletRequestImpl)var5).getContext().getSecurityManager().getWebAppSecurity().isSubjectInRole(var2, var1, ((ServletRequestImpl)var5).getSecurityContextHandler(), ((ServletRequestImpl)var5).getServletStub());
               }

               return false;
            }

            var4 = (Map.Entry)var3.next();
         } while(!SecurityServiceManager.isUserInRole(var2, var1, (Map)var4.getValue()));

         return true;
      }
   }

   private void initRoleMaps(AuthenticatedSubject var1) {
      if (!this.initted) {
         String var2 = (String)this.messageContext.getProperty("weblogic.wsee.service_name");
         String var3 = (String)this.messageContext.getProperty("weblogic.wsee.context_path");
         String var4 = (String)this.messageContext.getProperty("weblogic.wsee.security_realm");
         String var5 = (String)this.messageContext.getProperty("weblogic.wsee.application_id");
         RoleManager var6 = (RoleManager)SecurityServiceManager.getSecurityService(KERNEL_ID, var4, ServiceType.ROLE);
         Method[] var7 = this.jws.getMethods();
         Method[] var8 = var7;
         int var9 = var7.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            Method var11 = var8[var10];
            if (Util.isWebMethod(var11, this.eiClass) || var11.isAnnotationPresent(CallbackMethod.class)) {
               WebServiceResource var12 = createWebServiceResource(var5, var3, var2, var11);
               Map var13 = var6.getRoles(var1, var12, (ContextHandler)null);
               if (var13 != null) {
                  this.roles.put(var12, var13);
                  if (DEBUG) {
                     Debug.say("*** Roles map for " + var12 + " is " + var13);
                  }
               }
            }
         }

         this.initted = true;
      }

   }

   private static final WebServiceResource createWebServiceResource(String var0, String var1, String var2, Method var3) {
      String var4 = var3.getName();
      ArrayList var5 = new ArrayList();
      Class[] var6 = var3.getParameterTypes();
      Class[] var7 = var6;
      int var8 = var6.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         Class var10 = var7[var9];
         var5.add(var10.getCanonicalName());
      }

      String[] var11 = (String[])var5.toArray(new String[var5.size()]);
      WebServiceResource var12 = new WebServiceResource(var0, var1, var2, var4, var11);
      if (DEBUG) {
         Debug.say("*** Creating WebServiceResource: " + var12);
      }

      return var12;
   }

   private static AuthenticatedSubject getSubject() {
      return SecurityServiceManager.getCurrentSubject(KERNEL_ID);
   }
}
