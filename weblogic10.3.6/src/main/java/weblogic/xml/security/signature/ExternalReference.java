package weblogic.xml.security.signature;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import weblogic.xml.security.transforms.Transforms;

public class ExternalReference extends InternalReference {
   public ExternalReference(String var1) {
      super(var1);
   }

   public ExternalReference(String var1, DigestMethod var2) {
      super(var1, var2);
   }

   protected void process(Transforms var1) throws InvalidReferenceException {
      try {
         InputStream var2 = this.resolveExternalReference(this.getURI());
         var1.perform(var2);
      } catch (IOException var3) {
         throw new InvalidReferenceException(var3, this);
      }
   }

   private InputStream resolveExternalReference(String var1) throws IOException {
      return (new URL(var1)).openStream();
   }
}
