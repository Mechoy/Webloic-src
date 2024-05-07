package weblogic.corba.j2ee.naming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import javax.naming.CompoundName;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingException;
import weblogic.corba.iiop.cluster.RandomSelector;
import weblogic.corba.iiop.cluster.RoundRobinSelector;
import weblogic.corba.iiop.cluster.Selector;

public final class NameParser implements javax.naming.NameParser {
   public static final String IIOP_PROTOCOL = "iiop";
   public static final String TGIOP_PROTOCOL = "tgiop";
   public static final String COMPLEX_PROTOCOL = "complex";
   public static final String IIOPS_PROTOCOL = "iiops";
   public static final String HTTP_PROTOCOL = "http";
   public static final String HTTPS_PROTOCOL = "https";
   public static final String TGIOP_PREFIX = "tgiop:";
   public static final String IIOP_PREFIX = "iiop:";
   public static final String T3_PREFIX = "t3:";
   public static final String T3S_PREFIX = "t3s:";
   public static final String IOR_PREFIX = "IOR:";
   public static final String IIOPS_PREFIX = "iiops:";
   public static final String IIOPLOC_PREFIX = "iioploc:";
   public static final String HTTP_PREFIX = "http:";
   public static final String HTTPS_PREFIX = "https:";
   public static final String CORBALOC_PREFIX = "corbaloc:";
   public static final String IIOPNAME_PREFIX = "iiopname:";
   public static final String CORBANAME_PREFIX = "corbaname:";
   public static final String NAME_SERVICE = "NameService";
   public static final boolean DEBUG = false;
   private static HashMap protocolMap = new HashMap();
   private static HashMap clientProtocolMap;
   public static final String CORBALOC_RIR_PREFIX = "corbaloc:rir:";
   public static final String CORBALOC_TGIOP_PREFIX = "corbaloc:tgiop:";
   public static final String CORBALOC_IIOP_PREFIX = "corbaloc:iiop:";
   public static final String CORBALOC_HTTP_PREFIX = "corbaloc:http:";
   public static final String CORBALOC_HTTPS_PREFIX = "corbaloc:https:";
   public static final String CORBALOC_IIOP_PREFIX2 = "corbaloc::";
   public static final String CORBALOC_IIOPS_PREFIX = "corbaloc:iiops:";
   public static final String CORBANAME_RIR_PREFIX = "corbaname:rir:";
   public static final String CORBANAME_TGIOP_PREFIX = "corbaname:tgiop:";
   public static final String CORBANAME_IIOP_PREFIX = "corbaname:iiop:";
   public static final String CORBANAME_IIOP_PREFIX2 = "corbaname::";
   public static final String CORBANAME_IIOPS_PREFIX = "corbaname:iiops:";
   private static HashMap complexProtocolMap;
   private static int defaultMinorVersion;
   private static final Properties defaultProps;

   public static void initialize(int var0) {
      defaultMinorVersion = var0;
   }

   public static boolean isGIOPProtocol(String var0) {
      return getProtocol(var0) != null;
   }

   public Name parse(String var1) throws NamingException {
      return parseName(var1);
   }

   public static Name parseName(String var0) throws NamingException {
      return new CompoundName(var0, defaultProps);
   }

