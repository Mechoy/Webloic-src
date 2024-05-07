package weblogic.xml.security.utils;

import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.XMLName;

public class MutableAttribute implements Attribute {
   private XMLName attrName;
   private final String value;
   private final XMLName schemaType;
   private final String type;
   private static final String DEFAULT_TYPE = "CDATA";

   public MutableAttribute(Attribute var1) {
      this(var1.getName(), var1.getValue(), var1.getType(), var1.getSchemaType());
   }

   public MutableAttribute(XMLName var1, String var2) {
      this(var1, var2, "CDATA", (XMLName)null);
   }

   MutableAttribute(XMLName var1) {
      this(var1, (String)null, "CDATA", (XMLName)null);
   }

   private MutableAttribute(XMLName var1, String var2, String var3, XMLName var4) {
      this.attrName = null;
      this.attrName = var1;
      this.value = var2;
      this.type = var3;
      this.schemaType = var4;
   }

   public MutableAttribute(String var1, String var2, String var3) {
      this((XMLName)null, var3);
      this.attrName = new PrefixableName(var1, var2);
   }

   public XMLName getName() {
      return this.attrName;
   }

   void setName(XMLName var1) {
      this.attrName = var1;
   }

   public String getValue() {
      return this.value;
   }

   public final String getType() {
      return this.type;
   }

   public final XMLName getSchemaType() {
      return this.schemaType;
   }

   public final boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof MutableAttribute)) {
         return false;
      } else {
         MutableAttribute var2 = (MutableAttribute)var1;
         if (!this.attrName.equals(var2.attrName)) {
            return false;
         } else {
            return this.getValue().equals(var2.getValue());
         }
      }
   }

   public final int hashCode() {
      int var1 = this.attrName.hashCode();
      var1 = 29 * var1 + this.getValue().hashCode();
      return var1;
   }

   public String toString() {
      return " " + this.attrName + "=" + "\"" + this.value + "\"";
   }
}
