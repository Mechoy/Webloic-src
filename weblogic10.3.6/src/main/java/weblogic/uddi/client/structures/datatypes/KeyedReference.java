package weblogic.uddi.client.structures.datatypes;

public class KeyedReference {
   private String tModelKey;
   private String keyName;
   private String keyValue;

   public KeyedReference() {
      this.tModelKey = null;
      this.keyName = null;
      this.keyValue = null;
   }

   public KeyedReference(String var1, String var2, String var3) {
      this.tModelKey = var1;
      this.keyName = var2;
      this.keyValue = var3;
   }

   public void setTModelKey(String var1) {
      this.tModelKey = var1;
   }

   public void setKeyName(String var1) {
      this.keyName = var1;
   }

   public void setKeyValue(String var1) {
      this.keyValue = var1;
   }

   public String getTModelKey() {
      return this.tModelKey;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public String getKeyValue() {
      return this.keyValue;
   }
}
