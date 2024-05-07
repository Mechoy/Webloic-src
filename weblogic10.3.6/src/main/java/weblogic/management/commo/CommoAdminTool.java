package weblogic.management.commo;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.Descriptor;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import weblogic.management.MBeanHome;
import weblogic.management.RemoteMBeanServer;
import weblogic.management.WebLogicMBean;
import weblogic.management.commandline.CommandLineArgs;
import weblogic.management.commandline.OutputFormatter;
import weblogic.management.commo.internal.CommoCommandLineTextFormatter;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.management.jmx.MBeanServerInvocationHandler;
import weblogic.management.security.RealmMBean;
import weblogic.management.security.authentication.UserLockoutManagerMBean;

public class CommoAdminTool {
   DocumentBuilderFactory factory;
   DocumentBuilder docBuilder;
   MBeanServer mbs = null;
   MBeanHome home = null;
   CommandLineArgs params = null;
   static List mbeanTypes = new ArrayList();

   public CommoAdminTool(CommandLineArgs var1, MBeanServer var2) {
      this.mbs = var2;
      this.params = var1;
   }

   public CommoAdminTool(CommandLineArgs var1, MBeanServer var2, MBeanHome var3) {
      this.mbs = var2;
      this.params = var1;
      this.home = var3;
   }

   private static void printNotSupported() {
      System.out.println("Create's, Delete's and Set's on Security MBeans which include MBean's of types,\nweblogic.management.security.RealmMBean,\nweblogic.management.security.authentication.UserLockoutManagerMBean\nand any Security Provider MBeans are not supported via weblogic.Admin.\nPlease use weblogic.WLST.");
   }

   public static void doIt(CommandLineArgs var0, MBeanServer var1, String var2, MBeanHome var3) throws Exception {
      switch (var0.getOperation()) {
         case 29:
            (new CommoAdminTool(var0, var1, var3)).executeQueryOperation(var2);
         case 30:
         default:
            break;
         case 31:
            (new CommoAdminTool(var0, var1, var3)).executeNewGetOperation(var2);
            break;
         case 32:
         case 33:
         case 34:
            printNotSupported();
            break;
         case 35:
            (new CommoAdminTool(var0, var1, var3)).executeNewInvokeOperation();
      }

   }

   boolean isEncrypted(ModelMBeanAttributeInfo var1) {
      Boolean var2 = (Boolean)var1.getDescriptor().getFieldValue("encrypted");
      return var2 != null ? var2 : false;
   }

   boolean isExcluded(ModelMBeanAttributeInfo var1) {
      Boolean var2 = (Boolean)var1.getDescriptor().getFieldValue("exclude");
      return var2 != null ? var2 : false;
   }

   boolean isOperationExcluded(ModelMBeanOperationInfo var1) {
      Boolean var2 = (Boolean)var1.getDescriptor().getFieldValue("exclude");
      return var2 != null ? var2 : false;
   }

   private AttributeList getAttributes(MBeanHome var1, ObjectName var2) throws Exception {
      RemoteMBeanServer var3 = var1.getMBeanServer();
      ModelMBeanInfo var4 = (ModelMBeanInfo)var3.getMBeanInfo(var2);
      ModelMBeanAttributeInfo[] var5 = (ModelMBeanAttributeInfo[])((ModelMBeanAttributeInfo[])var4.getAttributes());
      ArrayList var6 = new ArrayList();
      AttributeList var7 = new AttributeList();

      for(int var8 = 0; var8 < var5.length; ++var8) {
         String var9 = var5[var8].getName();
         if (!this.isExcluded(var5[var8]) && !var6.contains(var9) && !var9.equals("MBeanInfo")) {
            var6.add(var9);
            Object var10 = var3.getAttribute(var2, var9);
            if (this.isEncrypted(var5[var8])) {
               var7.add(new Attribute(var9, "******"));
            } else {
               var7.add(new Attribute(var9, var10));
            }
         }
      }

      return var7;
   }

