package weblogic.uddi.client.serialize.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.InstanceParms;

public class InstanceParmsDOMBinder {
   public static InstanceParms fromDOM(Element var0) {
      return new InstanceParms(TextDOMBinder.fromDOM(var0));
   }

   public static Element toDOM(InstanceParms var0, Document var1) {
      return TextDOMBinder.toDOM("instanceParms", var0.getValue(), var1);
   }
}
