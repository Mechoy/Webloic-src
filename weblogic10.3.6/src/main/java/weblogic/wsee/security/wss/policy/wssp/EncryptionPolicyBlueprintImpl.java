package weblogic.wsee.security.wss.policy.wssp;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.policy.EncryptionTarget;
import weblogic.wsee.security.policy.MessagePartsEvaluator;
import weblogic.wsee.security.policy.assertions.ConfidentialityAssertion;
import weblogic.wsee.security.policy.assertions.xbeans.ConfidentialityDocument;
import weblogic.wsee.security.policy.assertions.xbeans.ConfidentialityTargetType;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;
import weblogic.wsee.security.policy12.assertions.EncryptedElements;
import weblogic.wsee.security.policy12.assertions.XPath;
import weblogic.wsee.security.wss.plan.fact.SecurityTokenFactory;
import weblogic.wsee.security.wss.plan.helper.SOAPSecurityHeaderHelper;
import weblogic.wsee.security.wss.plan.helper.XpathNodesHelper;
import weblogic.wsee.security.wss.policy.EncryptionPolicy;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wssp.QNameExpr;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.dsig.api.spec.C14NMethodParameterSpec;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.TBE;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionFactory;
import weblogic.xml.crypto.encrypt.api.dom.DOMTBEXML;
import weblogic.xml.crypto.encrypt.api.spec.EncryptionMethodParameterSpec;
import weblogic.xml.crypto.wss.WSSecurityException;

public class EncryptionPolicyBlueprintImpl extends EncryptionPolicyImpl implements EncryptionPolicy {
   public static final boolean verbose = Verbose.isVerbose(EncryptionPolicyBlueprintImpl.class);
   static int sequnce = 0;
   private XMLEncryptionFactory encryptionFactory;

   public EncryptionPolicyBlueprintImpl() {
   }

   public EncryptionPolicyBlueprintImpl(XMLEncryptionFactory var1) {
      this.encryptionFactory = var1;
   }

   /** @deprecated */
   public EncryptionPolicyBlueprintImpl(XMLEncryptionFactory var1, SOAPMessageContext var2, ConfidentialityAssertion var3, boolean var4) throws PolicyException, WSSecurityException {
      Map var5 = var3.getNamespaceMap();
      ConfidentialityDocument.Confidentiality var6 = var3.getXbean().getConfidentiality();
      String var7 = "http://www.w3.org/2001/10/xml-exc-c14n#";

      try {
         this.setCanonicalizationMethod(var7);
         if (var6.getKeyWrappingAlgorithm() != null) {
            this.setKeyWrapMethod(var6.getKeyWrappingAlgorithm().getURI());
         }
      } catch (SecurityPolicyArchitectureException var14) {
         throw new WSSecurityException(var14.getMessage(), var14);
      }

      ConfidentialityTargetType[] var8 = var6.getTargetArray();

      for(int var9 = 0; var9 < var8.length; ++var9) {
         ConfidentialityTargetType var10 = var8[var9];

         try {
            this.setEncryptionMethod(var10.getEncryptionAlgorithm().getURI());
         } catch (SecurityPolicyArchitectureException var13) {
            throw new WSSecurityException(var13.getMessage(), var13);
         }

         if (var4) {
            List var11 = null;
            MessagePartsEvaluator var12 = new MessagePartsEvaluator(var10.getMessageParts(), var2, var5);
            if (var10.getEncryptContentOnly()) {
               var11 = var12.getNodesContent();
            } else {
               var11 = var12.getNodes();
            }

            if (var11 != null && var11.size() != 0) {
               this.addEncryptionTarget(this.encryptionMethod, var11, var10.getEncryptContentOnly());
            } else if (verbose) {
               Verbose.log((Object)("MessageParts expression '" + var10.getMessageParts() + "' did not evaluate to any nodes in the message; target will be skipped"));
            }
         }
      }

      SecurityTokenType[] var15 = var6.getKeyInfo().getSecurityTokenArray();
      this.addEncryptionTokens(var15);
   }

   public void addEncryptionTarget(List var1, boolean var2) throws WSSecurityException {
      this.addEncryptionTarget(this.getEncryptionMethod(), var1, var2);
   }

   public void addEncryptionTarget(EncryptionMethod var1, List var2, boolean var3) {
      if (var2 != null && var2.size() != 0) {
         this.doAddEncryptionTarget(var1, var2, var3);
      } else {
         if (verbose) {
            Verbose.log((Object)"empty list found");
         }

      }
   }

   public void doAddEncryptionTarget(EncryptionMethod var1, List var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      Iterator var5;
      DOMTBEXML var7;
      if (var3) {
         var5 = var2.iterator();

         while(var5.hasNext()) {
            NodeList var6 = (NodeList)var5.next();
            var7 = new DOMTBEXML(var6, this.canonicalizationMethod);
            var4.add(var7);
         }
      } else {
         var5 = var2.iterator();

         while(var5.hasNext()) {
            Element var12 = (Element)var5.next();
            var7 = new DOMTBEXML(var12, this.canonicalizationMethod);
            var4.add(var7);
         }
      }

      boolean var11 = true;
      Iterator var13 = this.encryptionTargets.iterator();

      while(var13.hasNext()) {
         EncryptionTarget var14 = (EncryptionTarget)var13.next();
         if (var14.getEncryptionMethod().equals(var1)) {
            List var8 = var14.getTBEs();
            Iterator var9 = var4.iterator();

            while(var9.hasNext()) {
               TBE var10 = (TBE)var9.next();
               var8.add(var10);
            }

            var11 = false;
            break;
         }
      }

      if (var11) {
         this.encryptionTargets.add(new EncryptionTarget(var1, var4));
      }

   }

