package weblogic.wtc.jatmi;

import com.bea.core.jatmi.common.ntrace;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import weblogic.utils.collections.NumericHashtable;
import weblogic.utils.collections.NumericValueHashtable;

public final class HTableParser {
   private static HTableParser _instance;
   private static final int FLD_SHORT = 0;
   private static final int FLD_LONG = 1;
   private static final int FLD_CHAR = 2;
   private static final int FLD_FLOAT = 3;
   private static final int FLD_DOUBLE = 4;
   private static final int FLD_STRING = 5;
   private static final int FLD_CARRAY = 6;
   private static final int FLD_INT = 7;
   private static final int FLD_DECIMAL = 8;
   private static final int FLD_PTR = 9;
   private static final int FLD_FML32 = 10;
   private static final int FLD_VIEW32 = 11;
   private static final int FNMASK32 = 33554431;
   private static final int FTMASK32 = 63;
   private static final int FTSHIFT32 = 25;
   private static final int FNMASK = 8191;
   private static final int FTMASK = 7;
   private static final int FTSHIFT = 13;
   private NumericValueHashtable nametofieldHashTable;
   private NumericHashtable fieldtonameHashTable;
   private Vector fieldNameSet;

   public static HTableParser getInstance(String var0, boolean var1) {
      return getInstance(var0, var1, 101, 0.75F);
   }

   public static synchronized HTableParser getInstance(String var0, boolean var1, int var2, float var3) {
      if (_instance == null) {
         _instance = new HTableParser(var0, var1, var2, var3);
      }

      return _instance;
   }

   public HTableParser(String var1, boolean var2) {
      this(var1, var2, 101, 0.75F);
   }

   public HTableParser(String var1, boolean var2, int var3, float var4) {
      BufferedReader var6 = null;
      int var10 = 0;
      int var11 = 0;
      boolean var14 = false;
      boolean var15 = false;
      boolean var16 = false;
      boolean var18 = ntrace.isTraceEnabled(4);
      if (var18) {
         ntrace.doTrace("[/HTableParser/Startup/Initial Capacity = " + var3 + "/Load Factor = " + var4);
      }

      File var5 = new File(var1);

      try {
         var6 = new BufferedReader(new FileReader(var5));
      } catch (FileNotFoundException var23) {
         if (var18) {
            ntrace.doTrace("]/HTableParser/Startup/Could not find file " + var1 + "/20");
         }

         ClassLoader var20 = Thread.currentThread().getContextClassLoader();
         InputStream var21 = var20.getResourceAsStream(var1);
         if (var21 == null) {
            if (var18) {
               ntrace.doTrace("]/HTableParser/Startup/Could not find resource " + var1 + "/25");
            }

            return;
         }

         var6 = new BufferedReader(new InputStreamReader(var21));
      }

      this.nametofieldHashTable = new NumericValueHashtable(var3, var4);
      this.fieldtonameHashTable = new NumericHashtable(var3, var4);
      this.fieldNameSet = new Vector();

      while(true) {
         String var7;
         int var9;
         try {
            if ((var7 = var6.readLine()) == null) {
               break;
            }

            var7 = var7.trim();
            ++var11;
            var9 = 0;
         } catch (IOException var22) {
            System.out.println("Finished! " + var22);
            if (var18) {
               ntrace.doTrace("]/HTableParser/Startup/Finished!/30");
            }
            break;
         }

         int var8 = var7.length();
         if (var8 != 0 && !var7.startsWith("#") && !var7.startsWith("$")) {
            int var12;
            if (var7.startsWith("*base")) {
               if (!Character.isWhitespace(var7.charAt(5))) {
                  if (var18) {
                     ntrace.doTrace("]/HTableParser/Startup/*base must be followed by white space/40");
                  }

                  return;
               }

               for(var9 = 6; Character.isWhitespace(var7.charAt(var9)); ++var9) {
               }

               if (!Character.isDigit(var7.charAt(var9))) {
                  if (var18) {
                     ntrace.doTrace("]/HTableParser/Startup/*base must be followed by a numeric value/50");
                  }

                  return;
               }

               for(var12 = var9++; var9 < var7.length() && Character.isDigit(var7.charAt(var9)); ++var9) {
               }

               var10 = new Integer(var7.substring(var12, var9));
            } else {
               while(!Character.isWhitespace(var7.charAt(var9))) {
                  ++var9;
               }

               String var13;
               for(var13 = var7.substring(0, var9); Character.isWhitespace(var7.charAt(var9)); ++var9) {
               }

               if (!Character.isDigit(var7.charAt(var9))) {
                  if (var18) {
                     ntrace.doTrace("]/HTableParser/Startup/" + var13 + " must be followed by a numeric value/60");
                  }

                  return;
               }

               for(var12 = var9++; Character.isDigit(var7.charAt(var9)); ++var9) {
               }

               int var24 = new Integer(var7.substring(var12, var9)) + var10;
               if (var2) {
                  if (var24 > 33554431) {
                     if (var18) {
                        ntrace.doTrace("]/HTableParser/Startup/" + var24 + " must be less than or equal to " + 33554431 + "/70");
                     }

                     return;
                  }
               } else if (var24 > 8191) {
                  if (var18) {
                     ntrace.doTrace("]/HTableParser/Startup/" + var24 + " must be less than or equal to " + 8191 + "/80");
                  }

                  return;
               }

               while(Character.isWhitespace(var7.charAt(var9))) {
                  ++var9;
               }

               var12 = var9;

               while(!Character.isWhitespace(var7.charAt(var9)) && var7.charAt(var9) != '\n') {
                  ++var9;
                  if (var9 >= var8) {
                     var9 = var8;
                     break;
                  }
               }

               String var17 = var7.substring(var12, var9);
               byte var26;
               if (var17.equals("char")) {
                  var26 = 2;
               } else if (var17.equals("string")) {
                  var26 = 5;
               } else if (var17.equals("short")) {
                  var26 = 0;
               } else if (var17.equals("long")) {
                  var26 = 1;
               } else if (var17.equals("float")) {
                  var26 = 3;
               } else if (var17.equals("double")) {
                  var26 = 4;
               } else if (var17.equals("carray")) {
                  var26 = 6;
               } else if (var2 && var17.equals("ptr")) {
                  var26 = 9;
               } else if (var2 && var17.equals("fml32")) {
                  var26 = 10;
               } else {
                  if (!var2 || !var17.equals("view32")) {
                     if (var18) {
                        ntrace.doTrace("]/HTableParser/Startup/" + var17 + " is invalid/90");
                     }

                     return;
                  }

                  var26 = 11;
               }

               int var25;
               if (var2) {
                  var25 = (var26 & 63) << 25 | var24 & 33554431;
               } else {
                  var25 = (var26 & 7) << 13 | var24 & 8191;
               }

               this.fieldtonameHashTable.put((long)var25, var13);
               this.nametofieldHashTable.put(var13, (long)var25);
               this.fieldNameSet.add(var13);
            }
         }
      }

      if (var18) {
         ntrace.doTrace("]/HTableParser/Startup/Loaded/100");
      }

   }

   public NumericValueHashtable getnametofieldHashTable() {
      return this.nametofieldHashTable;
   }

   public NumericHashtable getfieldtonameHashTable() {
      return this.fieldtonameHashTable;
   }

   public Vector getfieldNameSet() {
      return this.fieldNameSet;
   }
}
