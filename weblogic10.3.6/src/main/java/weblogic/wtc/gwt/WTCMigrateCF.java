package weblogic.wtc.gwt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.apache.xerces.parsers.DOMParser;
import weblogic.jndi.Environment;
import weblogic.management.DistributedManagementException;
import weblogic.management.MBeanCreationException;
import weblogic.management.MBeanHome;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.WTCExportMBean;
import weblogic.management.configuration.WTCImportMBean;
import weblogic.management.configuration.WTCLocalTuxDomMBean;
import weblogic.management.configuration.WTCPasswordMBean;
import weblogic.management.configuration.WTCRemoteTuxDomMBean;
import weblogic.management.configuration.WTCResourcesMBean;
import weblogic.management.configuration.WTCServerMBean;
import weblogic.management.configuration.WTCtBridgeGlobalMBean;
import weblogic.management.configuration.WTCtBridgeRedirectMBean;

public final class WTCMigrateCF {
   private int ltdcnt = 0;
   private File c_fname = null;
   private HashMap accessMap = new HashMap();
   private HashMap accessLMap = new HashMap();
   private HashMap accessRMap = new HashMap();
   private HashMap accessIdMap = new HashMap();
   private String currSection = null;
   private MBeanHome configHome = null;
   private String serverName = null;
   private String domainName = null;
   private ServerMBean srvrmb = null;
   private String wtcsmbNm = null;
   private WTCServerMBean wtcsmb;
   private static boolean DebugOn = false;
   private static boolean DeployOn = false;
   private static String[] myFldTbls16;
   private static String[] myFldTbls32;
   private static String[] myViewTbls16;
   private static String[] myViewTbls32;
   private static final String localDomainSection = "T_DM_LOCAL_TDOMAIN";
   private static final String remoteDomainSection = "T_DM_REMOTE_TDOMAIN";
   private static final String exportSection = "T_DM_EXPORT";
   private static final String importSection = "T_DM_IMPORT";
   private static final String passwdSection = "T_DM_PASSWORD";
   private static final String resourceSection = "T_DM_RESOURCES";
   private static final String redirSection = "fromto";
   private static final String tbglobalSection = "tBridge";

   public static void main(String[] var0) {
      if (var0.length == 0) {
         System.out.println("Usage: weblogic.wtc.gwt.WTCMigrateCF -url URL -username USERNAME -password PASSWORD -infile CONFIGWTC [-server SERVERNAME] [-domain DOMAIN] [-protocol PROTOCOL] [-deploy]");
      } else {
         WTCMigrateCF var1 = new WTCMigrateCF();
         if (System.getProperty("weblogic.wtc.migrateDebug") != null) {
            DebugOn = true;
         }

         var1.migrate(var0);
      }
   }

   WTCMigrateCF() {
   }

   void migrate(String[] var1) {
      String var2 = null;
      String var3 = null;
      String var4 = null;
      String var5 = null;
      String var6 = null;

      for(int var7 = 0; var7 < var1.length; ++var7) {
         if (var1[var7].equals("-url")) {
            if (DebugOn) {
               System.out.println("URL: " + var1[var7 + 1]);
            }

            var2 = var1[var7 + 1];
         } else if (var1[var7].equals("-username")) {
            if (DebugOn) {
               System.out.println("USERNAME: " + var1[var7 + 1]);
            }

            var3 = var1[var7 + 1];
         } else if (var1[var7].equals("-password")) {
            if (DebugOn) {
               System.out.println("PASSWORD: " + var1[var7 + 1]);
            }

            var4 = var1[var7 + 1];
         } else if (var1[var7].equals("-infile")) {
            if (DebugOn) {
               System.out.println("INFILE: " + var1[var7 + 1]);
            }

            var5 = var1[var7 + 1];
         } else if (var1[var7].equals("-server")) {
            if (DebugOn) {
               System.out.println("SERVERNAME: " + var1[var7 + 1]);
            }

            this.serverName = var1[var7 + 1];
         } else if (var1[var7].equals("-domain")) {
            if (DebugOn) {
               System.out.println("DOMAINNAME: " + var1[var7 + 1]);
            }

            this.domainName = var1[var7 + 1];
         } else if (var1[var7].equals("-deploy")) {
            if (DebugOn) {
               System.out.println("DEPLOY is set: ");
            }

            DeployOn = true;
         } else if (var1[var7].equals("-protocol")) {
            if (DebugOn) {
               System.out.println("PROTOCOL: " + var1[var7 + 1]);
            }

            var6 = var1[var7 + 1];
         }
      }

      if (var2 != null && var3 != null && var4 != null && var5 != null) {
         boolean var20 = true;

         try {
            Environment var8 = new Environment();
            if (var6 == null) {
               var8.setProviderUrl("t3:" + var2);
            } else {
               var8.setProviderUrl(var6 + ":" + var2);
            }

            var8.setSecurityPrincipal(var3);
            var8.setSecurityCredentials(var4);
            Context var9 = var8.getInitialContext();
            this.configHome = (MBeanHome)var9.lookup("weblogic.management.adminhome");
            this.c_fname = new File(var5);
            System.out.println("Migrating WTC config file: " + this.c_fname.getPath());
            int var10 = (int)(Math.random() * 1000000.0);
            this.wtcsmbNm = "WTCServer-" + var10;
            if (this.serverName == null && this.domainName == null) {
               this.serverName = this.configHome.getMBeanServer().getServerName();
               this.domainName = this.configHome.getDomainName();
            } else if (this.serverName == null) {
               this.serverName = this.configHome.getMBeanServer().getServerName();
            } else if (this.domainName == null) {
               this.domainName = this.configHome.getDomainName();
            }

            this.srvrmb = (ServerMBean)this.configHome.getAdminMBean(this.serverName, "Server", this.domainName);
            this.wtcsmb = (WTCServerMBean)this.configHome.createAdminMBean(this.wtcsmbNm, "WTCServer", this.domainName);
            System.out.println("Using domain " + this.domainName + " and server " + this.serverName);
            if (!this.loadFile(this.c_fname)) {
               System.out.println("ERROR: Migration failed for configuration file!");
               return;
            }

            if (DeployOn) {
               try {
                  NamingEnumeration var11 = var9.listBindings("tuxedo.services");
                  if (var11.hasMore()) {
                     DeployOn = false;
                     System.out.println("WARNING: Ignoring deploy option! Only one WTCServerMBean deployment permitted(at a time per server)");
                  }
               } catch (NameNotFoundException var12) {
               }

               if (DeployOn) {
                  this.wtcsmb.addTarget(this.srvrmb);
               }
            }
         } catch (NamingException var13) {
            System.out.println("NamingException ERROR: " + var13.toString());
            var20 = false;
         } catch (NullPointerException var14) {
            System.out.println("NullPointerException ERROR: " + var14.toString());
            var20 = false;
         } catch (MBeanCreationException var15) {
            System.out.println("MBeanCreationException ERROR: " + var15.toString());
            var20 = false;
         } catch (InstanceNotFoundException var16) {
            System.out.println("InstanceNotFoundException ERROR: " + var16.toString());
            var20 = false;
         } catch (InvalidAttributeValueException var17) {
            System.out.println("InvalidattributeException ERROR: " + var17.toString());
            var20 = false;
         } catch (DistributedManagementException var18) {
            System.out.println("DistributedManagementException ERROR: " + var18.toString());
            var20 = false;
         } catch (ManagementException var19) {
            System.out.println("ManagementException ERROR: " + var19.toString());
            var20 = false;
         }

         if (var20) {
            System.out.println("The WTC configuration file migration is done!");
            System.out.println("No error found!!!");
         }

      } else {
         System.out.println("Usage: weblogic.wtc.gwt.WTCMigrateCF -url URL -username USERNAME -password PASSWORD -infile BDMCONFIG [-server SERVERNAME] [-domain DOMAIN] [-deploy]");
      }
   }

