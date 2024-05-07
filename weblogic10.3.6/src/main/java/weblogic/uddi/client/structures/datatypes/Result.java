package weblogic.uddi.client.structures.datatypes;

public class Result {
   private ErrInfo errInfo = null;
   private String keyType = null;
   private String errno = null;

   public ErrInfo getErrInfo() {
      return this.errInfo;
   }

   public void setErrInfo(ErrInfo var1) {
      this.errInfo = var1;
   }

   public String getKeyType() {
      return this.keyType;
   }

   public void setKeyType(String var1) {
      this.keyType = var1;
   }

   public String getErrno() {
      return this.errno;
   }

   public void setErrno(String var1) {
      this.errno = var1;
   }
}
