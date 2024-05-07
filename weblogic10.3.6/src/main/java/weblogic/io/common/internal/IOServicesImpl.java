package weblogic.io.common.internal;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import weblogic.common.T3Client;
import weblogic.common.T3Exception;
import weblogic.common.T3ServicesDef;
import weblogic.io.common.IOServicesDef;
import weblogic.io.common.T3File;
import weblogic.io.common.T3FileInputStream;
import weblogic.io.common.T3FileOutputStream;
import weblogic.io.common.T3FileSystem;

public class IOServicesImpl implements IOServicesDef {
   private T3Client t3;
   private T3ServicesDef svc;
   private Context ctxFile;
   private Vector fileSystemList = null;
   private Hashtable remoteFileSystemCache;
   public T3FileSystem localFileSystem;

   public IOServicesImpl(T3ServicesDef var1, T3Client var2) throws T3Exception {
      this.t3 = var2;
      this.svc = var1;
      this.localFileSystem = new T3FileSystemLocal();
      this.remoteFileSystemCache = new Hashtable();

      try {
         this.ctxFile = (Context)var1.name().getInitialContext().lookup("weblogic.fileSystem");
      } catch (Exception var4) {
         throw new T3Exception("Unable to JNDI-lookup file services on the server", var4);
      }
   }

   public T3FileSystem getFileSystem(String var1) throws T3Exception {
      if (var1 != null && !var1.equals("")) {
         Object var2 = (T3FileSystem)this.remoteFileSystemCache.get(var1);
         if (var2 == null) {
            T3FileSystemProxy var3;
            try {
               var3 = (T3FileSystemProxy)this.ctxFile.lookup(var1);
            } catch (NamingException var5) {
               throw new T3Exception("Unknown file system " + var1);
            }

            var2 = new T3FileSystemRemote(this.svc, var3);
            this.remoteFileSystemCache.put(var1, var2);
         }

         return (T3FileSystem)var2;
      } else {
         return this.localFileSystem;
      }
   }

   public Enumeration listFileSystems() throws T3Exception {
      if (this.fileSystemList == null) {
         NamingEnumeration var1;
         try {
            var1 = this.ctxFile.list("");
         } catch (NamingException var3) {
            throw new T3Exception("Unable to list file systems", var3);
         }

         this.fileSystemList = new Vector();

         while(var1.hasMoreElements()) {
            this.fileSystemList.addElement(((NameClassPair)var1.nextElement()).getName());
         }
      }

      return this.fileSystemList.elements();
   }

   public T3File getFile(String var1) throws T3Exception {
      String var2 = null;
      if (var1 != null && var1.length() > 2) {
         char var3 = var1.charAt(0);
         int var4 = var1.indexOf(var3, 2);
         if ((var3 == '/' || var3 == '\\') && var1.charAt(1) == var3 && var4 != -1) {
            var2 = var1.substring(2, var4);
            var1 = var1.substring(var4 + 1);
         }
      }

      return this.getFileSystem(var2).getFile(var1);
   }

   public T3FileInputStream getFileInputStream(T3File var1) throws T3Exception {
      return var1.getFileInputStream();
   }

   public T3FileInputStream getFileInputStream(T3File var1, int var2, int var3) throws T3Exception {
      return var1.getFileInputStream(var2, var3);
   }

   public T3FileOutputStream getFileOutputStream(T3File var1) throws T3Exception {
      return var1.getFileOutputStream();
   }

   public T3FileOutputStream getFileOutputStream(T3File var1, int var2, int var3) throws T3Exception {
      return var1.getFileOutputStream(var2, var3);
   }

   public T3FileInputStream getFileInputStream(String var1) throws T3Exception {
      return this.getFile(var1).getFileInputStream();
   }

   public T3FileInputStream getFileInputStream(String var1, int var2, int var3) throws T3Exception {
      return this.getFile(var1).getFileInputStream(var2, var3);
   }

   public T3FileOutputStream getFileOutputStream(String var1) throws T3Exception {
      return this.getFile(var1).getFileOutputStream();
   }

   public T3FileOutputStream getFileOutputStream(String var1, int var2, int var3) throws T3Exception {
      return this.getFile(var1).getFileOutputStream(var2, var3);
   }
}
