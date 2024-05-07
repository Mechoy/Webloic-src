package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import java.util.Date;
import weblogic.auddi.uddi.UDDILengths;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.UDDITags;
import weblogic.auddi.util.Util;

public abstract class UDDIElement implements Serializable, UDDITags, UDDILengths {
   protected Date m_date = null;

   protected String fixStringForXML(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = Util.fixStringForXML(var1);
         return var1;
      }
   }

   protected String truncateString(String var1, int var2) {
      String var3 = var1;
      if (var1 == null) {
         return null;
      } else if (var2 < 0) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.stringLength.0"));
      } else {
         if (var1.length() > var2) {
            var3 = var1.substring(0, var2);
         }

         return var3;
      }
   }

   public void setDate(Date var1) {
      this.m_date = var1;
   }

   public Date getDate() {
      return this.m_date;
   }

   public abstract String toXML();
}
