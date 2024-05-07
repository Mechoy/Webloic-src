package weblogic.xml.dom;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ResourceEntityResolver implements EntityResolver {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   protected Map entityCatalog = new HashMap();
   private boolean unresolvedIsFatal = false;

   public void setUnresolvedIsFatal(boolean var1) {
      this.unresolvedIsFatal = var1;
   }

   public InputSource resolveEntity(String var1, String var2) throws SAXException, IOException {
      if (var1 == null) {
         return null;
      } else {
         InputSource var3 = null;
         EntityResource var4 = (EntityResource)this.entityCatalog.get(var1);
         if (var4 == null) {
            String var7 = "Could not locate public Id " + var1 + "\nin entityCatalog";
            if (this.unresolvedIsFatal) {
               throw new SAXException(var7);
            } else {
               return null;
            }
         } else {
            InputStream var5 = null;
            if (var4 != null) {
               var5 = var4.getResourceAsStream();
            }

            if (var5 == null) {
               String var6 = "ResourceEntityResolver: did not resolve entity for publicId = " + var1 + " with resource name " + var4.getResourceName();
               if (this.unresolvedIsFatal) {
                  throw new SAXException(var6);
               } else {
                  return null;
               }
            } else {
               var3 = new InputSource(var5);
               var3.setPublicId(var1);
               return var3;
            }
         }
      }
   }

   public void addEntityResource(String var1, String var2) {
      this.addEntityResource(var1, var2, (Class)null);
   }

   public void addEntityResource(String var1, String var2, Class var3) {
      this.entityCatalog.put(var1, new EntityResource(var2, var3));
   }

   public String toString() {
      return this.entityCatalog.toString();
   }

   protected static class EntityResource {
      private final String name;
      private final Class clazz;

      public EntityResource(String var1, Class var2) {
         this.name = var1;
         this.clazz = var2;
      }

      public String getResourceName() {
         return this.name;
      }

      public Class getResourceClass() {
         return this.clazz;
      }

      public InputStream getResourceAsStream() {
         return this.clazz == null ? this.getClass().getResourceAsStream(this.name) : this.clazz.getResourceAsStream(this.name);
      }

      public String toString() {
         return this.name + "/" + this.clazz;
      }
   }
}
