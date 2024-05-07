package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.uddi.client.structures.datatypes.BindingTemplate;
import weblogic.uddi.client.structures.datatypes.Description;

public class BindingTemplateDOMBinder {
   public static BindingTemplate fromDOM(Element var0) {
      BindingTemplate var1 = new BindingTemplate();
      if (var0.hasAttribute("bindingKey")) {
         var1.setBindingKey(var0.getAttribute("bindingKey"));
      }

      if (var0.hasAttribute("serviceKey")) {
         var1.setServiceKey(var0.getAttribute("serviceKey"));
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
      var2 = var0.getElementsByTagName("accessPoint");
      if (var2.getLength() > 0) {
         var1.setAccessPoint(AccessPointDOMBinder.fromDOM((Element)var2.item(0)));
      }

      var2 = var0.getElementsByTagName("hostingRedirector");
      if (var2.getLength() > 0) {
         var1.setHostingRedirector(HostingRedirectorDOMBinder.fromDOM((Element)var2.item(0)));
      }

      var2 = var0.getElementsByTagName("tModelInstanceDetails");
      if (var2.getLength() > 0) {
         var1.setTModelInstanceDetails(TModelInstanceDetailsDOMBinder.fromDOM((Element)var2.item(0)));
      }

      return var1;
   }

   public static Element toDOM(BindingTemplate var0, Document var1) {
      Element var2 = var1.createElement("bindingTemplate");
      if (var0.getBindingKey() != null) {
         var2.setAttribute("bindingKey", var0.getBindingKey());
      }

      if (var0.getServiceKey() != null) {
         var2.setAttribute("serviceKey", var0.getServiceKey());
      }

      Vector var3 = var0.getDescriptionVector();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         var2.appendChild(DescriptionDOMBinder.toDOM((Description)var3.elementAt(var4), var1));
      }

      if (var0.getAccessPoint() != null) {
         var2.appendChild(AccessPointDOMBinder.toDOM(var0.getAccessPoint(), var1));
      }

      if (var0.getHostingRedirector() != null) {
         var2.appendChild(HostingRedirectorDOMBinder.toDOM(var0.getHostingRedirector(), var1));
      }

      if (var0.getTModelInstanceDetails() != null) {
         var2.appendChild(TModelInstanceDetailsDOMBinder.toDOM(var0.getTModelInstanceDetails(), var1));
      }

      return var2;
   }
}
