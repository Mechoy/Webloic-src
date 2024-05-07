package weblogic.xml.crypto.encrypt.api;

abstract class TBEBase implements TBE {
   private final String encoding;
   private final String mimeType;
   private final String type;

   protected TBEBase(String var1, String var2, String var3) {
      this.encoding = var3;
      this.mimeType = var2;
      this.type = var1;
   }

   public String getEncoding() {
      return this.encoding;
   }

   public String getMimeType() {
      return this.mimeType;
   }

   public String getType() {
      return this.type;
   }
}
