package weblogic.diagnostics.accessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import weblogic.diagnostics.i18n.DiagnosticsTextTextFormatter;
import weblogic.utils.FileUtils;

public class AccessorServletClient implements AccessorClientConstants {
   public static void exportDiagnosticData(String var0, String var1, String var2, String var3, String var4, long var5, long var7, String var9) throws IOException {
      FileOutputStream var10 = new FileOutputStream(var9);
      PrintStream var11 = new PrintStream(var10, false, "UTF-8");
      exportDiagnosticData(var0, var1, var2, var3, var4, var5, var7, var11);
   }

   private static void exportDiagnosticData(String var0, String var1, String var2, String var3, String var4, long var5, long var7, PrintStream var9) throws IOException {
      try {
         String var10 = var2 + "/bea_wls_diagnostics/accessor?" + "logicalName" + "=" + URLEncoder.encode(var3, "UTF-8") + "&" + "query" + "=" + URLEncoder.encode(var4, "UTF-8") + "&" + "beginTimestamp" + "=" + var5 + "&" + "endTimestamp" + "=" + var7;
         URL var11 = new URL(var10);
         HttpURLConnection var12 = (HttpURLConnection)var11.openConnection();
         var12.setRequestProperty("username", URLEncoder.encode(var0, "UTF-8"));
         var12.setRequestProperty("password", URLEncoder.encode(var1, "UTF-8"));
         var12.connect();
         InputStream var13 = var12.getInputStream();
         BufferedReader var14 = new BufferedReader(new InputStreamReader(var13, "UTF-8"));
         String var15 = null;

         while((var15 = var14.readLine()) != null) {
            var9.println(var15);
         }

         int var16 = var12.getResponseCode();
         if (var16 != 200) {
            throw new IOException(DiagnosticsTextTextFormatter.getInstance().getAccessorClientIOException(var16));
         }
      } finally {
         var9.flush();
         var9.close();
      }

   }

   public static String[] listAvailableImages(String var0, String var1, String var2) throws IOException, ClassNotFoundException {
      String var3 = var2 + "/bea_wls_diagnostics/accessor?" + "listAvailableImages" + "=";
      URL var4 = new URL(var3);
      HttpURLConnection var5 = (HttpURLConnection)var4.openConnection();
      var5.setRequestProperty("username", URLEncoder.encode(var0, "UTF-8"));
      var5.setRequestProperty("password", URLEncoder.encode(var1, "UTF-8"));
      var5.connect();
      InputStream var6 = var5.getInputStream();
      ObjectInputStream var7 = new ObjectInputStream(var6);
      String[] var8 = (String[])((String[])var7.readObject());
      int var9 = var5.getResponseCode();
      if (var9 != 200) {
         throw new IOException(DiagnosticsTextTextFormatter.getInstance().getAccessorClientIOException(var9));
      } else {
         return var8;
      }
   }

   public static void saveDiagnosticImageCaptureFile(String var0, String var1, String var2, String var3, String var4) throws IOException {
      String var5 = var2 + "/bea_wls_diagnostics/accessor?" + "getImage" + "=" + URLEncoder.encode(var3, "UTF-8");
      retrieveFile(var0, var1, var4, var5);
   }

   public static void saveDiagnosticImageCaptureEntryFile(String var0, String var1, String var2, String var3, String var4, String var5) throws IOException {
      String var6 = var2 + "/bea_wls_diagnostics/accessor?" + "getImage" + "=" + URLEncoder.encode(var3, "UTF-8") + "&" + "getImageEntry" + "=" + URLEncoder.encode(var4, "UTF-8");
      retrieveFile(var0, var1, var5, var6);
   }

   private static void retrieveFile(String var0, String var1, String var2, String var3) throws MalformedURLException, IOException, UnsupportedEncodingException, FileNotFoundException {
      URL var4 = new URL(var3);
      HttpURLConnection var5 = (HttpURLConnection)var4.openConnection();
      var5.setRequestProperty("username", URLEncoder.encode(var0, "UTF-8"));
      var5.setRequestProperty("password", URLEncoder.encode(var1, "UTF-8"));
      var5.connect();
      InputStream var6 = var5.getInputStream();
      File var7 = new File(var2);
      FileUtils.writeToFile(var6, var7);
      int var8 = var5.getResponseCode();
      if (var8 != 200) {
         if (var7.exists()) {
            var7.delete();
         }

         throw new IOException(DiagnosticsTextTextFormatter.getInstance().getAccessorClientIOException(var8));
      }
   }
}
