using BoarderooAPI.Model;
using BoarderooAPI.Requests;
using FirebaseAdmin.Auth;
using Google.Cloud.Firestore;
using System.Xml.Linq;
namespace BoarderooAPI.Service;
public class UserService
{
 private readonly FirestoreDb _database;
private readonly EmailService _emailService;
public UserService(FireBaseService firebaseService,EmailService emailService)
{
            _database = firebaseService.getDatabase(); 
            _emailService=emailService;

}
public async Task<ServiceResult<UserDocument>> AddUser(UserDocument user)
    {
        try{
        var userCollection = this.getUserCollectionByEmail(user.Email);
        var data = await userCollection.GetSnapshotAsync();
       
         if(data.Documents.Count<1)
    {
         var usersCollection = this.getUserCollection();
            //var newUser=ConvertModeltoDocument(user);
            //newUser.Location=geoPoint;
            user.Password=HashService.hashfunction(user.Password);
            await usersCollection.AddAsync(user);
            //var u=ConvertDocumentToModel(newUser);
            //nie ma uzytnika w bazie (dodajemy)
              return new ServiceResult<UserDocument>
        {
            Message="Uzytkownik dodany poprawnie!",
            ResultCode=200,
            Data=user
        };
    }
        else return new ServiceResult<UserDocument>
        {
            Message="Juz istnieje uzytkownik o takim emailu!",
            ResultCode=409
        }; //409
        }
        catch (Exception e)
        {
            return new ServiceResult<UserDocument>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
        
    }
     public async Task<ServiceResult<string>> ResetPassword(string password,string token)
    {
        //mailem 
        try
        {
        var usersCollection = _database.Collection("users").WhereEqualTo("Token", token);
        var data = await usersCollection.GetSnapshotAsync();
        if (data.Documents.Count<1)
        {
             return new ServiceResult<string>
                    {
                        Message = "Brak uzytkownika w bazie danych!",
                        ResultCode = 400
                    };
        }else
        {
            var user=data[0].ConvertTo<UserDocument>();
            var time=user.TokenCreationDate;
            //Timestamp protoTimestamp = Timestamp.FromDateTime(DateTime.UtcNow.AddHours(-24));
            var now = DateTime.UtcNow.AddHours(-2);
            if (now<time)
        {
           // await (user.Email,true);//change password
           LoginRequest req=new LoginRequest();
           req.Email=user.Email;
           req.Password=password;
            await ChangeUserPassword(req);
            //await UpdateToken(user.Email, "");//usuwanie tokena
            await DeleteToken(user.Email);

             return new ServiceResult<string>
        {
            Message="Haslo zostalo zmienione!",
            ResultCode=200,
            Data=user.Email
        };
        }
         else
        {
            return new ServiceResult<string>
        {
            Message="Token wygasl!",
            ResultCode=204,
            Data=time.ToString()
        };
        }

        }
       
        }
        catch (Exception e)
        {
            return new ServiceResult<string>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }

    public async Task<ServiceResult<string>> ResetPasswordValidate(string mail)
    {
        //mailem 
        try
        {
             var usersCollection = getUserCollectionByEmail(mail);
        var data = await usersCollection.GetSnapshotAsync();
        if (data.Documents.Count<1)
        {
            return new ServiceResult<string>
        {
            Message="Uzytkownik o takim emailu nie istnieje!",
            ResultCode=400
        };
        }else
        {
            string token = Convert.ToBase64String(Guid.NewGuid().ToByteArray())
            .Replace("+", "-")
            .Replace("/", "_")
            .TrimEnd('=');
            await UpdateToken(mail, token);
            string url=$"https://boarderoo-71469.firebaseapp.com/resethasla?code={token}";
            string message=$"Witaj, twoj link aktywacyjny do Boarderoo Application to: {url}";
            var result=await _emailService.SendEmailAsync(mail,$"Weryfikacja Boarderoo",message);
            //var result=message;
            return new ServiceResult<string>
        {
            Message="Wyslano token do zmiany hasla!",
            ResultCode=200,
            Data=token
           
        };
        }
       
        }
        catch (Exception e)
        {
            return new ServiceResult<string>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }
    public async Task<ServiceResult<string>> ChangeUserPassword(LoginRequest request)
    {
        try
        {
            var emailDocuments = this.getUserCollectionByEmail(request.Email);
            var data = await emailDocuments.GetSnapshotAsync();
            if (data.Documents.Count < 1)
            {
                return new ServiceResult<string>
                {
                    Message = "Brak uzytkownika o takim emailu!",
                    ResultCode = 404
                };
            }
            if (!data[0].Exists)
            {
                return new ServiceResult<string>
                {
                    Message = "Brak uzytkownika o takim emailu!",
                    ResultCode = 404
                };
            }
            else
            {
  
                Dictionary<string, object> userdict = new Dictionary<string, object>()
                {
                    { "Password", HashService.hashfunction(request.Password) },
                };

                DocumentReference emailDocument = data.Documents[0].Reference;

                await emailDocument.UpdateAsync(userdict);

                return new ServiceResult<string>
                {
                    Message = "Uzytkownik zaktualizowany poprawnie!",
                    ResultCode = 200,
                    Data = "Ok"
                };
            }
        }
        catch (Exception e)
        {
            return new ServiceResult<string>
            {
                Message = "Blad" + e.ToString(),
                ResultCode = 500
            };
        }
    }

    public async Task<ServiceResult<string>> UpdateUserPassword(PasswordUpdateRequest request)
    {
        try
        {
            var emailDocuments = this.getUserCollectionByEmail(request.Email);
            var data = await emailDocuments.GetSnapshotAsync();
            if (data.Documents.Count < 1)
            {
                return new ServiceResult<string>
                {
                    Message = "Brak uzytkownika o takim emailu!",
                    ResultCode = 404
                };
            }
            if (!data[0].Exists)
            {
                return new ServiceResult<string>
                {
                    Message = "Brak uzytkownika o takim emailu!",
                    ResultCode = 404
                };
            }
            else
            {
                if(data[0].GetValue<string>("Password") != HashService.hashfunction(request.OldPassword))
                {
                    return new ServiceResult<string>
                    {
                        Message = "Incorrect password",
                        ResultCode = 400
                    };
                }
                Dictionary<string, object> userdict = new Dictionary<string, object>()
                {
                    { "Password", HashService.hashfunction(request.NewPassword) },
                };

                DocumentReference emailDocument = data.Documents[0].Reference;

                await emailDocument.UpdateAsync(userdict);

                return new ServiceResult<string>
                {
                    Message = "Uzytkownik zaktualizowany poprawnie!",
                    ResultCode = 200,
                    Data = "Ok"
                };
            }
        }
        catch (Exception e)
        {
            return new ServiceResult<string>
            {
                Message = "Blad" + e.ToString(),
                ResultCode = 500
            };
        }
    }

    public async Task<ServiceResult<UserDocument>> UpdateUser(string email, string? name, string? surname, string? address)
    {
        try{
    var emailDocuments = this.getUserCollectionByEmail(email);
    var data = await emailDocuments.GetSnapshotAsync();
    if(data.Documents.Count<1)
    {
        return new ServiceResult<UserDocument>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=404
        };
    }
        if (!data[0].Exists)
        {
            return new ServiceResult<UserDocument>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=404
        };
        }
        else
        {

                Dictionary<string, object> userdict = new Dictionary<string, object>()
            {
                { "Address", address == null ? data[0].GetValue<string>("Address") : address },
                { "Name", name == null ? data[0].GetValue<string>("Name") : name },
                { "Surname", surname == null ? data[0].GetValue<string>("Surname") : surname }
            };
            
            DocumentReference emailDocument=data.Documents[0].Reference;

            await emailDocument.UpdateAsync(userdict);
            var usersCollection = getUserCollectionByEmail(email);
            var updatedUser = data[0].ConvertTo<UserDocument>();

             return new ServiceResult<UserDocument>
        {
            Message="Uzytkownik zaktualizowany poprawnie!",
            ResultCode=200,
            Data=updatedUser
        };

            
        }}
         catch(Exception e)
        {
                return new ServiceResult<UserDocument>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }


    }

    public async Task<ServiceResult<UserDocument>> DeleteUser(string email)//moze jeszcze haslo?
    {
        try{
        var usersCollection = this.getUserCollectionByEmail(email);
        var data = await usersCollection.GetSnapshotAsync();
        if(data.Documents.Count<1)
    {
        return new ServiceResult<UserDocument>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=404
        };
    }
        if (!data[0].Exists)
        {
            return new ServiceResult<UserDocument>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=404
        };
        }
        else
        {
            DocumentReference emailDocument=data.Documents[0].Reference;

            await emailDocument.DeleteAsync();
            return new ServiceResult<UserDocument>
        {
            Message="Uzytkownik usuniety pomyslnie!",
            ResultCode=200
        };

        }}
        catch(Exception e)
        {
                return new ServiceResult<UserDocument>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }

