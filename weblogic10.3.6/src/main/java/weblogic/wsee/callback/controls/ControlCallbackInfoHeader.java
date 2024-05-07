package weblogic.wsee.callback.controls;

import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class ControlCallbackInfoHeader extends MsgHeader {
   private ControlCallbackReferenceData controlCallbackReferenceData = null;
   public static final QName NAME;
   public static final MsgHeaderType TYPE;

   public ControlCallbackInfoHeader() {
   }

   public ControlCallbackInfoHeader(ControlCallbackReferenceData var1) {
      this.controlCallbackReferenceData = var1;
   }

   public QName getName() {
      return NAME;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public ControlCallbackReferenceData getControlCallbackReferenceData() {
      return this.controlCallbackReferenceData;
   }

   public void write(Element var1) throws MsgHeaderException {
      if (this.controlCallbackReferenceData != null) {
         Set var2 = this.controlCallbackReferenceData.keySet();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            String var5 = this.controlCallbackReferenceData.get(var4);
            if (var5 != null) {
               DOMUtils.addValueNS(var1, "http://www.bea.com/2005/08/controlcallback", "controlcallback:" + var4, var5);
            }
         }
      }

   }

   public void read(Element var1) throws MsgHeaderException {
      Object var2 = null;

      try {
         this.controlCallbackReferenceData = getElementValuesByNS(var1, "http://www.bea.com/2005/08/controlcallback");
      } catch (DOMProcessingException var4) {
         throw new MsgHeaderException("Could not read control callback info elements: ", var4);
      }
   }

   private static ControlCallbackReferenceData getElementValuesByNS(Element var0, String var1) throws DOMProcessingException {
      if (var0 == null) {
         return null;
      } else if (var1 != null && var1.trim().length() >= 1) {
         GenericControlCallbackReferenceData var2 = new GenericControlCallbackReferenceData();
         Node var3 = var0.getFirstChild();

         int var4;
         for(var4 = 0; var3 != null; var3 = var3.getNextSibling()) {
            if (var3 instanceof Element) {
               Element var5 = (Element)var3;
               if (var1.equals(var5.getNamespaceURI())) {
                  String var6 = var5.getLocalName();
                  String var7 = DOMUtils.getTextData(var5);
                  if (var7 == null) {
                     var7 = "";
                  }

                  if (var6 != null) {
                     var2.put(var6, var7);
                     ++var4;
                  }
               }
            }
         }

         if (var4 == 0) {
            return null;
         } else {
            return var2;
         }
      } else {
         return null;
      }
   }

   static {
      NAME = ControlCallbackConstants.CONTROL_CALLBACK_INFO_HEADER;
      TYPE = new MsgHeaderType();
   }
}
