package weblogic.xml.crypto.wss11.internal.bst;

import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import weblogic.xml.crypto.encrypt.Utils;
import weblogic.xml.crypto.utils.CertUtils;
import weblogic.xml.crypto.wss.BSTUtils;
import weblogic.xml.crypto.wss.BinarySecurityTokenHandler;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.X509Credential;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;
import weblogic.xml.crypto.wss.api.KeyIdentifier;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public class BSTHandler extends BinarySecurityTokenHandler {
   private static final String[] VALUE_TYPES = new String[]{"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v1", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509PKIPathv1", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#PKCS7", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509SubjectKeyIdentifier", "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1"};

   protected SecurityToken getTokenByKeyId(KeyIdentifier var1, String var2, String var3, List var4, Purpose var5, WSSecurityContext var6) throws WSSecurityException {
      Iterator var7 = var4.iterator();

      SecurityToken var8;
      do {
         if (!var7.hasNext()) {
            Object var9 = this.getCredential("com.bea.contextelement.xml.KeyIdentifier", var1, var2, var5, var6);
            if (var9 != null) {
               return this.getToken(var9, var2, var6);
            }

            X509Certificate var10 = lookupCertificate(var1.getIdentifier(), var3);
            if (var10 != null) {
               return this.getToken(var10, var2, var6);
            }

            throw new WSSecurityException("Failed to resolve security token from key identifier " + var1, WSSConstants.FAILURE_TOKEN_UNAVAILABLE);
         }

         var8 = (SecurityToken)var7.next();
      } while(!(var8 instanceof BinarySecurityToken) || !matches(var1, var3, (X509Credential)var8.getCredential()));

      return this.amend((BinarySecurityToken)var8, var5, var6);
   }

   public SecurityTokenReference getSTR(QName var1, String var2, SecurityToken var3) throws WSSecurityException {
      return var3 == null ? null : new BSTR(var1, var2, var3);
   }

   private static X509Certificate lookupCertificate(byte[] var0, String var1) {
      return "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1".equals(var1) ? CertUtils.lookupCertificate(Utils.toBase64(var0)) : CertUtils.lookupCertificate(var0);
   }

   public static boolean matches(KeyIdentifier var0, String var1, X509Credential var2) {
      return "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1".equals(var1) ? BSTUtils.matchesThumbprint(var0, var2) : BSTUtils.matches(var0, var2);
   }

   public String[] getValueTypes() {
      return VALUE_TYPES;
   }
}
