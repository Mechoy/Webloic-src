package weblogic.uddi.client.structures.datatypes;

public class ErrInfo extends TextNode {
   private String errCode;

   public ErrInfo() {
      this.errCode = null;
   }

   public ErrInfo(String var1, String var2) {
      super(var2);
      this.errCode = var1;
   }

   public String getErrCode() {
      return this.errCode;
   }

   public void setErrCode(String var1) {
      this.errCode = var1;
   }
}
