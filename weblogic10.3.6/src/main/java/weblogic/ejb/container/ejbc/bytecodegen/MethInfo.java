package weblogic.ejb.container.ejbc.bytecodegen;

class MethInfo {
   private String methodName;
   private Class<?>[] args = null;
   private Class<?> retType;
   private Class<?>[] exs;
   private String mdName;

   MethInfo() {
      this.retType = Void.TYPE;
      this.exs = null;
   }

   String getMethodName() {
      return this.methodName;
   }

   String getMdName() {
      return this.mdName;
   }

   Class<?>[] getArgs() {
      return this.args;
   }

   Class<?> getRetType() {
      return this.retType;
   }

   Class<?>[] getExs() {
      return this.exs;
   }

   static Creator of(String var0, String var1) {
      Creator var2 = new Creator();
      var2.mInfo.methodName = var0;
      var2.mInfo.mdName = var1;
      return var2;
   }

   static class Creator {
      private final MethInfo mInfo;

      private Creator() {
         this.mInfo = new MethInfo();
      }

      Creator args(Class<?>... var1) {
         this.mInfo.args = var1;
         return this;
      }

      Creator exceps(Class<?>... var1) {
         this.mInfo.exs = var1;
         return this;
      }

      Creator returns(Class<?> var1) {
         this.mInfo.retType = var1;
         return this;
      }

      MethInfo create() {
         return this.mInfo;
      }

      // $FF: synthetic method
      Creator(Object var1) {
         this();
      }
   }
}
