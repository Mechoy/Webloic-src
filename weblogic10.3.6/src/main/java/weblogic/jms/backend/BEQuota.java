package weblogic.jms.backend;

import java.util.HashMap;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.QuotaBean;
import weblogic.management.utils.GenericBeanListener;
import weblogic.messaging.kernel.Quota;
import weblogic.messaging.kernel.QuotaPolicy;

public final class BEQuota {
   private static final HashMap quotaSignature = new HashMap();
   private String name;
   private Quota quota;
   private int references = 0;
   private GenericBeanListener listener;

   BEQuota(String var1, Quota var2) {
      this.name = var1;
      this.quota = var2;
   }

   String getName() {
      return this.name;
   }

   void setQuotaBean(QuotaBean var1) {
      if (this.listener == null) {
         DescriptorBean var2 = (DescriptorBean)var1;
         this.listener = new GenericBeanListener(var2, this, quotaSignature);
      }

      ++this.references;
   }

   boolean close() {
      --this.references;
      if (this.references <= 0 && this.listener != null) {
         this.listener.close();
         this.listener = null;
         this.references = 0;
         return true;
      } else {
         return false;
      }
   }

   Quota getQuota() {
      return this.quota;
   }

   public synchronized void setBytesMaximum(long var1) {
      this.quota.setBytesMaximum(var1);
   }

   public synchronized void setMessagesMaximum(long var1) {
      int var3;
      if (var1 > 2147483647L) {
         var3 = Integer.MAX_VALUE;
      } else {
         var3 = (int)var1;
      }

      this.quota.setMessagesMaximum(var3);
   }

   public synchronized void setPolicy(String var1) {
      this.quota.setPolicy(this.findPolicy(var1));
   }

   private QuotaPolicy findPolicy(String var1) {
      if (var1 == null) {
         return QuotaPolicy.FIFO;
      } else if (var1.equalsIgnoreCase("FIFO")) {
         return QuotaPolicy.FIFO;
      } else if (var1.equalsIgnoreCase("Preemptive")) {
         return QuotaPolicy.PREEMPTIVE;
      } else {
         throw new AssertionError("Invalid quota policy " + var1);
      }
   }

   static {
      quotaSignature.put("BytesMaximum", Long.TYPE);
      quotaSignature.put("MessagesMaximum", Long.TYPE);
      quotaSignature.put("Policy", String.class);
   }
}
