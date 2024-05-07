package weblogic.servlet.ejb2jsp;

import java.util.ArrayList;
import java.util.List;
import weblogic.servlet.ejb2jsp.dd.EJBTaglibDescriptor;

public class EJBTaglibGenerator {
   private List methods = new ArrayList();
   EJBTaglibDescriptor dd;
   BeanGenerator[] bgs;

   public EJBTaglibGenerator(EJBTaglibDescriptor var1) {
      this.dd = var1;
   }

   public void setGenerators(BeanGenerator[] var1) {
      this.bgs = (BeanGenerator[])((BeanGenerator[])var1.clone());
   }

   public EJBTaglibDescriptor getDD() {
      return this.dd;
   }

   private void mangleConflictingClassNames() {
      EJBMethodGenerator[] var1 = new EJBMethodGenerator[this.methods.size()];
      this.methods.toArray(var1);
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2 - 1; ++var3) {
         String var4 = var1[var3].generated_class_name();
         int var5 = 0;

         for(int var6 = var3 + 1; var6 < var2; ++var6) {
            if (var4.equals(var1[var6].generated_class_name())) {
               var1[var3].setGeneratedClassName(var4 + var5++);
            }
         }
      }

   }

   public String[] generateSources() throws Exception {
      this.methods.clear();

      for(int var1 = 0; var1 < this.bgs.length; ++var1) {
         List var2 = this.bgs[var1].getMethods();
         this.methods.addAll(var2);
      }

      this.mangleConflictingClassNames();
      ArrayList var5 = new ArrayList();

      for(int var6 = 0; var6 < this.bgs.length; ++var6) {
         String[] var3 = this.bgs[var6].generateSources();

         for(int var4 = 0; var3 != null && var4 < var3.length; ++var4) {
            var5.add(var3[var4]);
         }
      }

      String[] var7 = new String[var5.size()];
      var5.toArray(var7);
      return var7;
   }

   static void p(String var0) {
      System.err.println("[ejbtaggen]: " + var0);
   }

   public String toXML() {
      return (new TLDOutputter(this.methods)).generate();
   }
}
