package weblogic.messaging.saf.utils;

import java.io.StreamCorruptedException;

public final class SAFClientUtil {
   public static StreamCorruptedException versionIOException(int var0, int var1, int var2) {
      return new StreamCorruptedException("Unsupported class version " + var0 + "." + "  Expected a value between " + var1 + " and " + var2 + " inclusive." + (var0 > var1 ? "  Possible attempt to access newer version then current version." : "  Possible attempt to access unsupported older version."));
   }
}
