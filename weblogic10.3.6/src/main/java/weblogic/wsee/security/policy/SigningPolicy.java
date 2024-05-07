package weblogic.wsee.security.policy;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.policy.assertions.IntegrityAssertion;
import weblogic.wsee.security.policy.assertions.xbeans.IntegrityDocument;
import weblogic.wsee.security.policy.assertions.xbeans.IntegrityTargetType;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;
import weblogic.wsee.security.policy.assertions.xbeans.TransformType;
import weblogic.wsee.security.wss.SecurityPolicyException;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.dsig.api.DigestMethod;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignatureMethod;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.dsig.api.spec.C14NMethodParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.DigestMethodParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.SignatureMethodParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.TransformParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.XPathFilterParameterSpec;
import weblogic.xml.crypto.wss.WSSecurityException;

public class SigningPolicy {
   private List validSignatureTokens = new ArrayList();
   private SignedInfo signedInfo;
   private boolean includeSigningTokens = false;
   private boolean X509AuthConditional = false;
   private List references = new ArrayList();
   private SignatureMethod sigMethod = null;
   private CanonicalizationMethod cMethod = null;
   private String digestAlgorithm = null;
   public static final String XPATH_TXFORM_URI = "http://www.w3.org/TR/1999/REC-xpath-19991116";
   public static final QName XPATH_FILTER_ELEMENT = new QName("http://www.w3.org/TR/1999/REC-xpath-19991116", "XPath");

   public SigningPolicy(XMLSignatureFactory var1, SigningReferencesFactory var2, SOAPMessageContext var3, Set var4) throws PolicyException, SecurityPolicyException, WSSecurityException {
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         IntegrityAssertion var6 = (IntegrityAssertion)var5.next();
         Map var7 = var6.getNamespaceMap();
         IntegrityDocument.Integrity var8 = var6.getXbean().getIntegrity();
         this.includeSigningTokens |= var8.getSignToken();
         this.X509AuthConditional |= var8.getX509AuthConditional();
         if (!var8.isSetSupportedTokens()) {
            throw new PolicyException("Supportedtokens element is missing from Integrity assertion");
         }

         SecurityTokenType[] var9 = var8.getSupportedTokens().getSecurityTokenArray();

         SecurityToken var12;
         for(int var10 = 0; var10 < var9.length; ++var10) {
            SecurityTokenType var11 = var9[var10];
            var12 = new SecurityToken(XBeanUtils.getElement(var11), (String)null, var11.getTokenType(), var11.getIncludeInMessage());
            var12.setDerivedFromTokenType(var11.getDerivedFromTokenType());
            this.validSignatureTokens.add(var12);
         }

         IntegrityTargetType[] var22 = var8.getTargetArray();

         DigestMethod var14;
         for(int var23 = 0; var23 < var22.length; ++var23) {
            IntegrityTargetType var25 = var22[var23];
            List var13 = null;
            var14 = null;

            try {
               var13 = processSigningTransforms(var1, var25);
               String var15 = var25.getDigestAlgorithm().getURI();
               this.digestAlgorithm = var15;
               var14 = var1.newDigestMethod(var15, (DigestMethodParameterSpec)null);
            } catch (NoSuchAlgorithmException var18) {
               throw new SecurityPolicyException(var18.getMessage(), var18);
            } catch (InvalidAlgorithmParameterException var19) {
               throw new SecurityPolicyException(var19.getMessage(), var19);
            }

            List var29 = var2.getSigningReferences(var1, var25.getMessageParts(), var14, var13, var3, var7);
            this.references.addAll(var29);
         }

         String var24 = var8.getCanonicalizationAlgorithm().getURI();
         var12 = null;

         try {
            CanonicalizationMethod var26 = var1.newCanonicalizationMethod(var24, (C14NMethodParameterSpec)null);
            if (this.cMethod == null) {
               this.cMethod = var26;
            }
         } catch (NoSuchAlgorithmException var20) {
            throw new WSSecurityException(var20.getMessage(), var20);
         } catch (InvalidAlgorithmParameterException var21) {
            throw new WSSecurityException(var21.getMessage(), var21);
         }

         String var27 = var8.getSignatureAlgorithm().getURI();
         var14 = null;

         try {
            SignatureMethod var28 = var1.newSignatureMethod(var27, (SignatureMethodParameterSpec)null);
            if (this.sigMethod == null) {
               this.sigMethod = var28;
            }
         } catch (NoSuchAlgorithmException var16) {
            throw new WSSecurityException(var16.getMessage(), var16);
         } catch (InvalidAlgorithmParameterException var17) {
            throw new WSSecurityException(var17.getMessage(), var17);
         }
      }

