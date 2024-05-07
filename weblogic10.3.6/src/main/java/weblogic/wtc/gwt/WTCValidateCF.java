package weblogic.wtc.gwt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Timer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.apache.xerces.parsers.DOMParser;
import weblogic.wtc.jatmi.FldTbl;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TuxXidRply;
import weblogic.wtc.jatmi.ViewHelper;

public final class WTCValidateCF {
   private Hashtable myImportedServices = new Hashtable();
   private Hashtable myExportedServices = new Hashtable();
   private FldTbl[] myFldTbls16;
   private FldTbl[] myFldTbls32;
   private TDMLocalTDomain[] ltd_list;
   private TDMRemoteTDomain[] rtd_list;
   private TDMPasswd[] pwd_list;
   private String myAppPwIV;
   private String myAppPwPWD;
   private int ltdcnt = 0;
   private int rtdcnt = 0;
   private int pwdcnt = 0;
   private File c_fname = null;
   private HashMap accessMap = new HashMap();
   private String currSection = null;
   private static final String localDomainSection = "T_DM_LOCAL_TDOMAIN";
   private static final String remoteDomainSection = "T_DM_REMOTE_TDOMAIN";
   private static final String exportSection = "T_DM_EXPORT";
   private static final String importSection = "T_DM_IMPORT";
   private static final String passwdSection = "T_DM_PASSWORD";
   private static final String resourceSection = "T_DM_RESOURCES";

   public static void main(String[] var0) {
      if (var0.length != 1) {
         System.out.println("Usage: WTCValidateCF <BDMCONFIG>");
      } else {
         String var1 = var0[0];
         WTCValidateCF var2 = new WTCValidateCF();
         var2.checkSyntax(var1);
      }
   }

   WTCValidateCF() {
   }

   void checkSyntax(String var1) {
      try {
         this.c_fname = new File(var1);
         System.out.println("Validating Config file " + this.c_fname.getPath() + "...");
         if (!this.loadFile(this.c_fname)) {
            System.out.println("ERROR: Validation failed for configuration XML file!");
            return;
         }

         System.out.println("The XML configuration file validation is done!");
         System.out.println("No error found!!!");
      } catch (NullPointerException var3) {
         System.out.println("ERROR: " + var3.toString());
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
      } catch (TPException var9) {
         System.out.println("ERROR: TP Exception, reason(" + var9.getMessage() + ")!");
         if (this.currSection != null) {
            System.out.println("INFO: error occurred in " + this.currSection + " element.");
         }

         return false;
      } catch (NumberFormatException var10) {
         System.out.println("ERROR: Invalid number format(" + var10.toString() + ")!");
         if (this.currSection != null) {
            System.out.println("INFO: error occurred in " + this.currSection + " element.");
         }

         return false;
      } catch (Exception var11) {
         System.out.println("ERROR: Exception, reason(" + var11.toString() + ")!");
         if (this.currSection != null) {
            System.out.println("INFO: error occurred in " + this.currSection + " element.");
         }

         return false;
      }
   }

