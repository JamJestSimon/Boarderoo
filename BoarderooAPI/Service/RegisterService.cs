using BoarderooAPI.Model;
using FirebaseAdmin.Auth;
using Google.Cloud.Firestore;
namespace BoarderooAPI.Service;

using Microsoft.AspNetCore.Mvc.Routing;
using Microsoft.Extensions.Configuration;
public class RegisterService
{
private readonly UserService _userService;
private readonly EmailService _emailService;
private readonly FirestoreDb _database;

public RegisterService(UserService userService,EmailService emailService,FireBaseService firebaseService)
{
            _userService = userService;
            _emailService=emailService;
            _database=firebaseService.getDatabase();
}


    // public async Task<ServiceResult<UserDocument>> Register(UserDocument user)
    // {
    //     //sprawdz czy jest ziomek w bazie danyhc
    //     try
    //     {
    //          var usersCollection = _userService.getUserCollectionByEmail(user.Email);
    //     var data = await usersCollection.GetSnapshotAsync();
    //     if (data!=null)
    //     {
    //         var u=_userService.AddUser(user);
    //         return new ServiceResult<UserDocument>
    //     {
    //         Message="Uzytkownik juz istnieje!",
    //         ResultCode=200
    //     };
    //     }
    //     else 
    //     {
    //         return new ServiceResult<UserDocument>
    //     {
    //         Message="Uzytkownik juz istnieje!",
    //         ResultCode=400
    //     };
    //     }
    //     }
    //     catch(Exception e)
    //     {
    //         return new ServiceResult<UserDocument>
    //     {
    //         Message="Blad"+e.ToString(),
    //         ResultCode=500
    //     };
    //     }
    //     //sprawdzamy czy uzytkownik istnieje w bazie danych
    //     //generujesz token uzytkownika z data
    //     //dodajesz do bazy danych
    //     //wysylasz maila z linkiem
    //    // return 0;
    // }

    public async Task<ServiceResult<string>> Register(string email,string password)
    {

        var usersCollection = _userService.getUserCollectionByEmail(email);
        var data = await usersCollection.GetSnapshotAsync();
        if (data.Documents.Count<1)
        {
            //brak uzytkownika w bazie
            //generujesz token uzytkownika z data
            UserDocument u=new UserDocument();
            u.Email=email;
            u.Password=password;
           // u.Token=""; //  tutaj trzeba bedzie generowac token
            //u.TokenCreationDate=;
            byte[] token=Guid.NewGuid().ToByteArray();
            string utf8Token = System.Text.Encoding.UTF8.GetString(token);
            await _userService.AddUser(u);
            await _userService.UpdateToken(email,utf8Token); //aktualizujemy token
            //await EmailService.SendEmail();//wysylamy email z kodem
            //dodajesz do bazy danych
            //wysylasz maila z linkiem
            


    // Wysłanie e-maila
    //await emailService.SendEmailAsync("recipient@example.com", "Temat wiadomości", "<h1>Treść wiadomości</h1>");
            string url=$"https://boarderoo-71469.firebaseapp.com/?code={token}";
            string message=$"Witaj, twoj link aktywacyjny do Boarderoo Application to: {url}";
            //var result=await _emailService.SendEmailAsync(email,$"Weryfikacja Boarderoo",message);
            var result=message;
            return new ServiceResult<string>
        {
            Message="Zarejestrowano pomyslnie!",
            ResultCode=200,
            Data=result
           
        };
        }else
        {
          //uzytkownik juz istnieje w bazie danych
           return new ServiceResult<string>
        {
            Message="Uzytkownik o takim email juz istnieje!",
            ResultCode=400
        };
        }
    }

    public async Task<ServiceResult<string>>Verify(string token)
    {
        try{
        var usersCollection = _database.Collection("users").WhereEqualTo("Token", token);
        var data = await usersCollection.GetSnapshotAsync();
        if (data.Documents.Count<1)
        {
             return new ServiceResult<string>
                    {
                        Message = "Brak uzytkownika w bazie danych!",
                        ResultCode = 400
                    };
        }
        else
        {
            var user=data[0].ConvertTo<UserDocument>();
            var time=user.TokenCreationDate;
            //Timestamp protoTimestamp = Timestamp.FromDateTime(DateTime.UtcNow.AddHours(-24));
            var now = Timestamp.FromDateTime(DateTime.UtcNow.AddHours(-24));

        if (now>time)
        {
            _userService.UpdateVerified(user.Email,true);
             return new ServiceResult<string>
        {
            Message="Uzytkownik zweryfikowany poprawnie!",
            ResultCode=200,
            Data=user.Email
        };
        }
        else
        {
            return new ServiceResult<string>
        {
            Message="Token wygasl!",
            ResultCode=200,
            Data=time.ToString()
        };
        }
            
        }
       
        }
        catch(Exception e)
        {
            return new ServiceResult<string>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
    }

    
}