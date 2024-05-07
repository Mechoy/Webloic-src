package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.UDDIException;

public class BindingTemplatesHandler extends UDDIXMLHandler {
   private static UDDIXMLHandlerMaker maker = null;
   private BindingTemplates bindingTemplates = null;

   public Object create(Node var1) throws UDDIException {
      this.bindingTemplates = new BindingTemplates();
      maker = UDDIXMLHandlerMaker.getInstance();
      NodeList var2 = var1.getChildNodes();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.getLength(); ++var3) {
            if (var2.item(var3).getNodeType() != 8 && var2.item(var3).getNodeName().equals("bindingTemplate")) {
               BindingTemplateHandler var4 = (BindingTemplateHandler)maker.makeHandler("bindingTemplate");
               BindingTemplate var5 = (BindingTemplate)var4.create(var2.item(var3));
               this.bindingTemplates.add(var5);
            }
         }
      }

      return this.bindingTemplates;
   }
}