   private boolean extractInfo(Document var1) throws TPException {
      Element var2 = var1.getDocumentElement();
      if (var2 == null) {
         System.out.println("ERROR: No top element from file " + this.c_fname.getPath());
         return false;
      } else {
         String var3 = var2.getTagName();
         if (var3 != null && var3.equals("WTC_CONFIG")) {
            Element var6 = null;

            Node var4;
            Element var5;
            String var7;
            for(var4 = var2.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
               if (var4.getNodeType() == 1 && var4 instanceof Element) {
                  var5 = (Element)var4;
                  var7 = var5.getTagName();
                  if (var7.equals("tBridge")) {
                     System.out.println("INFO: tBridge is configured.");
                     break;
                  }
               }
            }

            for(var4 = var2.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
               if (var4.getNodeType() == 1 && var4 instanceof Element) {
                  var5 = (Element)var4;
                  var7 = var5.getTagName();
                  if (var7.equals("BDMCONFIG")) {
                     var6 = var5;
                     break;
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
                     var7 = var5.getTagName();
                     if (var7.equals("T_DM_LOCAL_TDOMAIN")) {
                        ++this.ltdcnt;
                     } else if (var7.equals("T_DM_REMOTE_TDOMAIN")) {
                        ++this.rtdcnt;
                     } else if (var7.equals("T_DM_PASSWORD")) {
                        ++this.pwdcnt;
                     }
                  }
               }

               if (this.ltdcnt == 0) {
                  System.out.println("ERROR: Requires at least one local domain defined.");
                  return false;
               } else {
                  this.ltd_list = new TDMLocalTDomain[this.ltdcnt];
                  if (this.rtdcnt != 0) {
                     this.rtd_list = new TDMRemoteTDomain[this.rtdcnt];
                  }

                  if (this.pwdcnt != 0) {
                     this.pwd_list = new TDMPasswd[this.pwdcnt];
                  }

                  boolean var8 = true;
                  int var9 = 0;
                  int var10 = 0;
                  int var11 = 0;

                  for(var4 = var6.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
                     if (var4.getNodeType() == 1 && var4 instanceof Element) {
                        var5 = (Element)var4;
                        var7 = var5.getTagName();
                        if (var7.equals("T_DM_LOCAL_TDOMAIN")) {
                           var8 = this.setupTDMLocalTD(var5, var10);
                           if (!var8) {
                              System.out.println("ERROR: Could not complete processing of element " + var7);
                              return false;
                           }

                           ++var10;
                        }
                     }
                  }

                  for(var4 = var6.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
                     if (var4.getNodeType() == 1 && var4 instanceof Element) {
                        var5 = (Element)var4;
                        var7 = var5.getTagName();
                        if (var7.equals("T_DM_REMOTE_TDOMAIN")) {
                           var8 = this.setupTDMRemoteTD(var5, var9);
                           if (var8) {
                              ++var9;
                           }
                        } else if (var7.equals("T_DM_EXPORT")) {
                           var8 = this.setupTDMExport(var5);
                        } else if (var7.equals("T_DM_IMPORT")) {
                           var8 = this.setupTDMImport(var5);
                        } else if (var7.equals("T_DM_PASSWORD")) {
                           var8 = this.setupTDMPasswd(var5, var11);
                           if (var8) {
                              ++var11;
                           }
                        } else if (var7.equals("T_DM_RESOURCES")) {
                           var8 = this.setupTDMResources(var5);
                        }

                        if (!var8) {
                           System.out.println("ERROR: Could not complete processing of element " + var7);
                           return false;
                        }
                     }
                  }

                  if (!this.crossChecking()) {
                     System.out.println("ERROR: Second phase processing failed.");
                     return false;
                  } else {
                     return true;
                  }
               }
            }
         } else {
            System.out.println("ERROR: Bad top element name \"" + var3 + "\" from config file " + this.c_fname.getPath());
            return false;
         }
      }
   }

