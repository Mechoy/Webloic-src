package weblogic.corba.cos.naming;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.ObjectHelper;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;
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
import org.omg.CosNaming.NamingContextExtPackage.URLStringHelper;
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

public class _NamingContextAnyStub extends ObjectImpl implements NamingContextAny {
   private static String[] __ids = new String[]{"IDL:weblogic/corba/cos/naming/NamingContextAny:1.0", "IDL:omg.org/CosNaming/NamingContextExt:1.0", "IDL:omg.org/CosNaming/NamingContext:1.0"};

   public void bind_any(WNameComponent[] var1, Any var2) throws NotFound, CannotProceed, InvalidName, AlreadyBound, TypeNotSupported {
      InputStream var3 = null;

      try {
         OutputStream var4 = this._request("bind_any", true);
         WNameHelper.write(var4, var1);
         var4.write_any(var2);
         var3 = this._invoke(var4);
         return;
      } catch (ApplicationException var10) {
         var3 = var10.getInputStream();
         String var5 = var10.getId();
         if (var5.equals("IDL:weblogic/corba/cos/naming/NamingContextAny/NotFound:1.0")) {
            throw NotFoundHelper.read(var3);
         }

         if (var5.equals("IDL:weblogic/corba/cos/naming/NamingContextAny/CannotProceed:1.0")) {
            throw CannotProceedHelper.read(var3);
         }

         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
            throw InvalidNameHelper.read(var3);
         }

         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/AlreadyBound:1.0")) {
            throw AlreadyBoundHelper.read(var3);
         }

         if (var5.equals("IDL:weblogic/corba/cos/naming/NamingContextAny/TypeNotSupported:1.0")) {
            throw TypeNotSupportedHelper.read(var3);
         }

