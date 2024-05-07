package weblogic.xml.crypto.encrypt;

import java.util.List;
import weblogic.xml.crypto.encrypt.api.DataReference;

public class WLDataReference extends WLReferenceType implements DataReference {
   public static final String TAG_DATA_REFERENCE = "DataReference";

   public WLDataReference() {
   }

   public WLDataReference(String var1, List var2) {
      super(var1, var2);
   }

   protected final String getLocalName() {
      return "DataReference";
   }

   protected final String getNamespace() {
      return "http://www.w3.org/2001/04/xmlenc#";
   }
}