   private boolean loadFile(File var1) {
      try {
         FileReader var2 = new FileReader(var1);
         InputSource var3 = new InputSource(var2);
         DOMParser var4 = new DOMParser();
         var4.parse(var3);
         var2.close();
         var2 = null;
         var3 = null;
         Document var5 = var4.getDocument();
         var4 = null;
         if (var5 == null) {
            System.out.println("ERROR: Parser returned null for Config doc.");
            return false;
         } else if (!this.extractInfo(var5)) {
            System.out.println("ERROR: Failed to extract the config attributes.");
            return false;
         } else {
            var5 = null;
            return true;
         }
      } catch (FileNotFoundException var6) {
         System.out.println("ERROR: File not found, reason(" + var6.toString() + ")!");
         return false;
      } catch (IOException var7) {
         System.out.println("ERROR: IO Exception, reason(" + var7.toString() + ")!");
         if (this.currSection != null) {
            System.out.println("INFO: error occurred in " + this.currSection + " element.");
         }

         return false;
      } catch (SAXException var8) {
         System.out.println("ERROR: SAX Exception, reason(" + var8.toString() + ")!");
         if (this.currSection != null) {
            System.out.println("INFO: error occurred in " + this.currSection + " element.");
         }

         return false;
      } catch (NumberFormatException var9) {
         System.out.println("ERROR: Invalid number format(" + var9.toString() + ")!");
         if (this.currSection != null) {
            System.out.println("INFO: error occurred in " + this.currSection + " element.");
         }

         return false;
      } catch (Exception var10) {
         System.out.println("ERROR: Exception, reason(" + var10.toString() + ")!");
         if (this.currSection != null) {
            System.out.println("INFO: error occurred in " + this.currSection + " element.");
         }

         return false;
      }
   }

   private boolean extractInfo(Document var1) throws Exception {
      Element var2 = var1.getDocumentElement();
      if (var2 == null) {
         System.out.println("ERROR: No top element from file " + this.c_fname.getPath());
         return false;
      } else {
         String var3 = var2.getTagName();
         if (var3 != null && var3.equals("WTC_CONFIG")) {
            Element var6 = null;
            Element var7 = null;

            Node var4;
            Element var5;
            String var8;
            for(var4 = var2.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
               if (var4.getNodeType() == 1 && var4 instanceof Element) {
                  var5 = (Element)var4;
                  var8 = var5.getTagName();
                  if (var8.equals("BDMCONFIG")) {
                     var6 = var5;
                  } else if (var8.equals("tBridge")) {
                     var7 = var5;
                  }
               }
            }

            if (var6 == null) {
               System.out.println("ERROR: No BDMCONFIG element in config file " + this.c_fname.getPath());
               return false;
            } else {
               for(var4 = var6.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
                  if (var4.getNodeType() == 1 && var4 instanceof Element) {
                     var5 = (Element)var4;
                     var8 = var5.getTagName();
                     if (var8.equals("T_DM_LOCAL_TDOMAIN")) {
                        ++this.ltdcnt;
                     }
                  }
               }

               if (this.ltdcnt == 0) {
                  System.out.println("ERROR: Requires at least one local domain defined.");
                  return false;
               } else {
                  boolean var9 = true;
                  int var10 = 0;
                  int var11 = 0;
                  int var12 = 0;
                  int var13 = 0;
                  int var14 = 0;

                  for(var4 = var6.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
                     if (var4.getNodeType() == 1 && var4 instanceof Element) {
                        var5 = (Element)var4;
                        var8 = var5.getTagName();
                        if (var8.equals("T_DM_LOCAL_TDOMAIN")) {
                           var9 = this.setupLocalTD(var5, var11);
                           if (!var9) {
                              System.out.println("ERROR: Could not complete processing of element " + var8);
                              return false;
                           }

                           ++var11;
                        }
                     }
                  }

                  for(var4 = var6.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
                     if (var4.getNodeType() == 1 && var4 instanceof Element) {
                        var5 = (Element)var4;
                        var8 = var5.getTagName();
                        if (var8.equals("T_DM_REMOTE_TDOMAIN")) {
                           var9 = this.setupRemoteTD(var5, var10);
                           if (!var9) {
                              System.out.println("ERROR: Could not complete processing of element " + var8);
                              return false;
                           }

                           ++var10;
                        }
                     }
                  }

                  for(var4 = var6.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
                     if (var4.getNodeType() == 1 && var4 instanceof Element) {
                        var5 = (Element)var4;
                        var8 = var5.getTagName();
                        if (var8.equals("T_DM_EXPORT")) {
                           var9 = this.setupExport(var5, var13);
                           if (var9) {
                              ++var13;
                           }
                        } else if (var8.equals("T_DM_IMPORT")) {
                           var9 = this.setupImport(var5, var14);
                           if (var9) {
                              ++var14;
                           }
                        } else if (var8.equals("T_DM_PASSWORD")) {
                           var9 = this.setupPasswd(var5, var12);
                           if (var9) {
                              ++var12;
                           }
                        } else if (var8.equals("T_DM_RESOURCES")) {
                           var9 = this.setupResources(var5);
                        }

                        if (!var9) {
                           System.out.println("ERROR: Could not complete processing of element " + var8);
                           return false;
                        }
                     }
                  }

                  if (var7 == null) {
                     return true;
                  } else {
                     var9 = this.setupTBGlobal(var7);
                     if (!var9) {
                        System.out.println("ERROR: Could not complete tBridge Global processing.");
                        return false;
                     } else {
                        Object var15 = null;

                        for(var4 = var7.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
                           if (var4.getNodeType() == 1 && var4 instanceof Element) {
                              var5 = (Element)var4;
                              var8 = var5.getTagName();
                              if (var15 == null && var8.equals("redirect")) {
                                 int var16 = 0;

                                 for(Node var17 = var5.getFirstChild(); var17 != null; var17 = var17.getNextSibling()) {
                                    if (var17.getNodeType() == 1 && var17 instanceof Element) {
                                       Element var18 = (Element)var17;
                                       String var19 = var18.getTagName();
                                       if (var19.equals("fromto")) {
                                          var9 = this.setupRedirect(var18, var16);
                                          if (!var9) {
                                             System.out.println("ERROR: Could not complete tBridge Redirect processing.");
                                             return false;
                                          }

                                          ++var16;
                                       }
                                    }
                                 }

                                 return true;
                              }
                           }
                        }

                        return true;
                     }
                  }
               }
            }
         } else {
            System.out.println("ERROR: Bad top element name \"" + var3 + "\" from config file " + this.c_fname.getPath());
            return false;
         }
      }
   }

