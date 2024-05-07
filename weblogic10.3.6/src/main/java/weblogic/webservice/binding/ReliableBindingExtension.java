package weblogic.webservice.binding;

/** @deprecated */
public class ReliableBindingExtension extends BindingExtension {
   private boolean dupElim;
   private int retries;
   private int retryIntervalSecs;
   private int persistIntervalSecs;

   public ReliableBindingExtension() {
      this.dupElim = true;
      this.retries = 10;
      this.retryIntervalSecs = 60;
      this.persistIntervalSecs = 600;
      this.setKey(BindingExtension.RELIABLE);
   }

   public ReliableBindingExtension(boolean var1, int var2, int var3, int var4) {
      this();
      this.dupElim = var1;
      this.retries = var2;
      this.retryIntervalSecs = var3;
      this.persistIntervalSecs = var4;
   }

   public boolean isDuplicateElimination() {
      return this.dupElim;
   }

   public void setDuplicateElimination(boolean var1) {
      this.dupElim = var1;
   }

   public int getRetryCount() {
      return this.retries;
   }

   public void setRetryCount(int var1) {
      this.retries = var1;
   }

   public int getRetryInterval() {
      return this.retryIntervalSecs;
   }

   public void setRetryInterval(int var1) {
      this.retryIntervalSecs = var1;
   }

   public int getPersistInterval() {
      return this.persistIntervalSecs;
   }

   public void setPersistInterval(int var1) {
      this.persistIntervalSecs = var1;
   }
}
