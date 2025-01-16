using BoarderooAPI.Model;
using FirebaseAdmin.Auth;
using Google.Cloud.Firestore;
namespace BoarderooAPI.Service;
public class UserService
{
 private readonly FirestoreDb _database;

public UserService(FireBaseService firebaseService)
{
            _database = firebaseService.getDatabase(); 

}
public async Task<ServiceResult<User>> AddUser(User user)
    {
        try{
        var userCollection = this.getUserCollectionByEmail(user.Email);
        var data = await userCollection.GetSnapshotAsync();
        var geoPoint = new GeoPoint(user.Location.Latitude, user.Location.Longitude);
         if(data.Documents.Count<1)
    {
         var usersCollection = this.getUserCollection();
            var newUser=ConvertModeltoDocument(user);
            newUser.Location=geoPoint;
            await usersCollection.AddAsync(newUser);
            //nie ma uzytnika w bazie (dodajemy)
              return new ServiceResult<User>
        {
            Message="Uzytkownik dodany poprawnie!",
            ResultCode=200,
            Data=user
        };
    }
        else return new ServiceResult<User>
        {
            Message="Juz istnieje uzytkownik o takim emailu!",
            ResultCode=409
        }; //409
        }
        catch (Exception e)
        {
            return new ServiceResult<User>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
        
    }

    public async Task<ServiceResult<User>> UpdateUser(User user)
    {
        try{
    var emailDocuments = this.getUserCollectionByEmail(user.Email);
    var data = await emailDocuments.GetSnapshotAsync();
    if(data.Documents.Count<1)
    {
        return new ServiceResult<User>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=404
        };
    }
        if (!data[0].Exists)
        {
            return new ServiceResult<User>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=404
        };
        }
        else
        {
           
               
                Dictionary<string, object> userdict = new Dictionary<string, object>()
            {
                { "Email",user.Email},
                { "Location",user.Location },
                { "Name",user.Name },
                { "Password",user.Password },
                { "Surname",user.Surname }
            };
            
            DocumentReference emailDocument=data.Documents[0].Reference;

            await emailDocument.UpdateAsync(userdict);
            var usersCollection = getUserCollectionByEmail(user.Email);
            var updatedUser = data[0].ConvertTo<User>();

             return new ServiceResult<User>
        {
            Message="Uzytkownik zaaktualizowany poprawnie!",
            ResultCode=200,
            Data=updatedUser
        };

            
        }}
         catch(Exception e)
        {
                return new ServiceResult<User>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }


    }

    public async Task<ServiceResult<User>> DeleteUser(string email)//moze jeszcze haslo?
    {
        try{
        var usersCollection = this.getUserCollectionByEmail(email);
        var data = await usersCollection.GetSnapshotAsync();
        if(data.Documents.Count<1)
    {
        return new ServiceResult<User>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=404
        };
    }
        if (!data[0].Exists)
        {
            return new ServiceResult<User>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=404
        };
        }
        else
        {
            DocumentReference emailDocument=data.Documents[0].Reference;

            await emailDocument.DeleteAsync();
            return new ServiceResult<User>
        {
            Message="Uzytkownik usuniety pomyslnie!",
            ResultCode=200
        };

        }}
        catch(Exception e)
        {
                return new ServiceResult<User>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }

    public async Task<ServiceResult<User>> GetUserByEmail(string email)
    {
        try{
        var usersCollection = this.getUserCollectionByEmail(email);
        var data = await usersCollection.GetSnapshotAsync();
        //data=data[0];
        if(data.Documents.Count<1)
    {
        return new ServiceResult<User>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=404
        };
    }
        if (!data[0].Exists)
        {
            //brak uzytkownika w bazie
            return new ServiceResult<User>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=404
        };
        }
        else
        {
            var user = data[0].ConvertTo<UserDocument>();
            var help=ConvertDocumentToModel(user);
             return new ServiceResult<User>
        {
            Message="Uzytkownik pobrany poprawnie!",
            ResultCode=200,
            Data=help
        };
        }}
        catch (Exception e)
        {
            return new ServiceResult<User>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
        
    }

    public async Task<ServiceResult<List<User>>> GetAllUsers()
    {
           var users=new List<User>();
            // get users collection
            try{
            var usersCollection = _database.Collection("users");

            if (usersCollection != null)
            {
                // get records/rows of users table
                var userList = await usersCollection.GetSnapshotAsync();

                foreach (var user in userList.Documents)
                {
                    // create user object using the fields of the record
                    User u = new User();
                    u.Email=user.GetValue<string>("Email");
                    u.IsVerified = user.GetValue<bool>("IsVerified");
                    u.Location = user.GetValue<GeoPoint>("Location");
                    u.Name = user.GetValue<string>("Name");
                    u.Password = user.GetValue<string>("Password");
                    u.Surname = user.GetValue<string>("Surname");
                    u.Token = user.GetValue<string>("Token");
                    u.TokenCreationDate = user.GetValue<DateTime>("TokenCreationDate");
                    //u.TokenCreationDate = user.GetValue<Google.Cloud.Firestore.Timestamp>("TokenCreationDate");
                    // add to users list
                    users.Add(u);
                }
            }
            else
            {
                
                 return new ServiceResult<List<User>>
            {
            Message="Brak uzytkownikow!",
            ResultCode=404
            };
            }
            return new ServiceResult<List<User>>
            {
            Message="Uzytkownicy pobrani pomyslnie!",
            ResultCode=200,
            Data=users
            };
            }
            catch (Exception e)
            {
            return new ServiceResult<List<User>>
            {
            Message="Blad"+e.ToString(),
            ResultCode=500
            };
            }
        }

    public async Task<ServiceResult<User>> UpdateToken(string email,string token)
    {
        try{
    var emailDocuments = this.getUserCollectionByEmail(email);
    var data = await emailDocuments.GetSnapshotAsync();
    if(data.Documents.Count<1)
    {
        return new ServiceResult<User>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=404
        };
    }
    else
        {
           
                //var updatedUser=ConvertModeltoDocument(user);
                Dictionary<string, object> userdict = new Dictionary<string, object>()
            {
                { "Token",token},
                { "TokenCreationDate", Google.Cloud.Firestore.Timestamp.FromDateTime(DateTime.UtcNow) }
            };
            
            DocumentReference emailDocument=data.Documents[0].Reference;

            await emailDocument.UpdateAsync(userdict);

            await emailDocument.UpdateAsync(userdict);
            var usersCollection = this.getUserCollectionByEmail(email);
            var user = data[0].ConvertTo<User>();

            return new ServiceResult<User>
        {
            Message="Uzytkownik zaaktualizowany poprawnie!",
            ResultCode=200,
            Data=user
        };

            
        }}
        catch (Exception e)
        {
            return new ServiceResult<User>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }

    public async Task<ServiceResult<User>> UpdateVerified(string email, bool state)
    {
        try{
        var emailDocuments = this.getUserCollectionByEmail(email);
    var data = await emailDocuments.GetSnapshotAsync();
    if(data.Documents.Count<1)
    {
        return new ServiceResult<User>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=404
        };
    }
    else
        {
           
                //var updatedUser=ConvertModeltoDocument(user);
                Dictionary<string, object> userdict = new Dictionary<string, object>()
            {
                { "IsVerified",state}
            };
            
            DocumentReference emailDocument=data.Documents[0].Reference;

            await emailDocument.UpdateAsync(userdict);
            var usersCollection = this.getUserCollectionByEmail(email);
            var user = data[0].ConvertTo<User>();
            return new ServiceResult<User>
        {
            Message="wartosc Verifed dla uzytkownika zaaktualizowana poprawnie!",
            ResultCode=200,
            Data=user
    };}
    }
    catch (Exception e)
    {
        return new ServiceResult<User>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
    }
    }
     private static UserDocument ConvertModeltoDocument(User user)
    {
        return new UserDocument{
            Email=user.Email,
            IsVerified=user.IsVerified,
            Location=user.Location,
            Name=user.Name,
            Password=user.Password,
            Surname=user.Surname,
            Token=user.Token,
            TokenCreationDate=Timestamp.FromDateTime(user.TokenCreationDate)
        };
    }
    private static User ConvertDocumentToModel(UserDocument userDocument)
    {
        return new User
        {
            Email=userDocument.Email,
            IsVerified=Convert.ToBoolean(userDocument.IsVerified),
            Location=userDocument.Location,
            Name=userDocument.Name,
            Password=userDocument.Password,
            Surname=userDocument.Surname,
            Token=userDocument.Token,
            TokenCreationDate=userDocument.TokenCreationDate.ToDateTime()
        };
    }

    public Google.Cloud.Firestore.CollectionReference getUserCollection()
    {
        return _database.Collection("users");
    }
    public Google.Cloud.Firestore.Query getUserCollectionByEmail(string email)
    {
        return _database.Collection("users").WhereEqualTo("Email", email);
    }
    public Google.Cloud.Firestore.DocumentReference getUserCollectionById(string id)
    {
        return _database.Collection("users").Document(id);
    }
}