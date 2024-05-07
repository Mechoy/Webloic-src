package weblogic.xml.registry;

public class XMLEntitySpecRegistryEntry extends XMLAbstractRegistryEntry {
   private String entityURI;
   private String whenToCache;
   private int cacheTimeoutInterval;
   private String handleEntityInvalidation;

   public XMLEntitySpecRegistryEntry(String var1, String var2, ConfigAbstraction.EntryConfig var3) {
      super(var1, var2, var3);
   }

   public String getEntityURI() {
      return this.entityURI;
   }

   public void setEntityURI(String var1) {
      this.entityURI = var1;
   }

   public String getWhenToCache() {
      return this.whenToCache;
   }

   public void setWhenToCache(String var1) {
      this.whenToCache = var1;
   }

   public int getCacheTimeoutInterval() {
      return this.cacheTimeoutInterval;
   }

   public void setCacheTimeoutInterval(int var1) {
      this.cacheTimeoutInterval = var1;
   }

   public String getHandleEntityInvalidation() {
      return this.handleEntityInvalidation;
   }

   public void setHandleEntityInvalidation(String var1) {
      this.handleEntityInvalidation = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("publicId = ");
      var1.append(this.getPublicId() == null ? "null, " : "\"" + this.getPublicId() + "\", ");
      var1.append("systemId = ");
      var1.append(this.getSystemId() == null ? "null, " : "\"" + this.getSystemId() + "\", ");
      var1.append("isPrivate = " + this.isPrivate() + ", ");
      var1.append("entityPath = " + this.getEntityURI() + ", ");
      var1.append("whenToCache = " + this.getWhenToCache() + ", ");
      var1.append("cacheTimeoutInterval = " + this.getCacheTimeoutInterval());
      var1.append("handleEntityInvalidation = " + this.getHandleEntityInvalidation());
      return var1.toString();
   }
}