    public async Task<ServiceResult<UserDocument>> GetUserByEmail(string email)
    {
        try{
        var usersCollection = this.getUserCollectionByEmail(email);
        var data = await usersCollection.GetSnapshotAsync();
        //data=data[0];
        if(data.Documents.Count<1)
    {
        return new ServiceResult<UserDocument>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=404
        };
    }
        if (!data[0].Exists)
        {
            //brak uzytkownika w bazie
            return new ServiceResult<UserDocument>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=404
        };
        }
        else
        {
            var user = data[0].ConvertTo<UserDocument>();
            //var help=ConvertDocumentToModel(user);
            user.Id=data[0].Id;
             return new ServiceResult<UserDocument>
        {
            Message="Uzytkownik pobrany poprawnie!",
            ResultCode=200,
            Data=user
        };
        }}
        catch (Exception e)
        {
            return new ServiceResult<UserDocument>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
        
    }

    public async Task<ServiceResult<List<UserDocument>>> GetAllUsers()
    {
           var users=new List<UserDocument>();
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
                    UserDocument u = new UserDocument();
                    u.Id=user.Id;
                    u.Email=user.GetValue<string>("Email");
                    u.IsVerified = user.GetValue<bool>("IsVerified");
                    u.Address = user.GetValue<string>("Address");
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
                
                 return new ServiceResult<List<UserDocument>>
            {
            Message="Brak uzytkownikow!",
            ResultCode=404
            };
            }
            return new ServiceResult<List<UserDocument>>
            {
            Message="Uzytkownicy pobrani pomyslnie!",
            ResultCode=200,
            Data=users
            };
            }
            catch (Exception e)
            {
            return new ServiceResult<List<UserDocument>>
            {
            Message="Blad"+e.ToString(),
            ResultCode=500
            };
            }
        }
    public async Task<ServiceResult<UserDocument>> DeleteToken(string email)
    {
        try{
    var emailDocuments = getUserCollectionByEmail(email);
    var data = await emailDocuments.GetSnapshotAsync();
    if(data.Documents.Count<1)
    {
        return new ServiceResult<UserDocument>
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
                { "Token",""},
                { "TokenCreationDate", DateTime.UtcNow.AddDays(-10) }
            };
            
            DocumentReference emailDocument=data.Documents[0].Reference;

            await emailDocument.UpdateAsync(userdict);

            //await emailDocument.UpdateAsync(userdict);
            var usersCollection = getUserCollectionByEmail(email);
            var user = data[0].ConvertTo<UserDocument>();

            return new ServiceResult<UserDocument>
        {
            Message="Uzytkownik zaaktualizowany poprawnie!",
            ResultCode=200,
            Data=user
        };

            
        }}
        catch (Exception e)
        {
            return new ServiceResult<UserDocument>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }
    public async Task<ServiceResult<UserDocument>> UpdateToken(string email,string token)
    {
        try{
    var emailDocuments = getUserCollectionByEmail(email);
    var data = await emailDocuments.GetSnapshotAsync();
    if(data.Documents.Count<1)
    {
        return new ServiceResult<UserDocument>
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
                { "TokenCreationDate", DateTime.UtcNow }
            };
            
            DocumentReference emailDocument=data.Documents[0].Reference;

            await emailDocument.UpdateAsync(userdict);

            //await emailDocument.UpdateAsync(userdict);
            var usersCollection = getUserCollectionByEmail(email);
            var user = data[0].ConvertTo<UserDocument>();

            return new ServiceResult<UserDocument>
        {
            Message="Uzytkownik zaaktualizowany poprawnie!",
            ResultCode=200,
            Data=user
        };

            
        }}
        catch (Exception e)
        {
            return new ServiceResult<UserDocument>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }

    public async Task<ServiceResult<UserDocument>> UpdateVerified(string email, bool state)
    {
        try{
        var emailDocuments = this.getUserCollectionByEmail(email);
    var data = await emailDocuments.GetSnapshotAsync();
    if(data.Documents.Count<1)
    {
        return new ServiceResult<UserDocument>
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
            var user = data[0].ConvertTo<UserDocument>();
            return new ServiceResult<UserDocument>
        {
            Message="wartosc Verifed dla uzytkownika zaaktualizowana poprawnie!",
            ResultCode=200,
            Data=user
    };}
    }
    catch (Exception e)
    {
        return new ServiceResult<UserDocument>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
    }
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