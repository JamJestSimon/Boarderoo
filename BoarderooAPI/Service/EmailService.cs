using System.Diagnostics;
using MailKit;
using System.Text;
using Microsoft.Extensions.Configuration;
namespace BoarderooAPI.Service;
using MailKit.Net.Smtp;
using MimeKit;

public class EmailService
{
private readonly string _smtpHost = "smtp.mailersend.net";
    private readonly int _smtpPort = 587;
    private readonly string _smtpUsername = "MS_HUcHcp@trial-yzkq3406ypk4d796.mlsender.net"; // Wstaw swój email
    private readonly string _smtpPassword = "WRugD8XmVvhw6N16"; // Wstaw hasło aplikacji, jeśli masz 2FA

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
         try
        {
            // Tworzymy wiadomość
            var message = new MimeMessage();
            message.From.Add(new MailboxAddress("Your Name", _smtpUsername));  // Adres nadawcy
            message.To.Add(new MailboxAddress("", toEmail));  // Adres odbiorcy
            message.Subject = subject;
            message.Body = new TextPart("plain") { Text = body };  // Treść wiadomości

            // Łączenie z serwerem SMTP i wysyłanie wiadomości
            using (var client = new SmtpClient())
            {
                await client.ConnectAsync(_smtpHost, _smtpPort, MailKit.Security.SecureSocketOptions.StartTls);  // Łączenie z serwerem
                await client.AuthenticateAsync(_smtpUsername, _smtpPassword);  // Logowanie
                await client.SendAsync(message);  // Wysyłanie wiadomości
                await client.DisconnectAsync(true);  // Rozłączanie się

                return "E-mail został wysłany pomyślnie.";
            }
        }
        catch (Exception ex)
        {
            return $"Błąd podczas wysyłania e-maila: {ex.Message}";
        }
    }
}


