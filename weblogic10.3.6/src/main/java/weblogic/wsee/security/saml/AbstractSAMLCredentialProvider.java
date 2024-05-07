package weblogic.wsee.security.saml;

import javax.security.auth.Subject;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;

public abstract class AbstractSAMLCredentialProvider implements CredentialProvider {
   private static boolean verbose = Verbose.isVerbose(AbstractSAMLCredentialProvider.class);

   public abstract String[] getValueTypes();

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4) {
      if (!var4.equals(Purpose.IDENTITY) && !var4.equals(Purpose.SIGN)) {
         return null;
      } else if (!(var3 instanceof SecurityTokenContextHandler)) {
         return null;
      } else {
         boolean var5 = var1.equals("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0");
         SecurityTokenContextHandler var6 = new SecurityTokenContextHandler();
         Object var7 = var3.getValue("com.bea.contextelement.saml.CachingRequested");
         if (var7 != null) {
            if (verbose) {
               Verbose.log((Object)"Requesting cached SAML assertion");
            }

            var6.addContextElement("com.bea.contextelement.saml.CachingRequested", var7);
         }

         Node var8 = (Node)var3.getValue("weblogic.xml.crypto.wss.policy.Claims");
         CSSUtils.processSAMLClaims(var5, var6, var8);
         CSSUtils.setupSAMLContextElements(var5, var6, var3);
         boolean var9 = null != var3.getValue("oracle.contextelement.saml2.AttributeOnly");
         if (verbose) {
            Verbose.log((Object)("Setting SAML Attributes for attributeOnly = " + var9));
         }

         CSSUtils.setupSAMLAttributesContextElements(var5, var6, var9, this.getSAMLAttributeData(CSSUtils.getCurrentAuthenticatedSubject()));
         Object var10 = null;
         if (CSSUtils.isHolderOfKey(var5, var6)) {
            if (SAMLIssuedTokenHelper.isSymmetricKeyTypeFromIssuedTokenClaim(var8)) {
               var10 = this.getKeyInfoCredential(var1, var2, (SecurityTokenContextHandler)var3, var4, var8);
               if (null == var10) {
                  return null;
               }
            } else {
               if (verbose) {
                  Verbose.log((Object)"Getting X509 KeyInfo credential");
               }

               var10 = this.getKeyInfoCredential(var1, var2, (SecurityTokenContextHandler)var3, var4);
            }
         }

         try {
            return CSSUtils.getSAMLCredential(var5, var1, var6, var10);
         } catch (WSSecurityException var12) {
            if (verbose) {
               Verbose.log("Exception while acquiring SAML credential", var12);
            }

            return null;
         }
      }
   }

   public Object getKeyInfoCredential(String var1, String var2, SecurityTokenContextHandler var3, Purpose var4) {
      return CSSUtils.getX509CredFromPKICredMapper(var3);
   }

   public Object getKeyInfoCredential(String var1, String var2, SecurityTokenContextHandler var3, Purpose var4, Node var5) {
      Object var6 = var3.getValue("com.bea.contextelement.saml.subject.dom.KeyInfo");
      if (var6 != null && verbose) {
         Verbose.log((Object)("Get Symmetric KeyInfo credential =" + var6));
      }

      if (verbose) {
         Verbose.log((Object)"Symmetric KeyInfo credential is NOT Supported!");
      }

      return null;
   }

   public SAMLAttributeStatementData getSAMLAttributeData(Subject var1) {
      if (verbose) {
         Verbose.log((Object)"No override on getSAMLAttributeData() for SAML Attributes");
      }

      return null;
   }
}
