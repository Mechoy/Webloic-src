package weblogic.uddi.client.structures.response;

public abstract class ListResponse extends Response {
   protected boolean truncated = false;

   public void setTruncated(boolean var1) {
      this.truncated = var1;
   }

   public boolean isTruncated() {
      return this.truncated;
   }
}
