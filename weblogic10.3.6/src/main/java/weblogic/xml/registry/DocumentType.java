package weblogic.xml.registry;

class DocumentType {
   private String rootTag;
   private String publicId;
   private String systemId;

   public DocumentType(String var1, String var2, String var3) {
      this.publicId = var1 == null ? null : (var1.trim().length() == 0 ? null : var1);
      this.systemId = var2 == null ? null : (var2.trim().length() == 0 ? null : var2);
      this.rootTag = var3 == null ? null : (var3.trim().length() == 0 ? null : var3);
   }

   public String toString() {
      return "[PublicID=" + this.publicId + ", SystemID=" + this.systemId + ", Root=" + this.rootTag + "]";
   }

   public boolean equals(Object var1) {
      DocumentType var2 = null;

      try {
         var2 = (DocumentType)var1;
      } catch (ClassCastException var4) {
         return false;
      }

      return this.nullableStringEquals(this.publicId, var2.publicId) && this.nullableStringEquals(this.systemId, var2.systemId) && this.nullableStringEquals(this.rootTag, var2.rootTag);
   }

   public int hashCode() {
      int var1 = 0;
      if (this.publicId != null) {
         var1 ^= this.publicId.hashCode();
      }

      if (this.systemId != null) {
         var1 ^= this.systemId.hashCode();
      }

      if (this.rootTag != null) {
         var1 ^= this.rootTag.hashCode();
      }

      return var1;
   }

   private boolean nullableStringEquals(String var1, String var2) {
      if (var1 == null) {
         return var2 == null;
      } else {
         return var1.equals(var2);
      }
   }
}
