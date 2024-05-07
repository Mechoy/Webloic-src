package weblogic.iiop;

import org.omg.CORBA.MARSHAL;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;

public final class TargetAddress {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   short addressingDisposition;
   ObjectKey object_key;
   IORAddressingInfo ior;
   TaggedProfile taggedProfile;

   TargetAddress(IOR var1, int var2) {
      this.addressingDisposition = 2;
      this.ior = new IORAddressingInfo(var1, var2);
   }

   TargetAddress(ObjectKey var1) {
      this.addressingDisposition = 0;
      this.object_key = var1;
   }

   TargetAddress(IIOPInputStream var1) {
      this.read(var1);
   }

   public final void reset() {
      this.object_key = null;
      this.ior = null;
      this.taggedProfile = null;
   }

   final void read(IIOPInputStream var1) {
      this.addressingDisposition = var1.read_short();
      switch (this.addressingDisposition) {
         case 0:
            this.object_key = new ObjectKey(var1);
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("read(ObjectKey)");
            }
            break;
         case 1:
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("read(TaggedProfile)");
            }

            this.taggedProfile = new TaggedProfile(var1);
            break;
         case 2:
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("read(IORAddressingInfo)");
            }

            this.ior = new IORAddressingInfo(var1);
            this.object_key = this.ior.ior.getProfile().getObjectKey();
            break;
         default:
            throw new MARSHAL("Unknown addressing disposition: " + this.addressingDisposition);
      }

   }

   final void write(IIOPOutputStream var1) {
      var1.write_short(this.addressingDisposition);
      switch (this.addressingDisposition) {
         case 0:
            this.object_key.write(var1);
            break;
         case 1:
            this.taggedProfile.write(var1);
            break;
         case 2:
            this.ior.write(var1);
            break;
         default:
            throw new MARSHAL("Unknown addressing disposition: " + this.addressingDisposition);
      }

   }

   static void p(String var0) {
      System.err.println("<TargetAddress> " + var0);
   }
}
