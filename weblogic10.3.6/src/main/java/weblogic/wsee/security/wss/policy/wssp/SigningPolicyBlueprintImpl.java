package weblogic.wsee.security.wss.policy.wssp;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.policy.SigningReferencesFactory;
import weblogic.wsee.security.policy.XBeanUtils;
import weblogic.wsee.security.policy.assertions.IntegrityAssertion;
import weblogic.wsee.security.policy.assertions.xbeans.IntegrityDocument;
import weblogic.wsee.security.policy.assertions.xbeans.IntegrityTargetType;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;
import weblogic.wsee.security.policy.assertions.xbeans.TransformType;
import weblogic.wsee.security.policy12.assertions.SignedElements;
import weblogic.wsee.security.policy12.assertions.XPath;
import weblogic.wsee.security.policy12.assertions.XPath2;
import weblogic.wsee.security.wss.SecurityPolicyException;
import weblogic.wsee.security.wss.plan.helper.SOAPSecurityHeaderHelper;
import weblogic.wsee.security.wss.plan.helper.XpathNodesHelper;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wss.policy.SignaturePolicy;
import weblogic.wsee.security.wssp.QNameExpr;
import weblogic.xml.crypto.dsig.XPathFilter2Transform;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.dsig.api.DigestMethod;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignatureMethod;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.Transform;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.dsig.api.spec.C14NMethodParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.DigestMethodParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.SignatureMethodParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.TransformParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.XPathFilter2ParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.XPathFilterParameterSpec;
import weblogic.xml.crypto.wss.WSSecurityException;

public class SigningPolicyBlueprintImpl extends SigningPolicyImpl implements SignaturePolicy {
   static int sequnce = 0;
   private XMLSignatureFactory signatureFactory;
   private SigningReferencesFactory signingReferencesFactory;
   public static final String XPATH_TXFORM_URI = "http://www.w3.org/TR/1999/REC-xpath-19991116";
   public static final QName XPATH_FILTER_ELEMENT = new QName("http://www.w3.org/TR/1999/REC-xpath-19991116", "XPath");

   public SigningPolicyBlueprintImpl(XMLSignatureFactory var1, SigningReferencesFactory var2) {
      this.signatureFactory = var1;
      this.signingReferencesFactory = var2;
   }

