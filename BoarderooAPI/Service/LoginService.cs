using BoarderooAPI.Model;
using FirebaseAdmin.Auth;
using Google.Api;
using Google.Cloud.Firestore;
namespace BoarderooAPI.Service;
public class LoginService
{
    private readonly UserService _userService;
    private readonly EmailService _emailService;
    public LoginService(UserService userService,EmailService emailService)
    {
        _userService = userService;
        _emailService=emailService;
    }

    public async Task<ServiceResult<string>>Verify(string email,string password,string token)
    {
        try
        {
            return new ServiceResult<string>
                    {
                        Message = "Brak uzytkownika w bazie danych!",
                        ResultCode = 400
                    };
        
        }
        catch (Exception e)
        {
return new ServiceResult<string>
        {
            Message="Blad: "+e.ToString(),
            ResultCode=500
        };
        }
        

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
                string hash = data[0].GetValue<string>("Password");
                string password_hashed=HashService.hashfunction(password);
                if (password_hashed == hash )
                {
                    //pomyslnie zalogowano
                    return new ServiceResult<string>
                    {
                        Message = "Uzytkownik zalogowany poprawnie!",
                        ResultCode = 200,
                        Data="Ok"
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
            else if(data[0].GetValue<bool>("IsVerified") == false)
            {
                // w przeciwnym przypadku
                // generujesz tokena
                string token=Convert.ToBase64String(Guid.NewGuid().ToByteArray());
                var state = await _userService.UpdateToken(email, token);
                // wpisujesz to do bazy danych (token i data)
                var result=await _emailService.SendEmailAsync(email,"Weryfikacja Boarderoo","Witaj, twoj link aktywacyjny do Boarderoo Application to:");
                return new ServiceResult<string>
            {
                Message="Link weryfikacyjny zostal wyslany na mail!",
                ResultCode=200,
                Data=result
            
            };    
            }

        return new ServiceResult<string>
        {
            Message="Blad: ",
            ResultCode=500
        };
        }
        }
        catch (Exception e)
        {
                return new ServiceResult<string>
        {
            Message="Blad: "+e.ToString(),
            ResultCode=500
        };
        }
    }
}