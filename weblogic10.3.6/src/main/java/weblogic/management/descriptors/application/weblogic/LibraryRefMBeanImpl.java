package weblogic.management.descriptors.application.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class LibraryRefMBeanImpl extends XMLElementMBeanDelegate implements LibraryRefMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_contextPath = false;
   private String contextPath;
   private boolean isSet_implementationVersion = false;
   private String implementationVersion;
   private boolean isSet_specificationVersion = false;
   private String specificationVersion;
   private boolean isSet_libraryName = false;
   private String libraryName;
   private boolean isSet_exactMatch = false;
   private String exactMatch;

   public String getContextPath() {
      return this.contextPath;
   }

   public void setContextPath(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.contextPath;
      this.contextPath = var1;
      this.isSet_contextPath = var1 != null;
      this.checkChange("contextPath", var2, this.contextPath);
   }

   public String getImplementationVersion() {
      return this.implementationVersion;
   }

   public void setImplementationVersion(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.implementationVersion;
      this.implementationVersion = var1;
      this.isSet_implementationVersion = var1 != null;
      this.checkChange("implementationVersion", var2, this.implementationVersion);
   }

   public String getSpecificationVersion() {
      return this.specificationVersion;
   }

   public void setSpecificationVersion(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.specificationVersion;
      this.specificationVersion = var1;
      this.isSet_specificationVersion = var1 != null;
      this.checkChange("specificationVersion", var2, this.specificationVersion);
   }

   public String getLibraryName() {
      return this.libraryName;
   }

   public void setLibraryName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.libraryName;
      this.libraryName = var1;
      this.isSet_libraryName = var1 != null;
      this.checkChange("libraryName", var2, this.libraryName);
   }

   public String getExactMatch() {
      return this.exactMatch;
   }

   public void setExactMatch(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.exactMatch;
      this.exactMatch = var1;
      this.isSet_exactMatch = var1 != null;
      this.checkChange("exactMatch", var2, this.exactMatch);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<library-ref");
      var2.append(">\n");
      if (null != this.getLibraryName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<library-name>").append(this.getLibraryName()).append("</library-name>\n");
      }

      if (null != this.getSpecificationVersion()) {
         var2.append(ToXML.indent(var1 + 2)).append("<specification-version>").append(this.getSpecificationVersion()).append("</specification-version>\n");
      }

      if (null != this.getImplementationVersion()) {
         var2.append(ToXML.indent(var1 + 2)).append("<implementation-version>").append(this.getImplementationVersion()).append("</implementation-version>\n");
      }

      if (null != this.getExactMatch()) {
         var2.append(ToXML.indent(var1 + 2)).append("<exact-match>").append(this.getExactMatch()).append("</exact-match>\n");
      }

      if (null != this.getContextPath()) {
         var2.append(ToXML.indent(var1 + 2)).append("<context-path>").append(this.getContextPath()).append("</context-path>\n");
      }

      var2.append(ToXML.indent(var1)).append("</library-ref>\n");
      return var2.toString();
   }
}