   public void addEncryptionToken(SecurityTokenType var1) {
      this.addEncryptionToken(SecurityTokenFactory.makeSecurityToken(var1));
   }

   public boolean hasEncryptionToken() {
      return this.validEncryptionTokens != null && !this.validEncryptionTokens.isEmpty();
   }

   public void setEncryptionMethod(String var1) throws SecurityPolicyArchitectureException {
      EncryptionMethod var2 = null;

      try {
         var2 = this.encryptionFactory.newEncryptionMethod(var1, (Integer)null, (EncryptionMethodParameterSpec)null);
         if (var2 == null) {
            throw new SecurityPolicyArchitectureException(this.encryptionFactory.toString() + " is not a supported encryption algorithm.");
         } else {
            this.encryptionMethod = var2;
         }
      } catch (InvalidAlgorithmParameterException var4) {
         throw new SecurityPolicyArchitectureException(var4.getMessage(), var4);
      }
   }

   public void setKeyWrapMethod(String var1) throws SecurityPolicyArchitectureException {
      if (null == var1) {
         this.keyWrapMethod = null;
      } else {
         try {
            this.keyWrapMethod = this.encryptionFactory.newEncryptionMethod(var1, (Integer)null, (EncryptionMethodParameterSpec)null);
         } catch (InvalidAlgorithmParameterException var3) {
            throw new SecurityPolicyArchitectureException(var3.getMessage(), var3);
         }
      }
   }

   public void setCanonicalizationMethod(String var1) throws SecurityPolicyArchitectureException {
      try {
         this.canonicalizationMethod = this.encryptionFactory.newCanonicalizationMethod(var1, (C14NMethodParameterSpec)null);
      } catch (InvalidAlgorithmParameterException var3) {
         throw new SecurityPolicyArchitectureException(var3.getMessage(), var3);
      } catch (NoSuchAlgorithmException var4) {
         throw new SecurityPolicyArchitectureException(var4.getMessage(), var4);
      }
   }

   public void setValidEncryptionTokens(List var1) {
      this.validEncryptionTokens = var1;
   }

   public void addQNameExprNode(String var1, QNameExpr var2) {
      if (var2 != null && var1 != null) {
         if (!this.nodeMap.containsKey(var1)) {
            this.nodeMap.put(var1, var2);
         } else {
            this.nodeMap.put(var1 + sequnce++, var2);
         }

      } else {
         throw new IllegalArgumentException("null arg received");
      }
   }

   public void addEncryptionNodeList(SOAPMessageContext var1) throws WSSecurityException, SecurityPolicyArchitectureException {
      if (!this.nodeMap.isEmpty()) {
         this.doAddEncryptionNodeList(var1);
      }
   }

   private void doAddEncryptionNodeList(SOAPMessageContext var1) throws WSSecurityException, SecurityPolicyArchitectureException {
      ArrayList var2 = new ArrayList();
      var2.addAll(this.nodeMap.values());

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         Object var4 = var2.get(var3);
         if (var4 != null) {
            if (var4 instanceof QNameExpr) {
               SOAPMessage var5 = var1.getMessage();
               List var6 = SOAPSecurityHeaderHelper.getNonSecurityElements(var5, (QNameExpr)var4);
               this.addEncryptionTarget(this.encryptionMethod, var6, false);
            } else if (var4 instanceof Node) {
               ArrayList var7 = new ArrayList();
               var7.add(var4);
               Object var9 = this.nodeMap.get("Body");
               if (var4.equals(var9)) {
                  this.addEncryptionTarget(this.encryptionMethod, var7, true);
               } else {
                  this.addEncryptionTarget(this.encryptionMethod, var7, false);
               }
            } else {
               List var8;
               if (var4 instanceof List) {
                  var8 = XpathNodesHelper.findNode((List)var4, var1, false);
                  if (null != var8 && var8.size() > 0) {
                     XPath var10 = (XPath)((XPath)((List)var4).get(0));
                     EncryptedElements.isValidElement(var8, var10.getEncryptContentOnly());
                     this.addEncryptionTarget(this.encryptionMethod, var8, var10.getEncryptContentOnly());
                  }
               } else {
                  if (!(var4 instanceof XPath)) {
                     throw new SecurityPolicyArchitectureException("Unknown object type found in encryption node list");
                  }

                  var8 = XpathNodesHelper.findNode((XPath)var4, var1, false);
                  if (null != var8 && var8.size() > 0) {
                     EncryptedElements.isValidElement(var8, ((XPath)var4).getEncryptContentOnly());
                     this.addEncryptionTarget(this.encryptionMethod, var8, ((XPath)var4).getEncryptContentOnly());
                  }
               }
            }
         }
      }

      this.nodeMap = new HashMap();
   }
}
