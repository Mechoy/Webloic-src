package weblogic.servlet.ejb2jsp;

import weblogic.servlet.ejb2jsp.dd.EJBMethodDescriptor;
import weblogic.utils.Getopt2;

public class HomeMethodGenerator extends EJBMethodGenerator {
   public HomeMethodGenerator(Getopt2 var1, BeanGenerator var2, EJBMethodDescriptor var3) {
      super(var1, var2, var3);
   }

   protected String getTemplatePath() {
      return "/weblogic/servlet/ejb2jsp/homemethodtag.j";
   }

   public String generated_class_name() {
      if (this.generatedClassName == null) {
         String var1 = this.homeType();
         int var2 = var1.lastIndexOf(46);
         if (var2 > 0) {
            var1 = var1.substring(var2 + 1);
         }

         this.generatedClassName = this.bg.getPackage() + "._" + var1 + "_" + this.getMethodName() + "Tag";
      }

      return this.generatedClassName;
   }

   public boolean equals(Object var1) {
      return !(var1 instanceof HomeMethodGenerator) ? false : super.equals(var1);
   }
}
