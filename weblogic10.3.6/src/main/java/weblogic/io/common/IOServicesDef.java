package weblogic.io.common;

import java.util.Enumeration;
import weblogic.common.T3Exception;

/** @deprecated */
public interface IOServicesDef {
   /** @deprecated */
   T3FileSystem getFileSystem(String var1) throws T3Exception;

   Enumeration listFileSystems() throws T3Exception;

   /** @deprecated */
   T3File getFile(String var1) throws T3Exception;

   /** @deprecated */
   T3FileInputStream getFileInputStream(T3File var1) throws T3Exception;

   /** @deprecated */
   T3FileInputStream getFileInputStream(T3File var1, int var2, int var3) throws T3Exception;

   /** @deprecated */
   T3FileOutputStream getFileOutputStream(T3File var1) throws T3Exception;

   /** @deprecated */
   T3FileOutputStream getFileOutputStream(T3File var1, int var2, int var3) throws T3Exception;

   /** @deprecated */
   T3FileInputStream getFileInputStream(String var1) throws T3Exception;

   /** @deprecated */
   T3FileInputStream getFileInputStream(String var1, int var2, int var3) throws T3Exception;

   /** @deprecated */
   T3FileOutputStream getFileOutputStream(String var1) throws T3Exception;

   /** @deprecated */
   T3FileOutputStream getFileOutputStream(String var1, int var2, int var3) throws T3Exception;
}
