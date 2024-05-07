package weblogic;

import weblogic.nodemanager.server.NMServer;

public final class NodeManager {
   public static void main(String[] var0) {
      if (var0.length > 0 && (var0[0].equalsIgnoreCase("-help") || var0[0].equalsIgnoreCase("help") || var0[0].equalsIgnoreCase("-h") || var0[0].equals("?") || var0[0].equals("-?"))) {
         System.out.println(getUsage());
      } else if (var0.length > 0 && var0[0].equalsIgnoreCase("-advanced")) {
         System.out.println(getAdvancedUsage());
      } else {
         NMServer.main(var0);
      }

   }

   public static String getUsage() {
      StringBuffer var0 = new StringBuffer();
      var0.append("Usage: java [<Property>=<PropertyValue>] weblogic.NodeManager [OPTIONS]\n");
      var0.append("\t  where the following properties are specified as:\n");
      var0.append("\t  -DListenAddress \t\t[String, default 'localhost']\n");
      var0.append("\t  -DListenPort \t\t\t[int, default '5556']\n");
      var0.append("\t  -DPropertiesFile \t\t[String, default 'nodemanager.properties']\n");
      var0.append("\n");
      var0.append("\t  where options include:\n");
      var0.append("\t  -n <home>  Specify node manager home directory (default is PWD)\n");
      var0.append("\t  -f <file>  Specify node manager properties file\n");
      var0.append("\t             (default is NM_HOME/nodemanager.properties)\n");
      var0.append("\t  -v         Run in verbose mode\n");
      var0.append("\t  -d         Enable debug output to log file\n");
      var0.append("\t  -?, -h     Print this usage message\n");
      var0.append("\t  -advanced  Print advanced options\n");
      return var0.toString();
   }

