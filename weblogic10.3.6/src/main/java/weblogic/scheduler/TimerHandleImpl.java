package weblogic.scheduler;

public class TimerHandleImpl implements TimerHandle {
   private static final long serialVersionUID = 5583868027857027818L;
   private String id;

   public TimerHandleImpl(String var1) {
      this.id = var1;
   }

   public Timer getTimer() throws NoSuchObjectLocalException, TimerException {
      return new TimerImpl(this.id);
   }

   public int hashCode() {
      int var1 = 17;
      if (this.id != null) {
         var1 *= this.id.hashCode();
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof TimerHandleImpl)) {
         return false;
      } else {
         TimerHandleImpl var2 = (TimerHandleImpl)var1;
         if (this.id == null && var2.id == null) {
            return true;
         } else {
            return this.id != null && this.id.equals(var2.id);
         }
      }
   }
}
