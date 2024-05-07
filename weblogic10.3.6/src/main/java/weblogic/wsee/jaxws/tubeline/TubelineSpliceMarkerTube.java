package weblogic.wsee.jaxws.tubeline;

import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.ContinuationCloner;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;

public class TubelineSpliceMarkerTube extends AbstractTubeImpl {
   private Tube delegate;

   public TubelineSpliceMarkerTube(Tube var1) {
      this.delegate = var1;
   }

   private TubelineSpliceMarkerTube(TubelineSpliceMarkerTube var1, TubeCloner var2) {
      super(var1, var2);
      this.delegate = var2.copy(var1.delegate);
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      if (var1 instanceof ContinuationCloner) {
         ContinuationCloner var2 = (ContinuationCloner)var1;
         if (var2.isSplicing()) {
            if (var2.isLead()) {
               var2.dropLast();
            }

            NullTube var3 = new NullTube();
            var2.add(var3, var3);
            return var3;
         }
      }

      return new TubelineSpliceMarkerTube(this, var1);
   }

   public void preDestroy() {
      this.delegate.preDestroy();
   }

   public NextAction processException(Throwable var1) {
      return this.delegate.processException(var1);
   }

   public NextAction processRequest(Packet var1) {
      return this.delegate.processRequest(var1);
   }

   public NextAction processResponse(Packet var1) {
      return this.delegate.processResponse(var1);
   }

   public String toString() {
      return this.delegate.toString();
   }

   private static class NullTube extends AbstractTubeImpl {
      NullTube() {
      }

      NullTube(NullTube var1, TubeCloner var2) {
         super(var1, var2);
      }

      public AbstractTubeImpl copy(TubeCloner var1) {
         return new NullTube(this, var1);
      }

      public void preDestroy() {
      }

      public NextAction processException(Throwable var1) {
         return this.doThrow(var1);
      }

      public NextAction processRequest(Packet var1) {
         throw new IllegalStateException("NullTube only for use with ResponseOnlyTube behavior...");
      }

      public NextAction processResponse(Packet var1) {
         return this.doReturnWith(var1);
      }
   }
}
