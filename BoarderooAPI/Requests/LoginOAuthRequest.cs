using System.ComponentModel.DataAnnotations;

public class LoginOAuthRequest
{
    [Required]
    [EmailAddress]
    public string Email { get; set; }

    [Required]
    public string Type { get; set; }
}
