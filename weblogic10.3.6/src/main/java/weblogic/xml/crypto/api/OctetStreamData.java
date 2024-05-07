package weblogic.xml.crypto.api;

import java.io.InputStream;

public class OctetStreamData implements Data {
   private String uri;
   private String mimeType;
   private InputStream octetStream;

   public OctetStreamData(InputStream var1) {
      this.octetStream = var1;
   }

   public OctetStreamData(InputStream var1, String var2, String var3) {
      this.octetStream = var1;
      this.uri = var2;
      this.mimeType = var3;
   }

   public String getMimeType() {
      return this.mimeType;
   }

   public InputStream getOctetStream() {
      return this.octetStream;
   }

   public String getURI() {
      return this.uri;
   }
}
