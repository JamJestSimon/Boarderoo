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

    public async Task<ServiceResult<string>> Register(UserDocument user)
    {

        var usersCollection = _userService.getUserCollectionByEmail(user.Email);
        var data = await usersCollection.GetSnapshotAsync();
        if (data.Documents.Count<1)
        {
            string token = Convert.ToBase64String(Guid.NewGuid().ToByteArray())
            .Replace("+", "-")
            .Replace("/", "_")
            .TrimEnd('=');
            user.IsVerified=false;
            if (user.Authorization!="discord"&&user.Authorization!="google")
            {
                user.Authorization="local";
                await _userService.AddUser(user);
            await _userService.UpdateToken(user.Email,token); //aktualizujemy token
            string url=$"https://boarderoo-71469.firebaseapp.com/weryfikacja?code={token}";
            string message=$"Witaj, twoj link aktywacyjny do Boarderoo Application to: {url}";
            var result=await _emailService.SendEmailAsync(user.Email,$"Weryfikacja Boarderoo",message);
            //var result=message;
            return new ServiceResult<string>
        {
            Message="Zarejestrowano pomyslnie!",
            ResultCode=200,
            Data=result
           
        };
            }
            await _userService.AddUser(user);
             return new ServiceResult<string>
        {
            Message="Zarejestrowano pomyslnie!",
            ResultCode=200,
            Data=user.Email
           
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
            var now = DateTime.UtcNow.AddHours(-24);

        if (now<time)
        {
            await _userService.UpdateVerified(user.Email,true);
            await _userService.UpdateToken(user.Email, ""); //usuwanie tokena

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
            ResultCode=204,
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