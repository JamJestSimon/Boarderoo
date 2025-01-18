using BoarderooAPI.Model;
using FirebaseAdmin.Auth;
using Google.Cloud.Firestore;
namespace BoarderooAPI.Service;
using Microsoft.Extensions.Configuration;
public class RegisterService
{
private readonly UserService _userService;
private readonly EmailService _emailService;

public RegisterService(UserService userService,EmailService emailService)
{
            _userService = userService;
            _emailService=emailService;
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
            string token=Convert.ToBase64String(Guid.NewGuid().ToByteArray());
            await _userService.AddUser(u);
            await _userService.UpdateToken(email,token); //aktualizujemy token
            //await EmailService.SendEmail();//wysylamy email z kodem
            //dodajesz do bazy danych
            //wysylasz maila z linkiem
            


    // Wysłanie e-maila
    //await emailService.SendEmailAsync("recipient@example.com", "Temat wiadomości", "<h1>Treść wiadomości</h1>");

           var result=await _emailService.SendEmailAsync(email,"Weryfikacja Boarderoo","Witaj, twoj link aktywacyjny do Boarderoo Application to:");
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

    public async Task<ServiceResult<string>>Verify(string email,string token)
    {
        return new ServiceResult<string>
                    {
                        Message = "Brak uzytkownika w bazie danych!",
                        ResultCode = 400
                    };
    }
}