   private boolean setupTDMLocalTD(Element var1, int var2) throws TPException {
      String var4 = this.currSection;
      this.currSection = "T_DM_LOCAL_TDOMAIN";
      String var5 = this.getEAVal(var1, "AccessPoint");

      TDMLocalTDomain var3;
      try {
         var3 = new TDMLocalTDomain(var5);
         if (this.accessMap.put(var5, var3) != null) {
            System.out.println("ERROR: Duplicated AccessPoint " + var5 + " found in Local TDomain definition!");
            this.currSection = var4;
            return false;
         }
      } catch (Exception var7) {
         System.out.println("ERROR: Could not create TDMLocalTDomain " + var5 + ", reason(" + var7.toString() + ")!");
         this.currSection = var4;
         return false;
      }

      var3.setWlsClusterName(this.getSubElemText(var1, "WlsClusterName", true));
      var3.setAccessPointId(this.getSubElemText(var1, "AccessPointId", true));
      var3.setNWAddr(this.getSubElemText(var1, "NWAddr", true));
      var3.setType(this.getSubElemText(var1, "Type", false));
      var3.setSecurity(this.getSubElemText(var1, "Security", false));
      var3.setConnectionPolicy(this.getSubElemText(var1, "ConnectionPolicy", false));
      var3.setInteroperate(this.getSubElemText(var1, "Interoperate", false));
      this.getSubElemText(var1, "RetryInterval", false);
      String var6;
      if ((var6 = this.getSubElemText(var1, "RetryInterval", false)) != null) {
         var3.setRetryInterval(Long.parseLong(var6, 10));
      }

      if ((var6 = this.getSubElemText(var1, "MaxRetries", false)) != null) {
         var3.setMaxRetries(Long.parseLong(var6, 10));
      }

      if ((var6 = this.getSubElemText(var1, "BlockTime", false)) != null) {
         var3.setBlockTime(Long.parseLong(var6) * 1000L);
      }

      if ((var6 = this.getSubElemText(var1, "CmpLimit", false)) != null) {
         var3.setCmpLimit(Integer.parseInt(var6, 10));
      }

      if ((var6 = this.getSubElemText(var1, "MinEncryptBits", false)) != null) {
         var3.setMinEncryptBits(Integer.parseInt(var6, 10));
      }

      if ((var6 = this.getSubElemText(var1, "MaxEncryptBits", false)) != null) {
         var3.setMaxEncryptBits(Integer.parseInt(var6, 10));
      }

      this.ltd_list[var2] = var3;
      this.currSection = var4;
      return true;
   }

   private boolean setupTDMRemoteTD(Element var1, int var2) throws TPException {
      String var4 = this.currSection;
      this.currSection = "T_DM_REMOTE_TDOMAIN";
      String var5 = this.getEAVal(var1, "AccessPoint");

      TDMRemoteTDomain var3;
      try {
         var3 = new TDMRemoteTDomain(var5, (TuxXidRply)null, (Timer)null);
         if (this.accessMap.put(var5, var3) != null) {
            System.out.println("ERROR: Duplicated AccessPoint " + var5 + " found in Remote TDomain definition!");
            this.currSection = var4;
            return false;
         }
      } catch (Exception var9) {
         System.out.println("ERROR: Could not create TDMRemoteTDomain " + var5 + ", reason(" + var9.toString() + ")!");
         this.currSection = var4;
         return false;
      }

      String var7 = this.getSubElemText(var1, "LocalAccessPoint", true);
      int var8 = this.getLTDindex(var7);
      if (var8 == -1) {
         if (var7 != null) {
            System.out.println("ERROR: The local TDomain " + var7 + " for remote TDomain " + var3.getAccessPoint() + " can not be found!");
         } else {
            System.out.println("ERROR: The local TDomain is not specified for remote TDomain " + var3.getAccessPoint() + "!");
         }

         this.currSection = var4;
         return false;
      } else {
         var3.setLocalAccessPoint(var7);
         var3.setAccessPointId(this.getSubElemText(var1, "AccessPointId", true));
         var3.setNWAddr(this.getSubElemText(var1, "NWAddr", true));
         var3.setType(this.getSubElemText(var1, "Type", false));
         var3.setAclPolicy(this.getSubElemText(var1, "AclPolicy", false));
         var3.setCredentialPolicy(this.getSubElemText(var1, "CredentialPolicy", false));
         var3.setTpUsrFile(this.getSubElemText(var1, "TpUsrFile", false));
         String var6;
         if ((var6 = this.getSubElemText(var1, "CmpLimit", false)) != null) {
            var3.setCmpLimit(Integer.parseInt(var6, 10));
         }

         if ((var6 = this.getSubElemText(var1, "MinEncryptBits", false)) != null) {
            var3.setMinEncryptBits(Integer.parseInt(var6, 10));
         }

         if ((var6 = this.getSubElemText(var1, "MaxEncryptBits", false)) != null) {
            var3.setMaxEncryptBits(Integer.parseInt(var6, 10));
         }

         var6 = this.getSubElemText(var1, "ConnectionPolicy", false);
         if (var6 != null && !var6.equals("LOCAL")) {
            var3.setConnPolicyConfigState(1);
         } else {
            if (var6 == null) {
               var3.setConnPolicyConfigState(2);
            } else {
               var3.setConnPolicyConfigState(3);
            }

            var6 = this.ltd_list[var8].getConnectionPolicy();
         }

         var3.setConnectionPolicy(var6);
         if ((var6 = this.getSubElemText(var1, "RetryInterval", false)) != null) {
            var3.setRetryInterval(Long.parseLong(var6, 10));
         } else {
            var3.setRetryInterval(this.ltd_list[var8].getRetryInterval());
         }

         if ((var6 = this.getSubElemText(var1, "MaxRetries", false)) != null) {
            var3.setMaxRetries(Long.parseLong(var6, 10));
         } else {
            var3.setMaxRetries(this.ltd_list[var8].getMaxRetries());
         }

         this.rtd_list[var2] = var3;
         this.currSection = var4;
         return true;
      }
   }

