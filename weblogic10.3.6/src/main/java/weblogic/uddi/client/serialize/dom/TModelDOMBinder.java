package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.Description;
import weblogic.uddi.client.structures.datatypes.TModel;

public class TModelDOMBinder {
   public static TModel fromDOM(Element var0) {
      TModel var3 = new TModel();
      if (var0.hasAttribute("tModelKey")) {
         var3.setTModelKey(var0.getAttribute("tModelKey"));
      }

      if (var0.hasAttribute("operator")) {
         var3.setOperator(var0.getAttribute("operator"));
      }

      if (var0.hasAttribute("authorizedName")) {
         var3.setAuthorizedName(var0.getAttribute("authorizedName"));
      }

      NodeList var1 = var0.getElementsByTagName("name");
      if (var1.getLength() > 0) {
         var3.setName(NameDOMBinder.fromDOM((Element)var1.item(0)));
      }

      Vector var4 = new Vector();
      var1 = var0.getElementsByTagName("description");
      int var2 = var1.getLength();

      for(int var5 = 0; var5 < var2; ++var5) {
         if (((Element)var1.item(var5)).getParentNode().equals(var0)) {
            var4.add(DescriptionDOMBinder.fromDOM((Element)var1.item(var5)));
         }
      }

      var3.setDescriptionVector(var4);
      var1 = var0.getElementsByTagName("overviewDoc");
      if (var1.getLength() > 0) {
         var3.setOverviewDoc(OverviewDocDOMBinder.fromDOM((Element)var1.item(0)));
      }

      var1 = var0.getElementsByTagName("identifierBag");
      if (var1.getLength() > 0) {
         var3.setIdentifierBag(IdentifierBagDOMBinder.fromDOM((Element)var1.item(0)));
      }

      var1 = var0.getElementsByTagName("categoryBag");
      if (var1.getLength() > 0) {
         var3.setCategoryBag(CategoryBagDOMBinder.fromDOM((Element)var1.item(0)));
      }

      return var3;
   }

   public static Element toDOM(TModel var0, Document var1) {
      Element var2 = var1.createElement("tModel");
      if (var0.getTModelKey() != null) {
         var2.setAttribute("tModelKey", var0.getTModelKey());
      }

      if (var0.getOperator() != null) {
         var2.setAttribute("operator", var0.getOperator());
      }

      if (var0.getAuthorizedName() != null) {
         var2.setAttribute("authorizedName", var0.getAuthorizedName());
      }

      if (var0.getName() != null) {
         var2.appendChild(NameDOMBinder.toDOM(var0.getName(), var1));
      }

      Vector var3 = var0.getDescriptionVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(DescriptionDOMBinder.toDOM((Description)var3.elementAt(var4), var1));
      }

      if (var0.getOverviewDoc() != null) {
         var2.appendChild(OverviewDocDOMBinder.toDOM(var0.getOverviewDoc(), var1));
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
