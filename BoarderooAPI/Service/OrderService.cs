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
    public async Task<ServiceResult<Order>>AddOrder(Order order)
    {
         try{
        var Collection = getOrderCollectionById(order.Id);
        var data = await Collection.GetSnapshotAsync();
         if(data.Documents.Count<1)
    {
         var ordersCollection = getOrderCollection();
            var newOrder=new Order();

            await ordersCollection.AddAsync(newOrder);
            //var u=ConvertDocumentToModel(newUser);
            //nie ma uzytnika w bazie (dodajemy)
              return new ServiceResult<Order>
        {
            Message="Uzytkownik dodany poprawnie!",
            ResultCode=200,
            Data=newOrder
        };
    }
        else return new ServiceResult<Order>
        {
            Message="Juz istnieje zamówienie o takim ID!",
            ResultCode=409
        }; //409
        }
        catch (Exception e)
        {
            return new ServiceResult<Order>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }

    public async Task<ServiceResult<Order>>GetOrder(string id)
    {
        try
        {
            var Collection=getOrderCollectionById(id);
            var data=await Collection.GetSnapshotAsync();
            if (!data[0].Exists)
        {
            //brak uzytkownika w bazie
            return new ServiceResult<Order>
        {
            Message="Brak zamówienia o takim id!",
            ResultCode=404
        };
        }
        else
        {
            var order = data[0].ConvertTo<Order>();
            //var help=ConvertDocumentToModel(user);
             return new ServiceResult<Order>
        {
            Message="Uzytkownik pobrany poprawnie!",
            ResultCode=200,
            Data=order
        };
        }

        }
        catch (Exception e)
        {
            return new ServiceResult<Order>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }

    public async Task<ServiceResult<Order>>DeleteOrder(string id)
    {
        try{
        var Collection = this.getOrderCollectionById(id);
        var data = await Collection.GetSnapshotAsync();
        if (!data[0].Exists)
        {
            return new ServiceResult<Order>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=404
        };
        }
        else
        {
            DocumentReference emailDocument=data.Documents[0].Reference;

            await emailDocument.DeleteAsync();
            return new ServiceResult<Order>
        {
            Message="Zamowienie usuniete pomyslnie!",
            ResultCode=200
        };

        }}
        catch(Exception e)
        {
                return new ServiceResult<Order>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }

    public async Task<ServiceResult<List<Order>>> GetAllOrders()
    {
         var orders = new List<Order>();

        try
        {
            var Collection = getOrderCollection();
            if(Collection!=null)
            {
                var orderList = await Collection.GetSnapshotAsync();
                foreach (var order in orderList.Documents)
                {
                    Order o = new Order();
                    o.Start=order.GetValue<System.DateTime>("Start");
                    o.End=order.GetValue<System.DateTime>("End");
                    o.Status=order.GetValue<StatusType>("Status");
                    o.Items=order.GetValue<List<Game>>("Items");
                    o.User=order.GetValue<User>("User");
                    o.Price=order.GetValue<float>("Price");

                    orders.Add(o);
                }
            }
            else
            {
                     return new ServiceResult<List<Order>>
            {
            Message="Brak zamówień!",
            ResultCode=404
            };
            }
            return new ServiceResult<List<Order>>
            {
            Message="Zamówienia pobrane pomyslnie!",
            ResultCode=200,
            Data=orders
            };
        }
        catch (Exception e)
        {
            return new ServiceResult<List<Order>>
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
    public Google.Cloud.Firestore.Query getOrderCollectionById(string id)
    {
        return _database.Collection("orders").WhereEqualTo("Id", id);
    }

 }
