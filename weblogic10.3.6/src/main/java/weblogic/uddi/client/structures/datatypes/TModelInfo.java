package weblogic.uddi.client.structures.datatypes;

public class TModelInfo {
   private Name name = null;
   private String tModelKey = null;

   public Name getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = new Name(var1);
   }

   public void setName(Name var1) {
      this.name = var1;
   }

   public String getTModelKey() {
      return this.tModelKey;
   }

   public void setTModelKey(String var1) {
      this.tModelKey = var1;
   }
}
