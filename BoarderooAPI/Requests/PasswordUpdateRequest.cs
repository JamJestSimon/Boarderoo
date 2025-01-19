using System.ComponentModel.DataAnnotations;

namespace BoarderooAPI.Requests
{
    public class PasswordUpdateRequest
    {
        [Required]
        [EmailAddress]
        public string Email { get; set; }

        [Required]
        public string OldPassword { get; set; }

        [Required]
        public string NewPassword { get; set; }
    }
}
