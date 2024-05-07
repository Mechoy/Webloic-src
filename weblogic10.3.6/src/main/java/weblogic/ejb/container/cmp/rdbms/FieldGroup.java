package weblogic.ejb.container.cmp.rdbms;

import java.util.TreeSet;

public final class FieldGroup {
   private String name;
   private int index;
   private TreeSet cmpFields = new TreeSet();
   private TreeSet cmrFields = new TreeSet();

   public void setName(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public void addCmpField(String var1) {
      this.cmpFields.add(var1);
   }

   public TreeSet getCmpFields() {
      return this.cmpFields;
   }

   public void addCmrField(String var1) {
      this.cmrFields.add(var1);
   }

   public TreeSet getCmrFields() {
      return this.cmrFields;
   }

   public void setIndex(int var1) {
      this.index = var1;
   }

   public int getIndex() {
      return this.index;
   }

   public String toString() {
      return "[FieldGroup name:" + this.name + " cmp fields:" + this.cmpFields + " cmr fields:" + this.cmrFields + "]";
   }
}
