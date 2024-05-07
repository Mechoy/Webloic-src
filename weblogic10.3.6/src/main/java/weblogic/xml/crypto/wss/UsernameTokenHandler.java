package weblogic.xml.crypto.wss;

import java.util.Iterator;
import java.util.List;
import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import org.w3c.dom.Node;
import weblogic.security.UsernameAndPassword;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.SecurityServiceManager;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.common.keyinfo.KeyProvider;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.api.NonceValidator;
import weblogic.xml.crypto.wss.api.UsernameToken;
import weblogic.xml.crypto.wss.nonce.NonceValidatorFactory;
import weblogic.xml.crypto.wss.policy.ClaimsBuilder;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.dom.Util;

public class UsernameTokenHandler implements SecurityTokenHandler {
   private static final QName[] qnames;
   private static final String[] valueTypes;
   private static final String POLICY_URI = "http://www.bea.com/wls90/security/policy";
   private static final QName POLICY_SUBJECT_NAME;
   private boolean passwordDigestSupported;
   public static final String OLD_USERNAME_TOKEN_URI = "http://www.docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0";
   private static final String OLD_PASSWORD_TYPE_TEXT = "http://www.docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";
   private static final String OLD_PASSWORD_TYPE_DIGEST = "http://www.docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest";
   private NonceValidator nonceChecker = null;

   public String[] getValueTypes() {
      return valueTypes;
   }

   public KeyProvider getKeyProvider(SecurityToken var1, MessageContext var2) {
      return null;
   }

   public SecurityToken getSecurityToken(SecurityTokenReference var1, MessageContext var2) {
      return null;
   }

   public SecurityTokenValidateResult validateUnmarshalled(SecurityToken var1, MessageContext var2) throws WSSecurityException {
      boolean var3 = true;
      UsernameToken var4 = (UsernameToken)var1;
      if (hasPassword(var4)) {
         String var5 = var4.getPasswordType();
         if (var5.equals("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest")) {
            String var9 = var4.getPasswordDigest();
            String var7 = var4.getEncodedNonce();
            if (var7 == null || var7.length() == 0 || var9 == null || var9.length() == 0 || var4.getCreated() == null) {
               var3 = false;
            }
         } else if (var5.equals("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText")) {
            byte[] var6 = var4.getPassword();
            if (var6 == null || var6.length == 0) {
               var3 = false;
            }
         }

         if (null != var4.getEncodedNonce() && var4.getEncodedNonce().length() > 0) {
            if (null != var4.getCreated()) {
               try {
                  this.nonceChecker = NonceValidatorFactory.getInstance();
                  this.nonceChecker.checkNonceAndTime(var4.getEncodedNonce(), var4.getCreated());
               } catch (SOAPFaultException var8) {
                  return new SecurityTokenValidateResult(false, "UNT Error:" + var8.getMessage());
               }
            } else {
               var3 = false;
            }
         } else if (null != var4.getCreated()) {
            var3 = false;
         }
      }

      return new SecurityTokenValidateResult(var3, var4.toString());
   }

   public SecurityTokenValidateResult validateProcessed(SecurityToken var1, MessageContext var2) {
      if (!hasPassword((UsernameToken)var1)) {
         WSSecurityContext var3 = WSSecurityContext.getSecurityContext(var2);
         Node var4 = var3.getNode(var1);
         List var5 = var3.getSignatures();
         Iterator var6 = var5.iterator();

         SignatureInfo var7;
         do {
            if (!var6.hasNext()) {
               return new SecurityTokenValidateResult(false);
            }

            var7 = (SignatureInfo)var6.next();
         } while(!var7.containsNode(var4));

         return new SecurityTokenValidateResult(true);
      } else {
         return new SecurityTokenValidateResult(true);
      }
   }

   public boolean matches(SecurityToken var1, String var2, String var3, ContextHandler var4, Purpose var5) {
      if (var1 == null) {
         return false;
      } else if (!Purpose.IDENTITY.equals(var5)) {
         return false;
      } else if (!(var1 instanceof UsernameToken)) {
         return false;
      } else {
         return this.matches((UsernameToken)var1, var4);
      }
   }

