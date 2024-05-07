package weblogic.iiop;

import weblogic.transaction.internal.PropagationContext;

public final class VendorInfoTx extends ServiceContext {
   PropagationContext ctx;

   public VendorInfoTx() {
      super(1111834881);
   }

   public VendorInfoTx(PropagationContext var1) {
      super(1111834881);
      this.ctx = var1;
   }

   public PropagationContext getTxContext() {
      return this.ctx;
   }

   protected VendorInfoTx(IIOPInputStream var1) {
      super(1111834881);
      this.readEncapsulatedContext(var1);
   }

   public void write(IIOPOutputStream var1) {
      this.writeEncapsulatedContext(var1);
   }

   protected void readEncapsulation(IIOPInputStream var1) {
      this.ctx = (PropagationContext)var1.read_value();
   }

   protected void writeEncapsulation(IIOPOutputStream var1) {
      var1.write_value(this.ctx);
   }

   public String toString() {
      return "VendorInfoTx Context: " + this.ctx;
   }
}
