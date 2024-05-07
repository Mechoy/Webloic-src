package weblogic.deploy.api.spi.config;

import javax.enterprise.deploy.shared.ModuleType;

public class DescriptorSupport {
   private ModuleType module;
   private String baseTag;
   private String configTag;
   private String baseNameSpace;
   private String configNameSpace;
   private String baseURI;
   private String configURI;
   private String stdClassName;
   private String dConfigClassName;
   private String configClassName;
   private boolean primary;
   private boolean flush = false;

   public DescriptorSupport(ModuleType var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, boolean var11) {
      this.module = var1;
      this.baseTag = var2;
      this.configTag = var3;
      this.baseURI = var6;
      this.configURI = var7;
      this.stdClassName = var8;
      this.dConfigClassName = var10;
      this.configClassName = var9;
      this.primary = var11;
      this.baseNameSpace = var4;
      this.configNameSpace = var5;
   }

   public ModuleType getModuleType() {
      return this.module;
   }

   public String getBaseTag() {
      return this.baseTag;
   }

   public String getConfigTag() {
      return this.configTag;
   }

   public void setConfigTag(String var1) {
      this.configTag = var1;
   }

   public String getBaseNameSpace() {
      return this.baseNameSpace;
   }

   public void setBaseNameSpace(String var1) {
      this.baseNameSpace = var1;
   }

   public String getConfigNameSpace() {
      return this.configNameSpace;
   }

   public void setConfigNameSpace(String var1) {
      this.configNameSpace = var1;
   }

   public String getBaseURI() {
      return this.baseURI;
   }

   public void setBaseURI(String var1) {
      this.baseURI = var1;
   }

   public String getConfigURI() {
      return this.configURI;
   }

   public void setConfigURI(String var1) {
      this.configURI = var1;
   }

   public String getStandardClassName() {
      return this.stdClassName;
   }

   public void setStandardClassName(String var1) {
      this.stdClassName = var1;
   }

   public String getDConfigClassName() {
      return this.dConfigClassName;
   }

   public void setDConfigClassName(String var1) {
      this.dConfigClassName = var1;
   }

   public String getConfigClassName() {
      return this.configClassName;
   }

   public void setConfigClassName(String var1) {
      this.configClassName = var1;
   }

   public boolean isPrimary() {
      return this.primary;
   }

   public boolean supportsConfigModules() {
      if (this.getModuleType().getValue() == ModuleType.EAR.getValue()) {
         return true;
      } else {
         return this.getModuleType().getValue() == ModuleType.WAR.getValue();
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getModuleType().toString());
      var1.append(":");
      var1.append(this.getBaseTag());
      var1.append(":");
      var1.append(this.getConfigTag());
      var1.append(":");
      var1.append(this.getBaseURI());
      var1.append(":");
      var1.append(this.getConfigURI());
      return var1.toString();
   }

   public int hashCode() {
      return this.toString().hashCode();
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof DescriptorSupport)) {
         return false;
      } else if (this == var1) {
         return true;
      } else {
         return this.hashCode() == var1.hashCode();
      }
   }

   boolean isFlush() {
      return this.flush;
   }

   void setFlush(boolean var1) {
      this.flush = var1;
   }
}