      this.signedInfo = var1.newSignedInfo(this.cMethod, this.sigMethod, this.references);
   }

   public SignedInfo newSignedInfo(XMLSignatureFactory var1, Reference var2) {
      ArrayList var3 = new ArrayList(this.references);
      var3.add(var2);
      return var1.newSignedInfo(this.cMethod, this.sigMethod, var3);
   }

   public SignedInfo getSignedInfo() {
      return this.signedInfo;
   }

   public List getValidSignatureTokens() {
      return this.validSignatureTokens;
   }

   public boolean signedSecurityTokens() {
      return this.includeSigningTokens;
   }

   public boolean isX509AuthConditional() {
      return this.X509AuthConditional;
   }

   private static List processSigningTransforms(XMLSignatureFactory var0, IntegrityTargetType var1) throws SecurityPolicyException {
      ArrayList var2 = new ArrayList();
      TransformType[] var3 = var1.getTransformArray();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         TransformType var5 = var3[var4];
         String var6 = DOMUtils.getAttributeValueAsString(XBeanUtils.getElement(var5), new QName("URI"));
         if (var6 == null) {
            throw new SecurityPolicyException("Could not read Transform URI from Transform element");
         }

         Object var7 = null;
         DocumentFragment var8 = XBeanUtils.getXMLBeanChildren(var5);
         if ("http://www.w3.org/TR/1999/REC-xpath-19991116".equals(var6)) {
            var7 = createXPathFilterSpec(var8);
            if (var7 == null) {
               throw new SecurityPolicyException("No XPath transform parameter.");
            }
         } else {
            var7 = new DOMParameterSpec(var8);
         }

         try {
            var2.add(var0.newTransform(var6, (TransformParameterSpec)var7));
         } catch (NoSuchAlgorithmException var12) {
            throw new SecurityPolicyException(var12);
         } catch (InvalidAlgorithmParameterException var13) {
            throw new SecurityPolicyException(var13);
         }
      }

      if (var2.size() == 0) {
         try {
            var2.add(var0.newTransform("http://www.w3.org/2001/10/xml-exc-c14n#", (TransformParameterSpec)null));
         } catch (NoSuchAlgorithmException var10) {
         } catch (InvalidAlgorithmParameterException var11) {
         }
      }

      return var2;
   }

   public String getDigestAlgorithm() {
      return null != this.digestAlgorithm ? this.digestAlgorithm : "http://www.w3.org/2000/09/xmldsig#sha1";
   }

   private static XPathFilterParameterSpec createXPathFilterSpec(DocumentFragment var0) {
      NodeList var1 = var0.getChildNodes();
      if (var1 != null && var1.getLength() != 0) {
         Element var2 = (Element)var1.item(0);
         String var3 = DOMUtils.getTextContent(var2, true);
         return new XPathFilterParameterSpec(var3);
      } else {
         return null;
      }
   }

   private static class DOMParameterSpec implements TransformParameterSpec {
      private DocumentFragment fragment;

      public DOMParameterSpec(DocumentFragment var1) {
         this.fragment = var1;
      }

      public DocumentFragment getDocumentFragment() {
         return this.fragment;
      }
   }
}
