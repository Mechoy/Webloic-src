package weblogic.iiop;

public final class UnknownExceptionInfo extends ServiceContext {
   private Throwable t;
   private IIOPInputStream ueStream;

   public UnknownExceptionInfo(Throwable var1) {
      super(9);
      this.t = var1;
   }

   protected UnknownExceptionInfo(IIOPInputStream var1) {
      super(9);
      this.ueStream = new IIOPInputStream(var1);
   }

   public Throwable getNestedThrowable() {
      if (this.t == null && this.ueStream != null) {
         this.readEncapsulation(this.ueStream);
         this.ueStream.close();
      }

      return this.t;
   }

   public void write(IIOPOutputStream var1) {
      this.writeEncapsulatedContext(var1);
   }

   protected void writeEncapsulation(IIOPOutputStream var1) {
      var1.write_value(this.t);
   }

   protected void readEncapsulation(IIOPInputStream var1) {
      this.t = (Throwable)var1.read_value();
   }

   public String toString() {
      return "UnknownExceptionInfo Context (" + this.t + ")";
   }
}
