package weblogic.ejb.container.persistence;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.Home;
import weblogic.ejb.container.EJBLogger;
import weblogic.logging.Loggable;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;
import weblogic.xml.process.ProcessorFactory;
import weblogic.xml.process.ProcessorFactoryException;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public class InstalledPersistence {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.persistence.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.persistence.verbose") != null;
   private static String PERSISTENCE_INSTALL_FILE = "persistence.install";
   private String installationLocation = null;
   private Set installedTypes = null;
   private boolean initialized = false;

   public synchronized Set getInstalledTypes() throws PersistenceException {
      if (!this.initialized) {
         this.initialize();
      }

      return this.installedTypes;
   }

   public synchronized PersistenceType getInstalledType(String var1, String var2) throws PersistenceException {
      if (!this.initialized) {
         this.initialize();
      }

      Iterator var3 = this.installedTypes.iterator();

      PersistenceType var4;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var4 = (PersistenceType)var3.next();
      } while(!var4.getIdentifier().equals(var1) || !var4.getVersion().equals(var2));

      return var4;
   }

   private synchronized void initialize() throws PersistenceException {
      this.setInstallationLocation();
      File var1 = new File(this.installationLocation, PERSISTENCE_INSTALL_FILE);
      Loggable var53;
      if (!var1.exists()) {
         var53 = EJBLogger.logInstalledPersistFileNotExistLoggable(var1.getAbsolutePath());
         throw new PersistenceException(var53.getMessage());
      } else if (!var1.canRead()) {
         var53 = EJBLogger.logInstalledPersistFileNotReadableLoggable(var1.getAbsolutePath());
         throw new PersistenceException(var53.getMessage());
      } else {
         BufferedReader var2 = null;

         try {
            try {
               var2 = new BufferedReader(new FileReader(var1));
            } catch (FileNotFoundException var48) {
               Loggable var4 = EJBLogger.logInstalledPersistFileCouldNotOpenLoggable(var1.getAbsolutePath());
               throw new PersistenceException(var4.getMessage());
            }

            try {
               if (verbose) {
                  if (!var2.ready()) {
                     Debug.say("Found no CMP descriptors in '" + var1.getAbsolutePath() + "'.");
                  } else {
                     Debug.say("Loading CMP Installation from '" + var1 + "'.");
                  }
               }

               HashSet var3 = new HashSet();

               label387:
               while(true) {
                  if (!var2.ready()) {
                     var3.add(PersistenceUtils.RDBMS_CMP_RESOURCE_NAME);
                     this.installedTypes = new HashSet();
                     ProcessorFactory var56 = new ProcessorFactory();
                     Iterator var5 = var3.iterator();

                     while(true) {
                        if (!var5.hasNext()) {
                           break label387;
                        }

                        String var6 = (String)var5.next();
                        InputStream var7 = null;
                        var7 = this.getClass().getResourceAsStream(var6);
                        if (var7 == null) {
                           var7 = this.getClass().getResourceAsStream('/' + var6);
                        }

                        if (verbose) {
                           if (var7 == null) {
                              Debug.say("xmlStream for resource '" + var6 + "' is null.");
                           } else {
                              Debug.say("xmlStream for resource '" + var6 + "' is not null.");
                           }
                        }

                        if (var7 == null) {
                           Loggable var57 = EJBLogger.logInstalledPersistErrorLoadingResourceLoggable(var6);
                           throw new PersistenceException(var57.getMessage());
                        }

                        BufferedInputStream var8 = null;

                        try {
                           Loggable var10;
                           try {
                              var8 = new BufferedInputStream(var7);
                              PersistenceVendorProcessor var9 = (PersistenceVendorProcessor)var56.getProcessor((InputStream)var8, true, PersistenceUtils.validPersistencePublicIds);
                              if (var9 == null) {
                                 var10 = EJBLogger.logInstalledPersistNoXMLProcessorLoggable(var6);
                                 throw new PersistenceException(var10.getMessage());
                              }

                              var9.process((InputStream)var8);
                              this.installedTypes.addAll(var9.getInstalledTypes());
                           } catch (XMLParsingException var44) {
                              throw new PersistenceException(var44.getMessage() + ": " + StackTraceUtils.throwable2StackTrace(var44));
                           } catch (XMLProcessingException var45) {
                              throw new PersistenceException(var45.getMessage());
                           } catch (ProcessorFactoryException var46) {
                              var10 = EJBLogger.logInstalledPersistNoXMLProcessorLoggable(var6);
                              throw new PersistenceException(var10.getMessage());
                           }
                        } finally {
                           try {
                              var8.close();
                           } catch (IOException var43) {
                           }

                        }
                     }
                  }

                  String var55 = var2.readLine();
                  if (verbose) {
                     Debug.say("found resource name: " + var55);
                  }

                  if (var55 != null && !var55.trim().equals("")) {
                     var3.add(var55);
                  }
               }
            } catch (IOException var49) {
               throw new PersistenceException(StackTraceUtils.throwable2StackTrace(var49));
            } catch (PersistenceException var50) {
               throw var50;
            } catch (Exception var51) {
               throw new AssertionError("Error while reading CMP Installation file '" + var1.getAbsolutePath() + "': " + StackTraceUtils.throwable2StackTrace(var51));
            }
         } finally {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (IOException var42) {
               }
            }

         }

         if (verbose) {
            Debug.say("Installed persistence types:");
            Iterator var54 = this.installedTypes.iterator();

            while(var54.hasNext()) {
               Debug.say(var54.next().toString());
            }
         }

         this.initialized = true;
      }
   }

   private synchronized void setInstallationLocation() {
      String var1 = Home.getPath();
      if (null == var1) {
         if (verbose) {
            Debug.say("weblogic.Home not found. There must be a problem with the classpath. Unable to find any Persistence Types.");
         }

         throw new AssertionError("weblogic.home not found.  There must be a problem with the classpath.");
      } else {
         this.installationLocation = var1 + File.separator + "lib" + File.separator + "persistence";
         File var2 = new File(this.installationLocation);
         if (!var2.exists()) {
            String var3 = var1 + File.separator + "server" + File.separator + "lib" + File.separator + "persistence";
            var2 = new File(var3);
            if (var2.exists()) {
               this.installationLocation = var3;
            }
         }

         if (verbose) {
            Debug.say("Set installation location to " + this.installationLocation);
         }

      }
   }

   public static void main(String[] var0) {
      InstalledPersistence var1 = new InstalledPersistence();
      System.out.println("Thank you for invoking this tool. It initialized successfully.");

      try {
         Iterator var2 = var1.getInstalledTypes().iterator();

         while(var2.hasNext()) {
            PersistenceType var3 = (PersistenceType)var2.next();
            System.out.println("\n---------------------------------------------");
            System.out.println("Found PersistenceType:");
            System.out.println("" + var3);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
