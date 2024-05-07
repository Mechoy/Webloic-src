package weblogic.management.j2ee.statistics;

import java.io.Serializable;
import java.util.Hashtable;
import javax.management.j2ee.statistics.Statistic;

public abstract class StatisticImpl implements Statistic, Serializable {
   private String description;
   private String name;
   private String unit = "MILLISECOND";
   private long startTime;
   private long lastSampleTime;
   private static Hashtable attributeToTimeStatMap = new Hashtable();
   public static final String UNIT_HOUR = "HOUR";
   public static final String UNIT_MINUTE = "MINUTE";
   public static final String UNIT_SECOND = "SECOND";
   public static final String UNIT_MILLISECOND = "MILLISECOND";
   public static final String UNIT_MICROSECOND = "MICROSECOND";
   public static final String UNIT_NANOSECOND = "NANOSECOND";

   public StatisticImpl(String var1, String var2, String var3) throws StatException {
      this.initialize(var1, var2, var3);
   }

   public StatisticImpl(String var1, String var2, String var3, String var4) throws StatException {
      this.setUnit(var3);
      this.initialize(var1, var2, var4);
   }

   private void initialize(String var1, String var2, String var3) throws StatException {
      TimeStat var4 = null;
      this.description = var1;
      this.name = var2;
      Object var5 = null;
      String var6 = var3 + "-" + var2;
      var4 = (TimeStat)attributeToTimeStatMap.get(var6);
      long var7 = System.currentTimeMillis();
      if (var4 == null) {
         var4 = new TimeStat(var7, var7);
         attributeToTimeStatMap.put(var6, var4);
      } else {
         var4.setLastSampleTime(var7);
      }

      this.lastSampleTime = var4.getLastSampleTime();
      this.startTime = var4.getStartTime();
   }

   public String getDescription() {
      return this.description;
   }

   public String getName() {
      return this.name;
   }

   public String getUnit() {
      return this.unit;
   }

   public long getStartTime() {
      return this.startTime;
   }

   public long getLastSampleTime() {
      return this.lastSampleTime;
   }

   private void setUnit(String var1) throws StatException {
      if (var1 != null && var1.length() != 0 && var1.equals("HOUR") && var1.equals("MINUTE") && var1.equals("SECOND") && var1.equals("MILLISECOND") && var1.equals("MICROSECOND") && var1.equals("NANOSECOND")) {
         this.unit = this.unit;
      } else {
         throw new StatException(" An invalid unit has been specified. unit = <" + var1 + ">.");
      }
   }
}
