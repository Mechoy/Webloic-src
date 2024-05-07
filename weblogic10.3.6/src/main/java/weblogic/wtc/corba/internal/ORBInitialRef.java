package weblogic.wtc.corba.internal;

import com.bea.core.jatmi.common.ntrace;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.Object;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

public final class ORBInitialRef {
   public static final String CORBALOC_PREFIX = "corbaloc";
   public static final String CORBANAME_PREFIX = "corbaname";
   public static final String CORBALOC_RIR_PREFIX = "corbaloc:rir:";
   public static final String CORBALOC_TGIOP_PREFIX = "corbaloc:tgiop:";
   public static final String CORBANAME_RIR_PREFIX = "corbaname:rir:";
   public static final String CORBANAME_TGIOP_PREFIX = "corbaname:tgiop:";
   public static final String NAME_SERVICE = "NameService";
   private static final Map initialRefIdMap = new HashMap();
   private static final Map initialRefOidMap;
   private static final Map initialRefOaIdMap;
   private final Map cachedReferences;
   private final org.omg.CORBA.ORB orb;

   public ORBInitialRef(org.omg.CORBA.ORB var1) {
      this.orb = var1;
      this.cachedReferences = Collections.synchronizedMap(new HashMap());
   }

   public Object convertTGIOPURLToObject(String var1) throws InvalidName {
      boolean var2 = ntrace.isTraceEnabled(8);
      if (var2) {
         ntrace.doTrace("[/ORBInitialRef/convertTGIOPURL/" + var1);
      }

      if (var1 != null && (var1.startsWith("corbaloc:tgiop:") || var1.startsWith("corbaname:tgiop:"))) {
         boolean var3 = false;
         boolean var4 = false;
         int var18;
         if (var1.startsWith("corbaloc:tgiop:")) {
            var1 = var1.substring("corbaloc:tgiop:".length());
            Object var5 = (Object)this.cachedReferences.get(var1);
            if (var5 != null) {
               if (var2) {
                  ntrace.doTrace("]/ORBInitialRef/convertTGIOPURL/20/" + var5);
               }

               return var5;
            }

            var18 = var1.indexOf(47);
         } else {
            var1 = var1.substring("corbaname:tgiop:".length());
            var3 = true;
            var18 = var1.indexOf(35);
         }

         int var19 = 1;
         int var6 = 0;
         String var7 = null;
         if (var3) {
            var7 = "NameService";
         } else {
            var7 = var1.substring(var18 + 1);
         }

         String var8 = (String)initialRefIdMap.get(var7);
         String var9 = (String)initialRefOidMap.get(var7);
         Integer var10 = (Integer)initialRefOaIdMap.get(var7);
         if (var8 == null) {
            if (var2) {
               ntrace.doTrace("*]/ORBInitialRef/convertTGIOPURL/40");
            }

            throw new InvalidName();
         } else {
            String var11 = var1.substring(0, var18);
            String var12 = var11;

            try {
               if (var11.indexOf(64) != -1) {
                  var12 = var11.substring(var11.indexOf(64) + 1);
                  String var13 = var11.substring(0, var11.indexOf(64));
                  var19 = Integer.parseInt(var13.substring(0, var13.indexOf(46)));
                  var6 = Integer.parseInt(var13.substring(var13.indexOf(46) + 1));
               }
            } catch (NumberFormatException var17) {
               if (var2) {
                  ntrace.doTrace("*]/ORBInitialRef/convertTGIOPURL/50");
               }

               throw new BAD_PARAM();
            }

            StringBuffer var20 = new StringBuffer();
            add_byte(var20, (byte)0);
            add_string(var20, var8);
            add_ulong(var20, 1);
            add_ulong(var20, 0);
            StringBuffer var14 = new StringBuffer();
            add_byte(var14, (byte)0);
            add_byte(var14, (byte)var19);
            add_byte(var14, (byte)var6);
            add_string(var14, "");
            add_short(var14, 0);
            StringBuffer var15 = new StringBuffer();
            add_byte(var15, (byte)0);
            add_byte(var15, (byte)66);
            add_byte(var15, (byte)69);
            add_byte(var15, (byte)65);
            add_byte(var15, (byte)8);
            add_byte(var15, (byte)1);
            add_byte(var15, (byte)2);
            add_byte(var15, var10.byteValue());
            add_string(var15, var12);
            add_ulong(var15, 0);
            add_string(var15, var8);
            add_string(var15, var9);
            add_ulong(var15, 0);
            add_ulong(var14, var15.length() / 2);
            var14.append(var15);
            if (var19 != 1 || var6 != 0) {
               add_ulong(var14, 0);
            }

            add_ulong(var20, var14.length() / 2);
            var20.append(var14);
            var20.insert(0, "IOR:");
            if (var2) {
               ntrace.doTrace("/ORBInitialRef/convertTGIOPURL/100/" + var20.toString());
            }

            Object var16 = this.orb.string_to_object(var20.toString());
            if (var3) {
               var16 = this.resolvePath(var16, var1.substring(var18 + 1));
            } else {
               this.cachedReferences.put(var1, var16);
            }

            if (var2) {
               ntrace.doTrace("]/ORBInitialRef/convertTGIOPURL/120/" + var16);
            }

            return var16;
         }
      } else {
         if (var2) {
            ntrace.doTrace("*]/ORBInitialRef/convertTGIOPURL/10");
         }

         throw new InvalidName();
      }
   }

