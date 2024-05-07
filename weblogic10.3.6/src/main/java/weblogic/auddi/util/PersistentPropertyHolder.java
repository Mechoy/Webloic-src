package weblogic.auddi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

public class PersistentPropertyHolder extends PropertyHolderImpl {
   private boolean m_isChanged = false;
   private long m_interval = 5000L;
   private boolean m_initialized = false;
   private String m_header = "A property file used by AUDDI";
   private String m_fileName = null;
   private static String s_headerPrefix = "###################################################################\n#\n#  AUDDI Configuration File\n#\n#  Created : ";
   private static String s_headerSuffix = "\n#\n###################################################################\n\n";
   private static SimpleDateFormat s_sdf = new SimpleDateFormat("yyyyy.MMMMM.dd GGG hh:mm aaa");

   public PersistentPropertyHolder(String var1) throws PropertyFileException {
      Logger.trace("+PersistentPropertyHolder.CTOR()");
      this.init(var1);
      Logger.trace("-PersistentPropertyHolder.CTOR()");
   }

   public PersistentPropertyHolder(String var1, Properties var2) throws PropertyFileException {
      super(var2);
      Logger.trace("+PersistentPropertyHolder.CTOR()");
      this.init(var1);
      Logger.trace("-PersistentPropertyHolder.CTOR()");
   }

   private void init(String var1) throws PropertyFileException {
      this.m_fileName = var1;
      this.loadFile();
      this.updateFile();
      IntervalTimer var2 = new IntervalTimer(new TimerListener() {
         public void onTimer() {
            try {
               PersistentPropertyHolder.this.updateFile();
            } catch (PropertyFileException var2) {
               throw new IllegalStateException(var2.getMessage());
            }
         }
      }, this.m_interval);
      var2.start();
   }

   public String setProperty(String var1, String var2) {
      Logger.trace("+PersistentPropertyHolder.setProperty()");
      this.m_isChanged = true;
      String var3 = super.setProperty(var1, var2);
      Logger.trace("-PersistentPropertyHolder.setProperty()");
      return var3;
   }

   private void loadFile() throws PropertyFileException {
      try {
         File var1 = new File(this.m_fileName);
         if (var1.exists()) {
            FileInputStream var2 = new FileInputStream(var1);
            this.m_props.load(var2);
            var2.close();
         } else {
            var1.createNewFile();
         }

      } catch (IOException var3) {
         throw new PropertyFileException("Error creating/reading property file: [" + this.m_fileName + "]");
      }
   }

   private void updateFile() throws PropertyFileException {
      if (this.m_isChanged) {
         try {
            FileWriter var1 = new FileWriter(this.m_fileName);
            Object[] var2 = this.m_props.keySet().toArray();
            Arrays.sort(var2);
            var1.write(getHeader());

            for(int var3 = 0; var3 < var2.length; ++var3) {
               String var4 = (String)var2[var3];
               String var5 = (String)this.m_props.get(var4);
               var1.write(var4 + "=" + var5 + "\n");
            }

            var1.flush();
            var1.close();
            this.m_isChanged = false;
            Logger.info("AUDDI configuration changes have been saved to properties file.");
         } catch (IOException var6) {
            throw new PropertyFileException("Unable to update Property file: " + this.m_fileName);
         }
      }

   }

   public static void main(String[] var0) throws PropertyFileException, InterruptedException {
      PersistentPropertyHolder var1 = new PersistentPropertyHolder("uddi.properties");
      int var2 = 0;

      while(var2 < 4) {
         ++var2;
         var1.setProperty("test" + var2, "test value" + var2);
      }

   }

   private static String getHeader() {
      StringBuffer var0 = new StringBuffer();
      var0.append(s_headerPrefix);
      var0.append(s_sdf.format(new Date()));
      var0.append(s_headerSuffix);
      return var0.toString();
   }
}
