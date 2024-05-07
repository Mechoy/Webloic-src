package weblogic.wsee.wsdl.internal;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlConstants;
import weblogic.wsee.wsdl.WsdlDocumentation;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.xml.dom.NodeIterator;

public class WsdlDocumentationImpl implements WsdlDocumentation {
   private String documentation;

   WsdlDocumentationImpl() {
   }

   WsdlDocumentationImpl(String var1) {
      this.documentation = var1;
   }

   public String getDocumentation() {
      return this.documentation;
   }

   public void setDocumentation(String var1) {
      this.documentation = var1;
   }

   public void parse(Element var1, String var2) throws WsdlException {
      NodeIterator var3 = new NodeIterator(var1);
      StringBuffer var4 = new StringBuffer();

      while(var3.hasNext()) {
         Node var5 = (Node)var3.next();
         if (var5.getNodeType() == 3) {
            var4.append(var5.getNodeValue());
         }
      }

      this.documentation = var4.toString();
   }

   public Node getAssociatedNode(Element var1) {
      return var1.getNextSibling();
   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "documentation", WsdlConstants.wsdlNS);
      var2.addText(var3, this.documentation);
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("name", "documentation");
      var1.writeField("type", "documentation");
      var1.writeField("documentation", this.documentation);
      var1.end();
   }
}