   public static String getAdvancedUsage() {
      StringBuffer var0 = new StringBuffer();
      var0.append("Usage: java [<Property>=<PropertyValue>] weblogic.NodeManager\n");
      var0.append("\t  where the following properties are specified as:\n");
      var0.append("\t  -DListenAddress \t\t[String, default 'localhost']\n");
      var0.append("\t  -DListenPort \t\t\t[int, default '5556']\n");
      var0.append("\t  -DWeblogicHome \t\t[String]\n");
      var0.append("\t    Specifies the root directory of the WebLogic\n");
      var0.append("\t    installation. This is used as the value for\n");
      var0.append("\t    -Dweblogic.RootDirectory\n");
      var0.append("\t  -DNativeVersionEnabled \t[boolean, default 'true']\n");
      var0.append("\t    Causes native libraries for the operating system to be used.\n");
      var0.append("\t    For UNIX systems other than Solaris, HP-UX, or Linux,\n");
      var0.append("\t    set this property to false to run Node Manager in \n");
      var0.append("\t    non-native mode. This will cause Node Manager to use the \n");
      var0.append("\t    start script specified by the StartScriptEnabled \n");
      var0.append("\t    property to start Managed Servers.\n");
      var0.append("\t  -DJavaHome \t\t\t[String]\n");
      var0.append("\t    Specifies the home directory of 'java'.\n");
      var0.append("\t    The managed servers will use the java (or 'java.exe')\n");
      var0.append("\t    from javaHome/bin directory when they are started.\n");
      var0.append("\n");
      var0.append("\t  -DDomainsFile \t\t[String, default 'NM_HOME/nodemanager.domains']\n");
      var0.append("\t    The name of the nodemanager.domains file.\n");
      var0.append("\t  -DDomainsFileEnabled \t\t[boolean, default 'true']\n");
      var0.append("\t    If set to true, use the file specified in DomainsFile. If false,\n");
      var0.append("\t    assumes the domain of the current directory or WL_HOME.\n");
      var0.append("\t  -DStartScriptName \t\t[String, default 'startWeblogic.sh/.cmd']\n");
      var0.append("\t    The name of the start script, located in the domain directory.\n");
      var0.append("\t  -DStartScriptEnabled \t\t[boolean, default 'false']\n");
      var0.append("\t    If true, use the start script specified by StartScriptName\n");
      var0.append("\t    to start a server.\n");
      var0.append("\n");
      var0.append("\t  -DLogFile \t\t\t[String, default 'NM_HOME/nodemanager.log']\n");
      var0.append("\t    Location of the NodeManager log file.\n");
      var0.append("\t  -DLogLimit \t\t\t[int, default unlimited]\n");
      var0.append("\t    Maximum size of the Node Manager log. When this limit is reached,\n");
      var0.append("\t    a new log file is started.\n");
      var0.append("\t  -DLogCount \t\t\t[int, default '1']\n");
      var0.append("\t    Maximum number of log files to create when LogLimit is reached.\n");
      var0.append("\t  -DLogAppend \t\t\t[boolean, default 'true']\n");
      var0.append("\t    If true, append to existing log files instead of creating a new\n");
      var0.append("\t    one when NodeManager is started.\n");
      var0.append("\t  -DLogToStderr \t\t[boolean, default 'true']\n");
      var0.append("\t    If true, log output is also sent to stderr. Setting this to false\n");
      var0.append("\t    will override the -v flag.\n");
      var0.append("\t  -DLogLevel \t\t\t[String, default 'WLLevel.INFO']\n");
      var0.append("\t    Severity level of logging used by NodeManager. Options: Trace\n");
      var0.append("\t    Debug, Info, Warning, Error, Notice, Critical, Alert, Emergency.\n");
      var0.append("\t  -DLogFormatter \t\t[String, default 'weblogic.nodemanager.server.LogFormatter']\n");
      var0.append("\t    Name of formatter class to use for NM log messages.\n");
      var0.append("\n");
      var0.append("\t  -DCrashRecoveryEnabled \t[boolean, default 'true']\n");
      var0.append("\t    Enables System Crash Recovery. If true, nodemanager will restart\n");
      var0.append("\t    crashed servers when it starts.\n");
      var0.append("\n");
      var0.append("\t  -DSecureListener \t\t[boolean, default 'true']\n");
      var0.append("\t    If true, use the secure listener. Otherwise use a plain socket.\n");
      var0.append("\t  -DCipherSuite \t\t[String, default 'TLS_RSA_EXPORT_WITH_RC4_40_MD5']\n");
      var0.append("\t    Name of the cipher suite to use with the secure listener.\n");
      var0.append("\n");
      var0.append("\t  -DKeyStores \t\t\t[String, default 'DemoIdentityAndDemoTrust']\n");
      var0.append("\t    Indicates the keystore configuration the nodemanager uses\n");
      var0.append("\t    to find its identity and trust.\n");
      var0.append("\t  -DJavaStandardTrustKeyStorePassPhrase \t[String, default none]\n");
      var0.append("\t    Specifies the password defined when creating the Trust keystore.\n");
      var0.append("\t    Weblogic only reads from the keystore, so whether or not you set.\n");
      var0.append("\t    This property depends on the requirements of the keystore.\n");
      var0.append("\t    This property is required when the KeyStores property is set as\n");
      var0.append("\t    CustomIdentityAndJavaStandardTrust or DemoIdentityAndDemoTrust.\n");
      var0.append("\t  -DCustomIdentityKeyStorePassPhrase \t\t[String, default none]\n");
      var0.append("\t    Specifies the password defined when creating the Identity keystore.\n");
      var0.append("\t    Weblogic only reads from the keystore, so whether or not you set.\n");
      var0.append("\t    This property depends on the requirements of the keystore.\n");
      var0.append("\t  -DCustomIdentityKeyStoreFileName \t\t[String, default none]\n");
      var0.append("\t    Specifies the name of the Identity keystore.\n");
      var0.append("\t    This property is required when the KeyStores property is set as\n");
      var0.append("\t    CustomIdentityAndCustomTrust or CustomIdentityAndJavaStandardTrust.\n");
      var0.append("\t  -DCustomIdentityAlias \t\t\t[String, default none]\n");
      var0.append("\t    Specifies the alias when loading the private key into\n");
      var0.append("\t    the keystore. This property is required when the KeyStores\n");
      var0.append("\t    property is set as CustomIdentityAndCustomTrust or\n");
      var0.append("\t    CustomIdentityAndJavaStandardTrust.\n");
      var0.append("\t  -DCustomIdentityPrivateKeyPassPhrase \t\t[String, default none]\n");
      var0.append("\t    Specifies the password used to retrieve the private key for\n");
      var0.append("\t    WebLogic Server from the Identity keystore. This property is\n");
      var0.append("\t    required when the KeyStores property is set as\n");
      var0.append("\t    CustomIdentityAndCustomTrust or CustomIdentityAndJavaStandardTrust.\n");
      var0.append("\t  -DCustomIdentityKeyStoreType \t\t\t[String, default keystore type from java.security ]\n");
      var0.append("\t    Specifies the type of the Identity Keystore. Generally,\n");
      var0.append("\t    this is JKS. This property is optional.\n");
      var0.append("\n");
      var0.append("\t  Additional properties that are used as defaults\n");
      var0.append("\t  when starting up Managed servers can be\n");
      var0.append("\t  specified. These are: \n");
      var0.append("\t  -Dbea.home \t\t\t[String]\n");
      var0.append("\t    Specifies the BEA_Home\n");
      var0.append("\t  -DWeblogicHome \t\t[String]\n");
      var0.append("\t    Specifies the root directory of the WebLogic\n");
      var0.append("\t    installation. This is used as the value for\n");
      var0.append("\t    -Dweblogic.RootDirectory\n");
      var0.append("\t  -Djava.security.policy \t[String]\n");
      var0.append("\t    Specifies the security policy to be used.\n");
      var0.append("\t    Note that in addition to being used as the\n");
      var0.append("\t    default security policy by the Managed servers\n");
      var0.append("\t    started by this NodeManager, this file also\n");
      var0.append("\t    specifies the policy file for this NodeManager.\n");
      var0.append("\n\n");
      var0.append("\t  You can specify all these properties in a properties file using\n");
      var0.append("\t  the following property.\n");
      var0.append("\t  -DPropertiesFile \t\t\t[String, default 'nodemanager.properties']\n");
      var0.append("\t  For more information see:\n");
      var0.append("\t  http://e-docs.bea.com/wls/docs103/nodemgr/intro.html");
      return var0.toString();
   }
}
