package weblogic.xml.crypto;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.OctetStreamData;
import weblogic.xml.crypto.api.URIDereferencer;
import weblogic.xml.crypto.api.URIReference;
import weblogic.xml.crypto.api.URIReferenceException;
import weblogic.xml.crypto.api.XMLCryptoContext;

public class URIDereferencerBase implements URIDereferencer {
   public Data dereference(URIReference var1, XMLCryptoContext var2) throws URIReferenceException {
      OctetStreamData var3 = null;

      try {
         var3 = new OctetStreamData((new URL(var1.getURI())).openStream());
         return var3;
      } catch (MalformedURLException var6) {
         throw new URIReferenceException("Unsupported reference format.", var6, var1);
      } catch (IOException var7) {
         String var5 = "Failed to open stream to URI " + var1.getURI() + ".";
         throw new URIReferenceException(var5, var7, var1);
      }
   }
}
