package weblogic.jms.dotnet.transport.t3client;

class T3 {
   static String PROTOCOL_NAME = "t3";
   static String PROTOCOL_DELIMITER = "\n";
   static String PROTOCOL_ABBV = "AS";
   static String PROTOCOL_ABBV_DELIMITER = ":";
   static int PROTOCOL_ABBV_SIZE = 255;
   static String PROTOCOL_HDR = "HL";
   static String PROTOCOL_HDR_DELIMITER = ":";
   static int PROTOCOL_HDR_SIZE = 19;
   static String PROTOCOL_REPLY_OK = "HELO";
   static String PROTOCOL_REPLY_OK_DELIMITER = ":";
   static String PROTOCOL_REPLY_DELIMITER = "\n";
   static byte PROTOCOL_IDENTIFY_REQUEST_NO_PEERINFO = 12;
   static byte PROTOCOL_IDENTIFY_RESPONSE_NO_PEERINFO = 13;
   static byte PROTOCOL_ONE_WAY = 4;
   static byte PROTOCOL_REQUEST = 5;
   static byte PROTOCOL_QOS_ANY = 101;
   static byte PROTOCOL_QOS_SECURE = 102;
   static byte PROTOCOL_FLAG_JVMID = 1;
   static byte PROTOCOL_FLAG_NONE = 0;
   static int PROTOCOL_RESPONSEID_NONE = -1;
   static int PROTOCOL_RESPONSEID = 1;
   static int PROTOCOL_INVOKEID_NONE = -1;
   static int PROTOCOL_INVOKEID_JMSCSHARP_SERVICE = 41;
   static int PROTOCOL_HEARTBEAT_DISABLE = 0;

   static void writeLength(MarshalWriterImpl var0, int var1) {
      if (var1 >= 0) {
         if (var1 < 254) {
            var0.write(var1);
            return;
         }

         if (var1 <= 65535) {
            var0.write(254);
            var0.write(var1 >> 8);
            var0.write(var1 & 255);
            return;
         }
      }

      var0.write(255);
      var0.write(var1 >>> 24);
      var0.write(var1 >>> 16);
      var0.write(var1 >>> 8);
      var0.write(var1 >>> 0);
   }

   static int getLengthNumBytes(int var0) {
      if (var0 >= 0) {
         if (var0 < 254) {
            return 1;
         }

         if (var0 <= 65535) {
            return 3;
         }
      }

      return 5;
   }
}
