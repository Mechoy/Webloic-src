package weblogic.j2ee;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationDescriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.descriptor.wl.ConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.ConnectionParamsBean;
import weblogic.j2ee.descriptor.wl.ConnectionPropertiesBean;
import weblogic.j2ee.descriptor.wl.JDBCConnectionPoolBean;
import weblogic.j2ee.descriptor.wl.ParameterBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionService;

/** @deprecated */
public final class PasswordEncrypt {
   public static boolean debug = false;

   public static void main(String[] var0) {
      System.out.println("");
      System.out.println("** This utility is deprecated and has been replaced by weblogic.security.Encrypt **");
      System.out.println("");
      if (var0 != null && var0.length != 2) {
         System.err.println("Correct usage: java weblogic.j2ee.PasswordEncrypt <descriptor file> <domain_config_dir>");
         System.err.println("<descriptor file) --  the application's weblogic-application.xml");
         System.err.println("<domain_config_dir) -- server domain directory");
         System.exit(-1);
      }

      if (debug) {
         System.out.println("Args: " + var0[0] + " " + var0[1]);
      }

      PasswordEncrypt var1 = new PasswordEncrypt();
      File var2 = null;

      try {
         var2 = (new File(var0[0])).getCanonicalFile();
      } catch (IOException var7) {
         System.err.println("Couldn't open " + var0[0] + " " + var7.getMessage());
         System.exit(-1);
      } catch (SecurityException var8) {
         System.err.println("Couldn't open " + var0[0] + " " + var8.getMessage());
         System.exit(-1);
      }

      EncryptionService var3 = null;
      ClearOrEncryptedService var4 = null;

      try {
         var3 = SerializedSystemIni.getEncryptionService(var0[1]);
         var4 = new ClearOrEncryptedService(var3);
      } catch (Exception var6) {
         System.err.println("Error obtaining EncryptionService using :" + var0[1] + " " + var6.getMessage());
         var6.printStackTrace();
         System.exit(-1);
      }

      var1.findAndUpdateWLSpwd(var2, var4);
      System.out.println("Password Encrypting Complete.  Please use file " + var0[0] + " when creating your .ear file");
      System.exit(0);
   }

   private void findAndUpdateWLSpwd(File var1, ClearOrEncryptedService var2) {
      WeblogicApplicationBean var3 = null;
      ApplicationDescriptor var4 = null;

      try {
         var4 = new ApplicationDescriptor((InputStream)null, new FileInputStream(var1));
         var3 = var4.getWeblogicApplicationDescriptor();
      } catch (IOException var17) {
         System.err.println("IOException parsing descriptor file " + var1.getName());
         var17.printStackTrace();
         System.exit(-1);
      } catch (XMLStreamException var18) {
         System.err.println("Error parsing descriptor file " + var1.getName());
         var18.printStackTrace();
         System.exit(-1);
      }

      JDBCConnectionPoolBean[] var5 = var3.getJDBCConnectionPools();
      if (var5 != null && var5.length > 0) {
         System.out.println("We have " + var5.length + " connectionPools defined");
         ConnectionFactoryBean var6 = null;
         ConnectionPropertiesBean var7 = null;
         ConnectionParamsBean[] var8 = null;
         ParameterBean[] var9 = null;
         String var10 = null;
         String var11 = null;

         for(int var12 = 0; var12 < var5.length; ++var12) {
            var6 = var5[var12].getConnectionFactory();
            var7 = var6.getConnectionProperties();
            var10 = var7.getPassword();
            if (var10 != null) {
               if (debug) {
                  System.out.println("BEFORE(topLevelPwd): " + var10);
               }

               var10 = var2.encrypt(var10);
               var7.setPassword(var10);
               if (debug) {
                  System.out.println("AFTER(topLevelPwd): " + var10);
               }
            }

            var8 = var7.getConnectionParams();

            for(int var13 = 0; var13 < var8.length; ++var13) {
               var9 = var8[var13].getParameters();

               for(int var14 = 0; var14 < var9.length; ++var14) {
                  if (var9[var14].getParamName().equals("password")) {
                     var11 = var9[var14].getParamValue();
                     if (debug) {
                        System.out.println("BEFORE(propsPwd): " + var11);
                     }

                     var11 = var2.encrypt(var11);
                     var9[var14].setParamValue(var11);
                     if (debug) {
                        System.out.println("AFTER(propsPwd): " + var11);
                     }
                  }
               }
            }
         }

         if (var10 == null && var11 == null) {
            System.out.println("No ConnectionPool passwords defined");
         } else {
            try {
               DescriptorManager var19 = new DescriptorManager();
               DescriptorBean var20 = (DescriptorBean)var4.getWeblogicApplicationDescriptor();
               if (var20 != null) {
                  DescriptorUtils.writeDescriptor(var19, var20, var1);
               }
            } catch (IOException var15) {
               System.err.println("IOException obtaining writing out new descriptor file: " + var15.getMessage());
               System.exit(-1);
            } catch (XMLStreamException var16) {
               System.err.println("XMLStreamException writing out new descriptor file: " + var16.getMessage());
               System.exit(-1);
            }

         }
      } else {
         System.out.println("There are no connectionPools to process.");
      }
   }
}
