package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.Address;
import weblogic.uddi.client.structures.datatypes.Contact;
import weblogic.uddi.client.structures.datatypes.Description;
import weblogic.uddi.client.structures.datatypes.Email;
import weblogic.uddi.client.structures.datatypes.Phone;

public class ContactDOMBinder {
   public static Contact fromDOM(Element var0) {
      Contact var1 = new Contact();
      if (var0.hasAttribute("useType")) {
         var1.setUseType(var0.getAttribute("useType"));
      }

      Vector var4 = new Vector();
      NodeList var2 = var0.getElementsByTagName("description");
      int var3 = var2.getLength();

      for(int var5 = 0; var5 < var3; ++var5) {
         if (((Element)var2.item(var5)).getParentNode().equals(var0)) {
            var4.add(DescriptionDOMBinder.fromDOM((Element)var2.item(var5)));
         }
      }

      var1.setDescriptionVector(var4);
      var2 = var0.getElementsByTagName("personName");
      if (var2.getLength() > 0) {
         var1.setPersonName(PersonNameDOMBinder.fromDOM((Element)var2.item(0)));
      }

      Vector var9 = new Vector();
      var2 = var0.getElementsByTagName("phone");
      var3 = var2.getLength();

      for(int var6 = 0; var6 < var3; ++var6) {
         var9.add(PhoneDOMBinder.fromDOM((Element)var2.item(var6)));
      }

      var1.setPhoneVector(var9);
      Vector var10 = new Vector();
      var2 = var0.getElementsByTagName("email");
      var3 = var2.getLength();

      for(int var7 = 0; var7 < var3; ++var7) {
         var10.add(EmailDOMBinder.fromDOM((Element)var2.item(var7)));
      }

      var1.setEmailVector(var10);
      Vector var11 = new Vector();
      var2 = var0.getElementsByTagName("address");
      var3 = var2.getLength();

      for(int var8 = 0; var8 < var3; ++var8) {
         var11.add(AddressDOMBinder.fromDOM((Element)var2.item(var8)));
      }

      var1.setAddressVector(var11);
      return var1;
   }

   public static Element toDOM(Contact var0, Document var1) {
      Element var2 = var1.createElement("contact");
      if (var0.getUseType() != null) {
         var2.setAttribute("useType", var0.getUseType());
      }

      Vector var3 = var0.getDescriptionVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(DescriptionDOMBinder.toDOM((Description)var3.elementAt(var4), var1));
      }

      if (var0.getPersonName() != null) {
         var2.appendChild(PersonNameDOMBinder.toDOM(var0.getPersonName(), var1));
      }

      Vector var8 = var0.getPhoneVector();

      for(int var5 = 0; var5 < var8.size(); ++var5) {
         var2.appendChild(PhoneDOMBinder.toDOM((Phone)var8.elementAt(var5), var1));
      }

      Vector var9 = var0.getEmailVector();

      for(int var6 = 0; var6 < var9.size(); ++var6) {
         var2.appendChild(EmailDOMBinder.toDOM((Email)var9.elementAt(var6), var1));
      }

      Vector var10 = var0.getAddressVector();

      for(int var7 = 0; var7 < var10.size(); ++var7) {
         var2.appendChild(AddressDOMBinder.toDOM((Address)var10.elementAt(var7), var1));
      }

      return var2;
   }
}
