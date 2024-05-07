package weblogic.auddi.util;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Logger {
   public static final String LOGGER_ENV_PREFIX = "logger.";
   public static final int ERROR = 0;
   public static final int WARNING = 1;
   public static final int INFO = 2;
   public static final int DEBUG = 3;
   public static final int TRACE = 4;
   public static final int OUTPUT_SHORT = 20;
   public static final int OUTPUT_LONG = 21;
   public static final int LOG_TYPE_FILE = 14;
   public static final int LOG_TYPE_SCREEN = 15;
   public static final int LOG_TYPE_SCREEN_FILE = 16;
   private int m_logType = -1;
   private int m_outputType = -1;
   private String m_logFileName = null;
   private PrintStream m_userOutput = null;
   private PrintWriter m_logOutput = null;
   private DateFormat m_userDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   private DateFormat m_fileDateFormat = new SimpleDateFormat("yyyy-MM-dd");
   private Calendar m_currentFileCalendar = Calendar.getInstance();
   private int m_nTraceLevel = 4;
   private String m_outputDir = null;
   private static Logger s_instance = null;
   private static ResourceBundle m_rb;
   private boolean m_indentingEnabled = false;
   private String m_indentStr = "  ";
   private int m_currentIndent = 0;
   private String m_currentIndentStr = "";
   private boolean m_addPrefix = true;
   private boolean m_addPrefixBar = true;
   private boolean m_isQuiet = false;
   private boolean m_loggingEnabled = true;
   private File m_logFile = null;
   private long m_maxLogSize = 5000000L;
   String m_currentLogFileName = null;

   public void resetIndent() {
      this.m_currentIndent = 0;
      this.m_currentIndentStr = "";
   }

   private Logger() {
      this.reloadParams();
      if (!this.m_isQuiet) {
         System.err.println("Logger Started");
      }

   }

   public void setAddPrefixBar(boolean var1) {
      this.m_addPrefixBar = var1;
   }

   public boolean getAddPrefixBar() {
      return this.m_addPrefixBar;
   }

   public void setAddPrefix(boolean var1) {
      this.m_addPrefix = var1;
   }

   public boolean getAddPrefix() {
      return this.m_addPrefix;
   }

   public void setQuiet(boolean var1) {
      this.m_isQuiet = var1;
      PropertyManager.setRuntimeProperty("logger.quiet", String.valueOf(var1));
   }

   public boolean isQuiet() {
      return this.m_isQuiet;
   }

   public static void info(String var0) {
      Log(2, (String)var0);
   }

   public static void debug(String var0) {
      Log(3, (String)var0);
   }

   public static void error(String var0) {
      Log(0, (String)var0);
   }

   public static void warning(String var0) {
      Log(1, (String)var0);
   }

   public static void trace(String var0) {
      Log(4, (String)var0);
   }

   public static void info(Throwable var0) {
      Log(2, (Throwable)var0);
   }

   public static void debug(Throwable var0) {
      Log(3, (Throwable)var0);
   }

   public static void warning(Throwable var0) {
      Log(1, (Throwable)var0);
   }

   public static void error(Throwable var0) {
      Log(0, (Throwable)var0);
   }

   public static void trace(Throwable var0) {
      Log(4, (Throwable)var0);
   }

   public void setIndentingEnabled(boolean var1) {
      this.m_indentingEnabled = var1;
      if (s_instance != null) {
         PropertyManager.setRuntimeProperty("logger.indent.enabled", String.valueOf(this.m_indentingEnabled));
      }

   }

   public void setIndentingEnabled(Boolean var1) {
      this.setIndentingEnabled(var1);
   }

   public boolean isEnabled() {
      return this.m_loggingEnabled;
   }

   public void setEnabled(boolean var1) {
      this.m_loggingEnabled = var1;
   }

   public boolean isIndentingEnabled() {
      return this.m_indentingEnabled;
   }

   public void setIndentSize(Integer var1) {
      this.m_indentStr = "";

      for(int var2 = 0; var2 < var1; ++var2) {
         this.m_indentStr = this.m_indentStr + " ";
      }

      if (s_instance != null) {
         PropertyManager.setRuntimeProperty("logger.indent.size", var1.toString());
      }

   }

   public int getIndentSize() {
      return this.m_indentStr.length();
   }

   private static String getParam(String var0) {
      String var1 = PropertyManager.getRuntimeProperty(var0);
      if (var1 == null) {
         var1 = System.getProperty(var0);
      }

      if (var1 == null) {
         try {
            var1 = m_rb.getString(var0);
         } catch (MissingResourceException var3) {
         }
      }

      if (var1 != null) {
         var1 = var1.trim();
      }

      return var1;
   }

   public void reloadParams() {
      m_rb = Util.getResource((Object)this);
      String var1 = PropertyManager.getRuntimeProperty("logger.quiet");
      if (var1 == null) {
         var1 = System.getProperty("logger.quiet");
      }

      if (var1 != null && var1.equals("false")) {
         this.m_isQuiet = false;
      } else {
         this.m_isQuiet = true;
      }

      this.m_nTraceLevel = this.getValueFromString(getParam("logger.verbosity"));
      this.m_outputDir = getParam("logger.log.dir");
      if (this.m_outputDir == null) {
         this.m_outputDir = getParam("logger.log.dir." + Util.getOSName());
         if (this.m_outputDir == null) {
            this.m_outputDir = ".";
         }
      }

      this.m_logType = this.getValueFromString(getParam("logger.log.type"));
      this.m_outputType = this.getValueFromString(getParam("logger.output.style"));
      this.m_logFileName = getParam("logger.log.file.stem");
      this.setIndentSize(Integer.valueOf(getParam("logger.indent.size")));
      this.setIndentingEnabled(new Boolean(getParam("logger.indent.enabled")));
      this.m_maxLogSize = Long.parseLong(getParam("logger.file.maxsize"));
   }

   public void outputParams(int var1) {
      this.log(var1, "New Logger parameters : ");
      this.log(var1, "       Logger Level : " + this.getTraceLevelAsString(this.m_nTraceLevel));
      this.log(var1, "        Output Dir : " + this.m_outputDir);
      this.log(var1, "          Log Type : " + this.getLogTypeAsString(this.m_logType));
      this.log(var1, "       Output Type : " + this.getOutputTypeAsString(this.m_outputType));
      this.log(var1, "   Log File Prefix : " + this.m_logFileName);
      this.log(var1, " Log File Max Size : " + this.m_maxLogSize);
      this.log(var1, "       Indent Size : " + this.m_indentStr.length());
      this.log(var1, "    Indent Enabled : " + this.m_indentingEnabled);
   }

   public void setLogOutputDirectory(String var1) {
      File var2 = new File(var1);
      if (var2.exists()) {
         this.m_outputDir = var1;
         PropertyManager.setRuntimeProperty("logger.log.dir", this.m_outputDir);
      } else {
         throw new LoggerException("Logger output directory '" + var1 + "' does not exist.");
      }
   }

   public void setLogFileStem(String var1) {
      this.m_logFileName = var1;
      PropertyManager.setRuntimeProperty("logger.log.file.stem", this.m_logFileName);
   }

   public String getLogFileStem() {
      return this.m_logFileName;
   }

   public static Logger instance() {
      if (s_instance == null) {
         Class var0 = Logger.class;
         synchronized(Logger.class) {
            if (s_instance == null) {
               s_instance = new Logger();
            }
         }
      }

      return s_instance;
   }

   public void setLogType(Integer var1) {
      this.setLogType(var1);
   }

   public int getLogType() {
      return this.m_logType;
   }

   public void setLogType(String var1) {
      int var2 = this.getValueFromString(var1);
      this.setLogType(var2);
   }

   public void setLogType(int var1) {
      if (!this.m_isQuiet) {
         this.getStream().println("# Logger log type set from '" + this.getLogTypeAsString(this.m_logType) + "' to '" + this.getLogTypeAsString(var1) + "'");
      }

      if (var1 == 15 && this.m_logOutput != null) {
         this.m_logOutput.close();
         this.m_logOutput = null;
      }

      this.m_logType = var1;
      PropertyManager.setRuntimeProperty("logger.log.type", this.getLogTypeAsString(this.m_logType));
   }

   public String getOutputDir() {
      return this.m_outputDir;
   }

   private void initLog() {
      this.initLog((String)null);
   }

   protected void finalize() throws Throwable {
      super.finalize();
      if (this.m_userOutput != null && this.m_userOutput != System.err) {
         this.m_userOutput.close();
      }

      if (this.m_logOutput != null) {
         this.m_logOutput.close();
      }

   }

   public final PrintStream getStream() {
      if (this.m_userOutput == null) {
         this.m_userOutput = System.err;
      }

      return this.m_userOutput;
   }

   public final void setStream(PrintStream var1) {
      this.m_userOutput = var1;
   }

   public void setTraceLevel(String var1) {
      int var2 = this.getValueFromString(var1);
      this.setTraceLevel(var2);
   }

   public void setTraceLevel(int var1) {
      if (!this.m_isQuiet) {
         this.getStream().println("# Logger level set from '" + this.getTraceLevelAsString(this.m_nTraceLevel) + "' to '" + this.getTraceLevelAsString(var1) + "'");
      }

      this.m_nTraceLevel = var1;
      PropertyManager.setRuntimeProperty("logger.verbosity", this.getTraceLevelAsString(var1));
   }

   public void setTraceLevel(Integer var1) {
      this.setTraceLevel(var1);
   }

   public final synchronized void log(int var1, String var2) {
      if (var2 != null) {
         if (var1 <= this.m_nTraceLevel) {
            StringBuffer var3 = new StringBuffer();
            this.addPrefix(var3, var2);
            var3.append(var2).append("\n");
            this.writeToStream(var3.toString());
         }

      }
   }

   private void addPrefix(StringBuffer var1, String var2) {
      if (this.m_outputType == 21) {
         var1.append(this.m_userDateFormat.format(new Date()));
         var1.append(" | ").append(Thread.currentThread().toString()).append(" | ");
      } else if (this.m_addPrefix) {
         var1.append(" | ");
      }

      if (this.m_indentingEnabled) {
         if (var2 != null && var2.startsWith("-")) {
            --this.m_currentIndent;
            if (!this.m_currentIndentStr.equals("")) {
               this.m_currentIndentStr = this.m_currentIndentStr.substring(0, this.m_currentIndentStr.length() - this.m_indentStr.length());
            }
         }

         var1.append(this.m_currentIndentStr);
         if (var2 != null && var2.startsWith("+")) {
            this.m_currentIndentStr = this.m_currentIndentStr + this.m_indentStr;
            ++this.m_currentIndent;
         }
      }

   }

   public final synchronized void log(int var1, Throwable var2) {
      if (var2 != null) {
         if (var1 <= this.m_nTraceLevel) {
            StringBuffer var3 = new StringBuffer();
            this.addPrefix(var3, (String)null);
            this.writeToStream(var3.toString());
            this.writeToStream(var2);
         }

      }
   }

   public static void Log(int var0, String var1) {
      instance().log(var0, var1);
   }

   public static void Log(int var0, Throwable var1) {
      instance().log(var0, var1);
   }

   public final boolean canLog(int var1) {
      return var1 <= this.m_nTraceLevel;
   }

   public static final boolean CanLog(int var0) {
      return instance().canLog(var0);
   }

   public int getTraceLevel() {
      return this.m_nTraceLevel;
   }

   public String getTraceLevelAsString(int var1) {
      switch (var1) {
         case 0:
            return "ERROR";
         case 1:
            return "WARNING";
         case 2:
            return "INFO";
         case 3:
            return "DEBUG";
         case 4:
            return "TRACE";
         default:
            throw new LoggerException("invalid Logger verbosity : " + var1);
      }
   }

   public String getLogTypeAsString(int var1) {
      switch (var1) {
         case 14:
            return "LOG_TYPE_FILE";
         case 15:
            return "LOG_TYPE_SCREEN";
         case 16:
            return "LOG_TYPE_SCREEN_FILE";
         default:
            throw new LoggerException("invalid Logger log type : " + var1);
      }
   }

   public String getOutputTypeAsString(int var1) {
      switch (var1) {
         case 20:
            return "OUTPUT_SHORT";
         case 21:
            return "OUTPUT_LONG";
         default:
            throw new LoggerException("invalid Logger output type : " + var1);
      }
   }

   public int getOutputType() {
      return this.m_outputType;
   }

   public void setOutputType(String var1) {
      int var2 = this.getValueFromString(var1);
      this.setOutputType(var2);
   }

   public void setOutputType(int var1) {
      if (!this.m_isQuiet) {
         this.getStream().println("# Logger Output Type set from '" + this.getOutputTypeAsString(this.m_outputType) + "' to '" + this.getOutputTypeAsString(var1) + "'");
      }

      this.m_outputType = var1;
      PropertyManager.setRuntimeProperty("logger.output.style", this.getOutputTypeAsString(this.m_outputType));
   }

   public void setMaxSize(long var1) {
      if (!this.m_isQuiet) {
         this.getStream().println("# Logger Maximum file size set from '" + this.m_maxLogSize + "' to '" + var1 + "'");
      }

      this.m_maxLogSize = var1;
      PropertyManager.setRuntimeProperty("logger.file.maxsize", String.valueOf(this.m_maxLogSize));
   }

   private int getValueFromString(String var1) {
      if (var1.equals("TRACE")) {
         return 4;
      } else if (var1.equals("ERROR")) {
         return 0;
      } else if (var1.equals("INFO")) {
         return 2;
      } else if (var1.equals("DEBUG")) {
         return 3;
      } else if (var1.equals("WARNING")) {
         return 1;
      } else if (var1.equals("OUTPUT_SHORT")) {
         return 20;
      } else if (var1.equals("OUTPUT_LONG")) {
         return 21;
      } else if (var1.equals("LOG_TYPE_FILE")) {
         return 14;
      } else if (var1.equals("LOG_TYPE_SCREEN")) {
         return 15;
      } else if (var1.equals("LOG_TYPE_SCREEN_FILE")) {
         return 16;
      } else {
         throw new LoggerException("The value '" + var1 + "' during Logger initialization was invalid.  Check your Logger property file.");
      }
   }

   public void restartLog() {
      if (this.m_logOutput != null) {
         synchronized(this.m_logOutput) {
            String var2 = "rotating the log file...";

            try {
               switch (this.m_logType) {
                  case 14:
                     this.logToFile(var2, false);
                     break;
                  case 15:
                     this.m_userOutput.write(var2.getBytes());
                     break;
                  case 16:
                     this.m_userOutput.write(var2.getBytes());
                     this.logToFile(var2, false);
               }
            } catch (Exception var6) {
               String var4 = "# Logger.writeToStream error: " + var6;
               System.err.println(var4);
               var6.printStackTrace(System.err);
               throw new LoggerException(var4);
            }

            this.m_logOutput.close();
            this.m_logOutput = null;
         }
      }

   }

   private void initLog(String var1) {
      this.initLog(var1, false);
   }

   private void initLog(String var1, boolean var2) {
      StringBuffer var3 = new StringBuffer(this.m_logFileName);
      this.m_currentFileCalendar.setTime(new Date());
      var3.append("_").append(this.m_fileDateFormat.format(this.m_currentFileCalendar.getTime())).append(".txt");
      String var4 = var1;
      if (var1 == null) {
         var4 = this.m_outputDir;
      }

      if (!var4.endsWith(File.separator)) {
         var4 = var4 + File.separator;
      }

      this.m_outputDir = var4;
      var3 = (new StringBuffer(var4)).append(var3);
      this.m_logFile = new File(var3.toString());
      String var5 = var3.toString();
      String var8;
      if (var2 && this.m_logFile.exists()) {
         int var6 = 1;
         String var7 = var5.substring(0, var5.length() - 4);
         var8 = var7 + "_" + var6 + ".txt";

         for(File var9 = new File(var8); var9.exists() && var9.length() > this.m_maxLogSize; var9 = new File(var8)) {
            ++var6;
            var8 = var7 + "_" + var6 + ".txt";
         }

         var5 = var8;
         this.m_logFile = new File(var8);
      }

      this.m_currentLogFileName = var5;

      try {
         this.m_logOutput = new PrintWriter(new FileWriter(var5, true));
      } catch (Exception var11) {
         try {
            this.m_logOutput = new PrintWriter(new FileWriter(var5));
         } catch (Exception var10) {
            var8 = "# EXC during creation of the Log for '" + var5 + "'";
            System.err.println(var8);
            var10.printStackTrace();
            throw new LoggerException(var8);
         }
      }

      if (!var2) {
         instance().log(4, (String)"\n--------------------------------------------------------------------------------\n");
         instance().log(4, (String)("Log file created at : " + var5));
      }

   }

   private void logToFile(String var1) {
      this.logToFile(var1, true);
   }

   private void logToFile(String var1, boolean var2) {
      if (this.m_logOutput == null) {
         this.initLog();
      }

      try {
         synchronized(this.m_logOutput) {
            if (var2 && this.m_logFile.length() > this.m_maxLogSize) {
               this.restartLog();
               this.initLog((String)null, true);
               this.logToFile(var1);
            } else {
               this.m_logOutput.write(var1);
               this.m_logOutput.flush();
            }
         }
      } catch (Exception var6) {
         String var4 = "# EXC during log : " + var6;
         System.err.println(var4);
         var6.printStackTrace(System.err);
         throw new LoggerException(var4);
      }
   }

   private void logToFile(Throwable var1) {
      if (this.m_logOutput == null) {
         this.initLog();
      }

      try {
         synchronized(this.m_logOutput) {
            var1.printStackTrace(this.m_logOutput);
            this.m_logOutput.flush();
         }
      } catch (Exception var5) {
         String var3 = "# EXC during log : " + var5;
         System.err.println(var3);
         var5.printStackTrace(System.err);
         throw new LoggerException(var3);
      }
   }

   private void writeToStream(String var1) {
      if (this.m_loggingEnabled) {
         if (this.m_userOutput == null) {
            this.m_userOutput = System.err;
         }

         try {
            switch (this.m_logType) {
               case 14:
                  this.logToFile(var1);
                  break;
               case 15:
                  this.m_userOutput.print(var1);
                  break;
               case 16:
                  this.m_userOutput.print(var1);
                  this.logToFile(var1);
            }

         } catch (Exception var4) {
            String var3 = "# Logger.writeToStream error: " + var4;
            System.err.println(var3);
            var4.printStackTrace(System.err);
            throw new LoggerException(var3);
         }
      }
   }

   private void writeToStream(Throwable var1) {
      if (this.m_loggingEnabled) {
         if (this.m_userOutput == null) {
            this.m_userOutput = System.err;
         }

         try {
            switch (this.m_logType) {
               case 14:
                  this.logToFile(var1);
                  break;
               case 15:
                  var1.printStackTrace(this.m_userOutput);
                  break;
               case 16:
                  var1.printStackTrace(this.m_userOutput);
                  this.logToFile(var1);
            }

         } catch (Exception var4) {
            String var3 = "# Logger.writeToStream error: " + var4;
            System.err.println(var3);
            var4.printStackTrace(System.err);
            throw new LoggerException(var3);
         }
      }
   }

   public String getLogFileName() {
      return this.m_currentLogFileName == null ? "" : this.m_currentLogFileName;
   }

   public static void main(String[] var0) {
   }
}
