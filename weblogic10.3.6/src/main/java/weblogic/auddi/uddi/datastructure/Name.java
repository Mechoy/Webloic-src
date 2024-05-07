package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.util.Util;

public class Name extends UDDIListObject implements Serializable {
   private String m_name;
   private Language m_lang;

   public Name(String var1) throws UDDIException {
      this(var1, (Language)null);
   }

   public Name(Name var1) {
      this.m_name = null;
      this.m_lang = null;
      this.m_name = var1.m_name;
      this.m_lang = var1.m_lang;
   }

   public Name(String var1, Language var2) {
      this.m_name = null;
      this.m_lang = null;
      this.setName(var1);
      this.m_lang = var2;
   }

   public void setName(String var1) {
      this.m_name = this.truncateString(var1, 255);
   }

   public void setName(String var1, Language var2) {
      this.setName(var1);
      this.m_lang = var2;
   }

   public String getName() {
      return this.m_name;
   }

   public Language getLang() {
      return this.m_lang;
   }

   public void setLang(Language var1) {
      this.m_lang = var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Name)) {
         return false;
      } else {
         Name var2 = (Name)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_name, (Object)var2.m_name);
         var3 &= Util.isEqual((Object)this.m_lang, (Object)var2.m_lang);
         return var3;
      }
   }

   public String toString() {
      return this.m_name;
   }

   public String toXML() {
      if (this.m_name == null) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         var1.append("<").append("name");
         if (this.m_lang != null) {
            var1.append(this.m_lang.toXML());
         }

         var1.append(">").append(this.fixStringForXML(this.m_name)).append("</").append("name").append(">");
         return var1.toString();
      }
   }
}