   private boolean setupTDMExport(Element var1) {
      String var2 = this.currSection;
      this.currSection = "T_DM_EXPORT";
      String var3 = this.getEAVal(var1, "LocalAccessPoint");
      int var4 = this.getLTDindex(var3);
      if (var4 == -1) {
         if (var3 != null) {
            System.out.println("ERROR: The Local TDomain " + var3 + " for exported service " + this.getEAVal(var1, "ResourceName") + " can not be found!");
         } else {
            System.out.println("ERROR: The Local TDomain is not specified for exported service " + this.getEAVal(var1, "ResourceName") + "!");
         }

         this.currSection = var2;
         return false;
      } else {
         TDMLocalTDomain var5 = this.ltd_list[var4];

         TDMExport var6;
         try {
            var6 = new TDMExport(this.getEAVal(var1, "ResourceName"), var5);
         } catch (Exception var8) {
            System.out.println("ERROR: Could not create TDMExport for exported service " + this.getEAVal(var1, "ResourceName"));
            this.currSection = var2;
            return false;
         }

         var6.setRemoteName(this.getSubElemText(var1, "RemoteName", false));
         var6.setEJBName(this.getSubElemText(var1, "EJBName", false));
         this.currSection = var2;
         return true;
      }
   }

   private boolean setupTDMImport(Element var1) throws TPException {
      String var2 = this.currSection;
      this.currSection = "T_DM_IMPORT";
      String var3 = this.getEAVal(var1, "LocalAccessPoint");
      int var4 = this.getLTDindex(var3);
      if (var4 == -1) {
         if (var3 != null) {
            System.out.println("ERROR: The local TDomain " + var3 + " for imported service " + this.getEAVal(var1, "ResourceName") + " can not be found!");
         } else {
            System.out.println("ERROR: The local TDomain is not specified for imported service " + this.getEAVal(var1, "ResourceName") + "!");
         }

         this.currSection = var2;
         return false;
      } else {
         TDMLocalTDomain var5 = this.ltd_list[var4];
         String var8 = this.getEAVal(var1, "RemoteAccessPointList");
         TDMRemoteTDomain var6;
         TDMRemoteTDomain[] var7;
         int var9;
         if (var8.indexOf(44) == -1) {
            var9 = this.getRTDindex(var8);
            if (var9 == -1) {
               System.out.println("ERROR: The remote TDomain list " + var8 + " is not defined for imported service " + this.getEAVal(var1, "ResourceName") + "!");
               this.currSection = var2;
               return false;
            }

            var6 = this.rtd_list[var9];
            var6.setLocalAccessPoint(var3);
            var7 = new TDMRemoteTDomain[]{var6};
         } else {
            StringTokenizer var10 = new StringTokenizer(var8, ",");
            var7 = new TDMRemoteTDomain[var10.countTokens()];

            for(int var11 = 0; var10.hasMoreTokens(); ++var11) {
               String var12 = var10.nextToken();
               var9 = this.getRTDindex(var12);
               if (var9 == -1) {
                  System.out.println("ERROR: The remote TDomain " + var12 + " is not defined for imported service " + this.getEAVal(var1, "ResourceName") + "!");
                  this.currSection = var2;
                  return false;
               }

               var6 = this.rtd_list[var9];
               var6.setLocalAccessPoint(var3);
               var7[var11] = var6;
            }
         }

         TDMImport var14;
         try {
            var14 = new TDMImport(this.getEAVal(var1, "ResourceName"), var5, var7);
         } catch (Exception var13) {
            System.out.println("ERROR: Could not create TDMImport for imported service " + this.getEAVal(var1, "ResourceName"));
            return false;
         }

         var14.setRemoteName(this.getSubElemText(var1, "RemoteName", false));
         String var15 = this.getSubElemText(var1, "TranTime", false);
         if (var15 != null) {
            var14.setTranTime(Integer.parseInt(var15, 10));
         }

         this.currSection = var2;
         return true;
      }
   }

