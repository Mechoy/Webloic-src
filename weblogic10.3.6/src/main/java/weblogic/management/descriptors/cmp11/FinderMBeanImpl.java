package weblogic.management.descriptors.cmp11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class FinderMBeanImpl extends XMLElementMBeanDelegate implements FinderMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_finderParams = false;
   private List finderParams;
   private boolean isSet_finderName = false;
   private String finderName;
   private boolean isSet_finderQuery = false;
   private String finderQuery;
   private boolean isSet_findForUpdate = false;
   private boolean findForUpdate = false;
   private boolean isSet_finderSQL = false;
   private String finderSQL;

   public String[] getFinderParams() {
      if (this.finderParams == null) {
         return new String[0];
      } else {
         String[] var1 = new String[this.finderParams.size()];
         var1 = (String[])((String[])this.finderParams.toArray(var1));
         return var1;
      }
   }

   public void setFinderParams(String[] var1) {
      String[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getFinderParams();
      }

      this.isSet_finderParams = true;
      if (this.finderParams == null) {
         this.finderParams = Collections.synchronizedList(new ArrayList());
      } else {
         this.finderParams.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.finderParams.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("FinderParams", var2, this.getFinderParams());
      }

   }

   public void addFinderParam(String var1) {
      this.isSet_finderParams = true;
      if (this.finderParams == null) {
         this.finderParams = Collections.synchronizedList(new ArrayList());
      }

      this.finderParams.add(var1);
   }

   public String getFinderName() {
      return this.finderName;
   }

   public void setFinderName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.finderName;
      this.finderName = var1;
      this.isSet_finderName = var1 != null;
      this.checkChange("finderName", var2, this.finderName);
   }

   public String getFinderQuery() {
      return this.finderQuery;
   }

   public void setFinderQuery(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.finderQuery;
      this.finderQuery = var1;
      this.isSet_finderQuery = var1 != null;
      this.checkChange("finderQuery", var2, this.finderQuery);
   }

   public boolean isFindForUpdate() {
      return this.findForUpdate;
   }

   public void setFindForUpdate(boolean var1) {
      boolean var2 = this.findForUpdate;
      this.findForUpdate = var1;
      this.isSet_findForUpdate = true;
      this.checkChange("findForUpdate", var2, this.findForUpdate);
   }

   public String getFinderSQL() {
      return this.finderSQL;
   }

   public void setFinderSQL(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.finderSQL;
      this.finderSQL = var1;
      this.isSet_finderSQL = var1 != null;
      this.checkChange("finderSQL", var2, this.finderSQL);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<finder");
      var2.append(">\n");
      if (null != this.getFinderName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<finder-name>").append(this.getFinderName()).append("</finder-name>\n");
      }

      for(int var3 = 0; var3 < this.getFinderParams().length; ++var3) {
         var2.append(ToXML.indent(var1 + 2)).append("<finder-param>").append(this.getFinderParams()[var3]).append("</finder-param>\n");
      }

      if (null != this.getFinderQuery()) {
         var2.append(ToXML.indent(var1 + 2)).append("<finder-query>").append("<![CDATA[" + this.getFinderQuery() + "]]>").append("</finder-query>\n");
      }

      if (null != this.getFinderSQL()) {
         var2.append(ToXML.indent(var1 + 2)).append("<finder-sql>").append("<![CDATA[" + this.getFinderSQL() + "]]>").append("</finder-sql>\n");
      }

      if (this.isSet_findForUpdate || this.isFindForUpdate()) {
         var2.append(ToXML.indent(var1 + 2)).append("<find-for-update>").append(ToXML.capitalize(Boolean.valueOf(this.isFindForUpdate()).toString())).append("</find-for-update>\n");
      }

      var2.append(ToXML.indent(var1)).append("</finder>\n");
      return var2.toString();
   }
}
