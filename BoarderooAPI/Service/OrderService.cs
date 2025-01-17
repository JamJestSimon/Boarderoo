using BoarderooAPI.Model;
using FirebaseAdmin.Auth;
using Google.Cloud.Firestore;
namespace BoarderooAPI.Service;
public class OrderService
 {

 
 private readonly FirestoreDb _database;

    public OrderService(FireBaseService firebaseService)
    {
        _database = firebaseService.getDatabase(); 
    }
    public async Task<ServiceResult<OrderDocument>>AddOrder(OrderDocument order)
    {
         try{
        var Collection = getOrderCollectionById(order.Id);
        var data = await Collection.GetSnapshotAsync();

         var ordersCollection = getOrderCollection();
            var newOrder=new OrderDocument();
            //var n=ConvertModeltoDocument(newOrder);
            await ordersCollection.AddAsync(newOrder);
            //var u=ConvertDocumentToModel(newUser);
            //nie ma uzytnika w bazie (dodajemy)
              return new ServiceResult<OrderDocument>
        {
            Message="Uzytkownik dodany poprawnie!",
            ResultCode=200,
            Data=order
        };
    
        }
        catch (Exception e)
        {
            return new ServiceResult<OrderDocument>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }

    public async Task<ServiceResult<OrderDocument>>GetOrder(string id)
    {
        try
        {
            var Collection=getOrderCollectionById(id);
            var data=await Collection.GetSnapshotAsync();
            if (!data.Exists)
        {
            //brak uzytkownika w bazie
            return new ServiceResult<OrderDocument>
        {
            Message="Brak zamówienia o takim id!",
            ResultCode=404
        };
        }
        else
        {
            var order = data.ConvertTo<OrderDocument>();
            //var help=ConvertDocumentToModel(user);
             return new ServiceResult<OrderDocument>
        {
            Message="Uzytkownik pobrany poprawnie!",
            ResultCode=200,
            Data=order
        };
        }

        }
        catch (Exception e)
        {
            return new ServiceResult<OrderDocument>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }

    public async Task<ServiceResult<OrderDocument>>DeleteOrder(string id)
    {
        try{
        var Collection = this.getOrderCollectionById(id);
        var data = await Collection.GetSnapshotAsync();
        if (!data.Exists)
        {
            return new ServiceResult<OrderDocument>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=404
        };
        }
        else
        {
            DocumentReference emailDocument=data.Reference;

            await emailDocument.DeleteAsync();
            return new ServiceResult<OrderDocument>
        {
            Message="Zamowienie usuniete pomyslnie!",
            ResultCode=200
        };

        }}
        catch(Exception e)
        {
                return new ServiceResult<OrderDocument>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }

    public async Task<ServiceResult<List<OrderDocument>>> GetAllOrders()
    {
         var orders = new List<OrderDocument>();

        try
        {
            var Collection = getOrderCollection();
            if(Collection!=null)
            {
                var orderList = await Collection.GetSnapshotAsync();
                foreach (var order in orderList.Documents)
                {
                    OrderDocument o = new OrderDocument();
                    o.Start=order.GetValue<Timestamp>("Start");
                    o.End=order.GetValue<Timestamp>("End");
                    o.Status=order.GetValue<OrderDocument.StatusType>("Status");
                    o.Items=order.GetValue<List<GameDocument>>("Items");
                    o.User=order.GetValue<UserDocument>("User");
                    o.Price=order.GetValue<float>("Price");

                    orders.Add(o);
                }
            }
            else
            {
                     return new ServiceResult<List<OrderDocument>>
            {
            Message="Brak zamówień!",
            ResultCode=404
            };
            }
            return new ServiceResult<List<OrderDocument>>
            {
            Message="Zamówienia pobrane pomyslnie!",
            ResultCode=200,
            Data=orders
            };
        }
        catch (Exception e)
        {
            return new ServiceResult<List<OrderDocument>>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }

    public Google.Cloud.Firestore.CollectionReference getOrderCollection()
    {
        return _database.Collection("orders");
    }
    public Google.Cloud.Firestore.DocumentReference getOrderCollectionById(string id)
    {
        return _database.Collection("orders").Document(id);
    }

 }
