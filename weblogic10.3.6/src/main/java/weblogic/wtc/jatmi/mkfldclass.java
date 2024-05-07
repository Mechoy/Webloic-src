package weblogic.wtc.jatmi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public final class mkfldclass extends Task {
   private String packageName;
   private String outputDirectory;
   private String fieldTable;
   private static final int FLD_SHORT = 0;
   private static final int FLD_LONG = 1;
   private static final int FLD_CHAR = 2;
   private static final int FLD_FLOAT = 3;
   private static final int FLD_DOUBLE = 4;
   private static final int FLD_STRING = 5;
   private static final int FLD_CARRAY = 6;
   private static final int FNMASK = 8191;
   private static final int FTMASK = 7;
   private static final int FTSHIFT = 13;

   private static void write_error(int var0, String var1) {
      System.out.println("Error at line " + var0 + ". " + var1 + ".");
      throw new BuildException();
   }

   public void execute() {
      boolean var1 = false;
      String[] var2 = new String[]{this.packageName, this.outputDirectory, this.fieldTable};
      doIt(var2);
   }

   public void setPackage(String var1) {
      this.packageName = var1;
   }

   public void setOutputDirectory(String var1) {
      this.outputDirectory = var1;
   }

   public void setFieldTable(String var1) {
      this.fieldTable = var1;
   }

   public static void main(String[] var0) {
      try {
         doIt(var0);
      } catch (BuildException var2) {
      }

   }

   private static void doIt(String[] var0) throws BuildException {
      BufferedWriter var4 = null;
      BufferedReader var5 = null;
      boolean var10 = false;
      int var11 = 0;
      int var12 = 0;
      boolean var16 = false;
      boolean var17 = false;
      byte var18 = 0;
      boolean var21 = true;
      boolean var24 = false;
      if (var0.length >= 1 && var0.length <= 3) {
         File var1;
         String var22;
         String var23;
         if (var0.length == 1) {
            var22 = null;
            var23 = null;
            var1 = new File(var0[0]);
         } else if (var0.length == 2) {
            var23 = null;
            var22 = var0[0];
            var1 = new File(var0[1]);
         } else {
            var22 = var0[0];
            if (var22 != null && var22.length() == 0) {
               var22 = null;
            }

            var23 = var0[1];
            if (var23 != null && var23.length() == 0) {
               var23 = null;
            }

            var1 = new File(var0[2]);
         }

         String var25 = System.getProperty("target");
         if (var25 == null) {
            var25 = new String("1.4");
         }

         try {
            var5 = new BufferedReader(new FileReader(var1));
         } catch (FileNotFoundException var30) {
            System.out.println("Could not find file " + var1);
            throw new BuildException();
         }

         String var3 = new String(var1.getName() + ".java");
         if (var23 != null) {
            var3 = var23 + File.separatorChar + var3;
         }

         File var2 = new File(var3);

         try {
            var4 = new BufferedWriter(new FileWriter(var2));
         } catch (FileNotFoundException var28) {
            System.out.println("Could not create file " + var3 + ". " + var28);
            throw new BuildException();
         } catch (IOException var29) {
            System.out.println("Error creating file " + var3 + ". " + var29);
            throw new BuildException();
         }

         LinkedList var14 = new LinkedList();

         while(true) {
            String var6;
            int var9;
            try {
               if ((var6 = var5.readLine()) == null) {
                  break;
               }

               var6 = var6.trim();
               ++var12;
               var9 = 0;
            } catch (IOException var32) {
               System.out.println("Finished! " + var32);
               break;
            }

            int var7 = var6.length();

            try {
               if (!var24 && var22 != null) {
                  var4.write("package " + var22 + ";");
                  var4.newLine();
                  var4.newLine();
               }

               var24 = true;
               if (var7 != 0 && !var6.startsWith("#")) {
                  if (var6.startsWith("$")) {
                     var4.write("//" + var6.substring(1));
                     var4.newLine();
                  } else {
                     int var13;
                     if (var6.startsWith("*base")) {
                        if (!Character.isWhitespace(var6.charAt(5))) {
                           write_error(var12, "*base must be followed by white space");
                        }

                        for(var9 = 6; Character.isWhitespace(var6.charAt(var9)); ++var9) {
                        }

                        if (!Character.isDigit(var6.charAt(var9))) {
                           write_error(var12, "*base must be followed by a numeric value");
                        }

                        for(var13 = var9++; var9 < var6.length() && Character.isDigit(var6.charAt(var9)); ++var9) {
                        }

                        var11 = new Integer(var6.substring(var13, var9));
                     } else {
                        if (!var10) {
                           var10 = true;
                           var4.write("import java.io.*;");
                           var4.newLine();
                           var4.write("import java.lang.*;");
                           var4.newLine();
                           var4.write("import java.util.*;");
                           var4.newLine();
                           var4.write("import weblogic.wtc.jatmi.*;");
                           var4.newLine();
                           var4.newLine();
                           var4.write("public final class " + var1.getName());
                           var4.newLine();
                           var4.write("\timplements weblogic.wtc.jatmi.FldTbl");
                           var4.newLine();
                           var4.write("{");
                           var4.newLine();
                           if (var25.compareTo("1.5") < 0) {
                              var4.write("\tHashtable nametofieldHashTable;");
                           } else {
                              var4.write("\tHashtable<String, Integer> nametofieldHashTable;");
                           }

                           var4.newLine();
                           if (var25.compareTo("1.5") < 0) {
                              var4.write("\tHashtable fieldtonameHashTable;");
                           } else {
                              var4.write("\tHashtable<Integer, String> fieldtonameHashTable;");
                           }

                           var4.newLine();
                           var4.newLine();
                        }

                        while(!Character.isWhitespace(var6.charAt(var9))) {
                           ++var9;
                        }

                        String var15;
                        for(var15 = var6.substring(0, var9); Character.isWhitespace(var6.charAt(var9)); ++var9) {
                        }

                        if (!Character.isDigit(var6.charAt(var9))) {
                           write_error(var12, "fieldname " + var15 + " must be followed by a numeric value");
                        }

                        for(var13 = var9++; Character.isDigit(var6.charAt(var9)); ++var9) {
                        }

                        int var34 = new Integer(var6.substring(var13, var9)) + var11;
                        if (var34 > 8191) {
                           write_error(var12, "fieldnumber " + var34 + " must be less than or equal to 8191");
                        }

                        while(Character.isWhitespace(var6.charAt(var9))) {
                           ++var9;
                        }

                        var13 = var9;

                        while(!Character.isWhitespace(var6.charAt(var9)) && var6.charAt(var9) != '\n') {
                           ++var9;
                           if (var9 >= var7) {
                              var9 = var7;
                              break;
                           }
                        }

                        String var19 = var6.substring(var13, var9);
                        if (var19.equals("char")) {
                           var18 = 2;
                        } else if (var19.equals("string")) {
                           var18 = 5;
                        } else if (var19.equals("short")) {
                           var18 = 0;
                        } else if (var19.equals("long")) {
                           var18 = 1;
                        } else if (var19.equals("float")) {
                           var18 = 3;
                        } else if (var19.equals("double")) {
                           var18 = 4;
                        } else if (var19.equals("carray")) {
                           var18 = 6;
                        } else {
                           write_error(var12, "fieldtype " + var19 + " is invalid");
                        }

                        int var35 = (var18 & 7) << 13 | var34 & 8191;
                        var4.write("\t/** number: " + var34 + "  type: " + var19 + " */");
                        var4.newLine();
                        var4.write("\tpublic final static int " + var15 + " = " + var35 + ";");
                        var4.newLine();
                        var14.add(var15);
                     }
                  }
               }
            } catch (IOException var33) {
               System.out.println("Unable to write output file " + var33);
            }
         }

         if (!var10) {
            System.out.println("Did not find any field entries");
            throw new BuildException();
         } else {
            try {
               var4.flush();
               var4.write("\n\tpublic String Fldid_to_name(int fldid)");
               var4.newLine();
               var4.write("\t{");
               var4.newLine();
               var4.write("\t\tif ( fieldtonameHashTable == null ) {");
               var4.newLine();
               if (var25.compareTo("1.5") < 0) {
                  var4.write("\t\t\tfieldtonameHashTable = new Hashtable();");
               } else {
                  var4.write("\t\t\tfieldtonameHashTable = new Hashtable<Integer, String>();");
               }

               var4.newLine();
               ListIterator var20 = var14.listIterator();

               String var26;
               while(var20.hasNext()) {
                  var26 = (String)var20.next();
                  var4.write("\t\t\t");
                  var4.write("fieldtonameHashTable.put(new Integer(" + var26 + "), \"" + var26 + "\");");
                  var4.newLine();
               }

               var4.write("\t\t}");
               var4.newLine();
               var4.newLine();
               var4.write("\t\treturn ((String)fieldtonameHashTable.get(new Integer(fldid)));");
               var4.newLine();
               var4.write("\t}");
               var4.newLine();
               var4.newLine();
               var4.write("\tpublic int name_to_Fldid(String name)");
               var4.newLine();
               var4.write("\t{");
               var4.newLine();
               var4.write("\t\tif ( nametofieldHashTable == null ) {");
               var4.newLine();
               if (var25.compareTo("1.5") < 0) {
                  var4.write("\t\t\tnametofieldHashTable = new Hashtable();");
               } else {
                  var4.write("\t\t\tnametofieldHashTable = new Hashtable<String, Integer>();");
               }

               var4.newLine();
               var20 = var14.listIterator();

               while(var20.hasNext()) {
                  var26 = (String)var20.next();
                  var4.write("\t\t\t");
                  var4.write("nametofieldHashTable.put(\"" + var26 + "\", new Integer(" + var26 + "));");
                  var4.newLine();
               }

               var4.write("\t\t}");
               var4.newLine();
               var4.newLine();
               var4.write("\t\tInteger fld = (Integer)nametofieldHashTable.get(name);");
               var4.newLine();
               var4.write("\t\tif (fld == null) {");
               var4.newLine();
               var4.write("\t\t\treturn (-1);");
               var4.newLine();
               var4.write("\t\t} else {");
               var4.newLine();
               var4.write("\t\t\treturn (fld.intValue());");
               var4.newLine();
               var4.write("\t\t}");
               var4.newLine();
               var4.write("\t}");
               var4.newLine();
               var4.newLine();
               var4.write("\tpublic String[] getFldNames()");
               var4.newLine();
               var4.write("\t{");
               var4.newLine();
               var4.write("\t\tString retval[] = new String[" + var14.size() + "];");
               var4.newLine();
               int var27 = 0;

               for(var20 = var14.listIterator(); var20.hasNext(); ++var27) {
                  var26 = (String)var20.next();
                  var4.write("\t\tretval[" + var27 + "] = new String(\"" + var26 + "\");");
                  var4.newLine();
               }

               var4.write("\t\treturn retval;");
               var4.newLine();
               var4.write("\t}");
               var4.newLine();
               var4.write("}");
               var4.newLine();
               var4.flush();
               var4.close();
            } catch (IOException var31) {
               System.out.println("Unable to write output file " + var31);
               throw new BuildException();
            }
         }
      } else {
         System.out.println("Usage: mkfldclass [package [outputdirectory]] fldtbl");
         throw new BuildException();
      }
   }
}