   private boolean setupLocalTD(Element var1, int var2) throws Exception {
      String var3 = "ltd" + var2;
      System.out.println("Processing LTDMBEAN : " + var3 + " started.");
      String var4 = this.currSection;
      this.currSection = "T_DM_LOCAL_TDOMAIN";

      try {
         WTCLocalTuxDomMBean var5 = (WTCLocalTuxDomMBean)this.configHome.createAdminMBean(var3, "WTCLocalTuxDom", this.domainName, this.wtcsmb);
         String var6 = this.getEAVal(var1, "AccessPoint");
         var5.setAccessPoint(var6);
         if (this.accessMap.put(var6, var5) != null) {
            System.out.println("ERROR: Duplicated AccessPoint " + var6 + " found in Local TDomain definitions!");
            this.currSection = var4;
            throw new Exception("Duplicated LTD AccessPoint");
         }

         this.accessLMap.put(var6, var5);
         String var7 = this.getSubElemText(var1, "AccessPointId", true);
         if (this.accessIdMap.put(var7, var5) != null) {
            System.out.println("ERROR: Duplicated AccessPointId " + var7 + " found in Local TDomain definitions!");
            this.currSection = var4;
            throw new Exception("Duplicated LTD AccessPointId");
         }

         var5.setAccessPointId(var7);
         var5.setNWAddr(this.getSubElemText(var1, "NWAddr", true));
         String var8;
         if ((var8 = this.getSubElemText(var1, "Security", false)) != null) {
            var5.setSecurity(var8);
         }

         if ((var8 = this.getSubElemText(var1, "ConnectionPolicy", false)) != null) {
            var5.setConnectionPolicy(var8);
         }

         if ((var8 = this.getSubElemText(var1, "Interoperate", false)) != null) {
            var5.setInteroperate(var8);
         }

         if ((var8 = this.getSubElemText(var1, "RetryInterval", false)) != null) {
            var5.setRetryInterval(Long.parseLong(var8, 10));
         }

         if ((var8 = this.getSubElemText(var1, "MaxRetries", false)) != null) {
            var5.setMaxRetries(Long.parseLong(var8, 10));
         }

         if ((var8 = this.getSubElemText(var1, "BlockTime", false)) != null) {
            var5.setBlockTime(Long.parseLong(var8));
         }

         if ((var8 = this.getSubElemText(var1, "CmpLimit", false)) != null) {
            var5.setCmpLimit(Integer.parseInt(var8, 10));
         }

         int var9 = -1;
         if ((var8 = this.getSubElemText(var1, "MinEncryptBits", false)) != null) {
            var9 = Integer.parseInt(var8, 10);
            var5.setMinEncryptBits(var8);
         }

         int var10 = -1;
         if ((var8 = this.getSubElemText(var1, "MaxEncryptBits", false)) != null) {
            var10 = Integer.parseInt(var8, 10);
            var5.setMaxEncryptBits(var8);
         }

         if (var9 != -1 && var10 != -1 && var9 > var10) {
            System.out.println("ERROR: MinEncryptBits > MaxEncryptBits  found in Local TDomain definition!");
            this.currSection = var4;
            throw new Exception("MinEncryptBits > MaxEncryptBits");
         }
      } catch (MBeanCreationException var11) {
         System.out.println("MBeanCreationException ERROR: " + var11.toString());
         this.currSection = var4;
         throw new Exception(var11.toString());
      } catch (InvalidAttributeValueException var12) {
         System.out.println("InvalidattributeException ERROR: " + var12.toString());
         this.currSection = var4;
         throw new Exception(var12.toString());
      } catch (InstanceNotFoundException var13) {
         System.out.println("InstanceNotFoundException ERROR: " + var13.toString());
         this.currSection = var4;
         throw new Exception(var13.toString());
      } catch (Exception var14) {
         System.out.println("ERROR: Could not create WTCLocalTuxDomMBean , reason(" + var14.toString() + ")!");
         this.currSection = var4;
         throw var14;
      }

      this.currSection = var4;
      System.out.println("Processing LTDMBEAN : " + var3 + " done.");
      return true;
   }

