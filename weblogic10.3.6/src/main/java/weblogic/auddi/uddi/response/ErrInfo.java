package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIErrorCodes;
import weblogic.auddi.util.Util;

public class ErrInfo {
   private int errno;
   private String errMsg;
   private String errCode;

   public ErrInfo(int var1) {
      this(var1, (String)null);
   }

   public ErrInfo() {
      this(-1, (String)null);
   }

   public ErrInfo(String var1) {
      this(-1, var1);
   }

   public ErrInfo(int var1, String var2) {
      this.errno = -1;
      this.errMsg = null;
      this.errCode = null;
      this.errno = var1;
      this.errCode = UDDIErrorCodes.getCode(var1);
      this.errMsg = var2;
   }

   public int getErrCode() {
      return this.errno;
   }

   public String getErrCodeString() {
      return this.errCode;
   }

   public void setErrCode(String var1) {
      this.errCode = var1;
   }

   public void setErrMsg(String var1) {
      this.errMsg = var1;
   }

   public String getErrMsg() {
      return this.errMsg;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      if (this.errMsg == null) {
         var1.append("<errInfo errCode=\"" + this.errCode + "\">");
      } else {
         var1.append("<errInfo errCode=\"" + this.errCode + "\">");
         var1.append(Util.fixStringForXML(this.errMsg));
      }

      var1.append("</errInfo>");
      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof ErrInfo)) {
         return false;
      } else {
         ErrInfo var2 = (ErrInfo)var1;
         boolean var3 = true;
         var3 &= this.errno == var2.errno;
         var3 &= Util.isEqual((Object)this.errMsg, (Object)var2.errMsg);
         var3 &= Util.isEqual((Object)this.errCode, (Object)var2.errCode);
         return var3;
      }
   }
}
