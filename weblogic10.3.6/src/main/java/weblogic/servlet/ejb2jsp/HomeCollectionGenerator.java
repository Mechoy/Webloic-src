package weblogic.servlet.ejb2jsp;

import weblogic.servlet.ejb2jsp.dd.EJBMethodDescriptor;
import weblogic.utils.Getopt2;

public class HomeCollectionGenerator extends HomeFinderGenerator {
   boolean isEnumeration;

   public void setIsEnumeration(boolean var1) {
      this.isEnumeration = var1;
   }

   public HomeCollectionGenerator(Getopt2 var1, BeanGenerator var2, EJBMethodDescriptor var3) {
      super(var1, var2, var3);
   }

   protected String getTemplatePath() {
      return "/weblogic/servlet/ejb2jsp/homecollectiontag.j";
   }
}
