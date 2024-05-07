package weblogic.cluster;

class CompositeKey {
   public String name;
   public String appId;

   public CompositeKey(String var1, String var2) {
      this.name = var1;
      this.appId = var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof CompositeKey)) {
         return false;
      } else {
         CompositeKey var2 = (CompositeKey)var1;
         return this.name.equals(var2.name) && (this.appId == null && var2.appId == null || this.appId.equals(var2.appId));
      }
   }

   public int hashCode() {
      return this.appId == null ? this.name.hashCode() : this.name.hashCode() ^ this.appId.hashCode();
   }
}
