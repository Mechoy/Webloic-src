package weblogic.jms.extensions;

import java.io.File;
import java.io.FileOutputStream;
import java.util.StringTokenizer;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.descriptor.wl.ClientSAFBean;
import weblogic.j2ee.descriptor.wl.DistributedQueueBean;
import weblogic.j2ee.descriptor.wl.DistributedTopicBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.JMSConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBean;
import weblogic.j2ee.descriptor.wl.SAFLoginContextBean;
import weblogic.j2ee.descriptor.wl.SAFQueueBean;
import weblogic.j2ee.descriptor.wl.SAFRemoteContextBean;
import weblogic.j2ee.descriptor.wl.SAFTopicBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedQueueBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedTopicBean;
import weblogic.jms.common.BeanHelper;
import weblogic.jms.module.JMSParser;
import weblogic.management.ManagementException;
import weblogic.utils.Getopt2;

public class ClientSAFGenerate {
   private static final String URL_OPT = "url";
   private static final String URL_PARAM = "admin-server-url";
   private static final String URL_DOC = "The URL of the admin server to connect to. If not specified then this utility will run in off-line mode";
   private static final String UN_OPT = "username";
   private static final String UN_PARAM = "name-of-user";
   private static final String UN_DOC = "The username of a user with read access to the Domain. Only used in connected mode";
   private static final String ECF_OPT = "existingClientFile";
   private static final String ECF_PARAM = "file-path";
   private static final String ECF_DOC = "The name of an existing client Store & Forward configuration file. If this parameter is specified then the existing file will be read and new entries will be added. If there are any conflicts detected between items being added and items already in the client configuration file a warning will be emitted and the new item will not be added. If this is specified but the file cannot be found an error is printed and the utility exits";
   private static final String MF_OPT = "moduleFile";
   private static final String MF_PARAM = "module-file-path ['@' plan-path ]";
   private static final String MF_DOC = "The name of a JMS module file. This file is not associated with any specific deployment. Only a few of the fields in the client configuration file can be filled in. This parameter can be used in both connected and off-line mode";
   private static final String OF_OPT = "outputFile";
   private static final String OF_PARAM = "output-file-path";
   private static final String OF_DOC = "The path to the output file";
   private static final String PROGRAM = "weblogic.jms.extensions.ClientSAFGenerate";
   private Getopt2 optionsParser = new Getopt2();
   private ClientSAFBean config;
   private static final String ALL = "All";

   public ClientSAFGenerate() {
      this.optionsParser.addOption("url", "admin-server-url", "The URL of the admin server to connect to. If not specified then this utility will run in off-line mode");
      this.optionsParser.addOption("username", "name-of-user", "The username of a user with read access to the Domain. Only used in connected mode");
      this.optionsParser.addOption("existingClientFile", "file-path", "The name of an existing client Store & Forward configuration file. If this parameter is specified then the existing file will be read and new entries will be added. If there are any conflicts detected between items being added and items already in the client configuration file a warning will be emitted and the new item will not be added. If this is specified but the file cannot be found an error is printed and the utility exits");
      this.optionsParser.addMultiOption("moduleFile", "module-file-path ['@' plan-path ]", "The name of a JMS module file. This file is not associated with any specific deployment. Only a few of the fields in the client configuration file can be filled in. This parameter can be used in both connected and off-line mode");
      this.optionsParser.addOption("outputFile", "output-file-path", "The path to the output file");
      this.optionsParser.setFailOnUnrecognizedOpts(true);
   }

   private void initFile() {
   }

   private void extractFromJMSBean(String var1, String var2, JMSBean var3) throws ManagementException {
      JMSConnectionFactoryBean[] var4 = var3.getConnectionFactories();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         JMSConnectionFactoryBean var6 = var4[var5];
         if (this.config.lookupConnectionFactory(var6.getName()) != null) {
            System.err.println("Warning: The connection factory " + var6.getName() + "" + "already exists in the client SAF configuration file.  Skipping...");
         } else {
            JMSConnectionFactoryBean var7 = this.config.createConnectionFactory(var6.getName());
            BeanHelper.copyConnectionFactory(var7, var6);
         }
      }

      SAFImportedDestinationsBean var17 = this.config.lookupSAFImportedDestinations(var1);
      if (var17 == null) {
         var17 = this.config.createSAFImportedDestinations(var1);
      }