   public static String getProtocol(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.indexOf(58);
         if (var1 < 0) {
            return null;
         } else {
            String var2 = var0.substring(0, var1 + 1);
            String var3 = (String)protocolMap.get(var2);
            if (var3 == "complex") {
               var1 = var0.indexOf(58, var1 + 1);
               if (var1 < 0) {
                  return null;
               }

               var2 = var0.substring(0, var1 + 1);
               var3 = (String)complexProtocolMap.get(var2);
            }

            return var3;
         }
      }
   }

   public static String getProtocolString(String var0) throws InvalidNameException {
      if (getProtocol(var0) == null) {
         return null;
      } else {
         try {
            int var1 = var0.indexOf(35);
            if (var1 >= 0) {
               return var0.substring(0, var1);
            } else {
               var1 = var0.indexOf(47);
               if (var1 < 0) {
                  return var0;
               } else {
                  int var4 = var0.indexOf(47, var1 + 1);
                  if (var4 < 0) {
                     return var0;
                  } else if (var0.substring(var1 + 1, var4).equals("NameService")) {
                     return var0.substring(0, var4 + 1);
                  } else {
                     var1 = var0.indexOf(47, var4 + 1);
                     return var1 < 0 ? var0 : var0.substring(0, var1 + 1);
                  }
               }
            }
         } catch (Exception var3) {
            InvalidNameException var2 = new InvalidNameException("Invalid scheme");
            var2.setRootCause(var3);
            throw var2;
         }
      }
   }

   public static String getNameString(String var0) {
      if (getProtocol(var0) == null) {
         return var0;
      } else {
         int var1 = var0.indexOf(35);
         if (var1 >= 0) {
            return var0.substring(var1 + 1);
         } else {
            var1 = var0.indexOf(47);
            if (var1 < 0) {
               return "";
            } else {
               int var2 = var0.indexOf(47, var1 + 1);
               if (var2 < 0) {
                  return "";
               } else if (var0.substring(var1 + 1, var2).equals("NameService")) {
                  return var0.substring(var2 + 1);
               } else {
                  var1 = var0.indexOf(47, var2 + 1);
                  return var1 >= 0 ? var0.substring(var1 + 1) : "";
               }
            }
         }
      }
   }

   public static URLInfo parseURL(String var0) throws InvalidNameException {
      if (var0 == null) {
         throw new InvalidNameException("url is null");
      } else {
         int var1 = var0.indexOf(58);
         String var2 = var0.substring(0, var1 + 1);
         String var3 = (String)clientProtocolMap.get(var2);
         if (var3 == "complex") {
            return parseComplexURL(var0);
         } else if (var3 == null) {
            return null;
         } else {
            URLInfo var4 = new URLInfo(var3);
            String var5 = var0.substring(var1 + 1);
            if (!var5.startsWith("//")) {
               throw new InvalidNameException("url does not contain //");
            } else {
               var5 = var5.substring(2);
               int var6 = 0;
               ArrayList var7 = null;

               while(true) {
                  do {
                     if (var6 < 0) {
                        if (var7 != null) {
                           var4.addressList = (EndPointInfo[])((EndPointInfo[])var7.toArray(var4.addressList));
                           var4.randomizeStart();
                        } else {
                           normalizeClusterAddress(var4);
                        }

                        return var4;
                     }

                     var6 = var5.indexOf(44);
                     if (var6 < 0) {
                        var6 = var5.indexOf(124);
                        if (var6 >= 0) {
                           var4.selector = RandomSelector.SELECTOR;
                        }
                     }

                     String var8 = var5;
                     if (var6 >= 0) {
                        var8 = var5.substring(0, var6);
                        var5 = var5.substring(var6 + 1);
                     }

                     String var9 = var8;
                     int var10 = -1;
                     if (var3 != "tgiop") {
                        var1 = var8.lastIndexOf(58);
                        if (var1 < 0) {
                           var1 = var5.indexOf(58);
                           if (var1 < 0) {
                              throw new InvalidNameException("url does not contain a port");
                           }

                           var9 = var8;
                           var8 = var5;
                        } else {
                           var9 = var8.substring(0, var1);
                        }

                        int var11 = var8.indexOf(47);
                        if (var11 < 0) {
                           var11 = var8.length();
                        }

                        try {
                           var10 = Integer.parseInt(var8.substring(var1 + 1, var11));
                        } catch (NumberFormatException var13) {
                        }

                        if (var8.length() > var11) {
                           var4.name = var8.substring(var11);
                        }
                     }

                     var4.addressList[0] = new EndPointInfo(var9, var10, 1, defaultMinorVersion);
                  } while(var6 <= 0 && var7 == null);

                  if (var7 == null) {
                     var7 = new ArrayList();
                  }

                  var7.add(var4.addressList[0]);
               }
            }
         }
      }
   }

   private static void normalizeClusterAddress(URLInfo var0) throws InvalidNameException {
   }

   public static URLInfo parseComplexURL(String var0) throws InvalidNameException {
      if (var0 == null) {
         throw new InvalidNameException("url is null");
      } else {
         int var1 = var0.indexOf(58);
         if (var1 < 0) {
            throw new InvalidNameException("url does not contain :");
         } else {
            var1 = var0.indexOf(58, var1 + 1);
            if (var1 < 0) {
               throw new InvalidNameException("url does not contain ::");
            } else {
               String var2 = var0.substring(0, var1 + 1);
               String var3 = (String)complexProtocolMap.get(var2);
               if (var3 == null) {
                  throw new InvalidNameException("url does not contain a known protocol");
               } else {
                  URLInfo var4 = new URLInfo(var3);
                  String var5 = var0.substring(var1 + 1);
                  if (var0.startsWith("corbaname:")) {
                     var1 = var5.indexOf(35);
                     if (var1 >= 0) {
                        var4.name = var5.substring(var1 + 1);
                     } else {
                        var1 = var5.length();
                     }
                  } else if (var0.startsWith("corbaloc:")) {
                     var1 = var5.indexOf(47);
                     if (var1 < 0) {
                        throw new InvalidNameException("url does not contain a service name");
                     }

                     String var6 = var5.substring(var1 + 1);
                     int var7 = var6.indexOf(47);
                     if (var7 >= 0) {
                        var4.serviceName = var6.substring(0, var7);
                        var4.name = var6.substring(var7 + 1);
                     } else {
                        var4.serviceName = var6;
                     }
                  }

                  var5 = var5.substring(0, var1);
                  int var17 = 0;
                  ArrayList var18 = null;

                  while(true) {
                     do {
                        if (var17 < 0) {
                           if (var18 != null) {
                              var4.addressList = (EndPointInfo[])((EndPointInfo[])var18.toArray(var4.addressList));
                              var4.randomizeStart();
                           } else {
                              normalizeClusterAddress(var4);
                           }

                           return var4;
                        }

                        int var8 = defaultMinorVersion;
                        int var9 = 1;
                        var17 = var5.indexOf(44);
                        if (var17 < 0) {
                           var17 = var5.indexOf(124);
                           if (var17 >= 0) {
                              var4.selector = RandomSelector.SELECTOR;
                           }
                        }

                        String var10 = var5;
                        if (var17 >= 0) {
                           var10 = var5.substring(0, var17);

                           try {
                              var5 = var5.substring(var17 + 1);
                              var5 = var5.substring(var5.indexOf(58) + 1);
                           } catch (IndexOutOfBoundsException var15) {
                              throw new InvalidNameException("url contains an Invalid multi-url specification");
                           }
                        }

                        String var11;
                        try {
                           if (var10.indexOf(64) != -1) {
                              var11 = var10.substring(0, var10.indexOf(64));
                              var10 = var10.substring(var10.indexOf(64) + 1);
                              var9 = Integer.parseInt(var11.substring(0, 1));
                              var8 = Integer.parseInt(var11.substring(2, 3));
                           }
                        } catch (NumberFormatException var14) {
                           throw new InvalidNameException("url contains an invalid version specification");
                        }

                        var11 = var10;
                        int var12 = -1;
                        if (var3 != "tgiop") {
                           var1 = var10.lastIndexOf(58);
                           if (var1 < 0) {
                              throw new InvalidNameException("url does not contain :");
                           }

                           var11 = var10.substring(0, var1);

                           try {
                              var12 = Integer.parseInt(var10.substring(var1 + 1));
                           } catch (NumberFormatException var16) {
                           }
                        }

                        var4.addressList[0] = new EndPointInfo(var11, var12, var9, var8);
                     } while(var17 <= 0 && var18 == null);

                     if (var18 == null) {
                        var18 = new ArrayList();
                     }

                     var18.add(var4.addressList[0]);
                  }
               }
            }
         }
      }
   }

   public static void main(String[] var0) throws InvalidNameException {
      if (var0.length == 1) {
         String var1 = var0[0];
         System.out.println(parseURL(var1));
      } else {
         System.out.println("NameParser <url>");
      }

   }

   private static void p(String var0) {
      System.out.println("<NameParser> " + var0);
   }

   static {
      protocolMap.put("iiops:", "iiops");
      protocolMap.put("iiop:", "iiop");
      protocolMap.put("IOR:", "iiop");
      protocolMap.put("tgiop:", "tgiop");
      protocolMap.put("iioploc:", "iiop");
      protocolMap.put("iiopname:", "iiop");
      protocolMap.put("corbaloc:", "complex");
      protocolMap.put("corbaname:", "complex");
      clientProtocolMap = new HashMap(protocolMap);
      clientProtocolMap.put("t3s:", "iiops");
      clientProtocolMap.put("t3:", "iiop");
      clientProtocolMap.put("http:", "http");
      clientProtocolMap.put("https:", "https");
      complexProtocolMap = new HashMap();
      complexProtocolMap.put("corbaloc:rir:", "iiop");
      complexProtocolMap.put("corbaloc:tgiop:", "tgiop");
      complexProtocolMap.put("corbaloc:iiop:", "iiop");
      complexProtocolMap.put("corbaloc::", "iiop");
      complexProtocolMap.put("corbaloc:iiops:", "iiops");
      complexProtocolMap.put("corbaloc:http:", "http");
      complexProtocolMap.put("corbaloc:https:", "https");
      complexProtocolMap.put("corbaname:rir:", "iiop");
      complexProtocolMap.put("corbaname:tgiop:", "tgiop");
      complexProtocolMap.put("corbaname:iiop:", "iiop");
      complexProtocolMap.put("corbaname::", "iiop");
      complexProtocolMap.put("corbaname:iiops:", "iiops");
      defaultMinorVersion = 2;
      defaultProps = new Properties();
      defaultProps.put("jndi.syntax.direction", "left_to_right");
      defaultProps.put("jndi.syntax.separator", "/");
      defaultProps.put("jndi.syntax.separator2", ".");
      defaultProps.put("jndi.syntax.ignorecase", "false");
      defaultProps.put("jndi.syntax.escape", "\\");
      defaultProps.put("jndi.syntax.beginquote", "\"");
      defaultProps.put("jndi.syntax.endquote", "\"");
      defaultProps.put("jndi.syntax.beginquote2", "'");
      defaultProps.put("jndi.syntax.endquote2", "'");
      defaultProps.put("jndi.syntax.separator.ava", ",");
      defaultProps.put("jndi.syntax.separator.typeval", "=");
   }

   public static class URLInfo {
      private Selector selector;
      private int current;
      public EndPointInfo[] addressList;
      public String name;
      public String serviceName;
      public String protocol;

      public URLInfo(String var1) {
         this.selector = RoundRobinSelector.SELECTOR;
         this.current = 0;
         this.name = "";
         this.serviceName = "NameService";
         this.protocol = null;
         this.protocol = var1;
         this.addressList = new EndPointInfo[]{new EndPointInfo()};
      }

      public final String getHost() {
         return this.addressList[this.current].getHost();
      }

      public final int getPort() {
         return this.addressList[this.current].getPort();
      }

      public final int getMinorVersion() {
         return this.addressList[this.current].getMinorVersion();
      }

      public final int getMajorVersion() {
         return this.addressList[this.current].getMajorVersion();
      }

      public final EndPointInfo getAddress() {
         return this.addressList[this.current];
      }

      public final EndPointInfo getNextAddress() {
         EndPointInfo var1 = this.addressList[this.current];
         this.current = this.selector.select(this.current, this.addressList.length);
         return var1;
      }

      public final String getService() {
         return this.serviceName;
      }

      public final String getProtocol() {
         return this.protocol;
      }

      void randomizeStart() {
         if (this.addressList != null && this.addressList.length > 1) {
            double var1 = Math.random() * (double)this.addressList.length + 0.5;
            this.current = (int)Math.round(var1) - 1;
         } else {
            this.current = 0;
         }

      }

      final String getKey() {
         return this.protocol + this.getAddress().getAddress();
      }

      final String getNextKey() {
         return this.protocol + this.getNextAddress().getAddress();
      }

      public String getURL() {
         return "corbaloc:" + this.protocol + ":" + this.addressList[this.current].getAddress() + "/" + this.serviceName;
      }

      public String getClusterURL() {
         StringBuffer var1 = new StringBuffer(this.protocol + "://");

         for(int var2 = 0; var2 < this.addressList.length; ++var2) {
            if (var2 != 0) {
               var1.append(",");
            }

            var1.append(this.addressList[var2].getAddress());
         }

         return var1.toString();
      }

      public String getNeutralURL() {
         return "corbaloc::" + this.addressList[this.current].getAddress() + "/" + this.serviceName;
      }

      public boolean isSecure() {
         return this.protocol == "iiops";
      }

      public String toString() {
         return this.getURL() + (this.name.length() == 0 ? "" : "/" + this.name);
      }
   }
}
