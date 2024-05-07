package weblogic.xml.crypto.dsig;

import java.util.Iterator;
import java.util.List;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.dsig.api.Reference;

public class SignatureValidateResult {
   boolean status;
   String signature;
   KeySelectorResult keySelectorResult;
   List refValidateResults;
   boolean refStatus = true;

   protected SignatureValidateResult(boolean var1, String var2, KeySelectorResult var3, List var4) {
      this.status = var1;
      this.signature = var2;
      this.keySelectorResult = var3;
      this.refValidateResults = var4;
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         Reference.ValidateResult var6 = (Reference.ValidateResult)var5.next();
         if (!var6.status()) {
            this.refStatus = false;
            break;
         }
      }

   }

   public boolean status() {
      return this.status;
   }

   public String getSignatureValue() {
      return this.signature;
   }

   public KeySelectorResult getKeySelectorResult() {
      return this.keySelectorResult;
   }

   public List getReferenceValidateResults() {
      return this.refValidateResults;
   }

   public boolean getReferenceValidationStatus() {
      return this.refStatus;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.refValidateResults != null) {
         for(int var2 = 0; var2 < this.refValidateResults.size(); ++var2) {
            var1.append("\n" + this.refValidateResults.get(var2));
         }
      }

      return "[SignatureValidationResult:\n[status: " + this.status + "]" + "\n[signature: " + this.signature + "]" + "\n[keySelectorResult: " + this.keySelectorResult + "]" + "\n[refValidationResults: " + var1 + "]]";
   }

   public String toFaultString() {
      String var1 = "";
      if (!this.status) {
         var1 = "SignatureValue: " + this.signature + " does not validate.";
      }

      Iterator var2 = this.refValidateResults.iterator();

      while(var2.hasNext()) {
         Reference.ValidateResult var3 = (Reference.ValidateResult)var2.next();
         if (!var3.status()) {
            var1 = var1 + " Reference: " + var3.getReferenceURI() + " does not validate.\n";
         }
      }

      return var1;
   }
}