   private boolean setupRemoteTD(Element var1, int var2) throws Exception {
      String var3 = "rtd" + var2;
      System.out.println("Processing RTDMBEAN : " + var3 + " started.");
      String var4 = this.currSection;
      this.currSection = "T_DM_REMOTE_TDOMAIN";

      try {
         WTCRemoteTuxDomMBean var5 = (WTCRemoteTuxDomMBean)this.configHome.createAdminMBean(var3, "WTCRemoteTuxDom", this.domainName, this.wtcsmb);
         String var6 = this.getEAVal(var1, "AccessPoint");
         var5.setAccessPoint(var6);
         if (this.accessMap.put(var6, var5) != null) {
            System.out.println("ERROR: Duplicated AccessPoint " + var6 + " found in Remote TDomain definition!");
            this.currSection = var4;
            throw new Exception("Duplicated RTD AccessPoint");
         }

         this.accessRMap.put(var6, var5);
         String var7 = this.getSubElemText(var1, "LocalAccessPoint", true);
         if (!this.accessLMap.containsKey(var7)) {
            System.out.println("ERROR: Undefined LocalAccessPoint " + var7 + " found in Remote TDomain definition!");
            this.currSection = var4;
            throw new Exception("Undefined RTD LocalAccessPoint");
         }

         var5.setLocalAccessPoint(var7);
         String var8 = this.getSubElemText(var1, "AccessPointId", true);
         StringBuffer var9 = (new StringBuffer(var8)).append(var7);
         String var10 = var9.toString();
         if (this.accessIdMap.put(var10, var5) != null) {
            System.out.println("ERROR: Duplicated combination of RemoteAccessPointId(" + var8 + ") and Local AccessPoint(" + var7 + ") found in Remote TDomain definitions!");
            this.currSection = var4;
            throw new Exception("Duplicated RTD AccessPointId");
         }

         var5.setAccessPointId(var8);
         var5.setNWAddr(this.getSubElemText(var1, "NWAddr", true));
         String var11;
         if ((var11 = this.getSubElemText(var1, "AclPolicy", false)) != null) {
            var5.setAclPolicy(var11);
         }

         if ((var11 = this.getSubElemText(var1, "CredentialPolicy", false)) != null) {
            var5.setCredentialPolicy(var11);
         }

         if ((var11 = this.getSubElemText(var1, "TpUsrFile", false)) != null) {
            var5.setTpUsrFile(var11);
         }

         if ((var11 = this.getSubElemText(var1, "CmpLimit", false)) != null) {
            var5.setCmpLimit(Integer.parseInt(var11, 10));
         }

         int var12 = -1;
         if ((var11 = this.getSubElemText(var1, "MinEncryptBits", false)) != null) {
            var12 = Integer.parseInt(var11, 10);
            var5.setMinEncryptBits(var11);
         }

         int var13 = -1;
         if ((var11 = this.getSubElemText(var1, "MaxEncryptBits", false)) != null) {
            var13 = Integer.parseInt(var11, 10);
            var5.setMaxEncryptBits(var11);
         }

         if (var12 != -1 && var13 != -1 && var12 > var13) {
            System.out.println("ERROR: MinEncryptBits > MaxEncryptBits  found in Remote TDomain definition!");
            this.currSection = var4;
            throw new Exception("MinEncryptBits > MaxEncryptBits");
         }

         if ((var11 = this.getSubElemText(var1, "ConnectionPolicy", false)) != null) {
            var5.setConnectionPolicy(var11);
         }

         if ((var11 = this.getSubElemText(var1, "RetryInterval", false)) != null) {
            var5.setRetryInterval(Long.parseLong(var11, 10));
         }

         if ((var11 = this.getSubElemText(var1, "MaxRetries", false)) != null) {
            var5.setMaxRetries(Long.parseLong(var11, 10));
         }

         if ((var11 = this.getSubElemText(var1, "FederationURL", false)) != null) {
            var5.setFederationURL(var11);
         }

         if ((var11 = this.getSubElemText(var1, "FederationName", false)) != null) {
            var5.setFederationName(var11);
         }
      } catch (MBeanCreationException var14) {
         System.out.println("MBeanCreationException ERROR: " + var14.toString());
         this.currSection = var4;
         throw new Exception(var14.toString());
      } catch (InvalidAttributeValueException var15) {
         System.out.println("InvalidattributeException ERROR: " + var15.toString());
         this.currSection = var4;
         throw new Exception(var15.toString());
      } catch (InstanceNotFoundException var16) {
         System.out.println("InstanceNotFoundException ERROR: " + var16.toString());
         this.currSection = var4;
         throw new Exception(var16.toString());
      } catch (Exception var17) {
         System.out.println("ERROR: Could not create WTCRemoteTuxDomMBean , reason(" + var17.toString() + ")!");
         this.currSection = var4;
         throw var17;
      }

      this.currSection = var4;
      System.out.println("Processing RTDMBEAN : " + var3 + " done.");
      return true;
   }

   private boolean setupExport(Element var1, int var2) throws Exception {
      String var3 = "exp" + var2;
      System.out.println("Processing EXPMBEAN : " + var3 + " started.");
      String var4 = this.currSection;
      this.currSection = "T_DM_EXPORT";

      try {
         WTCExportMBean var5 = (WTCExportMBean)this.configHome.createAdminMBean(var3, "WTCExport", this.domainName, this.wtcsmb);
         String var6 = this.getEAVal(var1, "LocalAccessPoint");
         if (!this.accessLMap.containsKey(var6)) {
            System.out.println("ERROR: Undefined LocalAccessPoint " + var6 + " found in Export definition!");
            this.currSection = var4;
            throw new Exception("Undefined Export LocalAccessPoint");
         }

         var5.setLocalAccessPoint(var6);
         var5.setResourceName(this.getEAVal(var1, "ResourceName"));
         String var7;
         if ((var7 = this.getSubElemText(var1, "RemoteName", false)) != null) {
            var5.setRemoteName(var7);
         }

         if ((var7 = this.getSubElemText(var1, "EJBName", false)) != null) {
            var5.setEJBName(var7);
         }
      } catch (MBeanCreationException var8) {
         System.out.println("MBeanCreationException ERROR: " + var8.toString());
         this.currSection = var4;
         throw new Exception(var8.toString());
      } catch (InvalidAttributeValueException var9) {
         System.out.println("InvalidattributeException ERROR: " + var9.toString());
         this.currSection = var4;
         throw new Exception(var9.toString());
      } catch (InstanceNotFoundException var10) {
         System.out.println("InstanceNotFoundException ERROR: " + var10.toString());
         this.currSection = var4;
         throw new Exception(var10.toString());
      } catch (Exception var11) {
         System.out.println("ERROR: Could not create WTCExport , reason(" + var11.toString() + ")!");
         this.currSection = var4;
         throw var11;
      }

      this.currSection = var4;
      System.out.println("Processing EXPMBEAN : " + var3 + " done.");
      return true;
   }

