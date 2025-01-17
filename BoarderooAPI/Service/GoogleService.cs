using Google.Apis.Auth.OAuth2;
using Google.Apis.Gmail.v1;
using Google.Apis.Gmail.v1.Data;
using Google.Apis.Services;
using Google.Apis.Util.Store;
using System;
using System.IO;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using MimeKit;
using Google;

class GoogleService
{
    static string[] Scopes = { GmailService.Scope.GmailSend };
    static string ApplicationName = "BoarderooApi";
    static string CredentialsPath = Path.Combine(Directory.GetCurrentDirectory(), "Credentials", "credentials.json");  
    static string UserEmail = "boarderooapi@peaceful-surge-448119-q1.iam.gserviceaccount.com"; // The email you want to send emails from

    public static async Task SendEmailAsync( string to, string subject, string body)
    {
        // Authenticate using service account credentials
        var credential = GoogleCredential.FromFile(CredentialsPath)
            .CreateScoped(Scopes)
            .CreateWithUser(UserEmail);

        // Create Gmail service
        var service = new GmailService(new BaseClientService.Initializer()
        {
            HttpClientInitializer = credential,
            ApplicationName = ApplicationName,
        });

        // Create email message using MimeKit
        var message = new MimeMessage();
        message.From.Add(new MailboxAddress("Nadawca",UserEmail));
        message.To.Add(new MailboxAddress("Odbiorca",to));
        message.Subject = subject;

        var bodyPart = new TextPart("plain")
        {
            Text = body
        };

        message.Body = bodyPart;

        // Encode MimeMessage to base64
        var gmailMessage = new Message
        {
            Raw = Convert.ToBase64String(Encoding.UTF8.GetBytes(message.ToString())).Replace('+', '-').Replace('/', '_').Replace("=", "")        };

        // Send the email via Gmail API
        try
{
    await service.Users.Messages.Send(gmailMessage, UserEmail).ExecuteAsync();
}
catch (GoogleApiException ex)
{
    Console.WriteLine($"Google API Error: {ex.Error.Code} - {ex.Error.Message}");
    foreach (var error in ex.Error.Errors)
    {
        Console.WriteLine($"Reason: {error.Reason}, Message: {error.Message}");
    }
    throw;
}
catch (Exception ex)
{
    Console.WriteLine($"General Error: {ex.Message}");
    throw;
}
    }
}
