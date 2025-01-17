using BoarderooAPI.Model;
using FirebaseAdmin.Auth;
using Google.Cloud.Firestore;
namespace BoarderooAPI.Service;
public class RegisterService
{
private readonly UserService _userService;

public RegisterService(UserService userService)
{
            _userService = userService; 
}


    public async Task<ServiceResult<string>> Register(string mail, string password)
    {
        try
        {
return new ServiceResult<string>
        {
            Message="Zarejestrowano pomyslnie!",
            ResultCode=200
        };
        }
        catch(Exception e)
        {
            return new ServiceResult<string>
        {
            Message="Blad"+e.ToString(),
            ResultCode=500
        };
        }
        //sprawdzamy czy uzytkownik istnieje w bazie danych
        //generujesz token uzytkownika z data
        //dodajesz do bazy danych
        //wysylasz maila z linkiem
       // return 0;
    }

    public async Task<ServiceResult<string>> Register(string email,string password, FireBaseService fireBaseService)
    {

        var usersCollection = _userService.getUserCollectionByEmail(email);
        var data = await usersCollection.GetSnapshotAsync();
        if (data==null)
        {
            //brak uzytkownika w bazie
            //generujesz token uzytkownika z data
            User u=new User();
            u.Email=email;
            u.Password=password;
           // u.Token=""; //  tutaj trzeba bedzie generowac token
            //u.TokenCreationDate=;
            string token="";
            await _userService.AddUser(u);
            await _userService.UpdateToken(email,token); //aktualizujemy token
            //await EmailService.SendEmail();//wysylamy email z kodem
            //dodajesz do bazy danych
            //wysylasz maila z linkiem
            return new ServiceResult<string>
        {
            Message="Zarejestrowano pomyslnie!",
            ResultCode=200
        };
        }else
        {
          //uzytkownik juz istnieje w bazie danych
           return new ServiceResult<string>
        {
            Message="Brak uzytkownika o takim emailu!",
            ResultCode=400
        };
        }
    }
}