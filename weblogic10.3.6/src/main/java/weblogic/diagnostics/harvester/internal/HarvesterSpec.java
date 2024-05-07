package weblogic.diagnostics.harvester.internal;

import weblogic.diagnostics.harvester.HarvesterException;
import weblogic.diagnostics.harvester.I18NConstants;

final class HarvesterSpec implements I18NConstants {
   private String typeName = null;
   private boolean isEnabled = true;
   private StringArray requestedHarvestableAttributes = null;
   private StringArray requestedHarvestableInstances = null;

   HarvesterSpec(String var1, String[] var2, String[] var3, boolean var4) throws HarvesterException.NullName {
      if (var1 == null) {
         throw new HarvesterException.NullName(TYPE_I18N);
      } else {
         this.typeName = var1;
         this.isEnabled = var4;
         this.requestedHarvestableAttributes = new StringArray(var2);
         this.requestedHarvestableAttributes.normalizeEntries();
         this.requestedHarvestableInstances = new StringArray(var3);
         this.requestedHarvestableInstances.normalizeEntries();
      }
   }

   String getTypeName() {
      return this.typeName;
   }

   public boolean isEnabled() {
      return this.isEnabled;
   }

   public void setEnabled(boolean var1) {
      this.isEnabled = var1;
   }

   private class StringArray {
      private String[] array;

      private StringArray(String[] var2) {
         this.array = var2;
      }

      private String[] getStringArray() {
         return this.array;
      }

      private void normalizeEntries() {
         String[] var1 = (String[])this.getStringArray();
         if (var1 != null) {
            int var2 = this.normalizeEntries(0, 0);
            if (var2 != 0) {
               String[] var3 = new String[var1.length - var2];
               int var4 = 0;

               for(int var5 = 0; var5 < var1.length; ++var5) {
                  String var6 = var1[var5];
                  if (var6 != null) {
                     var3[var4] = var6;
                     ++var4;
                  }
               }

               this.array = var3;
            }

         }
      }

      private int normalizeEntries(int var1, int var2) {
         String[] var3 = (String[])this.getStringArray();
         if (var1 + 1 > var3.length) {
            return 0;
         } else {
            if (var1 + 1 < var3.length) {
               var2 += this.normalizeEntries(var1 + 1, var2);
            }

            String var4 = var3[var1];
            var4 = var4.trim();
            var3[var1] = var4;

            int var5;
            for(var5 = var1 + 1; var5 < var3.length; ++var5) {
               String var6 = var3[var5];
               if (var4.equals(var6)) {
                  break;
               }
            }

            if (var5 != var3.length) {
               ++var2;
               var3[var1] = null;
            }

            return var2;
         }
      }

      public String toString() {
         String[] var1 = (String[])this.getStringArray();
         if (var1 == null) {
            return "<ALL>";
         } else {
            String var2 = "[";

            for(int var3 = 0; var3 < var1.length; ++var3) {
               String var4 = var1[var3];
               if (var3 != 0) {
                  var2 = var2 + ",";
               }

               var2 = var2 + var4;
            }

            var2 = var2 + "]";
            return var2;
         }
      }

      // $FF: synthetic method
      StringArray(String[] var2, Object var3) {
         this(var2);
      }
   }
}
