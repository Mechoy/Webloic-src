package weblogic.wtc.tbridge;

import com.bea.core.jatmi.common.ntrace;

final class tBsetPriority {
   private static final int TUXMINP = 1;
   private static final int TUXMAXP = 100;
   private static final int JMSMINP = 0;
   private static final int JMSMAXP = 9;

   public tBsetPriority() {
   }

   public boolean setPVmap(tBpvalueMap var1, tBstartArgs var2) {
      boolean var3 = ntrace.isTraceEnabled(1);
      if (var3) {
         ntrace.doTrace("[/tBsetPriority/setPVmap/");
      }

      String var4 = new String("JMS2TUX");
      if (var1.Pway == 1) {
         var4 = "TUX2JMS";
      }

      if (var3) {
         ntrace.doTrace("/tBsetPriority/setPVmap/P:Pway=" + var4 + " Prange=" + var1.Prange + " Pval=" + var1.Pvalue);
      }

      int var5 = var1.Prange.indexOf("-");
      if (var5 == -1) {
         var1.Pstart = Integer.parseInt(var1.Prange);
         var1.Pend = var1.Pstart;
      } else {
         var1.Pstart = Integer.parseInt(var1.Prange.substring(0, var5));
         var1.Pend = Integer.parseInt(var1.Prange.substring(var5 + 1, var1.Prange.length()));
      }

      if (var3) {
         ntrace.doTrace("/tBsetPriority/setPVmap/P:Pstart=" + var1.Pstart + " Pend:" + var1.Pend + " S:TmapLen=" + var2.pMapJmsToTux.length + " JmapLen=" + var2.pMapTuxToJms.length);
      }

      boolean var6;
      var6 = true;
      int var7;
      label101:
      switch (var1.Pway) {
         case 1:
            if (var1.Pstart > var1.Pend) {
               if (var3) {
                  ntrace.doTrace("/tBsetPriority/setPVmap/TUX2JMS: JMS range start greater than range end");
               }

               var6 = false;
            } else if (var1.Pstart >= 0 && var1.Pend <= 9) {
               if (var1.Pvalue >= 1 && var1.Pvalue <= 100) {
                  var7 = var1.Pstart;

                  while(true) {
                     if (var7 > var1.Pend) {
                        break label101;
                     }

                     var2.pMapJmsToTux[var7] = var1.Pvalue;
                     ++var7;
                  }
               } else {
                  if (var3) {
                     ntrace.doTrace("/tBsetPriority/setPVmap/TUX2JMS: TUX value out of bounds");
                  }

                  var6 = false;
               }
            } else {
               if (var3) {
                  ntrace.doTrace("/tBsetPriority/setPVmap/TUX2JMS: JMS range out of bounds");
               }

               var6 = false;
            }
            break;
         case 2:
            if (var1.Pstart > var1.Pend) {
               if (var3) {
                  ntrace.doTrace("/tBsetPriority/setPVmap/JMS2TUX: TUX range start greater than range end");
               }

               var6 = false;
            } else if (var1.Pstart >= 1 && var1.Pend <= 100) {
               if (var1.Pvalue >= 0 && var1.Pvalue <= 9) {
                  for(var7 = var1.Pstart; var7 <= var1.Pend; ++var7) {
                     var2.pMapTuxToJms[var7] = var1.Pvalue;
                  }
               } else {
                  if (var3) {
                     ntrace.doTrace("/tBsetPriority/setPVmap/JMS2TUX: JMS value out of bounds");
                  }

                  var6 = false;
               }
            } else {
               if (var3) {
                  ntrace.doTrace("/tBsetPriority/setPVmap/JMS2TUX: TUX range out of bounds");
               }

               var6 = false;
            }
      }

      if (var3) {
         ntrace.doTrace("]/tBsetPriority/setPVmap/" + var6);
      }

      return var6;
   }
}
