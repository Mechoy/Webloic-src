package weblogic.iiop;

import weblogic.corba.cos.codebase.CodeBaseImpl;

public final class SendingContextRunTime extends ServiceContext {
   private IOR codebase;
   private static final SendingContextRunTime runtime = new SendingContextRunTime();

   public SendingContextRunTime(IOR var1) {
      super(6);
      this.codebase = var1;
   }

   public SendingContextRunTime() {
      super(6);
      this.codebase = CodeBaseImpl.getCodeBase().getIOR();
   }

   public static final SendingContextRunTime getSendingContextRuntime() {
      return runtime;
   }

   public final IOR getCodeBase() {
      return this.codebase;
   }

   protected SendingContextRunTime(IIOPInputStream var1) {
      super(6);
      this.readEncapsulatedContext(var1);
   }

   public void write(IIOPOutputStream var1) {
      this.writeEncapsulatedContext(var1);
   }

   protected void writeEncapsulation(IIOPOutputStream var1) {
      this.codebase.write(var1);
   }

   protected void readEncapsulation(IIOPInputStream var1) {
      this.codebase = new IOR(var1);
   }

   public String toString() {
      return "SendingContextRunTime Context (" + this.codebase + ")";
   }
}
