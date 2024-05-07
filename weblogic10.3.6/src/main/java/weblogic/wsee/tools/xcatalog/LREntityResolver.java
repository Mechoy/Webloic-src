package weblogic.wsee.tools.xcatalog;

import java.io.IOException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class LREntityResolver implements EntityResolver {
   private final EntityResolver lhs;
   private final EntityResolver rhs;
   private boolean isLocal = false;

   public LREntityResolver(EntityResolver var1, EntityResolver var2) {
      this.lhs = var1;
      this.rhs = var2;
   }

   public boolean isLocal() {
      return this.isLocal;
   }

   public InputSource resolveEntity(String var1, String var2) throws SAXException, IOException {
      if (this.lhs != null) {
         InputSource var3 = this.lhs.resolveEntity(var1, var2);
         if (var3 != null) {
            this.isLocal = true;
            return var3;
         }
      }

      if (this.rhs == null) {
         return null;
      } else {
         this.isLocal = false;
         return this.rhs.resolveEntity(var1, var2);
      }
   }
}
