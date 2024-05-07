package weblogic.xml.crypto.encrypt;

import weblogic.xml.crypto.encrypt.api.KeyReference;

public class WLKeyReference extends WLReferenceType implements KeyReference {
   public static final String TAG_KEY_REFERENCE = "KeyReference";

   public WLKeyReference() {
   }

   public WLKeyReference(String var1) {
      super(var1);
   }

   protected final String getLocalName() {
      return "KeyReference";
   }

   protected final String getNamespace() {
      return "http://www.w3.org/2001/04/xmlenc#";
   }
}