   private boolean setupTDMPasswd(Element var1, int var2) {
      String var3 = this.currSection;
      this.currSection = "T_DM_PASSWORD";
      String var4 = this.getEAVal(var1, "LocalAccessPoint");
      int var5 = this.getLTDindex(var4);
      if (var5 == -1) {
         if (var4 != null) {
            System.out.println("ERROR: The local TDomain " + var4 + " for password " + this.getEAVal(var1, "LocalAccessPoint") + " can not be found!");
         } else {
            System.out.println("ERROR: The local TDomain is not specified for password ");
         }

         this.currSection = var3;
         return false;
      } else {
         TDMLocalTDomain var10000 = this.ltd_list[var5];
         String var7 = this.getEAVal(var1, "RemoteAccessPoint");
         int var8 = this.getRTDindex(var7);
         if (var8 == -1) {
            if (var7 != null) {
               System.out.println("ERROR: Can not continue processing the TDMPassword object because the RemoteDomain " + var7 + " is not valid!");
            } else {
               System.out.println("ERROR: Can not continue processing the TDMPassword object because the RemoteDomain  is not specified!");
            }

            this.currSection = var3;
            return false;
         } else {
            TDMRemoteTDomain var13 = this.rtd_list[var8];

            TDMPasswd var10;
            try {
               var10 = new TDMPasswd(var4, var7);
            } catch (Exception var12) {
               System.out.println("ERROR: Could not construct TDMPasswd object for local domain " + var4 + " and remote domain " + var7 + " (" + var12.toString() + ")!");
               this.currSection = var3;
               return false;
            }

            var10.setLocalPasswordIV(this.getSubEAVal(var1, "LocalPassword", "IV"));
            var10.setLocalPassword(this.getSubElemText(var1, "LocalPassword", true));
            var10.setRemotePasswordIV(this.getSubEAVal(var1, "RemotePassword", "IV"));
            var10.setRemotePassword(this.getSubElemText(var1, "RemotePassword", true));
            this.pwd_list[var2] = var10;
            this.currSection = var3;
            return true;
         }
      }
   }

