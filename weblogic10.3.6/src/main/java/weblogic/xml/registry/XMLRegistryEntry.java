package weblogic.xml.registry;

import java.io.Serializable;

public class XMLRegistryEntry implements Serializable {
   private String publicId;
   private String systemId;
   private String rootTag;
   private String entityPath;
   private String parserClassName;
   private String documentBuilderFactoryClassName;
   private String saxParserFactoryClassName;
   private boolean isPrivate = false;

   public XMLRegistryEntry(String var1, String var2, String var3) {
      this.publicId = var1;
      this.systemId = var2;
      this.rootTag = var3;
   }

   public String getPublicId() {
      return this.publicId;
   }

   public String getSystemId() {
      return this.systemId;
   }

   public String getRootElementTag() {
      return this.rootTag;
   }

   public String getEntityPath() {
      return this.entityPath;
   }

   public void setEntityPath(String var1) {
      this.entityPath = var1;
   }

   public String getParserClassName() {
      return this.parserClassName;
   }

   public void setParserClassName(String var1) {
      this.parserClassName = var1;
   }

   public String getDocumentBuilderFactory() {
      return this.documentBuilderFactoryClassName;
   }

   public void setDocumentBuilderFactory(String var1) {
      this.documentBuilderFactoryClassName = var1;
   }

   public void setPrivate(boolean var1) {
      this.isPrivate = var1;
   }

   public boolean isPrivate() {
      return this.isPrivate;
   }

   public String getSAXParserFactory() {
      return this.saxParserFactoryClassName;
   }

   public void setSAXParserFactory(String var1) {
      this.saxParserFactoryClassName = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("publicId = ");
      var1.append(this.publicId == null ? "null" : "\"" + this.publicId + "\"");
      var1.append("\nsystemId = ");
      var1.append(this.systemId == null ? "null" : "\"" + this.systemId + "\"");
      var1.append("\nisPrivate = " + this.isPrivate);
      var1.append("\nrootTag = " + this.rootTag);
      var1.append("\nentityPath = " + this.entityPath);
      var1.append("\nparserClassName = " + this.parserClassName);
      var1.append("\ndocumentBuilderFactoryClassName = " + this.documentBuilderFactoryClassName);
      var1.append("\nsaxParserFactoryClassName = " + this.saxParserFactoryClassName);
      return var1.toString();
   }
}
