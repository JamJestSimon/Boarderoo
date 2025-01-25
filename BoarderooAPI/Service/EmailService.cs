using System.Diagnostics;
using MailKit;
using System.Text;
using Microsoft.Extensions.Configuration;
namespace BoarderooAPI.Service;
using MailKit.Net.Smtp;
using MimeKit;
using Newtonsoft.Json;

public class EmailService
{
private readonly string _smtpHost;
    private readonly int _smtpPort;
    private readonly string _smtpUsername;
    private readonly string _smtpPassword;

    public EmailService()
    {
        var builder=new ConfigurationBuilder().SetBasePath(Directory.GetCurrentDirectory()).AddJsonFile("appsettings.json",optional:true,reloadOnChange:true);
        IConfiguration configuration=builder.Build();
        this._smtpHost=configuration["EmailSettings:SmtpServer"];
        this._smtpPort=587;
        this._smtpUsername=configuration["EmailSettings:SmtpUsername"];
        this._smtpPassword=configuration["EmailSettings:SmtpPassword"];
    }
    

        public async Task<string> SendEmailAsync(string toEmail, string subject, string body)
    {
        string apiKey = "mlsn.f313f788b48ac589808efa1c9514211de58c6045b70d3aede1238d90cfc70e4c";
            
            // Tworzenie klienta HTTP
            using (var client = new HttpClient())
            {
                client.DefaultRequestHeaders.Add("Authorization", $"Bearer {apiKey}");

                // Adres URL API Mailersend
                string url = "https://api.mailersend.com/v1/email";

                // Dane e-maila w formacie JSON
                var emailData = new
                {
                    from = new { email = _smtpUsername, name = "BoarderooApp" },
                    to = new[] { new { email = toEmail } },
                    subject = subject,
                    text = body
                };

                // Serializowanie danych do JSON
                var jsonContent = JsonConvert.SerializeObject(emailData);

                // Tworzenie zawartości żądania
                var content = new StringContent(jsonContent, Encoding.UTF8, "application/json");

                // Wysłanie żądania POST do API Mailersend
                var response = await client.PostAsync(url, content);

                if (response.IsSuccessStatusCode)
                {
                    return "Email sent successfully.";
                }
                else
                {
                    Console.WriteLine($"Failed to send email. Status Code: {response.StatusCode}");
                    string responseContent = await response.Content.ReadAsStringAsync();
                    return $"Response: {responseContent}";
                }
            }
        }
    }