   private boolean setupImport(Element var1, int var2) throws Exception {
      String var3 = "imp" + var2;
      System.out.println("Processing IMPMBEAN : " + var3 + " started.");
      String var4 = this.currSection;
      this.currSection = "T_DM_IMPORT";

      try {
         WTCImportMBean var5 = (WTCImportMBean)this.configHome.createAdminMBean(var3, "WTCImport", this.domainName, this.wtcsmb);
         String var6 = this.getEAVal(var1, "LocalAccessPoint");
         if (!this.accessLMap.containsKey(var6)) {
            System.out.println("ERROR: Undefined LocalAccessPoint " + var6 + " found in Import definition!");
            this.currSection = var4;
            throw new Exception("Undefined Import LocalAccessPoint");
         }

         var5.setLocalAccessPoint(var6);
         var5.setResourceName(this.getEAVal(var1, "ResourceName"));
         String var7;
         if ((var7 = this.getSubElemText(var1, "RemoteName", false)) != null) {
            var5.setRemoteName(var7);
         }

         String var8 = this.getEAVal(var1, "RemoteAccessPointList");
         if (var8.indexOf(44) == -1) {
            if (!this.accessRMap.containsKey(var8)) {
               System.out.println("ERROR: Undefined RemoteAccessPoint " + var8 + " in Import RemoteAccessPointList definition!");
               this.currSection = var4;
               throw new Exception("Undefined Import RemoteAccessPoint");
            }
         } else {
            StringTokenizer var10 = new StringTokenizer(var8, ",");

            while(var10.hasMoreTokens()) {
               String var9 = var10.nextToken();
               if (!this.accessRMap.containsKey(var9)) {
                  System.out.println("ERROR: Undefined RemoteAccessPoint " + var9 + " in Import RemoteAccessPointList definition!");
                  this.currSection = var4;
                  throw new Exception("Undefined Import RemoteAccessPoint");
               }
            }
         }

         var5.setRemoteAccessPointList(var8);
      } catch (MBeanCreationException var11) {
         System.out.println("MBeanCreationException ERROR: " + var11.toString());
         this.currSection = var4;
         throw new Exception(var11.toString());
      } catch (InvalidAttributeValueException var12) {
         System.out.println("InvalidattributeException ERROR: " + var12.toString());
         this.currSection = var4;
         throw new Exception(var12.toString());
      } catch (InstanceNotFoundException var13) {
         System.out.println("InstanceNotFoundException ERROR: " + var13.toString());
         this.currSection = var4;
         throw new Exception(var13.toString());
      } catch (Exception var14) {
         System.out.println("ERROR: Could not create WTCImport , reason(" + var14.toString() + ")!");
         this.currSection = var4;
         throw var14;
      }

      this.currSection = var4;
      System.out.println("Processing IMPMBEAN : " + var3 + " done.");
      return true;
   }

   private boolean setupPasswd(Element var1, int var2) throws Exception {
      String var3 = "pwd" + var2;
      System.out.println("Processing PASSWDMBEAN : " + var3 + " started.");
      String var4 = this.currSection;
      this.currSection = "T_DM_PASSWORD";

      try {
         WTCPasswordMBean var5 = (WTCPasswordMBean)this.configHome.createAdminMBean(var3, "WTCPassword", this.domainName, this.wtcsmb);
         String var6 = this.getEAVal(var1, "LocalAccessPoint");
         if (!this.accessLMap.containsKey(var6)) {
            System.out.println("ERROR: Undefined LocalAccessPoint " + var6 + " found in Export definition!");
            this.currSection = var4;
            throw new Exception("Undefined Export LocalAccessPoint");
         }

         var5.setLocalAccessPoint(var6);
         String var7 = this.getEAVal(var1, "RemoteAccessPoint");
         if (!this.accessRMap.containsKey(var7)) {
            System.out.println("ERROR: Undefined RemoteAccessPoint " + var7 + " found in Password definition!");
            this.currSection = var4;
            throw new Exception("Undefined Password RemoteAccessPoint");
         }

         var5.setRemoteAccessPoint(var7);
         var5.setLocalPassword(this.getSubElemText(var1, "LocalPassword", true));
         var5.setLocalPasswordIV(this.getSubEAVal(var1, "LocalPassword", "IV"));
         var5.setRemotePassword(this.getSubElemText(var1, "RemotePassword", true));
         var5.setRemotePasswordIV(this.getSubEAVal(var1, "RemotePassword", "IV"));
      } catch (MBeanCreationException var8) {
         System.out.println("MBeanCreationException ERROR: " + var8.toString());
         this.currSection = var4;
         throw new Exception(var8.toString());
      } catch (InvalidAttributeValueException var9) {
         System.out.println("InvalidattributeException ERROR: " + var9.toString());
         this.currSection = var4;
         throw new Exception(var9.toString());
      } catch (InstanceNotFoundException var10) {
         System.out.println("InstanceNotFoundException ERROR: " + var10.toString());
         this.currSection = var4;
         throw new Exception(var10.toString());
      } catch (Exception var11) {
         System.out.println("ERROR: Could not create WTCPassword , reason(" + var11.toString() + ")!");
         this.currSection = var4;
         throw var11;
      }

      this.currSection = var4;
      System.out.println("Processing PASSWDMBEAN : " + var3 + " done.");
      return true;
   }

