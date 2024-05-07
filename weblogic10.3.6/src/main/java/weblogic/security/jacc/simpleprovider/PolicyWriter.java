package weblogic.security.jacc.simpleprovider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import javax.security.jacc.PolicyContextException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.DomainDir;
import weblogic.management.provider.ManagementService;
import weblogic.security.SecurityLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

class PolicyWriter {
   private static DebugLogger jaccDebugLogger = DebugLogger.getDebugLogger("DebugSecurityJACCNonPolicy");
   private static final String defaultRepository;
   private static final String REPOSITORY = "weblogic.jaccprovider.repository";
   private static final String REPOSITORY_DIR_NAME = "jacc";
   private static String repository = null;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private PolicyWriter() {
   }

   protected static void createRepositoryDirectory() {
      File var0 = new File(repository);
      var0 = new File(var0.getAbsolutePath());
      if (!var0.exists()) {
         if (!var0.mkdirs()) {
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("PolicyWriter unable to create: " + var0.toString());
            }

            throw new RuntimeException(SecurityLogger.getUnableToCreatePolicyWriterDirectory(var0.toString()));
         }

         new File(repository);
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyWriter created JACC repository directory: " + var0.toString());
         }
      } else if (!var0.isDirectory()) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyWriter Cannot create " + var0.toString() + ". Non directory file already exists with the same name. Please remove it");
         }

         throw new RuntimeException(SecurityLogger.getFileInTheWayOfDirectory(var0.toString()));
      }

   }

   protected static void createAppDirectory(String var0) {
      if (var0 == null) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyWriter Cannot create app directory because a null directory name was passed");
         }

         throw new AssertionError(SecurityLogger.getUnexpectedNullVariable("dirPath"));
      } else {
         File var1 = new File(var0);
         var1 = new File(var1.getAbsolutePath());
         if (!var1.exists()) {
            if (!var1.mkdirs()) {
               if (jaccDebugLogger.isDebugEnabled()) {
                  jaccDebugLogger.debug("PolicyWriter unable to create: " + var1.toString());
               }

               throw new RuntimeException(SecurityLogger.getUnableToCreatePolicyWriterDirectory(var1.toString()));
            }

            new File(repository);
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("PolicyWriter created JACC repository directory: " + var1.toString());
            }
         } else {
            if (!var1.isDirectory()) {
               if (jaccDebugLogger.isDebugEnabled()) {
                  jaccDebugLogger.debug("PolicyWriter Cannot create " + var1.toString() + ". Non directory file already exists with the same name. Please remove it");
               }

               throw new RuntimeException(SecurityLogger.getFileInTheWayOfDirectory(var1.toString()));
            }

            String var2 = new String(var0 + File.separator + "granted.policy");
            File var3 = new File(var2);
            if (var3.exists()) {
               if (!var3.delete()) {
                  if (jaccDebugLogger.isDebugEnabled()) {
                     jaccDebugLogger.debug("PolicyWriter.createAppDirectory unable to delete old: " + var2);
                  }

                  throw new RuntimeException("Unable to delete policy file: " + var2);
               }

               if (jaccDebugLogger.isDebugEnabled()) {
                  jaccDebugLogger.debug("PolicyWriter.createAppDirectory removed old " + var2);
               }
            }

            String var4 = new String(var0 + File.separator + "excluded.policy");
            File var5 = new File(var4);
            if (var5.exists()) {
               if (!var5.delete()) {
                  if (jaccDebugLogger.isDebugEnabled()) {
                     jaccDebugLogger.debug("PolicyWriter.createAppDirectory unable to delete old: " + var4);
                  }

                  throw new RuntimeException("Unable to delete policy file: " + var4);
               }

               if (jaccDebugLogger.isDebugEnabled()) {
                  jaccDebugLogger.debug("PolicyWriter.createAppDirectory removed old " + var4);
               }
            }
         }

      }
   }

   protected static void writeGrantStatements(String var0, String var1, String var2) throws PolicyContextException {
      if (var0 == null) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyWriter:writeGrantStatements null appDirName");
         }

      } else if (var1 == null) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyWriter:writeGrantStatements null fileType");
         }

      } else if (var2 == null) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyWriter:writeGrantStatements null grantStatements");
         }

      } else {
         FileOutputStream var3 = null;
         String var4 = var0 + File.separator + var1 + ".policy";

         try {
            var3 = new FileOutputStream(var4);
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("PolicyWriter:writeGrantStatements opened: " + var4);
            }
         } catch (FileNotFoundException var16) {
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("PolicyWriter:policy file can't be be found (filename may be too long): " + var4 + " " + var16);
            }

            throw new PolicyContextException(SecurityLogger.getCannotOpenPolicyFile(var4, var16));
         }

         try {
            writeStatements(var4, var3, var2);
         } catch (IOException var14) {
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("PolicyWriter:cannot write to policy file: " + var4 + " " + var14);
            }

            throw new PolicyContextException(SecurityLogger.getCannotWriteToPolicyFile(var4, var14));
         } finally {
            try {
               var3.close();
               if (jaccDebugLogger.isDebugEnabled()) {
                  jaccDebugLogger.debug("PolicyWriter:writeGrantStatements closed:" + var4);
               }

               var3 = null;
            } catch (IOException var13) {
               if (jaccDebugLogger.isDebugEnabled()) {
                  jaccDebugLogger.debug("PolicyWriter:writeGrantStatements Caught an IOException while trying to close " + var4 + " after an earlier exception.");
               }
            }

         }

      }
   }

   private static void writeStatements(String var0, FileOutputStream var1, String var2) throws IOException {
      var1.write(var2.getBytes());
      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("PolicyWriter:writeStatements wrote to : " + var0 + " the following grant statements: " + var2);
      }

   }

   protected static String generateAppDirectoryFileName(String var0) {
      StringBuffer var1 = new StringBuffer(repository + File.separator + var0);
      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("PolicyWriter generated app directory file name: " + var1.toString());
      }

      return var1.toString();
   }

   protected static void deletePolicyFiles(String var0) throws IOException {
      if (var0 != null) {
         File var1 = new File(var0 + File.separator + "excluded.policy");
         URL var2 = var1.toURL();
         File var3 = new File(var0 + File.separator + "granted.policy");
         URL var4 = var3.toURL();
         if (var1.exists()) {
            var1.delete();
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("PolicyWriter delete removed policy file " + var1.getAbsolutePath());
            }
         }

         var3.delete();
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("PolicyWriter delete removed policy file " + var3.getAbsolutePath());
         }

         deleteFile(var0);
      }
   }

   protected static void deleteFile(String var0) throws IOException {
      if (var0 != null) {
         File var1 = new File(var0);
         if (var1.exists()) {
            var1.delete();
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("PolicyWriter delete removed file " + var1.getAbsolutePath());
            }
         }

      }
   }

   static {
      defaultRepository = DomainDir.getTempDirForServer(ManagementService.getRuntimeAccess(kernelId).getServerName()) + File.separator + "jacc";
      repository = System.getProperty("weblogic.jaccprovider.repository", defaultRepository);
   }
}
