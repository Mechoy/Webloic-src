package weblogic.wsee.security.wss.policy.wssp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Node;
import weblogic.wsee.security.wss.plan.helper.SOAPSecurityHeaderHelper;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wss.policy.SignaturePolicy;
import weblogic.wsee.security.wssp.QNameExpr;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.wss.WSSecurityException;

public class SigningPolicyOutlineImpl extends SigningPolicyImpl implements SignaturePolicy {
   static int sequnce = 0;

   public void setSignatureMethod(String var1) throws SecurityPolicyArchitectureException {
   }

   public void setCanonicalizationMethod(String var1) throws SecurityPolicyArchitectureException {
      Object var2 = null;
   }

   public void setDigestMethod(String var1) throws SecurityPolicyArchitectureException {
   }

   public SignedInfo newSignedInfo(Reference var1) {
      return null;
   }

   public SignedInfo getSignedInfo() {
      return null;
   }

   public void addSignatureNodeListToReference(SOAPMessageContext var1) throws SecurityPolicyArchitectureException, WSSecurityException {
      if (!this.signingNodeMap.isEmpty()) {
         this.doAddSignatureNodeListToReference(var1);
      }
   }

   private void doAddSignatureNodeListToReference(SOAPMessageContext var1) throws SecurityPolicyArchitectureException, WSSecurityException {
      ArrayList var2 = new ArrayList();
      ArrayList var3 = new ArrayList();
      Collection var4 = this.signingNodeMap.values();
      var4.remove((Object)null);
      var2.addAll(var4);

      for(int var5 = 0; var5 < var2.size(); ++var5) {
         Object var6 = var2.get(var5);
         if (var6 != null) {
            if (var6 instanceof QNameExpr) {
               SOAPMessage var7 = var1.getMessage();
               List var8 = SOAPSecurityHeaderHelper.getNonSecurityElements(var7, (QNameExpr)var6);
               this.addSignatureNodeListToReference(var8);
            } else if (var6 instanceof Node) {
               var3.add(var6);
            }
         }
      }

      this.addSignatureNodeListToReference((List)var3);
   }

   public void addSignatureNodeListToReference() throws SecurityPolicyArchitectureException, WSSecurityException {
      if (!this.signingNodeMap.isEmpty()) {
         this.doAddSignatureNodeListToReference();
      }
   }

   private void doAddSignatureNodeListToReference() throws SecurityPolicyArchitectureException, WSSecurityException {
      ArrayList var1 = new ArrayList();
      Collection var2 = this.signingNodeMap.values();
      var2.remove((Object)null);
      var1.addAll(var2);
      this.addSignatureNodeListToReference((List)var1);
   }

   public void addSignatureNodeListToReference(List var1) throws SecurityPolicyArchitectureException, WSSecurityException {
   }

   public void setNewSignatureNodeListToReference(List var1) throws SecurityPolicyArchitectureException, WSSecurityException {
   }

   public List getReferences() throws SecurityPolicyArchitectureException, WSSecurityException {
      this.addSignatureNodeListToReference();
      return this.references;
   }
}
