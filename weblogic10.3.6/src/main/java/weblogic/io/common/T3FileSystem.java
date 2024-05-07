package weblogic.io.common;

import weblogic.common.T3Exception;

/** @deprecated */
public interface T3FileSystem {
   /** @deprecated */
   String separator();

   /** @deprecated */
   String pathSeparator();

   /** @deprecated */
   T3File getFile(String var1);

   T3File getFile(String var1, String var2);

   /** @deprecated */
   String getName();

   /** @deprecated */
   T3FileInputStream getFileInputStream(String var1) throws T3Exception;

   /** @deprecated */
   T3FileInputStream getFileInputStream(String var1, int var2, int var3) throws T3Exception;

   /** @deprecated */
   T3FileOutputStream getFileOutputStream(String var1) throws T3Exception;

   /** @deprecated */
   T3FileOutputStream getFileOutputStream(String var1, int var2, int var3) throws T3Exception;
}
