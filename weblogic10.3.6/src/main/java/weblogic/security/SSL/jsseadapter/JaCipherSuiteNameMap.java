package weblogic.security.SSL.jsseadapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

final class JaCipherSuiteNameMap {
   private static final String SYSPROPNAME_DISABLE_CIPHERSUITE_ALIASES = "weblogic.security.SSL.disableJsseCipherSuiteAliases";
   private static final boolean DISABLE_CIPHERSUITE_ALIASES = Boolean.getBoolean("weblogic.security.SSL.disableJsseCipherSuiteAliases");
   private static final Map<String, String> nameMap_toJsse;
   private static final Map<String, String> nameMap_fromJsse;
   private static final int NAMEMAP_FROMJSSE_SIZE;

   static final String[] toJsse(String[] var0) {
      if (DISABLE_CIPHERSUITE_ALIASES) {
         return var0;
      } else if (null == var0) {
         throw new IllegalArgumentException("Non-null cipherSuiteNames expected.");
      } else if (0 == var0.length) {
         return new String[0];
      } else {
         ArrayList var1 = new ArrayList(var0.length);
         String[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if (null != var5) {
               String var6 = var5.trim();
               if (0 != var6.length()) {
                  String var7 = (String)nameMap_toJsse.get(var6);
                  String var8;
                  if (null == var7) {
                     var8 = var6;
                  } else {
                     var8 = var7;
                  }

                  if (!var1.contains(var8)) {
                     var1.add(var8);
                  }
               }
            }
         }

         var2 = (String[])var1.toArray(new String[0]);
         return var2;
      }
   }

   static final String[] fromJsse(String[] var0) {
      if (DISABLE_CIPHERSUITE_ALIASES) {
         return var0;
      } else if (null == var0) {
         throw new IllegalArgumentException("Non-null jsseCipherSuiteNames expected.");
      } else if (0 == var0.length) {
         return new String[0];
      } else {
         ArrayList var1 = new ArrayList(var0.length + NAMEMAP_FROMJSSE_SIZE);
         String[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if (null != var5) {
               String var6 = var5.trim();
               if (0 != var6.length()) {
                  if (!var1.contains(var6)) {
                     var1.add(var6);
                  }

                  String var7 = (String)nameMap_fromJsse.get(var6);
                  if (null != var7 && !var1.contains(var7)) {
                     var1.add(var7);
                  }
               }
            }
         }

         var2 = (String[])var1.toArray(new String[0]);
         return var2;
      }
   }

   static {
      HashMap var0 = new HashMap();
      var0.put("TLS_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA", "SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA");
      var0.put("TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA", "SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA");
      var0.put("TLS_DHE_DSS_WITH_DES_CBC_SHA", "SSL_DHE_DSS_WITH_DES_CBC_SHA");
      var0.put("TLS_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA", "SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA");
      var0.put("TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA", "SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA");
      var0.put("TLS_DHE_RSA_WITH_DES_CBC_SHA", "SSL_DHE_RSA_WITH_DES_CBC_SHA");
      var0.put("TLS_DH_anon_EXPORT_WITH_DES40_CBC_SHA", "SSL_DH_anon_EXPORT_WITH_DES40_CBC_SHA");
      var0.put("TLS_DH_anon_EXPORT_WITH_RC4_40_MD5", "SSL_DH_anon_EXPORT_WITH_RC4_40_MD5");
      var0.put("TLS_DH_anon_WITH_3DES_EDE_CBC_SHA", "SSL_DH_anon_WITH_3DES_EDE_CBC_SHA");
      var0.put("TLS_DH_anon_WITH_DES_CBC_SHA", "SSL_DH_anon_WITH_DES_CBC_SHA");
      var0.put("TLS_DH_anon_WITH_RC4_128_MD5", "SSL_DH_anon_WITH_RC4_128_MD5");
      var0.put("TLS_RSA_EXPORT_WITH_DES40_CBC_SHA", "SSL_RSA_EXPORT_WITH_DES40_CBC_SHA");
      var0.put("TLS_RSA_EXPORT_WITH_RC4_40_MD5", "SSL_RSA_EXPORT_WITH_RC4_40_MD5");
      var0.put("TLS_RSA_WITH_3DES_EDE_CBC_SHA", "SSL_RSA_WITH_3DES_EDE_CBC_SHA");
      var0.put("TLS_RSA_WITH_DES_CBC_SHA", "SSL_RSA_WITH_DES_CBC_SHA");
      var0.put("TLS_RSA_WITH_RC4_128_MD5", "SSL_RSA_WITH_RC4_128_MD5");
      var0.put("TLS_RSA_WITH_RC4_128_SHA", "SSL_RSA_WITH_RC4_128_SHA");
      nameMap_toJsse = Collections.unmodifiableMap(var0);
      HashMap var1 = new HashMap();
      Iterator var2 = nameMap_toJsse.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         var1.put(var3.getValue(), var3.getKey());
      }

      nameMap_fromJsse = Collections.unmodifiableMap(var1);
      NAMEMAP_FROMJSSE_SIZE = nameMap_fromJsse.size();
   }
}