   private ObjectName getObjectName(Object var1) {
      if (Proxy.isProxyClass(var1.getClass())) {
         InvocationHandler var2 = Proxy.getInvocationHandler(var1);
         if (var2 instanceof MBeanServerInvocationHandler) {
            return MBeanServerInvocationHandler.getObjectName(var1);
         }

         if (var1 instanceof WebLogicMBean) {
            return ((WebLogicMBean)var1).getObjectName();
         }

         if (var1 instanceof StandardInterface) {
            return ((StandardInterface)var1).wls_getObjectName();
         }
      } else {
         if (var1 instanceof WebLogicMBean) {
            return ((WebLogicMBean)var1).getObjectName();
         }

         if (var1 instanceof StandardInterface) {
            return ((StandardInterface)var1).wls_getObjectName();
         }
      }

      return null;
   }

   public void executeNewGetOperation(String var1) throws Exception {
      ObjectName var2 = new ObjectName(this.home.getDomainName() + ":Name=" + this.home.getDomainName() + ",Type=SecurityConfiguration");
      SecurityConfigurationMBean var3 = (SecurityConfigurationMBean)this.home.getMBean(var2);
      String var4 = this.params.getMBeanObjName();
      CommandLineArgs var10000 = this.params;
      String var5 = CommandLineArgs.typeMunged ? null : this.params.getMBeanType();
      ManagementTextTextFormatter var18;
      if (var5 != null && var5.indexOf(".") == -1) {
         var18 = new ManagementTextTextFormatter();
         System.out.println(var18.getNoMBeansFound());
      } else if (var4 != null && !this.mbs.isRegistered(new ObjectName(var4))) {
         var18 = new ManagementTextTextFormatter();
         System.out.println(var18.getNoMBeansFound());
      } else {
         String var6 = this.params.getNewMBeanName();
         RealmMBean[] var7;
         int var8;
         RealmMBean var9;
         UserLockoutManagerMBean var21;
         if (var4 != null) {
            var7 = var3.getRealms();

            String[] var10;
            for(var8 = 0; var8 < var7.length; ++var8) {
               var9 = var7[var8];
               if (this.getObjectName(var9).toString().equals(var4)) {
                  var10 = null;
                  if (this.params.getAttribList() != null && this.params.getAttribList().size() != 0) {
                     this.printAttributes(this.params.getAttribList(), var10, this.getObjectName(var9).toString());
                  } else {
                     AttributeList var11 = this.getAttributes(this.home, this.getObjectName(var9));
                     var10 = this.getNamesFromAttrList(var11);
                     this.printAttributes(var11, var10, this.getObjectName(var9).toString());
                  }

                  return;
               }
            }

            var21 = var3.findDefaultRealm().getUserLockoutManager();
            if (this.getObjectName(var21).toString().equals(var4)) {
               AttributeList var25 = this.getAttributes(this.home, this.getObjectName(var21));
               var10 = this.getNamesFromAttrList(var25);
               this.printAttributes(var25, var10, this.getObjectName(var21).toString());
               return;
            }

            var9 = null;
            var10 = null;
            var9 = null;
            var10 = null;
            ObjectName[] var26 = (ObjectName[])((ObjectName[])this.home.getMBeanServer().invoke(this.getObjectName(var3), "pre90getProviders", var9, var10));

            for(int var12 = 0; var12 < var26.length; ++var12) {
               ObjectName var13 = var26[var12];
               if (var13.toString().equals(var4)) {
                  AttributeList var14 = this.getAttributes(this.home, var13);
                  String[] var15 = this.getNamesFromAttrList(var14);
                  this.printAttributes(var14, var15, var13.toString());
                  return;
               }
            }
         }

         if (this.params.getMBeanType() != null) {
            if (this.params.getMBeanType().equals("weblogic.management.security.Realm")) {
               var7 = var3.getRealms();

               for(var8 = 0; var8 < var7.length; ++var8) {
                  var9 = var7[var8];
                  AttributeList var27 = this.getAttributes(this.home, this.getObjectName(var9));
                  String[] var29 = this.getNamesFromAttrList(var27);
                  this.printAttributes(var27, var29, this.getObjectName(var9).toString());
               }
            } else if (this.params.getMBeanType().equals("weblogic.management.security.authentication.UserLockoutManager")) {
               UserLockoutManagerMBean var19 = var3.findDefaultRealm().getUserLockoutManager();
               if (this.getObjectName(var19).toString().equals(var4)) {
                  AttributeList var22 = this.getAttributes(this.home, this.getObjectName(var19));
                  String[] var23 = this.getNamesFromAttrList(var22);
                  this.printAttributes(var22, var23, this.getObjectName(var19).toString());
               }
            } else {
               var7 = null;
               var21 = null;
               var7 = null;
               var21 = null;
               ObjectName[] var24 = (ObjectName[])((ObjectName[])this.home.getMBeanServer().invoke(this.getObjectName(var3), "pre90getProviders", var7, var21));

               for(int var28 = 0; var28 < var24.length; ++var28) {
                  ObjectName var30 = var24[var28];
                  if (var4 != null) {
                     if (var30.toString().equals(var4)) {
                        AttributeList var31 = this.getAttributes(this.home, var30);
                        String[] var33 = this.getNamesFromAttrList(var31);
                        this.printAttributes(var31, var33, var30.toString());
                        return;
                     }
                  } else {
                     ModelMBeanInfo var32 = (ModelMBeanInfo)this.home.getMBeanServer().getMBeanInfo(var30);
                     Descriptor var34 = var32.getMBeanDescriptor();
                     String var35 = (String)var34.getFieldValue("interfaceclassname");
                     String var36 = var35.substring(0, var35.lastIndexOf("MBean"));
                     if (var5.equals(var36)) {
                        AttributeList var16 = this.getAttributes(this.home, var30);
                        String[] var17 = this.getNamesFromAttrList(var16);
                        this.printAttributes(var16, var17, var30.toString());
                     }
                  }
               }
            }
         } else {
            ManagementTextTextFormatter var20 = new ManagementTextTextFormatter();
            System.out.println(var20.getNoMBeansFound());
         }

      }
   }

