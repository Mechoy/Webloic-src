package weblogic.entitlement.rules;

public final class TimeOfDay implements Comparable {
   public int MS_PER_MINUTE = 60000;
   public int MS_PER_HOUR;
   public int MS_PER_DAY;
   private int time;

   public TimeOfDay(int var1) {
      this.MS_PER_HOUR = 60 * this.MS_PER_MINUTE;
      this.MS_PER_DAY = 24 * this.MS_PER_HOUR;
      this.time = 0;
      if (var1 >= 0 && var1 < this.MS_PER_DAY) {
         this.time = var1;
      } else {
         throw new IllegalArgumentException("Time of day must be >= 0 and < " + this.MS_PER_DAY);
      }
   }

   public TimeOfDay(int var1, int var2, int var3) {
      this.MS_PER_HOUR = 60 * this.MS_PER_MINUTE;
      this.MS_PER_DAY = 24 * this.MS_PER_HOUR;
      this.time = 0;
      if (var1 >= 0 && var1 <= 23) {
         if (var2 >= 0 && var2 <= 59) {
            if (var3 >= 0 && var3 <= 59) {
               this.time = var1 * this.MS_PER_HOUR + var2 * this.MS_PER_MINUTE + var3 * 1000;
            } else {
               throw new IllegalArgumentException("Illegal seconds value " + var3);
            }
         } else {
            throw new IllegalArgumentException("Illegal minutes value " + var2);
         }
      } else {
         throw new IllegalArgumentException("Illegal hours value " + var1);
      }
   }

   public int getTime() {
      return this.time;
   }

   public int getHours() {
      return this.time / this.MS_PER_HOUR;
   }

   public int getMinutes() {
      return this.time % this.MS_PER_HOUR / this.MS_PER_MINUTE;
   }

   public int getSeconds() {
      return this.time % this.MS_PER_MINUTE / 1000;
   }

   public boolean equals(Object var1) {
      return var1 == this || var1 instanceof TimeOfDay && this.time == ((TimeOfDay)var1).getTime();
   }

   public int hashCode() {
      return this.time;
   }

   public int compareTo(Object var1) {
      int var2 = ((TimeOfDay)var1).getTime();
      return this.time < var2 ? -1 : (this.time > var2 ? 1 : 0);
   }
}
