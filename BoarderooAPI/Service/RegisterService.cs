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


    public int Register(string mail, string password)
    {
        //sprawdzamy czy uzytkownik istnieje w bazie danych
        //generujesz token uzytkownika z data
        //dodajesz do bazy danych
        //wysylasz maila z linkiem
        return 0;
    }

    public async Task<int> Register(string email,string password, FireBaseService fireBaseService)
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
            return 200;
        }else
        {
          //uzytkownik juz istnieje w bazie danych
          return 400;//
        }
    }
}