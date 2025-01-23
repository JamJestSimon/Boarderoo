using BoarderooAPI.Service;

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
        string userId="928336702407-bdifeaptq727tsor03bcbaqkvunbg7h1.apps.googleusercontent.com";
        string secret_key="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";

         var values = new Dictionary<string, string>
        {
            { "client_id", userId },
            { "client_secret", secret_key },
            { "grant_type", "authorization_code"  },
            { "code", decodedString },
            { "redirect_uri", "https://boarderoo-71469.firebaseapp.com" }
        };

        using (var client = new HttpClient())
        {
             var content = new FormUrlEncodedContent(values);

            var response = await client.PostAsync("https://oauth2.googleapis.com/token", content);
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
                var json = await response.Content.ReadAsStringAsync();
                return new ServiceResult<string>
                {
                    Message=$"Błąd: {json} - {response.ReasonPhrase}",
                    ResultCode=400
                };
            }
        }}

        public async Task<ServiceResult<string>> GetGoogleUserInfo(string token)
    {
        using (var client = new HttpClient())
    {
        // Nagłówek z tokenem dostępu
        client.DefaultRequestHeaders.Add("Authorization", $"Bearer {token}");

        // Wysłanie zapytania do API Google
        var response = await client.GetAsync("https://www.googleapis.com/oauth2/v3/userinfo");

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
}}