         throw new MARSHAL(var5);
      } catch (RemarshalException var11) {
         this.bind_any(var1, var2);
      } finally {
         this._releaseReply(var3);
      }

   }

   public void rebind_any(WNameComponent[] var1, Any var2) throws NotFound, CannotProceed, InvalidName, TypeNotSupported {
      InputStream var3 = null;

      try {
         OutputStream var4 = this._request("rebind_any", true);
         WNameHelper.write(var4, var1);
         var4.write_any(var2);
         var3 = this._invoke(var4);
         return;
      } catch (ApplicationException var10) {
         var3 = var10.getInputStream();
         String var5 = var10.getId();
         if (var5.equals("IDL:weblogic/corba/cos/naming/NamingContextAny/NotFound:1.0")) {
            throw NotFoundHelper.read(var3);
         }

         if (var5.equals("IDL:weblogic/corba/cos/naming/NamingContextAny/CannotProceed:1.0")) {
            throw CannotProceedHelper.read(var3);
         }

         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
            throw InvalidNameHelper.read(var3);
         }

         if (var5.equals("IDL:weblogic/corba/cos/naming/NamingContextAny/TypeNotSupported:1.0")) {
            throw TypeNotSupportedHelper.read(var3);
         }

         throw new MARSHAL(var5);
      } catch (RemarshalException var11) {
         this.rebind_any(var1, var2);
      } finally {
         this._releaseReply(var3);
      }

   }

   public Any resolve_any(WNameComponent[] var1) throws NotFound, CannotProceed, InvalidName {
      InputStream var2 = null;

      Any var4;
      try {
         OutputStream var3 = this._request("resolve_any", true);
         WNameHelper.write(var3, var1);
         var2 = this._invoke(var3);
         var4 = var2.read_any();
         Any var5 = var4;
         return var5;
      } catch (ApplicationException var10) {
         var2 = var10.getInputStream();
         String var13 = var10.getId();
         if (var13.equals("IDL:weblogic/corba/cos/naming/NamingContextAny/NotFound:1.0")) {
            throw NotFoundHelper.read(var2);
         }

         if (var13.equals("IDL:weblogic/corba/cos/naming/NamingContextAny/CannotProceed:1.0")) {
            throw CannotProceedHelper.read(var2);
         }

         if (var13.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
            throw InvalidNameHelper.read(var2);
         }

         throw new MARSHAL(var13);
      } catch (RemarshalException var11) {
         var4 = this.resolve_any(var1);
      } finally {
         this._releaseReply(var2);
      }

      return var4;
   }

   public Any resolve_str_any(String var1) throws NotFound, CannotProceed, InvalidName {
      InputStream var2 = null;

      Any var4;
      try {
         OutputStream var3 = this._request("resolve_str_any", true);
         WStringNameHelper.write(var3, var1);
         var2 = this._invoke(var3);
         var4 = var2.read_any();
         Any var5 = var4;
         return var5;
      } catch (ApplicationException var10) {
         var2 = var10.getInputStream();
         String var13 = var10.getId();
         if (var13.equals("IDL:weblogic/corba/cos/naming/NamingContextAny/NotFound:1.0")) {
            throw NotFoundHelper.read(var2);
         }

         if (var13.equals("IDL:weblogic/corba/cos/naming/NamingContextAny/CannotProceed:1.0")) {
            throw CannotProceedHelper.read(var2);
         }

         if (var13.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
            throw InvalidNameHelper.read(var2);
         }

         throw new MARSHAL(var13);
      } catch (RemarshalException var11) {
         var4 = this.resolve_str_any(var1);
      } finally {
         this._releaseReply(var2);
      }

      return var4;
   }

   public String to_string(NameComponent[] var1) throws InvalidName {
      InputStream var2 = null;

      String var4;
      try {
         OutputStream var3 = this._request("to_string", true);
         NameHelper.write(var3, var1);
         var2 = this._invoke(var3);
         var4 = StringNameHelper.read(var2);
         String var5 = var4;
         return var5;
      } catch (ApplicationException var10) {
         var2 = var10.getInputStream();
         var4 = var10.getId();
         if (var4.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
            throw InvalidNameHelper.read(var2);
         }

         throw new MARSHAL(var4);
      } catch (RemarshalException var11) {
         var4 = this.to_string(var1);
      } finally {
         this._releaseReply(var2);
      }

      return var4;
   }

   public NameComponent[] to_name(String var1) throws InvalidName {
      InputStream var2 = null;

      NameComponent[] var4;
      try {
         OutputStream var3 = this._request("to_name", true);
         StringNameHelper.write(var3, var1);
         var2 = this._invoke(var3);
         var4 = NameHelper.read(var2);
         NameComponent[] var5 = var4;
         return var5;
      } catch (ApplicationException var10) {
         var2 = var10.getInputStream();
         String var13 = var10.getId();
         if (var13.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
            throw InvalidNameHelper.read(var2);
         }

         throw new MARSHAL(var13);
      } catch (RemarshalException var11) {
         var4 = this.to_name(var1);
      } finally {
         this._releaseReply(var2);
      }

      return var4;
   }

   public String to_url(String var1, String var2) throws InvalidAddress, InvalidName {
      InputStream var3 = null;

      String var5;
      try {
         OutputStream var4 = this._request("to_url", true);
         AddressHelper.write(var4, var1);
         StringNameHelper.write(var4, var2);
         var3 = this._invoke(var4);
         var5 = URLStringHelper.read(var3);
         String var6 = var5;
         return var6;
      } catch (ApplicationException var11) {
         var3 = var11.getInputStream();
         var5 = var11.getId();
         if (var5.equals("IDL:omg.org/CosNaming/NamingContextExt/InvalidAddress:1.0")) {
            throw InvalidAddressHelper.read(var3);
         }

         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
            throw InvalidNameHelper.read(var3);
         }

         throw new MARSHAL(var5);
      } catch (RemarshalException var12) {
         var5 = this.to_url(var1, var2);
      } finally {
         this._releaseReply(var3);
      }

      return var5;
   }

   public Object resolve_str(String var1) throws org.omg.CosNaming.NamingContextPackage.NotFound, org.omg.CosNaming.NamingContextPackage.CannotProceed, InvalidName {
      InputStream var2 = null;

      Object var4;
      try {
         OutputStream var3 = this._request("resolve_str", true);
         StringNameHelper.write(var3, var1);
         var2 = this._invoke(var3);
         var4 = ObjectHelper.read(var2);
         Object var5 = var4;
         return var5;
      } catch (ApplicationException var10) {
         var2 = var10.getInputStream();
         String var13 = var10.getId();
         if (var13.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0")) {
            throw org.omg.CosNaming.NamingContextPackage.NotFoundHelper.read(var2);
         }

         if (var13.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0")) {
            throw org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.read(var2);
         }

         if (var13.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
            throw InvalidNameHelper.read(var2);
         }

         throw new MARSHAL(var13);
      } catch (RemarshalException var11) {
         var4 = this.resolve_str(var1);
      } finally {
         this._releaseReply(var2);
      }

      return var4;
   }

   public void bind(NameComponent[] var1, Object var2) throws org.omg.CosNaming.NamingContextPackage.NotFound, org.omg.CosNaming.NamingContextPackage.CannotProceed, InvalidName, AlreadyBound {
      InputStream var3 = null;

      try {
         OutputStream var4 = this._request("bind", true);
         NameHelper.write(var4, var1);
         ObjectHelper.write(var4, var2);
         var3 = this._invoke(var4);
         return;
      } catch (ApplicationException var10) {
         var3 = var10.getInputStream();
         String var5 = var10.getId();
         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0")) {
            throw org.omg.CosNaming.NamingContextPackage.NotFoundHelper.read(var3);
         }

         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0")) {
            throw org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.read(var3);
         }

         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
            throw InvalidNameHelper.read(var3);
         }

         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/AlreadyBound:1.0")) {
            throw AlreadyBoundHelper.read(var3);
         }

         throw new MARSHAL(var5);
      } catch (RemarshalException var11) {
         this.bind(var1, var2);
      } finally {
         this._releaseReply(var3);
      }

   }

   public void rebind(NameComponent[] var1, Object var2) throws org.omg.CosNaming.NamingContextPackage.NotFound, org.omg.CosNaming.NamingContextPackage.CannotProceed, InvalidName {
      InputStream var3 = null;

      try {
         OutputStream var4 = this._request("rebind", true);
         NameHelper.write(var4, var1);
         ObjectHelper.write(var4, var2);
         var3 = this._invoke(var4);
         return;
      } catch (ApplicationException var10) {
         var3 = var10.getInputStream();
         String var5 = var10.getId();
         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0")) {
            throw org.omg.CosNaming.NamingContextPackage.NotFoundHelper.read(var3);
         }

         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0")) {
            throw org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.read(var3);
         }

         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
            throw InvalidNameHelper.read(var3);
         }

         throw new MARSHAL(var5);
      } catch (RemarshalException var11) {
         this.rebind(var1, var2);
      } finally {
         this._releaseReply(var3);
      }

   }

   public void bind_context(NameComponent[] var1, NamingContext var2) throws org.omg.CosNaming.NamingContextPackage.NotFound, org.omg.CosNaming.NamingContextPackage.CannotProceed, InvalidName, AlreadyBound {
      InputStream var3 = null;

      try {
         OutputStream var4 = this._request("bind_context", true);
         NameHelper.write(var4, var1);
         NamingContextHelper.write(var4, var2);
         var3 = this._invoke(var4);
         return;
      } catch (ApplicationException var10) {
         var3 = var10.getInputStream();
         String var5 = var10.getId();
         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0")) {
            throw org.omg.CosNaming.NamingContextPackage.NotFoundHelper.read(var3);
         }

         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0")) {
            throw org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.read(var3);
         }

         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
            throw InvalidNameHelper.read(var3);
         }

         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/AlreadyBound:1.0")) {
            throw AlreadyBoundHelper.read(var3);
         }

         throw new MARSHAL(var5);
      } catch (RemarshalException var11) {
         this.bind_context(var1, var2);
      } finally {
         this._releaseReply(var3);
      }

   }

   public void rebind_context(NameComponent[] var1, NamingContext var2) throws org.omg.CosNaming.NamingContextPackage.NotFound, org.omg.CosNaming.NamingContextPackage.CannotProceed, InvalidName {
      InputStream var3 = null;

      try {
         OutputStream var4 = this._request("rebind_context", true);
         NameHelper.write(var4, var1);
         NamingContextHelper.write(var4, var2);
         var3 = this._invoke(var4);
         return;
      } catch (ApplicationException var10) {
         var3 = var10.getInputStream();
         String var5 = var10.getId();
         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0")) {
            throw org.omg.CosNaming.NamingContextPackage.NotFoundHelper.read(var3);
         }

         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0")) {
            throw org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.read(var3);
         }

         if (var5.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
            throw InvalidNameHelper.read(var3);
         }

         throw new MARSHAL(var5);
      } catch (RemarshalException var11) {
         this.rebind_context(var1, var2);
      } finally {
         this._releaseReply(var3);
      }

   }

   public Object resolve(NameComponent[] var1) throws org.omg.CosNaming.NamingContextPackage.NotFound, org.omg.CosNaming.NamingContextPackage.CannotProceed, InvalidName {
      InputStream var2 = null;

      Object var4;
      try {
         OutputStream var3 = this._request("resolve", true);
         NameHelper.write(var3, var1);
         var2 = this._invoke(var3);
         var4 = ObjectHelper.read(var2);
         Object var5 = var4;
         return var5;
      } catch (ApplicationException var10) {
         var2 = var10.getInputStream();
         String var13 = var10.getId();
         if (var13.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0")) {
            throw org.omg.CosNaming.NamingContextPackage.NotFoundHelper.read(var2);
         }

         if (var13.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0")) {
            throw org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.read(var2);
         }

         if (var13.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
            throw InvalidNameHelper.read(var2);
         }

         throw new MARSHAL(var13);
      } catch (RemarshalException var11) {
         var4 = this.resolve(var1);
      } finally {
         this._releaseReply(var2);
      }

      return var4;
   }

   public void unbind(NameComponent[] var1) throws org.omg.CosNaming.NamingContextPackage.NotFound, org.omg.CosNaming.NamingContextPackage.CannotProceed, InvalidName {
      InputStream var2 = null;

      try {
         OutputStream var3 = this._request("unbind", true);
         NameHelper.write(var3, var1);
         var2 = this._invoke(var3);
         return;
      } catch (ApplicationException var9) {
         var2 = var9.getInputStream();
         String var4 = var9.getId();
         if (var4.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0")) {
            throw org.omg.CosNaming.NamingContextPackage.NotFoundHelper.read(var2);
         }

         if (var4.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0")) {
            throw org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.read(var2);
         }

         if (var4.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
            throw InvalidNameHelper.read(var2);
         }

         throw new MARSHAL(var4);
      } catch (RemarshalException var10) {
         this.unbind(var1);
      } finally {
         this._releaseReply(var2);
      }

   }

   public NamingContext new_context() {
      InputStream var1 = null;

      NamingContext var3;
      try {
         OutputStream var2 = this._request("new_context", true);
         var1 = this._invoke(var2);
         var3 = NamingContextHelper.read(var1);
         NamingContext var4 = var3;
         return var4;
      } catch (ApplicationException var9) {
         var1 = var9.getInputStream();
         String var12 = var9.getId();
         throw new MARSHAL(var12);
      } catch (RemarshalException var10) {
         var3 = this.new_context();
      } finally {
         this._releaseReply(var1);
      }

      return var3;
   }

   public NamingContext bind_new_context(NameComponent[] var1) throws org.omg.CosNaming.NamingContextPackage.NotFound, AlreadyBound, org.omg.CosNaming.NamingContextPackage.CannotProceed, InvalidName {
      InputStream var2 = null;

      NamingContext var4;
      try {
         OutputStream var3 = this._request("bind_new_context", true);
         NameHelper.write(var3, var1);
         var2 = this._invoke(var3);
         var4 = NamingContextHelper.read(var2);
         NamingContext var5 = var4;
         return var5;
      } catch (ApplicationException var10) {
         var2 = var10.getInputStream();
         String var13 = var10.getId();
         if (var13.equals("IDL:omg.org/CosNaming/NamingContext/NotFound:1.0")) {
            throw org.omg.CosNaming.NamingContextPackage.NotFoundHelper.read(var2);
         }

         if (var13.equals("IDL:omg.org/CosNaming/NamingContext/AlreadyBound:1.0")) {
            throw AlreadyBoundHelper.read(var2);
         }

         if (var13.equals("IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0")) {
            throw org.omg.CosNaming.NamingContextPackage.CannotProceedHelper.read(var2);
         }

         if (var13.equals("IDL:omg.org/CosNaming/NamingContext/InvalidName:1.0")) {
            throw InvalidNameHelper.read(var2);
         }

         throw new MARSHAL(var13);
      } catch (RemarshalException var11) {
         var4 = this.bind_new_context(var1);
      } finally {
         this._releaseReply(var2);
      }

      return var4;
   }

   public void destroy() throws NotEmpty {
      InputStream var1 = null;

      try {
         OutputStream var2 = this._request("destroy", true);
         var1 = this._invoke(var2);
         return;
      } catch (ApplicationException var8) {
         var1 = var8.getInputStream();
         String var3 = var8.getId();
         if (var3.equals("IDL:omg.org/CosNaming/NamingContext/NotEmpty:1.0")) {
            throw NotEmptyHelper.read(var1);
         }

         throw new MARSHAL(var3);
      } catch (RemarshalException var9) {
         this.destroy();
      } finally {
         this._releaseReply(var1);
      }

   }

   public void list(int var1, BindingListHolder var2, org.omg.CosNaming.BindingIteratorHolder var3) {
      InputStream var4 = null;

      try {
         OutputStream var5 = this._request("list", true);
         var5.write_ulong(var1);
         var4 = this._invoke(var5);
         var2.value = BindingListHelper.read(var4);
         var3.value = org.omg.CosNaming.BindingIteratorHelper.read(var4);
         return;
      } catch (ApplicationException var11) {
         var4 = var11.getInputStream();
         String var6 = var11.getId();
         throw new MARSHAL(var6);
      } catch (RemarshalException var12) {
         this.list(var1, var2, var3);
      } finally {
         this._releaseReply(var4);
      }

   }

   public String[] _ids() {
      return (String[])((String[])__ids.clone());
   }

   private void readObject(ObjectInputStream var1) throws IOException {
      String var2 = var1.readUTF();
      java.lang.Object var3 = null;
      java.lang.Object var4 = null;
      ORB var5 = ORB.init((String[])var3, (Properties)var4);

      try {
         Object var6 = var5.string_to_object(var2);
         Delegate var7 = ((ObjectImpl)var6)._get_delegate();
         this._set_delegate(var7);
      } finally {
         var5.destroy();
      }

   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      java.lang.Object var2 = null;
      java.lang.Object var3 = null;
      ORB var4 = ORB.init((String[])var2, (Properties)var3);

      try {
         String var5 = var4.object_to_string(this);
         var1.writeUTF(var5);
      } finally {
         var4.destroy();
      }

   }
}
