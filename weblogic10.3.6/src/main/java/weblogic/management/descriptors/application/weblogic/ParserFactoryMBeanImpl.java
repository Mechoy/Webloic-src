package weblogic.management.descriptors.application.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ParserFactoryMBeanImpl extends XMLElementMBeanDelegate implements ParserFactoryMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_transformerFactory = false;
   private String transformerFactory;
   private boolean isSet_saxparserFactory = false;
   private String saxparserFactory;
   private boolean isSet_documentBuilderFactory = false;
   private String documentBuilderFactory;

   public String getTransformerFactory() {
      return this.transformerFactory;
   }

   public void setTransformerFactory(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.transformerFactory;
      this.transformerFactory = var1;
      this.isSet_transformerFactory = var1 != null;
      this.checkChange("transformerFactory", var2, this.transformerFactory);
   }

   public String getSaxparserFactory() {
      return this.saxparserFactory;
   }

   public void setSaxparserFactory(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.saxparserFactory;
      this.saxparserFactory = var1;
      this.isSet_saxparserFactory = var1 != null;
      this.checkChange("saxparserFactory", var2, this.saxparserFactory);
   }

   public String getDocumentBuilderFactory() {
      return this.documentBuilderFactory;
   }

   public void setDocumentBuilderFactory(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.documentBuilderFactory;
      this.documentBuilderFactory = var1;
      this.isSet_documentBuilderFactory = var1 != null;
      this.checkChange("documentBuilderFactory", var2, this.documentBuilderFactory);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<parser-factory");
      var2.append(">\n");
      if (null != this.getSaxparserFactory()) {
         var2.append(ToXML.indent(var1 + 2)).append("<saxparser-factory>").append(this.getSaxparserFactory()).append("</saxparser-factory>\n");
      }

      if (null != this.getDocumentBuilderFactory()) {
         var2.append(ToXML.indent(var1 + 2)).append("<document-builder-factory>").append(this.getDocumentBuilderFactory()).append("</document-builder-factory>\n");
      }

      if (null != this.getTransformerFactory()) {
         var2.append(ToXML.indent(var1 + 2)).append("<transformer-factory>").append(this.getTransformerFactory()).append("</transformer-factory>\n");
      }

      var2.append(ToXML.indent(var1)).append("</parser-factory>\n");
      return var2.toString();
   }
}
