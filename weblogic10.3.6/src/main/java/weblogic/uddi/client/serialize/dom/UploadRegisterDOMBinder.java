package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.UploadRegister;

public class UploadRegisterDOMBinder {
   public static UploadRegister fromDOM(Element var0) {
      return new UploadRegister(TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(UploadRegister var0, Document var1) {
      return TextDOMBinder.toDOM("uploadRegister", var0.getValue(), var1);
   }
}
