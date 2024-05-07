package weblogic.jms.client;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.jms.JMSException;
import weblogic.jms.extensions.ClientSAFFactory;
import weblogic.jms.safclient.ClientSAFImpl;

public class ClientSAFDiscover {
   private static final String HELP_OPT = "-help";
   private static final String ROOT_DIRECTORY_OPT = "-clientSAFRootDir";
   private static final String CUTOFF_TIME_FORMAT_OPT = "-cutoffFormat";
   private static final String CUTOFF_TIME_OPT = "-cutoffTime";
   private static final String DISCOVERY_FILE_OPT = "-discoveryFile";
   private static final String CONFIGURATION_FILE_OPT = "-configurationFile";
   private static Set<String> supportedOptions = new HashSet();

   private static Map<String, String> parseOptions(String[] var0) {
      HashMap var1 = new HashMap();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         String var3 = var0[var2];
         if (supportedOptions.contains(var3)) {
            if ("-help".equals(var3)) {
               var1.put(var3, "");
               break;
            }

            ++var2;
            if (var2 >= var0.length) {
               errorOut("missing option value for option " + var3);
            }

            var1.put(var3, var0[var2]);
         } else {
            errorOut("unsupported option " + var3);
         }
      }

      if (var1.get("-clientSAFRootDir") != null && var1.get("-configurationFile") == null) {
         errorOut("The configurationFile option must be specified when rootDir option is specified");
      }

