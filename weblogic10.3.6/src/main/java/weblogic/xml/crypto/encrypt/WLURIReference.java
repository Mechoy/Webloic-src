package weblogic.xml.crypto.encrypt;

import weblogic.xml.crypto.api.URIReference;

public class WLURIReference implements URIReference {
   private String uri;
   private String type;

   protected WLURIReference(String var1, String var2) {
      this.uri = var1;
      this.type = var2;
   }

   public String getURI() {
      return this.uri;
   }

   public void setURI(String var1) {
      this.uri = var1;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String var1) {
      this.type = var1;
   }
}
