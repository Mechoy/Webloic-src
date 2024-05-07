package weblogic.wsee.security.wss.policy.wssp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.security.policy.EncryptionTarget;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;
import weblogic.wsee.security.wss.plan.fact.SecurityTokenFactory;
import weblogic.wsee.security.wss.plan.helper.SOAPSecurityHeaderHelper;
import weblogic.wsee.security.wss.policy.EncryptionPolicy;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wssp.QNameExpr;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.TBE;
import weblogic.xml.crypto.encrypt.api.dom.DOMTBEXML;
import weblogic.xml.crypto.wss.WSSecurityException;

public class EncryptionPolicyOutlineImpl extends EncryptionPolicyImpl implements EncryptionPolicy {
   public static final boolean verbose = Verbose.isVerbose(EncryptionPolicyOutlineImpl.class);
   static int sequnce = 0;

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

   private void doAddEncryptionTarget(EncryptionMethod var1, List var2, boolean var3) {
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
      Object var2 = null;
   }

   public void setKeyWrapMethod(String var1) throws SecurityPolicyArchitectureException {
      if (null == var1) {
         this.keyWrapMethod = null;
      }
   }

   public void setCanonicalizationMethod(String var1) throws SecurityPolicyArchitectureException {
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

   public void addEncryptionNodeList(SOAPMessageContext var1) throws WSSecurityException {
      if (!this.nodeMap.isEmpty()) {
         this.doAddEncryptionNodeList(var1);
      }
   }

   private void doAddEncryptionNodeList(SOAPMessageContext var1) throws WSSecurityException {
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
               this.addEncryptionTarget(this.encryptionMethod, var7, true);
            }
         }
      }

      this.nodeMap = new HashMap();
   }
}
