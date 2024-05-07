package weblogic.management.security.internal.compatibility;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.management.ObjectName;
import weblogic.management.DomainDir;
import weblogic.management.ManagementRuntimeException;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.commo.Commo;
import weblogic.management.commo.SecurityMBeanData;
import weblogic.utils.Debug;
import weblogic.utils.classloaders.ClasspathClassLoader;

public class SecurityDataInUserConfigMigrationProcessor {
   static final String NAME_OF_COMPATIBILITY_DIRECTORY = "compatibility";
   static final String NAME_OF_COMPATIBILITY_JAR = "wlUserConfigCompatibility.jar";
   static final String REALM_TYPE = "weblogic.management.security.Realm";
   static final String ULM_TYPE = "weblogic.management.security.authentication.UserLockoutManager";
   static boolean debugInit = false;
   static String _debugInit = System.getProperty("weblogic.DebugUserConfigLoad", "false");
   HashMap beansCreated = new HashMap();

   public InputStream updateConfiguration(InputStream var1, File var2) {
      if (_debugInit.equals("true")) {
         debugInit = true;
      }

      try {
         if (debugInit) {
            Debug.say("Will be loading COMMO instances from: " + var2.getAbsolutePath());
         }

         List var3 = getMBeans(var2);
         if (debugInit) {
            Debug.say("We loaded " + var3.size() + " mbeans ... ");
         }

         Iterator var4 = var3.iterator();

         ArrayList var5;
         SecurityMBeanData var6;
         for(var5 = new ArrayList(); var4.hasNext(); var5.add(var6)) {
            var6 = (SecurityMBeanData)var4.next();
            if (debugInit) {
               Debug.say("The instance name loaded is " + var6.getInstanceName() + " of type " + var6.getTypeName());
            }

            Iterator var7 = var6.getAttrValues().iterator();
            Vector var8 = new Vector();
            Commo.Pair var9 = null;

            while(true) {
               while(var7.hasNext()) {
                  Commo.Pair var10 = (Commo.Pair)var7.next();
                  if (var10.getValue() instanceof ObjectNameString) {
                     ObjectName var20 = new ObjectName(((ObjectNameString)var10.getValue()).getStringObjectName());
                     var9 = new Commo.Pair(var10.getKey(), var20);
                     var8.add(var9);
                  } else if (!(var10.getValue() instanceof ObjectNameString[])) {
                     var8.add(var10);
                  } else {
                     ObjectNameString[] var11 = (ObjectNameString[])((ObjectNameString[])var10.getValue());
                     ObjectName[] var12 = new ObjectName[var11.length];

                     for(int var13 = 0; var13 < var11.length; ++var13) {
                        var12[var13] = new ObjectName(var11[var13].getStringObjectName());
                     }

                     var9 = new Commo.Pair(var10.getKey(), var12);
                     var8.add(var9);
                  }
               }

               var6.setAttrValues(var8);
               Commo.Pair[] var19 = var6.getAdditionalDescriptorElements();
               if (var19 != null) {
                  Commo.Pair[] var21 = new Commo.Pair[var19.length];

                  for(int var22 = 0; var22 < var19.length; ++var22) {
                     Commo.Pair var23 = var19[var22];
                     if (var23.getValue() instanceof ObjectNameString) {
                        ObjectName var24 = new ObjectName(((ObjectNameString)var23.getValue()).getStringObjectName());
                        var21[var22] = new Commo.Pair(var23.getKey(), var24);
                     } else if (!(var23.getValue() instanceof ObjectNameString[])) {
                        var21[var22] = var23;
                     } else {
                        ObjectNameString[] var14 = (ObjectNameString[])((ObjectNameString[])var23.getValue());
                        ObjectName[] var15 = new ObjectName[var14.length];

                        for(int var16 = 0; var16 < var14.length; ++var16) {
                           var15[var16] = new ObjectName(var14[var16].toString());
                        }

                        var21[var22] = new Commo.Pair(var23.getKey(), var15);
                     }
                  }

                  var6.setAdditionalDescriptorValues(var21);
               }
               break;
            }
         }

         ProviderMigrationDriver var18 = new ProviderMigrationDriver(DomainDir.getRootDir(), var5);
         return var18.convert(var1);
      } catch (Exception var17) {
         throw new ManagementRuntimeException(var17);
      }
   }

