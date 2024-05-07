package weblogic.deploy.service.datatransferhandlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.FileDataStream;
import weblogic.deploy.service.MultiDataStream;
import weblogic.utils.StringUtils;

public class MultipartHelper {
   public static final String FILES_HEADER_NAME = "files_header";
   public static final String NO_FILES = "total_files";
   public static final String FILE_NAME = "name";
   public static final String FILE_TYPE = "type";
   public static final String FILE_LENGTH = "length";
   public static final String DELIMITER = ";";
   public static final String DELIMITER1 = "=";
   public static final String EMPTY_RELATIVE_LOCATION = "NOT_SET";

   public static FileInfo[] parseFilesHeader(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("FilesHeader has invalid value");
      } else {
         String[] var1 = StringUtils.splitCompletely(var0, ";");
         if (isDebugEnabled()) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               debugSay("splits[" + var2 + "] : " + var1[var2]);
            }
         }

         FileInfo[] var4 = constructFileInfos(var1);
         if (isDebugEnabled()) {
            for(int var3 = 0; var3 < var4.length; ++var3) {
               debugSay("FileInfo[" + var3 + "] = " + var4[var3]);
            }
         }

         return var4;
      }
   }

   private static int getNoOfFiles(String[] var0) {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         if (var0[var1].startsWith("total_files")) {
            String[] var2 = StringUtils.splitCompletely(var0[var1], "=");
            if (var2.length != 2) {
               throw new IllegalArgumentException("Invalid number of files value");
            }

            return Integer.valueOf(var2[1]);
         }
      }

      throw new IllegalArgumentException("No numger of files sub header");
   }

   private static FileInfo[] constructFileInfos(String[] var0) {
      int var1 = getNoOfFiles(var0);
      FileInfo[] var2 = new FileInfo[var1];
      int var3 = 0;
      FileInfo var4 = null;

      for(int var5 = 0; var5 < var0.length; ++var5) {
         if (var0[var5].indexOf("total_files") == -1) {
            if (var0[var5].indexOf("name") != -1) {
               var2[var3] = new FileInfo();
               var4 = var2[var3++];
               var4.setName(getValue(var0[var5]));
            } else if (var0[var5].indexOf("type") != -1) {
               var4.setType(getValue(var0[var5]));
            } else if (var0[var5].indexOf("length") != -1) {
               long var6 = Long.valueOf(getValue(var0[var5]));
               var4.setSize(var6);
            }
         }
      }

      return var2;
   }

   private static String getValue(String var0) {
      String[] var1 = StringUtils.splitCompletely(var0, "=");
      if (var1.length >= 2 && var1.length <= 2) {
         return var1[1];
      } else {
         throw new IllegalArgumentException("Invalid name value pair : " + var0);
      }
   }

   static String constructFilesHeaderValue(List var0) {
      if (var0.isEmpty()) {
         throw new IllegalArgumentException(" files list is empty");
      } else {
         String[] var1 = new String[var0.size()];
         StringBuffer var2 = new StringBuffer();
         var2.append("total_files").append("=");
         var2.append(var1.length).append(";");
         Iterator var3 = var0.iterator();

         while(var3.hasNext()) {
            File var4 = (File)var3.next();
            var2.append(constructEachFileHeaderValue(var4));
         }

         return var2.toString();
      }
   }

   static String constructFilesHeaderValue(MultiDataStream var0) throws IOException {
      Iterator var1 = var0.getDataStreams();
      boolean var2 = var1.hasNext();
      if (!var2) {
         throw new IllegalArgumentException("files list is empty");
      } else {
         String[] var3 = new String[var0.getSize()];
         StringBuffer var4 = new StringBuffer();
         var4.append("total_files").append("=");
         var4.append(var3.length).append(";");

         while(var1.hasNext()) {
            FileDataStream var5 = (FileDataStream)var1.next();
            var4.append(constructEachHeaderValue(var5));
         }

         return var4.toString();
      }
   }

   static String constructFilesHeaderValue(String[] var0) {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.add(new File(var0[var2]));
      }

      return constructFilesHeaderValue((List)var1);
   }

   private static String constructEachHeaderValue(FileDataStream var0) throws IOException {
      StringBuffer var1 = new StringBuffer();
      String var2 = var0.getName();
      String var3 = var0.isZip() ? "application/zip" : "text/plain";
      String var4 = Long.toString((long)var0.getLength());
      var1.append("name").append("=");
      var1.append(var2).append(";");
      var1.append("type").append("=");
      var1.append(var3).append(";");
      var1.append("length").append("=");
      var1.append(var4).append(";");
      return var1.toString();
   }

   private static String constructEachFileHeaderValue(File var0) {
      if (!var0.exists()) {
         throw new IllegalArgumentException(" File with name " + var0.getName() + " does not exists");
      } else if (!var0.isFile()) {
         throw new IllegalArgumentException(" File with name " + var0.getName() + " is not a file");
      } else {
         StringBuffer var1 = new StringBuffer();
         String var2 = var0.getName();
         String var3 = var2.endsWith(".jar") ? "application/zip" : "text/plain";
         String var4 = Long.toString(var0.length());
         var1.append("name").append("=");
         var1.append(var2).append(";");
         var1.append("type").append("=");
         var1.append(var3).append(";");
         var1.append("length").append("=");
         var1.append(var4).append(";");
         return var1.toString();
      }
   }

   public static void main(String[] var0) {
      String var1 = constructFilesHeaderValue(var0);
      debugSay("filesHeaderValue : " + var1);
      parseFilesHeader(var1);
   }

   private static boolean isDebugEnabled() {
      return Debug.isServiceHttpDebugEnabled();
   }

   private static void debugSay(String var0) {
      Debug.serviceHttpDebug(" +++ " + var0);
   }

   static class FileInfo {
      private String name;
      private String type;
      private long length;

      public String getType() {
         return this.type;
      }

      public String getName() {
         return this.name;
      }

      public long getSize() {
         return this.length;
      }

      public void setType(String var1) {
         this.type = var1;
      }

      public boolean isZip() {
         return this.type.equals("application/zip");
      }

      public void setName(String var1) {
         this.name = var1;
      }

      public void setSize(long var1) {
         this.length = var1;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("FileInfo(");
         var1.append("name=").append(this.name).append(",");
         var1.append("type=").append(this.type).append(",");
         var1.append("length=").append(this.length);
         var1.append(")");
         return var1.toString();
      }
   }
}
