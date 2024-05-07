package weblogic.kernel;

final class ServerExecuteThread extends ExecuteThread {
   private ClassLoader contextClassLoader;
   private ClassLoader defaultContextClassLoader;

   ServerExecuteThread(int var1, ExecuteThreadManager var2) {
      super(var1, var2);
   }

   ServerExecuteThread(int var1, ExecuteThreadManager var2, ThreadGroup var3) {
      super(var1, var2, var3);
   }

   protected void init(ExecuteThreadManager var1) {
      super.init(var1);
      this.defaultContextClassLoader = super.getContextClassLoader();
      this.setContextClassLoader(this.defaultContextClassLoader);
   }

   public ClassLoader getContextClassLoader() {
      return this.contextClassLoader;
   }

   public void setContextClassLoader(ClassLoader var1) {
      this.contextClassLoader = var1 != null ? var1 : ClassLoader.getSystemClassLoader();
   }

   protected final void reset() {
      super.reset();
      this.setContextClassLoader(this.defaultContextClassLoader);
   }

   public ClassLoader getDefaultContextClassLoader() {
      return this.defaultContextClassLoader;
   }
}