   public void executeQueryOperation(String var1) throws Exception {
   }

   public void executeNewInvokeOperation() throws Exception {
      CommandLineArgs var10000 = this.params;
      String var1 = CommandLineArgs.typeMunged ? null : this.params.getMBeanType();
      String var2 = this.params.getMBeanObjName();
      String var3 = this.params.getMethodName();
      Object[] var4 = null;
      String[] var5 = null;
      if (var2 != null) {
         var5 = this.getMethodSignature(new ObjectName(var2), this.home.getMBeanServer(), this.params.getMethodName());
         var4 = this.params.getMethodArguments(var5);
         Object var6 = null;
         var6 = this.home.getMBeanServer().invoke(new ObjectName(var2), var3, var4, var5);
         CommoCommandLineTextFormatter var7 = new CommoCommandLineTextFormatter();
         if (var6 != null) {
            System.out.println(var6);
         } else {
            System.out.println(var7.getOK());
         }
      } else {
         ObjectName var14 = new ObjectName(this.home.getDomainName() + ":Name=" + this.home.getDomainName() + ",Type=SecurityConfiguration");
         SecurityConfigurationMBean var15 = (SecurityConfigurationMBean)this.home.getMBean(var14);
         RealmMBean[] var8;
         CommoCommandLineTextFormatter var12;
         if (var1.equals("weblogic.management.security.Realm")) {
            var8 = var15.getRealms();

            for(int var9 = 0; var9 < var8.length; ++var9) {
               RealmMBean var10 = var8[var9];
               var5 = this.getMethodSignature(this.getObjectName(var10), this.home.getMBeanServer(), this.params.getMethodName());
               var4 = this.params.getMethodArguments(var5);
               Object var11 = null;
               var11 = this.home.getMBeanServer().invoke(this.getObjectName(var10), var3, var4, var5);
               var12 = new CommoCommandLineTextFormatter();
               if (var11 != null) {
                  System.out.println(var11);
               } else {
                  System.out.println(var12.getOK());
               }
            }
         } else {
            Object var17;
            if (var1.equals("weblogic.management.security.authentication.UserLockoutManager")) {
               UserLockoutManagerMBean var16 = var15.findDefaultRealm().getUserLockoutManager();
               var5 = this.getMethodSignature(this.getObjectName(var16), this.home.getMBeanServer(), this.params.getMethodName());
               var4 = this.params.getMethodArguments(var5);
               var17 = null;
               var17 = this.home.getMBeanServer().invoke(this.getObjectName(var16), var3, var4, var5);
               CommoCommandLineTextFormatter var18 = new CommoCommandLineTextFormatter();
               if (var17 != null) {
                  System.out.println(var17);
               } else {
                  System.out.println(var18.getOK());
               }
            } else {
               var8 = null;
               var17 = null;
               var8 = null;
               ObjectName[] var19 = (ObjectName[])((ObjectName[])this.home.getMBeanServer().invoke(this.getObjectName(var15), "pre90getProviders", var8, (String[])var17));

               for(int var20 = 0; var20 < var19.length; ++var20) {
                  var5 = this.getMethodSignature(var19[var20], this.home.getMBeanServer(), this.params.getMethodName());
                  var4 = this.params.getMethodArguments(var5);
                  var12 = null;
                  Object var21 = this.home.getMBeanServer().invoke(var19[var20], var3, var4, var5);
                  CommoCommandLineTextFormatter var13 = new CommoCommandLineTextFormatter();
                  if (var21 != null) {
                     System.out.println(var21);
                  } else {
                     System.out.println(var13.getOK());
                  }
               }
            }
         }
      }

   }

