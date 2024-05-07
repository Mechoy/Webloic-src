package weblogic.wsee.mc.tube;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.EndpointAddress;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.WSService;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.message.StringHeader;
import com.sun.xml.ws.server.WSEndpointImpl;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import org.w3c.dom.Element;
import weblogic.kernel.KernelStatus;
import weblogic.management.runtime.WseeBasePortRuntimeMBean;
import weblogic.management.runtime.WseeV2RuntimeMBean;
import weblogic.wsee.mc.mbean.WseeMcRuntimeMBeanImpl;
import weblogic.wsee.mc.utils.McConstants;
import weblogic.wsee.mc.utils.McProtocolUtils;
import weblogic.wsee.monitoring.WseePortRuntimeMBeanImpl;

public class McTubeUtils {
   private static String ANON_URI_TEMPLATE;
   private WSEndpoint _endpoint;
   private WSService _service;
   private WSBinding _binding;

   public McTubeUtils(@Nullable WSEndpoint var1, @Nullable WSService var2, @NotNull WSBinding var3) {
      this._endpoint = var1;
      this._binding = var3;
      this._service = var2;
   }

   public static WSEndpointReference getEndpointReferenceFromIncomingPacket(@NotNull Packet var0, @NotNull WSEndpointImpl var1) {
      String var2 = var0.webServiceContextDelegate.getEPRAddress(var0, var1);
      String var3 = null;
      if (var1.getServiceDefinition() != null) {
         var3 = var0.webServiceContextDelegate.getWSDLAddress(var0, var1);
      }

      Class var4 = W3CEndpointReference.class;
      return var1.getWSEndpointReference(var4, var2, var3, new Element[0]);
   }

   public static void setMessageID(Message var0, AddressingVersion var1, SOAPVersion var2) {
      String var3 = var0.getHeaders().getMessageID(var1, var2);
      if (var3 == null) {
         var3 = McProtocolUtils.generateUUID();
         StringHeader var4 = new StringHeader(var1.messageIDTag, var3);
         var0.getHeaders().add(var4);
      }

   }

   public static boolean isMcAnonURI(WSEndpointReference var0) {
      if (var0 == null) {
         return false;
      } else {
         String var1 = var0.getAddress();
         return var1.startsWith(ANON_URI_TEMPLATE) && var1.length() > ANON_URI_TEMPLATE.length();
      }
   }

   public static boolean isMcAnonURI(EndpointAddress var0) {
      if (var0 == null) {
         return false;
      } else {
         String var1 = var0.toString();
         return var1.startsWith(ANON_URI_TEMPLATE) && var1.length() > ANON_URI_TEMPLATE.length();
      }
   }

   public static boolean isMcAnonURI(String var0) {
      if (var0 == null) {
         return false;
      } else {
         return var0.startsWith(ANON_URI_TEMPLATE) && var0.length() > ANON_URI_TEMPLATE.length();
      }
   }

   public static String getUUID(WSEndpointReference var0) {
      String var1 = var0.getAddress();
      return var1.substring(ANON_URI_TEMPLATE.length());
   }

   public static String getUUID(EndpointAddress var0) {
      String var1 = var0.toString();
      return var1.substring(ANON_URI_TEMPLATE.length());
   }

   public static String getUUID(String var0) {
      return var0.substring(ANON_URI_TEMPLATE.length());
   }

   public static String getUriPattern(@NotNull Packet var0) {
      WseeV2RuntimeMBean var1 = (WseeV2RuntimeMBean)var0.component.getSPI(WseeV2RuntimeMBean.class);
      return var1 != null ? var1.getURI() : null;
   }

   public static String getUriPattern(@NotNull WSEndpoint var0) {
      WseeV2RuntimeMBean var1 = (WseeV2RuntimeMBean)var0.getSPI(WseeV2RuntimeMBean.class);
      return var1 != null ? var1.getURI() : null;
   }

   public static String getUniqueName(@NotNull Packet var0) {
      WseeV2RuntimeMBean var1 = (WseeV2RuntimeMBean)var0.component.getSPI(WseeV2RuntimeMBean.class);
      return var1 != null ? var1.getName() : null;
   }

   @Nullable
   public static WseeMcRuntimeMBeanImpl getMcRuntimeFromPacket(Packet var0) {
      if (KernelStatus.isServer()) {
         WseeBasePortRuntimeMBean var1 = WseePortRuntimeMBeanImpl.getFromPacket(var0);
         WseeMcRuntimeMBeanImpl var2 = getMcRuntimeFromPortRuntime(var1);
         return var2;
      } else {
         return null;
      }
   }

   private static WseeMcRuntimeMBeanImpl getMcRuntimeFromPortRuntime(WseeBasePortRuntimeMBean var0) {
      WseeMcRuntimeMBeanImpl var1 = null;

      try {
         if (var0 == null) {
            throw new IllegalStateException("Couldn't find WseePortRuntimeMBean for port");
         }

         var1 = (WseeMcRuntimeMBeanImpl)var0.getMc();
      } catch (Exception var3) {
      }

      return var1;
   }

   static {
      ANON_URI_TEMPLATE = McConstants.getAnonymousURITemplate(McConstants.McVersion.MC_11);
   }
}
