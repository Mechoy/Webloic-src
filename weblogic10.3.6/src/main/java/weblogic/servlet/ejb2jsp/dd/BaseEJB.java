package weblogic.servlet.ejb2jsp.dd;

class BaseEJB {
   String retType;
   String methodName;
   String argType;

   BaseEJB(String var1, String var2, String var3) {
      this.retType = var1;
      this.methodName = var2;
      this.argType = var3;
   }

   boolean matchesDescriptor(EJBMethodDescriptor var1) {
      if (!this.retType.equals(var1.getReturnType())) {
         return false;
      } else if (!this.methodName.equals(var1.getName())) {
         return false;
      } else {
         MethodParamDescriptor[] var2 = var1.getParams();
         if (this.argType != null) {
            return var2 != null && var2.length == 1 ? this.argType.equals(var2[0].getType()) : false;
         } else {
            return var2 == null || var2.length == 0;
         }
      }
   }
}
