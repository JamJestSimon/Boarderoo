namespace BoarderooAPI.Service;

public class LoginService
{
    public LoginService()
    {
        
    }

    public async Task<int> Login(string email,string password, FireBaseService fireBaseService)
    {

        var usersCollection = fireBaseService.getUserCollectionByEmail(email);
        var data = await usersCollection.GetSnapshotAsync();
        if (data==null)
        {
            //brak uzytkownika w bazie
        }else
        {
            if (data[0].GetValue<bool>("IsVerified")==true)
            {
                if(password==data[0].GetValue<string>("Password"))
                {
                    //pomyslnie zalogowano
                    return 200;
                }
                else
                {
                    return 400;//zle haslo
                }
            }
            else
            {
                // w przeciwnym przypadku
                // generujesz tokena
                var token="token";
                var state=  await fireBaseService.UpdateToken(email,token);
                // wpisujesz to do bazy danych (token i data)

                // i wysylasz maila z linkem
                // i dajesz odpowiedz ze wyslano mail do weryfikacji       
            }
            

        }
        return 0;
    }
}