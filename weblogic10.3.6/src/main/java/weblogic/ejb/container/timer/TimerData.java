package weblogic.ejb.container.timer;

import java.io.Serializable;
import java.util.Date;

public final class TimerData implements Serializable {
   private Long id;
   private Object pk;
   private Serializable info;
   private Date nextExpiration;
   private long intervalDuration;
   private long retryDelay = 0L;
   private int maxRetryAttempts = -1;
   private int successfulTimeouts = 0;
   private int maxTimeouts = 0;
   private int failureAction = 2;

   public Long getTimerId() {
      return this.id;
   }

   public void setTimerId(Long var1) {
      this.id = var1;
   }

   public Object getPk() {
      return this.pk;
   }

   public void setPk(Object var1) {
      this.pk = var1;
   }

   public Serializable getInfo() {
      return this.info;
   }

   public void setInfo(Serializable var1) {
      this.info = var1;
   }

   public Date getNextExpiration() {
      return this.nextExpiration;
   }

   public void setNextExpiration(Date var1) {
      this.nextExpiration = var1;
   }

   public long getIntervalDuration() {
      return this.intervalDuration;
   }

   public void setIntervalDuration(long var1) {
      this.intervalDuration = var1;
   }

   public long getRetryDelay() {
      return this.retryDelay;
   }

   public void setRetryDelay(long var1) {
      this.retryDelay = var1;
   }

   public int getMaxRetryAttempts() {
      return this.maxRetryAttempts;
   }

   public void setMaxRetryAttempts(int var1) {
      this.maxRetryAttempts = var1;
   }

   public int getMaxTimeouts() {
      return this.maxTimeouts;
   }

   public void setMaxTimeouts(int var1) {
      this.maxTimeouts = var1;
   }

   public int getFailureAction() {
      return this.failureAction;
   }

   public void setFailureAction(int var1) {
      this.failureAction = var1;
   }

   public int getSuccessfulTimeouts() {
      return this.successfulTimeouts;
   }

   public void setSuccessfulTimeouts(int var1) {
      this.successfulTimeouts = var1;
   }

   public void incrementSuccessfulTimeouts() {
      ++this.successfulTimeouts;
   }
}
