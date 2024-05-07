package weblogic.server.channels;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

public class AddressUtils {
   public static final InetAddress getLoopback() {
      return AddressUtils.AddressMaker.LOOPBACK;
   }

   public static final InetAddress getLocalHost() {
      return AddressUtils.AddressMaker.LOCALHOST;
   }

   public static final InetAddress[] getIPAny() {
      return AddressUtils.AddressMaker.IP_ANY;
   }

   public static final String getIPAny(int var0, boolean var1) {
      return var1 ? AddressUtils.AddressMaker.IP_ANY[var0].getHostName().toLowerCase() : AddressUtils.AddressMaker.IP_ANY[var0].getHostAddress();
   }

   private static final class AddressMaker {
      private static final InetAddress LOOPBACK = getLoopback();
      private static final InetAddress LOCALHOST = getLocalHost();
      private static final InetAddress[] IP_ANY = getAllAddresses();

      private static final InetAddress getLoopback() {
         try {
            return InetAddress.getByName((String)null);
         } catch (UnknownHostException var2) {
            AssertionError var1 = new AssertionError("Could not obtain the loopback address. The most likely cause is an error in the network configuration of this machine.");
            var1.initCause(var2);
            throw var1;
         }
      }

      private static final InetAddress getLocalHost() {
         try {
            return InetAddress.getLocalHost();
         } catch (UnknownHostException var2) {
            AssertionError var1 = new AssertionError("Could not obtain the localhost address. The most likely cause is an error in the network configuration of this machine.");
            var1.initCause(var2);
            throw var1;
         }
      }

      private static final InetAddress[] getAllAddresses() {
         AssertionError var1;
         try {
            ArrayList var0 = new ArrayList();
            ArrayList var11 = new ArrayList();
            ArrayList var2 = new ArrayList();
            ArrayList var3 = new ArrayList();
            Enumeration var4 = NetworkInterface.getNetworkInterfaces();

            while(true) {
               while(var4.hasMoreElements()) {
                  NetworkInterface var5 = (NetworkInterface)var4.nextElement();
                  boolean var6 = Jdk6.isVirtual(var5);
                  Enumeration var7;
                  InetAddress var8;
                  if (var6) {
                     var7 = var5.getInetAddresses();

                     while(var7.hasMoreElements()) {
                        var8 = (InetAddress)var7.nextElement();
                        var8 = InetAddress.getByAddress(var8.getAddress());
                        if (var8.isLoopbackAddress()) {
                           var3.add(var8);
                        } else if (var8.isSiteLocalAddress()) {
                           var11.add(var8);
                        } else if (var8.isLinkLocalAddress()) {
                           var2.add(var8);
                        } else if (!var8.isMulticastAddress() && !var8.isAnyLocalAddress()) {
                           var0.add(var8);
                        }
                     }
                  } else {
                     var7 = var5.getInetAddresses();

                     while(var7.hasMoreElements()) {
                        var8 = (InetAddress)var7.nextElement();
                        var8 = InetAddress.getByAddress(var8.getAddress());
                        if (var8.isLoopbackAddress()) {
                           var3.add(0, var8);
                        } else if (var8.isSiteLocalAddress()) {
                           var11.add(0, var8);
                        } else if (var8.isLinkLocalAddress()) {
                           var2.add(0, var8);
                        } else if (!var8.isMulticastAddress() && !var8.isAnyLocalAddress()) {
                           var0.add(0, var8);
                        }
                     }
                  }
               }

               if (!var11.isEmpty()) {
                  var0.addAll(var11);
               }

               if (!var2.isEmpty()) {
                  var0.addAll(var2);
               }

               if (!var3.isEmpty()) {
                  var0.addAll(var3);
               }

               return (InetAddress[])((InetAddress[])var0.toArray(new InetAddress[var0.size()]));
            }
         } catch (SocketException var9) {
            var1 = new AssertionError("An error occurred while retrieving the network addresses for this machine. The most likely cause is an error in the network configuration of this machine.");
            var1.initCause(var9);
            throw var1;
         } catch (UnknownHostException var10) {
            var1 = new AssertionError("An error occurred while retrieving the network addresses for this machine. The most likely cause is an error in the network configuration of this machine.");
            var1.initCause(var10);
            throw var1;
         }
      }
   }
}