   private boolean matches(UsernameToken var1, ContextHandler var2) {
      Object var3 = var2.getValue("weblogic.xml.crypto.wss.policy.Claims");
      if (var3 != null) {
         Node var4 = (Node)var3;
         LogUtils.logWss("Trying to match UsernameToken to assertion " + Util.printNode(var4));
         String var5 = ClaimsBuilder.getClaimFromAttr(var4, WSSConstants.POLICY_USE_PASSWD_QNAME, WSSConstants.POLICY_USE_PASSWD_TYPE_QNAME);
         if (var5 != null && !this.isSamePasswordType(var5, var1.getPasswordType())) {
            LogUtils.logWss("Password type " + var1.getPasswordType() + " does not match.");
            return false;
         }

         String var6 = ClaimsBuilder.getClaimFromAttr(var4, WSSConstants.POLICY_USE_PASSWD_QNAME, UsernameTokenImpl.POLICY_PASSWD_ATTR);
         if (null != var6) {
            LogUtils.logWss("Trying to match UsernameToken to Nonce and Created assertions");
            if ("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#NonceCreate".equals(var6)) {
               if (var1.getCreated() == null) {
                  LogUtils.logWss("Missing Created element in UNT");
                  return false;
               }

               if (var1.getEncodedNonce() == null) {
                  LogUtils.logWss("Missing Nonce element in UNT");
                  return false;
               }
            } else {
               if ("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#Nonce".equals(var6) && var1.getEncodedNonce() == null) {
                  LogUtils.logWss("Missing Encoded Nonce in UNT");
                  return false;
               }

               if ("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#Create".equals(var6) && var1.getCreated() == null) {
                  LogUtils.logWss("Missing Created in UNT");
                  return false;
               }
            }
         }

         if (var5 != null && !this.isSamePasswordType(var5, var1.getPasswordType())) {
            LogUtils.logWss("Password type " + var1.getPasswordType() + " does not match.");
            return false;
         }
      }

      return true;
   }

   private boolean isSamePasswordType(String var1, String var2) {
      if (var1.equals(var2)) {
         return true;
      } else if (var2.equals("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText") && var1.equals("http://www.docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText")) {
         return true;
      } else {
         return var2.equals("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest") && var1.equals("http://www.docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest");
      }
   }

   public Subject getSubject(SecurityToken var1, MessageContext var2) throws WSSecurityException {
      UsernameToken var3 = (UsernameToken)var1;
      String var4 = SecurityServiceManager.getDefaultRealmName();
      return hasPassword(var3) ? SecurityUtils.assertIdentity(var3, var4) : null;
   }

   private static boolean hasPassword(UsernameToken var0) {
      return var0.getPassword() != null || var0.getPasswordDigest() != null;
   }

   public SecurityToken getSecurityToken(String var1, Object var2, ContextHandler var3) throws WSSecurityException {
      return var2 != null ? new UsernameTokenImpl((UsernameAndPassword)var2, var3) : null;
   }

   public SecurityToken getSecurityToken(String var1, String var2, Purpose var3, ContextHandler var4) throws WSSecurityException {
      return null;
   }

   public SecurityToken newSecurityToken(Node var1) throws MarshalException {
      UsernameTokenImpl var2 = new UsernameTokenImpl();

      try {
         var2.unmarshal(var1);
         return var2;
      } catch (weblogic.xml.dom.marshal.MarshalException var4) {
         throw new MarshalException("Failed to unmarshal UserNameToken.", var4);
      }
   }

   public QName[] getQNames() {
      return qnames;
   }

   public SecurityTokenReference getSTR(QName var1, String var2, SecurityToken var3) throws WSSecurityException {
      return new UsernameTokenReference(var1, var2, var3);
   }

   public SecurityTokenReference newSecurityTokenReference(Node var1) {
      return new UsernameTokenReference();
   }

   public void setPasswordDigestSupported(boolean var1) {
      this.passwordDigestSupported = var1;
   }

   public boolean isPasswordDigestSupported() {
      return this.passwordDigestSupported;
   }

   static {
      qnames = new QName[]{WSSConstants.UNT_QNAME};
      valueTypes = new String[]{"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken"};
      POLICY_SUBJECT_NAME = new QName("http://www.bea.com/wls90/security/policy", "PasswordType");
   }
}
