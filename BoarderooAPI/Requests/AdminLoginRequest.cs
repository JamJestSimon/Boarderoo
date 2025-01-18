namespace BoarderooAPI.Requests
{
    public class AdminLoginRequest
    {
        [Required]
        public string Login { get; set; }

        [Required]
        public string Password { get; set; }
    }
}
