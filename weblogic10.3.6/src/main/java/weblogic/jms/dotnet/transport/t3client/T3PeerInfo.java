package weblogic.jms.dotnet.transport.t3client;

import java.util.StringTokenizer;

public class T3PeerInfo {
   public static int DEFAULT_PROTOCOL_VERSION_MAJOR = 10;
   public static int DEFAULT_PROTOCOL_VERSION_MINOR = 0;
   public static int DEFAULT_PROTOCOL_VERSION_SERVICEPACK = 0;
   public static int DEFAULT_PROTOCOL_VERSION_ROLLINGPATCH = 0;
   public static boolean DEFAULT_PROTOCOL_VERSION_TEMPORARYPATCH = false;
   public static T3PeerInfo defaultPeerInfo;
   private int major;
   private int minor;
   private int servicepack;
   private int rollingpatch;
   private boolean temporarypatch;

   T3PeerInfo(int var1, int var2, int var3, int var4, boolean var5) {
      this.major = var1;
      this.minor = var2;
      this.servicepack = var3;
      this.rollingpatch = var4;
      this.temporarypatch = var5;
   }

   T3PeerInfo(String var1) throws Exception {
      StringTokenizer var2 = new StringTokenizer(var1, ".");
      if (var2.countTokens() != 5) {
         throw new Exception("Unknown version syntax " + var1);
      } else {
         int var3 = 0;
         boolean var4 = false;
         boolean var5 = false;
         boolean var6 = false;
         boolean var7 = false;
         boolean var8 = false;

         while(var2.hasMoreElements()) {
            ++var3;
            String var9 = var2.nextToken();
            switch (var3) {
               case 1:
                  int var10 = Integer.parseInt(var9);
                  break;
               case 2:
                  int var11 = Integer.parseInt(var9);
                  break;
               case 3:
                  int var12 = Integer.parseInt(var9);
                  break;
               case 4:
                  int var13 = Integer.parseInt(var9);
                  break;
               case 5:
                  var8 = new Boolean(var9);
            }
         }

      }
   }

   public int getMajor() {
      return this.major;
   }

   public int getMinor() {
      return this.minor;
   }

   public int getServicePack() {
      return this.servicepack;
   }

   public int getRollingPatch() {
      return this.rollingpatch;
   }

   public boolean isTemporaryPatch() {
      return this.temporarypatch;
   }

   public String getVersion() {
      return "" + this.major + "." + this.minor + "." + this.servicepack + "." + this.rollingpatch;
   }

   public void write(MarshalWriterImpl var1) {
      var1.writeInt(this.major);
      var1.writeInt(this.minor);
      var1.writeInt(this.servicepack);
      var1.writeInt(this.rollingpatch);
      var1.writeBoolean(this.temporarypatch);
   }

   public String getDefaultVersion() {
      return DEFAULT_PROTOCOL_VERSION_MAJOR + "." + DEFAULT_PROTOCOL_VERSION_MINOR + "." + DEFAULT_PROTOCOL_VERSION_SERVICEPACK + "." + DEFAULT_PROTOCOL_VERSION_ROLLINGPATCH;
   }

   static {
      defaultPeerInfo = new T3PeerInfo(DEFAULT_PROTOCOL_VERSION_MAJOR, DEFAULT_PROTOCOL_VERSION_MINOR, DEFAULT_PROTOCOL_VERSION_SERVICEPACK, DEFAULT_PROTOCOL_VERSION_ROLLINGPATCH, DEFAULT_PROTOCOL_VERSION_TEMPORARYPATCH);
   }
}
