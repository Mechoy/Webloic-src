package weblogic.security.net;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import weblogic.security.SecurityLogger;
import weblogic.security.SecurityService;

public class ConnectionFilterImpl implements ConnectionFilter, ConnectionFilterRulesListener {
   static ConnectionFilter impl = new ConnectionFilterImpl();
   private FilterEntry[] rules;

   public void checkRules(String[] var1) throws ParseException {
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            String var2 = var1[var3];
            int var4 = var2.indexOf(35);
            if (var4 != -1) {
               var2 = var2.substring(0, var4).trim();
            }

            if (var2.length() != 0) {
               try {
                  this.parseLine(var2, (Vector)null);
               } catch (StreamCorruptedException var6) {
                  throw new ParseException(var6.getMessage(), var3 + 1);
               } catch (IllegalArgumentException var7) {
                  throw new ParseException(var7.getMessage(), var3 + 1);
               } catch (IOException var8) {
                  throw new ParseException(var8.getMessage(), var3 + 1);
               }
            }
         }
      }

   }

   protected void checkRules(String[] var1, Vector var2) throws ParseException {
      if (var1 != null) {
         for(int var4 = 0; var4 < var1.length; ++var4) {
            String var3 = var1[var4];
            int var5 = var3.indexOf(35);
            if (var5 != -1) {
               var3 = var3.substring(0, var5).trim();
            }

            if (var3.length() != 0) {
               try {
                  this.parseLine(var3, var2);
               } catch (StreamCorruptedException var7) {
                  throw new ParseException(var7.getMessage(), var4 + 1);
               } catch (IllegalArgumentException var8) {
                  throw new ParseException(var8.getMessage(), var4 + 1);
               } catch (IOException var9) {
                  throw new ParseException(var9.getMessage(), var4 + 1);
               }
            }
         }
      }

   }

   public void setRules(String[] var1) throws ParseException {
      Vector var2 = new Vector();
      this.checkRules(var1, var2);
      FilterEntry[] var3 = new FilterEntry[var2.size()];
      var2.copyInto(var3);
      this.rules = var3;
   }

   public void accept(ConnectionEvent var1) throws FilterException {
      if (this.rules != null) {
         InetAddress var2 = var1.getRemoteAddress();
         String var3 = var1.getProtocol().toLowerCase(Locale.ENGLISH);
         int var4 = protocolToMaskBit(var3);
         InetAddress var5 = var1.getLocalAddress();
         int var6 = var1.getLocalPort();
         int var7 = var1.getRemotePort();
         if (var4 == -559038737) {
            var4 = 0;
         }

         int var8 = 0;

         while(var8 < this.rules.length) {
            switch (this.rules[var8].check(var2, var4, var5, var6)) {
               case 0:
                  return;
               case 1:
                  throw new FilterException(SecurityLogger.getRuleDenied("" + (var8 + 1)));
               case 2:
                  ++var8;
                  break;
               default:
                  throw new RuntimeException(SecurityLogger.getConnFilterInternalErr());
            }
         }
      }

   }

   protected void parseLine(String var1, Vector var2) throws IOException, IllegalArgumentException {
      StringTokenizer var3 = new StringTokenizer(var1);
      String var4 = var3.nextToken();
      String var5 = var3.nextToken();
      int var6 = parseSingleAddress(var5);
      String var7 = var3.nextToken();
      int var8;
      if (var7.equals("*")) {
         var8 = -1;
      } else {
         var8 = new Integer(var7);
      }

      boolean var9 = parseAction(var3.nextToken());
      if (var4.startsWith("*")) {
         SlowFilterEntry var10 = new SlowFilterEntry(var9, parseProtocols(var3), var4, var6, var8);
         if (var2 != null) {
            var2.addElement(var10);
         }
      } else {
         int var16 = var4.indexOf(47);
         Object var11 = null;
         int var12 = -1;
         int[] var17;
         if (var16 != -1) {
            var17 = parseAddresses(var4.substring(0, var16));
            var12 = parseNetmask(var4.substring(var16 + 1));
         } else {
            var17 = parseAddresses(var4);
         }

         int var13 = parseProtocols(var3);

         for(int var14 = 0; var14 < var17.length; ++var14) {
            FastFilterEntry var15 = new FastFilterEntry(var9, var13, var17[var14], var12, var6, var8);
            if (var2 != null) {
               var2.addElement(var15);
            }
         }
      }

   }

   protected static final int parseProtocols(StringTokenizer var0) throws FilterException {
      int var1;
      int var3;
      for(var1 = 0; var0.hasMoreTokens(); var1 |= var3) {
         String var2 = var0.nextToken();
         var3 = protocolToMaskBit(var2);
         if (var3 == -559038737) {
            throw new IllegalArgumentException(SecurityLogger.getUnknownProtocol(var2));
         }
      }

      return var1;
   }

   private static final int protocolToMaskBit(String var0) throws FilterException {
      String[] var1 = new String[]{"http", "t3", "https", "t3s", "iiop", "iiops", "giop", "giops", "com", "dcom", "ldap", "ldaps"};
      if (var0 == null) {
         return 0;
      } else {
         var0 = var0.toLowerCase(Locale.ENGLISH);

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var0.equals(var1[var2])) {
               return 1 << var2 + 1;
            }
         }

         return -559038737;
      }
   }

   protected static final int[] parseAddresses(String var0) throws IOException {
      InetAddress[] var1 = InetAddress.getAllByName(var0);
      int[] var2 = new int[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = addressToInt(var1[var3]);
      }

      return var2;
   }

   protected static final int parseSingleAddress(String var0) throws IOException {
      if (var0.equals("*")) {
         return -1;
      } else {
         InetAddress var1 = InetAddress.getByName(var0);
         return addressToInt(var1);
      }
   }

   static final int addressToInt(InetAddress var0) {
      byte[] var1 = var0.getAddress();
      int var2 = 0;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2 |= (255 & var1[var3]) << 8 * (var1.length - var3 - 1);
      }

      return var2;
   }

   protected static final int parseNetmask(String var0) throws IOException {
      StringTokenizer var1 = new StringTokenizer(var0, ".");
      int var2 = var1.countTokens();

      try {
         int var3;
         if (var2 == 1) {
            var3 = Integer.parseInt(var1.nextToken());
            if (var3 <= 32 && var3 >= 0) {
               return ~((1 << 32 - var3) - 1);
            } else {
               throw new StreamCorruptedException(SecurityLogger.getBadNetMaskBits(var0));
            }
         } else {
            var3 = 0;
            if (var2 != 4) {
               throw new StreamCorruptedException(SecurityLogger.getBadNetMaskTokens(var0));
            } else {
               for(int var4 = 24; var1.hasMoreTokens(); var4 -= 8) {
                  int var5 = Integer.parseInt(var1.nextToken());
                  if (var5 < 0 || var5 > 255) {
                     throw new StreamCorruptedException(SecurityLogger.getBadNetMaskNum(var0));
                  }

                  var3 |= var5 << var4;
               }

               return var3;
            }
         }
      } catch (NumberFormatException var6) {
         throw new StreamCorruptedException(SecurityLogger.getBadNetMaskFormat(var0));
      }
   }

   protected static final boolean parseAction(String var0) throws IOException {
      String var1 = var0.toLowerCase(Locale.ENGLISH);
      if (var1.equals("allow")) {
         return true;
      } else if (var1.equals("deny")) {
         return false;
      } else {
         throw new StreamCorruptedException(SecurityLogger.getBadAction(var1));
      }
   }

   public static final boolean filterEnabled() {
      return SecurityService.getConnectionFilterEnabled();
   }

   public static final ConnectionFilter getFilter() {
      return SecurityService.getConnectionFilter();
   }

   /** @deprecated */
   public static final void setFilter(ConnectionFilter var0) {
      if (var0 == null) {
         throw new NullPointerException(SecurityLogger.getNullFilter());
      } else if (SecurityService.getConnectionFilter() != null) {
         throw new SecurityException(SecurityLogger.getSetFilterMoreThanOnce());
      } else {
         SecurityService.setConnectionFilter(var0);
      }
   }
}
