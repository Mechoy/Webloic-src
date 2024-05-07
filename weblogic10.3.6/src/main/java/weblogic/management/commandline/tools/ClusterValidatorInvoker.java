package weblogic.management.commandline.tools;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ClusterValidatorInvoker {
   private static final boolean debug = false;
   private static final boolean verbose = true;
   private Document configXML = null;
   private HashMap clusterAddresses = new HashMap();
   private boolean noError = true;
   private static String domainName = null;

   public static void main(String[] var0) {
      if (var0.length != 1) {
         usage();
      } else if (var0[0].equalsIgnoreCase("help")) {
         usage();
      } else {
         String var1 = var0[0];
         ClusterValidatorInvoker var2 = new ClusterValidatorInvoker();
         log("before XML parsing");
         Document var3 = null;

         try {
            var3 = var2.getDOM(var1);
         } catch (SAXException var7) {
            var2.error("Could not parse config.xml file.");
            return;
         } catch (Exception var8) {
            var2.error("Could not read the xml file. Make sure that the pathto the config file is correct");
            return;
         }

         Element var4 = var3.getDocumentElement();
         domainName = var4.getAttribute("Name");
         System.out.println("DomainName=" + domainName);
         HashMap var5 = var2.getMap(var4, "Cluster");
         HashMap var6 = var2.getMap(var4, "Server");
         var2.validateMulticastAddress(var5);
         var2.validateClusterAddressForServers(var6, var5);
         if (var2.noError) {
            System.out.println("Cluster configuration in " + var1 + " is valid.");
         }

      }
   }

   public static void usage() {
      System.out.println("Usage: java ClusterValidator config.xml");
   }

   public static void log(String var0) {
   }

   public static void warn(String var0) {
      System.err.println("WARNING:" + var0);
   }

   public void error(String var1) {
      this.noError = false;
      System.err.println("ERROR:" + var1);
   }

   private HashMap getMap(Element var1, String var2) {
      NodeList var3 = var1.getElementsByTagName(var2);
      HashMap var4 = new HashMap();

      for(int var5 = 0; var5 < var3.getLength(); ++var5) {
         Node var6 = var3.item(var5);
         String var7 = getAttribute(var6, "Name");
         if (var2.equals("Cluster") && var7.equals(domainName)) {
            warn("Cluster found with  same name as Domain. Recommend changing the name in config.xml");
         }

         var4.put(var7, var6);
      }

      return var4;
   }

   private Document getDOM(String var1) throws SAXException, Exception {
      if (this.configXML != null) {
         return this.configXML;
      } else {
         File var2 = new File(var1);
         FileInputStream var3 = new FileInputStream(var2);
         DocumentBuilderFactory var4 = DocumentBuilderFactory.newInstance();
         DocumentBuilder var5 = var4.newDocumentBuilder();
         this.configXML = var5.parse(new InputSource(var3));
         return this.configXML;
      }
   }

   private boolean validateClusterAddressForServers(HashMap var1, HashMap var2) {
      if (var1 == null) {
         return true;
      } else {
         boolean var3 = true;
         Node var4 = null;
         Node var5 = null;
         String var6 = null;
         String var7 = null;
         String var8 = null;
         String var9 = null;
         String var10 = null;
         Iterator var11 = var1.values().iterator();

         while(var11.hasNext()) {
            var4 = (Node)var11.next();
            var6 = getAttribute(var4, "Name");
            var7 = getAttribute(var4, "Cluster");
            if (var7 != null && var7.length() != 0) {
               var5 = (Node)((Node)var2.get(var7));
               if (var5 == null) {
                  this.error(": Server:" + var6 + " refers to a non-existent Cluster:" + var7);
                  var3 = false;
               } else {
                  var8 = getAttribute(var5, "ClusterAddress");
                  var9 = getAttribute(var4, "ListenAddress");
                  var10 = getAttribute(var4, "ListenPort");
                  if (!this.checkListenAddressInClusterAddress(var8, var9, var10, var6, var7)) {
                     this.error(": Server server:" + var6 + "'s address host:" + var9 + " port:" + var10 + " is not in clusterAddress:" + var8 + " for cluster:" + var7);
                     var3 = false;
                  }
               }
            }
         }

         return var3;
      }
   }

   private boolean checkListenAddressInClusterAddress(String var1, String var2, String var3, String var4, String var5) {
      InetAddress var6 = null;

      try {
         var6 = InetAddress.getByName(var2);
      } catch (UnknownHostException var16) {
         this.error(": Server:" + var4 + " from cluster:" + var5 + " is listening on Unknown Host listenAddress=" + var2);
      }

      boolean var7 = false;
      ClusterAddressComponent[] var8 = (ClusterAddressComponent[])((ClusterAddressComponent[])this.clusterAddresses.get(var5));
      int var10;
      int var11;
      if (var8 == null) {
         if (var1.indexOf(",") != -1) {
            warn(" Comma Separated Lists for cluster:" + var5 + "Address:" + var1 + " is not a recommended configuration");
            StringTokenizer var17 = new StringTokenizer(var1, ",");
            var10 = var17.countTokens();
            var8 = new ClusterAddressComponent[var10];

            for(var11 = 0; var11 < var10; ++var11) {
               String var12 = var17.nextToken();
               int var13 = var12.indexOf(58);
               if (var13 == -1) {
                  var8[var11] = new ClusterAddressComponent(var12, "-1");
               } else {
                  var8[var11] = new ClusterAddressComponent(var12.substring(0, var13), var12.substring(var13 + 1));
               }
            }
         } else {
            var8 = new ClusterAddressComponent[1];
            int var9 = var1.indexOf(58);
            if (var9 == -1) {
               var8[0] = new ClusterAddressComponent(var1, "-1");
            } else {
               var8[0] = new ClusterAddressComponent(var1.substring(0, var9), var1.substring(var9 + 1));
            }
         }

         this.clusterAddresses.put(var5, var8);
      }

      var7 = false;
      InetAddress[] var18 = null;
      var10 = -1;

      for(var11 = 0; var11 < var8.length; ++var11) {
         try {
            var18 = InetAddress.getAllByName(var8[var11].host);
         } catch (UnknownHostException var15) {
            this.error(": Cluster Address:" + var8[var11].host + " is an unknown host");
         }

         try {
            var10 = Integer.parseInt(var8[var11].port);
         } catch (NumberFormatException var14) {
            this.error(": Port for cluster:" + var5 + " is not a valid port number");
         }

         if (var18 != null) {
            for(int var19 = 0; var19 < var18.length; ++var19) {
               log("Comparing clusterHost:" + var18[var19] + " listenAddress" + var6 + "clusterPort:" + var10 + "listenPort:" + var3);
               if (var18[var19].equals(var6) && (var10 == -1 || var10 == Integer.parseInt(var3))) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean validateMulticastAddress(HashMap var1) {
      if (var1 == null) {
         return true;
      } else {
         boolean var2 = true;
         Object var3 = null;
         String var4 = null;
         Node var5 = null;
         Iterator var6 = var1.values().iterator();

         while(var6.hasNext()) {
            var5 = (Node)((Node)var6.next());
            String var7 = getAttribute(var5, "MulticastAddress");
            if (var7 != null) {
               var4 = getAttribute(var5, "Name");

               try {
                  InetAddress var8 = InetAddress.getByName(var7);
                  if (!var8.isMulticastAddress()) {
                     var2 = false;
                     this.error("Cluster name:" + var4 + " has an INVALID Multicast address:" + var7 + " Please pick an address between (224.0.0.1 and 255.255.255.255)");
                  }
               } catch (UnknownHostException var9) {
                  var2 = false;
                  this.error(": Cluster name:" + var4 + " refers to an UNKNOWN Multicast Address:" + var7 + " Please pick an address between (224.0.0.1 and 255.255.255.255)");
               }
            }
         }

         return var2;
      }
   }

   private static String getAttribute(Node var0, String var1) {
      Node var2 = var0.getAttributes().getNamedItem(var1);
      return var2 == null ? null : var2.getNodeValue();
   }

   class ClusterAddressComponent {
      public String host;
      public String port;

      public ClusterAddressComponent(String var2, String var3) {
         ClusterValidatorInvoker.log("Building ClusterComponent host:" + var2 + " port:" + var3);
         this.host = var2;
         this.port = var3;
      }
   }
}
