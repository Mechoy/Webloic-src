package weblogic.xml.crypto.encrypt.api.dom;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import org.w3c.dom.Element;
import weblogic.xml.crypto.NodeSetDataImpl;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.OctetStreamData;
import weblogic.xml.crypto.api.URIDereferencer;
import weblogic.xml.crypto.api.URIReference;
import weblogic.xml.crypto.api.URIReferenceException;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.api.dom.DOMIdMap;
import weblogic.xml.crypto.utils.DOMUtils;

public class DOMURIDerferencer implements URIDereferencer {
   private String baseURI;

   public DOMURIDerferencer() {
      this((String)null);
   }

   public DOMURIDerferencer(String var1) {
      this.baseURI = var1;
   }

   public static DOMURIDerferencer getInstance() {
      return new DOMURIDerferencer();
   }

   public Data dereference(URIReference var1, XMLCryptoContext var2) throws URIReferenceException {
      String var3 = var1.getURI();
      Object var4;
      if (var2 instanceof DOMEncryptContext) {
         DOMEncryptContext var5 = (DOMEncryptContext)var2;
         if (var3 == null || var3.length() == 0) {
            Element var6 = var5.getParent().getOwnerDocument().getDocumentElement();
            return new NodeSetDataImpl(DOMUtils.getNodeSet(var6, false));
         }

         var4 = var5;
      } else {
         if (!(var2 instanceof DOMDecryptContext)) {
            throw new IllegalArgumentException("This dereferencer depends on its parent context; it cannot be used with other contexts");
         }

         DOMDecryptContext var9 = (DOMDecryptContext)var2;
         var4 = var9;
      }

      if (var3.startsWith("#")) {
         Element var11 = ((DOMIdMap)var4).getElementById(var3.substring(1, var3.length()));
         HashSet var12 = new HashSet();
         var12.add(var11);
         return new NodeSetDataImpl(var12);
      } else {
         try {
            OctetStreamData var10 = new OctetStreamData((new URL(var1.getURI())).openStream());
            return var10;
         } catch (MalformedURLException var7) {
            throw new URIReferenceException("Unsupported reference format.");
         } catch (IOException var8) {
            throw new URIReferenceException("Failed to open stream to URI " + var1.getURI() + ".");
         }
      }
   }
}