      if (var2 != null) {
         var17.setSAFRemoteContext(this.config.lookupSAFRemoteContext(var2));
      }

      QueueBean[] var18 = var3.getQueues();

      String var10;
      for(int var19 = 0; var19 < var18.length; ++var19) {
         QueueBean var8 = var18[var19];
         String var9 = var8.getSAFExportPolicy();
         if (var9 == null || "All".equals(var9)) {
            var10 = var8.getJNDIName();
            if (var10 == null) {
               var10 = var8.getLocalJNDIName();
            }

            SAFQueueBean var11 = var17.lookupSAFQueue(var8.getName());
            if (var11 != null) {
               System.err.println("Warning: The queue " + var8.getName() + " in module " + var1 + " already exists in the client SAF configuration file.  Skipping...");
            } else {
               var11 = var17.createSAFQueue(var8.getName());
               if (var10 != null) {
                  var11.setLocalJNDIName(var10);
                  var11.setRemoteJNDIName(var10);
               }
            }
         }
      }

      TopicBean[] var20 = var3.getTopics();

      String var27;
      for(int var21 = 0; var21 < var20.length; ++var21) {
         TopicBean var23 = var20[var21];
         var10 = var23.getSAFExportPolicy();
         if (var10 == null || "All".equals(var10)) {
            var27 = var23.getJNDIName();
            if (var27 == null) {
               var27 = var23.getLocalJNDIName();
            }

            SAFTopicBean var12 = var17.lookupSAFTopic(var23.getName());
            if (var12 != null) {
               System.err.println("Warning: The topic " + var23.getName() + " in module " + var1 + " already exists in the client SAF configuration file.  Skipping...");
            } else {
               var12 = var17.createSAFTopic(var23.getName());
               if (var27 != null) {
                  var12.setLocalJNDIName(var27);
                  var12.setRemoteJNDIName(var27);
               }
            }
         }
      }

      DistributedQueueBean[] var22 = var3.getDistributedQueues();

      String var30;
      for(int var24 = 0; var24 < var22.length; ++var24) {
         DistributedQueueBean var26 = var22[var24];
         var27 = var26.getSAFExportPolicy();
         if (var27 == null || "All".equals(var27)) {
            var30 = var26.getJNDIName();
            SAFQueueBean var13 = var17.lookupSAFQueue(var26.getName());
            if (var13 != null) {
               System.err.println("Warning: The distributed queue " + var26.getName() + " in module " + var1 + " already exists in the client SAF configuration file.  Skipping...");
            } else {
               var13 = var17.createSAFQueue(var26.getName());
               if (var30 != null) {
                  var13.setLocalJNDIName(var30);
                  var13.setRemoteJNDIName(var30);
               }
            }
         }
      }

      DistributedTopicBean[] var25 = var3.getDistributedTopics();

      String var34;
      for(int var28 = 0; var28 < var25.length; ++var28) {
         DistributedTopicBean var31 = var25[var28];
         var30 = var31.getSAFExportPolicy();
         if (var30 == null || "All".equals(var30)) {
            var34 = var31.getJNDIName();
            SAFQueueBean var14 = var17.lookupSAFQueue(var31.getName());
            if (var14 != null) {
               System.err.println("Warning: The distributed queue " + var31.getName() + " in module " + var1 + " already exists in the client SAF configuration file.  Skipping...");
            } else {
               var14 = var17.createSAFQueue(var31.getName());
               if (var34 != null) {
                  var14.setLocalJNDIName(var34);
                  var14.setRemoteJNDIName(var34);
               }
            }
         }
      }

      UniformDistributedQueueBean[] var29 = var3.getUniformDistributedQueues();

      String var38;
      for(int var32 = 0; var32 < var29.length; ++var32) {
         UniformDistributedQueueBean var33 = var29[var32];
         var34 = var33.getSAFExportPolicy();
         if (var34 == null || "All".equals(var34)) {
            var38 = var33.getJNDIName();
            SAFQueueBean var15 = var17.lookupSAFQueue(var33.getName());
            if (var15 != null) {
               System.err.println("Warning: The distributed queue " + var33.getName() + " in module " + var1 + " already exists in the client SAF configuration file.  Skipping...");
            } else {
               var15 = var17.createSAFQueue(var33.getName());
               if (var38 != null) {
                  var15.setLocalJNDIName(var38);
                  var15.setRemoteJNDIName(var38);
               }
            }
         }
      }

