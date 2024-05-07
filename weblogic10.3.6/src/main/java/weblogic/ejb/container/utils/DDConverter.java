package weblogic.ejb.container.utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import weblogic.ejb.container.utils.ddconverter.ConvertLog;
import weblogic.ejb.container.utils.ddconverter.DDConvertToLatest;
import weblogic.ejb.container.utils.ddconverter.DDConverterBase;
import weblogic.ejb.container.utils.ddconverter.DDConverterException;
import weblogic.ejb.container.utils.ddconverter.DDConverterFactory;
import weblogic.ejb.container.utils.ddconverter.EJBddcTextFormatter;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.utils.Getopt2;
import weblogic.utils.StackTraceUtils;

/** @deprecated */
public final class DDConverter {
   private static final boolean debug = true;
   private static final EJBddcTextFormatter fmt = new EJBddcTextFormatter();

   public static void main(String[] var0) throws DDConverterException {
      System.out.println("\nDEPRECATED: The weblogic.ejb.container.utils.DDConverter tool is deprecated and will be removed in a future version of WebLogic Server.  Please use weblogic.DDConverter instead.\n");
      Getopt2 var1 = new Getopt2();
      var1.setFailOnUnrecognizedOpts(true);
      var1.setUsageHeader("\n\nDDConverter Utility.  Convert EJB 1.0, 1.1, 2.0 deployment descriptors to the latest supported version.\n  Input:\n    DD text (not .ser) from WLS 4.5 and earlier\n    EJB 1.1 jar(s) from WLS 5.1\n    EJB 2.0 jar(s) from WLS 6.x\n  Output:\n    EJB  jar(s) for WLS 9.0");
      var1.setUsageArgs("-d <destDir> file1 [file2] ...");
      var1.addOption("d", "destDir", "The destination directory for the output jar(s).  This is a required option.");
      var1.addOption("c", "jarName", "A jar file into which to combine all beans in the source files");
      var1.addOption("EJBVer", "2.1/1.1", "The output EJB version.  For CMP beans only.  It has to be either 2.1 or 1.1.  Default is 2.1.");
      var1.addOption("log", "logFile", "File into which to dump log information instead of \"ddconverter.log\".");
      var1.addFlag("verboseLog", "Include lots of extra information in the log file.");
      var1.addFlag("help", "Print out this message.");
      String[] var2 = var1.grok(var0).args();
      if (var2.length < 1) {
         var1.usageAndExit("weblogic.ejb.container.utils.DDConverter");
      }

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].endsWith(".xml")) {
            System.err.println("\nERROR: The input file has to be a WLS 4.5 deployment descriptor text file or WLS 5.1 jar file.");
            var1.usageAndExit("weblogic.ejb.container.utils.DDConverter");
         }
      }

      if (!var1.hasOption("d")) {
         System.err.println("\nERROR: The -d option must be specified.");
         var1.usageAndExit("weblogic.ejb.container.utils.DDConverter");
      }

      String var11 = var1.getOption("d");
      String var4 = null;
      if (var1.hasOption("c")) {
         var4 = var1.getOption("c");
      }

      String var5 = "2.1";
      if (var1.hasOption("EJBVer")) {
         var5 = var1.getOption("EJBVer");
      }

      if (!var5.equals("2.1") && !var5.equals("1.1")) {
         System.err.println("\nERROR: The -EJBVer option must be 2.1 or 1.1!");
         var1.usageAndExit("weblogic.ejb.container.utils.DDConverter");
      }

      ConvertLog var6 = null;

      try {
         if (var1.hasOption("log")) {
            var6 = new ConvertLog(new File(var1.getOption("log")));
         } else {
            var6 = new ConvertLog();
         }
      } catch (IOException var10) {
         System.err.println(fmt.ddconverterException(StackTraceUtils.throwable2StackTrace(var10)));
         System.exit(1);
      }

      if (var1.hasOption("verboseLog")) {
         var6.setVerbose(true);
      }

      if (var1.hasOption("help")) {
         var1.usageAndExit("weblogic.ejb.container.utils.DDConverter");
      }

      var6.logInfo(fmt.startup(DateFormat.getDateTimeInstance().format(new Date())));
      var6.logInfo(fmt.startupSources());

      for(int var7 = 0; var7 < var2.length; ++var7) {
         var6.logInfo("\t" + var2[var7]);
      }

      var6.logInfo(fmt.startupTargetDir(var11));
      if (var4 != null) {
         var6.logInfo(fmt.startupCombine(var4));
      }

      boolean var12 = false;

      try {
         DDConverterBase var8 = DDConverterFactory.getDDConverter(var2, var11, var5, var6);
         if (var4 != null) {
            var12 = var8.convert(var4);
         } else {
            var12 = var8.convert();
         }
      } catch (Exception var9) {
         System.err.println(fmt.ddconverterException(StackTraceUtils.throwable2StackTrace(var9)));
      }

      if (!var12) {
         System.err.println(fmt.ddconverterFailure(var6.getLogFileName()));
         System.exit(1);
      }

   }

   public static void convert(EjbDescriptorBean var0) throws DDConverterException {
      (new DDConvertToLatest()).convert(var0);
   }

   public static void convertTo11Latest(EjbDescriptorBean var0) throws DDConverterException {
      (new DDConvertToLatest(false)).convert(var0);
   }
}
