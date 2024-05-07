package weblogic.xml.crypto.encrypt.api;

import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;

public abstract class TBEXML extends TBEBase {
   private final CanonicalizationMethod c14nMethod;

   protected TBEXML(String var1, String var2, String var3, CanonicalizationMethod var4) {
      super(var1, var2, var3);
      this.c14nMethod = var4;
   }

   protected TBEXML(String var1, String var2, String var3) {
      this(var1, var2, var3, (CanonicalizationMethod)null);
   }

   public CanonicalizationMethod getCanonicalizationMethod() {
      return this.c14nMethod;
   }
}
