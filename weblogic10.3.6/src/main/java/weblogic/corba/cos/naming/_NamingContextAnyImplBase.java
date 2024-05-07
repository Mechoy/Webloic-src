package weblogic.corba.cos.naming;

import java.util.Hashtable;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.Object;
import org.omg.CORBA.ObjectHelper;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.CosNaming.BindingListHelper;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NameHelper;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;
import org.omg.CosNaming.NamingContextExtPackage.InvalidAddress;
import org.omg.CosNaming.NamingContextExtPackage.InvalidAddressHelper;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.CosNaming.NamingContextPackage.AlreadyBoundHelper;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.InvalidNameHelper;
import org.omg.CosNaming.NamingContextPackage.NotEmpty;
import org.omg.CosNaming.NamingContextPackage.NotEmptyHelper;
import weblogic.corba.cos.naming.NamingContextAnyPackage.CannotProceed;
import weblogic.corba.cos.naming.NamingContextAnyPackage.CannotProceedHelper;
import weblogic.corba.cos.naming.NamingContextAnyPackage.NotFound;
import weblogic.corba.cos.naming.NamingContextAnyPackage.NotFoundHelper;
import weblogic.corba.cos.naming.NamingContextAnyPackage.TypeNotSupported;
import weblogic.corba.cos.naming.NamingContextAnyPackage.TypeNotSupportedHelper;
import weblogic.corba.cos.naming.NamingContextAnyPackage.WNameComponent;
import weblogic.corba.cos.naming.NamingContextAnyPackage.WNameHelper;
import weblogic.corba.cos.naming.NamingContextAnyPackage.WStringNameHelper;

public abstract class _NamingContextAnyImplBase extends ObjectImpl implements NamingContextAny, InvokeHandler {
   private static Hashtable _methods = new Hashtable();
   private static String[] __ids;

