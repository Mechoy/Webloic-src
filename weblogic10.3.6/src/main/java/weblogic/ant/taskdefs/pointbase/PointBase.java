package weblogic.ant.taskdefs.pointbase;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Java;

public class PointBase extends Java implements Runnable {
   private static final String POINTBASE_MAIN_CLASS = "com.pointbase.net.netServer";
   private static final int DEFAULT_PORT = 9092;
   private boolean win = false;
   private int displayLevel = -1;
   private String database = null;
   private int port = 9092;
   private File logFile = null;
   private File pointbaseini = null;
   private boolean noconsole = true;
   private long timeout = 0L;
   private String home = null;
   private boolean execFailed = false;

   public void setWin(boolean var1) {
      this.win = var1;
   }

   public void setDisplayLevel(int var1) {
      this.displayLevel = var1;
   }

   public void setDatabase(String var1) {
      this.database = var1;
   }

   public void setPort(int var1) {
      this.port = var1;
   }

   public void setFile(File var1) {
      this.logFile = var1;
   }

   public void setPointBaseIni(File var1) {
      this.pointbaseini = var1;
   }

   public void setNoConsole(boolean var1) {
      this.noconsole = var1;
   }

   public void setTimeout(long var1) {
      this.timeout = var1 * 1000L;
   }

   public void setHome(String var1) {
      this.home = var1;
   }

   public void execute() throws BuildException {
      (new Thread(this, "Execute-PointBase")).start();
   }

   private void executePointBase() {
      this.setFork(true);
      this.setClassname("com.pointbase.net.netServer");
      this.setProperty("database.home", this.home);
      if (this.win) {
         this.createArg().setValue("/win");
      }

      if (this.displayLevel >= 0) {
         this.createArg().setValue("/d:" + this.displayLevel);
      }

      if (this.database != null) {
         this.createArg().setValue("/database:" + this.database);
      }

      this.createArg().setValue("/port:" + this.port);
      if (this.logFile != null) {
         this.createArg().setValue("/file=" + this.logFile.toString());
      }

      if (this.pointbaseini != null) {
         this.createArg().setValue("/pointbase.ini=" + this.pointbaseini.toString());
      }

      if (this.noconsole) {
         this.createArg().setValue("/noconsole");
      }

      if (this.executeJava() != 0) {
         this.execFailed = true;
      }

   }

   private void setProperty(String var1, Object var2) {
      if (var2 != null) {
         this.createJvmarg().setValue("-D" + var1 + "=" + var2.toString());
      }

   }

   private void setProperty(String var1, boolean var2) {
      if (var2) {
         this.createJvmarg().setValue("-D" + var1);
      }

   }

   public void run() {
      this.executePointBase();
   }
}