      UniformDistributedTopicBean[] var36 = var3.getUniformDistributedTopics();

      for(int var35 = 0; var35 < var36.length; ++var35) {
         UniformDistributedTopicBean var37 = var36[var35];
         var38 = var37.getSAFExportPolicy();
         if (var38 == null || "All".equals(var38)) {
            String var39 = var37.getJNDIName();
            SAFQueueBean var16 = var17.lookupSAFQueue(var37.getName());
            if (var16 != null) {
               System.err.println("Warning: The uniform distributed queue " + var37.getName() + " in module " + var1 + " already exists in the client SAF configuration file.  Skipping...");
            } else {
               var16 = var17.createSAFQueue(var37.getName());
               if (var39 != null) {
                  var16.setLocalJNDIName(var39);
                  var16.setRemoteJNDIName(var39);
               }
            }
         }
      }

   }

   private String generateRemoteContext(ClientSAFBean var1, String var2, String var3) {
      if (var2 == null) {
         return null;
      } else {
         SAFRemoteContextBean[] var4 = var1.getSAFRemoteContexts();

         int var5;
         String var8;
         for(var5 = 0; var5 < var4.length; ++var5) {
            SAFRemoteContextBean var6 = var4[var5];
            SAFLoginContextBean var7 = var6.getSAFLoginContext();
            var8 = var7.getLoginURL();
            if (var2.equals(var8)) {
               return var6.getName();
            }
         }

         var5 = 0;
         boolean var10 = false;
         String var11 = null;

         while(!var10) {
            var8 = "RemoteContext" + var5++;
            if (var1.lookupSAFRemoteContext(var8) == null) {
               var11 = var8;
               var10 = true;
            }
         }

         SAFRemoteContextBean var12 = var1.createSAFRemoteContext(var11);
         SAFLoginContextBean var9 = var12.getSAFLoginContext();
         var9.setLoginURL(var2);
         if (var3 != null) {
            var9.setUsername(var3);
         }

         return var11;
      }
   }

   private void go(String[] var1) throws Throwable {
      try {
         this.optionsParser.grok(var1);
      } catch (IllegalArgumentException var18) {
         this.optionsParser.usageError("weblogic.jms.extensions.ClientSAFGenerate");
         return;
      }

      String var2 = this.optionsParser.getOption("existingClientFile");
      if (var2 != null) {
         this.config = ClientSAFParser.createClientSAFDescriptor(var2);
      } else {
         this.config = (ClientSAFBean)(new DescriptorManager()).createDescriptorRoot(ClientSAFBean.class).getRootBean();
         this.initFile();
      }

      String var3 = this.optionsParser.getOption("url");
      String var4 = this.optionsParser.getOption("username");
      String var5 = this.generateRemoteContext(this.config, var3, var4);
      String[] var6 = this.optionsParser.getMultiOption("moduleFile", new String[100]);

      for(int var7 = 0; var7 < var6.length; ++var7) {
         String var8 = var6[var7];
         StringTokenizer var9 = new StringTokenizer(var8, "@");
         int var10 = var9.countTokens();
         String var11 = var9.nextToken();
         String var12 = null;
         if (var10 > 1) {
            var12 = var9.nextToken();
         }

         File var13 = new File(var11);
         String var14 = var13.getName().toLowerCase();
         JMSBean var15 = JMSParser.createJMSDescriptor(var11, var12);
         int var16 = var14.indexOf("-jms.xml");
         String var17 = var14.substring(0, var16);
         this.extractFromJMSBean(var17, var5, var15);
      }

      String var19 = this.optionsParser.getOption("outputFile");
      if (var19 == null) {
         DescriptorUtils.writeAsXML((DescriptorBean)this.config);
      } else {
         File var20 = new File(var19);
         FileOutputStream var21 = new FileOutputStream(var20);
         Descriptor var22 = ((DescriptorBean)this.config).getDescriptor();
         (new EditableDescriptorManager()).writeDescriptorAsXML(var22, var21);
      }
   }

   public static void main(String[] var0) {
      ClientSAFGenerate var1 = new ClientSAFGenerate();

      try {
         var1.go(var0);
      } catch (Throwable var5) {
         int var3 = 0;

         for(Throwable var4 = var5; var4 != null; var4 = var4.getCause()) {
            System.err.println("\nERROR: run threw an exception: level " + var3);
            ++var3;
            var4.printStackTrace();
         }
      }

   }
}