   private boolean setupTDMResources(Element var1) {
      String var2 = this.currSection;
      this.currSection = "T_DM_RESOURCES";
      String var10 = null;

      for(Node var3 = var1.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         if (var3.getNodeType() == 1 && var3 instanceof Element) {
            Element var6 = (Element)var3;
            String var7 = var6.getTagName();
            Node var4;
            Node var5;
            Element var8;
            String var9;
            if (!var7.equals("FieldTables")) {
               String var20;
               if (var7.equals("ViewTables")) {
                  var20 = null;
                  String var21 = null;
                  ViewHelper var22 = new ViewHelper();
                  ViewHelper var23 = ViewHelper.getInstance();

                  for(var4 = var6.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
                     if (var4.getNodeType() == 1 && var4 instanceof Element) {
                        var8 = (Element)var4;
                        var9 = var8.getTagName();
                        if (var9.equals("ViewTblClass")) {
                           var5 = var8.getFirstChild();
                           if (var5 == null) {
                              System.out.println("ERROR: Can not get ViewTbl Text NODE!");
                              this.currSection = var2;
                              return false;
                           }

                           var20 = var5.getNodeValue();
                           if (var20 == null) {
                              System.out.println("ERROR: Can not get ViewTbl NODE value!");
                              this.currSection = var2;
                              return false;
                           }

                           try {
                              var10 = this.getEAVal(var8, "Type");
                              if (!var10.equals("view32") && !var10.equals("view16")) {
                                 System.out.println("WARNING: Unknown Type label for ViewTblClass: " + var10);
                              }

                              var21 = var20.substring(var20.lastIndexOf(46) + 1);
                              var23.setViewClass(var21, var20.trim());
                           } catch (Exception var18) {
                              System.out.println("ERROR: Resources " + var10 + " Info " + ", reason(" + var18.toString() + ")!");
                              this.currSection = var2;
                              return false;
                           }
                        }
                     }
                  }
               } else if (var7.equals("AppPassword")) {
                  this.myAppPwIV = this.getEAVal(var6, "IV");
                  var5 = var6.getFirstChild();
                  if (var5 == null) {
                     System.out.println("ERROR: No TNODE for AppPassword was found!");
                     this.currSection = var2;
                     return false;
                  }

                  var20 = var5.getNodeValue();
                  if (var20 == null) {
                     System.out.println("ERROR: Failed to get AppPassword text!");
                     this.currSection = var2;
                     return false;
                  }

                  this.myAppPwPWD = var20.trim();
               }
            } else {
               int var11 = 0;
               int var12 = 0;

               for(var4 = var6.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
                  if (var4.getNodeType() == 1 && var4 instanceof Element) {
                     var8 = (Element)var4;
                     var9 = var8.getTagName();
                     if (var9.equals("FldTblClass")) {
                        var10 = this.getEAVal(var8, "Type");
                        if (var10 == null) {
                           System.out.println("ERROR: Type for the Field Table is not  specified!");
                           this.currSection = var2;
                           return false;
                        }

                        if (var10.equals("fml32")) {
                           ++var12;
                        } else {
                           ++var11;
                        }
                     }
                  }
               }

               if (var11 != 0) {
                  this.myFldTbls16 = new FldTbl[var11];
               }

               if (var12 != 0) {
                  this.myFldTbls32 = new FldTbl[var12];
               }

               int var13 = 0;
               int var14 = 0;

               for(var4 = var6.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
                  if (var4.getNodeType() == 1 && var4 instanceof Element) {
                     var8 = (Element)var4;
                     var9 = var8.getTagName();
                     if (var9.equals("FldTblClass")) {
                        var5 = var8.getFirstChild();
                        if (var5 == null) {
                           System.out.println("ERROR: Can not get TNODE!");
                           this.currSection = var2;
                           return false;
                        }

                        String var15 = var5.getNodeValue();
                        if (var15 == null) {
                           System.out.println("ERROR: Can not get TNODE value!");
                           this.currSection = var2;
                           return false;
                        }

                        try {
                           Class var16 = Class.forName(var15.trim());
                           var10 = this.getEAVal(var8, "Type");
                           if (var10.equals("fml32")) {
                              this.myFldTbls32[var14] = (FldTbl)var16.newInstance();
                              ++var14;
                           } else {
                              this.myFldTbls16[var13] = (FldTbl)var16.newInstance();
                              ++var13;
                           }
                        } catch (Exception var19) {
                           System.out.println("ERROR: Can not create TDMResources object, reason(" + var19.toString() + ")!");
                           this.currSection = var2;
                           return false;
                        }
                     }
                  }
               }
            }
         }
      }

      this.currSection = var2;
      return true;
   }

   private int getLTDindex(String var1) {
      for(int var3 = 0; var3 < this.ltdcnt; ++var3) {
         String var2 = this.ltd_list[var3].getAccessPoint();
         if (var2.equals(var1)) {
            return var3;
         }
      }

      return -1;
   }