   private boolean setupResources(Element var1) throws Exception {
      System.out.println("Processing RESMBEAN started");
      String var2 = this.currSection;
      this.currSection = "T_DM_RESOURCES";

      WTCResourcesMBean var7;
      try {
         var7 = (WTCResourcesMBean)this.configHome.createAdminMBean("res1", "WTCResources", this.domainName, this.wtcsmb);
      } catch (MBeanCreationException var22) {
         System.out.println("MBeanCreationException ERROR: " + var22.toString());
         this.currSection = var2;
         throw new Exception(var22.toString());
      }

      for(Node var3 = var1.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         if (var3.getNodeType() == 1 && var3 instanceof Element) {
            Element var5 = (Element)var3;
            String var6 = var5.getTagName();
            boolean var8 = false;
            boolean var9 = false;
            Node var4;
            Node var10;
            Element var11;
            String var12;
            String var13;
            String var14;
            int var23;
            int var24;
            if (var6.equals("FieldTables")) {
               var23 = 0;
               var24 = 0;

               for(var10 = var5.getFirstChild(); var10 != null; var10 = var10.getNextSibling()) {
                  if (var10.getNodeType() == 1 && var10 instanceof Element) {
                     var11 = (Element)var10;
                     var12 = var11.getTagName();
                     if (var12.equals("FldTblClass")) {
                        var13 = this.getEAVal(var11, "Type");
                        if (var13 == null) {
                           System.out.println("ERROR: Type for the Field Table is not specified!");
                           this.currSection = var2;
                           return false;
                        }

                        if (var13.equals("fml32")) {
                           ++var24;
                        } else if (var13.equals("fml16")) {
                           ++var23;
                        }
                     }
                  }
               }

               if (var23 != 0) {
                  myFldTbls16 = new String[var23];
               }

               if (var24 != 0) {
                  myFldTbls32 = new String[var24];
               }

               var23 = 0;
               var24 = 0;

               for(var10 = var5.getFirstChild(); var10 != null; var10 = var10.getNextSibling()) {
                  if (var10.getNodeType() == 1 && var10 instanceof Element) {
                     var11 = (Element)var10;
                     var12 = var11.getTagName();
                     if (var12.equals("FldTblClass")) {
                        var4 = var11.getFirstChild();
                        if (var4 == null) {
                           System.out.println("ERROR: Can not get TNODE!");
                           this.currSection = var2;
                           return false;
                        }

                        var14 = var4.getNodeValue();
                        if (var14 == null) {
                           System.out.println("ERROR: Can not get FldTbl NODE value!");
                           this.currSection = var2;
                           return false;
                        }

                        try {
                           var13 = this.getEAVal(var11, "Type");
                           if (var13.equals("fml32")) {
                              myFldTbls32[var24] = var14.trim();
                              ++var24;
                           } else if (var13.equals("fml16")) {
                              myFldTbls16[var23] = var14.trim();
                              ++var23;
                           } else {
                              System.out.println("WARNING: Unknown Type label for FldTblClass: " + var13);
                           }
                        } catch (Exception var17) {
                           System.out.println("ERROR: Can not get Resources FldTbl Typeobject, reason(" + var17.toString() + ")!");
                           this.currSection = var2;
                           throw var17;
                        }
                     }
                  }
               }

               try {
                  if (var23 != 0) {
                     var7.setFldTbl16Classes(myFldTbls16);
                  }

                  if (var24 != 0) {
                     var7.setFldTbl32Classes(myFldTbls32);
                  }
               } catch (InvalidAttributeValueException var21) {
                  System.out.println("InvalidattributeException ERROR: " + var21.toString());
                  this.currSection = var2;
                  throw new Exception(var21.toString());
               }
            } else if (!var6.equals("ViewTables")) {
               if (var6.equals("AppPassword")) {
                  try {
                     var4 = var5.getFirstChild();
                     if (var4 == null) {
                        System.out.println("ERROR: No TNODE for AppPassword was found!");
                        this.currSection = var2;
                        return false;
                     }

                     String var15 = var4.getNodeValue();
                     if (var15 == null) {
                        System.out.println("ERROR: Failed to get AppPassword text!");
                        this.currSection = var2;
                        return false;
                     }

                     var7.setAppPassword(var15.trim());
                     var7.setAppPasswordIV(this.getEAVal(var5, "IV"));
                  } catch (InvalidAttributeValueException var18) {
                     System.out.println("InvalidattributeException ERROR: " + var18.toString());
                     this.currSection = var2;
                     throw new Exception(var18.toString());
                  } catch (Exception var19) {
                     System.out.println("ERROR: Couldn't handle Resources App Passwd/IV info, reason(" + var19.toString() + ")!");
                     this.currSection = var2;
                     throw var19;
                  }
               }
            } else {
               var23 = 0;
               var24 = 0;

               for(var10 = var5.getFirstChild(); var10 != null; var10 = var10.getNextSibling()) {
                  if (var10.getNodeType() == 1 && var10 instanceof Element) {
                     var11 = (Element)var10;
                     var12 = var11.getTagName();
                     if (var12.equals("ViewTblClass")) {
                        var13 = this.getEAVal(var11, "Type");
                        if (var13 == null) {
                           System.out.println("ERROR: Type for the Field Table is not  specified!");
                           this.currSection = var2;
                           return false;
                        }

                        if (var13.equals("view32")) {
                           ++var24;
                        } else if (var13.equals("view16")) {
                           ++var23;
                        }
                     }
                  }
               }

               if (var23 != 0) {
                  myViewTbls16 = new String[var23];
               }

               if (var24 != 0) {
                  myViewTbls32 = new String[var24];
               }

               var23 = 0;
               var24 = 0;

               for(var10 = var5.getFirstChild(); var10 != null; var10 = var10.getNextSibling()) {
                  if (var10.getNodeType() == 1 && var10 instanceof Element) {
                     var11 = (Element)var10;
                     var12 = var11.getTagName();
                     if (var12.equals("ViewTblClass")) {
                        var4 = var11.getFirstChild();
                        if (var4 == null) {
                           System.out.println("ERROR: Can not get TNODE!");
                           this.currSection = var2;
                           return false;
                        }

                        var14 = var4.getNodeValue();
                        if (var14 == null) {
                           System.out.println("ERROR: Can not get ViewTbl NODE value!");
                           this.currSection = var2;
                           return false;
                        }

                        try {
                           var13 = this.getEAVal(var11, "Type");
                           if (var13.equals("view32")) {
                              myViewTbls32[var24] = var14.trim();
                              ++var24;
                           } else if (var13.equals("view16")) {
                              myViewTbls16[var23] = var14.trim();
                              ++var23;
                           } else {
                              System.out.println("WARNING: Unknown Type label for ViewTblClass: " + var13);
                           }
                        } catch (Exception var16) {
                           System.out.println("ERROR: Getting Resources ViewTbl Type, reason(" + var16.toString() + ")!");
                           this.currSection = var2;
                           throw var16;
                        }
                     }
                  }
               }

               try {
                  if (var23 != 0) {
                     var7.setViewTbl16Classes(myViewTbls16);
                  }

                  if (var24 != 0) {
                     var7.setViewTbl32Classes(myViewTbls32);
                  }
               } catch (InvalidAttributeValueException var20) {
                  System.out.println("InvalidattributeException ERROR: " + var20.toString());
                  this.currSection = var2;
                  throw new Exception(var20.toString());
               }
            }
         }
      }

      this.currSection = var2;
      System.out.println("Processing RESMBEAN done");
      return true;
   }

