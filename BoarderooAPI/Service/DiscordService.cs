
using System.Net.Http.Headers;
using System.Runtime.InteropServices;
using BoarderooAPI.Service;
//using Discord;

public class DiscordService
{
    private readonly UserService _userService;
  //  private DiscordSocketClient _client;
    public DiscordService(UserService userService)
    {
        _userService=userService;
    }

    public async Task<ServiceResult<string>> GetDiscordToken(string code)
    {
        string userId="1303087880503296182";
        string secret_key="lM6v9PP9dbEGNJR6z1o6FiW_iP3qD7EO";

         var values = new Dictionary<string, string>
        {
            { "client_id", userId },
            { "client_secret", secret_key },
            { "grant_type", "authorization_code"  },
            { "code", code },
            { "redirect_uri", "https://boarderoo-71469.firebaseapp.com/" }
        };

        using (var client = new HttpClient())
        {
             var content = new FormUrlEncodedContent(values);

            var response = await client.PostAsync("https://discord.com/api/v10/oauth2/token", content);
            var responseString = await response.Content.ReadAsStringAsync();
            // client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bot", token);
            // client.DefaultRequestHeaders.Add("User-Agent", "CSharp App");

            // var response = await client.GetAsync($"https://discord.com/api/v10/users/{userId}");

            if (response.IsSuccessStatusCode)
            {
                var json = await response.Content.ReadAsStringAsync();
                
                return new ServiceResult<string>
            {
                Message="Uzytkownik zautoryzowany pomyslnie!",
                ResultCode=200,
                Data=json
            };
            }
            else
            {
                return new ServiceResult<string>
                {
                    Message=$"Błąd: {response.StatusCode} - {response.ReasonPhrase}",
                    ResultCode=400
                };
            }
        }
    }
    
        public async Task<ServiceResult<string>> GetDiscordUserInfo(string token)
    {
        using (var client = new HttpClient())
    {
        // Nagłówek z tokenem dostępu
            client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", token);
        // Wysłanie zapytania do API Google
        var response = await client.GetAsync("https://discord.com/api/v10/users/@me");

        // Sprawdzenie, czy odpowiedź była pomyślna
        if (response.IsSuccessStatusCode)
        {
            // Odczytanie odpowiedzi jako string
            var responseString = await response.Content.ReadAsStringAsync();
            
                
                return new ServiceResult<string>
            {
                Message="Uzytkownik zautoryzowany pomyslnie!",
                ResultCode=200,
                Data=responseString
            };
        }
        else
        {
            Console.WriteLine("Błąd podczas pobierania danych użytkownika: " + response.StatusCode);
            var json = await response.Content.ReadAsStringAsync();
                return new ServiceResult<string>
                {
                    Message=$"Błąd: {json} - {response.ReasonPhrase}",
                    ResultCode=400
                };
        }
        }
}
    
    
    }