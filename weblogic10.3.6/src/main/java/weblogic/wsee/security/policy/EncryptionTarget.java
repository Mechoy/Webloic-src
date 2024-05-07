package weblogic.wsee.security.policy;

import java.util.ArrayList;
import java.util.List;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;

public class EncryptionTarget {
   private EncryptionMethod encryptionMethod;
   private List tbes = new ArrayList();

   public EncryptionTarget() {
   }

   public EncryptionTarget(EncryptionMethod var1, List var2) {
      this.encryptionMethod = var1;
      this.tbes = var2;
   }

   public EncryptionMethod getEncryptionMethod() {
      return this.encryptionMethod;
   }

   public void setEncryptionMethod(EncryptionMethod var1) {
      this.encryptionMethod = var1;
   }

   public List getTBEs() {
      return this.tbes;
   }

   public void setTBEs(List var1) {
      this.tbes = var1;
   }
}
