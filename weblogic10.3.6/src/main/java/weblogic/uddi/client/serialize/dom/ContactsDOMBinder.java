package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.Contact;
import weblogic.uddi.client.structures.datatypes.Contacts;

public class ContactsDOMBinder {
   public static Contacts fromDOM(Element var0) {
      Contacts var1 = new Contacts();
      Vector var2 = new Vector();
      NodeList var3 = var0.getElementsByTagName("contact");
      int var4 = var3.getLength();

      for(int var5 = 0; var5 < var4; ++var5) {
         var2.add(ContactDOMBinder.fromDOM((Element)var3.item(var5)));
      }

      var1.setContactVector(var2);
      return var1;
   }

   public static Element toDOM(Contacts var0, Document var1) {
      Element var2 = var1.createElement("contacts");
      Vector var3 = var0.getContactVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(ContactDOMBinder.toDOM((Contact)var3.elementAt(var4), var1));
      }

      return var2;
   }
}
