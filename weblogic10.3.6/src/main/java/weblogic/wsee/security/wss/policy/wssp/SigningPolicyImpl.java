package weblogic.wsee.security.wss.policy.wssp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Node;
import weblogic.wsee.security.policy.SecurityToken;
import weblogic.wsee.security.policy.assertions.xbeans.MessagePartsType;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;
import weblogic.wsee.security.policy12.assertions.XPath;
import weblogic.wsee.security.wss.plan.fact.SecurityTokenFactory;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wssp.QNameExpr;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.dsig.api.DigestMethod;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignatureMethod;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.wss.WSSecurityException;

public abstract class SigningPolicyImpl {
   private static final boolean verbose = Verbose.isVerbose(SigningPolicyImpl.class);
   private static final boolean debug = false;
   protected List validSignatureTokens = new ArrayList();
   protected boolean isTokenProtection = false;
   protected boolean includeSigningTokens = false;
   protected boolean X509AuthConditional = false;
   protected List references = new ArrayList();
   protected SignatureMethod signatureMethod = null;
   protected CanonicalizationMethod canonicalizationMethod = null;
   protected DigestMethod digestMethod = null;
   protected Map signingNodeMap = new HashMap();

   public void addSignatureTokens(SecurityTokenType[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.addSignatureToken(var1[var2]);
      }

   }

   public void addSignatureToken(SecurityTokenType var1) {
      SecurityToken var2 = SecurityTokenFactory.makeSecurityToken(var1);
      this.addSignatureToken(var2);
   }

   public void addSignatureToken(SecurityToken var1) {
      this.validSignatureTokens.add(var1);
   }

   public void setValidSignatureTokens(List var1) {
      this.validSignatureTokens = var1;
   }

   public List getValidSignatureTokens() {
      return this.validSignatureTokens;
   }

   public abstract void setSignatureMethod(String var1) throws SecurityPolicyArchitectureException;

   public void setSignatureMethod(SignatureMethod var1) {
      this.signatureMethod = var1;
   }

   public SignatureMethod getSignatureMethod() {
      return this.signatureMethod;
   }

   public abstract void setCanonicalizationMethod(String var1) throws SecurityPolicyArchitectureException;

   public void setCanonicalizationMethod(CanonicalizationMethod var1) {
      this.canonicalizationMethod = var1;
   }

   public CanonicalizationMethod getCanonicalizationMethod() {
      return this.canonicalizationMethod;
   }

   public abstract void setDigestMethod(String var1) throws SecurityPolicyArchitectureException;

   public void setDigestMethod(DigestMethod var1) {
      this.digestMethod = var1;
   }

   public DigestMethod getDigestMethod() {
      return this.digestMethod;
   }

   public void setTokenProtection(boolean var1) {
      this.isTokenProtection = var1;
   }

   public boolean signedSecurityTokens() {
      return this.isTokenProtection;
   }

   public void setIncludeSigningTokens(boolean var1) {
      this.includeSigningTokens = var1;
   }

   public boolean isIncludeSigningTokens() {
      return this.includeSigningTokens;
   }

   public void addSignatureNode(String var1, Node var2) {
      if (this.signingNodeMap.containsKey(var1)) {
         Object var3 = this.signingNodeMap.get(var1);
         if (null == var2 && null != var3) {
            return;
         }
      }

      this.signingNodeMap.put(var1, var2);
   }

   public void addSignatureReference(String var1, Reference var2) {
      if (this.signingNodeMap.containsKey(var1)) {
         Object var3 = this.signingNodeMap.get(var1);
         if (null == var2 && null != var3) {
            return;
         }
      }

      this.signingNodeMap.put(var1, var2);
   }

   public Map getSigningNodeMap() {
      return this.signingNodeMap;
   }

   public void setSigningNodeMap(Map var1) {
      this.signingNodeMap = var1;
   }

   public void addQNameExprNode(String var1, QNameExpr var2) {
      if (var2 != null && var1 != null) {
         if (!this.signingNodeMap.containsKey(var1)) {
            this.signingNodeMap.put(var1, var2);
         } else {
            this.signingNodeMap.put(var1 + SigningPolicyBlueprintImpl.sequnce++, var2);
         }

      } else {
         throw new IllegalArgumentException("null arg received");
      }
   }

   public void addXPathFilter2NodeList(String var1, List<XPath> var2) {
      assert var1 != null && var2 != null;

      this.signingNodeMap.put(var1 + SigningPolicyBlueprintImpl.sequnce++, var2);
   }

   public void addXPathNode(String var1, MessagePartsType var2) {
      if (var2 != null && var1 != null) {
         if (!this.signingNodeMap.containsKey(var1)) {
            this.signingNodeMap.put(var1, var2);
         } else {
            ArrayList var3 = new ArrayList();
            Object var4 = this.signingNodeMap.get(var1);
            if (var4 instanceof List) {
               var3.addAll((List)var4);
            } else {
               var3.add(var4);
            }

            var3.add(var2);
            this.signingNodeMap.put(var1, var3);
         }

      } else {
         throw new IllegalArgumentException("null arg received");
      }
   }

   public void addXPathNode(String var1, XPath var2) {
      if (var2 != null && var1 != null) {
         if (!this.signingNodeMap.containsKey(var1)) {
            this.signingNodeMap.put(var1, var2);
         } else {
            ArrayList var3 = new ArrayList();
            Object var4 = this.signingNodeMap.get(var1);
            if (var4 instanceof List) {
               var3.addAll((List)var4);
            } else {
               var3.add(var4);
            }

            var3.add(var2);
            this.signingNodeMap.put(var1, var3);
         }

      } else {
         throw new IllegalArgumentException("null arg received");
      }
   }

   public void addReferences(List var1) {
      this.references.addAll(var1);
   }

   public abstract List getReferences() throws SecurityPolicyArchitectureException, WSSecurityException;

   public void setX509AuthConditional(boolean var1) {
      this.X509AuthConditional = var1;
   }

   public boolean isX509AuthConditional() {
      return this.X509AuthConditional;
   }

   public boolean hasSignatureToken() {
      return this.validSignatureTokens != null && !this.validSignatureTokens.isEmpty();
   }

   public boolean isSignatureRequired() {
      return this.hasSignatureToken() && 0 != this.signingNodeMap.size() + this.references.size();
   }

   public String getDerivedFromTokenType() {
      if (this.validSignatureTokens.size() == 0) {
         return null;
      } else {
         List var1 = this.getValidSignatureTokens();
         Iterator var2 = var1.iterator();

         SecurityToken var3;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            var3 = (SecurityToken)var2.next();
         } while(var3.getDerivedFromTokenType() == null);

         return var3.getDerivedFromTokenType();
      }
   }

   public boolean isPolicyValid() {
      if (0 == this.signingNodeMap.size() + this.references.size()) {
         return true;
      } else {
         return this.validSignatureTokens.size() > 0;
      }
   }

   public SignedInfo newSignedInfo(XMLSignatureFactory var1, Reference var2) {
      ArrayList var3 = new ArrayList(this.references);
      var3.add(var2);
      return var1.newSignedInfo(this.canonicalizationMethod, this.signatureMethod, var3);
   }
}
