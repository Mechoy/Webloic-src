package weblogic.wsee.util;

import com.bea.util.jam.JAnnotatedElement;
import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import weblogic.jws.Policies;
import weblogic.jws.Policy;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicies;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicy;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.runtime.BuiltinPolicyFinder;
import weblogic.wsee.policy.runtime.PolicyFinder;
import weblogic.wsee.security.policy.assertions.SecurityPolicyAssertionFactory;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.jws.decl.PolicyDecl;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.LogEvent;
import weblogic.wsee.tools.logging.Logger;

public abstract class PolicyValidateUtil {
   public static final void checkClassScopedOWSMPolicy(JClass var0, Logger var1) {
      JAnnotation var2 = var0.getAnnotation(Policy.class);
      JAnnotation var3 = var0.getAnnotation(SecurityPolicy.class);
      JAnnotation var4 = var0.getAnnotation(Policies.class);
      JAnnotation var5 = var0.getAnnotation(SecurityPolicies.class);
      if (var2 != null && var3 != null) {
         var1.log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var0, "annotation.WLSPolicy.OWSMSecurityPolicy.notAllowed", new Object[]{Policy.class.getName(), SecurityPolicy.class.getName()})));
      }

      if (var4 != null && var5 != null) {
         var1.log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var0, "annotation.WLSPolicy.OWSMSecurityPolicy.notAllowed", new Object[]{Policy.class.getName(), SecurityPolicy.class.getName()})));
      }

      if (var3 != null || var5 != null) {
         String var6 = var3 != null ? SecurityPolicy.class.getName() : SecurityPolicies.class.getName();
         JMethod[] var7 = var0.getMethods();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            JMethod var10 = var7[var9];
            if (var10.getAnnotation(Policy.class) != null || var10.getAnnotation(Policies.class) != null) {
               String var11 = var10.getAnnotation(Policy.class) != null ? Policy.class.getName() : Policies.class.getName();
               var1.log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var0, "annotation.WLSPolicy.OWSMSecurityPolicy.notAllowed", new Object[]{var11, var6})));
            }
         }
      }

   }

   public static final void checkClassScopedWebLogicProprietaryPolicy(WebServiceSEIDecl var0, Logger var1) {
      Iterator var2 = var0.getPoilices();

      while(var2.hasNext()) {
         PolicyDecl var3 = (PolicyDecl)var2.next();
         PolicyStatement var4 = getPolicyStatement(var3, var0.getJClass(), var1);

         try {
            if (var4 != null && SecurityPolicyAssertionFactory.hasSecurityPolicy(var4.normalize())) {
               var1.log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var0.getJClass(), "policy.9.xPolicyOnJAXWS.notallowed", new Object[0])));
            }
         } catch (PolicyException var6) {
         }
      }

   }

   public static final void checkMethodScopedWebLogicProprietaryPolicy(WebMethodDecl var0, Logger var1) {
      Iterator var2 = var0.getPoilices();

      while(var2.hasNext()) {
         PolicyDecl var3 = (PolicyDecl)var2.next();
         PolicyStatement var4 = getPolicyStatement(var3, var0.getJMethod(), var1);

         try {
            if (var4 != null && SecurityPolicyAssertionFactory.hasSecurityPolicy(var4.normalize())) {
               var1.log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var0.getJMethod(), "policy.9.xPolicyOnJAXWS.notallowed", new Object[0])));
            }
         } catch (PolicyException var6) {
         }
      }

   }

   public static final void checkIllegalPolicyMixUp(WebServiceSEIDecl var0, Logger var1) {
      HashSet var2 = new HashSet();
      Iterator var3 = var0.getPoilices();

      while(var3.hasNext()) {
         var2.add(var3.next());
      }

      Iterator var4 = var0.getWebMethods();

      while(var4.hasNext()) {
         var3 = ((WebMethodDecl)var4.next()).getPoilices();

         while(var3.hasNext()) {
            var2.add(var3.next());
         }
      }

      validatePolicyMixUp(var2.iterator(), var0.getJClass(), var1);
   }

   private static final void validatePolicyMixUp(Iterator<PolicyDecl> var0, JAnnotatedElement var1, Logger var2) {
      boolean var3 = false;
      boolean var4 = false;

      while(var0.hasNext()) {
         PolicyDecl var5 = (PolicyDecl)var0.next();

         try {
            PolicyStatement var6 = getPolicyStatement(var5, var1, var2);
            if (var6 != null && SecurityPolicyAssertionFactory.hasSecurityPolicy(var6.normalize())) {
               var3 = true;
            }

            if (var6 != null && SecurityPolicyAssertionInfoFactory.hasSecurityPolicy(var6.normalize())) {
               var4 = true;
            }

            if (var3 & var4) {
               var2.log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "policy.9.xPolicyAndWsspPolicy.MixUp.notallowed", new Object[0])));
               return;
            }
         } catch (PolicyException var7) {
         }
      }

   }

   private static PolicyStatement getPolicyStatement(PolicyDecl var0, JAnnotatedElement var1, Logger var2) {
      PolicyStatement var3 = null;

      try {
         if (var0.isBuiltInPolicy()) {
            String var4 = var0.getBuiltInUriWithoutPrefix();
            var3 = BuiltinPolicyFinder.getInstance().findPolicy(var4, (String)null);
         } else {
            InputStream var6 = var0.getPolicyURI().toURL().openConnection().getInputStream();
            var3 = PolicyFinder.readPolicyFromStream(var0.getUri(), var6, true);
         }
      } catch (Exception var5) {
      }

      return var3;
   }
}