      return var1;
   }

   private static void discover(Map<String, String> var0) throws Throwable {
      long var1 = -1L;
      String var3 = (String)var0.get("-cutoffTime");
      String var4;
      if (var3 != null) {
         var4 = (String)var0.get("-cutoffFormat");
         if (var4 == null) {
            var4 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
         }

         SimpleDateFormat var5 = new SimpleDateFormat(var4);

         try {
            var1 = var5.parse(var3).getTime();
         } catch (ParseException var13) {
            throwJMSException(var13, "The cutoff time option " + var3 + " is not of " + var4 + " format");
         }
      }

      var4 = (String)var0.get("-clientSAFRootDir");
      File var15 = var4 == null ? null : new File(var4);
      String var6 = (String)var0.get("-configurationFile");
      FileInputStream var7 = null;

      try {
         File var8;
         if (var6 != null) {
            var8 = new File(var6);
            if (!var8.exists()) {
               throwJMSException((Throwable)null, "The configuration file " + var6 + " does not exist");
            }

            var7 = new FileInputStream(var8);
         }

         var8 = null;
         ClientSAFImpl var16;
         if (var15 == null) {
            if (var7 == null) {
               var16 = (ClientSAFImpl)ClientSAFFactory.getClientSAF();
            } else {
               var16 = (ClientSAFImpl)ClientSAFFactory.getClientSAF(var7);
            }
         } else {
            var16 = (ClientSAFImpl)ClientSAFFactory.getClientSAF(var15, var7);
         }

         String var9 = (String)var0.get("-discoveryFile");
         var16.discover(var9, var1);
      } finally {
         if (var7 != null) {
            var7.close();
         }

      }

   }

   public static void main(String[] var0) {
      Map var1 = parseOptions(var0);
      if (var1.containsKey("-help")) {
         printUsage();
      } else {
         try {
            discover(var1);
         } catch (Throwable var3) {
            var3.printStackTrace();
         }

      }
   }

   private static void throwJMSException(Throwable var0, String var1) throws JMSException {
      JMSException var2 = new JMSException(var1);
      if (var0 != null) {
         var2.initCause(var0);
      }

      throw var2;
   }

   private static void errorOut(String var0) {
      System.out.println(var0 + "\n");
      printUsage();
      System.exit(1);
   }

   private static void printUsage() {
      String var0 = "Usage: java weblogic.jms.extensions.ClientSAFDiscover [options]\n\nThis command helps users to survey their existing local SAF messages\nbefore applying the fix for bug 8174629 or upgrading from WL 10.3.2 or\nprior releases to 11gR1PS2/10.3.3 or later. The survey can help determine\nwhether the upgrade needs to be tuned for non-default behavior.\nPlease check the description of bug 8174629 in the release note.\n\nThe options include:\n    -help             Print this usage information.\n    -clientSAFRootDir <client-saf-root-directory>  Optional, defaults to\n                      current directory. The client saf root directory of\n                      the target Client SAF that users want to discover.\n                      Any relative paths in the Client SAF configuration\n                      file are relative to this directory (for example,\n                      the store directory).\n    -configurationFile <configuration-file> Optional, defaults to\n                      \"ClientSAF.xml\". The location of the configuration\n                      file used for the target Client SAF. The configuration\n                      file is a well-formed xml file with respect to\n                      the weblogic-jmsmd.xsd schema and which has a \n                      root element of weblogic-client-jms. This option\n                      is required if the clientSAFRootDir option is specified.\n                      If neither clientSAFRootDir option nor this option is\n                      specified, SAFClientDiscover will use the\n                      \"ClientSAF.xml\" under the current working\n                      directory as the configuration file. In any case,\n                      Exception will be thrown if the specified\n                      configuration file does not exist.\n    -cutoffFormat <pattern> Optional, defaults to\n                      \"yyyy-MM-dd'T'HH:mm:ss.SSSZ\". The date and time\n                      pattern for the optional cutoff time of this\n                      ClientSAFDiscover. Check the javadoc for the\n                      java.text.SimpleDateFormat class for more\n                      information.\n    -cutoffTime <cutoff-time> Optional, defaults to \"not set\".\n                      Print data on messages that would be discarded\n                      during upgrade if the optional upgrade cut-off-time\n                      Java system property is set. Users can optionally\n                      specify a cutoff timestamp as a Java system property\n                      for the message upgrade to only upgrade messages\n                      sent after this timestamp. Messages before\n                      this timestamp will be discarded. No messages are\n                      discarded in the client SAF discovery process.\n                      The cutoff time format depends on the cutoffFormat\n                      option. For example, if the cutoffFormat is the default\n                      \"yyyy-MM-dd'T'HH:mm:ss.SSSZ\", an example cutoff\n                      time can be \"2009-12-16T10:34:17.887-0800\".\n                      An exception will be thrown if the specified cutoff\n                      time does not match the cutoffFormat pattern.\n                      If a cutoff time is not specified, the message\n                      upgrade will migrate all the messages and the\n                      SAFClientDiscover command line tool will not\n                      report any message to be discarded.\n    -discoveryFile <discovery-file> Optional, defaults to SAF_DISCOVERY.\n                      The discovery file contains the output for\n                      ClientSAFDiscover.  It is placed relative to the\n                      client SAF root directory unless an absolute path\n                      is specified. If the specified file already exists\n                      ClientSafDiscover deletes the file and creates a\n                      new one.\n\nExample:\nIf a program calls\n\nClientSAFFactory.getClientSAF(new File(\"c:\\\\foo\"),\n    new FileInputStream(\"c:\\\\ClientSAF-jms.xml\"));\n\nto create their client SAF, you can run the following\nClientSAFDiscover command to print out the automatic upgrade that\nwould occur(without actually causing an upgrade or changing any\nstored data):\n\njava weblogic.jms.client.ClientSAFDiscover -rootDir c:\\foo\n    -configurationFile c:\\ClientSAF-jms.xml\n\nThe discovery information will be written to the default location\nc:\\foo\\SAF_DISCOVERY.\n";
      System.out.println(var0);
   }

   static {
      supportedOptions.add("-help");
      supportedOptions.add("-clientSAFRootDir");
      supportedOptions.add("-cutoffFormat");
      supportedOptions.add("-cutoffTime");
      supportedOptions.add("-discoveryFile");
      supportedOptions.add("-configurationFile");
   }
}
