using System.ComponentModel.DataAnnotations;

public class ResetPasswordRequest
{
    [Required]
    public string Password { get; set; }

    [Required]
    public string Token { get; set; }
}
