package weblogic.tools.ui;

import java.util.ArrayList;

public abstract class MultiBeanPropertySet extends PropertySet {
   protected PropertySet[] ps;

   public MultiBeanPropertySet(Class var1, PropertySet[] var2) {
      super(var1, mergeProps(var2));
      this.ps = (PropertySet[])((PropertySet[])var2.clone());
   }

   public abstract void setBean(Object var1);

   public Object createNewBean() {
      Object var1 = super.createNewBean();
      this.customizeNewBean(var1);
      return var1;
   }

   protected void customizeNewBean(Object var1) {
   }

   public void modelToUI() {
      for(int var1 = 0; var1 < this.ps.length; ++var1) {
         this.ps[var1].modelToUI();
      }

   }

   public void uiToModel() {
      for(int var1 = 0; var1 < this.ps.length; ++var1) {
         this.ps[var1].uiToModel();
      }

   }

   private static Property[] mergeProps(PropertySet[] var0) {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         Property[] var3 = var0[var2].getProps();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var1.add(var3[var4]);
         }
      }

      Property[] var5 = new Property[var1.size()];
      var1.toArray(var5);
      return var5;
   }
}