   public SigningPolicyBlueprintImpl(XMLSignatureFactory var1, SigningReferencesFactory var2, SOAPMessageContext var3, Set var4) throws PolicyException, SecurityPolicyException, WSSecurityException {
      this.signatureFactory = var1;
      this.signingReferencesFactory = var2;
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
         this.addSignatureTokens(var9);
         IntegrityTargetType[] var10 = var8.getTargetArray();

         for(int var11 = 0; var11 < var10.length; ++var11) {
            IntegrityTargetType var12 = var10[var11];
            List var13 = null;
            DigestMethod var14 = null;

            try {
               var13 = processSigningTransforms(var1, var12);
               String var15 = var12.getDigestAlgorithm().getURI();
               var14 = var1.newDigestMethod(var15, (DigestMethodParameterSpec)null);
            } catch (NoSuchAlgorithmException var16) {
               throw new SecurityPolicyException(var16.getMessage(), var16);
            } catch (InvalidAlgorithmParameterException var17) {
               throw new SecurityPolicyException(var17.getMessage(), var17);
            }

            List var20 = var2.getSigningReferences(var1, var12.getMessageParts(), var14, var13, var3, var7);
            this.references.addAll(var20);
         }

         String var18 = var8.getCanonicalizationAlgorithm().getURI();
         this.setCanonicalizationMethod(var18);
         String var19 = var8.getSignatureAlgorithm().getURI();
         this.setSignatureMethod(var19);
      }

   }

   public void setSignatureMethod(String var1) throws SecurityPolicyArchitectureException {
      SignatureMethod var2 = null;

      try {
         var2 = this.signatureFactory.newSignatureMethod(var1, (SignatureMethodParameterSpec)null);
         this.signatureMethod = var2;
      } catch (NoSuchAlgorithmException var4) {
         throw new SecurityPolicyArchitectureException(var4.getMessage(), var4);
      } catch (InvalidAlgorithmParameterException var5) {
         throw new SecurityPolicyArchitectureException(var5.getMessage(), var5);
      }
   }

   public void setCanonicalizationMethod(String var1) throws SecurityPolicyArchitectureException {
      CanonicalizationMethod var2 = null;

      try {
         var2 = this.signatureFactory.newCanonicalizationMethod(var1, (C14NMethodParameterSpec)null);
         this.canonicalizationMethod = var2;
      } catch (NoSuchAlgorithmException var4) {
         throw new SecurityPolicyArchitectureException(var4.getMessage(), var4);
      } catch (InvalidAlgorithmParameterException var5) {
         throw new SecurityPolicyArchitectureException(var5.getMessage(), var5);
      }
   }

   public void setDigestMethod(String var1) throws SecurityPolicyArchitectureException {
      try {
         this.digestMethod = this.signatureFactory.newDigestMethod(var1, (DigestMethodParameterSpec)null);
      } catch (NoSuchAlgorithmException var3) {
         throw new SecurityPolicyArchitectureException(var3.getMessage(), var3);
      } catch (InvalidAlgorithmParameterException var4) {
         throw new SecurityPolicyArchitectureException(var4.getMessage(), var4);
      }
   }

   public SignedInfo newSignedInfo(Reference var1) {
      ArrayList var2 = new ArrayList(this.references);
      var2.add(var1);
      return this.signatureFactory.newSignedInfo(this.canonicalizationMethod, this.signatureMethod, var2);
   }

   public SignedInfo getSignedInfo() {
      this.removeDuplicatedReference();
      return this.signatureFactory.newSignedInfo(this.canonicalizationMethod, this.signatureMethod, this.references);
   }

   private void removeDuplicatedReference() {
      if (null != this.references && this.references.size() >= 2) {
         HashMap var1 = new HashMap(this.references.size());

         for(int var2 = 0; var2 < this.references.size(); ++var2) {
            var1.put(((Reference)this.references.get(var2)).getURI(), this.references.get(var2));
         }

         if (var1.size() != this.references.size()) {
            Collection var3 = var1.values();
            this.references = new ArrayList(var3);
         }
      }
   }

   public void addSignatureNodeListToReference(SOAPMessageContext var1) throws SecurityPolicyArchitectureException, WSSecurityException {
      if (!this.signingNodeMap.isEmpty()) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();
         Collection var4 = this.signingNodeMap.values();
         var4.remove((Object)null);
         var2.addAll(var4);

         for(int var5 = 0; var5 < var2.size(); ++var5) {
            Object var6 = var2.get(var5);
            if (var6 != null) {
               List var8;
               if (var6 instanceof QNameExpr) {
                  SOAPMessage var7 = var1.getMessage();
                  var8 = SOAPSecurityHeaderHelper.getNonSecurityElements(var7, (QNameExpr)var6);
                  this.addSignatureNodeListToReference(var8);
               } else if (var6 instanceof Node) {
                  var3.add(var6);
               } else if (var6 instanceof List) {
                  Object var9 = ((List)var6).get(0);
                  if (var9 instanceof XPath2) {
                     this.addXPathFilter2Reference((List)var6, var1);
                  } else if (var9 instanceof XPath) {
                     var8 = XpathNodesHelper.findNode((List)var6, var1, false);
                     if (null != var8 && var8.size() > 0) {
                        this.addSignatureNodeListToReference(var8);
                     }
                  }
               } else if (var6 instanceof XPath2) {
                  this.addXPathFilter2Reference((XPath2)var6, var1);
               } else if (var6 instanceof XPath) {
                  List var10 = XpathNodesHelper.findNode((XPath)var6, var1, false);
                  if (null != var10 && var10.size() > 0) {
                     this.addSignatureNodeListToReference(var10);
                  }
               } else {
                  if (!(var6 instanceof Reference)) {
                     throw new SecurityPolicyArchitectureException("Unknown object type found in signature node list");
                  }

                  this.addReferences(Arrays.asList((Reference)var6));
               }
            }
         }

         SignedElements.isValidElement((List)var3);
         this.addSignatureNodeListToReference((List)var3);
      }
   }

   private void addXPathFilter2Reference(List<XPath2> var1, SOAPMessageContext var2) throws SecurityPolicyArchitectureException {
      ArrayList var3 = new ArrayList();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         XPath2 var5 = (XPath2)var4.next();

         assert "http://www.w3.org/2002/06/xmldsig-filter2".equals(var5.getXPathVersion());

         weblogic.xml.crypto.dsig.api.spec.XPath var6 = convertToSpecXPath(var5);
         var3.add(var6);
      }

      XPathFilter2ParameterSpec var12 = new XPathFilter2ParameterSpec(var3);
      ArrayList var13 = new ArrayList();

      try {
         Transform var14 = this.signatureFactory.newTransform("http://www.w3.org/2002/06/xmldsig-filter2", var12);
         var13.add(var14);
      } catch (NoSuchAlgorithmException var10) {
         throw new SecurityPolicyArchitectureException(var10.getMessage(), var10);
      } catch (InvalidAlgorithmParameterException var11) {
         throw new SecurityPolicyArchitectureException(var11.getMessage(), var11);
      }

      ArrayList var15 = new ArrayList();
      var15.add(var2.getMessage().getSOAPPart().getDocumentElement());

      try {
         List var7 = this.signingReferencesFactory.getSigningReferences(this.signatureFactory, var15, this.digestMethod, var13);
         this.addReferences(var7);
      } catch (WSSecurityException var8) {
         throw new SecurityPolicyArchitectureException(var8.getMessage(), var8);
      } catch (PolicyException var9) {
         throw new SecurityPolicyArchitectureException(var9.getMessage(), var9);
      }
   }

   private static weblogic.xml.crypto.dsig.api.spec.XPath convertToSpecXPath(XPath2 var0) {
      return new weblogic.xml.crypto.dsig.api.spec.XPath(var0.getXPathExpr(), XPathFilter2Transform.getFilter(var0.getFilter()), var0.getXPathNamespaces());
   }

   private void addXPathFilter2Reference(XPath2 var1, SOAPMessageContext var2) throws SecurityPolicyArchitectureException {
      ArrayList var3 = new ArrayList();
      var3.add(var1);
      this.addXPathFilter2Reference((List)var3, var2);
   }

   public void addSignatureNodeListToReference() throws SecurityPolicyArchitectureException, WSSecurityException {
      if (!this.signingNodeMap.isEmpty()) {
         ArrayList var1 = new ArrayList();
         Collection var2 = this.signingNodeMap.values();
         var2.remove((Object)null);
         var1.addAll(var2);
         this.addSignatureNodeListToReference((List)var1);
      }
   }

   public void addSignatureNodeListToReference(List var1) throws SecurityPolicyArchitectureException, WSSecurityException {
      if (null != var1 && var1.size() != 0) {
         try {
            List var2 = this.signingReferencesFactory.getSigningReferences(this.signatureFactory, var1, this.digestMethod, this.getSigningTransforms());
            this.addReferences(var2);
         } catch (PolicyException var3) {
            throw new SecurityPolicyArchitectureException(var3.getMessage(), var3);
         }
      }
   }

   public void setNewSignatureNodeListToReference(List var1) throws SecurityPolicyArchitectureException, WSSecurityException {
      try {
         this.references = this.signingReferencesFactory.getSigningReferences(this.signatureFactory, var1, this.digestMethod, this.getSigningTransforms());
      } catch (PolicyException var3) {
         throw new SecurityPolicyArchitectureException(var3.getMessage(), var3);
      }
   }

   public List getReferences() throws SecurityPolicyArchitectureException, WSSecurityException {
      this.addSignatureNodeListToReference();
      return this.references;
   }

   private List getSigningTransforms() throws SecurityPolicyArchitectureException {
      ArrayList var1 = new ArrayList();

      try {
         var1.add(this.signatureFactory.newTransform("http://www.w3.org/2001/10/xml-exc-c14n#", (TransformParameterSpec)null));
         return var1;
      } catch (NoSuchAlgorithmException var3) {
         throw new SecurityPolicyArchitectureException(var3);
      } catch (InvalidAlgorithmParameterException var4) {
         throw new SecurityPolicyArchitectureException(var4);
      }
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
