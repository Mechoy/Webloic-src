package weblogic.iiop;

import weblogic.protocol.LocalServerIdentity;
import weblogic.rjvm.JVMID;

public final class BiDirIIOPContextImpl extends ServiceContext {
   private ConnectionKey[] listenPoints;
   private static BiDirIIOPContextImpl context = new BiDirIIOPContextImpl();

   public BiDirIIOPContextImpl() {
      super(5);
      this.listenPoints = new ConnectionKey[]{new ConnectionKey(JVMID.localID().address().getHostAddress(), -1)};
      this.listenPoints[0].setBidirSet();
   }

   protected BiDirIIOPContextImpl(IIOPInputStream var1) {
      super(5);
      this.readEncapsulatedContext(var1);
   }

   public static final BiDirIIOPContextImpl getContext() {
      return context;
   }

   public ConnectionKey[] getListenPoints() {
      return this.listenPoints;
   }

   public void write(IIOPOutputStream var1) {
      this.writeEncapsulatedContext(var1);
   }

   protected void readEncapsulation(IIOPInputStream var1) {
      int var2 = var1.read_ulong();
      this.listenPoints = new ConnectionKey[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.listenPoints[var3] = new ConnectionKey(var1);
         this.listenPoints[var3].setBidirSet();
      }

   }

   protected void writeEncapsulation(IIOPOutputStream var1) {
      if (this.listenPoints == null) {
         var1.write_ulong(0);
      } else {
         var1.write_ulong(this.listenPoints.length);

         for(int var2 = 0; var2 < this.listenPoints.length; ++var2) {
            this.listenPoints[var2].writeForChannel(var1, LocalServerIdentity.getIdentity());
         }
      }

   }

   public String toString() {
      return "BiDirIIOPContextImpl: " + this.listenPoints == null ? "<null>" : this.listenPoints[0].toString();
   }
}
