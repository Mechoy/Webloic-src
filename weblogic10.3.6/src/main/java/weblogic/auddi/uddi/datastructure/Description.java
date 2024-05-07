package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.Util;

public class Description extends UDDIListObject implements Serializable {
   Language m_lang;
   String m_text;

   public Description(String var1, Language var2) {
      this.m_lang = null;
      this.m_text = null;
      this.m_text = this.truncateString(var1, 255);
      this.m_lang = var2;
   }

   public Description(String var1) throws UDDIException {
      this(var1, new Language());
   }

   public Description(Description var1) throws UDDIException {
      this.m_lang = null;
      this.m_text = null;
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         if (var1.m_lang != null) {
            this.m_lang = new Language(var1.m_lang);
         }

         this.m_text = var1.m_text;
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Description)) {
         return false;
      } else {
         Description var2 = (Description)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_lang, (Object)var2.m_lang);
         var3 &= Util.isEqual((Object)this.m_text, (Object)var2.m_text);
         if (!var3) {
            Logger.debug("Description not equal:::" + this.m_text + "!=" + var2.m_text);
         }

         return var3;
      }
   }

   public Language getLang() {
      return this.m_lang;
   }

   public void setLang(Language var1) {
      this.m_lang = var1;
   }

   public String getText() {
      return this.m_text;
   }

   public String toString() {
      return this.m_text;
   }

   public String toXML() {
      if (this.m_text == null) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         var1.append("<").append("description");
         if (this.m_lang != null) {
            var1.append(" ").append("xml:lang").append("=\"").append(this.m_lang.toString()).append("\"");
         }

         var1.append(">").append(this.fixStringForXML(this.m_text)).append("</").append("description").append(">");
         return var1.toString();
      }
   }
}
