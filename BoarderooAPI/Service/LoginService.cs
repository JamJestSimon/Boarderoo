using BoarderooAPI.Model;
using FirebaseAdmin.Auth;
using Google.Cloud.Firestore;
namespace BoarderooAPI.Service;
public class LoginService
{
    private readonly UserService _userService;

    public LoginService(UserService userService)
    {
        _userService = userService;
    }


    public async Task<ServiceResult<string>> Login(string email, string password)
    {
        try
        {
        var usersCollection = _userService.getUserCollectionByEmail(email);
        var data = await usersCollection.GetSnapshotAsync();
        if (data == null)
        {
            //brak uzytkownika w bazie
            return new ServiceResult<string>
                    {
                        Message = "Brak uzytkownika w bazie danych!",
                        ResultCode = 400
                    };
        }
        else
        {
            if (data[0].GetValue<bool>("IsVerified") == true)
            {
                if (password == data[0].GetValue<string>("Password"))
                {
                    //pomyslnie zalogowano
                    return new ServiceResult<string>
                    {
                        Message = "Uzytkownik zalogowany poprawnie!",
                        ResultCode = 200
                    };

                }
                else
                {
                    return new ServiceResult<string>
                    {
                        Message = "Niepoprawne hasło!",
                        ResultCode = 400
                    };
                }
            }
            else
            {
                // w przeciwnym przypadku
                // generujesz tokena
                var token = "token";
                var state = await _userService.UpdateToken(email, token);
                // wpisujesz to do bazy danych (token i data)
                return new ServiceResult<string>
                    {
                        Message = "tu trzeba rejestracje!",
                        ResultCode = 400
                    };
                // i wysylasz maila z linkem
                // i dajesz odpowiedz ze wyslano mail do weryfikacji       
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
}