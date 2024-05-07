package weblogic.wsee.security;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.WebServiceResource;
import weblogic.wsee.handler.DirectInvokeData;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.ServerSecurityHelper;
import weblogic.wsee.util.Verbose;

public class DirectInvokeAuthorizer implements Authorizer {
   private final ResourceMap resourceMap = new ResourceMap();
   private AuthorizationManager am;
   private AuthorizationContext authContext;
   private static final boolean verbose = Verbose.isVerbose(DirectInvokeAuthorizer.class);

   public DirectInvokeAuthorizer(AuthorizationContext var1) {
      this.authContext = var1;
      this.am = ServerSecurityHelper.getAuthManager(this.authContext.getSecurityRealm());
   }

   public boolean isAccessAllowed(WlMessageContext var1) {
      AuthenticatedSubject var2 = ServerSecurityHelper.getCurrentSubject();
      Object var3 = var1.getProperty("weblogic.wsee.direct.invoke.data.prop");
      if (var3 != null && var3 instanceof DirectInvokeData) {
         DirectInvokeData var4 = (DirectInvokeData)var3;
         Method var5 = var4.getRequest().getMethod();
         String var6 = var1.getDispatcher().getWsdlPort().getName().getLocalPart();
         WebServiceResource var7 = this.getResource(var5, var6);
         if (verbose) {
            Verbose.log((Object)("DirectInvokeAuthorizer got operation " + var5.getName() + " and user " + var2 + ", using resource " + var7));
         }

         boolean var8 = this.am.isAccessAllowed(var2, var7, (ContextHandler)null);
         if (verbose) {
            if (var8) {
               Verbose.log((Object)("Access granted for subject " + var2 + " to resource " + var7));
            } else {
               Verbose.log((Object)("Access denied for subject " + var2 + " to resource " + var7));
            }
         }

         return var8;
      } else {
         return false;
      }
   }

   public WebServiceResource getResource(Method var1, String var2) {
      WebServiceResource var3 = this.lookupResource(var1);
      if (var3 == null) {
         var3 = createResource(this.authContext, var2, var1);
         this.cacheResource(var1, var3);
      }

      return var3;
   }

   private WebServiceResource lookupResource(Method var1) {
      return this.resourceMap.get(var1);
   }

   private WebServiceResource cacheResource(Method var1, WebServiceResource var2) {
      this.resourceMap.put(var1, var2);
      return var2;
   }

   private static WebServiceResource createResource(AuthorizationContext var0, String var1, Method var2) {
      String var3 = var2.getName();
      ArrayList var5 = new ArrayList();
      Class[] var6 = var2.getParameterTypes();
      Class[] var7 = var6;
      int var8 = var6.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         Class var10 = var7[var9];
         var5.add(var10.getCanonicalName());
      }

      int var11 = var5.size();
      String[] var4 = new String[var11];
      var5.toArray(var4);
      WebServiceResource var12 = null;
      if (verbose) {
         Verbose.log((Object)"** Args to WebServiceResource");
         Verbose.log((Object)("**   methodName = " + var3));
         Verbose.log((Object)("**   methodParams = " + var4));
      }

      var12 = new WebServiceResource(var0.getApplicationName(), var0.getContextPath(), var1, var3, var4);
      return var12;
   }

   String getSecurityRealm() {
      return this.authContext.getSecurityRealm();
   }

   protected static class ResourceMap {
      private Map resourceMap = null;

      protected ResourceMap() {
         this.resourceMap = Collections.synchronizedMap(new HashMap());
      }

      protected WebServiceResource get(Method var1) {
         return (WebServiceResource)this.resourceMap.get(var1);
      }

      protected WebServiceResource put(Method var1, WebServiceResource var2) {
         this.resourceMap.put(var1, var2);
         return var2;
      }
   }
}
