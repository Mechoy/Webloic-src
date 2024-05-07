package weblogic.ejb.container.cmp.rdbms;

public final class ObjectLink {
   public static final boolean verbose = false;
   public static final boolean debug = false;
   private String beanField = null;
   private String dbmsColumn = null;

   public ObjectLink() {
   }

   public ObjectLink(String var1, String var2) {
      this.setBeanField(var1);
      this.setDBMSColumn(var2);
   }

   public void setBeanField(String var1) {
      this.beanField = var1;
   }

   public String getBeanField() {
      return this.beanField;
   }

   public void setDBMSColumn(String var1) {
      this.dbmsColumn = var1;
   }

   public String getDBMSColumn() {
      return this.dbmsColumn;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof ObjectLink)) {
         return false;
      } else {
         ObjectLink var2 = (ObjectLink)var1;
         if (!this.beanField.equals(var2.getBeanField())) {
            return false;
         } else {
            return this.dbmsColumn.equals(var2.getDBMSColumn());
         }
      }
   }

   public int hashCode() {
      return this.beanField.hashCode() | this.dbmsColumn.hashCode();
   }

   public String toString() {
      return "[ObjectLink: field:" + this.beanField + " to column:" + this.dbmsColumn + "]";
   }
}