   String[] getNamesFromAttrList(AttributeList var1) {
      Iterator var2 = var1.iterator();
      String[] var3 = new String[var1.size()];

      for(int var4 = 0; var2.hasNext(); ++var4) {
         Attribute var5 = (Attribute)var2.next();
         String var6 = var5.getName();
         var3[var4] = var6;
      }

      return var3;
   }

   String[] getMethodSignature(ObjectName var1, MBeanServer var2, String var3) throws CommoOperationsException {
      String[] var4 = null;

      try {
         MBeanInfo var5 = var2.getMBeanInfo(var1);
         IllegalArgumentException var6 = null;
         MBeanOperationInfo[] var7 = var5.getOperations();

         for(int var8 = 0; var8 < var7.length; ++var8) {
            if (var7[var8].getName().equalsIgnoreCase(var3)) {
               MBeanParameterInfo[] var9 = var7[var8].getSignature();
               var4 = new String[var9.length];

               for(int var10 = 0; var10 < var9.length; ++var10) {
                  var4[var10] = var9[var10].getType();
               }

               try {
                  this.params.getMethodArguments(var4);
                  return var4;
               } catch (IllegalArgumentException var11) {
                  var6 = var11;
               }
            }
         }

         if (null != var6) {
            throw var6;
         } else {
            CommoCommandLineTextFormatter var15 = new CommoCommandLineTextFormatter();
            System.out.println(var15.getInstanceMethNotFound(var3, var1.toString()));
            return var4;
         }
      } catch (InstanceNotFoundException var12) {
         throw new CommoOperationsException(var12);
      } catch (IntrospectionException var13) {
         throw new CommoOperationsException(var13);
      } catch (ReflectionException var14) {
         throw new CommoOperationsException(var14);
      }
   }

   void printAttributes(AttributeList var1, String[] var2, String var3) {
      try {
         OutputFormatter var4 = new OutputFormatter(System.out, this.params.isPretty());
         var4.mbeanBegin(var3);
         var4.printAttribs(var1, var2);
         var4.mbeanEnd();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }
}
