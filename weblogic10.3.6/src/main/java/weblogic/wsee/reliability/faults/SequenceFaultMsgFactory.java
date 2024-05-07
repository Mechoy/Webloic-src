package weblogic.wsee.reliability.faults;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.WsrmProtocolUtils;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class SequenceFaultMsgFactory {
   private static final SequenceFaultMsgFactory INSTANCE = new SequenceFaultMsgFactory();
   private final Map<QName, Class> faultClasses = new HashMap();
   private final Map<Integer, QName> safCodeMapping = new HashMap();

   private SequenceFaultMsgFactory() {
   }

   public static SequenceFaultMsgFactory getInstance() {
      return INSTANCE;
   }

   public SequenceFaultMsg createSequenceFaultMsg(QName var1, WsrmConstants.RMVersion var2) throws SequenceFaultException {
      Class var3 = (Class)this.faultClasses.get(var1);
      if (var3 == null) {
         throw new SequenceFaultException("unknown fault: " + var1);
      } else {
         try {
            Constructor var4 = var3.getConstructor(WsrmConstants.RMVersion.class);
            return (SequenceFaultMsg)var4.newInstance(var2);
         } catch (InvocationTargetException var5) {
            throw new AssertionError(var5);
         } catch (NoSuchMethodException var6) {
            throw new AssertionError(var6);
         } catch (IllegalAccessException var7) {
            throw new AssertionError(var7);
         } catch (InstantiationException var8) {
            throw new AssertionError(var8);
         }
      }
   }

   public SequenceFaultMsg parseSoapFault(SOAPMessage var1, WsrmConstants.RMVersion var2) throws SequenceFaultException {
      return this.parseSoapFault(var1, new WsrmConstants.RMVersion[]{var2});
   }

   public SequenceFaultMsg parseSoapFault(SOAPMessage var1) throws SequenceFaultException {
      return this.parseSoapFault(var1, WsrmConstants.RMVersion.values());
   }

   public SequenceFaultMsg parseSoapFault(SOAPMessage var1, WsrmConstants.RMVersion[] var2) throws SequenceFaultException {
      if (var1 == null) {
         throw new SequenceFaultException("SOAPMessage is null");
      } else {
         try {
            SOAPHeader var3 = var1.getSOAPHeader();
            if (var3 == null) {
               throw new SequenceFaultException("SOAPHeader is null");
            } else {
               SOAPEnvelope var4 = var1.getSOAPPart().getEnvelope();
               String var5 = var4.getNamespaceURI();
               WsrmConstants.SOAPVersion var6 = WsrmProtocolUtils.getSOAPVersionFromNamespaceUri(var5);
               WsrmConstants.RMVersion var8 = null;
               QName var7;
               int var13;
               if (var6 == WsrmConstants.SOAPVersion.SOAP_11) {
                  Element var9 = null;
                  Element var10 = null;
                  WsrmConstants.RMVersion[] var11 = var2;
                  int var12 = var2.length;

                  for(var13 = 0; var13 < var12; ++var13) {
                     WsrmConstants.RMVersion var14 = var11[var13];
                     Element var15 = this.getOptionalElementByTagNameNS(var3, WsrmConstants.Element.SEQUENCE_FAULT.getElementName(), var14);
                     if (var15 != null) {
                        var9 = var15;
                        var8 = var14;
                        var10 = this.getOptionalElementByTagNameNS(var15, WsrmConstants.Element.FAULT_CODE.getElementName(), var14);
                     }
                  }

                  if (var10 == null) {
                     throw new SequenceFaultException("No valid WS-RM FaultCode element could be found for an WS-RM version");
                  }

                  String var21 = DOMUtils.getTextContent(var10, true);
                  var12 = -1;
                  if (var21 != null) {
                     var12 = var21.indexOf(":");
                  }

                  if (var12 < 0 || var21 == null) {
                     throw new SequenceFaultException("WS-RM FaultCode contained invalid content: " + var21);
                  }

                  String var24 = var21.substring(0, var12);
                  String var25 = var9.lookupNamespaceURI(var24);
                  if (var25 == null || !var25.equals(var8.getNamespaceUri())) {
                     throw new SequenceFaultException("WS-RM FaultCode contained subcode with unknown or incorrect namespace prefix: " + var24 + " mapped to namespace URI: " + var25);
                  }

                  String var27 = var21.substring(var12 + 1);
                  var7 = new QName(var8.getNamespaceUri(), var27, var24);
               } else {
                  SOAPBody var18 = var1.getSOAPBody();
                  SOAPFault var20 = var18.getFault();
                  Iterator var22 = var20.getFaultSubcodes();
                  if (!var22.hasNext()) {
                     throw new SequenceFaultException("No fault code");
                  }

                  var7 = (QName)var22.next();
                  WsrmConstants.RMVersion[] var23 = var2;
                  var13 = var2.length;

                  for(int var26 = 0; var26 < var13; ++var26) {
                     WsrmConstants.RMVersion var28 = var23[var26];
                     if (var28.getNamespaceUri().equals(var7.getNamespaceURI())) {
                        var8 = var28;
                        break;
                     }
                  }
               }

               SequenceFaultMsg var19 = this.createSequenceFaultMsg(var7, var8);
               var19.read(var1);
               return var19;
            }
         } catch (DOMProcessingException var16) {
            throw new SequenceFaultException("DOMProcessingException", var16);
         } catch (SOAPException var17) {
            throw new SequenceFaultException("SOAPException", var17);
         }
      }
   }

   private Element getOptionalElementByTagNameNS(Element var1, String var2, WsrmConstants.RMVersion var3) throws DOMProcessingException {
      return DOMUtils.getOptionalElementByTagNameNS(var1, var3.getNamespaceUri(), var2);
   }

   public QName getSAFResultCodeMapping(int var1) {
      return (QName)this.safCodeMapping.get(var1);
   }

   private void addSAFResultCodeMapping(int var1, QName var2) {
      this.safCodeMapping.put(var1, var2);
   }

   private void addFaultClass(QName var1, Class var2) {
      this.faultClasses.put(var1, var2);
   }

   static {
      WsrmConstants.RMVersion[] var0 = WsrmConstants.RMVersion.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         WsrmConstants.RMVersion var3 = var0[var2];
         INSTANCE.addSAFResultCodeMapping(6, (new UnknownSequenceFaultMsg(var3)).getSubCodeQName());
         INSTANCE.addSAFResultCodeMapping(7, (new SequenceRefusedFaultMsg(var3)).getSubCodeQName());
         INSTANCE.addSAFResultCodeMapping(5, (new SequenceTerminatedFaultMsg(var3)).getSubCodeQName());
         INSTANCE.addFaultClass((new UnknownSequenceFaultMsg(var3)).getSubCodeQName(), UnknownSequenceFaultMsg.class);
         INSTANCE.addFaultClass((new SequenceRefusedFaultMsg(var3)).getSubCodeQName(), SequenceRefusedFaultMsg.class);
         INSTANCE.addFaultClass((new MessageNumRolloverFaultMsg(var3)).getSubCodeQName(), MessageNumRolloverFaultMsg.class);
         INSTANCE.addFaultClass((new LastMessageNumExceededFaultMsg(var3)).getSubCodeQName(), LastMessageNumExceededFaultMsg.class);
         INSTANCE.addFaultClass((new SequenceTerminatedFaultMsg(var3)).getSubCodeQName(), SequenceTerminatedFaultMsg.class);
         INSTANCE.addFaultClass((new SequenceClosedFaultMsg(var3)).getSubCodeQName(), SequenceClosedFaultMsg.class);
         INSTANCE.addFaultClass((new InvalidAckFaultMsg(var3)).getSubCodeQName(), InvalidAckFaultMsg.class);
         INSTANCE.addFaultClass((new IllegalRMVersionFaultMsg(var3)).getSubCodeQName(), IllegalRMVersionFaultMsg.class);
         INSTANCE.addFaultClass((new SecurityMismatchFaultMsg(var3)).getSubCodeQName(), SecurityMismatchFaultMsg.class);
         INSTANCE.addFaultClass((new WSRMRequiredFaultMsg(var3)).getSubCodeQName(), WSRMRequiredFaultMsg.class);
      }

   }
}
