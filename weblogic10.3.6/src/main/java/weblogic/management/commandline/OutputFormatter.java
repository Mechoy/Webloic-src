package weblogic.management.commandline;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.ObjectName;
import weblogic.management.WebLogicMBean;
import weblogic.management.WebLogicObjectName;
import weblogic.management.commo.StandardInterface;
import weblogic.management.configuration.ConfigurationError;
import weblogic.management.configuration.ConfigurationException;

public final class OutputFormatter {
   OutputStream out;
   boolean isPretty;

   public OutputFormatter(OutputStream var1, boolean var2) {
      this.out = var1;
      this.isPretty = var2;
   }

   public void mbeanBegin(String var1) throws IOException {
      if (!this.isPretty) {
         this.out.write(("{MBeanName=\"" + var1 + "\"").getBytes());
      } else {
         this.out.write(("---------------------------\nMBeanName: \"" + var1 + "\"\n").getBytes());
      }

   }

   public void mbeanEnd() throws IOException {
      if (!this.isPretty) {
         this.out.write("}\n".getBytes());
      }

   }

   public void printReturnValue(String var1, Object var2) throws IOException {
      if (null != var2) {
         try {
            if (!this.isPretty) {
               this.out.write(("{" + var1 + "=" + getAttributeStringFromValue(var2) + "}").getBytes());
            } else {
               this.out.write(("\t" + var1 + ": " + getAttributeStringFromValue(var2) + "\n").getBytes());
            }

         } catch (ConfigurationException var4) {
            throw new ConfigurationError(var4);
         }
      }
   }

   public void printAttribs(AttributeList var1, String[] var2) throws IOException {
      int var4 = 0;
      if (var1 != null) {
         var4 = var1.size();
      }

      int var5 = 0;
      if (var2 != null) {
         var5 = var2.length;
      }

      if (var4 == 0 && var5 == 0) {
         String var14 = "\n\t\tIt appears that no attributes have been specified for this MBean\n";
         this.out.write(var14.getBytes());
      } else {
         int var7;
         if (var5 > var4) {
            String[] var6 = new String[var5];
            var7 = 0;

            for(int var8 = 0; var8 < var5; ++var8) {
               boolean var9 = false;

               for(int var10 = 0; var10 < var4; ++var10) {
                  String var11 = ((Attribute)var1.get(var10)).getName();
                  if (var11.equals(var2[var8])) {
                     var9 = true;
                  }
               }

               if (!var9) {
                  var6[var7] = var2[var8];
                  ++var7;
                  if (var7 == var6.length) {
                     break;
                  }
               }
            }

            if (var4 == 0) {
               return;
            }
         }

         Object[] var13 = var1.toArray();
         Arrays.sort(var13, new Comparator() {
            public int compare(Object var1, Object var2) {
               return ((Attribute)var1).getName().compareTo(((Attribute)var2).getName());
            }
         });

         for(var7 = 0; var7 < var13.length; ++var7) {
            Attribute var3 = (Attribute)var13[var7];

            try {
               if (var3.getValue() == null) {
                  this.property(var3.getName(), "");
               } else {
                  this.property(var3.getName(), getAttributeStringFromValue(var3.getValue()));
               }
            } catch (ConfigurationException var12) {
               throw new ConfigurationError(var12);
            }
         }

      }
   }

   public void property(String var1, String var2) throws IOException {
      if (!this.isPretty) {
         this.out.write(("{" + var1 + "=" + var2 + "}").getBytes());
      } else {
         this.out.write(("\t" + var1 + ": " + var2 + "\n").getBytes());
      }

   }

   public void println(String var1) throws IOException {
      this.print(var1 + "\n");
   }

   public void print(String var1) throws IOException {
      this.out.write(var1.getBytes());
   }

   private static String getAttributeStringFromValue(Object var0) throws ConfigurationException {
      try {
         if (var0 instanceof ObjectName) {
            return ((ObjectName)var0).getKeyProperty("Name");
         } else if (var0 instanceof ObjectName) {
            return ((ObjectName)var0).toString();
         } else if (var0 instanceof WebLogicMBean) {
            WebLogicObjectName var12 = ((WebLogicMBean)var0).getObjectName();
            return var12.getName();
         } else {
            String var2;
            int var3;
            if (var0 instanceof String[]) {
               String[] var11 = (String[])((String[])var0);
               var2 = "";

               for(var3 = 0; var3 < var11.length; ++var3) {
                  if (var2.length() != 0) {
                     var2 = var2 + ",";
                  }

                  var2 = var2 + var11[var3];
               }

               return var2;
            } else {
               ObjectName[] var10;
               if (var0 instanceof ObjectName[]) {
                  var10 = (ObjectName[])((ObjectName[])var0);
                  var2 = "";

                  for(var3 = 0; var3 < var10.length; ++var3) {
                     if (var2.length() != 0) {
                        var2 = var2 + ",";
                     }

                     var2 = var2 + var10[var3].getKeyProperty("Name");
                  }

                  return var2;
               } else if (var0 instanceof ObjectName[]) {
                  var10 = (ObjectName[])((ObjectName[])var0);
                  var2 = "";

                  for(var3 = 0; var3 < var10.length; ++var3) {
                     if (var2.length() != 0) {
                        var2 = var2 + ",";
                     }

                     var2 = var2 + var10[var3].getKeyProperty("Name");
                  }

                  return var2;
               } else if (var0 instanceof WebLogicMBean[]) {
                  WebLogicMBean[] var9 = (WebLogicMBean[])((WebLogicMBean[])var0);
                  var2 = "";

                  for(var3 = 0; var3 < var9.length; ++var3) {
                     if (var2.length() != 0) {
                        var2 = var2 + ",";
                     }

                     WebLogicObjectName var16 = var9[var3].getObjectName();
                     var2 = var2 + var16.getName();
                  }

                  return var2;
               } else if (var0 instanceof Properties) {
                  StringBuffer var8 = new StringBuffer();
                  Properties var14 = (Properties)var0;
                  Iterator var17 = var14.keySet().iterator();

                  while(var17.hasNext()) {
                     if (var8.length() != 0) {
                        var8.append(";");
                     }

                     String var4 = (String)var17.next();
                     String var5 = (String)var14.get(var4);
                     var8.append(var4);
                     var8.append("=");
                     var8.append(var5);
                  }

                  return var8.toString();
               } else if (var0 instanceof Map) {
                  String var7 = var0.toString();
                  StringTokenizer var13 = new StringTokenizer(var7, ", ");

                  String var15;
                  for(var15 = ""; var13.hasMoreTokens(); var15 = var15 + var13.nextToken()) {
                     if (var15.length() != 0) {
                        var15 = var15 + ";";
                     }
                  }

                  var15 = var15.replace('{', ' ');
                  var15 = var15.replace('}', ' ');
                  var15 = var15.trim();
                  return var15;
               } else if (var0 instanceof StandardInterface[]) {
                  StandardInterface[] var1 = (StandardInterface[])((StandardInterface[])var0);
                  var2 = "";

                  for(var3 = 0; var3 < var1.length; ++var3) {
                     if (var2.length() != 0) {
                        var2 = var2 + ",";
                     }

                     var2 = var2 + var1[var3].wls_getObjectName();
                  }

                  return var2;
               } else {
                  return var0 == null ? "null" : var0.toString();
               }
            }
         }
      } catch (Exception var6) {
         throw new ConfigurationException(var6);
      }
   }
}
