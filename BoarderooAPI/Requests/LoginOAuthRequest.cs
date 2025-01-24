using System.ComponentModel.DataAnnotations;

public class LoginOAuthRequest
{
    [Required]
    public string Token { get; set; }
}
