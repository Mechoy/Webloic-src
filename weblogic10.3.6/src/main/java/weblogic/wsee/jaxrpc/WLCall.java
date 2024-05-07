package weblogic.wsee.jaxrpc;

import java.util.Iterator;
import javax.xml.rpc.Call;
import javax.xml.rpc.ParameterMode;

public interface WLCall extends Call {
   String RUNTIME_BINDING_PROVIDER = "weblogic.wsee.bind.runtimeBindingProvider";

   Iterator getParameterNames();

   Class getParameterJavaType(String var1);

   ParameterMode getParameterMode(String var1);
}
