package weblogic.wsee.wsdl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.util.ToStringWriter;

public class UnknownExtension implements WsdlExtension {
   public static final String KEY = "unknown";
   private Element data;

   public String getKey() {
      return "unknown";
   }

   public Element getExtensionElement() {
      return this.data;
   }

   public void parse(Element var1) {
      this.data = var1;
   }

   public void write(Element var1, WsdlWriter var2) {
      Node var3 = var1.getOwnerDocument().importNode(this.data, true);
      var1.appendChild(var3);
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("key", "unknown");
      var1.writeField("xml", this.data);
      var1.end();
   }
}
