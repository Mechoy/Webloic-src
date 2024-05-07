package weblogic.wsee.jaxws.tubeline;

import com.sun.xml.ws.api.Component;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.ContinuationCloner;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import java.util.Map;

public class FlowControlTube extends AbstractTubeImpl {
   private Tube inner;
   private boolean prot = false;

   public FlowControlTube(Tube var1) {
      this.inner = var1;
      this.prot = !(var1 instanceof FlowControlAware) || var1.getClass().getAnnotation(FlowControlProtected.class) != null;
   }

   protected FlowControlTube(FlowControlTube var1, TubeCloner var2) {
      super(var1, var2);
      if (var2 instanceof ContinuationCloner) {
         ((ContinuationCloner)var2).setContinuationObserver(new ContinuationCloner.ContinuationObserver() {
            public Tube notify(Tube var1) {
               return new FlowControlTube(var1);
            }
         });
      }

      this.inner = var2.copy(var1.inner);
      this.prot = var1.prot;
   }

   public FlowControlTube copy(TubeCloner var1) {
      return new FlowControlTube(this, var1);
   }

   public void preDestroy() {
      this.inner.preDestroy();
   }

   public NextAction processException(Throwable var1) {
      FlowControlImpl var2 = this.initialize();
      if (!this.prot && var2.isSkip) {
         return this.doThrow(var1);
      } else {
         NextAction var3 = this.inner.processException(var1);
         Tube var4 = var3.getNext();
         if (var4 != null) {
            var3.setNext(new FlowControlTube(var4));
         }

         return var3;
      }
   }

   public NextAction processRequest(Packet var1) {
      this.initialize();
      NextAction var2 = this.inner.processRequest(var1);
      Tube var3 = var2.getNext();
      if (var3 != null) {
         var2.setNext(new FlowControlTube(var3));
      }

      return var2;
   }

   public NextAction processResponse(Packet var1) {
      FlowControlImpl var2 = this.initialize();
      if (!this.prot && var2.isSkip) {
         return this.doReturnWith(var1);
      } else {
         NextAction var3 = this.inner.processResponse(var1);
         Tube var4 = var3.getNext();
         if (var4 != null) {
            var3.setNext(new FlowControlTube(var4));
         }

         return var3;
      }
   }

   public String toString() {
      return this.inner.toString();
   }

   private FlowControlImpl initialize() {
      Map var1 = Fiber.current().getComponentRegistry();
      FlowControlComponent var2 = (FlowControlComponent)var1.get(FlowControlComponent.class);
      if (var2 == null) {
         var2 = new FlowControlComponent();
         var1.put(FlowControlComponent.class, var2);
      }

      return var2.control;
   }

   private static class FlowControlImpl implements FlowControl {
      private boolean isSkip;

      private FlowControlImpl() {
         this.isSkip = false;
      }

      public void doSkip() {
         this.isSkip = true;
      }

      // $FF: synthetic method
      FlowControlImpl(Object var1) {
         this();
      }
   }

   private static class FlowControlComponent implements Component {
      private final FlowControlImpl control;

      private FlowControlComponent() {
         this.control = new FlowControlImpl();
      }

      public <T> T getSPI(Class<T> var1) {
         return var1 == FlowControl.class ? var1.cast(this.control) : null;
      }

      // $FF: synthetic method
      FlowControlComponent(Object var1) {
         this();
      }
   }
}
