package weblogic.xml.sax;

import java.util.Hashtable;
import java.util.Map;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;

public class DeclEventListener implements DeclHandler {
   private Hashtable entitiesSystemIds = new Hashtable();
   private Hashtable entitiesPublicIds = new Hashtable();

   public void attributeDecl(String var1, String var2, String var3, String var4, String var5) throws SAXException {
   }

   public void elementDecl(String var1, String var2) throws SAXException {
   }

   public void externalEntityDecl(String var1, String var2, String var3) throws SAXException {
      if (var1 != null) {
         if (var3 != null) {
            this.entitiesSystemIds.put(var1, var3);
         }

         if (var2 != null) {
            this.entitiesPublicIds.put(var1, var2);
         }
      }

   }

   public void internalEntityDecl(String var1, String var2) throws SAXException {
   }

   protected Map getExternalEntitiesSystemIds() {
      return this.entitiesSystemIds;
   }

   protected Map getExternalEntitiesPublicIds() {
      return this.entitiesPublicIds;
   }
}
