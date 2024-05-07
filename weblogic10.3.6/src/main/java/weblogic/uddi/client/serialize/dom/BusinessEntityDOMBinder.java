package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.BusinessEntity;
import weblogic.uddi.client.structures.datatypes.Description;

public class BusinessEntityDOMBinder {
   public static BusinessEntity fromDOM(Element var0) {
      BusinessEntity var3 = new BusinessEntity();
      if (var0.hasAttribute("businessKey")) {
         var3.setBusinessKey(var0.getAttribute("businessKey"));
      }

      if (var0.hasAttribute("operator")) {
         var3.setOperator(var0.getAttribute("operator"));
      }

      if (var0.hasAttribute("authorizedName")) {
         var3.setAuthorizedName(var0.getAttribute("authorizedName"));
      }

      NodeList var1 = var0.getElementsByTagName("discoveryURLs");
      if (var1.getLength() > 0) {
         var3.setDiscoveryURLs(DiscoveryURLsDOMBinder.fromDOM((Element)var1.item(0)));
      }

      var1 = var0.getElementsByTagName("name");
      if (var1.getLength() > 0) {
         var3.setName(NameDOMBinder.fromDOM((Element)var1.item(0)));
      }

      Vector var4 = new Vector();
      var1 = var0.getElementsByTagName("description");
      int var2 = var1.getLength();

      int var5;
      for(var5 = 0; var5 < var2; ++var5) {
         if (((Element)var1.item(var5)).getParentNode().equals(var0)) {
            var4.add(DescriptionDOMBinder.fromDOM((Element)var1.item(var5)));
         }
      }

      var3.setDescriptionVector(var4);
      var1 = var0.getElementsByTagName("contacts");
      if (var1.getLength() > 0) {
         var3.setContacts(ContactsDOMBinder.fromDOM((Element)var1.item(0)));
      }

      var1 = var0.getElementsByTagName("businessServices");
      if (var1.getLength() > 0) {
         var3.setBusinessServices(BusinessServicesDOMBinder.fromDOM((Element)var1.item(0)));
      }

      var1 = var0.getElementsByTagName("identifierBag");
      if (var1.getLength() > 0) {
         var3.setIdentifierBag(IdentifierBagDOMBinder.fromDOM((Element)var1.item(0)));
      }

      var1 = var0.getElementsByTagName("categoryBag");
      var2 = var1.getLength();

      for(var5 = 0; var5 < var2; ++var5) {
         if (((Element)var1.item(var5)).getParentNode().equals(var0)) {
            var3.setCategoryBag(CategoryBagDOMBinder.fromDOM((Element)var1.item(var5)));
            break;
         }
      }

      return var3;
   }

   public static Element toDOM(BusinessEntity var0, Document var1) {
      Element var2 = var1.createElement("businessEntity");
      if (var0.getBusinessKey() != null) {
         var2.setAttribute("businessKey", var0.getBusinessKey());
      }

      if (var0.getOperator() != null) {
         var2.setAttribute("operator", var0.getOperator());
      }

      if (var0.getAuthorizedName() != null) {
         var2.setAttribute("authorizedName", var0.getAuthorizedName());
      }

      if (var0.getDiscoveryURLs() != null) {
         var2.appendChild(DiscoveryURLsDOMBinder.toDOM(var0.getDiscoveryURLs(), var1));
      }

      if (var0.getName() != null) {
         var2.appendChild(NameDOMBinder.toDOM(var0.getName(), var1));
      }

      Vector var3 = var0.getDescriptionVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(DescriptionDOMBinder.toDOM((Description)var3.elementAt(var4), var1));
      }

      if (var0.getContacts() != null) {
         var2.appendChild(ContactsDOMBinder.toDOM(var0.getContacts(), var1));
      }

      if (var0.getBusinessServices() != null) {
         var2.appendChild(BusinessServicesDOMBinder.toDOM(var0.getBusinessServices(), var1));
      }

      if (var0.getIdentifierBag() != null) {
         var2.appendChild(IdentifierBagDOMBinder.toDOM(var0.getIdentifierBag(), var1));
      }

      if (var0.getCategoryBag() != null) {
         var2.appendChild(CategoryBagDOMBinder.toDOM(var0.getCategoryBag(), var1));
      }

      return var2;
   }
}
