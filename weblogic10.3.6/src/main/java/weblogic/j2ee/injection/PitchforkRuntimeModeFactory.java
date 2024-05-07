package weblogic.j2ee.injection;

import com.oracle.pitchfork.interfaces.PitchforkRuntimeMode;

public final class PitchforkRuntimeModeFactory {
   private PitchforkRuntimeModeFactory() {
   }

   public static PitchforkRuntimeMode createPitchforkRuntimeMode(String var0) {
      return new DefaultPitchforkRuntimeModeImpl(var0);
   }
}
