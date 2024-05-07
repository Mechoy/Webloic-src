package weblogic.xml.security.utils;

import weblogic.xml.stream.XMLName;

public class PrefixableName implements XMLName {
   private final XMLName name;
   private String prefix;

   public PrefixableName(XMLName var1, String var2) {
      this.name = var1;
      this.prefix = var2;
   }

   public PrefixableName(XMLName var1) {
      this(var1, var1.getPrefix());
   }

   public PrefixableName(String var1, String var2) {
      this((XMLName)ElementFactory.createXMLName(var1, var2), (String)null);
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public boolean equals(Object var1) {
      return this.name.equals(var1);
   }

   public PrefixableName(String var1, String var2, String var3) {
      this(weblogic.xml.stream.ElementFactory.createXMLName(var1, var2), var3);
   }

   public final String getPrefix() {
      return this.prefix;
   }

   final void setPrefix(String var1) {
      this.prefix = var1;
   }

   public final String getNamespaceUri() {
      return this.name.getNamespaceUri();
   }

   public final String getLocalName() {
      return this.name.getLocalName();
   }

   public final String getQualifiedName() {
      return this.prefix != null && this.prefix.length() > 0 ? this.prefix + ":" + this.name.getLocalName() : this.getLocalName();
   }

   public final String toString() {
      String var1 = "";
      String var2 = this.name.getNamespaceUri();
      if (var2 != null) {
         var1 = var1 + "['" + var2 + "']:";
      }

      if (this.prefix != null) {
         var1 = var1 + this.prefix + ":";
      }

      var1 = var1 + this.name.getLocalName();
      return var1;
   }
}
