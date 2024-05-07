package weblogic.wsee.security.saml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.security.wss.SecurityPolicyException;
import weblogic.wsee.security.wss.policy.GeneralPolicy;
import weblogic.wsee.security.wssc.dk.DKClaims;
import weblogic.wsee.security.wssp.IssuedTokenAssertion;
import weblogic.wsee.security.wst.binding.CanonicalizationAlgorithm;
import weblogic.wsee.security.wst.binding.EncryptWith;
import weblogic.wsee.security.wst.binding.EncryptionAlgorithm;
import weblogic.wsee.security.wst.binding.KeySize;
import weblogic.wsee.security.wst.binding.KeyType;
import weblogic.wsee.security.wst.binding.SecondaryParameters;
import weblogic.wsee.security.wst.binding.SignWith;
import weblogic.wsee.security.wst.binding.TokenType;
import weblogic.wsee.security.wst.internal.v13.WSTConstants;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.policy.ClaimsBuilder;
import weblogic.xml.dom.DOMProcessingException;

public class SAMLIssuedTokenHelper {
   public static final String ISSUED_TOKEN_POLICY = "IssuedTokenPolicy";
   public static final String ISSUER_URI = "IssuerUri";
   public static final String REQ_INTERNAL_REFERENCE = "RequireInternalReference";
   public static final String REQ_EXTERNAL_REFERENCE = "RequireExternalReference";
   public static final String TRUST_VERSION = "TrustVersion";
   public static final QName ISSUED_TK_POLICY_QNAME = new QName("http://www.bea.com/wls90/security/policy", "IssuedTokenPolicy");
   public static final String TRUST_VERSOIN_10 = "http://schemas.xmlsoap.org/ws/2005/02/trust";
   public static final String TRUST_VERSOIN_13 = "http://docs.oasis-open.org/ws-sx/ws-trust/200512";
   public static final QName TRUST13_TOKEN_TYPE;
   public static final QName TRUST10_TOKEN_TYPE;
   public static final QName TRUST13_KEY_TYPE;
   public static final QName TRUST13_KEY_SIZE;
   public static final QName TRUST13_C14N_ALGO;
   public static final QName TRUST13_ENC_ALGO;
   public static final QName TRUST13_ENC_WITH;
   public static final QName TRUST13_SIGN_WITH;
   private static final boolean debug = false;
   private boolean requireInternalReference = false;
   private boolean requireExternalReference = false;
   private String issuerAddressUri = null;
   private String trustVersion = null;
   private Map templateMap;
   private String namespaceUri;

   public SAMLIssuedTokenHelper(Element var1) {
      if (null != var1) {
         this.issuerAddressUri = var1.getAttribute("IssuerUri");
         if ("".equals(this.issuerAddressUri)) {
            this.issuerAddressUri = null;
         }

         this.trustVersion = var1.getAttribute("TrustVersion");
         if ("".equals(this.trustVersion)) {
            this.trustVersion = null;
         }

         if ("true".equals(var1.getAttribute("RequireExternalReference"))) {
            this.requireExternalReference = true;
         }

         if ("true".equals(var1.getAttribute("RequireInternalReference"))) {
            this.requireInternalReference = true;
         }

         NodeList var2 = var1.getChildNodes();
         if (var2 != null) {
            this.templateMap = new HashMap();

            for(int var3 = 0; var3 < var2.getLength(); ++var3) {
               Node var4 = var2.item(var3);
               if (var4 instanceof Element) {
                  Element var5 = (Element)var4;
                  QName var6 = DOMUtils.getQName(var4);
                  if (this.namespaceUri == null) {
                     this.namespaceUri = var6.getNamespaceURI();
                  }

                  String var7 = DOMUtils.getText(var5);
                  this.templateMap.put(var6, var7);
               }
            }
         }

      }
   }

   public boolean isRequireInternalReference() {
      return this.requireInternalReference;
   }

   public boolean isRequireExternalReference() {
      return this.requireExternalReference;
   }

   public String getIssuerAddressUri() {
      return this.issuerAddressUri;
   }

   public String getTrustVersion() {
      return this.trustVersion;
   }

   public String getRequestSecurityTokenTemplateVale(QName var1) {
      return null == this.templateMap ? null : (String)this.templateMap.get(var1);
   }

   public String getTokenType() {
      String var1 = this.getRequestSecurityTokenTemplateVale(TRUST13_TOKEN_TYPE);
      return null == var1 ? this.getRequestSecurityTokenTemplateVale(TRUST10_TOKEN_TYPE) : var1;
   }

   public SecondaryParameters biuldSecondaryParameters() {
      SecondaryParameters var1 = new SecondaryParameters(this.namespaceUri);
      if (this.templateMap != null && !this.templateMap.isEmpty()) {
         String var2 = this.getTokenType();
         if (null != var2) {
            TokenType var3 = new TokenType(this.namespaceUri);
            var3.setTokenType(var2);
            var1.setTokenType(var3);
         }

         String var7 = this.getKeyType();
         if (null != var7) {
            KeyType var4 = new KeyType(this.namespaceUri);
            var4.setKeyType(var7);
            var1.setKeyType(var4);
         }

         String var8 = this.getRequestSecurityTokenTemplateVale(TRUST13_KEY_SIZE);
         if (null != var8) {
            KeySize var5 = new KeySize(this.namespaceUri);
            var5.setSize(Integer.parseInt(var8));
            var1.setKeySize(var5);
         }

         String var9 = this.getRequestSecurityTokenTemplateVale(TRUST13_C14N_ALGO);
         if (null != var9) {
            CanonicalizationAlgorithm var6 = new CanonicalizationAlgorithm(this.namespaceUri);
            var6.setUri(var9);
            var1.setCanonicalizationAlgorithm(var6);
         }

         var9 = this.getRequestSecurityTokenTemplateVale(TRUST13_ENC_ALGO);
         if (null != var9) {
            EncryptionAlgorithm var10 = new EncryptionAlgorithm(this.namespaceUri);
            var10.setUri(var9);
            var1.setEncryptionAlgorithm(var10);
         }

         var9 = this.getRequestSecurityTokenTemplateVale(TRUST13_ENC_WITH);
         if (null != var9) {
            EncryptWith var11 = new EncryptWith(this.namespaceUri);
            var11.setUri(var9);
            var1.setEncryptWith(var11);
         }

         var9 = this.getRequestSecurityTokenTemplateVale(TRUST13_SIGN_WITH);
         if (null != var9) {
            SignWith var12 = new SignWith(this.namespaceUri);
            var12.setUri(var9);
            var1.setSignWith(var12);
         }

         return var1;
      } else {
         return var1;
      }
   }

