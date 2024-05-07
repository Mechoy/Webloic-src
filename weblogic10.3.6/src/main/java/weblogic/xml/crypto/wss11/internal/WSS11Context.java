package weblogic.xml.crypto.wss11.internal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Node;
import weblogic.xml.crypto.dsig.api.XMLSignature;
import weblogic.xml.crypto.encrypt.Utils;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss11.internal.bst.BSTHandler;
import weblogic.xml.crypto.wss11.internal.enckey.EncryptedKeyToken;
import weblogic.xml.crypto.wss11.internal.enckey.EncryptedKeyTokenHandler;

public class WSS11Context extends WSSecurityContext {
   private static final long serialVersionUID = -8569410379759001216L;
   private static final String WSS11_PREVIOUS_MESSAGE_SIGNATURE_VALUES = "weblogic.xml.crypto.wss11.previousMessageSignatureValues";
   private transient boolean useSignatureConfirmation = false;
   private transient List sigConfs = new ArrayList();
   private transient Object policyOutline = null;
   private transient int requestPolicyIdx = 0;

   public WSS11Context(Node var1) {
      super(var1);
   }

   public WSS11Context(Node var1, Node var2, Set var3, Map var4) {
      super(var1, var2, var3, var4);
   }

   protected void init() {
      super.init();
      this.setTokenHandler(new EncryptedKeyTokenHandler());
      this.setTokenHandler(new BSTHandler());
   }

   public void addSignatureConfirmation(SignatureConfirmation var1) {
      this.sigConfs.add(var1);
   }

   public List getSignatureConfirmations() {
      return this.sigConfs;
   }

   public void addPreviousMessageSignatureValues(String[] var1) {
      List var2 = this.internalGetPreviousMessageSignatureValues();
      var2.add(var1);
      this.setProperty("weblogic.xml.crypto.wss11.previousMessageSignatureValues", var2);
   }

   private List<String[]> internalGetPreviousMessageSignatureValues() {
      Object var1 = this.getProperty("weblogic.xml.crypto.wss11.previousMessageSignatureValues");
      if (var1 instanceof String[]) {
         ArrayList var2 = new ArrayList();
         var2.add((String[])((String[])var1));
         var1 = var2;
      }

      Object var3 = (List)var1;
      if (var3 == null) {
         var3 = new ArrayList();
      }

      return (List)var3;
   }

   public List<String[]> getPreviousMessageSignatureValues() {
      Object var1 = this.internalGetPreviousMessageSignatureValues();
      if (((List)var1).size() == 0) {
         var1 = new ArrayList();
         ((List)var1).add((Object)null);
      }

      return (List)var1;
   }

   public String[] getSignatureValues() {
      String[] var1 = new String[this.getSignatures().size()];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = Utils.base64(((XMLSignature)this.getSignatures().get(var2)).getSignatureValue());
      }

      return var1;
   }

   public boolean isUseSignatureConfirmation() {
      return this.useSignatureConfirmation;
   }

   public void setUseSignatureConfirmation(boolean var1) {
      this.useSignatureConfirmation = var1;
   }

   public Object getPolicyOutline() {
      return this.policyOutline;
   }

   public void setPolicyOutline(Object var1) {
      this.policyOutline = var1;
   }

   public int getRequestPolicyIdx() {
      return this.requestPolicyIdx;
   }

   public void setRequestPolicyIdx(int var1) {
      this.requestPolicyIdx = var1;
   }

   public boolean hasDerivedKey() {
      List var1 = this.getSecurityTokens();
      if (null != var1 && var1.size() != 0) {
         for(int var2 = 0; var2 < var1.size(); ++var2) {
            Object var3 = var1.get(var2);
            if (var3 instanceof EncryptedKeyToken) {
               EncryptedKeyToken var4 = (EncryptedKeyToken)var3;
               if (var4.getSecretKey() != null) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private void readObject(ObjectInputStream var1) throws ClassNotFoundException, IOException {
      var1.defaultReadObject();
      this.useSignatureConfirmation = false;
      this.sigConfs = new ArrayList();
      this.policyOutline = null;
      this.requestPolicyIdx = 0;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
   }
}
