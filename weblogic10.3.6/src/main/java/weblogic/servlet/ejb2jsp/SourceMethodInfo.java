package weblogic.servlet.ejb2jsp;

import java.util.List;
import weblogic.servlet.ejb2jsp.dd.EJBMethodDescriptor;
import weblogic.servlet.ejb2jsp.dd.MethodParamDescriptor;

public class SourceMethodInfo {
   private String methodName;
   private String retType;
   private String[][] params;

   private String[][] doParams(List var1, List var2) {
      String[][] var3 = new String[][]{new String[var1.size()], null};
      var1.toArray(var3[0]);
      var3[1] = new String[var2.size()];
      var2.toArray(var3[1]);
      return var3;
   }

   public String toString() {
      return this.retType + ' ' + this.methodName + '(' + this.getArgListString() + ')';
   }

   public String getArgListString() {
      StringBuffer var1 = new StringBuffer();
      int var2 = this.params[0].length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1.append(this.params[0][var3]);
         var1.append(' ');
         var1.append(this.params[1][var3]);
         if (var3 + 1 != var2) {
            var1.append(',');
         }
      }

      return var1.toString();
   }

   public String[][] getParams() {
      return this.params;
   }

   public SourceMethodInfo(String var1, String var2, List var3, List var4) {
      this.methodName = var1;
      this.retType = var2;
      if (var3.size() != var4.size()) {
         throw new IllegalArgumentException("type list size " + var3.size() + "!=" + "name list size " + var4.size());
      } else {
         this.params = this.doParams(var3, var4);
      }
   }

   public String getMethodName() {
      return this.methodName;
   }

   public String getReturnType() {
      return this.retType;
   }

   public static boolean equalClassNames(String var0, String var1) {
      int var2 = var0.lastIndexOf(46);
      if (var2 >= 0) {
         var0 = var0.substring(var2 + 1);
      }

      var2 = var1.lastIndexOf(46);
      if (var2 >= 0) {
         var1 = var1.substring(var2 + 1);
      }

      return var0.equals(var1);
   }

   static void p(String var0) {
      System.err.println("[SMI]: " + var0);
   }

   public boolean equalsMethod(EJBMethodDescriptor var1) {
      if (!this.methodName.equals(var1.getName())) {
         return false;
      } else {
         String var2 = this.getReturnType();
         String var3 = var1.getReturnType();
         if (Utils.isVoid(var2)) {
            if (!Utils.isVoid(var3)) {
               return false;
            }
         } else if (!equalClassNames(var3, var2)) {
            return false;
         }

         String[] var4 = this.getParams()[0];
         MethodParamDescriptor[] var5 = var1.getParams();
         if (var5 != null && var5.length != 0) {
            if (var5.length != var4.length) {
               return false;
            } else {
               for(int var6 = 0; var6 < var5.length; ++var6) {
                  if (!equalClassNames(var4[var6], var5[var6].getType())) {
                     return false;
                  }
               }

               return true;
            }
         } else {
            return var4 == null || var4.length == 0;
         }
      }
   }
}
