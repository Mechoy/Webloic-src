package weblogic.wsee.security.bst;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Set;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.security.SSL.TrustManager;
import weblogic.security.service.ContextHandler;
import weblogic.utils.encoders.BASE64Decoder;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.security.policy.assertions.ConfidentialityAssertion;
import weblogic.wsee.security.policy.assertions.xbeans.ConfidentialityDocument;
import weblogic.wsee.security.policy.assertions.xbeans.KeyInfoType;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenReferenceType;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.BSTUtils;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.X509Credential;
import weblogic.xml.crypto.wss.provider.Purpose;

public class PolicyBSTCredentialProvider extends BSTCredentialProvider {
   private X509Credential serverCredential = null;

   public PolicyBSTCredentialProvider(PolicyAlternative var1, WSSecurityContext var2) throws IOException, CertificateException, WSSecurityException {
      if (var1 != null) {
         Set var3 = var1.getAssertions(ConfidentialityAssertion.class);
         Iterator var4 = var3.iterator();

         while(true) {
            while(var4.hasNext()) {
               ConfidentialityAssertion var5 = (ConfidentialityAssertion)var4.next();
               ConfidentialityDocument.Confidentiality var6 = var5.getXbean().getConfidentiality();
               KeyInfoType var7 = var6.getKeyInfo();
               SecurityTokenReferenceType[] var8 = var7.getSecurityTokenReferenceArray();

               for(int var9 = 0; var8 != null && var9 < var8.length; ++var9) {
                  SecurityTokenReferenceType var10 = var8[var9];
                  NodeList var11 = ((Element)var10.newDomNode().getFirstChild()).getElementsByTagNameNS("http://www.bea.com/wls90/security/policy", "Embedded");
                  if (var11 != null && var11.getLength() > 0) {
                     NodeList var12 = ((Element)var11.item(0)).getElementsByTagNameNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "BinarySecurityToken");
                     if (var12 != null && var12.getLength() > 0) {
                        String var13 = DOMUtils.getText((Element)var12.item(0));
                        byte[] var14 = (new BASE64Decoder()).decodeBuffer(var13);
                        CertificateFactory var15 = CertificateFactory.getInstance("X.509");
                        X509Certificate var16 = (X509Certificate)var15.generateCertificate(new ByteArrayInputStream(var14));
                        if (!this.isTrusted(var16, var2)) {
                           throw new WSSecurityException("Server cert not trusted.");
                        }

                        this.serverCredential = new X509Credential(var16);
                        break;
                     }
                  }
               }
            }

            return;
         }
      }
   }

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4) {
      return this.serverCredential != null && (var4 == null || isForEncryption(var4) || isForVerification(var4)) && BSTUtils.matches(this.serverCredential, var3) ? this.serverCredential : null;
   }

   private boolean isTrusted(X509Certificate var1, WSSecurityContext var2) throws WSSecurityException {
      TrustManager var3 = (TrustManager)var2.getProperty("weblogic.wsee.security.wss.TrustManager");
      if (var3 == null) {
         throw new WSSecurityException("No TrustManager set.");
      } else {
         X509Certificate[] var4 = new X509Certificate[]{var1};
         return var3.certificateCallback(var4, 16);
      }
   }
}
