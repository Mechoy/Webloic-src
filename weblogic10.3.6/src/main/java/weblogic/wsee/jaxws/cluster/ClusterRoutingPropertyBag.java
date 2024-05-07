package weblogic.wsee.jaxws.cluster;

import com.sun.xml.ws.api.PropertySet;
import com.sun.xml.ws.api.PropertySet.Property;
import weblogic.wsee.jaxws.framework.PropertySetUtil;

public class ClusterRoutingPropertyBag extends PropertySet {
   public static final String UNCORRELATED_ASYNC_RESPONSE = "weblogic.wsee.jaxws.cluster.UncorrelatedAsyncResponse";
   public static PropertySetUtil.PropertySetRetriever<ClusterRoutingPropertyBag> propertySetRetriever = PropertySetUtil.getRetriever(ClusterRoutingPropertyBag.class);
   private static final PropertySet.PropertyMap model = parse(ClusterRoutingPropertyBag.class);
   private boolean _uncorrelatedAsyncResponse = false;

   protected PropertySet.PropertyMap getPropertyMap() {
      return model;
   }

   @Property({"weblogic.wsee.jaxws.cluster.UncorrelatedAsyncResponse"})
   public boolean getUncorrelatedAsyncResponse() {
      return this._uncorrelatedAsyncResponse;
   }

   public void setUncorrelatedAsyncResponse(boolean var1) {
      this._uncorrelatedAsyncResponse = var1;
   }
}
