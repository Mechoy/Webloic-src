package weblogic.wsee.tools.xcatalog;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import org.apache.tools.ant.types.XMLCatalog;

public class CatalogOptions {
   private File catalog = null;
   private XMLCatalog xmlCatalog = null;
   private HashMap<URL, String> xmlMaps = null;

   public File getCatalog() {
      return this.catalog;
   }

   public void setCatalog(File var1) {
      this.catalog = var1;
   }

   public XMLCatalog getXmlCatalog() {
      return this.xmlCatalog;
   }

   public void setXmlCatalog(XMLCatalog var1) {
      this.xmlCatalog = var1;
   }

   public void setXmlMaps(HashMap<URL, String> var1) {
      this.xmlMaps = var1;
   }

   public HashMap<URL, String> getXmlMaps() {
      if (this.xmlMaps == null) {
         this.xmlMaps = new HashMap();
      }

      return this.xmlMaps;
   }
}
