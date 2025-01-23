
using System.Net.Http.Headers;
using System.Text.Json;
using BoarderooAPI.Service;
using Newtonsoft.Json;
//using Discord;
namespace BoarderooAPI.Service;
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
                string decodedString = System.Web.HttpUtility.UrlDecode(code);
        var builder=new ConfigurationBuilder().SetBasePath(Directory.GetCurrentDirectory()).AddJsonFile("appsettings.json",optional:true,reloadOnChange:true);
        IConfiguration configuration=builder.Build();
        string userId=configuration["GoogleSettings:userID"];
        string secret_key=configuration["GoogleSettings:secret_key"];

         var values = new Dictionary<string, string>
        {
            { "client_id", userId },
            { "client_secret", secret_key },
            { "grant_type", "authorization_code"  },
            { "code", decodedString },
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
                using var jsonDoc = JsonDocument.Parse(responseString);
            var root = jsonDoc.RootElement;

            // Pobierz wartości z JSON
            var accessToken = root.GetProperty("access_token").GetString();
                
                return new ServiceResult<string>
            {
                Message="Uzytkownik zautoryzowany pomyslnie!",
                ResultCode=200,
                Data=accessToken
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
            using var jsonDoc = JsonDocument.Parse(responseString);
            var root = jsonDoc.RootElement;
            var email = root.GetProperty("email").GetString();
            var userCollection = _userService.getUserCollectionByEmail(email);
            var data = await userCollection.GetSnapshotAsync();
            bool exists=false;
            if(data.Documents.Count>0)
            {
                exists=true;
            }

            var template = new
            {
                Email=root.GetProperty("email").GetString(),
                Type="Discord",
                Exists=exists

            };
            string result=JsonConvert.SerializeObject(template,Formatting.Indented);
                
                return new ServiceResult<string>
            {
                Message="Uzytkownik zautoryzowany pomyslnie!",
                ResultCode=200,
                Data=result
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