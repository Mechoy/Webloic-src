package weblogic.wsee.server.servlet;

import java.security.cert.X509Certificate;
import java.util.Iterator;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import weblogic.management.configuration.SSLMBean;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.utils.http.HttpParsing;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.security.wssp.HttpsTokenAssertion;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfo;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.wsee.security.wssp.TransportBindingInfo;
import weblogic.wsee.util.MBeanUtil;
import weblogic.wsee.util.ServerSecurityHelper;

public class SecurityHelper {
   private static String AUTHENTICATION_HEADER = "Authorization";
   private final String securityRealm;

   public SecurityHelper(WebAppServletContext var1) {
      this.securityRealm = var1.getSecurityRealmName();
   }

   public String getSecurityRealm() {
      return this.securityRealm;
   }

   public static boolean isAnonymous(AuthenticatedSubject var0) {
      return SubjectUtils.isUserAnonymous(var0);
   }

   public final AuthenticatedSubject getRequestSubject(HttpServletRequest var1) throws LoginException {
      return getRequestSubject(var1, this.securityRealm);
   }

   public static AuthenticatedSubject getRequestSubject(HttpServletRequest var0, String var1) throws LoginException {
      String var2 = var0.getHeader(AUTHENTICATION_HEADER);
      if (var2 != null) {
         String[] var3 = HttpParsing.getAuthInfo(var2);
         if (var3 == null) {
            return null;
         }

         if (var3 != null && var3[0] != null && var3[1] != null) {
            return ServerSecurityHelper.assertIdentity(var3[0], var3[1], var1);
         }
      }

      if (var0.isSecure()) {
         X509Certificate[] var4 = getClientCerts(var0);
         if (var4 != null && var4.length > 0) {
            return ServerSecurityHelper.assertIdentity(var4, var1);
         }
      }

      return null;
   }

   public static boolean isHttpsRequiredByWssp(NormalizedExpression var0) {
      return SecurityPolicyAssertionInfoFactory.hasTransportSecurityPolicy(var0);
   }

   public static boolean hasWsspMessageSecurityPolicy(NormalizedExpression var0) {
      return SecurityPolicyAssertionInfoFactory.hasMessageSecurityPolicy(var0);
   }

   public static boolean hasWsTrustPolicy(NormalizedExpression var0) {
      return SecurityPolicyAssertionInfoFactory.hasWsTrustPolicy(var0);
   }

   public static HttpsTokenAssertion getHttpsTokenAssertion(NormalizedExpression var0) {
      if (var0.getPolicyAlternatives() != null) {
         Iterator var1 = var0.getPolicyAlternatives().iterator();

         while(var1.hasNext()) {
            PolicyAlternative var2 = (PolicyAlternative)var1.next();
            SecurityPolicyAssertionInfo var3 = SecurityPolicyAssertionInfoFactory.getSecurityPolicyAssertionInfo(var2);
            if (var3 != null) {
               TransportBindingInfo var4 = var3.getTransportBindingInfo();
               if (var4 != null) {
                  return var4.getHttpsTokenAssertion();
               }
            }
         }
      }

      return null;
   }

   public static boolean isClientCertRequiredByWssp(NormalizedExpression var0) {
      HttpsTokenAssertion var1 = getHttpsTokenAssertion(var0);
      return var1 != null && var1.isClientCertificateRequired();
   }

   public static boolean isClientCertPresent(HttpServletRequest var0) {
      X509Certificate[] var1 = getClientCerts(var0);
      return var1 != null && var1.length > 0;
   }

   public static boolean isBasicAuthReqByWssp(NormalizedExpression var0) {
      HttpsTokenAssertion var1 = getHttpsTokenAssertion(var0);
      return var1 != null && var1.isHttpBasicAuthenticationRequired();
   }

   private static final X509Certificate[] getClientCerts(HttpServletRequest var0) {
      try {
         return (X509Certificate[])((X509Certificate[])var0.getAttribute("javax.servlet.request.X509Certificate"));
      } catch (ClassCastException var2) {
         return null;
      }
   }

   public static boolean isTwoWaySSLEnabled() {
      SSLMBean var0 = MBeanUtil.getLocalSSLMBean();
      return var0.isTwoWaySSLEnabled() || var0.isClientCertificateEnforced();
   }
}
