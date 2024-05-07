package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class HostingRedirector extends UDDIElement implements Serializable {
   private BindingKey m_bindingKey = null;

   public HostingRedirector(HostingRedirector var1) {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         this.m_bindingKey = var1.m_bindingKey;
      }
   }

   public HostingRedirector(BindingKey var1) {
      this.m_bindingKey = var1;
   }

   public void setBindingKey(BindingKey var1) {
      this.m_bindingKey = var1;
   }

   public BindingKey getBindingKey() {
      return this.m_bindingKey;
   }

   public String toString() {
      return this.m_bindingKey.toString();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof HostingRedirector)) {
         return false;
      } else {
         HostingRedirector var2 = (HostingRedirector)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_bindingKey, (Object)var2.m_bindingKey);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<hostingRedirector");
      if (this.m_bindingKey != null) {
         var1.append(" bindingKey=\"").append(this.m_bindingKey.toString()).append("\"");
      }

      var1.append(">");
      var1.append("</hostingRedirector>");
      return var1.toString();
   }
}
