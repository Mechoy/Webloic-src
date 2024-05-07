package weblogic.wsee.security.wss.policy.wssp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Node;
import weblogic.wsee.security.policy.EncryptionTarget;
import weblogic.wsee.security.policy.SecurityToken;
import weblogic.wsee.security.policy.assertions.xbeans.MessagePartsType;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;
import weblogic.wsee.security.policy12.assertions.XPath;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;

public abstract class EncryptionPolicyImpl {
   private static final boolean verbose = Verbose.isVerbose(EncryptionPolicyImpl.class);
   private static final boolean debug = false;
   protected EncryptionMethod keyWrapMethod = null;
   protected EncryptionMethod encryptionMethod = null;
   protected CanonicalizationMethod canonicalizationMethod;
   protected List<SecurityToken> validEncryptionTokens = new ArrayList();
   protected List<EncryptionTarget> encryptionTargets = new ArrayList();
   protected Map nodeMap = new HashMap();

   public List<EncryptionTarget> getEncryptionTargets() {
      return this.encryptionTargets;
   }

   public void addEncryptionToken(SecurityToken var1) {
      this.validEncryptionTokens.add(var1);
   }

   public EncryptionMethod getEncryptionMethod() {
      return this.encryptionMethod;
   }

   public void setEncryptionMethod(EncryptionMethod var1) {
      this.encryptionMethod = var1;
   }

   public abstract void setEncryptionMethod(String var1) throws SecurityPolicyArchitectureException;

   public abstract void setKeyWrapMethod(String var1) throws SecurityPolicyArchitectureException;

   public void setKeyWrapMethod(EncryptionMethod var1) {
      this.keyWrapMethod = var1;
   }

   public EncryptionMethod getKeyWrapMethod() {
      return this.keyWrapMethod;
   }

   public abstract void setCanonicalizationMethod(String var1) throws SecurityPolicyArchitectureException;

   public void setCanonicalizationMethod(CanonicalizationMethod var1) {
      this.canonicalizationMethod = var1;
   }

   public CanonicalizationMethod getCanonicalizationMethod() {
      return this.canonicalizationMethod;
   }

   public List getValidEncryptionTokens() {
      return this.validEncryptionTokens;
   }

   public void addXPathNode(String var1, MessagePartsType var2) {
      if (var2 != null && var1 != null) {
         if (!this.nodeMap.containsKey(var1)) {
            this.nodeMap.put(var1, var2);
         } else {
            ArrayList var3 = new ArrayList();
            Object var4 = this.nodeMap.get(var1);
            if (var4 instanceof List) {
               var3.addAll((List)var4);
            } else {
               var3.add(var4);
            }

            var3.add(var2);
            this.nodeMap.put(var1, var3);
         }

      } else {
         throw new IllegalArgumentException("null arg received");
      }
   }

   public void addXPathNode(String var1, XPath var2) {
      if (var2 != null && var1 != null) {
         if (!this.nodeMap.containsKey(var1)) {
            this.nodeMap.put(var1, var2);
         } else {
            ArrayList var3 = new ArrayList();
            Object var4 = this.nodeMap.get(var1);
            if (var4 instanceof List) {
               var3.addAll((List)var4);
            } else {
               var3.add(var4);
            }

            var3.add(var2);
            this.nodeMap.put(var1, var3);
         }

      } else {
         throw new IllegalArgumentException("null arg received");
      }
   }

   public void addNode(String var1, Node var2) {
      if (this.nodeMap.containsKey(var1)) {
         Object var3 = this.nodeMap.get(var1);
         if (null == var2 && null != var3) {
            return;
         }
      }

      this.nodeMap.put(var1, var2);
   }

   public Map getNodeMap() {
      return this.nodeMap;
   }

   public boolean isEncryptionRequired() {
      return this.validEncryptionTokens.size() > 0 && 0 != this.nodeMap.size() + this.encryptionTargets.size();
   }

   public boolean isPolicyValid() {
      if (0 == this.nodeMap.size() + this.encryptionTargets.size()) {
         return true;
      } else {
         return this.validEncryptionTokens.size() > 0;
      }
   }

   public void addEncryptionTokens(SecurityTokenType[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.addEncryptionToken(var1[var2]);
      }

   }

   public abstract void addEncryptionToken(SecurityTokenType var1);

   public void setCanonicalizationMethod() throws SecurityPolicyArchitectureException {
      this.setCanonicalizationMethod("http://www.w3.org/2001/10/xml-exc-c14n#");
   }

   public void setNodeMap(Map var1) {
      this.nodeMap = var1;
   }
}