   public String getKeyType() {
      return this.getRequestSecurityTokenTemplateVale(TRUST13_KEY_TYPE);
   }

   public int getKeySize() {
      String var1 = this.getRequestSecurityTokenTemplateVale(TRUST13_KEY_SIZE);
      return null == var1 ? -1 : Integer.parseInt(var1);
   }

   public static Element makeIssuedTokenClaimElement(IssuedTokenAssertion var0) throws SecurityPolicyException {
      Element var1 = DKClaims.makeClaimsNode();
      return makeIssuedTokenClaimElement(var0, var1, (String)null);
   }

   public static Element makeIssuedTokenClaimElement(IssuedTokenAssertion var0, Node var1, String var2) {
      if (null != var1 && null != var0) {
         Element var3 = DOMUtils.createAndAddElement((Element)var1, ISSUED_TK_POLICY_QNAME, var1.getPrefix());
         String var4 = var0.getIssuerString();
         if (null != var4) {
            var3.setAttribute("IssuerUri", var4);
         }

         if (var0.isRequireExternalReference()) {
            var3.setAttribute("RequireExternalReference", "true");
         }

         if (var0.isRequireInternalReference()) {
            var3.setAttribute("RequireInternalReference", "true");
         }

         if (var0.hasRequestSecurityTokenTemplate()) {
            Map var5 = var0.getRequestSecurityTokenTemplate().getTemplateMap();
            Set var6 = var5.entrySet();
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               Map.Entry var8 = (Map.Entry)var7.next();
               Element var9 = DOMUtils.createAndAddElement(var3, (QName)var8.getKey(), var1.getPrefix());
               DOMUtils.addText(var9, (String)var8.getValue());
            }

            if (var5.get(TRUST13_TOKEN_TYPE) != null) {
               var2 = "http://docs.oasis-open.org/ws-sx/ws-trust/200512";
            } else if (var5.get(TRUST10_TOKEN_TYPE) != null) {
               var2 = "http://schemas.xmlsoap.org/ws/2005/02/trust";
            }
         }

         if (null != var2) {
            var3.setAttribute("TrustVersion", var2);
         }

         return var3;
      } else {
         return null;
      }
   }

   public static String getTrustVersionFromPolicy(GeneralPolicy var0) {
      String var1 = null;
      if (var0.hasTrustOptions()) {
         if (var0.getTrustOptions().isWst10()) {
            var1 = "http://schemas.xmlsoap.org/ws/2005/02/trust";
         } else {
            var1 = "http://schemas.xmlsoap.org/ws/2005/02/trust";
         }
      }

      return var1;
   }

   public static String getClaimFromChildElt(Node var0, String var1) {
      if (var0 != null && var0 instanceof Element) {
         try {
            Element var2 = weblogic.xml.dom.DOMUtils.getElementByTagName((Element)var0, var1);
            return null == var2 ? null : weblogic.xml.dom.DOMUtils.getTextContent(var2, true);
         } catch (DOMProcessingException var3) {
            return null;
         }
      } else {
         return null;
      }
   }

   public static String getKeyTypeFromClaims(Node var0) {
      return var0 == null ? null : getClaimFromChildElt(var0, "KeyType");
   }

   public static String getTrustKeyTypeFromIssuedTokenClaims(Node var0) {
      return var0 == null ? null : ClaimsBuilder.getClaimFromElt(var0, WSTConstants.T13_KEY_TYPE);
   }

   public static boolean isSymmetricKeyTypeFromIssuedTokenClaim(Node var0) {
      String var1 = getTrustKeyTypeFromIssuedTokenClaims(var0);
      return SAMLUtils.isSymmetricKeyType(var1);
   }

   private static String getValueFromFromIssuedTokenClaims(Node var0, QName var1, String var2) {
      if (var0 == null) {
         return var2;
      } else {
         String var3 = ClaimsBuilder.getClaimFromElt(var0, var1);
         return null == var3 ? var2 : var3;
      }
   }

   static {
      TRUST13_TOKEN_TYPE = WSTConstants.T13_TOKEN_TYPE;
      TRUST10_TOKEN_TYPE = new QName("http://schemas.xmlsoap.org/ws/2005/02/trust", "TokenType");
      TRUST13_KEY_TYPE = WSTConstants.T13_KEY_TYPE;
      TRUST13_KEY_SIZE = WSTConstants.T13_KEY_SIZE;
      TRUST13_C14N_ALGO = WSTConstants.T13_C14N_ALGO;
      TRUST13_ENC_ALGO = WSTConstants.T13_ENC_ALGO;
      TRUST13_ENC_WITH = WSTConstants.T13_ENC_WITH;
      TRUST13_SIGN_WITH = WSTConstants.T13_SIGN_WITH;
   }
}
