package weblogic.wsee.cluster;

import java.io.Serializable;

public interface ClusterService {
   String getTargetURI();

   Serializable dispatch(Serializable var1) throws ClusterServiceException;
}
