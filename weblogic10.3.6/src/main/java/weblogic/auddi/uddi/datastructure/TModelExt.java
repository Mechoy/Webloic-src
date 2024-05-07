package weblogic.auddi.uddi.datastructure;

import java.util.ArrayList;
import java.util.List;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class TModelExt extends TModel {
   public static String ORIGIN_USER = "user";
   public static String ORIGIN_STANDARD = "standard";
   private String m_origin = null;
   private String m_type = null;
   private boolean m_checked = false;
   private List m_scopes = new ArrayList();

   public TModelExt() {
   }

   public TModelExt(TModelKey var1) {
      super(var1);
   }

   public TModelExt(TModel var1) throws UDDIException {
      super(var1);
      if (var1 instanceof TModelExt) {
         TModelExt var2 = (TModelExt)var1;
         List var3 = var2.m_scopes;
         if (var3.size() > 0) {
            for(int var4 = 0; var4 < var3.size(); ++var4) {
               this.m_scopes.add(var3.get(var4));
            }
         }

         if (var2.m_origin != null) {
            this.m_origin = var2.m_origin;
         }

         if (var2.m_origin != null) {
            this.m_origin = var2.m_origin;
         }

         if (var2.m_type != null) {
            this.m_type = var2.m_type;
         }

         this.m_checked = var2.m_checked;
      }

   }

   public TModelExt(TModelKey var1, String var2) throws UDDIException {
      super(var1, var2);
   }

   public boolean isChecked() {
      return this.m_checked;
   }

   public void setChecked(boolean var1) {
      this.m_checked = var1;
   }

   public String getOrigin() {
      return this.m_origin;
   }

   public void setOrigin(String var1) {
      if (!var1.equals(ORIGIN_USER) && !var1.equals(ORIGIN_STANDARD)) {
         throw new IllegalArgumentException(UDDIMessages.get("error.invalid.tmodel.origin", var1));
      } else {
         this.m_origin = var1;
      }
   }

   public String getType() {
      return this.m_type;
   }

   public void setType(String var1) throws UDDIException {
      this.m_type = var1;
   }

   public void addApplicableScope(String var1) {
      this.m_scopes.add(var1);
   }

   public List getApplicableScopes() {
      return this.m_scopes;
   }
}
