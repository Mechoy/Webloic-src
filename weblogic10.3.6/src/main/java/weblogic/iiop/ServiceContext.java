package weblogic.iiop;

import weblogic.corba.cos.transactions.PropagationContextImpl;
import weblogic.corba.idl.poa.MessagingPolicy;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.csi.SASServiceContext;
import weblogic.kernel.Kernel;

public class ServiceContext {
   public static final int TransactionService = 0;
   public static final int CodeSets = 1;
   public static final int ChainBypassCheck = 2;
   public static final int ChainBypassInfo = 3;
   public static final int LogicalThreadId = 4;
   public static final int BI_DIR_IIOP = 5;
   public static final int SendingContextRunTime = 6;
   public static final int INVOCATION_POLICIES = 7;
   public static final int FORWARDED_IDENTITY = 8;
   public static final int UnknownExceptionInfo = 9;
   public static final int SecurityAttributeService = 15;
   public static final int RMICustomMaxStreamFormat = 17;
   int context_id;
   protected byte[] context_data;
   protected static final boolean DEBUG = false;
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");

   public String toString() {
      return "ServiceContext: ContextId = " + VMCIDToString(this.context_id);
   }

   public static String VMCIDToString(int var0) {
      StringBuffer var1 = new StringBuffer();
      int var2 = var0 >> 24 & 255;
      var1.append(var2 >= 32 && var2 <= 126 ? Character.toString((char)var2) : "\\" + Integer.toString(var2));
      var2 = var0 >> 16 & 255;
      var1.append(var2 >= 32 && var2 <= 126 ? Character.toString((char)var2) : "\\" + Integer.toString(var2));
      var2 = var0 >> 8 & 255;
      var1.append(var2 >= 32 && var2 <= 126 ? Character.toString((char)var2) : "\\" + Integer.toString(var2));
      var2 = var0 & 255;
      var1.append(var2 >= 32 && var2 <= 126 ? Character.toString((char)var2) : "\\" + Integer.toString(var2));
      return var1.toString();
   }

   protected ServiceContext(int var1, IIOPInputStream var2) {
      this.context_id = var1;
      this.read(var2);
   }

   public ServiceContext(int var1, byte[] var2) {
      this.context_id = var1;
      this.context_data = var2;
   }

   public ServiceContext(int var1) {
      this(var1, (byte[])null);
   }

   public ServiceContext() {
   }

   public int getContextId() {
      return this.context_id;
   }

   public byte[] getContextData() {
      return this.context_data;
   }

   public static ServiceContext readServiceContext(IIOPInputStream var0) {
      int var1 = var0.read_long();
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("read(" + Integer.toHexString(var1) + ")");
      }

      switch (var1) {
         case 0:
            return new PropagationContextImpl(var0);
         case 1:
            return new CodeSet(var0);
         case 2:
         case 3:
         case 4:
         case 8:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 16:
         default:
            if ((var1 & -256) == 1111834880) {
               return VendorInfo.readServiceContext(var1, var0);
            }

            return new ServiceContext(var1, var0);
         case 5:
            return new BiDirIIOPContextImpl(var0);
         case 6:
            return new SendingContextRunTime(var0);
         case 7:
            return new MessagingPolicy(var0);
         case 9:
            return new UnknownExceptionInfo(var0);
         case 15:
            return new SASServiceContext(var0);
         case 17:
            return new SFVContext(var0);
      }
   }

   protected void readEncapsulation(IIOPInputStream var1) {
   }

   protected void writeEncapsulation(IIOPOutputStream var1) {
   }

   public void write(IIOPOutputStream var1) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("write(" + this.toString() + ")");
      }

      var1.write_long(this.context_id);
      if (this.context_data == null) {
         var1.write_long(0);
      } else {
         var1.write_octet_sequence(this.context_data);
      }

   }

   public final void writeEncapsulatedContext(IIOPOutputStream var1) {
      var1.write_long(this.context_id);
      if (this.context_data != null) {
         var1.write_octet_sequence(this.context_data);
      } else {
         long var2 = var1.startEncapsulation();
         this.writeEncapsulation(var1);
         var1.endEncapsulation(var2);
      }

   }

   public final void premarshal() {
      IIOPOutputStream var1 = new IIOPOutputStream();
      var1.putEndian();
      this.writeEncapsulation(var1);
      this.context_data = var1.getBuffer();
      var1.close();
   }

   protected final void read(IIOPInputStream var1) {
      this.context_data = var1.read_octet_sequence();
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("read(" + this.toString() + ")");
      }

   }

   protected final void readEncapsulatedContext(IIOPInputStream var1) {
      long var2 = var1.startEncapsulation();
      if (var2 != 0L) {
         this.readEncapsulation(var1);
         var1.endEncapsulation(var2);
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("readEncapsulatedContext(" + this.toString() + ")");
         }
      }

   }

   protected static void p(String var0) {
      System.err.println("<ServiceContext> " + var0);
   }
}
