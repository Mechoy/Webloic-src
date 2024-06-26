package weblogic.messaging.saf.internal;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.SAFStatisticsCommonMBean;

public class SAFStatisticsCommonMBeanImpl extends SAFMessageCursorRuntimeImpl implements SAFStatisticsCommonMBean {
   public SAFStatisticsCommonMBeanImpl(String var1, RuntimeMBean var2, boolean var3) throws ManagementException {
      super(var1, var2, var3);
   }

   public long getMessagesCurrentCount() {
      return 0L;
   }

   public long getMessagesPendingCount() {
      return 0L;
   }

   public long getMessagesHighCount() {
      return 0L;
   }

   public long getMessagesReceivedCount() {
      return 0L;
   }

   public long getMessagesThresholdTime() {
      return 0L;
   }

   public long getBytesCurrentCount() {
      return 0L;
   }

   public long getBytesPendingCount() {
      return 0L;
   }

   public long getBytesHighCount() {
      return 0L;
   }

   public long getBytesReceivedCount() {
      return 0L;
   }

   public long getBytesThresholdTime() {
      return 0L;
   }

   public long getFailedMessagesTotal() {
      return 0L;
   }
}
