package weblogic.xml.crypto;

import weblogic.xml.crypto.api.Data;

public class OctetData implements Data {
   private byte[] data;

   public OctetData(byte[] var1) {
      this.data = var1;
   }

   public byte[] getBytes() {
      return this.data;
   }
}
