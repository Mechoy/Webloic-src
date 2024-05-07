package weblogic.xml.security.utils;

import java.util.Map;
import weblogic.xml.stream.XMLName;

public class QNameAttribute extends MutableAttribute {
   private XMLName attrValue;
   private String computedValue;

   public QNameAttribute(XMLName var1, XMLName var2) {
      super(var1);
      this.attrValue = null;
      this.computedValue = null;
      this.attrValue = var2;
      this.computedValue = null;
   }

   public QNameAttribute(String var1, String var2, XMLName var3) {
      this(ElementFactory.createXMLName(var1, var2), var3);
   }

   public QNameAttribute(String var1, String var2, String var3, XMLName var4) {
      this(ElementFactory.createXMLName(var1, var2, var3), var4);
   }

   public String getValue() {
      if (this.computedValue == null) {
         this.computeValue();
      }

      return this.computedValue;
   }

   void setQNameValue(XMLName var1) {
      this.attrValue = var1;
      this.computedValue = null;
   }

   XMLName getQNameValue() {
      return this.attrValue;
   }

   private void computeValue() {
      this.computedValue = this.attrValue.getQualifiedName();
   }

   public static XMLName parseQName(String var0, Map var1) {
      int var2 = var0.indexOf(58);
      if (var2 < 0) {
         return ElementFactory.createXMLName(var0);
      } else {
         String var3 = var0.substring(0, var2);
         return ElementFactory.createXMLName((String)var1.get(var3), var0.substring(var2 + 1), var3);
      }
   }

   public String toString() {
      return " " + this.getName() + "=" + "\"" + this.attrValue + "\"";
   }
}
