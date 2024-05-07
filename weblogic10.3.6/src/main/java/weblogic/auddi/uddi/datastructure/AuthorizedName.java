package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class AuthorizedName extends UDDIElement implements Serializable {
   private String m_name = null;

   public AuthorizedName(String var1) {
      this.setName(var1);
   }

   public AuthorizedName(AuthorizedName var1) {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         this.m_name = var1.m_name;
      }
   }

   public void setName(String var1) {
      this.m_name = this.truncateString(var1, 255);
   }

   public String getName() {
      return this.m_name == null ? "" : this.m_name;
   }

   public String toString() {
      return this.getName();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof AuthorizedName)) {
         return false;
      } else {
         AuthorizedName var2 = (AuthorizedName)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_name, (Object)var2.m_name);
         return var3;
      }
   }

   public String toXML() {
      return this.m_name == null ? "" : "<authorizedName>" + this.fixStringForXML(this.m_name) + "</authorizedName>";
   }
}
