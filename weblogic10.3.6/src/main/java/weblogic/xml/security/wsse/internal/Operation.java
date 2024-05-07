package weblogic.xml.security.wsse.internal;

import weblogic.xml.security.NamedKey;
import weblogic.xml.security.encryption.EncryptedKey;
import weblogic.xml.security.signature.Signature;
import weblogic.xml.security.specs.OperationSpec;

public class Operation {
   private final Object operation;
   private final OperationSpec spec;

   public Operation(EncryptedKey var1, OperationSpec var2) {
      this.operation = var1;
      this.spec = var2;
   }

   public Operation(Signature var1, OperationSpec var2) {
      this.operation = var1;
      this.spec = var2;
   }

   public Operation(NamedKey var1, OperationSpec var2) {
      this.operation = var1;
      this.spec = var2;
   }

   public Object getOperation() {
      return this.operation;
   }

   public OperationSpec getSpec() {
      return this.spec;
   }
}
