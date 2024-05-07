package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.util.Util;

public class TModelStandardInfo {
   private String m_name = null;
   private String m_description = null;
   private String m_categorization = null;
   private boolean m_checked = false;

   public TModelStandardInfo() {
   }

   public TModelStandardInfo(String var1, String var2, String var3, boolean var4) {
      this.m_name = var1;
      this.m_description = var2;
      this.m_categorization = var3;
      this.m_checked = var4;
   }

   public boolean isChecked() {
      return this.m_checked;
   }

   public void setChecked(boolean var1) {
      this.m_checked = var1;
   }

   public String getName() {
      return this.m_name;
   }

   public void setName(String var1) {
      this.m_name = var1;
   }

   public String getCategorization() {
      return this.m_categorization;
   }

   public void setCategorization(String var1) {
      this.m_categorization = var1;
   }

   public String getDescription() {
      return this.m_description;
   }

   public void setDescription(String var1) {
      this.m_description = var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof TModelStandardInfo)) {
         return false;
      } else {
         TModelStandardInfo var2 = (TModelStandardInfo)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_categorization, (Object)var2.m_categorization);
         var3 &= Util.isEqual((Object)this.m_description, (Object)var2.m_description);
         var3 &= Util.isEqual((Object)this.m_name, (Object)var2.m_name);
         var3 &= this.m_checked == var2.m_checked;
         return var3;
      }
   }
}
