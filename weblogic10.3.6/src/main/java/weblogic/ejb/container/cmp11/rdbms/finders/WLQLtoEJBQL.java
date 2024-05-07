package weblogic.ejb.container.cmp11.rdbms.finders;

import weblogic.ejb.container.ejbc.EJBCException;
import weblogic.ejb20.cmp.rdbms.RDBMSException;

public final class WLQLtoEJBQL {
   public static String doWLQLtoEJBQL(String var0, String var1) throws RDBMSException {
      String var2 = "SELECT DISTINCT OBJECT(o) FROM " + var1 + " o " + doWLQLtoEJBQL(var0);
      return var2;
   }

   public static String doWLQLtoEJBQL(String var0) throws RDBMSException {
      WLQLExpression var1 = null;
      WLQLParser var2 = new WLQLParser();
      if (var0 != null && var0.length() != 0) {
         try {
            var1 = var2.parse(var0);
         } catch (EJBCException var7) {
            throw new RDBMSException("Could not parse WLQL: " + var7.toString());
         }

         WLQLtoEJBQLExpander var3 = new WLQLtoEJBQLExpander(var1);
         String var4 = "";

         try {
            String var5 = var3.toEJBQL();
            if (var5 != null) {
               var4 = "WHERE " + var3.toEJBQL();
            }

            return var4;
         } catch (IllegalExpressionException var6) {
            throw new RDBMSException(var6.toString());
         }
      } else {
         return "";
      }
   }

   public static void main(String[] var0) {
      if (var0.length < 1) {
         System.out.println("\nUsage:  WLQLtoEJBQL <wlql-string>\n\n");
         System.exit(0);
      }

      String var1 = "";

      try {
         var1 = doWLQLtoEJBQL(var0[0]);
      } catch (RDBMSException var3) {
         System.out.println(var3.toString());
         System.exit(0);
      }

      System.out.println("EJBQL: '" + var1 + "'");
   }
}
