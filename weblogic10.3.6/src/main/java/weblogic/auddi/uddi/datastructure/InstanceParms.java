package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class InstanceParms extends UDDIElement {
   private String m_parms = null;

   public InstanceParms(String var1) throws UDDIException {
      this.m_parms = this.truncateString(var1, 255);
   }

   public InstanceParms(InstanceParms var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         this.m_parms = var1.m_parms;
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof InstanceParms)) {
         return false;
      } else {
         InstanceParms var2 = (InstanceParms)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_parms, (Object)var2.m_parms);
         return var3;
      }
   }

   public String toXML() {
      if (this.m_parms == null) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         var1.append("<instanceParms>").append(this.fixStringForXML(this.m_parms)).append("</instanceParms>");
         return var1.toString();
      }
   }

   public String toString() {
      return this.m_parms;
   }
}
