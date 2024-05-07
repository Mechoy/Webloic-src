package weblogic.management.descriptors.webservice;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;
import weblogic.xml.stream.XMLName;

public class TypeMappingEntryMBeanImpl extends XMLElementMBeanDelegate implements TypeMappingEntryMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_xsdTypeName = false;
   private XMLName xsdTypeName;
   private boolean isSet_deserializerName = false;
   private String deserializerName;
   private boolean isSet_serializerName = false;
   private String serializerName;
   private boolean isSet_className = false;
   private String className;
   private boolean isSet_elementName = false;
   private XMLName elementName;

   public XMLName getXSDTypeName() {
      return this.xsdTypeName;
   }

   public void setXSDTypeName(XMLName var1) {
      XMLName var2 = this.xsdTypeName;
      this.xsdTypeName = var1;
      this.isSet_xsdTypeName = var1 != null;
      this.checkChange("xsdTypeName", var2, this.xsdTypeName);
   }

   public String getDeserializerName() {
      return this.deserializerName;
   }

   public void setDeserializerName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.deserializerName;
      this.deserializerName = var1;
      this.isSet_deserializerName = var1 != null;
      this.checkChange("deserializerName", var2, this.deserializerName);
   }

   public String getSerializerName() {
      return this.serializerName;
   }

   public void setSerializerName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.serializerName;
      this.serializerName = var1;
      this.isSet_serializerName = var1 != null;
      this.checkChange("serializerName", var2, this.serializerName);
   }

   public String getClassName() {
      return this.className;
   }

   public void setClassName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.className;
      this.className = var1;
      this.isSet_className = var1 != null;
      this.checkChange("className", var2, this.className);
   }

   public XMLName getElementName() {
      return this.elementName;
   }

   public void setElementName(XMLName var1) {
      XMLName var2 = this.elementName;
      this.elementName = var1;
      this.isSet_elementName = var1 != null;
      this.checkChange("elementName", var2, this.elementName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<type-mapping-entry");
      if (this.isSet_deserializerName) {
         var2.append(" deserializer=\"").append(String.valueOf(this.getDeserializerName())).append("\"");
      }

      if (this.isSet_xsdTypeName) {
         var2.append(" xmlns:").append(this.getXSDTypeName().getPrefix()).append("=\"").append(this.getXSDTypeName().getNamespaceUri()).append("\" type=\"").append(this.getXSDTypeName().getQualifiedName()).append("\"");
      }

      if (this.isSet_serializerName) {
         var2.append(" serializer=\"").append(String.valueOf(this.getSerializerName())).append("\"");
      }

      if (this.isSet_className) {
         var2.append(" class-name=\"").append(String.valueOf(this.getClassName())).append("\"");
      }

      if (this.isSet_elementName) {
         var2.append(" xmlns:").append(this.getElementName().getPrefix()).append("=\"").append(this.getElementName().getNamespaceUri()).append("\" element=\"").append(this.getElementName().getQualifiedName()).append("\"");
      }

      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</type-mapping-entry>\n");
      return var2.toString();
   }
}
