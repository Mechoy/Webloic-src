package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.util.StandardLanguages;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.PropertyManager;
import weblogic.auddi.util.Util;

public class Language extends UDDIElement {
   public static final String DEFAULT_LANG = PropertyManager.getRuntimeProperty("auddi.default.lang");
   private String m_lang;
   private String m_name;
   private String m_family;

   public Language() throws UDDIException {
      this(DEFAULT_LANG == null ? "en" : DEFAULT_LANG);
   }

   public Language(String var1) throws UDDIException {
      this.m_lang = null;
      this.m_name = null;
      this.m_family = null;
      if (var1 != null) {
         Language var2 = StandardLanguages.getInstance().getByCode(var1);
         if (var2 == null) {
            throw new IllegalArgumentException(UDDIMessages.get("error.runtime.language.invalid"));
         }

         this.m_lang = var2.m_lang;
         this.m_name = var2.m_name;
         this.m_family = var2.m_family;
      }

   }

   public Language(String var1, String var2, String var3) {
      this.m_lang = null;
      this.m_name = null;
      this.m_family = null;
      this.m_lang = var1;
      this.m_name = var2;
      this.m_family = var3;
   }

   public Language(Language var1) {
      this.m_lang = null;
      this.m_name = null;
      this.m_family = null;
      if (var1 == null) {
         throw new IllegalArgumentException("Parameter to copy constructor was null");
      } else {
         this.m_lang = var1.m_lang;
         this.m_name = var1.m_name;
         this.m_family = var1.m_family;
      }
   }

   public String getFamily() {
      return this.m_family;
   }

   public String getName() {
      return this.m_name;
   }

   public String getText() {
      return this.getLang();
   }

   public String getLang() {
      return this.m_lang == null ? "" : this.m_lang;
   }

   public String toString() {
      return this.m_lang == null ? "" : this.m_lang;
   }

   public String toXML() {
      if (this.m_lang == null) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         var1.append(" ").append("xml:lang").append("=\"").append(this.m_lang).append("\"");
         return var1.toString();
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Language)) {
         return false;
      } else {
         Language var2 = (Language)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_lang, (Object)var2.m_lang);
         if (!var3) {
            Logger.debug("Language not equal:::" + this.m_lang + "!=" + var2.m_lang);
         }

         return var3;
      }
   }
}
