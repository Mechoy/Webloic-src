package weblogic.ejb.container.dd.xml;

import weblogic.ejb.container.EJBTextTextFormatter;

public final class IllegalResourceException extends Exception {
   private static final long serialVersionUID = -8555753153924982052L;
   private String resourceId;

   public IllegalResourceException(String var1) {
      this.resourceId = var1;
   }

   public String getMessage() {
      String var1 = super.getMessage();
      if (var1 != null && var1.length() != 0) {
         return var1;
      } else {
         StringBuffer var2 = new StringBuffer();
         EJBTextTextFormatter var3 = new EJBTextTextFormatter();
         var2.append(var3.noLocalResource(this.resourceId));
         return var2.toString();
      }
   }
}
