package weblogic.servlet.logging;

import weblogic.servlet.HTTPLogger;
import weblogic.utils.StringUtils;

public final class LogFormat {
   private LogField[] entryFormat;

   public LogFormat(String var1) {
      String[] var2 = StringUtils.splitCompletely(var1);
      this.entryFormat = new LogField[var2.length];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         String var4 = null;
         String var5;
         if (var2[var3].indexOf("x-") != -1) {
            try {
               var5 = null;
               var4 = var2[var3].substring(var2[var3].indexOf("x-") + 2, var2[var3].length());
               Object var14 = Class.forName(var4, true, Thread.currentThread().getContextClassLoader()).newInstance();
               CustomELFLogger var15 = (CustomELFLogger)var14;
               this.entryFormat[var3] = var15;
               continue;
            } catch (ClassNotFoundException var10) {
               HTTPLogger.logELFApplicationFieldFailure(var2[var3], var10);
            } catch (InstantiationException var11) {
               HTTPLogger.logELFApplicationFieldFailure(var2[var3], var11);
            } catch (IllegalAccessException var12) {
               HTTPLogger.logELFApplicationFieldFailure(var2[var3], var12);
            } catch (ClassCastException var13) {
               HTTPLogger.logELFApplicationFieldFailureCCE(var2[var3], var4);
            }
         }

         if (!"time".equals(var2[var3]) && !"date".equals(var2[var3]) && !"bytes".equals(var2[var3]) && !"time-taken".equals(var2[var3])) {
            var5 = null;
            String var6 = null;
            String var7 = null;
            int var8;
            if ((var8 = var2[var3].indexOf(40)) != -1) {
               int var9;
               if ((var9 = var2[var3].indexOf(41)) == -1) {
                  HTTPLogger.logELFApplicationFieldFormatError(var2[var3]);
                  continue;
               }

               var5 = var2[var3].substring(0, var8);
               var6 = var2[var3].substring(var8 + 1, var9);
            } else if ((var8 = var2[var3].indexOf(45)) != -1) {
               var5 = var2[var3].substring(0, var8);
               var7 = var2[var3].substring(var8 + 1);
            }

            if (var6 != null) {
               this.entryFormat[var3] = new HeaderLogField(var5, var6);
            } else if (!"uri".equals(var7) && !"uri-stem".equals(var7) && !"uri-query".equals(var7) && !"comment".equals(var7) && !"method".equals(var7) && !"status".equals(var7)) {
               if (!"ip".equals(var7) && !"dns".equals(var7)) {
                  this.entryFormat[var3] = new NullLogField();
               } else {
                  this.entryFormat[var3] = new HostLogField(var5, var7);
               }
            } else {
               this.entryFormat[var3] = new URILogField(var5, var7);
            }
         } else {
            this.entryFormat[var3] = new TimeLogField(var2[var3]);
         }
      }

   }

   public int countFields() {
      return this.entryFormat.length;
   }

   public LogField getFieldAt(int var1) {
      return this.entryFormat[var1];
   }
}