   private int getRTDindex(String var1) {
      for(int var3 = 0; var3 < this.rtdcnt; ++var3) {
         String var2 = this.rtd_list[var3].getAccessPoint();
         if (var2.equals(var1)) {
            return var3;
         }
      }

      return -1;
   }

   private String getEAVal(Element var1, String var2) {
      String var3 = var1.getAttribute(var2);
      if (var3 == null) {
         System.out.println("ERROR: The element " + var1.getTagName() + " does not have an attribute " + var2 + " defined!");
         return null;
      } else {
         return var3;
      }
   }

   private String getSubElemText(Element var1, String var2, boolean var3) {
      NodeList var4 = var1.getElementsByTagName(var2);
      if (var4.getLength() != 1) {
         if (var3) {
            System.out.println("ERROR: The parent element " + var1.getTagName() + " has more than one sub element of the tag name " + var2 + "!");
         }

         return null;
      } else {
         Node var5 = var4.item(0);
         Node var6 = var5.getFirstChild();
         if (var6 == null) {
            if (var3) {
               System.out.println("ERROR: The parent element " + var1.getTagName() + " has zero sub element of the tag name " + var2 + "!");
            }

            return null;
         } else {
            String var7 = var6.getNodeValue();
            if (var7 == null) {
               if (var3) {
                  System.out.println("ERROR: The parent element " + var1.getTagName() + " has the sub element of the tag name " + var2 + " without value!");
               }

               return null;
            } else {
               return var7.trim();
            }
         }
      }
   }

   private String getSubEAVal(Element var1, String var2, String var3) {
      NodeList var4 = var1.getElementsByTagName(var2);
      if (var4.getLength() != 1) {
         System.out.println("ERROR: The parent element " + var1.getTagName() + " has more than one sub element of the tag name " + var2 + "!");
         return null;
      } else {
         Element var5 = (Element)var4.item(0);
         String var6 = var5.getAttribute(var3);
         if (var6 == null) {
            System.out.println("ERROR: The element " + var1.getTagName() + " does not have an attribute " + var2 + " defined!");
            return null;
         } else {
            return var6;
         }
      }
   }

   private boolean crossChecking() {
      HashMap var4 = new HashMap();
      HashMap var5 = new HashMap();

      int var1;
      RDomainListEntry var7;
      for(var1 = 0; var1 < this.rtd_list.length; ++var1) {
         TDMRemoteTDomain var3 = this.rtd_list[var1];

         try {
            var3.checkConfigIntegrity();
         } catch (TPException var12) {
            return false;
         }

         var4.put(var3.getAccessPointId(), var3);
         RDomainListEntry var6 = new RDomainListEntry(var3);
         if ((var7 = (RDomainListEntry)var5.put(var3.getAccessPointId(), var6)) != null) {
            var6.setNext(var7);
         }
      }

      for(var1 = 0; var1 < this.ltd_list.length; ++var1) {
         TDMLocalTDomain var2 = this.ltd_list[var1];

         try {
            var2.checkConfigIntegrity();
         } catch (TPException var11) {
            return false;
         }

         if (var4.put(var2.getAccessPointId(), var2) != null) {
            System.out.println("ERROR: Duplicated local domain id " + var2.getAccessPointId() + " found!");
            return false;
         }
      }

      Iterator var13 = var5.values().iterator();

      while(var13.hasNext()) {
         var7 = (RDomainListEntry)var13.next();
         HashMap var8 = new HashMap();

         while(true) {
            TDMRemoteTDomain var9 = var7.getRDom();
            TDMLocal var10 = var9.getLocalAccessPointObject();
            if (var8.put(var10.getAccessPointId(), var10) != null) {
               System.out.println("ERROR: There are two remote domain " + var9.getAccessPointId() + " with same local domain " + var10.getAccessPointId());
               return false;
            }

            if ((var7 = var7.getNext()) == null) {
               break;
            }
         }
      }

      return true;
   }
}
