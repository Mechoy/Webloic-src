package weblogic.xml.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

public final class ImplementationFactory implements DOMImplementation {
   private static ImplementationFactory singleton = new ImplementationFactory();

   private ImplementationFactory() {
   }

   public static DOMImplementation newImplementation() {
      return singleton;
   }

   public Document createDocument(String var1, String var2, DocumentType var3) {
      if (var3 != null) {
         throw new DOMException((short)15, "This DOM implementation does not support DocumentType objects");
      } else {
         return new DocumentImpl(var1, var2);
      }
   }

   public DocumentType createDocumentType(String var1, String var2, String var3) {
      throw new DOMException((short)15, "This DOM implementation does not support DocumentType objects");
   }

   public boolean hasFeature(String var1, String var2) {
      return false;
   }

   public Object getFeature(String var1, String var2) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }
}
