package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class PersistenceMBeanImpl extends XMLElementMBeanDelegate implements PersistenceMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_isModifiedMethodName = false;
   private String isModifiedMethodName;
   private boolean isSet_persistenceUse = false;
   private PersistenceUseMBean persistenceUse;
   private boolean isSet_delayUpdatesUntilEndOfTx = false;
   private boolean delayUpdatesUntilEndOfTx = true;
   private boolean isSet_findersLoadBean = false;
   private boolean findersLoadBean = true;

   public String getIsModifiedMethodName() {
      return this.isModifiedMethodName;
   }

   public void setIsModifiedMethodName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.isModifiedMethodName;
      this.isModifiedMethodName = var1;
      this.isSet_isModifiedMethodName = var1 != null;
      this.checkChange("isModifiedMethodName", var2, this.isModifiedMethodName);
   }

   public PersistenceUseMBean getPersistenceUse() {
      return this.persistenceUse;
   }

   public void setPersistenceUse(PersistenceUseMBean var1) {
      PersistenceUseMBean var2 = this.persistenceUse;
      this.persistenceUse = var1;
      this.isSet_persistenceUse = var1 != null;
      this.checkChange("persistenceUse", var2, this.persistenceUse);
   }

   public boolean getDelayUpdatesUntilEndOfTx() {
      return this.delayUpdatesUntilEndOfTx;
   }

   public void setDelayUpdatesUntilEndOfTx(boolean var1) {
      boolean var2 = this.delayUpdatesUntilEndOfTx;
      this.delayUpdatesUntilEndOfTx = var1;
      this.isSet_delayUpdatesUntilEndOfTx = true;
      this.checkChange("delayUpdatesUntilEndOfTx", var2, this.delayUpdatesUntilEndOfTx);
   }

   public boolean getFindersLoadBean() {
      return this.findersLoadBean;
   }

   public void setFindersLoadBean(boolean var1) {
      boolean var2 = this.findersLoadBean;
      this.findersLoadBean = var1;
      this.isSet_findersLoadBean = true;
      this.checkChange("findersLoadBean", var2, this.findersLoadBean);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<persistence");
      var2.append(">\n");
      if (null != this.getIsModifiedMethodName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<is-modified-method-name>").append(this.getIsModifiedMethodName()).append("</is-modified-method-name>\n");
      }

      if (this.isSet_delayUpdatesUntilEndOfTx || !this.getDelayUpdatesUntilEndOfTx()) {
         var2.append(ToXML.indent(var1 + 2)).append("<delay-updates-until-end-of-tx>").append(ToXML.capitalize(Boolean.valueOf(this.getDelayUpdatesUntilEndOfTx()).toString())).append("</delay-updates-until-end-of-tx>\n");
      }

      if (this.isSet_findersLoadBean || !this.getFindersLoadBean()) {
         var2.append(ToXML.indent(var1 + 2)).append("<finders-load-bean>").append(ToXML.capitalize(Boolean.valueOf(this.getFindersLoadBean()).toString())).append("</finders-load-bean>\n");
      }

      if (null != this.getPersistenceUse()) {
         var2.append(this.getPersistenceUse().toXML(var1 + 2)).append("\n");
      }

      var2.append(ToXML.indent(var1)).append("</persistence>\n");
      return var2.toString();
   }
}
