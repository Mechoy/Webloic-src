package weblogic.io.common;

import java.io.FilenameFilter;
import java.io.IOException;
import weblogic.common.T3Exception;

/** @deprecated */
public interface T3File {
   /** @deprecated */
   T3FileInputStream getFileInputStream() throws T3Exception;

   /** @deprecated */
   T3FileInputStream getFileInputStream(int var1, int var2) throws T3Exception;

   /** @deprecated */
   T3FileOutputStream getFileOutputStream() throws T3Exception;

   /** @deprecated */
   T3FileOutputStream getFileOutputStream(int var1, int var2) throws T3Exception;

   /** @deprecated */
   T3File extend(String var1);

   /** @deprecated */
   String getName();

   /** @deprecated */
   String getPath();

   /** @deprecated */
   String getAbsolutePath();

   /** @deprecated */
   String getCanonicalPath() throws IOException;

   /** @deprecated */
   String getParent();

   /** @deprecated */
   boolean exists() throws SecurityException;

   /** @deprecated */
   boolean canWrite() throws SecurityException;

   /** @deprecated */
   boolean canRead() throws SecurityException;

   /** @deprecated */
   boolean isFile() throws SecurityException;

   /** @deprecated */
   boolean isDirectory() throws SecurityException;

   /** @deprecated */
   boolean isAbsolute();

   /** @deprecated */
   long lastModified() throws SecurityException;

   /** @deprecated */
   long length() throws SecurityException;

   /** @deprecated */
   boolean mkdir() throws SecurityException;

   /** @deprecated */
   boolean renameTo(T3File var1) throws SecurityException;

   /** @deprecated */
   boolean mkdirs() throws SecurityException;

   /** @deprecated */
   String[] list() throws SecurityException;

   /** @deprecated */
   String[] list(FilenameFilter var1) throws SecurityException;

   /** @deprecated */
   boolean delete() throws SecurityException;

   /** @deprecated */
   int hashCode();

   /** @deprecated */
   boolean equals(Object var1);

   /** @deprecated */
   String toString();
}
