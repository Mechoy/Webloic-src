package weblogic.cache;

public class StallEvent {
   private int stallTime;
   private String scope;
   private String key;

   public StallEvent(int var1, String var2, String var3) {
      this.stallTime = var1;
      this.scope = var2;
      this.key = var3;
   }

   public int getStallTime() {
      return this.stallTime;
   }

   public String getScope() {
      return this.scope;
   }

   public String getKey() {
      return this.key;
   }
}
