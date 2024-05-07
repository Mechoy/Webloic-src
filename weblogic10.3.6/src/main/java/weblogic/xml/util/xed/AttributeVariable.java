package weblogic.xml.util.xed;

import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.events.AttributeImpl;

public class AttributeVariable extends Variable {
   Attribute attribute;

   public AttributeVariable(String var1, String var2) {
      super(var1);
      this.attribute = new AttributeImpl(var1, var2);
   }

   public Object evaluate(Context var1) throws StreamEditorException {
      return this.attribute;
   }

   public String toString() {
      return this.attribute.toString();
   }
}
