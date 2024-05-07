package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.UDDIListObject;
import weblogic.auddi.util.Util;

public class Result extends UDDIListObject {
   private int errno;
   private ErrInfo errInfo;
   private String keyType;

   public Result(int var1, String var2) {
      this.errno = -1;
      this.errInfo = null;
      this.keyType = null;
      this.errno = var1;
      this.setErrInfo(new ErrInfo(var1, var2));
   }

   public Result(int var1) {
      this(var1, (String)null);
   }

   public Result(UDDIException var1) {
      this(var1.getErrno(), var1.getMessage());
   }

   public String getErrMsg() {
      String var1 = null;
      if (this.errInfo != null) {
         var1 = this.errInfo.getErrMsg();
      }

      return var1;
   }

   public String getErrCode() {
      return this.errInfo != null ? this.errInfo.getErrCodeString() : "";
   }

   public Result() {
      this(-1, (String)null);
   }

   public int getErrno() {
      return this.errno;
   }

   public void setErrno(String var1) {
      this.errno = Integer.parseInt(var1);
   }

   public void setErrno(int var1) {
      this.errno = var1;
   }

   public void setErrInfo(ErrInfo var1) {
      this.errInfo = var1;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<result errno=\"" + this.getErrno() + "\"");
      if (this.keyType != null) {
         var1.append(" keyType=\"" + this.keyType + "\"");
      }

      var1.append(">");
      var1.append(this.errInfo.toXML());
      var1.append("</result>");
      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Result)) {
         return false;
      } else {
         Result var2 = (Result)var1;
         boolean var3 = true;
         var3 &= this.errno == var2.errno;
         var3 &= Util.isEqual((Object)this.errInfo, (Object)var2.errInfo);
         return var3;
      }
   }
}
