package weblogic.spring.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.SpringTransactionManagerRuntimeMBean;

public class SpringTransactionManagerRuntimeMBeanImpl extends SpringBaseRuntimeMBeanImpl implements SpringTransactionManagerRuntimeMBean {
   private long rollbackCount;
   private long commitCount;
   private long resumeCount;
   private long suspendCount;
   private long failedRollbacks;
   private long failedCommits;
   private long failedResumes;
   private long failedSuspends;

   public SpringTransactionManagerRuntimeMBeanImpl(Object var1, String var2, Object var3) throws ManagementException {
      super(var1, var3, var2, false);
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringTransactionManagerRuntimeMBeanImpl(" + var2 + ")");
      }

   }

   public synchronized long getRollbackCount() {
      return this.rollbackCount;
   }

   public synchronized long getCommitCount() {
      return this.commitCount;
   }

   public synchronized long getResumeCount() {
      return this.resumeCount;
   }

   public synchronized long getSuspendCount() {
      return this.suspendCount;
   }

   public synchronized long getRollbackFailedCount() {
      return this.failedRollbacks;
   }

   public synchronized long getCommitFailedCount() {
      return this.failedCommits;
   }

   public synchronized long getResumeFailedCount() {
      return this.failedResumes;
   }

   public synchronized long getSuspendFailedCount() {
      return this.failedSuspends;
   }

   public synchronized void addRollback(boolean var1) {
      ++this.rollbackCount;
      if (!var1) {
         ++this.failedRollbacks;
      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringTransactionManagerRuntimeMBeanImpl.addRollback() : " + this.name);
      }

   }

   public synchronized void addCommit(boolean var1) {
      ++this.commitCount;
      if (!var1) {
         ++this.failedCommits;
      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringTransactionManagerRuntimeMBeanImpl.addCommit() : " + this.name);
      }

   }

   public synchronized void addResume(boolean var1) {
      ++this.resumeCount;
      if (!var1) {
         ++this.failedResumes;
      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringTransactionManagerRuntimeMBeanImpl.addResume() : " + this.name);
      }

   }

   public synchronized void addSuspend(boolean var1) {
      ++this.suspendCount;
      if (!var1) {
         ++this.failedSuspends;
      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringTransactionManagerRuntimeMBeanImpl.addSuspend() : " + this.name);
      }

   }
}
