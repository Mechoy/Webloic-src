package weblogic.xml.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.ProcessingInstruction;

public class PINode extends ChildNode implements ProcessingInstruction {
   public PINode() {
      this.setNodeType((short)7);
   }

   public PINode(String var1) {
      this();
      this.setNodeName(var1);
   }

   public PINode(String var1, String var2) {
      this(var1);
      this.setNodeValue(var2);
   }

   public String getTarget() {
      return this.getNodeName();
   }

   public String getData() {
      return this.getNodeValue();
   }

   public void setData(String var1) throws DOMException {
      this.setNodeValue(var1);
   }

   public void print(StringBuffer var1, int var2) {
      var1.append(this.toString());
   }

   public String toString() {
      String var1 = this.getTarget();
      String var2 = this.getData();
      if (var1 == null) {
         var1 = "";
      }

      if (var2 == null) {
         var2 = "";
      } else {
         var2 = " " + var2;
      }

      return "<?" + var1 + var2 + "?>";
   }
}