   public static final List getMBeans(File var0) {
      if (debugInit) {
         Debug.say("get mbeans from " + var0.getAbsolutePath());
      }

      ArrayList var1 = new ArrayList();

      try {
         File var2 = new File(var0, "userConfig");
         if (debugInit) {
            Debug.say("user config dir is  " + var2.getAbsolutePath());
         }

         if (debugInit) {
            Debug.say("Will be loading COMMO instances from: " + var2);
         }

         String[] var3 = var2.list();
         if (var3 != null) {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               String var5 = var2 + File.separator + var3[var4];
               String[] var6 = (new File(var5)).list();
               if (debugInit) {
                  Debug.say("There are " + var6.length + " COMMO instances in domain " + var3[var4]);
               }

               for(int var7 = 0; var7 < var6.length; ++var7) {
                  String var8 = var6[var7];

                  try {
                     if (debugInit) {
                        Debug.say("Process " + var8);
                     }
                  } catch (Exception var11) {
                     if (var8.trim().endsWith("X")) {
                        if (debugInit) {
                           Debug.say(var8 + " ends with X so skip it");
                        }
                        continue;
                     }

                     if (debugInit) {
                        Debug.say("Skip " + var8 + " because of exception " + var11);
                        var11.printStackTrace();
                     }
                     continue;
                  }

                  try {
                     Object var9 = readMBeanDataFromFile(var8);
                     var1.add(var9);
                  } catch (Exception var10) {
                  }
               }
            }
         }

         return var1;
      } catch (Exception var12) {
         throw new ManagementRuntimeException(var12);
      }
   }

   protected static Object readMBeanDataFromFile(String var0) throws Exception {
      if (debugInit) {
         Debug.say("Reading data from " + var0);
      }

      Object var1 = null;
      String var2 = "Security";
      String var3 = "userConfig";
      String var4 = new String(DomainDir.getRootDir() + File.separator + var3 + File.separator + var2 + File.separator + var0);
      String var5 = null;
      String var6 = null;
      File var7 = new File(var4);
      String[] var8 = var7.list();
      String[] var9;
      int var10;
      String var21;
      switch (var8.length) {
         case 0:
            var7.delete();
            break;
         case 1:
            var5 = var8[0];
            var6 = null;
            break;
         case 3:
            var9 = new String[2];
            var10 = 0;
            String var11 = null;

            int var12;
            for(var12 = 0; var12 < 3; ++var12) {
               if (var8[var12].indexOf(46) != -1) {
                  var9[var10] = var8[var12];
                  ++var10;
               }
            }

            int var13;
            if (var10 == 2) {
               var12 = var9[0].indexOf(46);
               String var23 = var9[0].substring(var12 + 1);
               int var26 = var9[1].indexOf(46);
               String var15 = var9[1].substring(var26 + 1);
               int var16 = Integer.parseInt(var23);
               int var17 = Integer.parseInt(var15);
               if (var16 < var17) {
                  var11 = var9[1];
               } else {
                  var11 = var9[0];
               }

               File var18 = new File(var4 + File.separator + var11);
               var18.delete();
            } else {
               var10 = 0;

               for(var12 = 0; var12 < 3; ++var12) {
                  if (var8[var12].indexOf(46) == -1) {
                     var9[var10] = var8[var12];
                     ++var10;
                  }
               }

               var12 = Integer.parseInt(var9[0]);
               var13 = Integer.parseInt(var9[1]);
               if (var12 < var13) {
                  var11 = var9[1];
               } else {
                  var11 = var9[0];
               }

               File var14 = new File(var4 + File.separator + var11);
               var14.delete();
            }

            String[] var25 = new String[2];
            var10 = 0;

            for(var13 = 0; var13 < 3; ++var13) {
               if (!var8[var13].equals(var11)) {
                  var25[var10] = var8[var13];
                  ++var10;
               }
            }

            var8 = var25;
         case 2:
            var5 = null;
            if (var8[0].indexOf(46) != -1) {
               if (var8[1].indexOf(46) == -1) {
                  var5 = var8[1];
                  var6 = var8[0];
               } else {
                  float var20 = Float.parseFloat(var8[0]);
                  float var22 = Float.parseFloat(var8[1]);
                  if (var20 > var22) {
                     var5 = var8[0];
                     var6 = var8[1];
                  } else {
                     var5 = var8[1];
                     var6 = var8[0];
                  }
               }
            } else if (var8[1].indexOf(46) != -1 && var8[0].indexOf(46) == -1) {
               var5 = var8[0];
               var6 = var8[1];
            }

            if (var5 == null) {
               var9 = null;
               var10 = Integer.parseInt(var8[0]);
               int var24 = Integer.parseInt(var8[1]);
               if (var10 < var24) {
                  var21 = var8[1];
                  var5 = var8[0];
                  var6 = null;
               } else {
                  var21 = var8[0];
                  var5 = var8[1];
                  var6 = null;
               }

               File var27 = new File(var4 + File.separator + var21);
               var27.delete();
            }
            break;
         default:
            throw new Exception("Error loading userConfig directory");
      }

      var9 = null;
      var10 = 0;

      while(var10 < 2) {
         try {
            var21 = var4 + File.separator + var5;
            var1 = loadData(var21);
            break;
         } catch (Exception var19) {
            var5 = var6;
            var6 = null;
            ++var10;
         }
      }

      return var1;
   }

   public static final Object loadData(String var0) throws Exception {
      String var1 = (new File(BootStrap.getPathRelativeWebLogicHome("lib") + File.separator + "compatibility" + File.separator + "wlUserConfigCompatibility.jar")).getAbsolutePath();
      File var2 = new File(var0);
      WLUserConfigCompatabilityClassLoader var3 = new WLUserConfigCompatabilityClassLoader(var1);
      Class var4 = var3.loadClass("weblogic.management.commo.SecurityObjectInputStream");
      FileInputStream var5 = new FileInputStream(var2);
      Constructor var6 = var4.getConstructor(InputStream.class, ClassLoader.class, Boolean.TYPE, Boolean.TYPE);
      ObjectInputStream var7 = (ObjectInputStream)var6.newInstance(var5, var3, new Boolean("false"), new Boolean("false"));
      Object var8 = null;

      try {
         var8 = var7.readObject();
      } catch (Exception var10) {
         if (debugInit) {
            var10.printStackTrace();
         }

         throw var10;
      }

      var7.close();
      var5.close();
      return var8;
   }

   static class WLUserConfigCompatabilityClassLoader extends ClasspathClassLoader {
      HashMap map = new HashMap();

      WLUserConfigCompatabilityClassLoader(String var1) {
         super(var1);
      }

      public Class loadClass(String var1) throws ClassNotFoundException {
         Class var2 = null;

         try {
            var2 = this.findClass(var1);
         } catch (ClassNotFoundException var4) {
         }

         if (var2 == null) {
            var2 = super.loadClass(var1);
            this.map.put(var1, var2);
         }

         return var2;
      }

      protected Class findClass(String var1) throws ClassNotFoundException {
         Class var2 = (Class)this.map.get(var1);
         if (var2 != null) {
            return var2;
         } else {
            var2 = super.findClass(var1);
            if (var2 != null) {
               this.map.put(var1, var2);
            }

            return var2;
         }
      }
   }
}
