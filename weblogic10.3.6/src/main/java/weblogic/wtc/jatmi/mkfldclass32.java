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

public final class mkfldclass32 extends Task {
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
   private static final int FLD_INT = 7;
   private static final int FLD_DECIMAL = 8;
   private static final int FLD_PTR = 9;
   private static final int FLD_FML32 = 10;
   private static final int FLD_VIEW32 = 11;
   private static final int FLD_MBSTRING = 12;
   private static final int FNMASK32 = 33554431;
   private static final int FTMASK32 = 63;
   private static final int FTSHIFT32 = 25;

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

   public static void generateDynamicCode(String var0, String var1, String var2, File var3) {
      String var5 = null;
      BufferedWriter var6 = null;
      var5 = new String(var3.getName() + ".java");
      if (var2 != null) {
         var5 = var2 + File.separatorChar + var5;
      }

      File var4 = new File(var5);

      try {
         var6 = new BufferedWriter(new FileWriter(var4));
      } catch (FileNotFoundException var9) {
         System.out.println("Could not create file " + var5 + ". " + var9);
         return;
      } catch (IOException var10) {
         System.out.println("Error creating file " + var5 + ". " + var10);
         return;
      }

      try {
         if (var1 != null) {
            var6.write("package " + var1 + ";");
            var6.newLine();
            var6.newLine();
         }

         var6.newLine();
         var6.write("import weblogic.wtc.jatmi.DynamicFldTbl;");
         var6.newLine();
         var6.newLine();
         var6.write("/**");
         var6.newLine();
         var6.write(" * @author Copyright (c) 2003 by BEA Systems, Inc. All Rights Reserved.");
         var6.newLine();
         var6.write(" */");
         var6.newLine();
         var6.newLine();
         var6.write("/**");
         var6.newLine();
         var6.write(" * This class reads a FML32 Field Table during boot time");
         var6.newLine();
         var6.write(" * which implements the FldTbl interface.  ");
         var6.newLine();
         var6.write(" * @see weblogic.wtc.jatmi.FldTbl");
         var6.newLine();
         var6.write(" * @see weblogic.wtc.jatmi.FML");
         var6.newLine();
         var6.write(" * @author BEA Systems, Inc.");
         var6.newLine();
         var6.write(" */");
         var6.newLine();
         var6.write("final public class " + var3);
         var6.newLine();
         var6.write("\textends weblogic.wtc.jatmi.DynamicFldTbl");
         var6.newLine();
         var6.write("{");
         var6.newLine();
         var6.newLine();
         var6.write("\t// Users need to modify this to where the field file is during boot time.");
         var6.newLine();
         var6.write("\t// The string may be an absolute pathname, a path relative to the root directory");
         var6.newLine();
         var6.write("\t// from which Java is started, or a relative path that can be found as a resource file");
         var6.newLine();
         var6.write("\t// by the class loader.");
         var6.newLine();
         var6.write("\tstatic final String USERFMLTABLEFILE=\"" + var0 + "\";");
         var6.newLine();
         var6.newLine();
         var6.write("\t/**");
         var6.newLine();
         var6.write("\t * The constructor.  ");
         var6.newLine();
         var6.write("\t */");
         var6.newLine();
         var6.write("\tpublic " + var3 + "() {");
         var6.newLine();
         var6.write("\t\t// the 2nd parameter, true for FML32, false for FML");
         var6.newLine();
         var6.write("\t\t// Default initial capacity is 101 and load factor is 0.75f");
         var6.newLine();
         var6.write("\t\tsuper(USERFMLTABLEFILE, true);");
         var6.newLine();
         var6.newLine();
         var6.write("\t\t// super(USERFMLTABLEFILE, true, 1000, 0.5f);");
         var6.newLine();
         var6.write("\t\t// the 3rd parameter, Initial Capacity");
         var6.newLine();
         var6.write("\t\t// the 4th parameter, Load Factor");
         var6.newLine();
         var6.write("\t}");
         var6.newLine();
         var6.newLine();
         var6.write("\tpublic static void main(String[] args) {");
         var6.newLine();
         var6.write("\t\t" + var3 + " bf = new " + var3 + "();");
         var6.newLine();
         var6.write("\t\tString[] list = bf.getFldNames();");
         var6.newLine();
         var6.write("\t\tfor (int i = 0; i < list.length; i++ ) {");
         var6.newLine();
         var6.write("\t\t\tSystem.out.println(\"field name = [\" + list[i] + \"]\t\t\tfield name from field id = [\" + bf.Fldid_to_name(bf.name_to_Fldid(list[i]))+ \"]\");");
         var6.newLine();
         var6.write("\t\t}");
         var6.newLine();
         var6.write("\t\tSystem.out.println(\"Total entries = \" + list.length);");
         var6.newLine();
         var6.write("\t}");
         var6.newLine();
         var6.write("\t");
         var6.newLine();
         var6.write("}");
         var6.newLine();
         var6.flush();
         var6.close();
      } catch (IOException var8) {
         System.out.println("Unable to write output file " + var8);
      }

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
      int var19 = 0;
      boolean var22 = true;
      boolean var25 = false;
      if (var0.length >= 1 && var0.length <= 3) {
         File var1;
         String var23;
         String var24;
         if (var0.length == 1) {
            var23 = null;
            var24 = null;
            var1 = new File(var0[0]);
         } else if (var0.length == 2) {
            var24 = null;
            var23 = var0[0];
            var1 = new File(var0[1]);
         } else {
            var23 = var0[0];
            if (var23 != null && var23.length() == 0) {
               var23 = null;
            }

            var24 = var0[1];
            if (var24 != null && var24.length() == 0) {
               var24 = null;
            }

            var1 = new File(var0[2]);
         }

         String var26 = System.getProperty("DynRdHdr");
         if (var26 != null) {
            generateDynamicCode(var26, var23, var24, var1);
         } else {
            String var27 = System.getProperty("target");
            if (var27 == null) {
               var27 = new String("1.4");
            }

            try {
               var5 = new BufferedReader(new FileReader(var1));
            } catch (FileNotFoundException var32) {
               System.out.println("Could not find file " + var1);
               throw new BuildException();
            }

            String var3 = new String(var1.getName() + ".java");
            if (var24 != null) {
               var3 = var24 + File.separatorChar + var3;
            }

            File var2 = new File(var3);

            try {
               var4 = new BufferedWriter(new FileWriter(var2));
            } catch (FileNotFoundException var30) {
               System.out.println("Could not create file " + var3 + ". " + var30);
               throw new BuildException();
            } catch (IOException var31) {
               System.out.println("Error creating file " + var3 + ". " + var31);
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
               } catch (IOException var34) {
                  System.out.println("Finished! " + var34);
                  break;
               }

               int var7 = var6.length();

               try {
                  if (!var25 && var23 != null) {
                     var4.write("package " + var23 + ";");
                     var4.newLine();
                     var4.newLine();
                  }

                  var25 = true;
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
                              if (var27.compareTo("1.5") < 0) {
                                 var4.write("\tHashtable nametofieldHashTable;");
                              } else {
                                 var4.write("\tHashtable<String, Integer> nametofieldHashTable;");
                              }

                              var4.newLine();
                              if (var27.compareTo("1.5") < 0) {
                                 var4.write("\tHashtable fieldtonameHashTable;");
                              } else {
                                 var4.write("\tHashtable<Integer, String> fieldtonameHashTable;");
                              }

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

                           int var36 = new Integer(var6.substring(var13, var9)) + var11;
                           if (var36 > 33554431) {
                              write_error(var12, "fieldnumber " + var36 + " must be less than or equal to 33554431");
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

                           String var20 = var6.substring(var13, var9);
                           if (var20.equals("char")) {
                              var18 = 2;
                           } else if (var20.equals("string")) {
                              var18 = 5;
                           } else if (var20.equals("short")) {
                              var18 = 0;
                           } else if (var20.equals("long")) {
                              var18 = 1;
                           } else if (var20.equals("float")) {
                              var18 = 3;
                           } else if (var20.equals("double")) {
                              var18 = 4;
                           } else if (var20.equals("carray")) {
                              var18 = 6;
                           } else if (var20.equals("ptr")) {
                              var18 = 9;
                           } else if (var20.equals("fml32")) {
                              var18 = 10;
                           } else if (var20.equals("view32")) {
                              var18 = 11;
                           } else if (var20.equals("mbstring")) {
                              var18 = 12;
                           } else {
                              write_error(var12, "fieldtype " + var20 + " is invalid");
                           }

                           int var37 = (var18 & 63) << 25 | var36 & 33554431;
                           var4.write("\t/** number: " + var36 + "  type: " + var20 + " */");
                           var4.newLine();
                           var4.write("\tpublic final static int " + var15 + " = " + var37 + ";");
                           var4.newLine();
                           var14.add(var15);
                           ++var19;
                        }
                     }
                  }
               } catch (IOException var35) {
                  System.out.println("Unable to write output file " + var35);
               }
            }

