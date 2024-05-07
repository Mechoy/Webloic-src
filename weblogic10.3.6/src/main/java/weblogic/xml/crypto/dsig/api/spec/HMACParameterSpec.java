package weblogic.xml.crypto.dsig.api.spec;

public final class HMACParameterSpec implements SignatureMethodParameterSpec {
   private int hMACOutputLength;

   public HMACParameterSpec(int var1) {
      this.hMACOutputLength = var1;
   }

   public int getHMACOutputLength() {
      return this.hMACOutputLength;
   }
}
