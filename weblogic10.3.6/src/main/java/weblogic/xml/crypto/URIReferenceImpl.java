package weblogic.xml.crypto;

import weblogic.xml.crypto.api.URIReference;

public class URIReferenceImpl implements URIReference {
   private String type;
   private String uri;

   public URIReferenceImpl(String var1) {
      this.uri = var1;
   }

   public String getType() {
      return this.type;
   }

   public String getURI() {
      return this.uri;
   }
}
