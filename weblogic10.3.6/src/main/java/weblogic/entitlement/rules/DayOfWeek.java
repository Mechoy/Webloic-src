package weblogic.entitlement.rules;

import java.util.Locale;

public final class DayOfWeek {
   public static final DayOfWeek SUNDAY = new DayOfWeek(1, "Sunday", "Sunday");
   public static final DayOfWeek MONDAY = new DayOfWeek(2, "Monday", "Monday");
   public static final DayOfWeek TUESDAY = new DayOfWeek(3, "Tuesday", "Tuesday");
   public static final DayOfWeek WEDNESDAY = new DayOfWeek(4, "Wednesday", "Wednesday");
   public static final DayOfWeek THURSDAY = new DayOfWeek(5, "Thursday", "Thursday");
   public static final DayOfWeek FRIDAY = new DayOfWeek(6, "Friday", "Friday");
   public static final DayOfWeek SATURDAY = new DayOfWeek(7, "Saturday", "Saturday");
   private int id;
   private String name;
   private String nameId;

   private DayOfWeek(int var1, String var2, String var3) {
      this.id = var1;
      this.name = var2;
      this.nameId = var3;
   }

   public int getCalendarDayId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public String getLocalizedName(Locale var1) {
      return Localizer.getText(this.nameId, var1);
   }

   public static DayOfWeek getInstance(int var0) {
      if (var0 == SUNDAY.id) {
         return SUNDAY;
      } else if (var0 == MONDAY.id) {
         return MONDAY;
      } else if (var0 == TUESDAY.id) {
         return TUESDAY;
      } else if (var0 == WEDNESDAY.id) {
         return WEDNESDAY;
      } else if (var0 == THURSDAY.id) {
         return THURSDAY;
      } else if (var0 == FRIDAY.id) {
         return FRIDAY;
      } else if (var0 == SATURDAY.id) {
         return SATURDAY;
      } else {
         throw new IllegalArgumentException("Unexpected day of week id: " + var0);
      }
   }
}
