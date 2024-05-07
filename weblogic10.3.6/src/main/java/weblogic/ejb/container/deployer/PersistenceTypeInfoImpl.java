package weblogic.ejb.container.deployer;

import weblogic.ejb.container.interfaces.PersistenceTypeInfo;

class PersistenceTypeInfoImpl implements PersistenceTypeInfo {
   public static final boolean debug = false;
   public static final String FILE_PREFIX = "META-INF/WL-CMP-TYPES";
   private String typeIdentifier = null;
   private String typeVersion = null;
   private String typeFileName = null;

   public PersistenceTypeInfoImpl() {
   }

   public PersistenceTypeInfoImpl(String var1, String var2, String var3) {
      this.setIdentifier(var1);
      this.setVersion(var2);
      this.setFileName(var3);
   }

   public String getIdentifier() {
      return this.typeIdentifier;
   }

   public void setIdentifier(String var1) {
      this.typeIdentifier = var1;
   }

   public String getVersion() {
      return this.typeVersion;
   }

   public void setVersion(String var1) {
      this.typeVersion = var1;
   }

   public String getFileName() {
      return this.typeFileName;
   }

   public void setFileName(String var1) {
      this.typeFileName = var1;
   }

   public String generateFileName() {
      this.typeFileName = "META-INF/WL-CMP-TYPES/";
      this.typeFileName = this.typeFileName + "CMP_" + this.typeIdentifier;
      this.typeFileName = this.typeFileName + "" + System.currentTimeMillis();
      return this.typeFileName;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof PersistenceTypeInfoImpl)) {
         return false;
      } else {
         PersistenceTypeInfoImpl var2 = (PersistenceTypeInfoImpl)var1;
         if (!this.typeIdentifier.equals(var2.getIdentifier())) {
            return false;
         } else {
            return this.typeVersion.equals(var2.getVersion());
         }
      }
   }

   public int hashCode() {
      return this.typeIdentifier.hashCode() ^ this.typeVersion.hashCode();
   }
}
