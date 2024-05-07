package weblogic.iiop;

import weblogic.corba.cos.transactions.TransactionPolicyComponent;
import weblogic.corba.idl.poa.MessagingPolicyComponent;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.csi.CompoundSecMechList;
import weblogic.kernel.Kernel;
import weblogic.protocol.ServerIdentity;
import weblogic.utils.Hex;

public class TaggedComponent {
   static final int TAG_ORB_TYPE = 0;
   static final int TAG_CODE_SETS = 1;
   public static final int TAG_POLICIES = 2;
   public static final int TAG_SSL_SEC_TRANS = 20;
   static final int TAG_JAVA_CODEBASE = 25;
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   public static final int TAG_TRANSACTION_POLICY = 26;
   public static final int TAG_OTS_POLICY = 31;
   public static final int TAG_INV_POLICY = 32;
   public static final int TAG_CSI_SEC_MECH_LIST = 33;
   public static final int TAG_NULL_TAG = 34;
   public static final int TAG_TLS_SEC_TRANS = 36;
   public static final int TAG_RMI_CUSTOM_MAX_STREAM_FORMAT = 38;
   static final int TAG_WLS_VERSION = 1111834880;
   public static final int TAG_WLS_CLUSTER_KEY = 1111834883;
   protected final int tag;
   private byte[] component_data;

   public TaggedComponent(int var1) {
      this.tag = var1;
   }

   public TaggedComponent(int var1, IIOPInputStream var2) {
      this.tag = var1;
      this.read(var2);
   }

   final int getTag() {
      return this.tag;
   }

   static final TaggedComponent readComponent(IIOPInputStream var0, ServerIdentity var1) {
      int var2 = var0.read_long();
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("read(" + Integer.toHexString(var2) + ") @" + var0.pos());
      }

      switch (var2) {
         case 1:
            return new CodeSetsComponent(var0);
         case 2:
            return new MessagingPolicyComponent(var0);
         case 20:
            return new SSLSecTransComponent(var0);
         case 25:
            return new CodebaseComponent(var0);
         case 31:
         case 32:
            return new TransactionPolicyComponent(var0, var2);
         case 33:
            return new CompoundSecMechList(var0, var1);
         case 36:
            return new TLSSecTransComponent(var0, var1);
         case 38:
            return new SFVComponent(var0);
         case 1111834883:
            return new ClusterComponent(var0);
         default:
            return new TaggedComponent(var2, var0);
      }
   }

   public void read(IIOPInputStream var1) {
      this.component_data = var1.read_octet_sequence();
   }

   public void write(IIOPOutputStream var1) {
      var1.write_ulong(this.tag);
      var1.write_octet_sequence(this.component_data);
   }

   protected static void p(String var0) {
      System.err.println("<TaggedComponent> " + var0);
   }

   public String toString() {
      return Integer.toHexString(this.tag) + ": " + (this.component_data != null ? Hex.dump(this.component_data, 0, this.component_data.length) : " ");
   }
}