            if (!var10) {
               System.out.println("Did not find any field entries");
               throw new BuildException();
            } else {
               if (var19 > 3000) {
                  System.out.println("WARNING: Field tables with large numbers of fields can cause run-time");
                  System.out.println("problems in the Java Virtual Machine.  Consider creating a dynamically");
                  System.out.println("parsed field table by setting the DynRdHdr Java property to the run-time");
                  System.out.println("path name of the field definition file.");
               }

               try {
                  var4.flush();
                  var4.write("\n\tpublic String Fldid_to_name(int fldid)");
                  var4.newLine();
                  var4.write("\t{");
                  var4.newLine();
                  var4.write("\t\tif ( fieldtonameHashTable == null ) {");
                  var4.newLine();
                  if (var27.compareTo("1.5") < 0) {
                     var4.write("\t\t\tfieldtonameHashTable = new Hashtable();");
                  } else {
                     var4.write("\t\t\tfieldtonameHashTable = new Hashtable<Integer, String>();");
                  }

                  var4.newLine();
                  ListIterator var21 = var14.listIterator();

                  String var28;
                  while(var21.hasNext()) {
                     var28 = (String)var21.next();
                     var4.write("\t\t\t");
                     var4.write("fieldtonameHashTable.put(new Integer(" + var28 + "), \"" + var28 + "\");");
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
                  if (var27.compareTo("1.5") < 0) {
                     var4.write("\t\t\tnametofieldHashTable = new Hashtable();");
                  } else {
                     var4.write("\t\t\tnametofieldHashTable = new Hashtable<String, Integer>();");
                  }

                  var4.newLine();
                  var21 = var14.listIterator();

                  while(var21.hasNext()) {
                     var28 = (String)var21.next();
                     var4.write("\t\t\t");
                     var4.write("nametofieldHashTable.put(\"" + var28 + "\", new Integer(" + var28 + "));");
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
                  int var29 = 0;

                  for(var21 = var14.listIterator(); var21.hasNext(); ++var29) {
                     var28 = (String)var21.next();
                     var4.write("\t\tretval[" + var29 + "] = new String(\"" + var28 + "\");");
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
               } catch (IOException var33) {
                  System.out.println("Unable to write output file " + var33);
                  throw new BuildException();
               }
            }
         }
      } else {
         System.out.println("Usage: mkfldclass32 [package [outputdirectory]] fldtbl");
         throw new BuildException();
      }
   }
}