   public Object resolvePath(Object var1, String var2) throws InvalidName {
      boolean var3 = ntrace.isTraceEnabled(8);
      if (var3) {
         ntrace.doTrace("[/ORBInitialRef/resolvePath/" + var2);
      }

      NamingContext var4 = NamingContextHelper.narrow(var1);
      int var5 = 1;

      String var6;
      for(var6 = var2; var6.indexOf(47) != -1; var6 = var6.substring(var6.indexOf(47) + 1)) {
         ++var5;
      }

      NameComponent[] var7 = new NameComponent[var5];
      var6 = var2;

      for(var5 = 0; var6.indexOf(47) != -1; var6 = var6.substring(var6.indexOf(47) + 1)) {
         var7[var5] = new NameComponent(var6.substring(0, var6.indexOf(47)), "");
         ++var5;
      }

      var7[var5] = new NameComponent(var6, "");

      try {
         Object var8 = var4.resolve(var7);
         if (var3) {
            ntrace.doTrace("*]/ORBInitialRef/resolvePath/100/" + var8);
         }

         return var8;
      } catch (Exception var9) {
         if (var3) {
            ntrace.doTrace("*]/ORBInitialRef/resolvePath/120/" + var9);
         }

         throw new InvalidName();
      }
   }

   private static void add_nibble(StringBuffer var0, byte var1) {
      var0.append(Integer.toString(var1, 16));
   }

   private static void add_byte(StringBuffer var0, byte var1) {
      byte var2 = (byte)(var1 >>> 4 & 15);
      add_nibble(var0, var2);
      var2 = (byte)(var1 & 15);
      add_nibble(var0, var2);
   }

   private static void add_padding(StringBuffer var0, int var1) {
      int var2 = var0.length() / 2 % var1;
      if (var2 != 0) {
         for(int var3 = 0; var3 < var1 - var2; ++var3) {
            add_byte(var0, (byte)0);
         }

      }
   }

   private static void add_ulong(StringBuffer var0, int var1) {
      add_padding(var0, 4);
      byte var2 = (byte)(var1 >>> 24 & 255);
      byte var3 = (byte)(var1 >>> 16 & 255);
      byte var4 = (byte)(var1 >>> 8 & 255);
      byte var5 = (byte)(var1 & 255);
      add_byte(var0, var2);
      add_byte(var0, var3);
      add_byte(var0, var4);
      add_byte(var0, var5);
   }

   private static void add_short(StringBuffer var0, int var1) {
      add_padding(var0, 2);
      byte var2 = (byte)(var1 >>> 8 & 255);
      byte var3 = (byte)(var1 & 255);
      add_byte(var0, var2);
      add_byte(var0, var3);
   }

   private static void add_string(StringBuffer var0, String var1) {
      if (var1 == null) {
         add_ulong(var0, 0);
      } else {
         add_ulong(var0, var1.length() + 1);

         for(int var2 = 0; var2 < var1.length(); ++var2) {
            add_byte(var0, (byte)var1.charAt(var2));
         }

         add_byte(var0, (byte)0);
      }
   }

   static {
      initialRefIdMap.put("FactoryFinder", "IDL:beasys.com/Tobj/FactoryFinder:1.0");
      initialRefIdMap.put("InterfaceRepository", "IDL:omg.org/CORBA/Repository:1.0");
      initialRefIdMap.put("Tobj_SimpleEventsService", "IDL:beasys.com/TobjI_SimpleEvents/ChannelFactory:1.0");
      initialRefIdMap.put("NotificationService", "IDL:beasys.com/TobjI_Notification/EventChannelFactory:1.0");
      initialRefIdMap.put("NameService", "IDL:omg.org/CosNaming/NamingContextExt:1.0");
      initialRefOidMap = new HashMap();
      initialRefOidMap.put("FactoryFinder", "FactoryFinder");
      initialRefOidMap.put("InterfaceRepository", "Repository");
      initialRefOidMap.put("Tobj_SimpleEventsService", "Tobj_SimpleEventsService");
      initialRefOidMap.put("NotificationService", "NotificationService");
      initialRefOidMap.put("NameService", "BEA:NameService:Root");
      initialRefOaIdMap = new HashMap();
      initialRefOaIdMap.put("FactoryFinder", new Integer(3));
      initialRefOaIdMap.put("InterfaceRepository", new Integer(2));
      initialRefOaIdMap.put("Tobj_SimpleEventsService", new Integer(5));
      initialRefOaIdMap.put("NotificationService", new Integer(5));
      initialRefOaIdMap.put("NameService", new Integer(7));
   }
}
