using System.Text.Json;
using BoarderooAPI.Service;
using Newtonsoft.Json;
namespace BoarderooAPI.Service;
public class GoogleService
{
    private readonly UserService _userService;
    public GoogleService(UserService userService)
    {
        _userService=userService;
    }
     public async Task<ServiceResult<string>> GetGoogleToken(string code)
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
            { "redirect_uri", "https://boarderoo-71469.firebaseapp.com" }
        };
        var content = new FormUrlEncodedContent(values);


        using (var client = new HttpClient())
        {
            try{
            var response = await client.PostAsync("https://oauth2.googleapis.com/token", content);
            var responseString = await response.Content.ReadAsStringAsync();

            using var jsonDoc = JsonDocument.Parse(responseString);
            var root = jsonDoc.RootElement;

            // Pobierz wartości z JSON
            var accessToken = root.GetProperty("access_token").GetString();
            int expiresIn = root.GetProperty("expires_in").GetInt32();

            // client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bot", token);
            // client.DefaultRequestHeaders.Add("User-Agent", "CSharp App");

            // var response = await client.GetAsync($"https://discord.com/api/v10/users/{userId}");

             if (response.IsSuccessStatusCode)
            {
                var json = await response.Content.ReadAsStringAsync();
                string dec = System.Web.HttpUtility.UrlDecode(json);
                var result = dec.Trim().TrimStart('/').TrimEnd('/');
                
                return new ServiceResult<string>
            {
                Message="Uzytkownik zautoryzowany pomyslnie!",
                ResultCode=200,
                Data=accessToken
            };
            }
            else
            {
                var json = await response.Content.ReadAsStringAsync();
                return new ServiceResult<string>
                {
                    Message=$"Błąd: {json} - {response.ReasonPhrase}",
                    ResultCode=400
                };
            }
            }
            catch(Exception ex)
            {
                return new ServiceResult<string>
                {
                    Message=$"Błąd połączenia: {ex}",
                    ResultCode=504
                };
            }
           
        }}

        public async Task<ServiceResult<string>> GetGoogleUserInfo(string token)
    {
        using (var client = new HttpClient())
    {
        
        // Pobranie access_token
        //string accessToken = deserializedData.GetProperty("access_token").GetString();
        // Nagłówek z tokenem dostępu
        client.DefaultRequestHeaders.Add("Authorization", $"Bearer {token}");

        // Wysłanie zapytania do API Google
        var response = await client.GetAsync("https://www.googleapis.com/oauth2/v3/userinfo");

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
            // Pobierz wartości z JSON
            
            var template = new
            {
                Email=email,
                Type="Google",
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
            var jres = await response.Content.ReadAsStringAsync();
                return new ServiceResult<string>
                {
                    Message=$"Błąd: {jres} - {response.ReasonPhrase}",
                    ResultCode=400
                };
        }
        }
}}