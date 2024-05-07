package weblogic.corba.iiop.cluster;

import java.util.HashMap;

public class SelectorFactory {
   public static final Selector getSelector(String var0) {
      Selector var1 = (Selector)SelectorFactory.SelectorMaker.SELECTOR_MAP.get(var0);
      return (Selector)(var1 != null ? var1 : StickySelector.SELECTOR);
   }

   private static final HashMap getSelectorMap() {
      HashMap var0 = new HashMap();
      var0.put("random", RandomSelector.SELECTOR);
      var0.put("round-robin", RoundRobinSelector.SELECTOR);
      var0.put("round-robin-affinity", ServerAffinitySelector.SELECTOR);
      var0.put("random-affinity", ServerAffinitySelector.SELECTOR);
      var0.put("server-affinity", ServerAffinitySelector.SELECTOR);
      var0.put("weight-based-affinity", ServerAffinitySelector.SELECTOR);
      var0.put("primary-secondary", PrimarySecondarySelector.SELECTOR);
      return var0;
   }

   private static final class SelectorMaker {
      private static final HashMap SELECTOR_MAP = SelectorFactory.getSelectorMap();
   }
}
