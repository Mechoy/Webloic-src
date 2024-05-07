package weblogic.wsee.jaxws.cluster.spi;

public interface AffinityBasedRoutingInfoFinder extends RoutingInfoFinder {
   void recordRoutingIDAffinity(RoutingInfo var1, RoutingInfo var2);
}