   private boolean setupRedirect(Element var1, int var2) throws Exception {
      String var3 = "redir" + var2;
      System.out.println("Processing REDIRECTMBEAN : " + var3 + " started.");
      String var4 = this.currSection;
      this.currSection = "fromto";

      try {
         WTCtBridgeRedirectMBean var5 = (WTCtBridgeRedirectMBean)this.configHome.createAdminMBean(var3, "WTCtBridgeRedirect", this.domainName, this.wtcsmb);
         var5.setDirection(this.getSubElemText(var1, "direction", true));
         String var6;
         if ((var6 = this.getSubElemText(var1, "translateFML", false)) != null) {
            if (var6.equalsIgnoreCase("NO")) {
               var6 = "NO";
            }

            var5.setTranslateFML(var6);
         }

         if ((var6 = this.getSubElemText(var1, "replyQ", false)) != null) {
            var5.setReplyQ(var6);
         }

         if ((var6 = this.getSubElemText(var1, "metadataFile", false)) != null) {
            var5.setMetaDataFile(var6);
         }

         Element var7 = this.getSubElem(var1, "source", true);
         var5.setSourceName(this.getSubElemText(var7, "Name", true));
         if ((var6 = this.getSubElemText(var7, "AccessPoint", false)) != null) {
            var5.setSourceAccessPoint(var6);
         }

         if ((var6 = this.getSubElemText(var7, "Qspace", false)) != null) {
            var5.setSourceQspace(var6);
         }

         var7 = this.getSubElem(var1, "target", true);
         var5.setTargetName(this.getSubElemText(var7, "Name", true));
         if ((var6 = this.getSubElemText(var7, "AccessPoint", false)) != null) {
            var5.setTargetAccessPoint(var6);
         }

         if ((var6 = this.getSubElemText(var7, "Qspace", false)) != null) {
            var5.setTargetQspace(var6);
         }
      } catch (MBeanCreationException var8) {
         System.out.println("MBeanCreationException ERROR: " + var8.toString());
         this.currSection = var4;
         throw new Exception(var8.toString());
      } catch (InvalidAttributeValueException var9) {
         System.out.println("InvalidattributeException ERROR: " + var9.toString());
         this.currSection = var4;
         throw new Exception(var9.toString());
      } catch (InstanceNotFoundException var10) {
         System.out.println("InstanceNotFoundException ERROR: " + var10.toString());
         this.currSection = var4;
         throw new Exception(var10.toString());
      } catch (Exception var11) {
         System.out.println("ERROR: Could not create WTCtBridgeRedirect , reason(" + var11.toString() + ")!");
         this.currSection = var4;
         throw var11;
      }

      this.currSection = var4;
      System.out.println("Processing REDIRECTMBEAN : " + var3 + " done.");
      return true;
   }

   private boolean setupTBGlobal(Element var1) throws Exception {
      System.out.println("Processing TBGLOBALMBEAN started.");
      String var2 = this.currSection;
      this.currSection = "tBridge";

      try {
         WTCtBridgeGlobalMBean var3 = (WTCtBridgeGlobalMBean)this.configHome.createAdminMBean("tbgbl1", "WTCtBridgeGlobal", this.domainName, this.wtcsmb);
         String var4;
         if ((var4 = this.getSubElemText(var1, "transactional", false)) != null) {
            var3.setTransactional(var4);
         }

         if ((var4 = this.getSubElemText(var1, "timeout", false)) != null) {
            var3.setTimeout(Integer.parseInt(var4, 10));
         }

         if ((var4 = this.getSubElemText(var1, "retries", false)) != null) {
            var3.setRetries(Integer.parseInt(var4, 10));
         }

         if ((var4 = this.getSubElemText(var1, "retryDelay", false)) != null) {
            var3.setRetryDelay(Integer.parseInt(var4, 10));
         }

         if ((var4 = this.getSubElemText(var1, "wlsErrorDestination", false)) != null) {
            var3.setWlsErrorDestination(var4);
         }

         if ((var4 = this.getSubElemText(var1, "tuxErrorQueue", false)) != null) {
            var3.setTuxErrorQueue(var4);
         }

         if ((var4 = this.getSubElemText(var1, "deliveryModeOverride", false)) != null) {
            var3.setDeliveryModeOverride(var4);
         }

         if ((var4 = this.getSubElemText(var1, "defaultreplyDeliveryMode", false)) != null) {
            var3.setDefaultReplyDeliveryMode(var4);
         }

         if ((var4 = this.getSubElemText(var1, "userID", false)) != null) {
            var3.setUserId(var4);
         }

         if ((var4 = this.getSubElemText(var1, "allowNonStandardTypes", false)) != null) {
            var3.setAllowNonStandardTypes(var4);
         }

         var3.setJndiFactory(this.getSubElemText(var1, "jndiFactory", true));
         var3.setJmsFactory(this.getSubElemText(var1, "jmsFactory", true));
         var3.setTuxFactory(this.getSubElemText(var1, "tuxFactory", true));
         String var5;
         if ((var5 = this.getPmaps(var1, "TuxtoJms")) != null) {
            var3.setTuxToJmsPriorityMap(var5);
         }

         if ((var5 = this.getPmaps(var1, "JmstoTux")) != null) {
            var3.setJmsToTuxPriorityMap(var5);
         }
      } catch (MBeanCreationException var6) {
         System.out.println("MBeanCreationException ERROR: " + var6.toString());
         this.currSection = var2;
         throw new Exception(var6.toString());
      } catch (InvalidAttributeValueException var7) {
         System.out.println("InvalidattributeException ERROR: " + var7.toString());
         this.currSection = var2;
         throw new Exception(var7.toString());
      } catch (InstanceNotFoundException var8) {
         System.out.println("InstanceNotFoundException ERROR: " + var8.toString());
         this.currSection = var2;
         throw new Exception(var8.toString());
      } catch (Exception var9) {
         System.out.println("ERROR: Could not create WTCtBridgeGlobal , reason(" + var9.toString() + ")!");
         this.currSection = var2;
         throw var9;
      }

      this.currSection = var2;
      System.out.println("Processing TBGLOBALMBEAN done.");
      return true;
   }

