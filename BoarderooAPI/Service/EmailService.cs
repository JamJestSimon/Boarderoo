using System.Net;
using System.Net.Mail;
using Microsoft.Extensions.Configuration;
namespace BoarderooAPI.Service;

public class EmailService
{
    private readonly string host;
    private readonly int port;
    private readonly string username;
    private readonly string password;
    
public EmailService(IConfiguration configuration)
    {
        host=configuration["MailSettings:Host"];
        password=configuration["MailSettings:Password"];
    }
public async Task SendEmailAsync(string toEmail, string subject, string body)
    {
        try
        {
           using (var smtpClient = new SmtpClient("smtp.gmail.com",587))
            {
                smtpClient.Credentials = new NetworkCredential(host, password);
                smtpClient.EnableSsl = true;

                var mailMessage = new MailMessage
                {
                    From = new MailAddress(host),
                    Subject = subject,
                    Body = body
                };

                mailMessage.To.Add(toEmail);

                await smtpClient.SendMailAsync(mailMessage);
                Console.WriteLine("E-mail został wysłany pomyślnie.");
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Błąd podczas wysyłania e-maila: {ex.Message}");
            throw;
        }
    }
}

