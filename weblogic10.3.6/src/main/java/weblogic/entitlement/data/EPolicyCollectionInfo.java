package weblogic.entitlement.data;

public class EPolicyCollectionInfo {
   private String name;
   private String version;
   private String timeStamp;

   public EPolicyCollectionInfo(String var1, String var2, String var3) {
      this.name = var1;
      this.version = var2;
      this.timeStamp = var3;
   }

   public String getName() {
      return this.name;
   }

   public String getVersion() {
      return this.version;
   }

   public String getTimestamp() {
      return this.timeStamp;
   }
}