   private String getPmaps(Element var1, String var2) throws Exception {
      if (DebugOn) {
         System.out.println("getPmaps entry: " + var2);
      }

      if (var1 == null) {
         System.out.println("ERROR: The tBridge input was not defined.");
         throw new Exception("Element should always be defined");
      } else {
         Element var3 = this.getSubElem(var1, "priorityMapping", false);
         if (var3 == null) {
            return null;
         } else {
            Element var4 = this.getSubElem(var3, var2, true);
            String var10 = null;

            for(Node var5 = var4.getFirstChild(); var5 != null; var5 = var5.getNextSibling()) {
               if (var5.getNodeType() == 1 && var5 instanceof Element) {
                  Element var6 = (Element)var5;
                  String var7 = var6.getTagName();
                  if (var7.equals("pMap")) {
                     String var8 = this.getSubElemText(var6, "value", true);
                     String var9 = this.getSubElemText(var6, "range", true);
                     if (var10 == null) {
                        var10 = var8 + ":" + var9;
                     } else {
                        var10 = var10 + "|" + var8 + ":" + var9;
                     }
                  }
               }
            }

            if (DebugOn) {
               System.out.println("getPmaps returns: " + var10);
            }

            return var10;
         }
      }
   }

   private String getEAVal(Element var1, String var2) throws Exception {
      if (DebugOn) {
         System.out.println("getEAVal entry: " + var2);
      }

      String var3 = var1.getAttribute(var2);
      if (var3 == null) {
         System.out.println("ERROR: The element " + var1.getTagName() + " does not have an attribute " + var2 + " defined!");
         throw new Exception("Element missing attribute");
      } else {
         if (DebugOn) {
            System.out.println("getEAVal returns: " + var3);
         }

         return var3;
      }
   }

   private String getSubElemText(Element var1, String var2, boolean var3) throws Exception {
      if (DebugOn) {
         System.out.println("getSubElemText entry: " + var2);
      }

      NodeList var4 = var1.getElementsByTagName(var2);
      if (var4.getLength() != 1) {
         if (var3) {
            System.out.println("ERROR: The parent element " + var1.getTagName() + " does not have one(and only one) sub element of the tag name " + var2 + "!");
            throw new Exception("Count error on subelements");
         } else {
            return null;
         }
      } else {
         Node var5 = var4.item(0);
         Node var6 = var5.getFirstChild();
         if (var6 == null) {
            if (var3) {
               System.out.println("ERROR: The parent element " + var1.getTagName() + " has zero sub element of the tag name " + var2 + "!");
               throw new Exception("Missing subelement");
            } else {
               return null;
            }
         } else {
            String var7 = var6.getNodeValue();
            if (var7 == null) {
               if (var3) {
                  System.out.println("ERROR: The parent element " + var1.getTagName() + " has the sub element of the tag name " + var2 + " without value!");
                  throw new Exception("Missing subelement value");
               } else {
                  return null;
               }
            } else {
               if (DebugOn) {
                  System.out.println("getSubElemText returns: " + var7);
               }

               return var7.trim();
            }
         }
      }
   }

   private String getSubEAVal(Element var1, String var2, String var3) throws Exception {
      if (DebugOn) {
         System.out.println("getSubEAVal entry: " + var2 + " " + var3);
      }

      NodeList var4 = var1.getElementsByTagName(var2);
      if (var4.getLength() != 1) {
         System.out.println("ERROR: The parent element " + var1.getTagName() + " does not have one(and only one) sub element of the tag name " + var2 + "!");
         throw new Exception("Missing subelement");
      } else {
         Element var5 = (Element)var4.item(0);
         String var6 = var5.getAttribute(var3);
         if (var6 == null) {
            System.out.println("ERROR: The element " + var1.getTagName() + " does not have an attribute " + var2 + " defined!");
            throw new Exception("Missing subelement value");
         } else {
            if (DebugOn) {
               System.out.println("getSubEAVal returns: " + var6);
            }

            return var6;
         }
      }
   }

   private Element getSubElem(Element var1, String var2, boolean var3) throws Exception {
      if (DebugOn) {
         System.out.println("getSubElem entry: " + var2);
      }

      NodeList var4 = var1.getElementsByTagName(var2);
      if (var4.getLength() != 1) {
         if (var3) {
            System.out.println("ERROR: The parent element " + var1.getTagName() + " does not have one(and only one) sub element of the tag name " + var2 + "!");
            throw new Exception("Missing subelement");
         } else {
            return null;
         }
      } else {
         if (DebugOn) {
            System.out.println("getSubElem returns: ");
         }

         return (Element)var4.item(0);
      }
   }
}
