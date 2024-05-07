package weblogic.xml.registry;

public class XMLParserSelectRegistryEntry extends XMLAbstractRegistryEntry {
   private String rootTag;
   private String documentBuilderFactoryClassName;
   private String saxParserFactoryClassName;
   private String transformerFactoryClassName;
   private String parserClassName;

   public XMLParserSelectRegistryEntry(String var1, String var2, String var3, ConfigAbstraction.EntryConfig var4) {
      super(var1, var2, var4);
      this.rootTag = var3;
   }

   public String getRootElementTag() {
      return this.rootTag;
   }

   public String getDocumentBuilderFactory() {
      return this.documentBuilderFactoryClassName;
   }

   public void setDocumentBuilderFactory(String var1) {
      this.documentBuilderFactoryClassName = var1;
   }

   /** @deprecated */
   public String getParserClassName() {
      return this.parserClassName;
   }

   /** @deprecated */
   public void setParserClassName(String var1) {
      this.parserClassName = var1;
   }

   public String getSAXParserFactory() {
      return this.saxParserFactoryClassName;
   }

   public void setSAXParserFactory(String var1) {
      this.saxParserFactoryClassName = var1;
   }

   public String getTransformerFactory() {
      return this.transformerFactoryClassName;
   }

   public void setTransformerFactory(String var1) {
      this.transformerFactoryClassName = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("publicId = ");
      var1.append(this.getPublicId() == null ? "null, " : "\"" + this.getPublicId() + "\", ");
      var1.append("systemId = ");
      var1.append(this.getSystemId() == null ? "null, " : "\"" + this.getSystemId() + "\", ");
      var1.append("isPrivate = " + this.isPrivate() + ", ");
      var1.append("rootTag = " + this.getRootElementTag() + ", ");
      var1.append("documentBuilderFactoryClassName = " + this.documentBuilderFactoryClassName + ", ");
      var1.append("saxParserFactoryClassName = " + this.saxParserFactoryClassName + ", ");
      var1.append("transformerFactoryClassName = " + this.transformerFactoryClassName);
      return var1.toString();
   }
}
