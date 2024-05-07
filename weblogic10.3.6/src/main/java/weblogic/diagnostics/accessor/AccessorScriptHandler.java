package weblogic.diagnostics.accessor;

import java.io.IOException;
import org.python.core.PyLong;
import org.python.core.PyObject;
import org.python.core.PyString;
import weblogic.management.scripting.ScriptException;

public class AccessorScriptHandler {
   public static void exportDiagnosticDataFromServer(String var0, String var1, String var2, PyObject[] var3, String[] var4) throws ScriptException {
      PyString var5 = (PyString)var3[0];
      PyString var6 = (PyString)var3[1];
      PyLong var7 = (PyLong)var3[2];
      PyLong var8 = (PyLong)var3[3];
      PyString var9 = (PyString)var3[4];

      try {
         AccessorServletClient.exportDiagnosticData(var0, var1, var2, var5.toString(), var6.toString(), (long)var7.doubleValue(), (long)var8.doubleValue(), var9.toString());
      } catch (IOException var11) {
         throw new ScriptException(var11.getMessage(), "exportDiagnosticDataFromServer");
      }
   }

   public static String[] getAvailableCapturedImages(String var0, String var1, String var2, PyObject[] var3, String[] var4) throws ScriptException {
      String[] var5 = new String[0];

      try {
         var5 = AccessorServletClient.listAvailableImages(var0, var1, var2);
         return var5;
      } catch (Exception var7) {
         throw new ScriptException(var7.getMessage(), "listAvailableImages");
      }
   }

   public static void saveDiagnosticImageCaptureFile(String var0, String var1, String var2, PyObject[] var3, String[] var4) throws ScriptException {
      PyString var5 = (PyString)var3[0];
      PyString var6 = (PyString)var3[1];

      try {
         AccessorServletClient.saveDiagnosticImageCaptureFile(var0, var1, var2, var5.toString(), var6.toString());
      } catch (IOException var8) {
         throw new ScriptException(var8.getMessage(), "saveDiagnosticImageCaptureFile");
      }
   }

   public static void saveDiagnosticImageCaptureEntryFile(String var0, String var1, String var2, PyObject[] var3, String[] var4) throws ScriptException {
      PyString var5 = (PyString)var3[0];
      PyString var6 = (PyString)var3[1];
      PyString var7 = (PyString)var3[2];

      try {
         AccessorServletClient.saveDiagnosticImageCaptureEntryFile(var0, var1, var2, var5.toString(), var6.toString(), var7.toString());
      } catch (IOException var9) {
         throw new ScriptException(var9.getMessage(), "saveDiagnosticImageCaptureEntryFile");
      }
   }
}
