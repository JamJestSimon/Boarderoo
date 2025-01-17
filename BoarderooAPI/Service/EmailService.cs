using System.Diagnostics;
using MailKit;
using System.Text;
using Microsoft.Extensions.Configuration;
namespace BoarderooAPI.Service;
using MailKit.Net.Smtp;
using MimeKit;

public class EmailService
{

    public EmailService()
    {
        
    }
   //private readonly string api="AIzaSyCkoO3i2vSW1VNnaTJgb_iHVYVK8FpIniQ";
    private readonly string _smtpHost = "smtp.office365.com";
    private readonly int _smtpPort = 587;
    private readonly string _smtpUsername = "boarderooapp@outlook.com"; // Wstaw swój email
    private readonly string _smtpPassword = "`TW4V;w8h+3*"; // Wstaw hasło aplikacji, jeśli masz 2FA

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


