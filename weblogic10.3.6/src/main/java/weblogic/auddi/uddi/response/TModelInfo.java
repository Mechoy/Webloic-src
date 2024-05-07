package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.TModelKey;
import weblogic.auddi.uddi.datastructure.UDDIListObject;
import weblogic.auddi.util.Util;

public class TModelInfo extends UDDIListObject {
   private TModelKey tModelKey;
   private Name name;
   private boolean m_isHidden;

   public TModelInfo() {
      this.tModelKey = null;
      this.name = null;
   }

   public TModelInfo(TModelKey var1, String var2) throws UDDIException {
      this.tModelKey = var1;
      if (this.name != null && !this.name.equals("")) {
         this.name = new Name(var2);
      } else {
         this.name = null;
      }

   }

   public TModelKey getKey() {
      return this.tModelKey;
   }

   public void setKey(TModelKey var1) {
      this.tModelKey = var1;
   }

   public String getName() throws UDDIException {
      return this.name == null ? null : this.name.getName();
   }

   public void setName(String var1) throws UDDIException {
      if (this.name == null) {
         this.name = new Name(var1);
      } else {
         this.name.setName(var1);
      }

   }

   public boolean isHidden() {
      return this.m_isHidden;
   }

   public void setHidden(boolean var1) {
      this.m_isHidden = var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof TModelInfo)) {
         return false;
      } else {
         TModelInfo var2 = (TModelInfo)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.tModelKey, (Object)var2.tModelKey);
         var3 &= Util.isEqual((Object)this.name, (Object)var2.name);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<tModelInfo ");
      if (this.tModelKey != null) {
         var1.append("tModelKey=\"" + this.tModelKey.toString() + "\"");
      }

      var1.append(">");
      if (this.name != null) {
         var1.append(this.name.toXML());
      }

      var1.append("</tModelInfo>");
      return var1.toString();
   }
}