   public OutputStream _invoke(String var1, InputStream var2, ResponseHandler var3) {
      OutputStream var4 = null;
      Integer var5 = (Integer)_methods.get(var1);
      if (var5 == null) {
         throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
      } else {
         BindingListHolder var7;
         org.omg.CosNaming.BindingIteratorHolder var8;
         NameComponent[] var56;
         NamingContext var58;
         Object var59;
         String var61;
         String var62;
         WNameComponent[] var63;
         Any var65;
         switch (var5) {
            case 0:
               try {
                  var63 = WNameHelper.read(var2);
                  var65 = var2.read_any();
                  this.bind_any(var63, var65);
                  var4 = var3.createReply();
               } catch (NotFound var51) {
                  var4 = var3.createExceptionReply();
                  NotFoundHelper.write(var4, var51);
               } catch (CannotProceed var52) {
                  var4 = var3.createExceptionReply();
                  CannotProceedHelper.write(var4, var52);
               } catch (InvalidName var53) {
                  var4 = var3.createExceptionReply();
                  InvalidNameHelper.write(var4, var53);
               } catch (AlreadyBound var54) {
                  var4 = var3.createExceptionReply();
                  AlreadyBoundHelper.write(var4, var54);
               } catch (TypeNotSupported var55) {
                  var4 = var3.createExceptionReply();
                  TypeNotSupportedHelper.write(var4, var55);
               }
               break;
            case 1:
               try {
                  var63 = WNameHelper.read(var2);
                  var65 = var2.read_any();
                  this.rebind_any(var63, var65);
                  var4 = var3.createReply();
               } catch (NotFound var47) {
                  var4 = var3.createExceptionReply();
                  NotFoundHelper.write(var4, var47);
               } catch (CannotProceed var48) {
                  var4 = var3.createExceptionReply();
                  CannotProceedHelper.write(var4, var48);
               } catch (InvalidName var49) {
                  var4 = var3.createExceptionReply();
                  InvalidNameHelper.write(var4, var49);
               } catch (TypeNotSupported var50) {
                  var4 = var3.createExceptionReply();
                  TypeNotSupportedHelper.write(var4, var50);
               }
               break;
            case 2:
               try {
                  var63 = WNameHelper.read(var2);
                  var7 = null;
                  var65 = this.resolve_any(var63);
                  var4 = var3.createReply();
                  var4.write_any(var65);
               } catch (NotFound var44) {
                  var4 = var3.createExceptionReply();
                  NotFoundHelper.write(var4, var44);
               } catch (CannotProceed var45) {
                  var4 = var3.createExceptionReply();
                  CannotProceedHelper.write(var4, var45);
               } catch (InvalidName var46) {
                  var4 = var3.createExceptionReply();
                  InvalidNameHelper.write(var4, var46);
               }
               break;
            case 3:
               try {
                  var61 = WStringNameHelper.read(var2);
                  var7 = null;
                  var65 = this.resolve_str_any(var61);
                  var4 = var3.createReply();
                  var4.write_any(var65);
               } catch (NotFound var41) {
                  var4 = var3.createExceptionReply();
                  NotFoundHelper.write(var4, var41);
               } catch (CannotProceed var42) {
                  var4 = var3.createExceptionReply();
                  CannotProceedHelper.write(var4, var42);
               } catch (InvalidName var43) {
                  var4 = var3.createExceptionReply();
                  InvalidNameHelper.write(var4, var43);
               }
               break;
            case 4:
               try {
                  var56 = NameHelper.read(var2);
                  var7 = null;
                  var62 = this.to_string(var56);
                  var4 = var3.createReply();
                  var4.write_string(var62);
               } catch (InvalidName var40) {
                  var4 = var3.createExceptionReply();
                  InvalidNameHelper.write(var4, var40);
               }
               break;
            case 5:
               try {
                  var61 = StringNameHelper.read(var2);
                  var7 = null;
                  NameComponent[] var64 = this.to_name(var61);
                  var4 = var3.createReply();
                  NameHelper.write(var4, var64);
               } catch (InvalidName var39) {
                  var4 = var3.createExceptionReply();
                  InvalidNameHelper.write(var4, var39);
               }
               break;
            case 6:
               try {
                  var61 = AddressHelper.read(var2);
                  var62 = StringNameHelper.read(var2);
                  var8 = null;
                  String var60 = this.to_url(var61, var62);
                  var4 = var3.createReply();
                  var4.write_string(var60);
               } catch (InvalidAddress var37) {
                  var4 = var3.createExceptionReply();
                  InvalidAddressHelper.write(var4, var37);
               } catch (InvalidName var38) {
                  var4 = var3.createExceptionReply();
                  InvalidNameHelper.write(var4, var38);
               }
               break;
            case 7:
               try {
                  var61 = StringNameHelper.read(var2);
                  var7 = null;
                  var59 = this.resolve_str(var61);
                  var4 = var3.createReply();
                  ObjectHelper.write(var4, var59);
               } catch (org.omg.CosNaming.NamingContextPackage.NotFound var34) {
                  var4 = var3.createExceptionReply();
                  org.omg.CosNaming.NamingContextPackage.NotFoundHelper.write(var4, var34);
               } catch (org.omg.CosNaming.NamingContextPackage.CannotProceed var35) {
                  var4 = var3.createExceptionReply();
                  org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.write(var4, var35);
               } catch (InvalidName var36) {
                  var4 = var3.createExceptionReply();
                  InvalidNameHelper.write(var4, var36);
               }
               break;
            case 8:
               try {
                  var56 = NameHelper.read(var2);
                  var59 = ObjectHelper.read(var2);
                  this.bind(var56, var59);
                  var4 = var3.createReply();
               } catch (org.omg.CosNaming.NamingContextPackage.NotFound var30) {
                  var4 = var3.createExceptionReply();
                  org.omg.CosNaming.NamingContextPackage.NotFoundHelper.write(var4, var30);
               } catch (org.omg.CosNaming.NamingContextPackage.CannotProceed var31) {
                  var4 = var3.createExceptionReply();
                  org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.write(var4, var31);
               } catch (InvalidName var32) {
                  var4 = var3.createExceptionReply();
                  InvalidNameHelper.write(var4, var32);
               } catch (AlreadyBound var33) {
                  var4 = var3.createExceptionReply();
                  AlreadyBoundHelper.write(var4, var33);
               }
               break;
            case 9:
               try {
                  var56 = NameHelper.read(var2);
                  var59 = ObjectHelper.read(var2);
                  this.rebind(var56, var59);
                  var4 = var3.createReply();
               } catch (org.omg.CosNaming.NamingContextPackage.NotFound var27) {
                  var4 = var3.createExceptionReply();
                  org.omg.CosNaming.NamingContextPackage.NotFoundHelper.write(var4, var27);
               } catch (org.omg.CosNaming.NamingContextPackage.CannotProceed var28) {
                  var4 = var3.createExceptionReply();
                  org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.write(var4, var28);
               } catch (InvalidName var29) {
                  var4 = var3.createExceptionReply();
                  InvalidNameHelper.write(var4, var29);
               }
               break;
            case 10:
               try {
                  var56 = NameHelper.read(var2);
                  var58 = NamingContextHelper.read(var2);
                  this.bind_context(var56, var58);
                  var4 = var3.createReply();
               } catch (org.omg.CosNaming.NamingContextPackage.NotFound var23) {
                  var4 = var3.createExceptionReply();
                  org.omg.CosNaming.NamingContextPackage.NotFoundHelper.write(var4, var23);
               } catch (org.omg.CosNaming.NamingContextPackage.CannotProceed var24) {
                  var4 = var3.createExceptionReply();
                  org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.write(var4, var24);
               } catch (InvalidName var25) {
                  var4 = var3.createExceptionReply();
                  InvalidNameHelper.write(var4, var25);
               } catch (AlreadyBound var26) {
                  var4 = var3.createExceptionReply();
                  AlreadyBoundHelper.write(var4, var26);
               }
               break;
            case 11:
               try {
                  var56 = NameHelper.read(var2);
                  var58 = NamingContextHelper.read(var2);
                  this.rebind_context(var56, var58);
                  var4 = var3.createReply();
               } catch (org.omg.CosNaming.NamingContextPackage.NotFound var20) {
                  var4 = var3.createExceptionReply();
                  org.omg.CosNaming.NamingContextPackage.NotFoundHelper.write(var4, var20);
               } catch (org.omg.CosNaming.NamingContextPackage.CannotProceed var21) {
                  var4 = var3.createExceptionReply();
                  org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.write(var4, var21);
               } catch (InvalidName var22) {
                  var4 = var3.createExceptionReply();
                  InvalidNameHelper.write(var4, var22);
               }
               break;
            case 12:
               try {
                  var56 = NameHelper.read(var2);
                  var7 = null;
                  var59 = this.resolve(var56);
                  var4 = var3.createReply();
                  ObjectHelper.write(var4, var59);
               } catch (org.omg.CosNaming.NamingContextPackage.NotFound var17) {
                  var4 = var3.createExceptionReply();
                  org.omg.CosNaming.NamingContextPackage.NotFoundHelper.write(var4, var17);
               } catch (org.omg.CosNaming.NamingContextPackage.CannotProceed var18) {
                  var4 = var3.createExceptionReply();
                  org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.write(var4, var18);
               } catch (InvalidName var19) {
                  var4 = var3.createExceptionReply();
                  InvalidNameHelper.write(var4, var19);
               }
               break;
            case 13:
               try {
                  var56 = NameHelper.read(var2);
                  this.unbind(var56);
                  var4 = var3.createReply();
               } catch (org.omg.CosNaming.NamingContextPackage.NotFound var14) {
                  var4 = var3.createExceptionReply();
                  org.omg.CosNaming.NamingContextPackage.NotFoundHelper.write(var4, var14);
               } catch (org.omg.CosNaming.NamingContextPackage.CannotProceed var15) {
                  var4 = var3.createExceptionReply();
                  org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.write(var4, var15);
               } catch (InvalidName var16) {
                  var4 = var3.createExceptionReply();
                  InvalidNameHelper.write(var4, var16);
               }
               break;
            case 14:
               var56 = null;
               NamingContext var57 = this.new_context();
               var4 = var3.createReply();
               NamingContextHelper.write(var4, var57);
               break;
            case 15:
               try {
                  var56 = NameHelper.read(var2);
                  var7 = null;
                  var58 = this.bind_new_context(var56);
                  var4 = var3.createReply();
                  NamingContextHelper.write(var4, var58);
               } catch (org.omg.CosNaming.NamingContextPackage.NotFound var10) {
                  var4 = var3.createExceptionReply();
                  org.omg.CosNaming.NamingContextPackage.NotFoundHelper.write(var4, var10);
               } catch (AlreadyBound var11) {
                  var4 = var3.createExceptionReply();
                  AlreadyBoundHelper.write(var4, var11);
               } catch (org.omg.CosNaming.NamingContextPackage.CannotProceed var12) {
                  var4 = var3.createExceptionReply();
                  org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.write(var4, var12);
               } catch (InvalidName var13) {
                  var4 = var3.createExceptionReply();
                  InvalidNameHelper.write(var4, var13);
               }
               break;
            case 16:
               try {
                  this.destroy();
                  var4 = var3.createReply();
               } catch (NotEmpty var9) {
                  var4 = var3.createExceptionReply();
                  NotEmptyHelper.write(var4, var9);
               }
               break;
            case 17:
               int var6 = var2.read_ulong();
               var7 = new BindingListHolder();
               var8 = new org.omg.CosNaming.BindingIteratorHolder();
               this.list(var6, var7, var8);
               var4 = var3.createReply();
               BindingListHelper.write(var4, var7.value);
               org.omg.CosNaming.BindingIteratorHelper.write(var4, var8.value);
               break;
            default:
               throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
         }

         return var4;
      }
   }

   public String[] _ids() {
      return (String[])((String[])__ids.clone());
   }

   static {
      _methods.put("bind_any", new Integer(0));
      _methods.put("rebind_any", new Integer(1));
      _methods.put("resolve_any", new Integer(2));
      _methods.put("resolve_str_any", new Integer(3));
      _methods.put("to_string", new Integer(4));
      _methods.put("to_name", new Integer(5));
      _methods.put("to_url", new Integer(6));
      _methods.put("resolve_str", new Integer(7));
      _methods.put("bind", new Integer(8));
      _methods.put("rebind", new Integer(9));
      _methods.put("bind_context", new Integer(10));
      _methods.put("rebind_context", new Integer(11));
      _methods.put("resolve", new Integer(12));
      _methods.put("unbind", new Integer(13));
      _methods.put("new_context", new Integer(14));
      _methods.put("bind_new_context", new Integer(15));
      _methods.put("destroy", new Integer(16));
      _methods.put("list", new Integer(17));
      __ids = new String[]{"IDL:weblogic/corba/cos/naming/NamingContextAny:1.0", "IDL:omg.org/CosNaming/NamingContextExt:1.0", "IDL:omg.org/CosNaming/NamingContext:1.0"};
   }
}
