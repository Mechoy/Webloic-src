package weblogic.corba.cos.transactions;

import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPOutputStream;
import weblogic.iiop.TaggedComponent;
import weblogic.utils.Debug;

public final class TransactionPolicyComponent extends TaggedComponent {
   public static final int OTS_POLICY_RESERVED = 0;
   public static final int OTS_POLICY_REQUIRES = 1;
   public static final int OTS_POLICY_FORBIDS = 2;
   public static final int OTS_POLICY_ADAPTS = 3;
   public static final TransactionPolicyComponent EJB_OTS_POLICY = new TransactionPolicyComponent(31, 3);
   public static final TransactionPolicyComponent NON_TX_POLICY = new TransactionPolicyComponent(31, 2);
   private static final TransactionPolicyComponent[] OTS_POLICIES;
   public static final int INVOCATION_POLICY_EITHER = 0;
   public static final int INVOCATION_POLICY_SHARED = 1;
   public static final int INVOCATION_POLICY_UNSHARED = 2;
   public static final TransactionPolicyComponent EJB_INV_POLICY;
   private static final TransactionPolicyComponent[] INV_POLICIES;
   private int policy;

   public TransactionPolicyComponent(int var1, int var2) {
      super(var1);
      this.policy = var2;
   }

   public static TaggedComponent getInvocationPolicy(int var0) {
      Debug.assertion(var0 >= 0 && var0 <= 2);
      return INV_POLICIES[var0];
   }

   public static TaggedComponent getOTSPolicy(int var0) {
      Debug.assertion(var0 > 0 && var0 <= 3);
      return OTS_POLICIES[var0];
   }

   public final int getPolicy() {
      return this.policy;
   }

   public TransactionPolicyComponent(IIOPInputStream var1, int var2) {
      super(var2);
      this.read(var1);
   }

   public final void read(IIOPInputStream var1) {
      long var2 = var1.startEncapsulation();
      this.policy = var1.read_unsigned_short();
      var1.endEncapsulation(var2);
   }

   public final void write(IIOPOutputStream var1) {
      var1.write_long(this.tag);
      long var2 = var1.startEncapsulation();
      var1.write_unsigned_short(this.policy);
      var1.endEncapsulation(var2);
   }

   static {
      OTS_POLICIES = new TransactionPolicyComponent[]{null, new TransactionPolicyComponent(31, 1), new TransactionPolicyComponent(31, 2), EJB_OTS_POLICY};
      EJB_INV_POLICY = new TransactionPolicyComponent(32, 1);
      INV_POLICIES = new TransactionPolicyComponent[]{new TransactionPolicyComponent(32, 0), EJB_INV_POLICY, new TransactionPolicyComponent(32, 2)};
   }
}
