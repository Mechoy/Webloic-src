package weblogic.management.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class StartScriptWriter {
   public static void writeShFile(String var0, String var1, String var2, String var3) {
      String var4 = var0;
      String var5 = var1;
      String var6 = var2;
      String var8 = var3.replace('\\', '/');

      try {
         File var9 = new File(var4);
         var9.mkdir();
         File var10 = new File(var4 + File.separator + "start" + var5 + ".sh");
         String var11 = var10.getName();
         FileOutputStream var12 = new FileOutputStream(new File(var4 + File.separator + "start" + var5 + ".sh"));
         PrintStream var13 = new PrintStream(var12);
         println(var13, "#!/bin/sh");
         println(var13, "# ****************************************************************************");
         println(var13, "# This script is used to start WebLogic Server for the domain in the current ");
         println(var13, "# working directory.  This script simply sets the SERVER_NAME variable ");
         println(var13, "# and calls the startWLS.sh script under ${WL_HOME}/server/bin.");
         println(var13, "#");
         println(var13, "# To create your own start script for your domain, all you need to set is ");
         println(var13, "# SERVER_NAME, then start the server.");
         println(var13, "#");
         println(var13, "# Other variables that startWLS takes are:");
         println(var13, "#");
         println(var13, "# WLS_USER       - cleartext user for server startup");
         println(var13, "# WLS_PW         - cleartext password for server startup");
         println(var13, "# STARTMODE      - Set to true for production mode servers, false for ");
         println(var13, "#                  development mode");
         println(var13, "# JAVA_OPTIONS   - Java command-line options for running the server. (These");
         println(var13, "#                  will be tagged on to the end of the JAVA_VM and MEM_ARGS)");
         println(var13, "# JAVA_VM        - The java arg specifying the VM to run.  (i.e. -server, ");
         println(var13, "#                  -hotspot, etc.)");
         println(var13, "# MEM_ARGS       - The variable to override the standard memory arguments");
         println(var13, "#                  passed to java");
         println(var13, "#");
         println(var13, "# For additional information, refer to the WebLogic Server Administration Guide");
         println(var13, "# in the Oracle online documentation at (http://www.oracle.com/technology/products/weblogic/index.html).");
         println(var13, "# ****************************************************************************");
         println(var13, "");
         println(var13, "# set up WL_HOME, the root directory of your WebLogic installation");
         println(var13, "WL_HOME=\"" + var8 + "\"");
         println(var13, "");
         println(var13, "# set up common environment");
         println(var13, ". \"${WL_HOME}//common//bin//commEnv.sh\"");
         println(var13, "");
         println(var13, "# Set SERVER_NAME to the name of the server you wish to start up.");
         println(var13, "SERVER_NAME=\"" + var6 + "\"");
         println(var13, "");
         println(var13, "# Set WLS_USER equal to your system username and WLS_PW equal  ");
         println(var13, "# to your system password for no username and password prompt ");
         println(var13, "# during server startup.  Both are required to bypass the startup");
         println(var13, "# prompt.");
         println(var13, "WLS_USER=");
         println(var13, "WLS_PW=");
         println(var13, "");
         println(var13, "# Set Production Mode.  When this is set to true, the server starts up in ");
         println(var13, "# production mode.  When set to false, the server starts up in development ");
         println(var13, "# mode.  If it is not set, it will default to false.");
         println(var13, "STARTMODE=\"\"");
         println(var13, "");
         println(var13, "# Set JAVA_OPTIONS to the java flags you want to pass to the vm.  If there ");
         println(var13, "# are more than one, include quotes around them.  For instance: ");
         println(var13, "# JAVA_OPTIONS=\"-Dweblogic.attribute=value -Djava.attribute=value\"");
         println(var13, "");
         println(var13, "# Over-write JVM arguments initialized in commEnv.sh");
         println(var13, "case $JAVA_VENDOR in");
         println(var13, "BEA)");
         println(var13, "MEM_ARGS=");
         println(var13, "JAVA_VM=");
         println(var13, ";;");
         println(var13, "IBM)");
         println(var13, "MEM_ARGS=");
         println(var13, "JAVA_VM=");
         println(var13, ";;");
         println(var13, "HP)");
         println(var13, "MEM_ARGS=");
         println(var13, "JAVA_VM=");
         println(var13, ";;");
         println(var13, "SUN)");
         println(var13, "MEM_ARGS=");
         println(var13, "JAVA_VM=");
         println(var13, ";;");
         println(var13, "*)");
         println(var13, ";;");
         println(var13, "esac");
         println(var13, "");
         println(var13, "# Reset number of open file descriptors in the current process");
         println(var13, "# This function is defined in commEnv.sh");
         println(var13, "resetFd");
         println(var13, "");
         println(var13, "# Start WebLogic server");
         println(var13, "CLASSPATH=\"${WEBLOGIC_CLASSPATH}${CLASSPATHSEP}${POINTBASE_CLASSPATH}${CLASSPATHSEP}${JAVA_HOME}/jre/lib/rt.jar${CLASSPATHSEP}${WL_HOME}/server/lib/webservices.jar${CLASSPATHSEP}${CLASSPATH}\"");
         println(var13, "export CLASSPATH");
         println(var13, "");
         println(var13, "java ${JAVA_VM} ${MEM_ARGS} ${JAVA_OPTIONS} -Dweblogic.Name=${SERVER_NAME} -Dweblogic.management.username=${WLS_USER} -Dweblogic.management.password=${WLS_PW} -Dweblogic.ProductionModeEnabled=${STARTMODE} -Djava.security.policy=\"${WL_HOME}/server/lib/weblogic.policy\" weblogic.Server");
         Runtime.getRuntime().exec("chmod +x " + var11);
         var12.close();
      } catch (IOException var14) {
         var14.printStackTrace();
      }

   }

   public static void writeCmdFile(String var0, String var1, String var2, String var3) {
      String var4 = var0;
      String var5 = var1;
      String var6 = var2;

      try {
         File var8 = new File(var4);
         var8.mkdir();
         FileOutputStream var9 = new FileOutputStream(new File(var4 + File.separator + "start" + var5 + ".cmd"));
         PrintStream var10 = new PrintStream(var9);
         println(var10, "@rem *************************************************************************");
         println(var10, "@rem This script is used to start WebLogic Server for the domain in the ");
         println(var10, "@rem current working directory.  This script simply sets the SERVER_NAME ");
         println(var10, "@rem variable and calls the startWLS.cmd script under");
         println(var10, "@rem %WL_HOME%\\server\\bin.");
         println(var10, "@rem");
         println(var10, "@rem To create your own start script for your domain, all you need to set is ");
         println(var10, "@rem SERVER_NAME, then start the server.");
         println(var10, "@rem");
         println(var10, "@rem Other variables that startWLS takes are:");
         println(var10, "@rem");
         println(var10, "@rem WLS_USER     - cleartext user for server startup");
         println(var10, "@rem WLS_PW       - cleartext password for server startup");
         println(var10, "@rem STARTMODE    - true for production mode servers, false for ");
         println(var10, "@rem                development mode");
         println(var10, "@rem JAVA_OPTIONS - Java command-line options for running the server. (These");
         println(var10, "@rem                will be tagged on to the end of the JAVA_VM and MEM_ARGS)");
         println(var10, "@rem JAVA_VM      - The java arg specifying the VM to run.  (i.e. -server, ");
         println(var10, "@rem                -hotspot, etc.)");
         println(var10, "@rem MEM_ARGS     - The variable to override the standard memory arguments");
         println(var10, "@rem                passed to java");
         println(var10, "@rem");
         println(var10, "@rem For additional information, refer to the WebLogic Server Administration Guide");
         println(var10, "@rem in the Oracle online documentation at (http://www.oracle.com/technology/products/weblogic/index.html).");
         println(var10, "@rem *************************************************************************");
         println(var10, "");
         println(var10, "echo off");
         println(var10, "SETLOCAL");
         println(var10, "");
         println(var10, "set WL_HOME=\"" + var3 + "\"");
         println(var10, "call \"%WL_HOME%\\common\\bin\\commEnv.cmd\"");
         println(var10, "@rem Set SERVER_NAME to the name of the server you wish to start up.");
         println(var10, "set SERVER_NAME=\"" + var6 + "\"");
         println(var10, "@rem Set WLS_USER equal to your system username and WLS_PW equal  ");
         println(var10, "@rem to your system password for no username and password prompt ");
         println(var10, "@rem during server startup.  Both are required to bypass the startup");
         println(var10, "@rem prompt.  This is not recomended for a production environment.");
         println(var10, "set WLS_USER=");
         println(var10, "set WLS_PW=");
         println(var10, "");
         println(var10, "@rem Set Production Mode.  When this is set to true, the server starts up in ");
         println(var10, "@rem production mode.  When set to false, the server starts up in development ");
         println(var10, "@rem mode.  If it is not set, it will default to false.");
         println(var10, "set STARTMODE=");
         println(var10, "");
         println(var10, "@rem Set JAVA_OPTIONS to the java flags you want to pass to the vm. i.e.: ");
         println(var10, "@rem set JAVA_OPTIONS=-Dweblogic.attribute=value -Djava.attribute=value");
         println(var10, "set JAVA_OPTIONS=");
         println(var10, "");
         println(var10, "@rem Set JAVA_VM to the java virtual machine you want to run.  For instance:");
         println(var10, "@rem For instance:");
         println(var10, "set JAVA_VM=");
         println(var10, "");
         println(var10, "@rem Set MEM_ARGS to the memory args you want to pass to java.  For instance:");
         println(var10, "@rem if \"%JAVA_VENDOR%\"==\"BEA\" set MEM_ARGS=-Xms32m -Xmx200m");
         println(var10, "set MEM_ARGS=");
         println(var10, "");
         println(var10, "@rem Call WebLogic Server");
         println(var10, "");
         println(var10, "set CLASSPATH=%WEBLOGIC_CLASSPATH%;%POINTBASE_CLASSPATH%;%JAVA_HOME%\\jre\\lib\\rt.jar;%WL_HOME%\\server\\lib\\webservices.jar;%CLASSPATH%");
         println(var10, "\"%JAVA_HOME%\\bin\\java\" %JAVA_VM% %MEM_ARGS% %JAVA_OPTIONS% -Dweblogic.Name=%SERVER_NAME% -Dweblogic.management.username=%WLS_USER% -Dweblogic.management.password=%WLS_PW% -Dweblogic.ProductionModeEnabled=%STARTMODE% -Djava.security.policy=\"%WL_HOME%\\server\\lib\\weblogic.policy\" weblogic.Server");
         println(var10, "ENDLOCAL");
         var9.close();
      } catch (IOException var11) {
         var11.printStackTrace();
      }

   }

   public static void main(String[] var0) throws IOException {
      writeCmdFile(var0[0], var0[1], var0[2], var0[3]);
      writeShFile(var0[0], var0[1], var0[2], var0[3]);
   }

   private static void println(PrintStream var0, String var1) {
      var0.print(var1 + "\n");
   }

   private static void print(PrintStream var0, String var1) {
      var0.print(var1);
   }
}
